/* $Id$ */
package org.firebirdsql.vulcan;

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
	// NAME:        test_CursorForUpdate (was test_S0263306)
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
	public void test_CursorForUpdate() throws SQLException {
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

	
	//
	// Description - this test demonstrates that Vulcan is appending an extra
	// null to the end of error message
	//
	public void _test_NullValueOnErrorString() throws SQLException {
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