/* $Id$ */
//---------------------------------------------------------------------*
//
// Name:    TestsDefects.java
// Author:  sasgsf
// Purpose: Contain regression tests that correspond to SAS defects.
//          The current list includes:
//
//             S0251143
//             S0255310
//             S0263291
//             S0263306
//             S0267155
//             S0265442
//
// History:
//      Date     Description
//    --------- ------------------------------------------------------
//    28SEP2004 Initial creation. 
//---------------------------------------------------------------------*

package org.firebirdsql.phase2;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.firebirdsql.nist.BaseTab;
import org.firebirdsql.nist.NistTestBase;

public class TestDefects extends NistTestBase {
	private Connection        conn;  // Connection object.
	private Statement         stmt;  // Statement object.
	private ResultSet         rs;    // Result set.
	private ResultSetMetaData rsmd;  // Result set metadata.


	protected void setUp() throws Exception {
		super.setUp();
		conn = getConnectionViaDriverManager();
		stmt = conn.createStatement();
	}
	
	
	protected void tearDown() throws Exception {
		stmt.close();
		conn.close();
		super.tearDown();
	}

	
	public TestDefects(String arg0) {
		super(arg0);
	}
	
	
	//-----------------------------------------------------------------*
	// NAME:        test_S0251143
	//
	// DESCRIPTION: Vulcan runtime error creating duplicate index
	//-----------------------------------------------------------------*
	//	
	public void test_S0251143() throws SQLException {
		int rowCount;        // Row count.
		int errorCode;       // Error code.
		
		rowCount = stmt.executeUpdate("CREATE TABLE TABLEEXISTS " +
					"(COL CHAR(10));");
		
		rowCount = stmt.executeUpdate("INSERT INTO TABLEEXISTS " +
					"VALUES('Hello');");
		assertEquals(1, rowCount);
		
		rowCount = stmt.executeUpdate("CREATE INDEX INDEXEXISTS " +
					"ON TABLEEXISTS(COL);");
		
		rowCount = stmt.executeUpdate("CREATE VIEW VIEWEXISTS " +
					"AS SELECT * FROM TABLEEXISTS;");
		
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("CREATE INDEX INDEXEXISTS " +
						"ON TABLEEXISTS(COL);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351) {
				fail("Should return attempt to store duplicate value " +
					 "(visible to active transactions) in unique " +
					 "index 'RDB$INDEX_5'");
			}
		}
	}
		
	
	//-----------------------------------------------------------------*
	// NAME:        test_S0255310
	//
	// DESCRIPTION: SUB_TYPE TEXT is not currently supported.
	//-----------------------------------------------------------------*
	//	
	public void test_S0255310() throws SQLException {
		int    rowCount;       // Row count.
		int    columnCount;    // Column count.
		String columnTypeName; // Column name type.
		Blob   blobData;       // Blob data.
		long   blobLength;     // Blob length.
		
		stmt.executeUpdate("CREATE TABLE S0255310( " +
			"TEST_NAME CHAR(30), " +
			"ROW_ID SMALLINT, " +
			"B BLOB SUB_TYPE TEXT);");
		
		rs = stmt.executeQuery("SELECT * FROM S0255310;");
		
		//
		// Obtain the necessary table metadata.
		//
		rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();
		assertEquals(3, columnCount);
		
		columnTypeName = rsmd.getColumnTypeName(3);
		assertEquals("BLOB SUB_TYPE 1", columnTypeName);
		
		rowCount = stmt.executeUpdate("INSERT INTO S0255310 " +
				"VALUES('test_S0255310', 1, 'BLOB SUBTYPE TEXT data...');");
		assertEquals(1, rowCount);
		
		rs = stmt.executeQuery("SELECT * FROM S0255310;");
		//
		// Expected output:
		//
		//        COUNT 
		// ============ 
		//
		//            1 
		//
		rs.next();
		assertEquals("test_S0255310                 ", rs.getString(1));
		assertEquals(1, rs.getInt(2));
		blobData = rs.getBlob(3);
		blobLength = blobData.length();
		assertEquals(25, blobLength);
	}

	
	//-----------------------------------------------------------------*
	// NAME:        test_S0263291
	//
	// DESCRIPTION: All columns are reported as nullable.
	//-----------------------------------------------------------------*
	//	
	public void test_S0263291() throws SQLException {
		int    errorCode;      // Error code.
		int    rowCount;       // Row count.
		int    columnCount;    // Column count.
		String columnTypeName; // Column name type.
		int    isNull;         // Is NULL flag.
		
		stmt.executeUpdate("CREATE TABLE S0263291( " +
			"ROW_ID SMALLINT, " +
			"COL CHAR(10) NOT NULL);");

		rs = stmt.executeQuery("SELECT * FROM S0263291;");
		
		//
		// Obtain the necessary table metadata.
		//
		rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();
		assertEquals(2, columnCount);
		
		//
		// Column ROW_ID is nullable.
		//
		isNull = rsmd.isNullable(1);
		assertEquals(ResultSetMetaData.columnNullable, isNull);
		
		//
		// Column COL is NOT nullable.
		//
		isNull = rsmd.isNullable(2);
		assertEquals(ResultSetMetaData.columnNoNulls, isNull);
		
		columnTypeName = rsmd.getColumnTypeName(2);
		assertEquals("CHAR", columnTypeName);
		
		rowCount = stmt.executeUpdate("INSERT INTO S0263291 " +
					"VALUES(1, 'One       ');");
		assertEquals(1, rowCount);
		
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO S0263291 " +
						"VALUES(2, NULL);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544347)
			{
				fail("Should return validation error for column COL, value '*** null ***'.");	
			}	
		}
		
		rs = stmt.executeQuery("SELECT * FROM S0263291;");
		//
		// Expected output:
		//
		//  ROW_ID COL
		// ======= ==========
		//       1 One
		//
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals("One       ", rs.getString(2));
		
		rowCount = 1;
		while (rs.next())
		{
			rowCount++;
		}
		assertEquals(1, rowCount);
	}
	
	
	//-----------------------------------------------------------------*
	// NAME:        test_S0263306
	//
	// DESCRIPTION: Vulcan current or cursor operations not working
	//
	// NOTES:       Having difficulty reproducing this problem since 
	//              these operations are not currently implemented in
	//              Firebird 1.5 or Vulcan:
	//
	//              ResultSet.moveToInsertRow(),
	//              ResultSet.deleteRow(),
	//              ResultSet.updateInt(), and 
	//              ResultSet.HOLD_CURSORS_OVER_COMMIT
	//
	//-----------------------------------------------------------------*
	//	
	public void test_S0263306() throws SQLException {
		int       errorCode;   // Error code.
		int       rowCount;    // Row count.
		Statement cursor_stmt; // Cursor statement.
		String    cursorName;  // Cursor name.
		ResultSet rs2;         // Result set.
		
		rowCount = stmt.executeUpdate("CREATE TABLE S0263306 " +
					"(COL1 INTEGER, COL2 CHAR(20), COL3 INTEGER, " +
					"COL4 CHAR(20), COL5 INTEGER, COL6 CHAR(20));");
		
		rowCount = stmt.executeUpdate("INSERT INTO S0263306 VALUES " +
					"(1, 'Column 2 data - 1', " +
					"1, 'Column 4 data - 1'," +
					"1, 'Column 6 data - 1');");
		assertEquals(1, rowCount);
		
		rowCount = stmt.executeUpdate("INSERT INTO S0263306 VALUES " +
				"(2, 'Column 2 data - 2', " +
				"2, 'Column 4 data - 2'," +
				"2, 'Column 6 data - 2');");
		assertEquals(1, rowCount);

		// conn.setAutoCommit(false);
		
		cursor_stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										   ResultSet.CONCUR_UPDATABLE);
		//								   ResultSet.HOLD_CURSORS_OVER_COMMIT);
		cursor_stmt.setCursorName("Cursor_1");
		
		rs = cursor_stmt.executeQuery("SELECT * FROM S0263306 " +
				"WHERE COL1 = 2 FOR UPDATE;");
		// rs.next();
		
		// rs.moveToInsertRow();
		// rs.updateInt(1, 3);
		
		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		
		rs.beforeFirst();
		// rs.deleteRow();
		// rs.next();
		// rs.updateInt(3,3);
		// rs.updateRow();
		
		// rowCount = cursor_stmt.executeUpdate

		
		rowCount = cursor_stmt.executeUpdate("DELETE FROM S0263306 " +
					"WHERE COL1 = 2;");
		
		// conn.commit();
		
		rs2 = stmt.executeQuery("SELECT * FROM S0263306" +
				" WHERE COL1 = 2;");
		rowCount = 0;
		while (rs2.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		
		// conn.rollback();
		
		rs2 = stmt.executeQuery("SELECT * FROM S0263306" +
				" WHERE COL1 = 2;");
		rowCount = 0;
		while (rs2.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		
		// conn.rollback();
		
		// rs.next();
		// rowCount = 0;
		// while (rs.next()) {
		// 	rowCount++;
		// }
		// assertEquals(1, rowCount);
		// rs.deleteRow();
		
		// stmt.setCursorName("Cursor_1");
		cursorName = rs.getCursorName();
	}	

	
	//-----------------------------------------------------------------*
	// NAME:        test_S0267155
	//
	// DESCRIPTION: Incorrect offsets for wide character columns. 
	//-----------------------------------------------------------------*
	//	
	public void test_S0267155() throws SQLException {
		int    rowCount;       // Row count.
		int    columnCount;    // Column count.
		int    displaySize;    // Column display size.
		int    columnType;     // Column type.
		String columnTypeName; // Column name type.
		
		rowCount = stmt.executeUpdate("CREATE TABLE FOO " +
					"(UNICODE_COL CHAR(10) CHARACTER SET UNICODE_FSS, " +
					"REGULAR_COL CHAR(10));");
		
		rowCount = stmt.executeUpdate("INSERT INTO FOO VALUES('Unicode', " +
					"'Regular');");
		assertEquals(1, rowCount);

		rs = stmt.executeQuery("SELECT * FROM FOO;");
		rs.next();
		assertEquals("Unicode                       ", rs.getString(1));
		assertEquals("Regular   ", rs.getString(2));
		
		//
		// Obtain the necessary table metadata.
		//
		rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();
		assertEquals(2, columnCount);
		
		//
		// Obtain the column type name.
		//
		columnTypeName = rsmd.getColumnTypeName(1);
		assertEquals("CHAR", columnTypeName);
		
		columnTypeName = rsmd.getColumnTypeName(2);
		assertEquals("CHAR", columnTypeName);
		
		//
		// Obtain the column display size.
		//
		displaySize = rsmd.getColumnDisplaySize(1);
		//
		// TODO: Either the displaySize should be 30 or the problem
		//       is not reproducing.
		//
		assertEquals(10, displaySize);
		
		displaySize = rsmd.getColumnDisplaySize(2);
		assertEquals(10, displaySize);
		
		//
		// Obtain the column type.
		//
		columnType = rsmd.getColumnType(1);
		assertEquals(1, columnType);
		
		columnType = rsmd.getColumnType(2);
		assertEquals(1, columnType);
	}
	
	
	//-----------------------------------------------------------------*
	// NAME:        test_S0265442
	//
	// DESCRIPTION: Crash when creating a second procedure with the same
	//              name.
	//-----------------------------------------------------------------*
	//	
	public void test_S0265442() throws SQLException {
		int    errorCode;      // Error code.
		int    rowCount;       // Row count.
		
		rowCount = stmt.executeUpdate("CREATE PROCEDURE P1 RETURNS " +
					"(A INTEGER) AS BEGIN A = 4; END;");
		
		/** 
		 * TODO: The second attempt to create procedure P1 results in 
		 *       a Microsoft Visual C++ Runtime Library error when run
		 *       with Vulcan.  The error message text is:
		 * 
		 *       Runtime Error!
		 *       Program C:\Program Files\Java\j2re1.4.1\javaw.exe
		 *       abnormal program termination
		 *
		 *       Firebird 1.5 generates the appropriate error condition.
		 */
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("CREATE PROCEDURE P1 RETURNS " +
						"(A INTEGER) AS BEGIN A = 4; END;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351) {
				fail("Should return Procedure P1 already exists.");
			}
		}
		
		rowCount = stmt.executeUpdate("DROP PROCEDURE P1;");
	}	
	
	//
	// Description - this test demonstrates that Vulcan is appending an extra
	// null to the end of error message
	//
	public void test_NullValueOnErrorString() throws SQLException {
		int errorCode;

		BaseTab.setupBaseTab(stmt);
		errorCode = 0;
		try {
			rs = stmt
					.executeQuery("SELECT COL2/COL1+COL3 FROM VTABLE WHERE COL4=3;");
			fail();
			// PASS:0122 If ERROR Number not Divisible by Zero?
		} catch (SQLException sqle) {
			assertEquals(
					"GDS Exception. 335544321. arithmetic exception, numeric overflow, or string truncation",
					sqle.getMessage());
		}

	}

}