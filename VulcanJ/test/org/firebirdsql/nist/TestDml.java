/* $Id$ */
//---------------------------------------------------------------------*
//
// Name: TestDmljava
// Author: sasgsf
// Purpose: Regression tests for NIST ISQL Data Manipulation (DML)
//          tests.
// History:
//      Date Description
//    --------- ------------------------------------------------------
//    09AUG2004 Initial creation.
//---------------------------------------------------------------------*
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TestDml extends NistTestBase {
	private Connection conn; // Connection object.
	private Statement stmt; // Statement object.
	private ResultSet rs; // Result set.

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

	public TestDml(String arg0) {
		super(arg0);
	}

	//-----------------------------------------------------------------*
	// METHOD: testDml_001
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml001.
	//
	// ORIGIN: NIST file sql/dml001.sql
	//	
	public void testDml_001() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		// TEST:0001 SELECT with ORDER BY DESC!
		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "WHERE PNUM='P2' ORDER BY EMPNUM DESC;");
		//
		// Expected output:
		//
		// EMPNUM HOURS
		// ====== ============
		//
		// E4 20
		// E3 20
		// E2 80
		// E1 20
		//
		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0001 If 4 rows selected and last EMPNUM = 'E1'?

		// END TEST >>> 0001 <<< END TEST
		// *********************************************

		// TEST:0002 SELECT with ORDER BY integer ASC!
		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "WHERE PNUM='P2' ORDER BY 2 ASC;");
		//
		// Expected output:
		//
		// EMPNUM HOURS
		// ====== ============
		//
		// E1 20
		// E3 20
		// E4 20
		// E2 80
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0002 If 4 rows selected and last HOURS = 80?

		// END TEST >>> 0002 <<< END TEST
		// *********************************************

		// TEST:0003 SELECT with ORDER BY DESC integer, named column!
		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "WHERE PNUM = 'P2' ORDER BY 2 DESC,EMPNUM DESC;");
		//
		// Expected output:
		//
		// EMPNUM HOURS
		// ====== ============
		//
		// E2 80
		// E4 20
		// E3 20
		// E1 20
		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0003 If 4 rows selected and last EMPNUM = 'E1'?

		// END TEST >>> 0003 <<< END TEST
		// *********************************************

		// TEST:0004 SELECT with UNION, ORDER BY integer DESC!
		rs = stmt.executeQuery("SELECT WORKS.EMPNUM FROM WORKS "
				+ "WHERE WORKS.PNUM = 'P2' UNION "
				+ "SELECT STAFF.EMPNUM FROM STAFF "
				+ "WHERE STAFF.GRADE=13 ORDER BY 1 DESC;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E5
		// E4
		// E3
		// E2
		// E1
		rs.next();
		assertEquals("E5 ", rs.getString(1));

		rs.next();
		assertEquals("E4 ", rs.getString(1));

		rs.next();
		assertEquals("E3 ", rs.getString(1));

		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rowCount = 5;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(5, rowCount);
		// PASS:0004 If 5 rows selected and last EMPNUM = 'E1'?

		// END TEST >>> 0004 <<< END TEST
		// *********************************************

		// TEST:0005 SELECT with UNION ALL!
		rs = stmt.executeQuery("SELECT WORKS.EMPNUM FROM WORKS "
				+ "WHERE WORKS.PNUM = 'P2' UNION ALL "
				+ "SELECT STAFF.EMPNUM FROM STAFF WHERE STAFF.GRADE = 13;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		// E2
		// E3
		// E4
		// E3
		// E5
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rs.next();
		assertEquals("E3 ", rs.getString(1));

		rs.next();
		assertEquals("E4 ", rs.getString(1));

		rs.next();
		assertEquals("E3 ", rs.getString(1));

		rs.next();
		assertEquals("E5 ", rs.getString(1));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0005 If 6 rows selected?

		// END TEST >>> 0005 <<< END TEST
		// *********************************************

		// TEST:0158 SELECT with UNION and NOT EXISTS subquery!
		rs = stmt.executeQuery("SELECT EMPNAME,PNUM,HOURS FROM STAFF,WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM UNION "
				+ "SELECT EMPNAME,PNUM,HOURS FROM STAFF,WORKS "
				+ "WHERE NOT EXISTS (SELECT HOURS FROM WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM);");
		//
		// Expected output:
		//
		// EMPNAME PNUM HOURS
		// ==================== ====== ============
		// 
		// Alice P1 40
		// Alice P2 20
		// Alice P3 80
		// Alice P4 20
		// Alice P5 12
		// Alice P6 12
		// Betty P1 40
		// Betty P2 80
		// Carmen P2 20
		// Don P2 20
		// Don P4 40
		// Don P5 80
		// Ed P1 40
		// Ed P2 20
		// Ed P2 80
		// Ed P3 80
		// Ed P4 20
		// Ed P4 40
		// Ed P5 12
		// Ed P5 80
		// Ed P6 12
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("Alice               ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("Alice               ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("Alice               ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("Alice               ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("Alice               ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("Carmen              ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rowCount = 21;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(21, rowCount);
		// PASS:0158 If 21 rows selected?

		// END TEST >>> 0158 <<< END TEST
		// *********************************************

		// TEST:0159 SELECT with 2 UNIONs, ORDER BY 2 integers!
		rs = stmt.executeQuery("SELECT PNUM,EMPNUM,HOURS FROM WORKS "
				+ "WHERE HOURS=80 UNION SELECT PNUM,EMPNUM,HOURS "
				+ "FROM WORKS WHERE HOURS=40 UNION "
				+ "SELECT PNUM,EMPNUM,HOURS FROM WORKS "
				+ "WHERE HOURS=20 ORDER BY 3,1;");
		//
		// Expected output:
		//
		// PNUM EMPNUM HOURS
		// ====== ====== ============
		//
		// P2 E1 20
		// P2 E3 20
		// P2 E4 20
		// P4 E1 20
		// P1 E1 40
		// P1 E2 40
		// P4 E4 40
		// P2 E2 80
		// P3 E1 80
		// P5 E4 80
		//
		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E3 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("E2 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("P3 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rowCount = 10;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(10, rowCount);
		// PASS:0159 If 10 rows selected?

		// END TEST >>> 0159 <<< END TEST
		// *********************************************
		// TEST:0160 SELECT with parenthesized UNION, UNION ALL!

		// Firebird doesn't support the parenthesized UNION, so
		// we'll skip this test.
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT PNUM,EMPNUM,HOURS FROM WORKS "
					+ "WHERE HOURS=12 UNION ALL (SELECT PNUM,EMPNUM,HOURS "
					+ "FROM WORKS UNION SELECT PNUM,EMPNUM,HOURS FROM WORKS "
					+ "WHERE HOURS=80) ORDER BY 2,1;");
			rowCount = 0;
			while (rs.next())
				rowCount++;
			assertEquals(14, rowCount);
			// PASS:0160 If 14 rows selected?
		}
		// END TEST >>> 0160 <<< END TEST
	}

	//-----------------------------------------------------------------*
	// METHOD: testDml_004
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml004.
	//
	// ORIGIN: NIST file sql/dml004.sql
	//	
	public void testDml_004() throws SQLException {
		int rowCount; // Row count.

		//
		// Create and populate the test tables.
		//
		BaseTab.setupBaseTab(stmt);

		// MODULE DML004

		// SQL Test Suite, V6.0, Interactive SQL, dml004.sql
		// 59-byte ID
		// TEd Version #

		conn.setAutoCommit(false);

		// TEST:0008 SQLCODE 100:SELECT on empty table !
		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "WHERE PNUM = 'P8' ORDER BY EMPNUM DESC;");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0008 If 0 rows selected, SQLCODE = 100, end of data?

		// END TEST >>> 0008 <<< END TEST
		// ****************************************************************

		// TEST:0009 SELECT NULL value!

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS VALUES('E9','P9',NULL);");
		assertEquals(1, rowCount);
		// PASS:0009 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM FROM WORKS WHERE HOURS IS NULL;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E9
		//
		rs.next();
		assertEquals("E9 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0009 If EMPNUM = 'E9'?

		rs = stmt.executeQuery("SELECT EMPNUM, HOURS FROM WORKS "
				+ "WHERE PNUM = 'P9' ORDER BY EMPNUM DESC;");
		//
		// Expected output:
		//
		// EMPNUM HOURS
		// ====== ============
		//
		// E9 <null>
		//
		rs.next();
		assertEquals("E9 ", rs.getString(1));
		assertEquals(0, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0009 If EMPNUM = 'E9' and HOURS is NULL?

		// restore
		conn.rollback();

		// END TEST >>> 0009 <<< END TEST
		// ******************************************************************

		// NO_TEST:0161 FETCH NULL value without indicator, SQLCODE < 0!

		// Testing Indicators

		// **********************************************************

		// NO_TEST:0162 FETCH NULL value with indicator syntax!

		// Testing indicators

		// ****************************************************************

		// NO_TEST:0010 FETCH truncated CHAR column with indicator!

		// Testing indicators
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_005
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml005.
	//
	// ORIGIN: NIST file sql/dml005.sql
	//	
	public void testDml_005() throws SQLException {
		int rowCount; /* Row count. */

		// TEST:0011 FIPS sizing - DECIMAL (15)!
		// FIPS sizing TEST

		// setup
		stmt.executeUpdate("CREATE TABLE LONGINT (LONG_INT DECIMAL(15));");

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO LONGINT "
				+ "VALUES(123456789012345.);");
		assertEquals(1, rowCount);
		// PASS:0011 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT LONG_INT, LONG_INT /1000000, "
				+ "LONG_INT - 123456789000000. FROM LONGINT;");
		//
		// Expected output:
		//
		//              LONG_INT
		// ===================== ===================== =====================
		//
		//       123456789012345 123456789 12345
		//
		rs.next();
		assertEquals(123456789012345L, rs.getLong(1));
		assertEquals(123456789, rs.getInt(2));
		assertEquals(12345, rs.getInt(3));
		// PASS:0011 If values are (123456789012345, 123456789, 12345), but?
		// PASS:0011 Second value may be between 123456788 and 123456790?

		// END TEST >>> 0011 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_008
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml008.
	//
	// ORIGIN: NIST file sql/dml008.sql
	//	
	public void testDml_008() throws SQLException {
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		//
		// Turn off auto commit for this portion of the test.
		//
		conn.setAutoCommit(false);

		// TEST:0016 SELECT ALL syntax!
		rs = stmt.executeQuery("SELECT ALL EMPNUM FROM WORKS "
				+ "WHERE HOURS = 12;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		// E1
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0016 If 2 rows are selected and both EMPNUMs are 'E1'?

		// END TEST >>> 0016 <<< END TEST
		// *************************************************************

		// TEST:0164 SELECT:default is ALL, not DISTINCT!
		rs = stmt.executeQuery("SELECT EMPNUM FROM WORKS "
				+ "WHERE HOURS = 12;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		// E1
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0164 If 2 rows are selected and both EMPNUMs are 'E1'?

		// END TEST >>> 0164 <<< END TEST
		// ************************************************************

		// TEST:0017 SELECT:checks DISTINCT!
		rs = stmt.executeQuery("SELECT DISTINCT EMPNUM FROM WORKS "
				+ "WHERE HOURS = 12;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0017 If 1 row is selected and EMPNUM = 'E1'?

		// END TEST >>> 0017 <<< END TEST
		// ***********************************************************

		// TEST:0018 SQLCODE = 100, SELECT with no data!
		rs = stmt.executeQuery("SELECT EMPNUM,PNUM FROM WORKS "
				+ "WHERE EMPNUM = 'E16';");
		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0018 If 0 rows selected, SQLCODE = 100, end of data?

		// END TEST >>> 0018 <<< END TEST
		// ***********************************************************

		// TEST:0019 SQLCODE = 0, SELECT with data!
		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "WHERE EMPNUM = 'E1' AND PNUM = 'P4';");
		//
		// Expected output:
		//
		// EMPNUM HOURS
		// ====== ============
		//
		// E1 20
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0019 If HOURS = 20 ?

		// END TEST >>> 0019 <<< END TEST
		// **********************************************************

		// TEST:0020 SELECT NULL value !

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E18','P18',NULL);");
		assertEquals(1, rowCount);
		// PASS:0020 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "WHERE EMPNUM='E18' AND PNUM='P18';");
		//
		// Expected output:
		//
		// EMPNUM HOURS
		// ====== ============
		//
		// E18 <null>
		//
		rs.next();
		assertEquals("E18", rs.getString(1));
		assertEquals(0, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0020 If EMPNUM = 'E18' and HOURS is NULL?

		// restore
		conn.rollback();

		// END TEST >>> 0020 <<< END TEST
		// **********************************************************

		// NO_TEST:0021 SELECT CHAR(m) column into shorter var, get indic = m!

		// Testing indicators

		// ***********************************************************

		// NO_TEST:0165 Truncate CHAR column SELECTed into shorter var!

		// Testing host identifiers
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_009
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml009.
	//
	// ORIGIN: NIST file sql/dml009.sql
	//	
	public void testDml_009() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);

		conn.setAutoCommit(false);

		// TEST:0022 INSERT(column list) VALUES(literals and NULL)!
		rowCount = stmt.executeUpdate("INSERT INTO WORKS(PNUM,EMPNUM,HOURS) "
				+ "VALUES ('P22','E22',NULL);");
		assertEquals(1, rowCount);
		// PASS:0022 If 1 row inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,PNUM FROM WORKS "
				+ "WHERE HOURS IS NULL;");
		//
		// Expected output:
		//
		//
		// EMPNUM PNUM
		// ====== ======
		//
		// E22 P22
		//
		rs.next();
		assertEquals("E22", rs.getString(1));
		assertEquals("P22", rs.getString(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0022 If EMPNUM = 'E22'?

		// restore
		conn.rollback();

		// END TEST >>> 0022 <<< END TEST
		// **************************************************************

		// TEST:0023 DEC precision >= col.def.: ERROR if left-truncate!

		// setup
		stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");

		conn.commit();

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S(EMPNUM,GRADE,CITY) "
				+ "VALUES('E23',2323.4,'China');");
		assertEquals(1, rowCount);
		// PASS:0023 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0023 If count = 1?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "VALUES('E23',23234,'China');");
		assertEquals(1, rowCount);
		// PASS:0023 If 1 row inserted or ?
		// PASS:0023 insert fails due to precision of 23234?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            2
		//
		rs.next();
		assertEquals(2, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0023 If count = 1 or 2 (depending on previous insertion)?

		// restore
		conn.rollback();

		// END TEST >>> 0023 <<< END TEST
		// ***************************************************************

		// TEST:0024 INSERT:<query spec.> is empty: SQLCODE = 100!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "SELECT EMPNUM,GRADE,CITY FROM STAFF " + "WHERE GRADE > 13;");
		assertEquals(0, rowCount);
		// PASS:0024 If 0 rows selected, SQLCODE = 100, end of data?

		// restore
		conn.rollback();

		// END TEST >>> 0024 <<< END TEST
		// *************************************************************

		// TEST:0025 INSERT:<query spec.> is not empty!
		rowCount = stmt.executeUpdate("DELETE FROM TEMP_S;");

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S(EMPNUM,GRADE,CITY) "
				+ "SELECT EMPNUM,GRADE,CITY FROM STAFF WHERE GRADE > 12;");
		assertEquals(2, rowCount);
		// PASS:0025 If 2 rows are inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            2
		//
		rs.next();
		assertEquals(2, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0025 If count = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0025 <<< END TEST
		// *************************************************************

		// TEST:0026 INSERT into view with check option and unique violation!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0026 If count = 5?

		// setup
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO TEMP_SS "
					+ "SELECT EMPNUM,GRADE,CITY FROM STAFF3 "
					+ "WHERE GRADE = 10;");
			fail();
			// PASS:0026 If ERROR, view check constraint, 0 rows inserted OR ?
			// PASS:0026 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException excep) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0026 If count = 5?

		// restore
		conn.rollback();

		// END TEST >>> 0026 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_010
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml010.
	//
	// ORIGIN: NIST file sql/dml010.sql
	//	
	public void testDml_010() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		conn.setAutoCommit(false);

		// TEST:0027 INSERT short string in long col -- space padding !

		// setup
		stmt
				.executeUpdate("CREATE TABLE TMP (T1 CHAR (10), T2 DECIMAL(2), T3 CHAR (10));");
		conn.commit();
		rowCount = stmt.executeUpdate("INSERT INTO TMP (T1, T2, T3) "
				+ "VALUES ( 'xxxx',23,'xxxx');");
		assertEquals(1, rowCount);
		// PASS:0027 If 1 row inserted?

		rs = stmt.executeQuery("SELECT * FROM TMP "
				+ "WHERE T2 = 23 AND T3 = 'xxxx      ';");
		//
		// Expected output:
		//
		// T1 T2 T3
		// ========== ============ ==========
		//
		// xxxx 23 xxxx
		//
		rs.next();
		assertEquals("xxxx      ", rs.getString(1));
		assertEquals(23, rs.getInt(2));
		assertEquals("xxxx      ", rs.getString(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0027 If T1 = 'xxxx ' ?

		// restore
		conn.rollback();

		// END TEST >>> 0027 <<< END TEST
		// *************************************************************

		// TEST:0028 Insert String that fits Exactly in Column!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TMP (T1, T2, T3) "
				+ "VALUES ('xxxxxxxxxx', 23,'xxxxxxxxxx');");
		assertEquals(1, rowCount);
		// PASS:0028 If 1 row inserted?

		rs = stmt.executeQuery("SELECT * FROM TMP WHERE T2 = 23;");
		//
		// Expected output:
		//
		//
		// T1 T2 T3
		// ========== ============ ==========
		//
		// xxxxxxxxxx 23 xxxxxxxxxx
		//
		rs.next();
		assertEquals("xxxxxxxxxx", rs.getString(1));
		assertEquals(23, rs.getInt(2));
		assertEquals("xxxxxxxxxx", rs.getString(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0028 If T1 = 'xxxxxxxxxx'?

		// restore
		conn.rollback();

		// END TEST >>> 0028 <<< END TEST
		// ***********************************************************

		// TEST:0031 INSERT(column list) VALUES(NULL and literals)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TMP (T2, T3, T1) "
				+ "VALUES (NULL,'zz','z');");
		assertEquals(1, rowCount);
		// PASS:0031 If 1 row inserted?

		rs = stmt.executeQuery("SELECT * FROM TMP WHERE T2 IS NULL;");
		//
		// Expected output:
		//
		// T1 T2 T3
		// ========== ============ ==========
		//
		// z <null> zz
		//
		rs.next();
		assertEquals("z         ", rs.getString(1));
		assertEquals(0, rs.getInt(2));
		assertEquals("zz        ", rs.getString(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0031 If T1 = 'z '?

		// restore
		conn.rollback();

		// END TEST >>> 0031 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_011_UpdateViewWithoutWhereClause
	 * ----------------------------------------------------------------
	 */
	public void testDml_011_UpdateViewWithoutWhereClause() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0033 UPDATE view without <WHERE clause>!
		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE VIEW TEMP_SS(EMPNUM,GRADE,CITY) "
				+ "AS SELECT EMPNUM,GRADE,CITY " + "FROM   STAFF "
				+ "WHERE  GRADE > 12 " + "WITH CHECK OPTION;");

		conn.setAutoCommit(false);

		stmt.executeUpdate("UPDATE TEMP_SS SET GRADE = 15;");
		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_SS "
				+ "WHERE GRADE = 15;");
		rs.next();
		assertEquals(2, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0033 If count = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0033 <<< END TEST
		// ***************************************************************

		// TEST:0034 UPDATE table with SET column in <WHERE clause>!

		// setup
		rowCount = stmt.executeUpdate("UPDATE STAFF SET GRADE = 2*GRADE "
				+ "WHERE GRADE = 13;");
		assertEquals(2, rowCount);
		// PASS:0034 If 2 rows are updated?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE GRADE = 26;");
		rs.next();
		assertEquals(2, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0034 If count = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0034 <<< END TEST
		// ***********************************************************

		// TEST:0035 UPDATE with correlated subquery in <WHERE clause>!

		// setup
		rowCount = stmt.executeUpdate("UPDATE STAFF "
				+ "SET GRADE=10*STAFF.GRADE "
				+ "WHERE STAFF.EMPNUM NOT IN (SELECT WORKS.EMPNUM "
				+ "FROM WORKS WHERE STAFF.EMPNUM = WORKS.EMPNUM);");
		assertEquals(1, rowCount);
		// PASS:0035 If 1 row is updated?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE GRADE=130;");
		// 
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0035 If count = 1?

		// restore
		conn.rollback();

		// END TEST >>> 0035 <<< END TEST
		// ***************************************************************

		// TEST:0036 UPDATE view globally with check option violation!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF WHERE GRADE = 11;");
		// 
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0036 If count = 0?

	 // System.exit(0); 
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE TEMP_SS SET GRADE = 11;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
			System.out.println (errorCode); 
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0036 If ERROR, view check constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF WHERE GRADE = 11;");
		// 
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0036 If count = 0?

		// restore
		conn.rollback();

		// END TEST >>> 0036 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_012
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml012.
	//
	// ORIGIN: NIST file sql/dml012.sql
	//	
	public void testDml_012() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0037 DELETE without WHERE clause!
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0037 If count = 5?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0037 If 5 rows deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0037 If count = 0?

		// restore
		conn.rollback();

		// Testing Rollback
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0037 If count = 5?

		// END TEST >>> 0037 <<< END TEST
		// **************************************************************

		// TEST:0038 DELETE with correlated subquery in WHERE clause!
		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//           12
		//
		rs.next();
		assertEquals(12, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0038 If count = 12?

		rowCount = stmt.executeUpdate("DELETE FROM WORKS "
				+ "WHERE WORKS.PNUM IN " + "(SELECT PROJ.PNUM " + "FROM PROJ "
				+ "WHERE PROJ.PNUM=WORKS.PNUM " + "AND PROJ.CITY='Tampa');");
		assertEquals(1, rowCount);
		// PASS:0038 If 1 row deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//           11
		//
		rs.next();
		assertEquals(11, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0038 If count = 11?

		// restore
		conn.rollback();

		// Testing Rollback
		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//           12
		//
		rs.next();
		assertEquals(12, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0038 If count = 12?

		// END TEST >>> 0038 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_013
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml013.
	//
	// ORIGIN: NIST file sql/dml013.sql
	//	
	public void testDml_013() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0039 COUNT DISTINCT function!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E5','P5',NULL);");
		assertEquals(1, rowCount);
		// PASS:0039 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COUNT(DISTINCT HOURS) FROM WORKS;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0039 If count = 4?

		// restore
		conn.rollback();

		// END TEST >>> 0039 <<< END TEST
		// ************************************************************

		// TEST:0167 SUM ALL function!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E5','P5',NULL);");
		assertEquals(1, rowCount);
		// PASS:0167 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT SUM(ALL HOURS) FROM WORKS;");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                   464
		//
		rs.next();
		assertEquals(464, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0167 If SUM(ALL HOURS) = 464?

		// restore
		conn.rollback();

		// END TEST >>> 0167 <<< END TEST
		// ************************************************************

		// TEST:0168 SUM function!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E5','P5',NULL);");
		assertEquals(1, rowCount);
		// PASS:0168 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT SUM(HOURS) FROM WORKS;");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                   464
		//
		rs.next();
		assertEquals(464, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0168 If SUM(HOURS) = 464?

		// restore
		conn.rollback();

		// END TEST >>> 0168 <<< END TEST
		// ***********************************************************

		// TEST:0169 COUNT(*) function !

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E5','P5',NULL);");
		assertEquals(1, rowCount);
		// PASS:0169 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//           13
		//
		rs.next();
		assertEquals(13, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0169 If count = 13?

		// restore
		conn.rollback();

		// END TEST >>> 0169 <<< END TEST
		// *************************************************************

		// TEST:0040 SUM function with WHERE clause!
		rs = stmt.executeQuery("SELECT SUM(HOURS) FROM WORKS "
				+ "WHERE PNUM = 'P2';");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                   140
		//
		rs.next();
		assertEquals(140, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0040 If SUM(HOURS) = 140?

		// END TEST >>> 0040 <<< END TEST
		// ***************************************************************

		// TEST:0170 SUM DISTINCT function with WHERE clause!
		rs = stmt.executeQuery("SELECT SUM(DISTINCT HOURS) FROM WORKS "
				+ "WHERE PNUM = 'P2';");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                   100
		//
		rs.next();
		assertEquals(100, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0170 If SUM(DISTINCT HOURS) = 100?

		// END TEST >>> 0170 <<< END TEST
		// **************************************************************

		// TEST:0171 SUM(column) + value!
		rs = stmt.executeQuery("SELECT SUM(HOURS)+10 FROM WORKS "
				+ "WHERE PNUM = 'P2';");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                   150
		//
		rs.next();
		assertEquals(150, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0171 If SUM(HOURS)+10 = 150?

		// END TEST >>> 0171 <<< END TEST
		// ***************************************************************

		// TEST:0041 MAX function in subquery!
		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF "
				+ "WHERE GRADE = (SELECT MAX(GRADE) FROM STAFF) "
				+ "ORDER BY EMPNUM;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E3
		// E5
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));

		rs.next();
		assertEquals("E5 ", rs.getString(1));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0041 If 2 rows are selected and EMPNUMs = 'E3' and 'E5'?

		// END TEST >>> 0041 <<< END TEST
		// ***************************************************************

		// TEST:0042 MIN function in subquery!
		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF "
				+ "WHERE GRADE = (SELECT MIN(GRADE) FROM STAFF);");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E2
		//
		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0042 If EMPNUM = 'E2'?

		// END TEST >>> 0042 <<< END TEST
		// ***************************************************************

		// TEST:0043 AVG function!
		rs = stmt.executeQuery("SELECT AVG(GRADE) FROM STAFF;");
		//
		// Expected output:
		//
		//                   AVG
		// =====================
		//
		//                    12
		//
		rs.next();
		assertEquals(12, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0043 If AVG(GRADE) = 12?

		// END TEST >>> 0043 <<< END TEST
		// ***************************************************************
	}

	public void test_Dml_013_AvgFunctionEmptyResultNullValue()
			throws SQLException {
		int rowCount;
		// TEST:0044 AVG function - empty result NULL value!
		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");

		rowCount = stmt.executeUpdate("DELETE FROM TEMP_S;");

		rs = stmt.executeQuery("SELECT AVG(GRADE) FROM   TEMP_S;");
		//
		// Expected output:
		//
		//                   AVG
		// =====================
		//
		//                <null>
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0044 If AVG(GRADE) is NULL?

		// END TEST >>> 0044 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_014
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml014.
	//
	// ORIGIN: NIST file sql/dml014.sql
	//	
	public void testDml_014() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);
		// TEST:0045 BETWEEN predicate!
		rs = stmt.executeQuery("SELECT PNUM FROM PROJ "
				+ "WHERE BUDGET BETWEEN 40000 AND 60000;");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P6
		//
		rs.next();
		assertEquals("P6 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0045 If PNUM = 'P6'?

		rs = stmt.executeQuery("SELECT PNUM FROM PROJ "
				+ "WHERE BUDGET >= 40000 AND BUDGET <= 60000;");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P6
		//
		rs.next();
		assertEquals("P6 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0045 If PNUM = 'P6'?

		// END TEST >>> 0045 <<< END TEST
		// ***********************************************************

		// TEST:0046 NOT BETWEEN predicate !
		rs = stmt.executeQuery("SELECT CITY FROM STAFF "
				+ "WHERE GRADE NOT BETWEEN 12 AND 13;");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// Vienna
		//
		rs.next();
		assertEquals("Vienna         ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0046 If CITY = 'Vienna'?

		rs = stmt.executeQuery("SELECT CITY FROM STAFF "
				+ "WHERE NOT(GRADE BETWEEN 12 AND 13);");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// Vienna
		//
		rs.next();
		assertEquals("Vienna         ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0046 If CITY = 'Vienna'?

		// END TEST >>> 0046 <<< END TEST
		// *************************************************************

		// TEST:0047 IN predicate!
		rs = stmt.executeQuery("SELECT STAFF.EMPNAME FROM STAFF "
				+ "WHERE STAFF.EMPNUM IN " + "(SELECT WORKS.EMPNUM "
				+ "FROM WORKS " + "WHERE WORKS.PNUM IN " + "(SELECT PROJ.PNUM "
				+ "FROM PROJ " + "WHERE PROJ.CITY='Tampa'));");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0047 If EMPNAME = 'Alice'?

		rs = stmt.executeQuery("SELECT STAFF.EMPNAME FROM STAFF "
				+ "WHERE STAFF.EMPNUM = ANY " + "(SELECT WORKS.EMPNUM "
				+ "FROM WORKS " + "WHERE WORKS.PNUM IN " + "(SELECT PROJ.PNUM "
				+ "FROM PROJ " + "WHERE PROJ.CITY='Tampa'));");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0047 If EMPNAME = 'Alice'?

		// END TEST >>> 0047 <<< END TEST
		// ***********************************************************

		// TEST:0048 NOT IN predicate!
		rs = stmt.executeQuery("SELECT WORKS.HOURS FROM WORKS "
				+ "WHERE WORKS.PNUM NOT IN " + "(SELECT PROJ.PNUM "
				+ "FROM PROJ " + "WHERE PROJ.BUDGET BETWEEN 5000 AND 40000);");
		//
		// Expected output:
		//
		//        HOURS
		// ============
		//
		//           12
		//
		rs.next();
		assertEquals(12, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0048 If HOURS = 12?

		rs = stmt.executeQuery("SELECT WORKS.HOURS FROM WORKS "
				+ "WHERE NOT (WORKS.PNUM IN " + "(SELECT PROJ.PNUM "
				+ "FROM PROJ " + "WHERE PROJ.BUDGET BETWEEN 5000 AND 40000));");
		//
		// Expected output:
		//
		//        HOURS
		// ============
		//
		//           12
		//
		rs.next();
		assertEquals(12, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0048 If HOURS = 12?

		// END TEST >>> 0048 <<< END TEST
		// ****************************************************************

		// TEST:0049 IN predicate value list!
		rs = stmt.executeQuery("SELECT HOURS FROM WORKS "
				+ "WHERE PNUM NOT IN " + "(SELECT PNUM " + "FROM WORKS "
				+ "WHERE PNUM IN ('P1','P2','P4','P5','P6'));");
		//
		// Expected output:
		//
		//        HOURS
		// ============
		//
		//           80
		//
		rs.next();
		assertEquals(80, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0049 If HOURS = 80?

		rs = stmt.executeQuery("SELECT HOURS FROM WORKS "
				+ "WHERE NOT (PNUM IN " + "(SELECT PNUM " + "FROM WORKS "
				+ "WHERE PNUM IN ('P1','P2','P4','P5','P6')));");
		//
		// Expected output:
		//
		//        HOURS
		// ============
		//
		//           80
		//
		rs.next();
		assertEquals(80, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0049 If HOURS = 80?

		// END TEST >>> 0049 <<< END TEST
		// **************************************************************

		// TEST:0050 LIKE predicate -- %!
		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF "
				+ "WHERE EMPNAME LIKE 'Al%';");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0050 If EMPNAME = 'Alice'?

		// END TEST >>> 0050 <<< END TEST
		// **************************************************************

		// TEST:0051 LIKE predicate -- underscore!
		rs = stmt.executeQuery("SELECT CITY FROM STAFF "
				+ "WHERE EMPNAME LIKE 'B__t%';");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// Vienna
		//
		rs.next();
		assertEquals("Vienna         ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0051 If CITY = 'Vienna'?

		// END TEST >>> 0051 <<< END TEST
		// *************************************************************

		// TEST:0052 LIKE predicate -- ESCAPE character!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E36','Huyan',36,'Xi_an%');");
		assertEquals(1, rowCount);
		// PASS:0052 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CITY FROM STAFF "
				+ "WHERE CITY LIKE 'XiS___S%%' ESCAPE 'S';");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// Xi_an%
		//
		rs.next();
		assertEquals("Xi_an%         ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0052 If CITY = 'Xi_an%' ?

		// restore
		conn.rollback();

		// END TEST >>> 0052 <<< END TEST
		// **************************************************************

		// TEST:0053 NOT LIKE predicate!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E36','Huyan',36,'Xi_an%');");
		assertEquals(1, rowCount);
		// PASS:0053 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE EMPNUM  NOT LIKE '_36';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0053 If count = 5?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE NOT(EMPNUM  LIKE '_36');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0053 If count = 5?

		// restore
		conn.rollback();

		// END TEST >>> 0053 <<< END TEST
		// ***************************************************************

		// TEST:0054 IS NULL predicate!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E36','Huyan',36,NULL);");
		assertEquals(1, rowCount);
		// PASS:0054 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF "
				+ "WHERE CITY IS NULL;");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Huyan
		//
		rs.next();
		assertEquals("Huyan               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0054 If EMPNAME = 'Huyan'?

		// restore
		conn.rollback();

		// END TEST >>> 0054 <<< END TEST
		// ************************************************************

		// TEST:0055 NOT NULL predicate!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E36','Huyan',36,NULL);");
		// PASS:0055 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            6
		//
		rs.next();
		assertEquals(6, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0055 If count = 6?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE CITY IS NOT NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0055 If count = 5?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE NOT (CITY IS NULL);");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0055 If count = 5?

		// restore
		conn.rollback();

		// END TEST >>> 0055 <<< END TEST
		// ***************************************************************

		// TEST:0056 NOT EXISTS predicate!
		rs = stmt.executeQuery("SELECT STAFF.EMPNAME FROM STAFF "
				+ "WHERE NOT EXISTS " + "(SELECT * " + "FROM PROJ "
				+ "WHERE NOT EXISTS " + "(SELECT * " + "FROM WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM "
				+ "AND WORKS.PNUM=PROJ.PNUM));");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0056 If EMPNAME = 'Alice'?

		// END TEST >>> 0056 <<< END TEST
		// ************************************************************

		// TEST:0057 ALL quantifier !
		rs = stmt.executeQuery("SELECT CITY FROM PROJ " + "WHERE BUDGET > ALL "
				+ "(SELECT BUDGET " + "FROM PROJ " + "WHERE CITY='Vienna');");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// Deale
		//
		rs.next();
		assertEquals("Deale          ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0057 If CITY = 'Deale'?

		// END TEST >>> 0057 <<< END TEST
		// **************************************************************

		// TEST:0058 SOME quantifier!
		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF "
				+ "WHERE GRADE < SOME " + "(SELECT BUDGET/1000 - 39 "
				+ "FROM PROJ " + "WHERE CITY='Deale');");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Betty
		//
		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0058 If EMPNAME = 'Betty'?

		// END TEST >>> 0058 <<< END TEST
		// *************************************************************

		// TEST:0059 ANY quantifier !
		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF "
				+ "WHERE GRADE < ANY " + "(SELECT BUDGET/1000 - 39 "
				+ "FROM PROJ " + "WHERE CITY = 'Deale');");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Betty
		//
		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0059 If EMPNAME = 'Betty'?

		// END TEST >>> 0059 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_015
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml015.
	//
	// ORIGIN: NIST file sql/dml015.sql
	//	
	public void testDml_015() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");

		conn.setAutoCommit(false);

		// NO_TEST:0060 COMMIT work closes CURSORs!

		// Testing cursors

		// ************************************************************

		// TEST:0061 COMMIT work keeps changes to database!

		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "SELECT EMPNUM, GRADE, CITY FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0061 If 5 rows are inserted?

		conn.commit();

		// verify previous COMMIT keeps changes
		conn.rollback();

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0061 If count = 5?

		// END TEST >>> 0061 <<< END TEST
		// ************************************************************

		// TEST:0062 ROLLBACK work cancels changes to database!
		// NOTE:0062 uses data created by TEST 0061

		rowCount = stmt.executeUpdate("DELETE FROM TEMP_S "
				+ "WHERE EMPNUM = 'E5';");
		assertEquals(1, rowCount);
		// PASS:0062 If 1 row is deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0062 If count = 4?

		// restore
		conn.rollback();

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0062 If count = 5?

		// restore
		rowCount = stmt.executeUpdate("DELETE FROM TEMP_S;");
		assertEquals(5, rowCount);
		conn.commit();

		// END TEST >>> 0062 <<< END TEST
		// ***********************************************************

		// NO_TEST:0063 ROLLBACK work closes CURSORs!

		// Testing cursors
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_016
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml016.
	//
	// ORIGIN: NIST file sql/dml016.sql
	//	
	public void testDml_016() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0064 SELECT USER!

		rs = stmt.executeQuery("SELECT USER, PNAME FROM PROJ;");
		//
		// Expected output:
		//
		// USER PNAME
		// =============================== ====================
		//
		// SYSDBA MXSS
		// SYSDBA CALM
		// SYSDBA SDP
		// SYSDBA SDP
		// SYSDBA IRM
		// SYSDBA PAYR
		//
		rs.next();
		assertEquals("SYSDBA", rs.getString(1));
		assertEquals("MXSS                ", rs.getString(2));

		rs.next();
		assertEquals("SYSDBA", rs.getString(1));
		assertEquals("CALM                ", rs.getString(2));

		rs.next();
		assertEquals("SYSDBA", rs.getString(1));
		assertEquals("SDP                 ", rs.getString(2));

		rs.next();
		assertEquals("SYSDBA", rs.getString(1));
		assertEquals("SDP                 ", rs.getString(2));

		rs.next();
		assertEquals("SYSDBA", rs.getString(1));
		assertEquals("IRM                 ", rs.getString(2));

		rs.next();
		assertEquals("SYSDBA", rs.getString(1));
		assertEquals("PAYR                ", rs.getString(2));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0064 If 6 rows are selected and each USER = 'SULLIVAN' ?

		// END TEST >>> 0064 <<< END TEST
		// ***********************************************************

		// NO_TEST:0172 SELECT USER into short variable!
		// Tests Host Variable

		// **********************************************************

		// TEST:0065 SELECT CHAR literal and term with numeric literal!

		rs = stmt.executeQuery("SELECT 'USER',PNAME FROM PROJ;");
		//
		// Expected output:
		//
		//        PNAME
		// ====== ====================
		//
		// USER MXSS
		// USER CALM
		// USER SDP
		// USER SDP
		// USER IRM
		// USER PAYR
		//
		rs.next();
		assertEquals("USER", rs.getString(1));
		assertEquals("MXSS                ", rs.getString(2));

		rs.next();
		assertEquals("USER", rs.getString(1));
		assertEquals("CALM                ", rs.getString(2));

		rs.next();
		assertEquals("USER", rs.getString(1));
		assertEquals("SDP                 ", rs.getString(2));

		rs.next();
		assertEquals("USER", rs.getString(1));
		assertEquals("SDP                 ", rs.getString(2));

		rs.next();
		assertEquals("USER", rs.getString(1));
		assertEquals("IRM                 ", rs.getString(2));

		rs.next();
		assertEquals("USER", rs.getString(1));
		assertEquals("PAYR                ", rs.getString(2));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0065 If 6 rows are selected and first column is value 'USER'?

		rs = stmt.executeQuery("SELECT PNUM,'BUDGET IN GRAMS IS ',BUDGET * 5 "
				+ "FROM PROJ WHERE PNUM = 'P1';");
		//
		// Expected output:
		//
		// PNUM
		// ====== =================== =====================
		//
		// P1 BUDGET IN GRAMS IS 50000
		//
		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("BUDGET IN GRAMS IS ", rs.getString(2));
		assertEquals(50000, rs.getInt(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0065 If values are 'P1', 'BUDGET IN GRAMS IS ', 50000?

		// END TEST >>> 0065 <<< END TEST
		// ************************************************************

		// TEST:0066 SELECT numeric literal!
		rs = stmt.executeQuery("SELECT EMPNUM,10 FROM STAFF "
				+ "WHERE GRADE = 10;");
		//
		// Expected output:
		//
		// EMPNUM
		// ====== ============
		//
		// E2 10
		//
		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(10, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0066 If 1 row with values 'E2' and 10?

		rs = stmt.executeQuery("SELECT EMPNUM, 10 FROM STAFF;");
		//
		// Expected output:
		//
		// EMPNUM
		// ====== ============
		//
		// E1 10
		// E2 10
		// E3 10
		// E4 10
		// E5 10
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(10, rs.getInt(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(10, rs.getInt(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(10, rs.getInt(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(10, rs.getInt(2));

		rs.next();
		assertEquals("E5 ", rs.getString(1));
		assertEquals(10, rs.getInt(2));

		rowCount = 5;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(5, rowCount);
		// PASS:0066 If 5 rows are selected with second value always = 10?
		// PASS:0066 and EMPNUMs are 'E1', 'E2', 'E3', 'E4', 'E5'?

		// END TEST >>> 0066 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_018
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml018.
	//
	// ORIGIN: NIST file sql/dml018.sql
	//	
	public void testDml_018() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		// TEST:0069 HAVING COUNT with WHERE, GROUP BY!
		rs = stmt.executeQuery("SELECT PNUM FROM WORKS " + "WHERE PNUM > 'P1' "
				+ "GROUP BY PNUM " + "HAVING COUNT(*) > 1;");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P2
		// P4
		// P5
		//
		rs.next();
		assertEquals("P2 ", rs.getString(1));

		rs.next();
		assertEquals("P4 ", rs.getString(1));

		rs.next();
		assertEquals("P5 ", rs.getString(1));

		rowCount = 3;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(3, rowCount);
		// PASS:0069 If 3 rows are selected with PNUMs = 'P2', 'P4', 'P5'?

		// END TEST >>> 0069 <<< END TEST
		// ***********************************************************

		// TEST:0070 HAVING COUNT with GROUP BY!
		rs = stmt.executeQuery("SELECT PNUM FROM WORKS " + "GROUP BY PNUM "
				+ "HAVING COUNT(*) > 2;");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P2
		//
		rs.next();
		assertEquals("P2 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0070 If PNUM = 'P2'?

		// END TEST >>> 0070 <<< END TEST
		// ***********************************************************

		// TEST:0071 HAVING MIN, MAX with GROUP BY 3 columns!
		rs = stmt.executeQuery("SELECT EMPNUM, PNUM, HOURS FROM WORKS "
				+ "GROUP BY PNUM, EMPNUM, HOURS "
				+ "HAVING MIN(HOURS) > 12 AND MAX(HOURS) < 80;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E1 P1 40
		// E2 P1 40
		// E1 P2 20
		// E3 P2 20
		// E4 P2 20
		// E1 P4 20
		// E4 P4 40
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rowCount = 7;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(7, rowCount);
		// PASS:0071 If 7 rows are selected: EMPNUM/PNUMs are 'E1'/'P1',?
		// PASS:0071 'E1'/'P2','E1'/'P4', 'E2'/'P1',?
		// PASS:0071 'E3'/'P2', 'E4'/'P2', 'E4'/'P4'?

		// END TEST >>> 0071 <<< END TEST
		// *************************************************************

		// TEST:0072 Nested HAVING IN with no outer reference!
		rs = stmt.executeQuery("SELECT WORKS.PNUM FROM WORKS "
				+ "GROUP BY WORKS.PNUM "
				+ "HAVING WORKS.PNUM IN (SELECT PROJ.PNUM " + "FROM PROJ "
				+ "GROUP BY PROJ.PNUM " + "HAVING SUM(PROJ.BUDGET) > 25000);");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P2
		// P4
		// P5
		//
		rs.next();
		assertEquals("P2 ", rs.getString(1));

		rs.next();
		assertEquals("P3 ", rs.getString(1));

		rs.next();
		assertEquals("P6 ", rs.getString(1));

		rowCount = 3;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(3, rowCount);
		// PASS:0072 If 3 rows are selected: WORKS.PNUMs are 'P2', 'P3', 'P6'?

		// END TEST >>> 0072 <<< END TEST
		// ***********************************************************

		// TEST:0073 HAVING MIN with no GROUP BY!
		rs = stmt.executeQuery("SELECT SUM(HOURS) FROM WORKS "
				+ "HAVING MIN(PNUM) > 'P0';");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                   464
		//
		rs.next();
		assertEquals(464, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0073 If 1 row is selected with SUM(HOURS) = 464?

		// END TEST >>> 0073 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_019
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml019.
	//
	// ORIGIN: NIST file sql/dml019.sql
	//	
	public void testDml_019() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		// TEST:0074 GROUP BY col with SELECT col., SUM!
		rs = stmt.executeQuery("SELECT PNUM, SUM(HOURS) FROM WORKS "
				+ "GROUP BY PNUM;");
		//
		// Expected output:
		// 
		// PNUM SUM
		// ====== =====================
		//
		// P1 80
		// P2 140
		// P3 80
		// P4 60
		// P5 92
		// P6 12
		//
		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals(140, rs.getInt(2));

		rs.next();
		assertEquals("P3 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals(60, rs.getInt(2));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals(92, rs.getInt(2));

		rs.next();
		assertEquals("P6 ", rs.getString(1));
		assertEquals(12, rs.getInt(2));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0074 If 6 rows are selected?
		// PASS:0074 If PNUMs: 'P1', 'P2', 'P3', 'P4', 'P5', 'P6'?
		// PASS:0074 If SUM(HOURS) for 'P2' is 140 ?

		// END TEST >>> 0074 <<< END TEST
		// **********************************************************

		// TEST:0075 GROUP BY clause!
		rs = stmt
				.executeQuery("SELECT EMPNUM FROM WORKS " + "GROUP BY EMPNUM;");
		//
		// Expected output:
		// 
		// EMPNUM
		// ======
		//
		// E1
		// E2
		// E3
		// E4
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rs.next();
		assertEquals("E3 ", rs.getString(1));

		rs.next();
		assertEquals("E4 ", rs.getString(1));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0075 If 4 rows are selected with EMPNUMs: 'E1','E2','E3','E4'?

		// END TEST >>> 0075 <<< END TEST
		// ************************************************************

		// TEST:0076 GROUP BY 2 columns!
		rs = stmt.executeQuery("SELECT EMPNUM,HOURS FROM WORKS "
				+ "GROUP BY EMPNUM,HOURS;");
		//
		// Expected output:
		// 
		// EMPNUM HOURS
		// ====== ============
		//
		// E1 12
		// E1 20
		// E1 40
		// E1 80
		// E2 40
		// E2 80
		// E3 20
		// E4 20
		// E4 40
		// E4 80
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(12, rs.getInt(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(40, rs.getInt(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(40, rs.getInt(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(40, rs.getInt(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rowCount = 10;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(10, rowCount);
		// PASS:0076 If 10 rows are selected and EMPNUM = 'E1' in 4 rows ?
		// PASS:0076 for 1 row EMPNUM = 'E1' and HOURS = 12?

		// END TEST >>> 0076 <<< END TEST
		// ***********************************************************

		// TEST:0077 GROUP BY all columns with SELECT * !
		rs = stmt.executeQuery("SELECT * FROM WORKS "
				+ "GROUP BY PNUM,EMPNUM,HOURS;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E1 P1 40
		// E2 P1 40
		// E1 P2 20
		// E2 P2 80
		// E3 P2 20
		// E4 P2 20
		// E1 P3 80
		// E1 P4 20
		// E4 P4 40
		// E1 P5 12
		// E4 P5 80
		// E1 P6 12
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rowCount = 12;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(12, rowCount);
		// PASS:0077 If 12 rows are selected ?

		// END TEST >>> 0077 <<< END TEST
		// ***********************************************************

		// TEST:0078 GROUP BY three columns, SELECT two!
		rs = stmt.executeQuery("SELECT PNUM,EMPNUM FROM WORKS "
				+ "GROUP BY EMPNUM,PNUM,HOURS;");
		//
		// Expected output:
		//
		// PNUM EMPNUM
		// ====== ======
		//
		// P1 E1
		// P2 E1
		// P3 E1
		// P4 E1
		// P5 E1
		// P6 E1
		// P1 E2
		// P2 E2
		// P2 E3
		// P2 E4
		// P4 E4
		// P5 E4
		//
		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));

		rs.next();
		assertEquals("P3 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));

		rs.next();
		assertEquals("P6 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));

		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("E2 ", rs.getString(2));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E2 ", rs.getString(2));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E3 ", rs.getString(2));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));

		rowCount = 12;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(12, rowCount);
		// PASS:0078 If 12 rows are selected ?

		// END TEST >>> 0078 <<< END TEST
		// *********************************************************

		// TEST:0079 GROUP BY NULL value!

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO STAFF(EMPNUM,EMPNAME,GRADE) "
						+ "VALUES('E6','WANG',40);");
		assertEquals(1, rowCount);
		// PASS:0079 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO STAFF(EMPNUM,EMPNAME,GRADE) "
						+ "VALUES('E7','SONG',50);");
		assertEquals(1, rowCount);
		// PASS:0079 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT SUM(GRADE) FROM STAFF "
				+ "WHERE CITY IS NULL GROUP BY CITY;");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                    90
		//
		rs.next();
		assertEquals(90, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0079 If SUM(GRADE) = 90?

		// restore
		rowCount = stmt.executeUpdate("DELETE FROM STAFF WHERE CITY IS NULL;");
		assertEquals(2, rowCount);
		// PASS:0079 If 2 rows deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0079 If count = 5?

		// END TEST >>> 0079 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_020
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml020.
	//
	// ORIGIN: NIST file sql/dml020.sql
	//	
	public void testDml_020() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		// TEST:0080 Simple two-table join!
		rs = stmt
				.executeQuery("SELECT EMPNUM,EMPNAME,GRADE,STAFF.CITY, PNAME, PROJ.CITY "
						+ "FROM STAFF, PROJ " + "WHERE STAFF.CITY = PROJ.CITY " +
						"order by EMPNAME, PNAME;");
		
		// wbo - added order by since S64 test results differed from windows
		// new expected output:
		// EMPNUM EMPNAME GRADE CITY PNAME CITY
		// E1 Alice 12 Deale MXSS Deale
		// E1 Alice 12 Deale PAYR Deale
		// E1 Alice 12 Deale SDP Deale
		
		//
		// OLD Expected output:
		//
		// EMPNUM EMPNAME GRADE CITY PNAME CITY
		// E4 Don 12 Deale MXSS Deale
		// E4 Don 12 Deale PAYR Deale
		// E4 Don 12 Deale SDP Deale
		// E1 Alice 12 Deale MXSS Deale
		// E1 Alice 12 Deale PAYR Deale
		// E1 Alice 12 Deale SDP Deale
		// E2 Betty 10 Vienna CALM Vienna
		// E2 Betty 10 Vienna IRM Vienna
		// E3 Carmen 13 Vienna CALM Vienna
		// E3 Carmen 13 Vienna IRM Vienna
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Alice               ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals("Deale          ", rs.getString(4));
		assertEquals("MXSS                ", rs.getString(5));
		assertEquals("Deale          ", rs.getString(6));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Alice               ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals("Deale          ", rs.getString(4));
		assertEquals("PAYR                ", rs.getString(5));
		assertEquals("Deale          ", rs.getString(6));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Alice               ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals("Deale          ", rs.getString(4));
		assertEquals("SDP                 ", rs.getString(5));
		assertEquals("Deale          ", rs.getString(6));

// now count the rows, verify 10
		rowCount = 0; 
		rs = stmt
		.executeQuery("SELECT EMPNUM,EMPNAME,GRADE,STAFF.CITY, PNAME, PROJ.CITY "
				+ "FROM STAFF, PROJ " + "WHERE STAFF.CITY = PROJ.CITY " +
				"order by EMPNAME, PNAME;");

		while (rs.next()) {
			rowCount++;
		}
		assertEquals(10, rowCount);
		// PASS:0080 If 10 rows are selected with EMPNAMEs:'Alice', 'Betty', ?
		// PASS:0080 'Carmen', and 'Don' but not 'Ed'?

		// END TEST >>> 0080 <<< END TEST
		// **************************************************************

		// TEST:0081 Simple two-table join with filter!
		rs = stmt
				.executeQuery("SELECT EMPNUM,EMPNAME,GRADE,STAFF.CITY,PNUM,PNAME, "
						+ "PTYPE,BUDGET,PROJ.CITY "
						+ "FROM STAFF, PROJ "
						+ "WHERE STAFF.CITY = PROJ.CITY " + "AND GRADE <> 12;");
		//
		// Expected output:
		//
		// EMPNUM EMPNAME GRADE CITY PNUM PNAME PTYPE BUDGET CITY
		// ====== ==================== ============ =============== ======
		// ==================== ====== ============ ===============
		//
		// E2 Betty 10 Vienna P5 IRM Test 10000 Vienna
		// E2 Betty 10 Vienna P2 CALM Code 30000 Vienna
		// E3 Carmen 13 Vienna P5 IRM Test 10000 Vienna
		// E3 Carmen 13 Vienna P2 CALM Code 30000 Vienna
		//
		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Betty               ", rs.getString(2));
		assertEquals(10, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));
		assertEquals("P5 ", rs.getString(5));
		assertEquals("IRM                 ", rs.getString(6));
		assertEquals("Test  ", rs.getString(7));
		assertEquals(10000, rs.getInt(8));
		assertEquals("Vienna         ", rs.getString(9));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Betty               ", rs.getString(2));
		assertEquals(10, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));
		assertEquals("P2 ", rs.getString(5));
		assertEquals("CALM                ", rs.getString(6));
		assertEquals("Code  ", rs.getString(7));
		assertEquals(30000, rs.getInt(8));
		assertEquals("Vienna         ", rs.getString(9));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("Carmen              ", rs.getString(2));
		assertEquals(13, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));
		assertEquals("P5 ", rs.getString(5));
		assertEquals("IRM                 ", rs.getString(6));
		assertEquals("Test  ", rs.getString(7));
		assertEquals(10000, rs.getInt(8));
		assertEquals("Vienna         ", rs.getString(9));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("Carmen              ", rs.getString(2));
		assertEquals(13, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));
		assertEquals("P2 ", rs.getString(5));
		assertEquals("CALM                ", rs.getString(6));
		assertEquals("Code  ", rs.getString(7));
		assertEquals(30000, rs.getInt(8));
		assertEquals("Vienna         ", rs.getString(9));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0081 If 4 rows selected with EMPNAMEs 'Betty' and 'Carmen' ?

		// END TEST >>> 0081 <<< END TEST
		// **************************************************************

		// TEST:0082 Join 3 tables!
		rs = stmt.executeQuery("SELECT DISTINCT STAFF.CITY, PROJ.CITY "
				+ "FROM STAFF, WORKS, PROJ "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM "
				+ "AND WORKS.PNUM = PROJ.PNUM;");
		//
		// Expected output:
		//
		// CITY CITY
		// =============== ===============
		// 
		// Deale Deale
		// Deale Tampa
		// Deale Vienna
		// Vienna Deale
		// Vienna Vienna
		//
		rs.next();
		assertEquals("Deale          ", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));

		rs.next();
		assertEquals("Deale          ", rs.getString(1));
		assertEquals("Tampa          ", rs.getString(2));

		rs.next();
		assertEquals("Deale          ", rs.getString(1));
		assertEquals("Vienna         ", rs.getString(2));

		rs.next();
		assertEquals("Vienna         ", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));

		rs.next();
		assertEquals("Vienna         ", rs.getString(1));
		assertEquals("Vienna         ", rs.getString(2));

		rowCount = 5;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(5, rowCount);
		// PASS:0082 If 5 distinct rows are selected ?

		// END TEST >>> 0082 <<< END TEST
		// ************************************************************

		// TEST:0083 Join a table with itself!
		rs = stmt.executeQuery("SELECT FIRST1.EMPNUM, SECOND2.EMPNUM "
				+ "FROM STAFF FIRST1, STAFF SECOND2 "
				+ "WHERE FIRST1.CITY = SECOND2.CITY "
				+ "AND FIRST1.EMPNUM < SECOND2.EMPNUM;");
		//
		// Expected output:
		//
		// EMPNUM EMPNUM
		// ====== ======
		//
		// E1 E4
		// E2 E3
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("E3 ", rs.getString(2));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0083 If 2 rows are selected and ?
		// PASS:0083 If EMPNUM pairs are 'E1'/'E4' and 'E2'/'E3'?

		// END TEST >>> 0083 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_021
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml021.
	//
	// ORIGIN: NIST file sql/dml021.sql
	//	
	public void testDml_021() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0084 Data type CHAR(20)!

		// setup
		stmt.executeUpdate("CREATE TABLE AA (CHARTEST     CHAR(20));");
		stmt.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");
		stmt.executeUpdate("CREATE TABLE CC (CHARTEST     CHARACTER(20));");
		stmt.executeUpdate("CREATE TABLE DD (CHARTEST     CHARACTER);");
		stmt.executeUpdate("CREATE TABLE EE (INTTEST     INTEGER);");
		stmt.executeUpdate("CREATE TABLE FF (INTTEST     INT);");
		stmt.executeUpdate("CREATE TABLE GG (REALTEST     REAL);");
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		stmt.executeUpdate("CREATE TABLE II (DOUBLETEST  DOUBLE PRECISION);");
		stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");
		stmt.executeUpdate("CREATE TABLE KK (FLOATTEST  FLOAT(32));");
		stmt.executeUpdate("CREATE TABLE LL (NUMTEST  NUMERIC(13,6));");
		stmt.executeUpdate("CREATE TABLE MM (NUMTEST  NUMERIC);");
		stmt.executeUpdate("CREATE TABLE MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE NN (NUMTEST  NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE OO (NUMTEST  NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE PP (NUMTEST  DECIMAL(13,6));");
		stmt.executeUpdate("CREATE TABLE QQ (NUMTEST  DECIMAL);");
		stmt.executeUpdate("CREATE TABLE RR (NUMTEST  DECIMAL(8));");
		stmt.executeUpdate("CREATE TABLE SS (NUMTEST  DEC(13,6));");

		conn.setAutoCommit(false);

		rowCount = stmt.executeUpdate("INSERT INTO AA "
				+ "VALUES('abcdefghijklmnopqrst');");
		assertEquals(1, rowCount);
		// PASS:0084 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CHARTEST FROM AA;");
		//
		// Expected output:
		//
		// CHARTEST
		// ====================
		// 
		// abcdefghijklmnopqrst
		//
		rs.next();
		assertEquals("abcdefghijklmnopqrst", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0084 If CHARTEST = 'abcdefghijklmnopqrst' ?

		// restore
		conn.rollback();

		// END TEST >>> 0084 <<< END TEST
		// *************************************************************

		// TEST:0173 Data type CHAR!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO BB VALUES('a');");
		assertEquals(1, rowCount);
		// PASS:0173 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CHARTEST FROM BB;");
		//
		// Expected output:
		//
		// CHARTEST
		// ========
		//
		// a
		//
		rs.next();
		assertEquals("a", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0173 If CHARTEST = 'a'?

		// restore
		conn.rollback();

		// END TEST >>> 0173 <<< END TEST
		// *****************************************************************

		// TEST:0085 Data type CHARACTER(20)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO CC "
				+ "VALUES('abcdefghijklmnopqrst');");
		assertEquals(1, rowCount);
		// PASS:0085 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CHARTEST FROM CC;");
		//
		// Expected output:
		//
		// CHARTEST
		// ====================
		// 
		// abcdefghijklmnopqrst
		//
		rs.next();
		assertEquals("abcdefghijklmnopqrst", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0085 If CHARTEST = 'abcdefghijklmnopqrst'?

		// restore
		conn.rollback();

		// END TEST >>> 0085 <<< END TEST
		// *************************************************************

		// TEST:0174 Data type CHARACTER!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO DD VALUES('a');");
		assertEquals(1, rowCount);
		// PASS:0174 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CHARTEST FROM DD;");
		//
		// Expected output:
		//
		// CHARTEST
		// ========
		//       
		// a
		//
		rs.next();
		assertEquals("a", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0174 If CHARTEST = 'a'?

		// restore
		conn.rollback();

		// END TEST >>> 0174 <<< END TEST
		// ****************************************************************

		// TEST:0086 Data type INTEGER!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO EE VALUES(123456);");
		assertEquals(1, rowCount);
		// PASS:0086 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT INTTEST FROM EE;");
		//
		// Expected output:
		//
		//      INTTEST
		// ============
		//
		//       123456
		//
		rs.next();
		assertEquals(123456, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0086 If INTTEST = 123456?

		// restore
		conn.rollback();

		// END TEST >>> 0086 <<< END TEST
		// ***************************************************************

		// TEST:0087 Data type INT!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO FF VALUES(123456);");
		assertEquals(1, rowCount);
		// PASS:0087 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT INTTEST FROM FF;");
		//
		// Expected output:
		//
		//      INTTEST
		// ============
		//
		//       123456
		//
		rs.next();
		assertEquals(123456, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0087 If INTTEST = 123456?

		// restore
		conn.rollback();

		// END TEST >>> 0087 <<< END TEST
		// **************************************************************

		// TEST:0089 Data type SMALLINT!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES(123);");
		assertEquals(1, rowCount);
		// PASS:0089 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM HH;");
		//
		// Expected output:
		//
		// SMALLTEST
		// =========
		//
		//       123
		//
		rs.next();
		assertEquals(123, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0089 If SMALLTEST = 123?

		// restore
		conn.rollback();

		// END TEST >>> 0089 <<< END TEST
		// ****************************************************************

		// TEST:0175 Data type NUMERIC!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO MM VALUES(7);");
		assertEquals(1, rowCount);
		// PASS:0175 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM MM;");
		//
		// Expected output:
		//
		//      NUMTEST
		// ============
		//
		//            7
		//
		rs.next();
		assertEquals(7, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0175 If NUMTEST = 7?

		// restore
		conn.rollback();

		// END TEST >>> 0175 <<< END TEST
		// ****************************************************************

		// TEST:0176 Data type NUMERIC(9), SELECT *!

		// making sure table is empty
		rowCount = stmt.executeUpdate("DELETE FROM NN;");

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO NN VALUES(123456789);");
		assertEquals(1, rowCount);
		// PASS:0176 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM NN;");
		//
		// Expected output:
		//
		//      NUMTEST
		// ============
		//
		//    123456789
		//
		rs.next();
		assertEquals(123456789, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0176 If NUMTEST = 123456789 ?

		// restore
		conn.rollback();

		// END TEST >>> 0176 <<< END TEST
		// *****************************************************************

		// TEST:0177 Data type NUMERIC(9), SELECT column!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO OO VALUES(123456789);");
		assertEquals(1, rowCount);
		// PASS:0177 If 1 row is inserted ?

		rs = stmt.executeQuery("SELECT NUMTEST FROM OO;");
		//
		// Expected output:
		//
		//      NUMTEST
		// ============
		//
		//    123456789
		//
		rs.next();
		assertEquals(123456789, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0177 If NUMTEST = 123456789?

		// restore
		conn.rollback();

		// END TEST >>> 0177 <<< END TEST
		// **************************************************************

		// TEST:0178 Data type DECIMAL!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO QQ VALUES(56);");
		assertEquals(1, rowCount);
		// PASS:0178 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM QQ;");
		//
		// Expected output:
		//
		//      NUMTEST
		// ============
		//
		//           56
		//
		rs.next();
		assertEquals(56, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0178 If NUMTEST = 56?

		// restore
		conn.rollback();

		// END TEST >>> 0178 <<< END TEST
		// **************************************************************

		// TEST:0179 Data type DECIMAL(8)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO RR VALUES(12345678);");
		assertEquals(1, rowCount);
		// PASS:0179 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM RR;");
		//
		// Expected output:
		//
		//      NUMTEST
		// ============
		//
		//     12345678
		//
		rs.next();
		assertEquals(12345678, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0179 If NUMTEST = 12345678?

		// restore
		conn.rollback();

		// END TEST >>> 0179 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_022
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml022.
	//
	// ORIGIN: NIST file sql/dml022.sql
	//	
	public void testDml_022() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0096 Subquery with MAX in < comparison predicate!

		BaseTab.setupBaseTab(stmt);
		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF " + "WHERE GRADE < "
				+ "(SELECT MAX(GRADE) " + "FROM STAFF);");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		// E2
		// E4
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rs.next();
		assertEquals("E4 ", rs.getString(1));

		rowCount = 3;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(3, rowCount);
		// PASS:0096 If 3 rows selected with EMPNUMs:'E1', 'E2', 'E4'?

		// END TEST >>> 0096 <<< END TEST
		// **********************************************************

		// TEST:0097 Subquery with AVG - 1 in <= comparison predicate!
		rs = stmt.executeQuery("SELECT * FROM STAFF " + "WHERE GRADE <= "
				+ "(SELECT AVG(GRADE)-1 " + "FROM STAFF);");
		//
		// Expected output:
		//
		// EMPNUM EMPNAME GRADE CITY
		// ====== ==================== ============ ===============
		//
		// E2 Betty 10 Vienna
		//
		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Betty               ", rs.getString(2));
		assertEquals(10, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0097 If EMPNUM = 'E2' and EMPNAME = 'Betty'?

		// END TEST >>> 0097 <<< END TEST
		// *******************************************************************

		// TEST:0098 IN predicate with simple subquery!
		rs = stmt.executeQuery("SELECT EMPNAME " + "FROM STAFF "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM " + "FROM WORKS "
				+ "WHERE PNUM = 'P2') " + "ORDER BY EMPNAME;");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		// Betty
		// Carmen
		// Don
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rs.next();
		assertEquals("Carmen              ", rs.getString(1));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0098 If 4 rows selected and first EMPNAME = 'Alice'?

		// END TEST >>> 0098 <<< END TEST
		// ***************************************************************

		// TEST:0099 Nested IN predicate - 2 levels!
		rs = stmt.executeQuery("SELECT EMPNAME " + "FROM STAFF "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM " + "FROM WORKS "
				+ "WHERE PNUM IN " + "(SELECT PNUM " + "FROM PROJ "
				+ "WHERE PTYPE = 'Design'));");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		// Betty
		// Don
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));

		rowCount = 3;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(3, rowCount);
		// PASS:0099 If 3 rows selected with EMPNAMEs:'Alice', 'Betty', 'Don'?

		// END TEST >>> 0099 <<< END TEST
		// *****************************************************************

		// TEST:0100 Nested IN predicate - 6 levels!
		rs = stmt.executeQuery("SELECT EMPNUM, EMPNAME " + "FROM STAFF "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM " + "FROM WORKS "
				+ "WHERE PNUM IN " + "(SELECT PNUM " + "FROM PROJ "
				+ "WHERE PTYPE IN (SELECT PTYPE " + "FROM PROJ "
				+ "WHERE PNUM IN " + "(SELECT PNUM " + "FROM WORKS "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM " + "FROM WORKS "
				+ "WHERE PNUM IN " + "(SELECT PNUM " + "FROM PROJ "
				+ "WHERE PTYPE = 'Design')))))) " + "ORDER BY EMPNUM;");
		//
		// Expected output:
		//
		// EMPNUM EMPNAME
		// ====== ====================
		//
		// E1 Alice
		// E2 Betty
		// E3 Carmen
		// E4 Don
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Alice               ", rs.getString(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Betty               ", rs.getString(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("Carmen              ", rs.getString(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("Don                 ", rs.getString(2));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0100 If 4 rows selected and first EMPNUM = 'E1'?
		// PASS:0100 and first EMPNAME = 'Alice'?

		// END TEST >>> 0100 <<< END TEST
		// ****************************************************************

		// TEST:0101 Quantified predicate <= ALL with AVG in GROUP BY!
		rs = stmt.executeQuery("SELECT EMPNUM,PNUM " + "FROM   WORKS "
				+ "WHERE  HOURS <= ALL " + "(SELECT AVG(HOURS) "
				+ "FROM   WORKS " + "GROUP BY PNUM);");
		//
		// Expected output:
		//
		// EMPNUM PNUM
		// ====== ======
		//
		// E1 P5
		// E1 P6
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0101 If 2 rows selected and each EMPNUM = 'E1'?

		// END TEST >>> 0101 <<< END TEST
		// *******************************************************************

		// TEST:0102 Nested NOT EXISTS with correlated subquery and DISTINCT!
		rs = stmt.executeQuery("SELECT DISTINCT EMPNUM " + "FROM WORKS WORKSX "
				+ "WHERE NOT EXISTS " + "(SELECT * " + "FROM WORKS WORKSY "
				+ "WHERE EMPNUM = 'E2' " + "AND NOT EXISTS " + "(SELECT * "
				+ "FROM WORKS WORKSZ " + "WHERE WORKSZ.EMPNUM = WORKSX.EMPNUM "
				+ "AND WORKSZ.PNUM = WORKSY.PNUM));");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		// E2
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0102 If 2 rows selected with EMPNUMs:'E1', 'E2'?

		// END TEST >>> 0102 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//
	// Statements used to exercise test sql/dml023.
	//
	// ORIGIN: NIST file sql/dml023.sql
	//	
	public void testDml_023() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		// TEST:0103 Subquery with comparison predicate!
		rs = stmt.executeQuery("SELECT PNUM FROM PROJ "
				+ "WHERE PROJ.CITY = (SELECT STAFF.CITY FROM STAFF "
				+ "WHERE EMPNUM = 'E1');");
		rs.next();
		assertEquals("P1 ", rs.getString(1));
		rs.next();
		assertEquals("P4 ", rs.getString(1));
		rs.next();
		assertEquals("P6 ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0103 If 3 rows are selected with PNUMs:'P1','P4','P6?

		// END TEST >>> 0103 <<< END TEST
		// **************************************************************

		// TEST:0104 SQLCODE < 0, subquery with more than 1 value!
		errorCode = 0;
		try {
			rs = stmt.executeQuery("SELECT PNUM FROM PROJ "
					+ "WHERE PROJ.CITY = (SELECT STAFF.CITY FROM STAFF "
					+ "WHERE EMPNUM > 'E1');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544652) {
				fail("Should return multiple rows in singleton select.");
			}
		}
		// PASS:0104 If ERROR, SELECT returns more than 1 row in subquery?
		// PASS:0104 If 0 rows are selected?

		// END TEST >>> 0104 <<< END TEST
		// ************************************************************

		// TEST:0105 Subquery in comparison predicate is empty!
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE STAFF.CITY = (SELECT PROJ.CITY FROM PROJ "
				+ "WHERE PNUM > 'P7');");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0105 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE NOT (STAFF.CITY = (SELECT PROJ.CITY FROM PROJ "
				+ "WHERE PNUM > 'P7'));");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0105 If count = 0?

		// END TEST >>> 0105 <<< END TEST
		// *************************************************************

		// TEST:0106 Comparison predicate <> !
		rs = stmt.executeQuery("SELECT PNUM FROM PROJ WHERE CITY <> 'Deale';");
		rs.next();
		assertEquals("P2 ", rs.getString(1));
		rs.next();
		assertEquals("P3 ", rs.getString(1));
		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0106 If 3 rows are selected with PNUMs:'P2','P3','P5'?

		// END TEST >>> 0106 <<< END TEST
		// *************************************************************

		// TEST:0107 Comp predicate with short string logically blank padded!
		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM WORKS WHERE EMPNUM = 'E1';");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:0107 If count = 6 ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS "
				+ "WHERE EMPNUM = 'E1' AND EMPNUM = 'E1 ';");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:0107 If count = 6?
		// END TEST >>> 0107 <<< END TEST
		// ****************************************************************

	}

	public void testDml_023_NullsSortTogetherInOrderBy() throws SQLException {
		// TEST:0180 NULLs sort together in ORDER BY!

		BaseTab.setupBaseTab(stmt);
		stmt
				.executeUpdate("UPDATE STAFF SET GRADE = NULL "
						+ "WHERE EMPNUM = 'E1' OR "
						+ "EMPNUM = 'E3' OR EMPNUM = 'E5';");
		// PASS:0180 If 3 rows are updated?

		rs = stmt.executeQuery("SELECT EMPNUM,GRADE FROM STAFF ORDER "
				+ "BY GRADE,EMPNUM;");

		// firebird nulls are sorted at end
		java.sql.DatabaseMetaData dbmd = conn.getMetaData();
		if (dbmd.nullsAreSortedAtEnd()) {
			rs.next();
			assertEquals("E2 ", rs.getString(1));
			assertEquals(10, rs.getInt(2));

			rs.next();
			assertEquals("E4 ", rs.getString(1));
			assertEquals(12, rs.getInt(2));

			rs.next();
			assertEquals("E1 ", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			rs.next();
			assertEquals("E3 ", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			rs.next();
			assertEquals("E5 ", rs.getString(1));
			assertEquals(0, rs.getInt(2));
			assertFalse(rs.next());
			// PASS:0180 If 5 rows are selected with NULLs together ?
			// PASS:0180 If first EMPNUM is either 'E1' or 'E2'?
			// PASS:0180 If last EMPNUM is either 'E4' or 'E5?
		} else if (dbmd.nullsAreSortedLow()) {
			rs.next();
			assertEquals("E1 ", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			rs.next();
			assertEquals("E3 ", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			rs.next();
			assertEquals("E5 ", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			rs.next();
			assertEquals("E2 ", rs.getString(1));
			assertEquals(10, rs.getInt(2));

			rs.next();
			assertEquals("E4 ", rs.getString(1));
			assertEquals(12, rs.getInt(2));
			assertFalse(rs.next());

		}
	}

	// END TEST >>> 0180 <<< END TEST
	// ***************************************************************
	public void testDml_023_NullsAreEqualForDistinct() throws SQLException {

		// TEST:0181 NULLs are equal for DISTINCT!
		BaseTab.setupBaseTab(stmt);
		stmt
				.executeUpdate("UPDATE STAFF SET GRADE = NULL "
						+ "WHERE EMPNUM = 'E1' OR "
						+ "EMPNUM = 'E3' OR EMPNUM = 'E5';");
		// PASS:0181 If 3 rows are updated?

		rs = stmt.executeQuery("SELECT DISTINCT USER, GRADE FROM STAFF "
				+ "ORDER BY GRADE;");
		// firebird 1.5 nulls are sorted at end
		java.sql.DatabaseMetaData dbmd = conn.getMetaData();
		if (dbmd.nullsAreSortedAtEnd()) {
			rs.next();
			assertEquals("SYSDBA", rs.getString(1));
			assertEquals(10, rs.getInt(2));

			rs.next();
			assertEquals("SYSDBA", rs.getString(1));
			assertEquals(12, rs.getInt(2));

			rs.next();
			assertEquals("SYSDBA", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			assertFalse(rs.next());
			// PASS:0181 If 3 rows are selected with GRADEs:10, 12, NULL ?
			// PASS:0181 GRADE 10 precedes GRADE 12?
		} else if (dbmd.nullsAreSortedLow()) {
			rs.next();
			assertEquals("SYSDBA", rs.getString(1));
			assertEquals(0, rs.getInt(2));

			rs.next();
			assertEquals("SYSDBA", rs.getString(1));
			assertEquals(10, rs.getInt(2));

			rs.next();
			assertEquals("SYSDBA", rs.getString(1));
			assertEquals(12, rs.getInt(2));

			assertFalse(rs.next());

			// PASS:0181 If 3 rows are selected with GRADEs:10, 12, NULL ?
			// PASS:0181 GRADE 10 precedes GRADE 12?
		}
		// END TEST >>> 0181 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_024
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml024.
	//
	// ORIGIN: NIST file sql/dml024.sql
	//	
	public void testDml_024() throws SQLException {
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0108 Search condition true OR NOT(true)!
		rs = stmt.executeQuery("SELECT EMPNUM,CITY FROM   STAFF "
				+ "WHERE  EMPNUM='E1' OR NOT(EMPNUM='E1');");
		//
		// Expected output:
		//
		// EMPNUM CITY
		// ====== ===============
		//
		// E1 Deale
		// E2 Vienna
		// E3 Vienna
		// E4 Deale
		// E5 Akron
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Vienna         ", rs.getString(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("Vienna         ", rs.getString(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));

		rs.next();
		assertEquals("E5 ", rs.getString(1));
		assertEquals("Akron          ", rs.getString(2));

		rowCount = 5;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(5, rowCount);
		// PASS:0108 If 5 rows are selected ?

		// END TEST >>> 0108 <<< END TEST
		// ****************************************************************

		// TEST:0109 Search condition true AND NOT(true)!
		rs = stmt.executeQuery("SELECT EMPNUM,CITY FROM   STAFF "
				+ "WHERE  EMPNUM='E1' AND NOT(EMPNUM='E1');");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0109 If 0 rows are selected ?

		// END TEST >>> 0109 <<< END TEST
		// **************************************************************

		// TEST:0110 Search condition unknown OR NOT(unknown)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E8','P8',NULL);");
		assertEquals(1, rowCount);
		// PASS:0110 If 1 row is inserted?

		rs = stmt
				.executeQuery("SELECT EMPNUM,PNUM FROM WORKS "
						+ "WHERE HOURS < (SELECT HOURS FROM WORKS "
						+ "WHERE EMPNUM = 'E8') OR NOT(HOURS < (SELECT HOURS FROM WORKS "
						+ "WHERE EMPNUM = 'E8'));");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0110 If 0 rows are selected ?

		// restore
		conn.rollback();

		// END TEST >>> 0110 <<< END TEST
		// *************************************************************

		// TEST:0111 Search condition unknown AND NOT(unknown)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E8','P8',NULL);");
		assertEquals(1, rowCount);
		// PASS:0111 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,PNUM FROM WORKS "
				+ "WHERE HOURS < (SELECT HOURS FROM WORKS "
				+ "WHERE EMPNUM = 'E8') "
				+ "AND NOT(HOURS< (SELECT HOURS FROM WORKS "
				+ "WHERE EMPNUM = 'E8'));");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0111 If 0 rows are selected?

		// restore
		conn.rollback();

		// END TEST >>> 0111 <<< END TEST
		// ***************************************************************

		// TEST:0112 Search condition unknown AND true!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E8','P8',NULL);");
		assertEquals(1, rowCount);
		// PASS:0112 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,PNUM FROM WORKS "
				+ "WHERE HOURS < (SELECT HOURS FROM WORKS "
				+ "WHERE EMPNUM = 'E8') "
				+ "AND   HOURS IN (SELECT HOURS FROM WORKS);");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0112 If 0 rows are selected?

		// restore
		conn.rollback();

		// END TEST >>> 0112 <<< END TEST
		// *************************************************************

		// TEST:0113 Search condition unknown OR true!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E8','P8',NULL);");
		assertEquals(1, rowCount);
		// PASS:0113 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,PNUM FROM WORKS "
				+ "WHERE HOURS < (SELECT HOURS FROM WORKS "
				+ "WHERE EMPNUM = 'E8') "
				+ "OR    HOURS IN (SELECT HOURS FROM WORKS) "
				+ "ORDER BY EMPNUM;");
		//
		// Expected output:
		//
		// EMPNUM PNUM
		// ====== ======
		//
		// E1 P1
		// E1 P2
		// E1 P3
		// E1 P4
		// E1 P5
		// E1 P6
		// E2 P1
		// E2 P2
		// E3 P2
		// E4 P2
		// E4 P4
		// E4 P5
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));

		rowCount = 12;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(12, rowCount);
		// PASS:0113 If 12 rows are selected?
		// PASS:0113 If first EMPNUM = 'E1'?

		// restore
		conn.rollback();

		// END TEST >>> 0113 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_025
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml025.
	//
	// ORIGIN: NIST file sql/dml025.sql
	//	
	public void testDml_025() throws SQLException {
		int rowCount; /* Row count. */

		// TEST:0114 Set functions without GROUP BY returns 1 row!
		BaseTab.setupBaseTab(stmt);
		rs = stmt
				.executeQuery("SELECT SUM(HOURS),AVG(HOURS),MIN(HOURS),MAX(HOURS) "
						+ "FROM    WORKS " + "WHERE   EMPNUM='E1';");
		//
		// Expected output:
		//
		//                   SUM AVG MIN MAX
		// ===================== ===================== ============ ============
		//
		//                   184 30 12 80
		//
		rs.next();
		assertEquals(184, rs.getInt(1));
		assertEquals(30, rs.getInt(2));
		assertEquals(12, rs.getInt(3));
		assertEquals(80, rs.getInt(4));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0114 If SUM(HOURS) = 184 and AVG(HOURS) is 30 to 31?
		// PASS:0114 If MIN(HOURS) = 12 and MAX(HOURS) = 80 ?

		// END TEST >>> 0114 <<< END TEST
		// ***********************************************************

		// TEST:0115 GROUP BY col, set function: 0 groups returns empty table!
		rs = stmt.executeQuery("SELECT PNUM,AVG(HOURS),MIN(HOURS),MAX(HOURS) "
				+ "FROM    WORKS " + "WHERE   EMPNUM='E8' " + "GROUP BY PNUM;");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0115 If 0 rows are selected ?

		// END TEST >>> 0115 <<< END TEST
		// ***********************************************************

		// TEST:0116 GROUP BY set functions: zero groups returns empty table!
		rs = stmt
				.executeQuery("SELECT SUM(HOURS),AVG(HOURS),MIN(HOURS),MAX(HOURS) "
						+ "FROM    WORKS "
						+ "WHERE   EMPNUM='E8' "
						+ "GROUP BY PNUM;");

		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0116 If 0 rows are selected?

		// END TEST >>> 0116 <<< END TEST
		// ***************************************************************

		// TEST:0117 GROUP BY column, set functions with several groups!
		rs = stmt.executeQuery("SELECT PNUM,AVG(HOURS),MIN(HOURS),MAX(HOURS) "
				+ "FROM    WORKS " + "GROUP BY PNUM " + "ORDER BY PNUM;");
		//
		// Expected output:
		//
		// PNUM AVG MIN MAX
		// ====== ===================== ============ ============
		//
		// P1 40 40 40
		// P2 35 20 80
		// P3 80 80 80
		// P4 30 20 40
		// P5 46 12 80
		// P6 12 12 12
		//
		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals(40, rs.getInt(2));
		assertEquals(40, rs.getInt(3));
		assertEquals(40, rs.getInt(4));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals(35, rs.getInt(2));
		assertEquals(20, rs.getInt(3));
		assertEquals(80, rs.getInt(4));

		rs.next();
		assertEquals("P3 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));
		assertEquals(80, rs.getInt(3));
		assertEquals(80, rs.getInt(4));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals(30, rs.getInt(2));
		assertEquals(20, rs.getInt(3));
		assertEquals(40, rs.getInt(4));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals(46, rs.getInt(2));
		assertEquals(12, rs.getInt(3));
		assertEquals(80, rs.getInt(4));

		rs.next();
		assertEquals("P6 ", rs.getString(1));
		assertEquals(12, rs.getInt(2));
		assertEquals(12, rs.getInt(3));
		assertEquals(12, rs.getInt(4));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0117 If 6 rows are selected and first PNUM = 'P1'?
		// PASS:0117 and first MAX(HOURS) = 40?

		// END TEST >>> 0117 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_026
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml026.
	//
	// ORIGIN: NIST file sql/dml026.sql
	//	
	public void testDml_026() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		conn.setAutoCommit(false);

		// TEST:0118 Monadic arithmetic operator +!

		rs = stmt.executeQuery("SELECT +MAX(DISTINCT HOURS) " + "FROM WORKS;");
		//
		// Expected output:
		//
		//          MAX
		// ============
		//
		//           80
		//
		rs.next();
		assertEquals(80, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0118 If +MAX(DISTINCT HOURS) = 80?

		// END TEST >>> 0118 <<< END TEST
		// *********************************************************

		// TEST:0119 Monadic arithmetic operator -!

		rs = stmt.executeQuery("SELECT -MAX(DISTINCT HOURS) " + "FROM WORKS;");
		//
		// Expected output:
		//
		// ============
		//
		//          -80
		//
		rs.next();
		assertEquals(-80, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0119 If -MAX(DISTINCT HOURS) = -80?

		// END TEST >>> 0119 <<< END TEST
		// *********************************************************

		// TEST:0120 Value expression with NULL primary IS NULL!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS1 "
				+ "SELECT * FROM WORKS;");
		assertEquals(12, rowCount);
		// PASS:0120 If 12 rows are inserted ?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS1 "
				+ "VALUES('E9','P1',NULL);");
		assertEquals(1, rowCount);
		// PASS:0120 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM "
				+ "FROM WORKS1 WHERE HOURS IS NULL;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E9
		//
		rs.next();
		assertEquals("E9 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0120 If EMPNUM = 'E9'?

		// NOTE:0120 we insert into WORKS from WORKS1

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "SELECT EMPNUM,'P9',20+HOURS " + "FROM WORKS1 "
				+ "WHERE EMPNUM='E9';");
		assertEquals(1, rowCount);
		// PASS:0120 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE EMPNUM='E9';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0120 If count = 1 ?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS IS NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0120 If count = 1 ?

		// restore
		conn.rollback();

		// END TEST >>> 0120 <<< END TEST
		// **********************************************************

		// TEST:0121 Dyadic operators +, -, *, /!

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VTABLE;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0121 If count = 4 ?

		rs = stmt.executeQuery("SELECT +COL1+COL2 - COL3*COL4/COL1 "
				+ "FROM VTABLE " + "WHERE COL1=10;");
		//
		// Expected output:
		//
		// =====================
		//
		//                   -90
		//
		rs.next();
		assertEquals(-90, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0121 If answer is -90?

		// END TEST >>> 0121 <<< END TEST
		// *********************************************************

		// TEST:0122 Divisor shall not be zero!
	}
	public void test_Dml026_DivisorShallNotBeZero() throws SQLException {
		int errorCode;

		BaseTab.setupBaseTab(stmt);
		errorCode = 0;
		try {
			rs = stmt
					.executeQuery("SELECT COL2/COL1+COL3 FROM VTABLE WHERE COL4=3;");
			fail();
			// PASS:0122 If ERROR Number not Divisible by Zero?
		} catch (SQLException sqle) {

		}

	}
	// END TEST >>> 0122 <<< END TEST
	// **********************************************************

	public void test_Dml026_EvaluationOrderOfExpression() throws SQLException {
		int errorCode;
		int rowCount;

		BaseTab.setupBaseTab(stmt);
		// TEST:0123 Evaluation order of expression!

		rs = stmt.executeQuery("SELECT (-COL2+COL1)*COL3 - COL3/COL1 "
				+ "FROM VTABLE " + "WHERE COL4 IS NULL;");
		//
		// Expected output:
		//
		// =====================
		//
		//               8999997
		//
		rs.next();
		assertEquals(8999997, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0123 If Answer is 8999997 (plus or minus 0.5)?

		// END TEST >>> 0123 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_027
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml027.
	//
	// ORIGIN: NIST file sql/dml027.sql
	//	
	public void testDml_027() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0124 UPDATE UNIQUE column (key = key + 1) interim conflict!

		// setup
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE UPUNIQ "
					+ "SET NUMKEY = NUMKEY + 1;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_17 on table STAFF");
			}
		}
		// PASS:0124 If 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*),SUM(NUMKEY) " + "FROM UPUNIQ;");
		//
		// Expected output:
		//
		//        COUNT SUM
		// ============ =====================
		//
		//            6 24
		//
		rs.next();
		assertEquals(6, rs.getInt(1));
		assertEquals(24, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0124 If count = 6 and SUM(NUMKEY) = 24?

		// restore
		conn.rollback();

		// END TEST >>> 0124 <<< END TEST
		// ********************************************************

		// TEST:0125 UPDATE UNIQUE column (key = key + 1) no interim conflit!

		// setup
		rowCount = stmt.executeUpdate("UPDATE UPUNIQ "
				+ "SET NUMKEY = NUMKEY + 1 " + "WHERE NUMKEY >= 4;");
		assertEquals(3, rowCount);
		// PASS:0125 If 3 rows are updated?

		rs = stmt.executeQuery("SELECT COUNT(*),SUM(NUMKEY) " + "FROM UPUNIQ;");
		//
		// Expected output:
		//
		//        COUNT SUM
		// ============ =====================
		//
		//            6 27
		//
		rs.next();
		assertEquals(6, rs.getInt(1));
		assertEquals(27, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0125 If count = 6 and SUM(NUMKEY) = 27?

		// restore
		conn.rollback();

		// END TEST >>> 0125 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_029
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml029.
	//
	// ORIGIN: NIST file sql/dml029.sql
	//	
	public void testDml_029() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");

		conn.setAutoCommit(false);

		// TEST:0129 Double quote work in character string literal!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E8','Yang Ling',15,'Xi''an');");
		assertEquals(1, rowCount);
		// PASS:0129 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT GRADE,CITY " + "FROM STAFF "
				+ "WHERE EMPNUM = 'E8';");
		//
		// Expected output:
		//
		//        GRADE CITY
		// ============ ===============
		//
		//           15 Xi'an
		//
		rs.next();
		assertEquals(15, rs.getInt(1));
		assertEquals("Xi'an          ", rs.getString(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0129 If GRADE = 15 and CITY = 'Xi'an'?

		// restore
		conn.rollback();

		// END TEST >>> 0129 <<< END TEST
		// ************************************************************

		// TEST:0130 Approximate numeric literal <mantissa>E<exponent>!
		rowCount = stmt.executeUpdate("INSERT INTO JJ " + "VALUES(123.456E3);");
		assertEquals(1, rowCount);
		// PASS:0130 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
				+ "WHERE FLOATTEST > 123455 AND FLOATTEST < 123457;");
		//
		// Expected output:
		//
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0130 If count = 1 ?

		// restore
		conn.rollback();

		// END TEST >>> 0130 <<< END TEST
		// ***************************************************************

		// TEST:0131 Approximate numeric literal with negative exponent!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO JJ " + "VALUES(123456E-3);");
		assertEquals(1, rowCount);
		// PASS:0131 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
				+ "WHERE FLOATTEST > 122 AND FLOATTEST < 124;");
		//
		// Expected output:
		//
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0131 If count = 1 ?

		// restore
		conn.rollback();

		// END TEST >>> 0131 <<< END TEST
		// ********************************************************

		// TEST:0182 Approx numeric literal with negative mantissa & exponent!

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO JJ " + "VALUES(-123456E-3);");
		assertEquals(1, rowCount);
		// PASS:0182 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
				+ "WHERE FLOATTEST > -124 AND FLOATTEST < -122;");
		//
		// Expected output:
		//
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0182 If count = 1 ?

		// restore
		conn.rollback();

		// END TEST >>> 0182 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_033
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml033.
	//
	// ORIGIN: NIST file sql/dml033.sql
	//	
	public void testDml_033() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0135 Upper and loer case letters are distinct!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('UPP','low',100);");
		assertEquals(1, rowCount);
		// PASS:0135 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,PNUM " + "FROM WORKS "
				+ "WHERE EMPNUM='UPP' AND PNUM='low';");
		//
		// Expected output:
		//
		// EMPNUM PNUM
		// ====== ======
		//
		// UPP low
		//
		rs.next();
		assertEquals("UPP", rs.getString(1));
		assertEquals("low", rs.getString(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0135 If EMPNUM = 'UPP' and PNUM = 'low'?

		rs = stmt.executeQuery("SELECT EMPNUM,PNUM " + "FROM WORKS "
				+ "WHERE EMPNUM='upp' OR PNUM='LOW';");
		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(0, rowCount);
		// PASS:0135 If 0 rows are selected - out of data?

		// restore
		conn.rollback();
		// END TEST >>> 0135 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_034
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml034.
	//
	// ORIGIN: NIST file sql/dml034.sql
	//
	// TODO: Are the getfloat() delta's acceptable?
	//
	public void testDml_034() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt.executeUpdate("CREATE TABLE GG (REALTEST     REAL);");
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		stmt.executeUpdate("CREATE TABLE II (DOUBLETEST  DOUBLE PRECISION);");
		stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");
		stmt.executeUpdate("CREATE TABLE KK (FLOATTEST  FLOAT(32));");
		stmt.executeUpdate("CREATE TABLE LL (NUMTEST  NUMERIC(13,6));");
		stmt.executeUpdate("CREATE TABLE MM (NUMTEST  NUMERIC);");
		stmt.executeUpdate("CREATE TABLE MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE NN (NUMTEST  NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE OO (NUMTEST  NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE PP (NUMTEST  DECIMAL(13,6));");
		stmt.executeUpdate("CREATE TABLE QQ (NUMTEST  DECIMAL);");
		stmt.executeUpdate("CREATE TABLE RR (NUMTEST  DECIMAL(8));");
		stmt.executeUpdate("CREATE TABLE SS (NUMTEST  DEC(13,6));");

		conn.setAutoCommit(false);

		// TEST:0088 Data type REAL!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO GG "
				+ "VALUES(123.4567E-2);");
		assertEquals(1, rowCount);
		// PASS:0088 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
		//
		// Expected output:
		//
		//       REALTEST
		// ==============
		//
		//      1.2345670
		//
		rs.next();
		assertEquals(1.2345670, rs.getFloat(1), 0.00000005);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0088 If REALTEST = 1.234567 ?
		// PASS:0088 OR is between 1.234562 and 1.234572 ?

		rs = stmt.executeQuery("SELECT * FROM GG "
				+ "WHERE REALTEST > 1.234561 and REALTEST < 1.234573;");
		//
		// Expected output:
		//
		//       REALTEST
		// ==============
		//
		//      1.2345670
		//
		rs.next();
		assertEquals(1.2345670, rs.getFloat(1), 0.00000005);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0088 If 1 row selected?

		// restore
		conn.rollback();

		// END TEST >>> 0088 <<< END TEST
		// ****************************************************************

		// TEST:0090 Data type DOUBLE PRECISION!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO II "
				+ "VALUES(0.123456123456E6);");
		assertEquals(1, rowCount);
		// PASS:0090 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
		//
		// Expected output:
		//
		//              DOUBLETEST
		// =======================
		//
		//       123456.1234560000
		//
		rs.next();
		assertEquals(123456.1234560000, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0090 If DOUBLETEST = 123456.123456 ?
		// PASS:0090 OR is between 123456.123451 and 123456.123461 ?

		rs = stmt
				.executeQuery("SELECT * FROM II "
						+ "WHERE DOUBLETEST > 123456.123450 and DOUBLETEST < 123456.123462;");
		//
		// Expected output:
		//
		//              DOUBLETEST
		// =======================
		//
		//       123456.1234560000
		//
		rs.next();
		assertEquals(123456.1234560000, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0090 If 1 row selected?

		// restore
		conn.rollback();

		// END TEST >>> 0090 <<< END TEST
		// ***********************************************************

		// TEST:0091 Data type FLOAT!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(12.345678);");
		assertEquals(1, rowCount);
		// PASS:0091 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
		//
		// Expected output:
		//
		//      FLOATTEST
		// ==============
		//
		//      12.345678
		//
		rs.next();
		assertEquals(12.345678, rs.getFloat(1), 0.0000005);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0091 If FLOATTEST = 12.345678 ?
		// PASS:0091 OR is between 12.345673 and 12.345683 ?

		rs = stmt.executeQuery("SELECT * FROM JJ "
				+ "WHERE FLOATTEST > 12.345672 and FLOATTEST < 12.345684;");
		//
		// Expected output:
		//
		//      FLOATTEST
		// ==============
		//
		//      12.345678
		//
		rs.next();
		assertEquals(12.345678, rs.getFloat(1), 0.0000005);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0091 If 1 row selected?

		// restore
		conn.rollback();

		// END TEST >>> 0091 <<< END TEST
		// **********************************************************

		// TEST:0092 Data type FLOAT(32)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO KK "
				+ "VALUES(123.456123456E+3);");
		assertEquals(1, rowCount);
		// PASS:0092 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT FLOATTEST FROM KK;");
		//
		// Expected output:
		//
		//               FLOATTEST
		// =======================
		//
		//       123456.1234560000
		//
		rs.next();
		assertEquals(123456.12345600, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0092 If FLOATTEST = 123456.123456 ?
		// PASS:0092 OR is between 123456.1233 and 123456.1236 ?

		rs = stmt
				.executeQuery("SELECT * FROM KK "
						+ "WHERE FLOATTEST > 123456.123450 and FLOATTEST < 123456.123462;");
		//
		// Expected output:
		//
		//               FLOATTEST
		// =======================
		//
		//       123456.1234560000
		//
		rs.next();
		assertEquals(123456.12345600, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0092 If 1 row selected?

		// restore
		conn.rollback();

		// END TEST >>> 0092 <<< END TEST
		// *************************************************************

		// TEST:0093 Data type NUMERIC(13,6)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO LL VALUES(123456.123456);");
		assertEquals(1, rowCount);
		// PASS:0093 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM LL;");
		//
		// Expected output:
		//
		//               NUMTEST
		// =====================
		//
		//         123456.123456
		//
		rs.next();
		assertEquals(123456.123456, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0093 If NUMTEST = 123456.123456 ?
		// PASS:0093 OR is between 123456.123451 and 123456.123461 ?

		rs = stmt.executeQuery("SELECT * FROM LL "
				+ "WHERE NUMTEST > 123456.123450 and NUMTEST < 123456.123462;");
		//
		// Expected output:
		//
		//               NUMTEST
		// =====================
		//
		//         123456.123456
		//
		rs.next();
		assertEquals(123456.123456, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0093 If 1 row selected?

		// restore
		conn.rollback();

		// END TEST >>> 0093 <<< END TEST
		// *************************************************************

		// TEST:0094 Data type DECIMAL(13,6)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO PP "
				+ "VALUES(123456.123456);");
		assertEquals(1, rowCount);
		// PASS:0094 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM PP;");
		//
		// Expected output:
		//
		//               NUMTEST
		// =====================
		//
		//         123456.123456
		//
		rs.next();
		assertEquals(123456.123456, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0094 If NUMTEST = 123456.123456 ?
		// PASS:0094 OR is between 123456.123451 and 123456.123461 ?

		// restore
		conn.rollback();

		// END TEST >>> 0094 <<< END TEST
		// **************************************************************

		// TEST:0095 Data type DEC(13,6)!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO SS "
				+ "VALUES(123456.123456);");
		assertEquals(1, rowCount);
		// PASS:0095 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM SS;");
		//
		// Expected output:
		//
		//               NUMTEST
		// =====================
		//
		//         123456.123456
		//
		rs.next();
		assertEquals(123456.123456, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0095 If NUMTEST = 123456.123456 ?
		// PASS:0095 OR is between 123456.123451 and 123456.123461 ?

		// restore
		conn.rollback();

		// END TEST >>> 0095 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_035
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml035.
	//
	// ORIGIN: NIST file sql/dml035.sql
	//
	// TODO: Are the getfloat() delta's acceptable?
	//
	public void testDml_035() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");
		stmt.executeUpdate("CREATE TABLE KK (FLOATTEST  FLOAT(32));");
		stmt.executeUpdate("CREATE TABLE LL (NUMTEST  NUMERIC(13,6));");
		stmt.executeUpdate("CREATE TABLE MM (NUMTEST  NUMERIC);");
		stmt.executeUpdate("CREATE TABLE MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE NN (NUMTEST  NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE OO (NUMTEST  NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE PP (NUMTEST  DECIMAL(13,6));");
		stmt.executeUpdate("CREATE TABLE QQ (NUMTEST  DECIMAL);");
		stmt.executeUpdate("CREATE TABLE RR (NUMTEST  DECIMAL(8));");
		stmt.executeUpdate("CREATE TABLE SS (NUMTEST  DEC(13,6));");

		conn.setAutoCommit(false);

		// TEST:0157 ORDER BY approximate numeric!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(66.2);");
		assertEquals(1, rowCount);
		// PASS:0157 If 1 row is inserted?
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(-44.5);");
		assertEquals(1, rowCount);
		// PASS:0157 If 1 row is inserted?
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(0.2222);");
		assertEquals(1, rowCount);
		// PASS:0157 If 1 row is inserted?
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(66.3);");
		assertEquals(1, rowCount);
		// PASS:0157 If 1 row is inserted?
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(-87);");
		assertEquals(1, rowCount);
		// PASS:0157 If 1 row is inserted?
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(-66.25);");
		assertEquals(1, rowCount);
		// PASS:0157 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ "
				+ "ORDER BY FLOATTEST DESC;");
		//
		// Expected output:
		//
		// FLOATTEST
		// ==============
		//
		//      66.300003
		//      66.199997
		//     0.22220001
		//     -44.500000
		//     -66.250000
		//     -87.000000
		//
		rs.next();
		assertEquals(66.300003, rs.getFloat(1), 0.0000001);

		rs.next();
		assertEquals(66.199997, rs.getFloat(1), 0.0000001);

		rs.next();
		assertEquals(0.22220001, rs.getFloat(1), 0.00000001);

		rs.next();
		assertEquals(-44.500000, rs.getFloat(1), 0.0);

		rs.next();
		assertEquals(-66.250000, rs.getFloat(1), 0.0);

		rs.next();
		assertEquals(-87.000000, rs.getFloat(1), 0.0);

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0157 If 6 rows are selected ?
		// PASS:0157 If last FLOATTEST = -87 OR is between -87.5 and -86.5 ?

		// restore
		conn.rollback();

		// END TEST >>> 0157 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_037
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml037.
	//
	// ORIGIN: NIST file sql/dml037.sql
	//
	public void testDml_037() throws SQLException {
		int errorCode; // Error code.
		int tmpint[];
		int rowCount; // Row count.

		stmt.executeUpdate("CREATE TABLE TEXT240  (TEXXT CHAR(240));");
		conn.setAutoCommit(false);

		// NO_TEST:0202 Host variable names same as column name!

		// Testing host identifier

		// ***********************************************************

		// TEST:0234 SQL-style comments with SQL statements!
		// OPTIONAL TEST

		rowCount = stmt.executeUpdate("DELETE  -- we empty the table \n"
				+ "FROM TEXT240;");

		rowCount = stmt
				.executeUpdate("INSERT INTO TEXT240   -- This is the test for the rules \n"
						+ "VALUES         -- for the placement \n"
						+ "('SQL-STYLE COMMENTS') -- of \n"
						+ "-- SQL-style comments \n" + ";");
		assertEquals(1, rowCount);
		// PASS:0234 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * " + "FROM TEXT240;");
		//
		// Expected output:
		//
		// TEXXT
		// ===============================================================================
		//
		// SQL-STYLE COMMENTS
		//
		rs.next();
		assertEquals(
				"SQL-STYLE COMMENTS                                                                                                                                                                                                                              ",
				rs.getString(1));
		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0234 If TEXXT = 'SQL-STYLE COMMENTS'?

		// restore
		conn.rollback();

		// END TEST >>> 0234 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_038
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml038.
	//
	// ORIGIN: NIST file sql/dml038.sql
	//
	public void testDml_038() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);

		// TEST:0205 Cartesian product is produced without WHERE clause!

		rs = stmt.executeQuery("SELECT GRADE, HOURS, BUDGET "
				+ "FROM STAFF, WORKS, PROJ;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		assertEquals(40, rs.getInt(2));
		assertEquals(10000, rs.getInt(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(360, rowCount);
		// PASS:0205 If 360 rows are selected ?

		// END TEST >>> 0205 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_039
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml039.
	//
	// ORIGIN: NIST file sql/dml039.sql
	//
	public void testDml_039() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);

		// TEST:0208 Upper and lower case in LIKE predicate!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E7', 'yanping',26,'China');");
		assertEquals(1, rowCount);
		// PASS:0208 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E8','YANPING',30,'NIST');");
		assertEquals(1, rowCount);
		// PASS:0208 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CITY " + "FROM STAFF "
				+ "WHERE EMPNAME LIKE 'yan____%';");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// China
		//
		rs.next();
		assertEquals("China          ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0208 If CITY = 'China'?

		rs = stmt.executeQuery("SELECT CITY " + "FROM STAFF "
				+ "WHERE EMPNAME LIKE 'YAN____%';");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// NIST
		//
		rs.next();
		assertEquals("NIST           ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0208 If CITY = 'NIST'?

		// END TEST >>> 0208 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_040
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml040.
	//
	// ORIGIN: NIST file sql/dml040.sql
	//
	public void testDml_040() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0209 Join 2 tables from different schemas!

		/**
		 * TODO: This test hangs Firebird/Vulcan. // setup rowCount =
		 * stmt.executeUpdate("INSERT INTO VTABLE " + "SELECT * " + "FROM
		 * VTABLE;");
		 * 
		 * rs = stmt.executeQuery("SELECT COL1, EMPNUM, GRADE " + "FROM VTABLE,
		 * STAFF " + "WHERE COL1 < 200 AND GRADE > 12;"); // PASS:0209 If 6 rows
		 * are selected and SUM(COL1) = 220?
		 */

		// END TEST >>> 0209 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/*
	 * 
	 * testDml_041_EnforcementOfCheckClauseInNestedViews()
	 * 
	 * TEST:0212 Enforcement of CHECK clause in nested views!
	 *  
	 */
	public void testDml_041_EnforcementOfCheckClauseInNestedViews()
			throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE VIEW V_WORKS1 " + "AS SELECT * FROM WORKS "
				+ "WHERE HOURS > 15 " + "WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE VIEW V_WORKS2 "
				+ "AS SELECT * FROM V_WORKS1 " + "WHERE EMPNUM = 'E1' "
				+ "OR EMPNUM = 'E6';");

		stmt.executeUpdate("CREATE VIEW V_WORKS3 "
				+ "AS SELECT * FROM V_WORKS2 " + "WHERE PNUM = 'P2' "
				+ "OR PNUM = 'P7' " + "WITH CHECK OPTION;");

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO V_WORKS2 "
					+ "VALUES('E9','P7',13);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table V_WORKS1");
			}
		}
		// PASS:0212 If ERROR, view check constraint, 0 rows inserted?

		stmt.executeUpdate("INSERT INTO V_WORKS2 " + "VALUES('E7','P4',95);");
		// PASS:0212 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO V_WORKS3 " + "VALUES('E8','P2',85);");
		// PASS:0212 If either 1 row is inserted OR ?
		// PASS:0212 If ERROR, view check constraint, 0 rows inserted?
		// NOTE:0212 Vendor interpretation follows
		// NOTE:0212 Insertion of row means: outer check option does not imply
		// NOTE:0212 inner check options

		// NOTE:0212 Failure to insert means: outer check option implies
		// NOTE:0212 inner check options

		stmt.executeUpdate("INSERT INTO V_WORKS3 " + "VALUES('E1','P7',90);");
		// PASS:0212 If 1 row is inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO V_WORKS3 "
					+ "VALUES('E9','P2',10);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table V_WORKS1");
			}
		}
		// PASS:0212 If ERROR, view check constraint, 0 rows inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE EMPNUM = 'E9';");
		rs.next();
		assertEquals(0, rs.getInt(1));

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS > 85;");
		rs.next();
		assertEquals(2, rs.getInt(1));
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_042
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml042.
	//
	// ORIGIN: NIST file sql/dml042.sql
	//
	public void testDml_042() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt
				.executeUpdate("CREATE TABLE T100(C1 CHAR(2),C2 CHAR(2),C3 CHAR(2),C4 CHAR(2), "
						+ "C5 CHAR(2),C6 CHAR(2),C7 CHAR(2),C8 CHAR(2), "
						+ "C9 CHAR(2),C10 CHAR(2),C11 CHAR(2),C12 CHAR(2), "
						+ "C13 CHAR(2),C14 CHAR(2),C15 CHAR(2),C16 CHAR(2), "
						+ "C17 CHAR(2),C18 CHAR(2),C19 CHAR(2),C20 CHAR(2), "
						+ "C21 CHAR(2),C22 CHAR(2),C23 CHAR(2),C24 CHAR(2), "
						+ "C25 CHAR(2),C26 CHAR(2),C27 CHAR(2),C28 CHAR(2), "
						+ "C29 CHAR(2),C30 CHAR(2),C31 CHAR(2),C32 CHAR(2), "
						+ "C33 CHAR(2),C34 CHAR(2),C35 CHAR(2),C36 CHAR(2), "
						+ "C37 CHAR(2),C38 CHAR(2),C39 CHAR(2),C40 CHAR(2), "
						+ "C41 CHAR(2),C42 CHAR(2),C43 CHAR(2),C44 CHAR(2), "
						+ "C45 CHAR(2),C46 CHAR(2),C47 CHAR(2),C48 CHAR(2), "
						+ "C49 CHAR(2),C50 CHAR(2),C51 CHAR(2),C52 CHAR(2), "
						+ "C53 CHAR(2),C54 CHAR(2),C55 CHAR(2),C56 CHAR(2), "
						+ "C57 CHAR(2),C58 CHAR(2),C59 CHAR(2),C60 CHAR(2), "
						+ "C61 CHAR(2),C62 CHAR(2),C63 CHAR(2),C64 CHAR(2), "
						+ "C65 CHAR(2),C66 CHAR(2),C67 CHAR(2),C68 CHAR(2), "
						+ "C69 CHAR(2),C70 CHAR(2),C71 CHAR(2),C72 CHAR(2), "
						+ "C73 CHAR(2),C74 CHAR(2),C75 CHAR(2),C76 CHAR(2), "
						+ "C77 CHAR(2),C78 CHAR(2),C79 CHAR(2),C80 CHAR(2), "
						+ "C81 CHAR(2),C82 CHAR(2),C83 CHAR(2),C84 CHAR(2), "
						+ "C85 CHAR(2),C86 CHAR(2),C87 CHAR(2),C88 CHAR(2), "
						+ "C89 CHAR(2),C90 CHAR(2),C91 CHAR(2),C92 CHAR(2), "
						+ "C93 CHAR(2),C94 CHAR(2),C95 CHAR(2),C96 CHAR(2), "
						+ "C97 CHAR(2),C98 CHAR(2),C99 CHAR(2),C100 CHAR(2));");

		// TEST:0213 FIPS sizing -- 100 columns in a row!
		// FIPS sizing TEST

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO T100(C1,C21,C41,C61,C81,C100) "
						+ "VALUES(' 1','21','41','61','81','00');");
		assertEquals(1, rowCount);
		// PASS:0213 If 1 row is inserted?

		rs = stmt
				.executeQuery("SELECT C1,C21,C41,C61,C81,C100 " + "FROM T100;");
		//
		// Expected output:
		//
		// C1 C21 C41 C61 C81 C100
		// ====== ====== ====== ====== ====== ======
		//
		// 1 21 41 61 81 00
		//
		rs.next();
		assertEquals(" 1", rs.getString(1));
		assertEquals("21", rs.getString(2));
		assertEquals("41", rs.getString(3));
		assertEquals("61", rs.getString(4));
		assertEquals("81", rs.getString(5));
		assertEquals("00", rs.getString(6));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0213 If C1 = ' 1' and C100 = '00' ?

		// restore

		// END TEST >>> 0213 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_043
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml043.
	//
	// ORIGIN: NIST file sql/dml043.sql
	//
	public void testDml_043() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt
				.executeUpdate("CREATE TABLE T2000(STR110 CHAR(110),STR120 CHAR(120), "
						+ "STR130 CHAR(130),STR140 CHAR(140), "
						+ "STR150 CHAR(150),STR160 CHAR(160), "
						+ "STR170 CHAR(170),STR180 CHAR(180), "
						+ "STR190 CHAR(190),STR200 CHAR(200), "
						+ "STR210 CHAR(210),STR216 CHAR(216));");
		// TEST:0214 FIPS sizing -- 2000-byte row!
		// FIPS sizing TEST

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO T2000(STR110,STR200,STR216) "
						+ "VALUES ('STR11111111111111111111111111111111111111111111111', "
						+ "'STR22222222222222222222222222222222222222222222222', "
						+ "'STR66666666666666666666666666666666666666666666666');");
		assertEquals(1, rowCount);
		// PASS:0214 If 1 row is inserted?

		rowCount = stmt.executeUpdate("UPDATE T2000 " + "SET STR140 = "
				+ "'STR44444444444444444444444444444444444444444444444';");
		assertEquals(1, rowCount);
		// PASS:0214 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE T2000 " + "SET STR180 = "
				+ "'STR88888888888888888888888888888888888888888888888';");
		assertEquals(1, rowCount);
		// PASS:0214 If 1 row is updated?

		rs = stmt.executeQuery("SELECT STR110,STR180,STR216 " + "FROM T2000;");
		//
		// Expected output:
		//
		// STR110 STR180 STR216
		// ===============================================================================
		// ===============================================================================
		// ===============================================================================
		//
		// STR11111111111111111111111111111111111111111111111
		// STR88888888888888888888888888888888888888888888888
		// STR66666666666666666666666666666666666666666666666
		//
		//
		rs.next();
		assertEquals(
				"STR11111111111111111111111111111111111111111111111                                                            ",
				rs.getString(1));
		assertEquals(
				"STR88888888888888888888888888888888888888888888888                                                                                                                                  ",
				rs.getString(2));
		assertEquals(
				"STR66666666666666666666666666666666666666666666666                                                                                                                                                                      ",
				rs.getString(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0214 If STR180 = ?
		// PASS:0214 'STR88888888888888888888888888888888888888888888888'?
		// PASS:0214 If STR216 = ?
		// PASS:0214 'STR66666666666666666666666666666666666666666666666'?

		// END TEST >>> 0214 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_044
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml044.
	//
	// ORIGIN: NIST file sql/dml044.sql
	//
	public void testDml_044() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0215 FIPS sizing -- 6 columns in a UNIQUE constraint!
		// FIPS sizing TEST

		stmt
				.executeUpdate("CREATE TABLE T8(COL1 CHAR(2) NOT NULL,COL2 CHAR(4) NOT NULL, "
						+ "COL3 CHAR(6) NOT NULL,COL4 CHAR(8) NOT NULL, "
						+ "COL5 CHAR(10) NOT NULL,COL6 CHAR(12) NOT NULL, "
						+ "COL7 CHAR(14),COL8 CHAR(16), "
						+ "UNIQUE(COL1,COL2,COL3,COL4,COL5,COL6));");

		rowCount = stmt.executeUpdate("INSERT INTO T8 "
				+ "VALUES('th','seco','third3','fourth_4','fifth_colu', "
				+ "'sixth_column','seventh_column','last_column_of_t');");
		assertEquals(1, rowCount);
		// PASS:0215 If 1 row is inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO T8 "
					+ "VALUES('th','seco','third3','fourth_4','fifth_colu', "
					+ "'sixth_column','column_seventh','column_eighth_la');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_24 on table T8");
			}
		}
		// PASS:0215 If ERROR, unique constraint, 0 rows inserted?

		rs = stmt
				.executeQuery("SELECT COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8 "
						+ "FROM T8;");
		//
		// Expected output:
		//
		// COL1 COL2 COL3 COL4 COL5 COL6 COL7 COL8
		// ====== ====== ====== ======== ========== ============ ==============
		// ================
		//
		// th seco third3 fourth_4 fifth_colu sixth_column seventh_column
		// last_column_of_t
		//
		rs.next();
		assertEquals("th", rs.getString(1));
		assertEquals("seco", rs.getString(2));
		assertEquals("third3", rs.getString(3));
		assertEquals("fourth_4", rs.getString(4));
		assertEquals("fifth_colu", rs.getString(5));
		assertEquals("sixth_column", rs.getString(6));
		assertEquals("seventh_column", rs.getString(7));
		assertEquals("last_column_of_t", rs.getString(8));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0215 If COL1 = 'th'?

		// END TEST >>> 0215 <<< END TEST
		// **************************************************************

		// TEST:0216 FIPS sizing -- 120 bytes in a UNIQUE constraint!
		// FIPS sizing TEST

		stmt.executeUpdate("CREATE TABLE T4(STR110 CHAR(110) NOT NULL, "
				+ "NUM6   NUMERIC(6) NOT NULL, "
				+ "COL3   CHAR(10),COL4 CHAR(20), " + "UNIQUE(STR110,NUM6));");

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO T4 VALUES "
						+ "('This test is trying to test the limit on the total length of an index', "
						+ "-123456, 'which is','not less than 120');");
		assertEquals(1, rowCount);
		// PASS:0216 If 1 row is inserted?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO T4 VALUES "
							+ "('This test is trying to test the limit on the total length of an index', "
							+ "-123456,'which is','not less than 120');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_29 on table T4");
			}
		}
		// PASS:0216 If ERROR, unique constraint, 0 rows inserted?

		rs = stmt.executeQuery("SELECT STR110 FROM T4;");
		//
		// Expected output:
		//
		// STR110
		// ===============================================================================
		//
		// This test is trying to test the limit on the total length of an index
		//
		rs.next();
		assertEquals(
				"This test is trying to test the limit on the total length of an index                                         ",
				rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0216 If STR110 starts with 'This test is trying to test the '?
		// PASS:0216 and ends with 'limit on the total length of an index'?

		// END TEST >>> 0216 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_045
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml045.
	//
	// ORIGIN: NIST file sql/dml045.sql
	//
	public void testDml_045() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt.executeUpdate("CREATE TABLE T12(COL1 CHAR(1), COL2 CHAR(2), "
				+ "COL3 CHAR(4), COL4 CHAR(6), "
				+ "COL5 CHAR(8), COL6 CHAR(10), "
				+ "COL7 CHAR(20), COL8 CHAR(30), "
				+ "COL9 CHAR(40), COL10 CHAR(50), "
				+ "COL11 INTEGER, COL12 INTEGER);");

		conn.setAutoCommit(false);

		// TEST:0218 FIPS sizing -- 6 columns in GROUP BY!
		// FIPS sizing TEST

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888888','0101010101', "
				+ "'2020...20','3030...30','4040...40','5050...50',44,48);");
		assertEquals(1, rowCount);
		// PASS:0218 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888888','1010101010', "
				+ "'2020...20','3030...30','4040...40','5050...50',11,12);");
		assertEquals(1, rowCount);
		// PASS:0218 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888888','1010101010', "
				+ "'2020...20','3030...30','4040...40','5050...50',22,24);");
		assertEquals(1, rowCount);
		// PASS:0218 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888888','0101010101', "
				+ "'2020...20','3030...30','4040...40','5050...50',33,36);");
		assertEquals(1, rowCount);
		// PASS:0218 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0218 If count = 4?

		rs = stmt.executeQuery("SELECT COL6,SUM(COL11),MAX(COL12) "
				+ "FROM T12 " + "GROUP BY COL1,COL5,COL3,COL6,COL2,COL4 "
				+ "ORDER BY COL6 DESC;");
		//
		// Expected output:
		//
		// COL6 SUM MAX
		// ========== ===================== ============
		//
		// 1010101010 33 24
		// 0101010101 77 48
		//
		rs.next();
		assertEquals("1010101010", rs.getString(1));
		assertEquals(33, rs.getInt(2));
		assertEquals(24, rs.getInt(3));

		rs.next();
		assertEquals("0101010101", rs.getString(1));
		assertEquals(77, rs.getInt(2));
		assertEquals(48, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0218 If 2 rows are selected and second COL6 = 0101010101 and ?
		// PASS:0218 second SUM(COL11) = 77 and second MAX(COL12) = 48?

		// restore
		conn.rollback();

		// END TEST >>> 0218 <<< END TEST
		// ****************************************************************

		// TEST:0219 FIPS sizing -- 120 bytes in GROUP BY!
		// FIPS sizing TEST

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4444','666666','88888888','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',111,112);");
		assertEquals(1, rowCount);
		// PASS:0219 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4444','666666','88888889','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',333,336);");
		assertEquals(1, rowCount);
		// PASS:0219 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4444','666666','88888889','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',444,448);");
		assertEquals(1, rowCount);
		// PASS:0219 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4444','666666','88888888','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',222,224);");
		// PASS:0219 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0219 If count = 4?

		rs = stmt.executeQuery("SELECT COL5,SUM(COL11),MAX(COL12) "
				+ "FROM T12 " + "GROUP BY COL9,COL5,COL7,COL4,COL3,COL8 "
				+ "ORDER BY COL5 DESC;");
		//
		// Expected output:
		//
		// COL5 SUM MAX
		// ======== ===================== ============
		//
		// 88888889 777 448
		// 88888888 333 224
		//
		rs.next();
		assertEquals("88888889", rs.getString(1));
		assertEquals(777, rs.getInt(2));
		assertEquals(448, rs.getInt(3));

		rs.next();
		assertEquals("88888888", rs.getString(1));
		assertEquals(333, rs.getInt(2));
		assertEquals(224, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0219 If 2 rows are selected ?
		// PASS:0219 If row #1 COL5=88888889, SUM(COL11)=777, MAX(COL12)=448?
		// PASS:0219 If row #2 COL5=88888888, SUM(COL11)=333, MAX(COL12)=224?

		// restore
		conn.rollback();

		// END TEST >>> 0219 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_046
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml046.
	//
	// ORIGIN: NIST file sql/dml046.sql
	//
	public void testDml_046() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt.executeUpdate("CREATE TABLE T12(COL1 CHAR(1), COL2 CHAR(2), "
				+ "COL3 CHAR(4), COL4 CHAR(6), "
				+ "COL5 CHAR(8), COL6 CHAR(10), "
				+ "COL7 CHAR(20), COL8 CHAR(30), "
				+ "COL9 CHAR(40), COL10 CHAR(50), "
				+ "COL11 INTEGER, COL12 INTEGER);");

		conn.setAutoCommit(false);

		// TEST:0220 FIPS sizing -- 6 column in ORDER BY!
		// FIPS sizing TEST

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888884','1010101010', "
				+ "'2020...20','3030...30','4040...40','5050...50',11,12);");
		assertEquals(1, rowCount);
		// PASS:0220 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888883','1010101010', "
				+ "'2020...20','3030...30','4040...40','5050...50',22,24);");
		assertEquals(1, rowCount);
		// PASS:0220 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888882','0101010101', "
				+ "'2020...20','3030...30','4040...40','5050...50',33,36);");
		assertEquals(1, rowCount);
		// PASS:0220 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO T12 "
				+ "VALUES('1','22','4444','666666','88888881','0101010101', "
				+ "'2020...20','3030...30','4040...40','5050...50',44,48);");
		assertEquals(1, rowCount);
		// PASS:0220 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0220 If count = 4?

		rs = stmt.executeQuery("SELECT COL5,COL6,COL11,COL3,COL4,COL7,COL8 "
				+ "FROM T12 " + "ORDER BY COL7,COL8,COL3,COL4,COL6,COL5 DESC;");
		//
		// Expected output:
		//
		// COL5 COL6 COL11 COL3 COL4 COL7 COL8
		// ======== ========== ============ ====== ====== ====================
		// ==============================
		//
		// 88888882 0101010101 33 4444 666666 2020...20 3030...30
		// 88888881 0101010101 44 4444 666666 2020...20 3030...30
		// 88888884 1010101010 11 4444 666666 2020...20 3030...30
		// 88888883 1010101010 22 4444 666666 2020...20 3030...30
		//
		rs.next();
		assertEquals("88888882", rs.getString(1));
		assertEquals("0101010101", rs.getString(2));
		assertEquals(33, rs.getInt(3));
		assertEquals("4444", rs.getString(4));
		assertEquals("666666", rs.getString(5));
		assertEquals("2020...20           ", rs.getString(6));
		assertEquals("3030...30                     ", rs.getString(7));

		rs.next();
		assertEquals("88888881", rs.getString(1));
		assertEquals("0101010101", rs.getString(2));
		assertEquals(44, rs.getInt(3));
		assertEquals("4444", rs.getString(4));
		assertEquals("666666", rs.getString(5));
		assertEquals("2020...20           ", rs.getString(6));
		assertEquals("3030...30                     ", rs.getString(7));

		rs.next();
		assertEquals("88888884", rs.getString(1));
		assertEquals("1010101010", rs.getString(2));
		assertEquals(11, rs.getInt(3));
		assertEquals("4444", rs.getString(4));
		assertEquals("666666", rs.getString(5));
		assertEquals("2020...20           ", rs.getString(6));
		assertEquals("3030...30                     ", rs.getString(7));

		rs.next();
		assertEquals("88888883", rs.getString(1));
		assertEquals("1010101010", rs.getString(2));
		assertEquals(22, rs.getInt(3));
		assertEquals("4444", rs.getString(4));
		assertEquals("666666", rs.getString(5));
		assertEquals("2020...20           ", rs.getString(6));
		assertEquals("3030...30                     ", rs.getString(7));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0220 If 4 rows are selected and first row?
		// PASS:0220 COL5 = 88888882, COL6 = 0101010101 and COL11 = 33?

		// restore
		conn.rollback();

		// END TEST >>> 0220 <<< END TEST
		// **************************************************************

		// TEST:0221 FIPS sizing -- 120 bytes in ORDER BY!
		// FIPS sizing TEST

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4442','666666','88888888','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',111,112);");
		assertEquals(1, rowCount);
		// PASS:0221 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4443','666666','88888888','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',222,224);");
		assertEquals(1, rowCount);
		// PASS:0221 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4441','666666','88888888','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',333,336);");
		assertEquals(1, rowCount);
		// PASS:0221 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("INSERT INTO T12 "
						+ "VALUES('1','22','4444','666666','88888888','1010101010', "
						+ "'20202020202020202020','303030303030303030303030303030', "
						+ "'4040404040404040404040404040404040404040', '5050...50',444,448);");
		assertEquals(1, rowCount);
		// PASS:0221 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0221 If count = 4?

		rs = stmt.executeQuery("SELECT COL3,COL11,COL9,COL8,COL7,COL5,COL4 "
				+ "FROM T12 " + "ORDER BY COL9,COL8,COL7,COL5,COL4,COL3;");
		//
		// Expected output:
		//
		// COL3 COL11 COL9 COL8 COL7 COL5 COL4
		// ====== ============ ========================================
		// ============================== ==================== ======== ======
		//
		// 4441 333 4040404040404040404040404040404040404040
		// 303030303030303030303030303030 20202020202020202020 88888888 666666
		// 4442 111 4040404040404040404040404040404040404040
		// 303030303030303030303030303030 20202020202020202020 88888888 666666
		// 4443 222 4040404040404040404040404040404040404040
		// 303030303030303030303030303030 20202020202020202020 88888888 666666
		// 4444 444 4040404040404040404040404040404040404040
		// 303030303030303030303030303030 20202020202020202020 88888888 666666
		//
		rs.next();
		assertEquals("4441", rs.getString(1));
		assertEquals(333, rs.getInt(2));
		assertEquals("4040404040404040404040404040404040404040", rs
				.getString(3));
		assertEquals("303030303030303030303030303030", rs.getString(4));
		assertEquals("20202020202020202020", rs.getString(5));
		assertEquals("88888888", rs.getString(6));
		assertEquals("666666", rs.getString(7));

		rs.next();
		assertEquals("4442", rs.getString(1));
		assertEquals(111, rs.getInt(2));
		assertEquals("4040404040404040404040404040404040404040", rs
				.getString(3));
		assertEquals("303030303030303030303030303030", rs.getString(4));
		assertEquals("20202020202020202020", rs.getString(5));
		assertEquals("88888888", rs.getString(6));
		assertEquals("666666", rs.getString(7));

		rs.next();
		assertEquals("4443", rs.getString(1));
		assertEquals(222, rs.getInt(2));
		assertEquals("4040404040404040404040404040404040404040", rs
				.getString(3));
		assertEquals("303030303030303030303030303030", rs.getString(4));
		assertEquals("20202020202020202020", rs.getString(5));
		assertEquals("88888888", rs.getString(6));
		assertEquals("666666", rs.getString(7));

		rs.next();
		assertEquals("4444", rs.getString(1));
		assertEquals(444, rs.getInt(2));
		assertEquals("4040404040404040404040404040404040404040", rs
				.getString(3));
		assertEquals("303030303030303030303030303030", rs.getString(4));
		assertEquals("20202020202020202020", rs.getString(5));
		assertEquals("88888888", rs.getString(6));
		assertEquals("666666", rs.getString(7));
		// PASS:0221 If 4 rows are selected ?
		// PASS:0221 If first row COL3 = 4441 and COL11 = 333?

		// restore
		conn.rollback();

		// END TEST >>> 0221 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_047
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml047.
	//
	// ORIGIN: NIST file sql/dml047.sql
	//
	public void testDml_047() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0222 FIPS sizing -- Length(240) of a character string!
		// FIPS sizing TEST
		// NOTE:0222 Literal length is only 78

		stmt.executeUpdate("CREATE TABLE T240(STR240 CHAR(240));");

		rowCount = stmt
				.executeUpdate("INSERT INTO T240 "
						+ "VALUES('Now is the time for all good men and women to come to the aid of their country');");
		assertEquals(1, rowCount);
		// PASS:0222 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT * FROM T240;");
		rs.next();
		assertEquals(
				"Now is the time for all good men and women to come to the aid of their country                                                                                                                                                                  ",
				rs.getString(1));
		assertFalse(rs.next());
		// PASS:0222 If STR240 starts with 'Now is the time for all good men'?
		// PASS:0222 and ends 'and women to come to the aid of their country'?

		// END TEST >>> 0222 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_049
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml049.
	//
	// ORIGIN: NIST file sql/dml049.sql
	//
	public void testDml_049() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);

		stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");

		stmt
				.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
						+ "PNAME  CHAR(20), "
						+ "PTYPE  CHAR(6), "
						+ "BUDGET DECIMAL(9), " + "CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		stmt.executeUpdate("CREATE TABLE STAFF4 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");

		conn.setAutoCommit(false);

		// TEST:0225 FIPS sizing -- ten tables in FROM clause!
		// FIPS sizing TEST

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "SELECT EMPNUM,GRADE,CITY " + "FROM STAFF "
				+ "WHERE GRADE > 11;");
		assertEquals(4, rowCount);
		// PASS:0225 If 4 rows are inserted ?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 " + "SELECT * "
				+ "FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0225 If 5 rows are inserted?

		rowCount = stmt.executeUpdate("INSERT INTO WORKS1 " + "SELECT * "
				+ "FROM WORKS;");
		assertEquals(12, rowCount);
		// PASS:0225 If 12 rows are inserted?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF4 " + "SELECT * "
				+ "FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0225 If 5 rows are inserted?

		rowCount = stmt.executeUpdate("INSERT INTO PROJ1 " + "SELECT * "
				+ "FROM PROJ;");
		assertEquals(6, rowCount);
		// PASS:0225 If 6 rows are inserted?

		rs = stmt.executeQuery("SELECT STAFF.EMPNUM,PROJ.PNUM,WORKS.HOURS, "
				+ "STAFF3.GRADE,STAFF4.CITY,WORKS1.HOURS, "
				+ "TEMP_S.GRADE,PROJ1.PNUM,STAFF1.GRADE, " + "UPUNIQ.COL2 "
				+ "FROM   STAFF,PROJ,WORKS,STAFF3,STAFF4,WORKS1, "
				+ "TEMP_S,PROJ1,STAFF1,UPUNIQ "
				+ "WHERE  STAFF.EMPNUM = WORKS.EMPNUM    AND "
				+ "PROJ.PNUM = WORKS.PNUM         AND "
				+ "STAFF3.EMPNUM = WORKS.EMPNUM   AND "
				+ "STAFF4.EMPNUM = WORKS.EMPNUM   AND "
				+ "WORKS1.EMPNUM = WORKS.EMPNUM   AND "
				+ "WORKS1.PNUM = WORKS.PNUM       AND "
				+ "TEMP_S.EMPNUM = WORKS.EMPNUM   AND "
				+ "PROJ1.PNUM = WORKS.PNUM        AND "
				+ "STAFF1.EMPNUM = WORKS.EMPNUM   AND " + "UPUNIQ.COL2 = 'A' "
				+ "ORDER BY 1, 2;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS GRADE CITY HOURS GRADE PNUM GRADE COL2
		// ====== ====== ============ ============ =============== ============
		// ============ ====== ============ ======
		//
		// E1 P1 40 12 Deale 40 12 P1 12 A
		// E1 P2 20 12 Deale 20 12 P2 12 A
		// E1 P3 80 12 Deale 80 12 P3 12 A
		// E1 P4 20 12 Deale 20 12 P4 12 A
		// E1 P5 12 12 Deale 12 12 P5 12 A
		// E1 P6 12 12 Deale 12 12 P6 12 A
		// E3 P2 20 13 Vienna 20 13 P2 13 A
		// E4 P2 20 12 Deale 20 12 P2 12 A
		// E4 P4 40 12 Deale 40 12 P4 12 A
		// E4 P5 80 12 Deale 80 12 P5 12 A
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(40, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P1 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(20, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P2 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(80, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P3 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(20, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P4 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(12, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P5 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(12, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P6 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));
		assertEquals(13, rs.getInt(4));
		assertEquals("Vienna         ", rs.getString(5));
		assertEquals(20, rs.getInt(6));
		assertEquals(13, rs.getInt(7));
		assertEquals("P2 ", rs.getString(8));
		assertEquals(13, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(20, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P2 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(40, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P4 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));
		assertEquals(12, rs.getInt(4));
		assertEquals("Deale          ", rs.getString(5));
		assertEquals(80, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		assertEquals("P5 ", rs.getString(8));
		assertEquals(12, rs.getInt(9));
		assertEquals("A ", rs.getString(10));

		rowCount = 10;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(10, rowCount);
		// PASS:0225 If 10 rows are selected ?
		// PASS:0225 If first STAFF.EMPNUM='E1',PROJ.PNUM='P1',WORKS.HOURS=40?
		// PASS:0225 If last STAFF.EMPNUM='E4',PROJ.PNUM='P5',WORKS.HOURS=80?

		// restore
		conn.rollback();

		// END TEST >>> 0225 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_050
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml050.
	//
	// ORIGIN: NIST file sql/dml050.sql
	//
	public void testDml_050() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);

		// TEST:0226 FIPS sizing - 10 tables in SQL statement!
		// FIPS sizing TEST

		rs = stmt.executeQuery("SELECT EMPNUM, EMPNAME " + "FROM STAFF "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM  FROM WORKS "
				+ "WHERE PNUM IN " + "(SELECT PNUM  FROM PROJ "
				+ "WHERE PTYPE IN " + "(SELECT PTYPE  FROM PROJ "
				+ "WHERE PNUM IN " + "(SELECT PNUM  FROM WORKS "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM  FROM WORKS "
				+ "WHERE PNUM IN " + "(SELECT PNUM   FROM PROJ "
				+ "WHERE PTYPE IN " + "(SELECT PTYPE  FROM PROJ "
				+ "WHERE CITY IN " + "(SELECT CITY  FROM STAFF "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM  FROM WORKS "
				+ "WHERE HOURS = 20 " + "AND PNUM = 'P2' )))))))));");
		//
		// Expected output:
		//
		// EMPNUM EMPNAME
		// ====== ====================
		//
		// E1 Alice
		// E2 Betty
		// E3 Carmen
		// E4 Don
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Alice               ", rs.getString(2));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Betty               ", rs.getString(2));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("Carmen              ", rs.getString(2));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("Don                 ", rs.getString(2));

		rowCount = 4;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0226 If 4 rows selected excluding EMPNUM='E5', EMPNAME='Ed'?

		// END TEST >>> 0226 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_051
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml051.
	//
	// ORIGIN: NIST file sql/dml051.sql
	//
	public void testDml_051() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */
		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0227 BETWEEN predicate with character string values!
		rs = stmt.executeQuery("SELECT PNUM " + "FROM   PROJ "
				+ "WHERE  PNAME BETWEEN 'A' AND 'F';");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P2
		//
		rs.next();
		assertEquals("P2 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0227 If PNUM = 'P2'?

		rs = stmt.executeQuery("SELECT PNUM " + "FROM   PROJ "
				+ "WHERE PNAME >= 'A' AND PNAME <= 'F';");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P2
		//
		rs.next();
		assertEquals("P2 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0227 If PNUM = 'P2'?

		// END TEST >>> 0227 <<< END TEST
		// ***********************************************************

		// TEST:0228 NOT BETWEEN predicate with character string values!
		rs = stmt.executeQuery("SELECT CITY " + "FROM   STAFF "
				+ "WHERE  EMPNAME NOT BETWEEN 'A' AND 'E';");
		//
		// Expected output:
		//
		// CITY
		// ===============
		// 
		// Akron
		//
		rs.next();
		assertEquals("Akron          ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0228 If CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT CITY " + "FROM   STAFF "
				+ "WHERE  NOT( EMPNAME BETWEEN 'A' AND 'E' );");
		//
		// Expected output:
		//
		// CITY
		// ===============
		// 
		// Akron
		//
		rs.next();
		assertEquals("Akron          ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0228 If CITY = 'Akron'?

		// END TEST >>> 0228 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_052
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml052.
	//
	// ORIGIN: NIST file sql/dml052.sql
	//
	public void testDml_052() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0229 Case-sensitive LIKE predicate!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E6','ALICE',11,'Gaithersburg');");
		assertEquals(1, rowCount);
		// PASS:0229 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNAME " + "FROM   STAFF "
				+ "WHERE  EMPNAME LIKE 'Ali%';");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0229 If 1 row is returned and EMPNAME = 'Alice' (not 'ALICE')?

		rs = stmt.executeQuery("SELECT EMPNAME " + "FROM   STAFF "
				+ "WHERE  EMPNAME LIKE 'ALI%';");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		//
		rs.next();
		assertEquals("ALICE               ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0229 If 1 row is returned and EMPNAME = 'ALICE' (not 'Alice')?

		// restore
		conn.rollback();

		// END TEST >>> 0229 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_053
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml053.
	//
	// ORIGIN: NIST file sql/dml053.sql
	//
	public void testDml_053() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
		conn.setAutoCommit(false);

		// TEST:0233 Table as multiset of rows - INSERT duplicate VALUES()!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "VALUES('E1',11,'Deale');");
		assertEquals(1, rowCount);
		// PASS:0233 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "VALUES('E1',11,'Deale');");
		assertEquals(1, rowCount);
		// PASS:0233 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMP_S "
				+ "WHERE EMPNUM='E1' AND GRADE=11 AND CITY='Deale';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            2
		//
		rs.next();
		assertEquals(2, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0233 If count = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0233 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_055
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml055.
	//
	// ORIGIN: NIST file sql/dml055.sql
	//
	public void testDml_055() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0243 FIPS sizing - precision of SMALLINT >= 4!
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");

		// setup
		assertEquals(1, stmt.executeUpdate("INSERT INTO HH " + "VALUES(9999);"));
		// PASS:0243 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM HH "
				+ "WHERE SMALLTEST = 9999;");
		rs.next();
		assertEquals(1, rs.getInt(1));

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO HH " + "VALUES(-9999);"));
		// PASS:0243 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT SMALLTEST " + "FROM HH "
				+ "WHERE SMALLTEST = -9999;");
		rs.next();
		assertEquals(-9999, rs.getInt(1));
		// PASS:0243 If SMALLTEST = -9999?
		assertFalse(rs.next());

		// END TEST >>> 0243 <<< END TEST
	}

	public void testDml_055_FipsSizingIntegerPrecision() throws SQLException {
		stmt.executeUpdate("CREATE TABLE EE (INTTEST     INTEGER);");
		// TEST:0244 FIPS sizing - precision of INTEGER >= 9!
		// FIPS sizing TEST

		assertEquals(1, stmt.executeUpdate("INSERT INTO EE "
				+ "VALUES(999999999);"));
		// PASS:0244 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT INTTEST " + "FROM EE "
				+ "WHERE INTTEST = 999999999;");
		rs.next();
		assertEquals(999999999, rs.getInt(1));
		// PASS:0244 If INTTEST = 999999999?
		assertFalse(rs.next());

		assertEquals(1, stmt.executeUpdate("INSERT INTO EE "
				+ "VALUES(-999999999);"));
		// PASS:0244 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM EE "
				+ "WHERE INTTEST = -999999999;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0244 If count = 1?

		// END TEST >>> 0244 <<< END TEST

	}

	public void testDml_055_FipsSizingDecimalPrecision() throws SQLException {

		// TEST:0245 FIPS sizing - precision of DECIMAL >= 15!
		// FIPS sizing TEST
		stmt.executeUpdate("CREATE TABLE PP_15 (NUMTEST  DECIMAL(15,15));");

		// setup
		assertEquals(1, stmt.executeUpdate("INSERT INTO PP_15 "
				+ "VALUES(.123456789012345);"));
		// PASS:0245 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT NUMTEST " + "FROM PP_15;");
		rs.next();
		assertEquals(0.123456789012345, rs.getDouble(1), 0.0);
		// PASS:0245 If NUMTEST = 0.123456789012345?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PP_15 "
				+ "WHERE NUMTEST = 0.123456789012345;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0245 If count = 1?

		assertEquals(1, stmt.executeUpdate("DELETE FROM PP_15;"));
		// PASS:0245 If 1 row is deleted?

		// setup
		assertEquals(1, stmt.executeUpdate("INSERT INTO PP_15 "
				+ "VALUES(-.912345678901234);"));
		// PASS:0245 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM PP_15 "
				+ "WHERE NUMTEST = -0.912345678901234;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		// END TEST >>> 0245 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/**
	 * ---------------------------------------------------------------
	 * testDml_056
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml056.
	//
	// ORIGIN: NIST file sql/dml056.sql
	//
	public void testDml_056() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		// TEST:0246 FIPS sizing - 100 values in INSERT!
		// FIPS sizing TEST

		stmt
				.executeUpdate("CREATE TABLE T100(C1 CHAR(2),C2 CHAR(2),C3 CHAR(2),C4 CHAR(2), "
						+ "C5 CHAR(2),C6 CHAR(2),C7 CHAR(2),C8 CHAR(2), "
						+ "C9 CHAR(2),C10 CHAR(2),C11 CHAR(2),C12 CHAR(2), "
						+ "C13 CHAR(2),C14 CHAR(2),C15 CHAR(2),C16 CHAR(2), "
						+ "C17 CHAR(2),C18 CHAR(2),C19 CHAR(2),C20 CHAR(2), "
						+ "C21 CHAR(2),C22 CHAR(2),C23 CHAR(2),C24 CHAR(2), "
						+ "C25 CHAR(2),C26 CHAR(2),C27 CHAR(2),C28 CHAR(2), "
						+ "C29 CHAR(2),C30 CHAR(2),C31 CHAR(2),C32 CHAR(2), "
						+ "C33 CHAR(2),C34 CHAR(2),C35 CHAR(2),C36 CHAR(2), "
						+ "C37 CHAR(2),C38 CHAR(2),C39 CHAR(2),C40 CHAR(2), "
						+ "C41 CHAR(2),C42 CHAR(2),C43 CHAR(2),C44 CHAR(2), "
						+ "C45 CHAR(2),C46 CHAR(2),C47 CHAR(2),C48 CHAR(2), "
						+ "C49 CHAR(2),C50 CHAR(2),C51 CHAR(2),C52 CHAR(2), "
						+ "C53 CHAR(2),C54 CHAR(2),C55 CHAR(2),C56 CHAR(2), "
						+ "C57 CHAR(2),C58 CHAR(2),C59 CHAR(2),C60 CHAR(2), "
						+ "C61 CHAR(2),C62 CHAR(2),C63 CHAR(2),C64 CHAR(2), "
						+ "C65 CHAR(2),C66 CHAR(2),C67 CHAR(2),C68 CHAR(2), "
						+ "C69 CHAR(2),C70 CHAR(2),C71 CHAR(2),C72 CHAR(2), "
						+ "C73 CHAR(2),C74 CHAR(2),C75 CHAR(2),C76 CHAR(2), "
						+ "C77 CHAR(2),C78 CHAR(2),C79 CHAR(2),C80 CHAR(2), "
						+ "C81 CHAR(2),C82 CHAR(2),C83 CHAR(2),C84 CHAR(2), "
						+ "C85 CHAR(2),C86 CHAR(2),C87 CHAR(2),C88 CHAR(2), "
						+ "C89 CHAR(2),C90 CHAR(2),C91 CHAR(2),C92 CHAR(2), "
						+ "C93 CHAR(2),C94 CHAR(2),C95 CHAR(2),C96 CHAR(2), "
						+ "C97 CHAR(2),C98 CHAR(2),C99 CHAR(2),C100 CHAR(2));");

		rowCount = stmt.executeUpdate("INSERT INTO T100 "
				+ "VALUES('ZA','ZB','CA','ZC','ZD','AA','ZE','ZF','BA','ZG', "
				+ "'YA','YB','CB','YC','YD','AB','YE','YF','BB','YG', "
				+ "'XA','XB','CC','XC','XD','AC','XE','XF','BC','XG', "
				+ "'UA','UB','CD','UC','UD','AD','UE','UF','BD','UG', "
				+ "'VA','VB','CE','VC','VD','AE','VE','VF','BE','VG', "
				+ "'WA','WB','CF','WC','WD','AF','WE','WF','BF','WG', "
				+ "'LA','LB','CG','LC','LD','AG','LE','LF','BG','LG', "
				+ "'MA','MB','CH','MC','MD','AH','ME','MF','BH','MG', "
				+ "'NA','NB','CI','NC','ND','AI','NE','NF','BI','NG', "
				+ "'OA','OB','CJ','OC','OD','AJ','OE','OF','BJ','OG');");
		assertEquals(1, rowCount);
		// PASS:0246 If 1 row is inserted?

		rs = stmt
				.executeQuery("SELECT C6, C16, C26, C36, C46, C56, C66, C76, C86, C96, C100 "
						+ "FROM T100 " + "WHERE C1 = 'ZA' AND C2 = 'ZB';");
		//
		// Expected output:
		//
		// C6 C16 C26 C36 C46 C56 C66 C76 C86 C96 C100
		// ====== ====== ====== ====== ====== ====== ====== ====== ====== ======
		// ======
		//
		// AA AB AC AD AE AF AG AH AI AJ OG
		//
		rs.next();
		assertEquals("AA", rs.getString(1));
		assertEquals("AB", rs.getString(2));
		assertEquals("AC", rs.getString(3));
		assertEquals("AD", rs.getString(4));
		assertEquals("AE", rs.getString(5));
		assertEquals("AF", rs.getString(6));
		assertEquals("AG", rs.getString(7));
		assertEquals("AH", rs.getString(8));
		assertEquals("AI", rs.getString(9));
		assertEquals("AJ", rs.getString(10));
		assertEquals("OG", rs.getString(11));

		assertFalse(rs.next());
		// PASS:0246 If C6 = 'AA', C16 = 'AB', C26 = 'AC', C36 = 'AD' ?
		// PASS:0246 If C46 = 'AE', C56 = 'AF', C66 = 'AG', C76 = 'AH' ?
		// PASS:0246 If C86 = 'AI', C96 = 'AJ', C100 = 'OG' ?

		// END TEST >>> 0246 <<< END TEST

		// *********************************************************************

		// TEST:0247 FIPS sizing - 20 values in update SET clause!
		// FIPS sizing TEST

		rowCount = stmt.executeUpdate("DELETE FROM T100;");
		rowCount = stmt.executeUpdate("INSERT INTO T100 "
				+ "VALUES('ZA','ZB','CA','ZC','ZD','AA','ZE','ZF','BA','ZG', "
				+ "'YA','YB','CB','YC','YD','AB','YE','YF','BB','YG', "
				+ "'XA','XB','CC','XC','XD','AC','XE','XF','BC','XG', "
				+ "'UA','UB','CD','UC','UD','AD','UE','UF','BD','UG', "
				+ "'VA','VB','CE','VC','VD','AE','VE','VF','BE','VG', "
				+ "'WA','WB','CF','WC','WD','AF','WE','WF','BF','WG', "
				+ "'LA','LB','CG','LC','LD','AG','LE','LF','BG','LG', "
				+ "'MA','MB','CH','MC','MD','AH','AE','AF','BH','BG', "
				+ "'NA','NB','CI','NC','ND','AI','NE','NF','BI','NG', "
				+ "'OA','OB','CJ','OC','OD','AJ','OE','OF','BJ','OG');");
		assertEquals(1, rowCount);
		// PASS:0247 If 1 row is inserted?

		rowCount = stmt
				.executeUpdate("UPDATE T100 "
						+ "SET C5 = 'BA', C10 = 'ZP', C15 = 'BB', C20 = 'YP', C25 = 'BC', "
						+ "C30 = 'XP', C35 = 'BD', C40 = 'UP', C45 = 'BE', C50 = 'VP', "
						+ "C55 = 'BF', C60 = 'WP', C65 = 'BG', C70 = 'LP', C75 = 'BH', "
						+ "C80 = 'MP', C85 = 'BI', C90 = 'NP', C95 = 'BJ', C100 = 'OP';");
		assertEquals(1, rowCount);
		// PASS:0247 If 1 row is updated ?

		rs = stmt
				.executeQuery("SELECT C5, C20, C35, C40, C55, C60, C75, C80, C90, C95, C100 "
						+ "FROM T100 " + "WHERE C1 = 'ZA' AND C2 = 'ZB';");
		//
		// Expected output:
		//
		// C5 C20 C35 C40 C55 C60 C75 C80 C90 C95 C100
		// ====== ====== ====== ====== ====== ====== ====== ====== ====== ======
		// ======
		//
		// BA YP BD UP BF WP BH MP NP BJ OP
		//
		rs.next();
		assertEquals("BA", rs.getString(1));
		assertEquals("YP", rs.getString(2));
		assertEquals("BD", rs.getString(3));
		assertEquals("UP", rs.getString(4));
		assertEquals("BF", rs.getString(5));
		assertEquals("WP", rs.getString(6));
		assertEquals("BH", rs.getString(7));
		assertEquals("MP", rs.getString(8));
		assertEquals("NP", rs.getString(9));
		assertEquals("BJ", rs.getString(10));
		assertEquals("OP", rs.getString(11));
		assertFalse(rs.next());
		// PASS:0247 If C5 = 'BA', C35 = 'BD', C55 = 'BF', C75 = 'BH' ?
		// PASS:0247 If C90 = 'NP', C100 = 'OP'?

		// END TEST >>> 0247 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/*
	 * 
	 * testDml_157_FipsSizingOfFloatRealAndDouble()
	 * 
	 * TEST:0248 FIPS sizing - binary precision of FLOAT >= 20!
	 *  
	 */
	public void testDml_057_FipsSizingOfFloatRealAndDouble()
			throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		stmt.executeUpdate("CREATE TABLE GG (REALTEST     REAL);");
		stmt.executeUpdate("CREATE TABLE II (DOUBLETEST  DOUBLE PRECISION);");
		stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO JJ " + "VALUES(0.1048575);");
		assertEquals(1, rowCount);
		// PASS:0248 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
		rs.next();
		assertEquals(0.10485750, rs.getFloat(1), 0.00000001);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0248 If FLOATTEST = 0.1048575 ?
		// PASS:0248 OR is between 0.1048574 and 0.1048576 ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM JJ "
				+ "WHERE FLOATTEST > 0.1048574 AND FLOATTEST < 0.1048576;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0248 If count = 1?

		rowCount = stmt.executeUpdate("DELETE FROM JJ;");
		// Making sure the table is empty

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO JJ " + "VALUES(-0.1048575);");
		assertEquals(1, rowCount);
		// PASS:0248 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
		//
		// Expected output:
		//
		//      FLOATTEST
		// ==============
		//
		//    -0.10485750
		//
		rs.next();
		assertEquals(-0.10485750, rs.getFloat(1), 0.00000001);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0248 If FLOATTEST = -0.1048575 ?
		// PASS:0248 OR is between -0.1048576 and -0.1048574 ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM JJ "
				+ "WHERE FLOATTEST > -0.1048576 AND FLOATTEST < -0.1048574;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0248 If count = 1?

		// restore

		// END TEST >>> 0248 <<< END TEST

		// *****************************************************************

		// TEST:0249 FIPS sizing - binary precision of REAL >= 20!
		// FIPS sizing TEST

		rowCount = stmt.executeUpdate("INSERT INTO GG " + "VALUES(0.1048575);");
		assertEquals(1, rowCount);
		// PASS:0249 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
		//
		// Expected output:
		//
		//       REALTEST
		// ==============
		//
		//     0.10485750
		//
		rs.next();
		assertEquals(0.10485750, rs.getFloat(1), 0.00000001);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0249 If REALTEST = 0.1048575 ?
		// PASS:0249 OR is between 0.1048574 and 0.1048576 ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM GG "
				+ "WHERE REALTEST > 0.1048574 AND REALTEST < 0.1048576;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0249 If count = 1?

		rowCount = stmt.executeUpdate("DELETE FROM GG;");
		// Making sure the table is empty

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO GG " + "VALUES(-0.1048575);");
		assertEquals(1, rowCount);
		// PASS:0249 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
		//
		// Expected output:
		//
		//       REALTEST
		// ==============
		//
		//    -0.10485750
		//
		rs.next();
		assertEquals(-0.10485750, rs.getFloat(1), 0.00000001);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0249 If REALTEST = -0.1048575 ?
		// PASS:0249 OR is between -0.1048576 and -0.1048574 ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM GG "
				+ "WHERE REALTEST > -0.1048576 AND REALTEST < -0.1048574;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0249 If count = 1?

		// END TEST >>> 0249 <<< END TEST

		// ***************************************************************

		// TEST:0250 FIPS sizing - bin. precision of DOUBLE >= 30!
		// FIPS sizing TEST
		rowCount = stmt.executeUpdate("INSERT INTO II "
				+ "VALUES(0.1073741823);");
		assertEquals(1, rowCount);
		// PASS:0250 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
		//
		// Expected output:
		//
		//              DOUBLETEST
		// =======================
		//
		//      0.1073741823000000
		//
		rs.next();
		assertEquals(0.1073741823000000, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0250 If DOUBLETEST = 0.1073741823 ?
		// PASS:0250 OR is between 0.1073741822 and 0.1073741824 ?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM II "
						+ "WHERE DOUBLETEST > 0.1073741822 AND DOUBLETEST < 0.1073741824;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0250 If count = 1?

		rowCount = stmt.executeUpdate("DELETE FROM II;");
		// Making sure the table is empty

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO II "
				+ "VALUES(-0.1073741823);");
		assertEquals(1, rowCount);
		// PASS:0250 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
		//
		// Expected output:
		//
		//              DOUBLETEST
		// =======================
		//
		//     -0.1073741823000000
		//
		rs.next();
		assertEquals(-0.1073741823000000, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0250 If DOUBLETEST = -0.1073741823 ?
		// PASS:0250 OR is between -0.1073741824 and -0.1073741822 ?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM II "
						+ "WHERE DOUBLETEST > -0.1073741824 AND DOUBLETEST < -0.1073741822;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0250 If count = 1?

		// END TEST >>> 0250 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/*
	 * 
	 * testDml_58
	 * 
	 * TEST:0251 COMMIT keeps changes of current transaction!
	 *  
	 */
	public void testDml_058() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */
		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");
		stmt
				.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
						+ "PNAME  CHAR(20), "
						+ "PTYPE  CHAR(6), "
						+ "BUDGET DECIMAL(9), " + "CITY   CHAR(15));");
		stmt.executeUpdate("CREATE TABLE T4(STR110 CHAR(110) NOT NULL, "
				+ "NUM6   NUMERIC(6) NOT NULL, "
				+ "COL3   CHAR(10),COL4 CHAR(20), " + "UNIQUE(STR110,NUM6));");

		conn.setAutoCommit(false);

		rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
		// Making sure the table is empty

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
				+ "SELECT * FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0251 If 5 rows are inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF1;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0251 If count = 5?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
				+ "VALUES('E9','Tom',50,'London');");
		assertEquals(1, rowCount);
		// PASS:0251 If 1 row is inserted?

		rowCount = stmt.executeUpdate("UPDATE STAFF1 " + "SET GRADE = 40 "
				+ "WHERE EMPNUM = 'E2';");
		assertEquals(1, rowCount);
		// PASS:0251 If 1 row is updated?

		conn.commit();

		rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
		assertEquals(6, rowCount);
		// PASS:0251 If 6 rows are deleted?

		// verify
		conn.rollback();

		// verify previous commit
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF1 "
				+ "WHERE GRADE > 12;");
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0251 If count = 4?

		// restore
		rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
		conn.commit();

		// END TEST >>> 0251 <<< END TEST

		// ***************************************************************

		// TEST:0252 ROLLBACK cancels changes of current transaction!

		rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
		// Making sure the table is empty

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 " + "SELECT * "
				+ "FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0252 If 5 rows are inserted?

		conn.commit();

		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
				+ "VALUES('E10','Tom',50,'London');");
		assertEquals(1, rowCount);
		// PASS:0252 If 1 row is inserted?

		rowCount = stmt.executeUpdate("UPDATE STAFF1 " + "SET GRADE = 40 "
				+ "WHERE EMPNUM = 'E1';");
		assertEquals(1, rowCount);
		// PASS:0252 If 1 row is updated?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF1 "
				+ "WHERE EMPNUM = 'E2';");
		assertEquals(1, rowCount);
		// PASS:0252 If 1 row is deleted?

		conn.rollback();

		// verify
		rs = stmt.executeQuery("SELECT SUM(GRADE) FROM STAFF1;");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                    60
		//
		rs.next();
		assertEquals(60, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0252 If SUM(GRADE) = 60?

		// restore
		rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
		conn.commit();

		// END TEST >>> 0252 <<< END TEST

		// ****************************************************************

		// TEST:0253 TEST0124 workaround (key = key+1)!

		rs = stmt.executeQuery("SELECT NUMKEY " + "FROM UPUNIQ "
				+ "ORDER BY NUMKEY DESC;");
		//
		// Expected output:
		//
		//        NUMKEY
		// ============
		//
		//            8
		//            6
		//            4
		//            3
		//            2
		//            1
		//
		rs.next();
		assertEquals(8, rs.getInt(1));

		rs.next();
		assertEquals(6, rs.getInt(1));

		rs.next();
		assertEquals(4, rs.getInt(1));

		rs.next();
		assertEquals(3, rs.getInt(1));

		rs.next();
		assertEquals(2, rs.getInt(1));

		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0253 If 6 rows are selected and first NUMKEY = 8 ?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 8 + 1 "
				+ "WHERE NUMKEY = 8;");
		assertEquals(1, rowCount);
		// PASS:0253 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 6 + 1 "
				+ "WHERE NUMKEY = 6;");
		assertEquals(1, rowCount);
		// PASS:0253 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 4 + 1 "
				+ "WHERE NUMKEY = 4;");
		assertEquals(1, rowCount);
		// PASS:0253 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 3 + 1 "
				+ "WHERE NUMKEY = 3;");
		assertEquals(1, rowCount);
		// PASS:0253 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 2 + 1 "
				+ "WHERE NUMKEY = 2;");
		assertEquals(1, rowCount);
		// PASS:0253 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 1 + 1 "
				+ "WHERE NUMKEY = 1;");
		assertEquals(1, rowCount);
		// PASS:0253 If 1 row is updated?

		rs = stmt.executeQuery("SELECT MAX(NUMKEY), MIN(NUMKEY) "
				+ "FROM UPUNIQ;");
		//
		// Expected output:
		//
		//          MAX MIN
		// ============ ============
		//
		//            9 2
		//
		rs.next();
		assertEquals(9, rs.getInt(1));
		assertEquals(2, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0253 If MAX(NUMKEY) = 9 AND MIN(NUMKEY) = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0253 <<< END TEST

		// **************************************************************

		// TEST:0254 Column name in SET clause!

		//		stmt.executeUpdate("CREATE TABLE PROJ1 (PNUM CHAR(3) NOT NULL UNIQUE,
		// " +
		//				"PNAME CHAR(20), " +
		//				"PTYPE CHAR(6), " +
		//				"BUDGET DECIMAL(9), " +
		//				"CITY CHAR(15));");
		rowCount = stmt.executeUpdate("INSERT INTO PROJ1 " + "SELECT * "
				+ "FROM PROJ;");
		assertEquals(6, rowCount);
		// PASS:0254 If 6 rows are inserted?

		rowCount = stmt.executeUpdate("UPDATE PROJ1 " + "SET CITY = PTYPE;");
		assertEquals(6, rowCount);
		// PASS:0254 If 6 rows are updated?

		rs = stmt.executeQuery("SELECT CITY " + "FROM PROJ1 "
				+ "WHERE PNUM = 'P1';");
		//
		// Expected output:
		//
		// CITY
		// ===============
		//
		// Design
		//
		rs.next();
		assertEquals("Design         ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0254 If CITY = 'Design'?

		// restore
		conn.rollback();

		// END TEST >>> 0254 <<< END TEST

		// **************************************************************

		// TEST:0255 Key word USER for INSERT, UPDATE!
		rowCount = stmt.executeUpdate("DELETE FROM T4;");
		// Making sure the table is empty

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES(USER,100,'good','luck');");
		assertEquals(1, rowCount);
		// PASS:0255 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT STR110 " + "FROM T4 "
				+ "WHERE NUM6 = 100;");
		//
		// Expected output:
		//
		// STR110
		// ===============================================================================
		//
		// SYSDBA
		//
		rs.next();
		assertEquals(
				"SYSDBA                                                                                                        ",
				rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0255 If STR110 = 'SYSDBA'?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES('Hello',101,'good','luck');");
		assertEquals(1, rowCount);
		// PASS:0255 If 1 row is inserted?

		rowCount = stmt.executeUpdate("UPDATE T4 " + "SET STR110 = USER "
				+ "WHERE NUM6 = 101;");
		assertEquals(1, rowCount);
		// PASS:0255 If 1 row is updated?

		rs = stmt.executeQuery("SELECT STR110 " + "FROM T4 "
				+ "WHERE NUM6 = 101;");
		//
		// Expected output:
		//
		// STR110
		// ===============================================================================
		//
		// SYSDBA
		//
		rs.next();
		assertEquals(
				"SYSDBA                                                                                                        ",
				rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0255 If STR110 = 'SYSDBA'?

		// restore
		conn.rollback();

		// END TEST >>> 0255 <<< END TEST

		// ***************************************************************

		// TEST:0256 Key word USER in WHERE clause!

		rowCount = stmt.executeUpdate("DELETE FROM T4;");
		// Making sure the table is empty

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES('SYSDBA',100,'good','luck');");
		assertEquals(1, rowCount);
		// PASS:0256 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT STR110 " + "FROM T4 "
				+ "WHERE STR110 = USER;");
		//
		// Expected output:
		//
		// STR110
		// ===============================================================================
		//
		// SYSDBA
		//
		rs.next();
		assertEquals(
				"SYSDBA                                                                                                        ",
				rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0256 If STR110 = 'SYSDBA'?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES('Hello',101,'good','luck');");
		assertEquals(1, rowCount);
		// PASS:0256 If 1 row is inserted?

		rowCount = stmt.executeUpdate("DELETE FROM T4 "
				+ "WHERE STR110 = USER;");
		assertEquals(1, rowCount);
		// PASS:0256 If 1 row is deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM T4 "
				+ "WHERE STR110 LIKE '%SYSDBA%';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0256 If count = 0?

		// restore
		conn.rollback();

		// END TEST >>> 0256 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/*
	 * 
	 * TestDml_059_SelectMaxMin()
	 * 
	 * TEST:0257 SELECT MAX, MIN (COL1 + or - COL2)!
	 *  
	 */
	public void testDml_059_SelectMaxMin() throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// TEST:0257 SELECT MAX, MIN (COL1 + or - COL2)!

		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(10,11,12,13,15);"));
		// PASS:0257 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(100,111,1112,113,115);"));
		// PASS:0257 If 1 row is inserted?

		rs = stmt
				.executeQuery("SELECT COL1, MAX(COL2 + COL3), MIN(COL3 - COL2) "
						+ "FROM VTABLE " + "GROUP BY COL1 " + "ORDER BY COL1;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertEquals(3, rs.getInt(2));
		assertEquals(1, rs.getInt(3));

		rs.next();
		assertEquals(10, rs.getInt(1));
		assertEquals(50, rs.getInt(2));
		assertEquals(1, rs.getInt(3));

		rs.next();
		assertEquals(100, rs.getInt(1));
		assertEquals(1223, rs.getInt(2));
		assertEquals(100, rs.getInt(3));

		rs.next();
		assertEquals(1000, rs.getInt(1));
		assertEquals(1000, rs.getInt(2));
		assertEquals(5000, rs.getInt(3));

		assertFalse(rs.next());
		// PASS:0257 If 4 rows are selected in order with values:?
		// PASS:0257 ( 0, 3, 1) ?
		// PASS:0257 ( 10, 50, 1)?
		// PASS:0257 ( 100, 1223, 100)?
		// PASS:0257 ( 1000, 1000, 5000)?

		// END TEST >>> 0257 <<< END TEST
	}

	/*
	 * 
	 * testDml_059_SelectSumInHavingSum()
	 * 
	 * TEST:0258 SELECT SUM(2*COL1*COL2) in HAVING SUM(COL2*COL3)!
	 *  
	 */
	public void testDml_059_SelectSumInHavingSum() throws SQLException {

		BaseTab.setupBaseTab(stmt);

		// TEST:0258 SELECT SUM(2*COL1*COL2) in HAVING SUM(COL2*COL3)!
		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES (10,11,12,13,15);"));
		// PASS:0258 if 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES (100,111,1112,113,115);"));
		// PASS:0258 if 1 row is inserted ?

		rs = stmt.executeQuery("SELECT COL1,SUM(2 * COL2 * COL3) "
				+ "FROM VTABLE " + "GROUP BY COL1 "
				+ "HAVING SUM(COL2 * COL3) > 2000 "
				+ "OR SUM(COL2 * COL3) < -2000 " + "ORDER BY COL1;");
		rs.next();
		assertEquals(100, rs.getInt(1));
		assertEquals(366864, rs.getInt(2));

		rs.next();
		assertEquals(1000, rs.getInt(1));
		assertEquals(-12000000, rs.getInt(2));

		assertFalse(rs.next());
		// PASS:0258 If 2 rows are selected?
		// PASS:0258 If first row has values (100, 366864) ?
		// PASS:0258 If second row has values (1000, -12000000) ?

		// END TEST >>> 0258 <<< END TEST
	}

	/*
	 * 
	 * testDml_059_SomeAnyInHavingClause()
	 * 
	 * TEST:0259 SOME, ANY in HAVING clause!
	 *  
	 */
	public void testDml_059_SomeAnyInHavingClause() throws SQLException {

		BaseTab.setupBaseTab(stmt);

		// TEST:0259 SOME, ANY in HAVING clause!
		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(10,11,12,13,15);"));
		// PASS:0259 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(100,111,1112,113,115);"));
		// PASS:0259 If 1 row is inserted?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COL1, MAX(COL2) " + "FROM VTABLE "
					+ "GROUP BY COL1 "
					+ "HAVING MAX(COL2) > ANY (SELECT GRADE FROM STAFF) "
					+ "AND MAX(COL2) < SOME (SELECT HOURS FROM WORKS) "
					+ "ORDER BY COL1;");
			rs.next();
			assertEquals(10, rs.getInt(1));
			assertEquals(20, rs.getInt(2));
			assertFalse(rs.next());
			// PASS:0259 If 1 row is selected and COL1 = 10 and MAX(COL2) = 20?
		}
		// END TEST >>> 0259 <<< END TEST

	}
	/*
	 * 
	 * testDml_059_ExistsInHavingClause()
	 * 
	 * TEST:0260 EXISTS in HAVING clause!
	 *  
	 */
	public void testDml_059_ExistsInHavingClause() throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// TEST:0260 EXISTS in HAVING clause!

		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(10,11,12,13,15);"));
		// PASS:0260 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(100,111,1112,113,115);"));
		// PASS:0260 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COL1, MAX(COL2) " + "FROM VTABLE "
				+ "GROUP BY COL1 " + "HAVING EXISTS (SELECT * " + "FROM STAFF "
				+ "WHERE EMPNUM = 'E1') " + "AND MAX(COL2) BETWEEN 10 AND 90 "
				+ "ORDER BY COL1;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		assertEquals(20, rs.getInt(2));

		assertFalse(rs.next());
		// PASS:0260 If 1 row is selected and COL1 = 10 and MAX(COL2) = 20?

		// END TEST >>> 0260 <<< END TEST
	}

	/*
	 * 
	 * testDml_059_WhereHavingWithoutGroupBy
	 * 
	 * TEST:0264 WHERE, HAVING without GROUP BY!
	 *  
	 */
	public void testDml_059_WhereHavingWithoutGroupBy() throws SQLException {

		BaseTab.setupBaseTab(stmt);

		// TEST:0264 WHERE, HAVING without GROUP BY!

		rs = stmt.executeQuery("SELECT SUM(COL1) " + "FROM VTABLE "
				+ "WHERE 10 + COL1 > COL2 " + "HAVING MAX(COL1) > 100;");
		rs.next();
		assertEquals(1000, rs.getInt(1));
		// PASS:0264 If SUM(COL1) = 1000?

		rs = stmt.executeQuery("SELECT SUM(COL1) " + "FROM VTABLE "
				+ "WHERE 1000 + COL1 >= COL2 " + "HAVING MAX(COL1) > 100;");
		rs.next();
		assertEquals(1110, rs.getInt(1));
		// PASS:0264 If SUM(COL1) = 1110?

		// END TEST >>> 0264 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	/***************************************************************************
	 * --------------------------------------------------------------- METHOD:
	 * testDml_060
	 * ----------------------------------------------------------------
	 */
	//
	// Statements used to exercise test sql/dml060.
	//
	// ORIGIN: NIST file sql/dml060.sql
	//
	public void testDml_060() throws SQLException {
		int errorCode; /* Error code. */
		int rowCount; /* Row count. */

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE VIEW SUBSP (EMPNUM,PNUM,HOURS) "
				+ "AS SELECT EMPNUM,PNUM,HOURS " + "FROM   WORKS "
				+ "WHERE  EMPNUM='E3' " + "WITH CHECK OPTION;");
		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		conn.setAutoCommit(false);

		// TEST:0261 WHERE (2 * (c1 - c2)) BETWEEN!

		rs = stmt.executeQuery("SELECT COL1, COL2 " + "FROM VTABLE "
				+ "WHERE(2*(COL3 - COL2)) BETWEEN 5 AND 200 "
				+ "ORDER BY COL1;");
		//
		// Expected output:
		//
		//         COL1 COL2
		// ============ ============
		//
		//           10 20
		//          100 200
		//
		rs.next();
		assertEquals(10, rs.getInt(1));
		assertEquals(20, rs.getInt(2));

		rs.next();
		assertEquals(100, rs.getInt(1));
		assertEquals(200, rs.getInt(2));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0261 If 2 rows are selected ?
		// PASS:0261 If first row is ( 10, 20)?
		// PASS:0261 If second row is (100, 200)?

		// END TEST >>> 0261 <<< END TEST

		// ********************************************************************

		// TEST:0262 WHERE clause with computation, ANY/ALL subqueries!

		rowCount = stmt.executeUpdate("UPDATE VTABLE " + "SET COL1 = 1 "
				+ "WHERE COL1 = 0;");
		assertEquals(1, rowCount);
		// PASS:0262 If 1 row is updated?

		rs = stmt.executeQuery("SELECT COL1, COL2 " + "FROM VTABLE "
				+ "WHERE (COL3 * COL2/COL1) > ALL "
				+ "(SELECT HOURS FROM WORKS) "
				+ "OR -(COL3 * COL2/COL1) > ANY "
				+ "(SELECT HOURS FROM WORKS) " + "ORDER BY COL1;");
		//
		// Expected output:
		//
		//         COL1 COL2
		// ============ ============
		//
		//          100 200
		//         1000 -2000
		//
		rs.next();
		assertEquals(100, rs.getInt(1));
		assertEquals(200, rs.getInt(2));

		rs.next();
		assertEquals(1000, rs.getInt(1));
		assertEquals(-2000, rs.getInt(2));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0262 If 2 rows are selected?
		// PASS:0262 If first row is ( 100, 200)?
		// PASS:0262 If second row is (1000, -2000)?

		// restore
		conn.rollback();

		// END TEST >>> 0262 <<< END TEST

		// ******************************************************************

		// TEST:0263 Computed column in ORDER BY!

		rs = stmt.executeQuery("SELECT COL1, (COL3 * COL2/COL1 - COL2 + 10) "
				+ "FROM VTABLE " + "WHERE COL1 > 0 " + "ORDER BY 2;");
		//
		// Expected output:
		//
		//         COL1
		// ============ =====================
		//
		//         1000 -3990
		//           10 50
		//          100 410
		//
		rs.next();
		assertEquals(1000, rs.getInt(1));
		assertEquals(-3990, rs.getInt(2));

		rs.next();
		assertEquals(10, rs.getInt(1));
		assertEquals(50, rs.getInt(2));

		rs.next();
		assertEquals(100, rs.getInt(1));
		assertEquals(410, rs.getInt(2));

		rowCount = 3;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(3, rowCount);
		// PASS:0263 If 3 rows are selected in order with values:?
		// PASS:0263 (1000, -3990)?
		// PASS:0263 ( 10, 50)?
		// PASS:0263 ( 100, 410)?

		// END TEST >>> 0263 <<< END TEST

		// ********************************************************************

		// TEST:0265 Update:searched - view with check option!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E3','P4',50);");
		assertEquals(1, rowCount);
		// PASS:0265 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM, PNUM, HOURS " + "FROM SUBSP;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E3 P2 20
		// E3 P4 50
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0265 If 2 rows are selected?

		rs = stmt.executeQuery("SELECT * FROM WORKS;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E1 P1 40
		// E1 P2 20
		// E1 P3 80
		// E1 P4 20
		// E1 P5 12
		// E1 P6 12
		// E2 P1 40
		// E2 P2 80
		// E3 P2 20
		// E4 P2 20
		// E4 P4 40
		// E4 P5 80
		// E3 P4 50
		//

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 13;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(13, rowCount);
		// PASS:0265 If 13 rows selected?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE SUBSP "
					+ "SET EMPNUM = 'E9' " + "WHERE PNUM = 'P2';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table SUBSP");
			}
		}
		// PASS:0265 If ERROR, view check constraint, 0 rows are updated?

		rs = stmt.executeQuery("SELECT * FROM WORKS;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E1 P1 40
		// E1 P2 20
		// E1 P3 80
		// E1 P4 20
		// E1 P5 12
		// E1 P6 12
		// E2 P1 40
		// E2 P2 80
		// E3 P2 20
		// E4 P2 20
		// E4 P4 40
		// E4 P5 80
		// E3 P4 50
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 13;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(13, rowCount);
		// PASS:0265 If 13 rows selected and no EMPNUM = 'E9'?

		// restore
		conn.rollback();

		// END TEST >>> 0265 <<< END TEST

		// ******************************************************************

		// TEST:0266 Update:searched - UNIQUE violation under view!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E3','P4',50);");
		assertEquals(1, rowCount);
		// PASS:0266 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM, PNUM, HOURS " + "FROM SUBSP;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E3 P2 20
		// E3 P4 50
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0266 If 2 rows are selected?

		rs = stmt.executeQuery("SELECT * FROM WORKS WHERE EMPNUM = 'E3';");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E3 P2 20
		// E3 P4 50
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0266 If 2 rows selected and PNUM values are 'P2' and 'P4'?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE SUBSP " + "SET PNUM = 'P6' "
					+ "WHERE EMPNUM = 'E3';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_7 on table WORKS");
			}
		}
		// PASS:0266 If ERROR, unique constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT EMPNUM, PNUM, HOURS " + "FROM SUBSP;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E3 P2 20
		// E3 P4 50
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0266 If 2 rows are selected?

		rs = stmt.executeQuery("SELECT * FROM WORKS WHERE EMPNUM = 'E3';");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E3 P2 20
		// E3 P4 50
		//	
		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(50, rs.getInt(3));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0266 If 2 rows selected and PNUM values are 'P2' and 'P4'?

		// restore
		conn.rollback();

		// END TEST >>> 0266 <<< END TEST

		// ******************************************************************

		// TEST:0267 Update compound key, interim uniqueness conflict!

		rowCount = stmt.executeUpdate("DELETE FROM WORKS1;");
		// Making sure the table is empty

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P1','P6',1);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P2','P6',2);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P3','P6',3);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P4','P6',4);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P5','P6',5);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P6','P6',6);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P1','P5',7);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P2','P5',8);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P3','P5',9);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P4','P5',10);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P5','P5',11);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P6','P5',12);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P1','P4',13);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P2','P4',14);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P3','P4',15);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P4','P4',16);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P5','P4',17);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P6','P4',18);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P1','P3',19);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P2','P3',20);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P3','P3',21);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P4','P3',22);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P5','P3',23);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P6','P3',24);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P1','P2',25);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P2','P2',26);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P3','P2',27);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P4','P2',28);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P5','P2',29);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P6','P2',30);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P1','P1',31);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P2','P1',32);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P3','P1',33);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P4','P1',34);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P5','P1',35);");
		assertEquals(1, rowCount);
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 VALUES ('P6','P1',36);");
		assertEquals(1, rowCount);

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE WORKS1 "
					+ "SET PNUM = EMPNUM, EMPNUM = PNUM;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_13 on table WORKS1");
			}
		}
		// TODO: The original comments indicate 36 rows should be updated. Zero
		//       rows are actually updated. The comment is being ignored.
		//
		//       PASS:0267 If 36 rows are updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS1 "
				+ "WHERE EMPNUM = 'P1' AND HOURS > 30;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0267 If count = 1?

		// restore
		conn.rollback();

		// END TEST >>> 0267 <<< END TEST

		// *************************************************////END-OF-MODULE
	}
	/*
	 * 
	 * testDml_061_BetweenValueExpressionInWrongOrder()
	 * 
	 * TEST:0269 BETWEEN value expressions in wrong order!
	 *  
	 */
	public void testDml_061_BetweenValueExpressionInWrongOrder()
			throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS BETWEEN 80 AND 40;");
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E6','P6',-60);");
		assertEquals(1, rowCount);

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS BETWEEN -40 AND -80;");
		rs.next();
		assertEquals(0, rs.getInt(1));

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS BETWEEN -80 AND -40;");
		rs.next();
		assertEquals(1, rs.getInt(1));

		// END TEST >>> 0269 <<< END TEST

	}
	/*
	 * 
	 * testDml_061_BetweenApproximateAndExactNumericValues()
	 *  
	 */
	public void testDml_061_BetweenApproximateAndExactNumericValues()
			throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS BETWEEN 11.999 AND 12 OR "
				+ "HOURS BETWEEN 19.999 AND 2.001E1;");
		rs.next();
		assertEquals(6, rs.getInt(1));

		// END TEST >>> 0270 <<< END TEST
	}
	/*
	 * 
	 * testDml_061_CountStarWithCartesianProductSubset()
	 * 
	 * TEST:0271 COUNT(*) with Cartesian product subset !
	 *  
	 */
	public void testDml_061_CountStarWithCartesianProductSubset()
			throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.
		BaseTab.setupBaseTab(stmt);
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS,STAFF "
				+ "WHERE WORKS.EMPNUM = 'E1';");
		rs.next();
		assertEquals(30, rs.getInt(1));
		// PASS:0271 If count = 30?

		// END TEST >>> 0271 <<< END TEST

		// ****************************************************************
	}
	/*
	 * 
	 * testDml_061_StatementRollbackForIntegrity()
	 * 
	 * TEST:0272 Statement rollback for integrity!
	 *  
	 */
	public void testDml_061_StatementRollbackForIntegrity() throws SQLException {

		int errorCode;
		int rowCount;
		BaseTab.setupBaseTab(stmt);
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE WORKS "
					+ "SET EMPNUM = 'E7' "
					+ "WHERE EMPNUM = 'E1' OR EMPNUM = 'E4';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_7 on table WORKS");
			}
		}
		// PASS:0272 If ERROR, unique constraint, 0 rows updated?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO WORKS "
					+ "SELECT 'E3',PNUM,17 FROM PROJ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_7 on table WORKS");
			}
		}
		// PASS:0272 If ERROR, unique constraint, 0 rows inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE V_WORKS1 "
					+ "SET HOURS = HOURS - 9;");
			fail();
			// PASS:0272 If ERROR, view check constraint, 0 rows updated?
		} catch (SQLException excep) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE EMPNUM = 'E7' OR HOURS = 31 OR HOURS = 17;");
		rs.next();
		assertEquals(0, rs.getInt(1));

		// END TEST >>> 0272 <<< END TEST
	}
	/*
	 * 
	 * testDml_061_SumMaxMinEqualsNullEmptyArguments()
	 * 
	 * TEST:0271 COUNT(*) with Cartesian product subset !
	 *  
	 */
	public void testDml_061_SumMaxMinEqualsNullEmptyArguments()
			throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);

		rowCount = stmt.executeUpdate("UPDATE WORKS " + "SET HOURS = NULL;");
		assertEquals(12, rowCount);
		// PASS:0273 If 12 rows updated?

		rs = stmt
				.executeQuery("SELECT SUM(HOURS),MAX(HOURS),MIN(HOURS),MIN(EMPNUM) "
						+ "FROM WORKS;");
		//
		// Expected output:
		//
		//                   SUM MAX MIN MIN
		// ===================== ============ ============ ======
		//
		//                 <null> <null> <null> E1
		//
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertEquals("E1 ", rs.getString(4));
		assertFalse(rs.next());
		// PASS:0273 If 1 row is selected?
		// PASS:0273 If SUM(HOURS), MAX(HOURS), and MIN(HOURS) are NULL?
	}

	/*
	 * 
	 * testDml_061_ComputationWithNullValueSpecification()
	 * 
	 * TEST:0271 COUNT(*) with Cartesian product subset !
	 *  
	 */
	public void testDml_061_ComputationWithNullValueSpecification()
			throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);

		// TEST:0277 Computation with NULL value specification!

		rowCount = stmt.executeUpdate("UPDATE WORKS "
				+ "SET HOURS = NULL  WHERE EMPNUM = 'E1';");
		assertEquals(6, rowCount);
		// PASS:0277 If 6 rows are updated?

		rowCount = stmt.executeUpdate("UPDATE WORKS "
				+ "SET HOURS = HOURS - (3 + -17);");
		assertEquals(12, rowCount);
		// PASS:0277 If 12 rows are updated?

		rowCount = stmt.executeUpdate("UPDATE WORKS "
				+ "SET HOURS = 3 / -17 * HOURS;");
		assertEquals(12, rowCount);
		// PASS:0277 If 12 rows are updated?

		rowCount = stmt.executeUpdate("UPDATE WORKS "
				+ "SET HOURS = HOURS + 5;");
		assertEquals(12, rowCount);
		// PASS:0277 If 12 rows are updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS IS NULL;");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// END TEST >>> 0277 <<< END TEST
	}
	/*
	 * 
	 * testDml_061_InValueListWithUserLiteralVariableSpec
	 * 
	 * TEST:0278 IN value list with USER, literal, variable spec.!
	 *  
	 */
	public void testDml_061_InValueListWithUserLiteralVariableSpec()
			throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);

		rowCount = stmt.executeUpdate("UPDATE STAFF "
				+ "SET EMPNAME = 'SYSDBA' " + "WHERE EMPNAME = 'Ed';");
		assertEquals(1, rowCount);
		// PASS:0278 If 1 row is updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE EMPNAME IN (USER,'Betty','Carmen');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            3
		//
		rs.next();
		assertEquals(3, rs.getInt(1));

	}

	/*
	 * 
	 * testDml_062_CommitAndRollbackAcrossSchemas()
	 * 
	 * TEST:0274 COMMIT and ROLLBACK across schemas!
	 * 
	 * Notes: Firebird/Vulcan don't support schemas...
	 *  
	 */
	public void testDml_062_CommitAndRollbackAcrossSchemas()
			throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;
		//BaseTab.setupBaseTab(stmt);
		//stmt.executeUpdate("CREATE SCHEMA AUTHORIZATION SULLIVAN1;");
		//stmt.executeUpdate("CREATE TABLE AUTH_TABLE (FIRST1 INTEGER, SECOND2
		// CHAR);");
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("DELETE FROM SULLIVAN1.AUTH_TABLE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}

		// setup
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO SULLIVAN1.AUTH_TABLE "
					+ "VALUES (10,'A');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}
		// PASS:0274 If 1 row inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO SULLIVAN1.AUTH_TABLE "
					+ "VALUES (100,'B');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}
		// PASS:0274 If 1 row inserted?

		rowCount = stmt.executeUpdate("DELETE FROM AA;");

		rowCount = stmt.executeUpdate("INSERT INTO AA "
				+ "VALUES ('In God we trust');");
		assertEquals(1, rowCount);
		// PASS:0274 If 1 row inserted ?

		conn.commit();

		// to be rolled back
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("DELETE FROM SULLIVAN1.AUTH_TABLE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}
		// to be rolled back
		rowCount = stmt.executeUpdate("DELETE FROM AA;");

		// revert to previous COMMIT
		conn.rollback();

		errorCode = 0;
		try {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM SULLIVAN1.AUTH_TABLE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}
		// PASS:0274 If count = 2?

		rs = stmt.executeQuery("SELECT CHARTEST FROM AA;");
		//
		// Expected output:
		//
		// CHARTEST
		// ====================
		// In God we trust
		//
		rs.next();
		assertEquals("In God we trust     ", rs.getString(1));

		// PASS:0274 If CHARTEST = 'In God we trust'?

		// restore
		rowCount = stmt.executeUpdate("DELETE FROM AA;");

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("DELETE FROM SULLIVAN1.AUTH_TABLE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}

		conn.commit();

		// END TEST >>> 0274 <<< END TEST

	}
	/*
	 * 
	 * testDml_062_CommitAndRollbackOfMultipleTables()
	 * 
	 * TEST:0275 COMMIT and ROLLBACK of multiple tables!
	 *  
	 */
	public void testDml_062_CommitAndRollbackOfMultipleTables()
			throws SQLException {
		int rowCount;
		int errorCode;

		BaseTab.setupBaseTab(stmt);

		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");
		stmt
				.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
						+ "PNAME  CHAR(20), "
						+ "PTYPE  CHAR(6), "
						+ "BUDGET DECIMAL(9), " + "CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		conn.setAutoCommit(false);
		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
				+ "SELECT * FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0275 If 5 rows are inserted?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS1 "
				+ "SELECT * FROM WORKS;");
		assertEquals(12, rowCount);
		// PASS:0275 If 12 rows are inserted?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO PROJ1 "
				+ "SELECT * FROM PROJ;");
		assertEquals(6, rowCount);
		// PASS:0275 If 6 rows are inserted?

		rowCount = stmt.executeUpdate("UPDATE STAFF1 " + "SET EMPNUM = 'E9' "
				+ "WHERE EMPNUM = 'E3';");
		assertEquals(1, rowCount);
		// PASS:0275 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE WORKS1 "
				+ "SET EMPNUM = 'E9', PNUM = 'P9' " + "WHERE EMPNUM = 'E3';");
		assertEquals(1, rowCount);
		// PASS:0275 If 1 row is updated?

		rowCount = stmt.executeUpdate("UPDATE PROJ1 " + "SET PNUM = 'P9' "
				+ "WHERE PNUM = 'P2';");
		assertEquals(1, rowCount);
		// PASS:0275 If 1 row is updated?

		conn.commit();

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF1,WORKS1,PROJ1 "
				+ "WHERE STAFF1.EMPNUM = 'E9' AND "
				+ "STAFF1.EMPNUM = WORKS1.EMPNUM AND "
				+ "PROJ1.PNUM = WORKS1.PNUM;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0275 If count = 1?

		// END TEST >>> 0275 <<< END TEST
		// ****************************************************************

	}
	/*
	 * 
	 * testDml_062_ViewAcrossSchemas
	 * 
	 * TEST:0276 View across schemas!
	 * 
	 * Notes: Firebird / Vulcan do not support schemas...
	 *  
	 */
	public void testDml_062_ViewAcrossSchemas() throws SQLException {
		int errorCode;
		int rowCount;
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;
		BaseTab.setupBaseTab(stmt);
		//		stmt.executeUpdate("CREATE SCHEMA AUTHORIZATION SULLIVAN1;");
		//		stmt.executeUpdate("CREATE TABLE AUTH_TABLE (FIRST1 INTEGER, SECOND2
		// CHAR);");

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("DELETE FROM SULLIVAN1.AUTH_TABLE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}

		// setup
		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO SULLIVAN1.AUTH_TABLE "
					+ "VALUES (12,'A');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}
		// PASS:0276 If 1 row is inserted?

		errorCode = 0;
		try {
			rs = stmt
					.executeQuery("SELECT EMPNUM,SECOND2 FROM SULLIVAN1.MUL_SCH "
							+ "ORDER BY EMPNUM;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569) {
				fail("Should return Token unknown");
			}
		}
		// PASS:0276 If 2 rows are selected?
		// PASS:0276 If first row EMPNUM = 'E1' and SECOND2 = 'A'?
		// PASS:0276 If second row EMPNUM = 'E4' and SECOND2 = 'A'?

		// restore

		// END TEST >>> 0276 <<< END TEST

	}
	/*
	 * 
	 * testDml_062_InIsAThreeValuedPredicateExistsIsTwoValued()
	 * 
	 * TEST:0269 BETWEEN value expressions in wrong order!
	 *  
	 */
	public void testDml_062_InIsAThreeValuedPredicateExistsIsTwoValued()
			throws SQLException {
		int rowCount;
		BaseTab.setupBaseTab(stmt);

		// TEST:0279 IN is a 3-valued predicate, EXISTS is 2-valued!
		rowCount = stmt.executeUpdate("UPDATE WORKS " + "SET HOURS = NULL "
				+ "WHERE PNUM = 'P5' OR EMPNUM = 'E4';");
		assertEquals(4, rowCount);
		// PASS:0279 If 4 rows are updated?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0279 If count = 5?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE 40 IN (SELECT HOURS FROM WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM);");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0279 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE 40 NOT IN (SELECT HOURS FROM WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM);");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0279 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE EXISTS (SELECT * FROM WORKS "
				+ "WHERE HOURS = 40 AND " + "STAFF.EMPNUM = WORKS.EMPNUM);");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0279 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE NOT EXISTS (SELECT * FROM WORKS "
				+ "WHERE HOURS = 40 AND  " + "STAFF.EMPNUM = WORKS.EMPNUM);");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0279 If count = 3?
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_064
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml064.
	//
	// ORIGIN: NIST file sql/dml064.sql
	//
	public void testDml_064() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		// TEST:0281 Updatable VIEW with ALL, IN, BETWEEN!
		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE VIEW STAFFV1 " + "AS SELECT * FROM STAFF "
				+ "WHERE  GRADE >= 12;");

		stmt.executeUpdate("CREATE VIEW STAFFV2 " + "AS SELECT * FROM STAFF "
				+ "WHERE  GRADE >= 12 " + "WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW1 " + "AS SELECT ALL CITY "
				+ "FROM PROJ;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW2 "
				+ "AS SELECT HOURS, EMPNUM, PNUM " + "FROM WORKS "
				+ "WHERE HOURS IN (10, 20, 40);");
		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW3 " + "AS SELECT * "
				+ "FROM WORKS " + "WHERE PNUM BETWEEN 'P2' AND 'P4' "
				+ "AND EMPNUM NOT BETWEEN 'E2' AND 'E3';");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW4 "
				+ "AS SELECT PNUM, EMPNUM " + "FROM WORKS "
				+ "WHERE PNUM LIKE '_2%';");
		stmt
				.executeUpdate("CREATE VIEW UPDATE_VIEW5 " + "AS SELECT * "
						+ "FROM STAFF "
						+ "WHERE EMPNAME IS NOT NULL AND CITY IS NULL;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW6 "
				+ "AS SELECT EMPNAME, CITY, GRADE " + "FROM STAFF "
				+ "WHERE EMPNAME >= 'Betty' AND EMPNUM < 'E35' "
				+ "OR CITY <= 'Deale' AND GRADE > 12 "
				+ "OR GRADE = 13 AND CITY <> 'Akron';");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW7 "
				+ "AS SELECT EMPNAME, CITY, GRADE " + "FROM STAFFV2 "
				+ "WHERE EMPNAME >= 'Betty' AND EMPNUM < 'E35' "
				+ "OR CITY <= 'Deale' AND GRADE > 12 "
				+ "OR GRADE = 13 AND CITY <> 'Akron';");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW8 "
				+ "AS SELECT MYTABLE.EMPNUM, MYTABLE.EMPNAME "
				+ "FROM STAFF MYTABLE " + "WHERE MYTABLE.GRADE = 12;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW9 "
				+ "AS SELECT EMPNAME, CITY, GRADE " + "FROM STAFF "
				+ "WHERE NOT EMPNAME >= 'Betty' AND EMPNUM <= 'E35' "
				+ "OR NOT (CITY <= 'Deale') AND GRADE > 9 "
				+ "AND NOT (GRADE = 13 AND CITY <> 'Akron') "
				+ "OR NOT CITY IN ('Vienna','New York','Deale');");

		conn.setAutoCommit(false);
		stmt.executeUpdate("DELETE FROM UPDATE_VIEW1 WHERE CITY = 'Tampa';");
		stmt.executeUpdate("DELETE FROM UPDATE_VIEW1 WHERE CITY = 'Deale';");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM PROJ;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0281 If count = 2?

		// restore
		conn.rollback();

		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW2 VALUES (10, 'E9', 'P7');");

		rowCount = stmt.executeUpdate("UPDATE UPDATE_VIEW2 "
				+ "SET HOURS = 10 WHERE EMPNUM = 'E4';");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM WORKS WHERE HOURS = 10;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0281 If count = 3?

		conn.rollback();

		stmt
				.executeUpdate("UPDATE UPDATE_VIEW3 SET EMPNUM = 'E7' WHERE EMPNUM = 'E1';");
		stmt.executeUpdate("DELETE FROM UPDATE_VIEW3 WHERE HOURS = 80;");

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM WORKS WHERE EMPNUM = 'E7';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0281 If count = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0281 <<< END TEST

		// ****************************************************************

		// TEST:0282 Updatable VIEW with LIKE, NULL, >, =, < !

		stmt.executeUpdate("DELETE FROM UPDATE_VIEW4 WHERE EMPNUM = 'E1';");

		stmt
				.executeUpdate("DELETE FROM UPDATE_VIEW4 "
						+ "WHERE EMPNUM = 'E3';");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM WORKS;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		// PASS:0282 If count = 10?

		// restore
		conn.rollback();

		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW5 VALUES ('E6',NULL,11,NULL);");
		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW5 VALUES ('E7',NULL,11,'Deale');");
		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW5 VALUES ('E8','Mary',11,NULL);");

		rowCount = stmt.executeUpdate("DELETE FROM UPDATE_VIEW5;");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF;");
		rs.next();
		assertEquals(7, rs.getInt(1));
		// PASS:0282 If count = 7?

		// restore
		conn.rollback();

		stmt
				.executeUpdate("UPDATE UPDATE_VIEW6 SET GRADE = 12 WHERE CITY = 'Vienna';");
		stmt.executeUpdate("DELETE FROM UPDATE_VIEW6 WHERE GRADE = 10;");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF WHERE GRADE = 13;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0282 If count = 1?

		// restore
		conn.rollback();

		// END TEST >>> 0282 <<< END TEST

		// ****************************************************************

		// TEST:0283 Updatable VIEW with view, correlation name, NOT!

		stmt.executeUpdate("UPDATE UPDATE_VIEW7 SET GRADE = 15;");
		stmt.executeUpdate("DELETE FROM UPDATE_VIEW7 WHERE CITY = 'Akron';");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF WHERE GRADE = 15;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0283 If count = 1?

		// restore
		conn.rollback();

		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW8 VALUES ('E6','GEORGE');");
		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW8 VALUES ('E7','SHARA');");
		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW8 VALUES ('E8','DAVID');");
		rowCount = stmt
				.executeUpdate("INSERT INTO UPDATE_VIEW8 VALUES ('E9','JOHNNY');");

		rowCount = stmt
				.executeUpdate("UPDATE UPDATE_VIEW8 SET EMPNAME = 'Kathy';");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF "
				+ "WHERE GRADE IS NULL OR EMPNAME = 'Kathy';");
		rs.next();
		assertEquals(6, rs.getInt(1));

		// PASS:0283 If count = 6?

		// restore
		conn.rollback();

		stmt.executeUpdate("DELETE FROM UPDATE_VIEW9 WHERE GRADE = 12;");
		stmt.executeUpdate("UPDATE UPDATE_VIEW9 SET GRADE = 15;");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF WHERE GRADE = 15;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0283 If count = 2?

		// restore
		conn.rollback();

		// END TEST >>> 0283 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_065
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml065.
	//
	// ORIGIN: NIST file sql/dml065.sql
	//
	public void testDml_065() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0284 INSERT, SELECT char. strings with blank!

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,EMPNAME) "
				+ "VALUES ('E6','Ed');");

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,EMPNAME) "
				+ "VALUES ('E7','Ed ');");

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,EMPNAME) "
				+ "VALUES ('E8','Ed                  ');");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE EMPNAME = 'Ed';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0284 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE EMPNAME = 'Ed ';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0284 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE EMPNAME = 'Ed                ';");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0284 If count = 4?

		// restore
		conn.rollback();

		// END TEST >>> 0284 <<< END TEST

		// *************************************************

		// TEST:0285 INSERT, SELECT integer with various formats!

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,GRADE) "
				+ "VALUES ('E6',25);");

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,GRADE) "
				+ "VALUES ('E7',25.0);");

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,GRADE) "
				+ "VALUES ('E8',-25);");

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM,GRADE) "
				+ "VALUES ('E9',25.000);");

		rowCount = stmt.executeUpdate("UPDATE STAFF " + "SET GRADE = -GRADE "
				+ "WHERE GRADE < 0;");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE GRADE = 25;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            4
		//
		rs.next();
		assertEquals(4, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0285 If count = 4?

		// restore
		conn.rollback();

		// END TEST >>> 0285 <<< END TEST

		// *************************************************

		// NO_TEST:0286 Compatibility of structures and host variables!

		// Testing host identifiers

		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_068
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml068.
	//
	// ORIGIN: NIST file sql/dml068.sql
	//
	public void testDml_068() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.
		stmt.executeUpdate("CREATE TABLE AA (CHARTEST     CHAR(20));");

		// TEST:0389 95-character graphic subset of ASCII!
		// NOTE: OPTIONAL test
		// NOTE:0389 Collating sequence is implementor defined

		rowCount = stmt.executeUpdate("DELETE FROM AA;");
		// Making sure the table is empty

		// setup
		stmt.executeUpdate("INSERT INTO AA VALUES('@ at');");
		stmt.executeUpdate("INSERT INTO AA VALUES('`-qt');");
		stmt.executeUpdate("INSERT INTO AA VALUES('!exc');");
		stmt.executeUpdate("INSERT INTO AA VALUES('\"dqt');");
		stmt.executeUpdate("INSERT INTO AA VALUES('#pou');");
		stmt.executeUpdate("INSERT INTO AA VALUES('$dol');");
		stmt.executeUpdate("INSERT INTO AA VALUES('%pct');");
		stmt.executeUpdate("INSERT INTO AA VALUES('&amp');");
		stmt.executeUpdate("INSERT INTO AA VALUES('''+qt');");
		stmt.executeUpdate("INSERT INTO AA VALUES('(lpr');");
		stmt.executeUpdate("INSERT INTO AA VALUES(')rpr');");
		stmt.executeUpdate("INSERT INTO AA VALUES('*ast');");
		stmt.executeUpdate("INSERT INTO AA VALUES('aaaa');");
		stmt.executeUpdate("INSERT INTO AA VALUES(':col');");
		stmt.executeUpdate("INSERT INTO AA VALUES('+plu');");
		stmt.executeUpdate("INSERT INTO AA VALUES(';sem');");
		stmt.executeUpdate("INSERT INTO AA VALUES('[lbk');");
		stmt.executeUpdate("INSERT INTO AA VALUES('{lbc');");
		stmt.executeUpdate("INSERT INTO AA VALUES(',com');");
		stmt.executeUpdate("INSERT INTO AA VALUES('< lt');");
		stmt.executeUpdate("INSERT INTO AA VALUES('\\bsl');");
		stmt.executeUpdate("INSERT INTO AA VALUES('|dvt');");
		stmt.executeUpdate("INSERT INTO AA VALUES('-hyp');");
		stmt.executeUpdate("INSERT INTO AA VALUES('=equ');");
		stmt.executeUpdate("INSERT INTO AA VALUES(']rbk');");
		stmt.executeUpdate("INSERT INTO AA VALUES('}rbc');");
		stmt.executeUpdate("INSERT INTO AA VALUES('.per');");
		stmt.executeUpdate("INSERT INTO AA VALUES('> gt');");
		stmt.executeUpdate("INSERT INTO AA VALUES('^hat');");
		stmt.executeUpdate("INSERT INTO AA VALUES('~til');");
		stmt.executeUpdate("INSERT INTO AA VALUES('/ sl');");
		stmt.executeUpdate("INSERT INTO AA VALUES('?que');");
		stmt.executeUpdate("INSERT INTO AA VALUES('_und');");
		stmt.executeUpdate("INSERT INTO AA VALUES('AAAA');");
		stmt.executeUpdate("INSERT INTO AA VALUES('0000');");
		stmt.executeUpdate("INSERT INTO AA VALUES('9999');");
		stmt.executeUpdate("INSERT INTO AA VALUES('zzzz');");
		stmt.executeUpdate("INSERT INTO AA VALUES('  sp');");
		stmt.executeUpdate("INSERT INTO AA VALUES('ZZZZ');");

		rs = stmt.executeQuery("SELECT * FROM AA ORDER BY CHARTEST;");
		rs.next();
		assertEquals("  sp                ", rs.getString(1));
		rs.next();
		assertEquals("!exc                ", rs.getString(1));
		rs.next();
		assertEquals("\"dqt                ", rs.getString(1));
		rs.next();
		assertEquals("#pou                ", rs.getString(1));
		rs.next();
		assertEquals("$dol                ", rs.getString(1));
		rs.next();
		assertEquals("%pct                ", rs.getString(1));
		rs.next();
		assertEquals("&amp                ", rs.getString(1));
		rs.next();
		assertEquals("'+qt                ", rs.getString(1));
		rs.next();
		assertEquals("(lpr                ", rs.getString(1));
		rs.next();
		assertEquals(")rpr                ", rs.getString(1));
		rs.next();
		assertEquals("*ast                ", rs.getString(1));
		rs.next();
		assertEquals("+plu                ", rs.getString(1));
		rs.next();
		assertEquals(",com                ", rs.getString(1));
		rs.next();
		assertEquals("-hyp                ", rs.getString(1));
		rs.next();
		assertEquals(".per                ", rs.getString(1));
		rs.next();
		assertEquals("/ sl                ", rs.getString(1));
		rs.next();
		assertEquals("0000                ", rs.getString(1));
		rs.next();
		assertEquals("9999                ", rs.getString(1));
		rs.next();
		assertEquals(":col                ", rs.getString(1));
		rs.next();
		assertEquals(";sem                ", rs.getString(1));
		rs.next();
		assertEquals("< lt                ", rs.getString(1));
		rs.next();
		assertEquals("=equ                ", rs.getString(1));
		rs.next();
		assertEquals("> gt                ", rs.getString(1));
		rs.next();
		assertEquals("?que                ", rs.getString(1));
		rs.next();
		assertEquals("@ at                ", rs.getString(1));
		rs.next();
		assertEquals("AAAA                ", rs.getString(1));
		rs.next();
		assertEquals("ZZZZ                ", rs.getString(1));
		rs.next();
		assertEquals("[lbk                ", rs.getString(1));
		rs.next();
		assertEquals("\\bsl                ", rs.getString(1));
		rs.next();
		assertEquals("]rbk                ", rs.getString(1));
		rs.next();
		assertEquals("^hat                ", rs.getString(1));
		rs.next();
		assertEquals("_und                ", rs.getString(1));
		rs.next();
		assertEquals("`-qt                ", rs.getString(1));
		rs.next();
		assertEquals("aaaa                ", rs.getString(1));
		rs.next();
		assertEquals("zzzz                ", rs.getString(1));
		rs.next();
		assertEquals("{lbc                ", rs.getString(1));
		rs.next();
		assertEquals("|dvt                ", rs.getString(1));
		rs.next();
		assertEquals("}rbc                ", rs.getString(1));
		rs.next();
		assertEquals("~til                ", rs.getString(1));

		assertFalse(rs.next());
		// PASS:0389 If character in 1st position matches ?
		// PASS:0389 description in positions 2-4 ?
		// PASS:0389 If ASCII, then ORDER is: space followed by characters?
		// PASS:0389 !"#$%&'()*+,-./09:;<=>?@AZ[\]^_`az{|}~ ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM AA;");
		rs.next();
		assertEquals(39, rs.getInt(1));
		// PASS:0389 If count = 39?

		// END TEST >>> 0389 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_069
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml069.
	//
	// ORIGIN: NIST file sql/dml069.sql
	//
	public void testDml_069() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		conn.setAutoCommit(false);

		// NO_TEST:0404 2 FETCHes (different target types) on same cursor!

		// Testing cursors

		// ***************************************************************

		// NO_TEST:0405 2 cursors open from different schemas (coded join)!

		// Testing cursors

		// ***************************************************************

		// TEST:0406 Subquery from different schema!

		rowCount = stmt.executeUpdate("DELETE FROM VTABLE;");
		// Making sure the table is empty

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO VTABLE VALUES (80, 100, 100, 100, 100.0);");
		rowCount = stmt
				.executeUpdate("INSERT INTO VTABLE VALUES (40, 200, 100, 100, 100.0);");

		rs = stmt.executeQuery("SELECT PNUM " + "FROM WORKS "
				+ "WHERE EMPNUM = 'E1' AND HOURS IN "
				+ "(SELECT COL1 FROM VTABLE " + "WHERE  COL1 > 50);");
		//
		// Expected output:
		//
		// PNUM
		// ======
		//
		// P3
		//
		rs.next();
		assertEquals("P3 ", rs.getString(1));
		// PASS:0406 If PNUM = 'P3'?

		// restore
		conn.rollback();

		// END TEST >>> 0406 <<< END TEST
		// *************************************************************

		// NO_TEST:0407 SELECT INTO :XX ... WHERE :XX = !

		// Testing host variables

		// **************************************************************

		// TEST:0408 UPDATE references column value BEFORE update!

		rowCount = stmt.executeUpdate("DELETE FROM WORKS1;");
		// Making sure the table is empty

		// setup
		rowCount = stmt
				.executeUpdate("INSERT INTO WORKS1 SELECT * FROM WORKS;");

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("UPDATE WORKS1 "
							+ "SET PNUM = EMPNUM, EMPNUM = PNUM, HOURS = (HOURS + 3) * HOURS;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_13 on table WORKS1");
			}
		}

		rs = stmt.executeQuery("SELECT * FROM WORKS1 "
				+ "WHERE EMPNUM = 'P2' ORDER BY EMPNUM, PNUM ASC;");
		assertFalse(rs.next());

		// PASS:0408 If FOR ROW #1, EMPNO1 = 'P2', PNUM1 = 'E1', HOURS1 = 460?
		// PASS:0408 If FOR ROW #2, EMPNO1 = 'P2', PNUM1 = 'E2',HOURS1 = 6640?
		// PASS:0408 If FOR ROW #3, EMPNO1 = 'P2', PNUM1 = 'E3', HOURS1 = 460?
		// PASS:0408 If FOR ROW #4, EMPNO1 = 'P2', PNUM1 = 'E4', HOURS1 = 460?

		// restore
		conn.rollback();

		// END TEST >>> 0408 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_070
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml070.
	//
	// ORIGIN: NIST file sql/dml070.sql
	//
	public void testDml_070() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0409 Effective outer join -- with 2 cursors!

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E6','Lendle',17,'Potomac');");

		rs = stmt.executeQuery("SELECT PNUM, WORKS.EMPNUM, EMPNAME, HOURS "
				+ "FROM WORKS, STAFF " + "WHERE STAFF.EMPNUM = WORKS.EMPNUM "
				+ "ORDER BY 2;");
		//
		// Expected output:
		//
		// PNUM EMPNUM EMPNAME HOURS
		// ====== ====== ==================== ============
		//
		// P5 E1 Alice 12
		// P6 E1 Alice 12
		// P2 E1 Alice 20
		// P4 E1 Alice 20
		// P1 E1 Alice 40
		// P3 E1 Alice 80
		// P1 E2 Betty 40
		// P2 E2 Betty 80
		// P2 E3 Carmen 20
		// P2 E4 Don 20
		// P4 E4 Don 40
		// P5 E4 Don 80
		//
		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals("Alice               ", rs.getString(3));
		assertEquals(12, rs.getInt(4));

		rs.next();
		assertEquals("P6 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals("Alice               ", rs.getString(3));
		assertEquals(12, rs.getInt(4));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals("Alice               ", rs.getString(3));
		assertEquals(20, rs.getInt(4));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals("Alice               ", rs.getString(3));
		assertEquals(20, rs.getInt(4));

		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals("Alice               ", rs.getString(3));
		assertEquals(40, rs.getInt(4));

		rs.next();
		assertEquals("P3 ", rs.getString(1));
		assertEquals("E1 ", rs.getString(2));
		assertEquals("Alice               ", rs.getString(3));
		assertEquals(80, rs.getInt(4));

		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals("E2 ", rs.getString(2));
		assertEquals("Betty               ", rs.getString(3));
		assertEquals(40, rs.getInt(4));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E2 ", rs.getString(2));
		assertEquals("Betty               ", rs.getString(3));
		assertEquals(80, rs.getInt(4));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E3 ", rs.getString(2));
		assertEquals("Carmen              ", rs.getString(3));
		assertEquals(20, rs.getInt(4));

		rs.next();
		assertEquals("P2 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));
		assertEquals("Don                 ", rs.getString(3));
		assertEquals(20, rs.getInt(4));

		rs.next();
		assertEquals("P4 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));
		assertEquals("Don                 ", rs.getString(3));
		assertEquals(40, rs.getInt(4));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals("E4 ", rs.getString(2));
		assertEquals("Don                 ", rs.getString(3));
		assertEquals(80, rs.getInt(4));

		rowCount = 12;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(12, rowCount);
		// PASS:0409 If twelve rows are selected with ROW #9 as follows?
		// PASS:0409 PNUM WORKS.EMPNUM EMPNAME HOURS?
		// PASS:0409 P2 E3 Carmen 20?

		rs = stmt.executeQuery("SELECT 'ZZ', EMPNUM, EMPNAME, -99 "
				+ "FROM STAFF " + "WHERE NOT EXISTS (SELECT * FROM WORKS "
				+ "WHERE WORKS.EMPNUM = STAFF.EMPNUM) " + "ORDER BY EMPNUM;");
		//
		// Expected output:
		//
		//        EMPNUM EMPNAME
		// ====== ====== ==================== ============
		//
		// ZZ E5 Ed -99
		// ZZ E6 Lendle -99
		//
		rs.next();
		assertEquals("ZZ", rs.getString(1));
		assertEquals("E5 ", rs.getString(2));
		assertEquals("Ed                  ", rs.getString(3));
		assertEquals(-99, rs.getInt(4));

		rs.next();
		assertEquals("ZZ", rs.getString(1));
		assertEquals("E6 ", rs.getString(2));
		assertEquals("Lendle              ", rs.getString(3));
		assertEquals(-99, rs.getInt(4));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0409 If 2 rows are selected in the following order?
		// PASS:0409 'ZZ' STAFF.EMPNUM EMPNAME HOURS?
		// PASS:0409 ZZ E5 Ed -99?
		// PASS:0409 ZZ E6 Lendle -99?

		// restore
		conn.rollback();

		// END TEST >>> 0409 <<< END TEST
		// *****************************************************

		// TEST:0411 Effective set difference!

		rs = stmt.executeQuery("SELECT W1.EMPNUM FROM WORKS W1 "
				+ "WHERE W1.PNUM = 'P2' "
				+ "AND NOT EXISTS (SELECT * FROM WORKS W2 "
				+ "WHERE W2.EMPNUM = W1.EMPNUM " + "AND W2.PNUM = 'P1') "
				+ "ORDER BY 1 ASC;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E3
		// E4
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));

		rs.next();
		assertEquals("E4 ", rs.getString(1));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0411 If 2 rows are selected?
		// PASS:0411 If FOR ROW #1, W1.EMPNUM = 'E3'?
		// PASS:0411 If FOR ROW #2, W1.EMPNUM = 'E4'?

		// END TEST >>> 0411 <<< END TEST
		// *************************************************************

		// TEST:0412 Effective set intersection!

		rs = stmt.executeQuery("SELECT W1.EMPNUM FROM WORKS W1 "
				+ "WHERE W1.PNUM = 'P2' "
				+ "AND EXISTS (SELECT * FROM WORKS W2 "
				+ "WHERE W1.EMPNUM = W2.EMPNUM " + "AND W2.PNUM = 'P1') "
				+ "ORDER BY EMPNUM ASC;");
		//
		// Expected output:
		//
		// EMPNUM
		// ======
		//
		// E1
		// E2
		//
		rs.next();
		assertEquals("E1 ", rs.getString(1));

		rs.next();
		assertEquals("E2 ", rs.getString(1));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0412 If 2 rows are selected?
		// PASS:0412 If FOR ROW #1, W1.EMPNUM = 'E1'?
		// PASS:0412 If FOR ROW #2, W1.EMPNUM = 'E2'?

		// END TEST >>> 0412 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/*
	 * 
	 * testDml_073_SumMaxOnCartesianProduct()
	 * 
	 * TEST:0393 SUM, MAX on Cartesian product!
	 *  
	 */
	public void testDml_073_SumMaxOnCartesianProduct() throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");

		rs = stmt
				.executeQuery("SELECT SUM(HOURS), MAX(HOURS) FROM STAFF, WORKS;");
		rs.next();
		assertEquals(2320, rs.getInt(1));
		assertEquals(80, rs.getInt(2));
		// PASS:0393 If SUM(HOURS) = 2320 and MAX(HOURS) = 80?

		// END TEST >>> 0393 <<< END TEST
		// *************************************************************

		// TEST:0394 AVG, MIN on joined table with WHERE without GROUP!
		rs = stmt.executeQuery("SELECT AVG(HOURS), MIN(HOURS) "
				+ "FROM STAFF, WORKS WHERE STAFF.EMPNUM = 'E2' AND "
				+ "STAFF.EMPNUM = WORKS.EMPNUM;");
		rs.next();
		assertEquals(60, rs.getInt(1));
		assertEquals(40, rs.getInt(2));
		// PASS:0394 If AVG(HOURS) = 60 and MIN(HOURS) = 40?

		// END TEST >>> 0394 <<< END TEST
	}
	/*
	 * 
	 * testDml_073_SumMinOnJoinedTableWithGroupNoWhere()
	 * 
	 * TEST:0395 SUM, MIN on joined table with GROUP without WHERE!
	 *  
	 */
	public void testDml_073_SumMinOnJoinedTableWithGroupNoWhere()
			throws SQLException {
		BaseTab.setupBaseTab(stmt);

		rs = stmt.executeQuery("SELECT STAFF.EMPNUM, SUM(HOURS), MIN(HOURS) "
				+ "FROM STAFF, WORKS GROUP BY STAFF.EMPNUM ORDER BY 1;");
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(464, rs.getInt(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals(464, rs.getInt(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(464, rs.getInt(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(464, rs.getInt(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E5 ", rs.getString(1));
		assertEquals(464, rs.getInt(2));
		assertEquals(12, rs.getInt(3));

		assertFalse(rs.next());
		// PASS:0395 If 5 rows are selected with the following order?
		// PASS:0395 STAFF.EMPNUM SUM(HOURS) MIN(HOURS)?
		// PASS:0395 'E1' 464 12?
		// PASS:0395 'E2' 464 12?
		// PASS:0395 'E3' 464 12?
		// PASS:0395 'E4' 464 12?
		// PASS:0395 'E5' 464 12?

		// END TEST >>> 0395 <<< END TEST
		// *************************************************************

		// TEST:0396 SUM, MIN on joined table with WHERE, GROUP BY, HAVING!
		rs = stmt.executeQuery("SELECT STAFF.EMPNUM, AVG(HOURS), MIN(HOURS) "
				+ "FROM  STAFF, WORKS WHERE STAFF.EMPNUM IN ('E1','E4','E3') "
				+ "AND STAFF.EMPNUM = WORKS.EMPNUM "
				+ "GROUP BY STAFF.EMPNUM HAVING COUNT(*) > 1 "
				+ "ORDER BY STAFF.EMPNUM;");
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals(30, rs.getInt(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals(46, rs.getInt(2));
		assertEquals(20, rs.getInt(3));

		assertFalse(rs.next());
		// PASS:0396 If 2 rows are selected with the following order?
		// PASS:0396 STAFF.EMPNUM AVG(HOURS) MIN(HOURS)?
		// PASS:0396 'E1' 30 to 31 12?
		// PASS:0396 'E4' 46 to 47 20?

		// END TEST >>> 0396 <<< END TEST
		// *************************************************************
	}
	/*
	 * 
	 * testDml_073_CartesianProductGroupBy2ColumnsNoNulls()
	 * 
	 * TEST:0417 Cartesian product GROUP BY 2 columns with NULLs!
	 *  
	 */
	public void testDml_073_CartesianProductGroupBy2ColumnsNoNulls()
			throws SQLException {
		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");
		stmt.executeUpdate("INSERT INTO STAFF VALUES ("
				+ "'E6', 'David', 17, NULL);");
		stmt.executeUpdate("INSERT INTO STAFF VALUES ('"
				+ "E7', 'Tony', 18, NULL);");
		stmt.executeUpdate("INSERT INTO STAFF1 SELECT * FROM STAFF;");

		rs = stmt.executeQuery("SELECT MAX(STAFF1.GRADE), SUM(STAFF1.GRADE) "
				+ "FROM STAFF1, STAFF GROUP BY STAFF1.CITY, STAFF.CITY;");

		// this ugly code checks for our 4 required rows in the result set.
		int rowCount = 0;
		boolean found1 = false, found2 = false, found3 = false, found4 = false;
		while (rs.next()) {
			rowCount++;
			if (rs.getInt(1) == 18) {
				if (rs.getInt(2) == 35)
					found1 = true;
				if (rs.getInt(2) == 70) {
					if (found2 == false)
						found2 = true;
					else if (found3 == false)
						found3 = true;
					else if (found4 == false)
						found4 = true;
				}
			}
		}
		assertEquals(16, rowCount);
		assertTrue(found1);
		assertTrue(found2);
		assertTrue(found3);
		assertTrue(found4);
		// PASS:0417 If 16 rows are selected in any order?
		// PASS:0417 Including the following four rows?
		// PASS:0417 MAX(STAFF1.GRADE) = 18 and SUM(STAFF1.GRADE) = 35?
		// PASS:0417 MAX(STAFF1.GRADE) = 18 and SUM(STAFF1.GRADE) = 70?
		// PASS:0417 MAX(STAFF1.GRADE) = 18 and SUM(STAFF1.GRADE) = 70?
		// PASS:0417 MAX(STAFF1.GRADE) = 18 and SUM(STAFF1.GRADE) = 70?

		// END TEST >>> 0417 <<< END TEST
		// *************************************************************

		// TEST:0418 AVG, SUM, COUNT on Cartesian product with NULL!
		rs = stmt
				.executeQuery("SELECT AVG(T1.COL4), AVG(T1.COL4 + T2.COL4), "
						+ "SUM(T2.COL4), COUNT(DISTINCT T1.COL4) FROM VTABLE T1, VTABLE T2;");
		rs.next();
		assertEquals(147, rs.getInt(1));
		assertEquals(295, rs.getInt(2));
		assertEquals(1772, rs.getInt(3));
		assertEquals(3, rs.getInt(4));
		// PASS:0418 If AVG(T1.COL4) = 147 or 148?
		// PASS:0418 If AVG(T1.COL4 + T2.COL4) = 295 or 296?
		// PASS:0418 If SUM(T2.COL4) = 1772?
		// PASS:0418 If COUNT(DISTINCT T1.COL4) = 3?

		// END TEST >>> 0418 <<< END TEST
		// *************************************************************
	}
	/*
	 * 
	 * testDml_073_SumMaxMinOnJoinedTableView()
	 * 
	 * TEST:0419 SUM, MAX, MIN on joined table view!
	 *  
	 */
	public void testDml_073_SumMaxMinOnJoinedTableView() throws SQLException {
		BaseTab.setupBaseTab(stmt);
		stmt
				.executeUpdate("CREATE VIEW STAFF_WORKS_DESIGN (NAME,COST,PROJECT) "
						+ "AS SELECT EMPNAME,HOURS*2*GRADE,PNAME "
						+ "FROM   PROJ,STAFF,WORKS "
						+ "WHERE  STAFF.EMPNUM=WORKS.EMPNUM "
						+ "AND WORKS.PNUM=PROJ.PNUM " + "AND PTYPE='Design';");
		rs = stmt.executeQuery("SELECT SUM(COST), MAX(COST), MIN(COST) "
				+ "FROM STAFF_WORKS_DESIGN;");
		rs.next();
		assertEquals(3488, rs.getInt(1));
		assertEquals(960, rs.getInt(2));
		assertEquals(288, rs.getInt(3));
		// PASS:0419 If SUM(COST) = 3488, MAX(COST) = 960, MIN(COST) = 288?

		// END TEST >>> 0419 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_075
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml075.
	//
	// ORIGIN: NIST file sql/dml075.sql
	//
	public void testDml_075() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15));");

		stmt
				.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
						+ "PNAME  CHAR(20), "
						+ "PTYPE  CHAR(6), "
						+ "BUDGET DECIMAL(9), " + "CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		conn.setAutoCommit(false);

		// TEST:0431 Redundant rows in IN subquery!

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM FROM WORKS);");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0431 If count = 4?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
				+ "SELECT * FROM STAFF;");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF1 "
				+ "WHERE EMPNUM IN " + "(SELECT EMPNUM FROM WORKS);");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0431 If count = 4?

		conn.rollback();

		// END TEST >>> 0431 <<< END TEST
		// *************************************************************

		// TEST:0432 Unknown comparison predicate in ALL, SOME, ANY!

		rowCount = stmt.executeUpdate("UPDATE PROJ SET CITY = NULL "
				+ "WHERE PNUM = 'P3';");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE CITY = ALL (SELECT CITY " + "FROM PROJ "
				+ "WHERE PNAME = 'SDP');");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0432 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CITY <> ALL (SELECT CITY " + "FROM PROJ "
				+ "WHERE PNAME = 'SDP');");
		rs.next();
		assertEquals(0, rs.getInt(1));

		// PASS:0432 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CITY = ANY (SELECT CITY " + "FROM PROJ "
				+ "WHERE PNAME = 'SDP');");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0432 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CITY <> ANY (SELECT CITY " + "FROM PROJ "
				+ "WHERE PNAME = 'SDP');");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0432 If count = 3?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CITY = SOME (SELECT CITY " + "FROM PROJ "
				+ "WHERE PNAME = 'SDP');");
		rs.next();
		assertEquals(2, rs.getInt(1));

		// PASS:0432 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CITY <> SOME (SELECT CITY " + "FROM PROJ "
				+ "WHERE PNAME = 'SDP');");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0432 If count = 3?

		conn.rollback();

		// END TEST >>> 0432 <<< END TEST
		// *************************************************************

		// TEST:0433 Empty subquery in ALL, SOME, ANY!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ "
				+ "WHERE PNUM = ALL (SELECT PNUM "
				+ "FROM WORKS WHERE EMPNUM = 'E8');");
		rs.next();
		assertEquals(6, rs.getInt(1));

		// PASS:0433 If count = 6?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ "
				+ "WHERE PNUM <> ALL (SELECT PNUM "
				+ "FROM WORKS WHERE EMPNUM = 'E8');");
		rs.next();
		assertEquals(6, rs.getInt(1));

		// PASS:0433 If count = 6?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ "
				+ "WHERE PNUM = ANY (SELECT PNUM "
				+ "FROM WORKS WHERE EMPNUM = 'E8');");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0433 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ "
				+ "WHERE PNUM <> ANY (SELECT PNUM "
				+ "FROM WORKS WHERE EMPNUM = 'E8');");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0433 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ "
				+ "WHERE PNUM = SOME (SELECT PNUM "
				+ "FROM WORKS WHERE EMPNUM = 'E8');");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0433 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ "
				+ "WHERE PNUM <> SOME (SELECT PNUM "
				+ "FROM WORKS WHERE EMPNUM = 'E8');");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0433 If count = 0?

		// END TEST >>> 0433 <<< END TEST
		// *************************************************************

		// TEST:0434 GROUP BY with HAVING EXISTS-correlated set function!

		rs = stmt.executeQuery("SELECT PNUM, SUM(HOURS) FROM WORKS "
				+ "GROUP BY PNUM " + "HAVING EXISTS (SELECT PNAME FROM PROJ "
				+ "WHERE PROJ.PNUM = WORKS.PNUM AND "
				+ "SUM(WORKS.HOURS) > PROJ.BUDGET / 200);");
		//
		// Expected output:
		//
		// PNUM SUM
		// ====== =====================
		//
		// P1 80
		// P5 92
		//
		rs.next();
		assertEquals("P1 ", rs.getString(1));
		assertEquals(80, rs.getInt(2));

		rs.next();
		assertEquals("P5 ", rs.getString(1));
		assertEquals(92, rs.getInt(2));

		rowCount = 2;
		assertFalse(rs.next());
		// PASS:0434 If 2 rows selected with values (in any order):?
		// PASS:0434 PNUM = 'P1', SUM(HOURS) = 80?
		// PASS:0434 PNUM = 'P5', SUM(HOURS) = 92?

		// END TEST >>> 0434 <<< END TEST
		// *************************************************************

		// TEST:0442 DISTINCT with GROUP BY, HAVING!

		rs = stmt.executeQuery("SELECT PTYPE, CITY FROM PROJ "
				+ "GROUP BY PTYPE, CITY " + "HAVING AVG(BUDGET) > 21000;");
		rs.next();
		assertEquals("Code  ", rs.getString(1));
		assertEquals("Vienna         ", rs.getString(2));

		rs.next();
		assertEquals("Design", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));

		rs.next();
		assertEquals("Test  ", rs.getString(1));
		assertEquals("Tampa          ", rs.getString(2));

		assertFalse(rs.next());
		// PASS:0442 If 3 rows selected with PTYPE/CITY values(in any order):?
		// PASS:0442 Code/Vienna, Design/Deale, Test/Tampa?

		rs = stmt.executeQuery("SELECT DISTINCT PTYPE, CITY FROM PROJ "
				+ "GROUP BY PTYPE, CITY " + "HAVING AVG(BUDGET) > 21000;");
		rs.next();
		assertEquals("Code  ", rs.getString(1));
		assertEquals("Vienna         ", rs.getString(2));

		rs.next();
		assertEquals("Design", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));

		rs.next();
		assertEquals("Test  ", rs.getString(1));
		assertEquals("Tampa          ", rs.getString(2));
		assertFalse(rs.next());

		// PASS:0442 If 3 rows selected with PTYPE/CITY values(in any order):?
		// PASS:0442 Code/Vienna, Design/Deale, Test/Tampa?

		rs = stmt.executeQuery("SELECT DISTINCT SUM(BUDGET) FROM PROJ "
				+ "GROUP BY PTYPE, CITY " + "HAVING AVG(BUDGET) > 21000;");
		rs.next();
		assertEquals(30000, rs.getInt(1));

		rs.next();
		assertEquals(80000, rs.getInt(1));
		assertFalse(rs.next());

		// PASS:0442 If 2 rows selected (in any order):?
		// PASS:0442 with SUM(BUDGET) values 30000 and 80000?

		// END TEST >>> 0442 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_076
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml076.
	//
	// ORIGIN: NIST file sql/dml076.sql
	//
	public void testDml_076() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		// setup
		stmt.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");
		stmt.executeUpdate("CREATE TABLE EE (INTTEST     INTEGER);");
		stmt.executeUpdate("CREATE TABLE GG (REALTEST     REAL);");
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		stmt.executeUpdate("CREATE TABLE II (DOUBLETEST  DOUBLE PRECISION);");
		stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");
		stmt.executeUpdate("CREATE TABLE MM (NUMTEST  NUMERIC);");
		stmt.executeUpdate("CREATE TABLE SS (NUMTEST  DEC(13,6));");

		conn.setAutoCommit(false);

		// NO_TEST:0435 Host variables in UPDATE WHERE CURRENT!

		// Testing cursors <update statement:positioned>

		// *************************************************************

		// TEST:0436 NULL values for various SQL data types!

		rowCount = stmt.executeUpdate("INSERT INTO BB VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO EE VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO GG VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO II VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO MM VALUES(NULL);");
		rowCount = stmt.executeUpdate("INSERT INTO SS VALUES(NULL);");

		rs = stmt.executeQuery("SELECT CHARTEST FROM BB;");
		//
		// Expected output:
		//
		// CHARTEST
		// ========
		//
		// <null>
		//
		rs.next();
		assertEquals(null, rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If CHARTEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT INTTEST FROM EE;");
		//
		// Expected output:
		//
		//      INTTEST
		// ============
		//
		//       <null>
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If INTTEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
		//
		// Expected output:
		//
		//       REALTEST
		// ==============
		//
		//         <null>
		//
		rs.next();
		assertEquals(0.0, rs.getFloat(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If REALTEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM GG "
				+ "WHERE REALTEST IS NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If count = 1?

		rs = stmt.executeQuery("SELECT SMALLTEST FROM HH;");
		//
		// Expected output:
		//
		// SMALLTEST
		// =========
		//
		//    <null>
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If SMALLTEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
		//
		// Expected output:
		//
		//              DOUBLETEST
		// =======================
		//
		//                  <null>
		//
		rs.next();
		assertEquals(0.0, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If DOUBLETEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM II "
				+ "WHERE DOUBLETEST IS NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If count = 1?

		rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
		//
		// Expected output:
		//
		//      FLOATTEST
		// ==============
		//
		//         <null>
		//
		rs.next();
		assertEquals(0.0, rs.getFloat(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If FLOATTEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
				+ "WHERE FLOATTEST IS NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If count = 1?

		rs = stmt.executeQuery("SELECT NUMTEST FROM MM;");
		//
		// Expected output:
		//
		//      NUMTEST
		// ============
		//
		//       <null>
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If NUMTEST is NULL (Implementor defined print format)?

		rs = stmt.executeQuery("SELECT NUMTEST FROM SS;");
		//
		// Expected output:
		//
		//               NUMTEST
		// =====================
		//
		//                <null>
		//
		rs.next();
		assertEquals(0.0, rs.getDouble(1), 0.0);

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0436 If NUMTEST is NULL (Implementor defined print format)?

		// restore
		conn.rollback();

		// END TEST >>> 0436 <<< END TEST
		// *************************************************************

		// NO_TEST:0437 NULL values for various host variable types!

		// Testing Host Variables & Indicator Variables

		// *************************************************************

		// NO_TEST:0410 NULL value in OPEN CURSOR!

		// Testing Cursors & Indicator Variables

		// *************************************************************

		// NO_TEST:0441 NULL value for various predicates!

		// Testing Indicator Variables

		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_077
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml077.
	//
	// ORIGIN: NIST file sql/dml077.sql
	//
	public void testDml_077() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE VIEW TEMP_SS(EMPNUM,GRADE,CITY) "
				+ "AS SELECT EMPNUM,GRADE,CITY " + "FROM   STAFF "
				+ "WHERE  GRADE > 12 " + "WITH CHECK OPTION;");

		conn.setAutoCommit(false);

		// TEST:0443 VIEW with check option rejects unknown!

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO TEMP_SS VALUES('E7',NULL,'Frankfurt');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0443 If ERROR, view check constraint, 0 rows inserted?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO TEMP_SS VALUES('E8',NULL,'Atlanta');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0443 If ERROR, view check constraint, 0 rows inserted?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO TEMP_SS(EMPNUM) VALUES('E9');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0443 If ERROR, view check constraint, 0 rows inserted?

		rowCount = stmt.executeUpdate("UPDATE WORKS " + "SET HOURS = NULL "
				+ "WHERE PNUM = 'P2';");
		assertEquals(4, rowCount);

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO TEMP_SS "
					+ "SELECT PNUM,HOURS,'Nowhere' " + "FROM WORKS "
					+ "WHERE EMPNUM = 'E1';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0443 If ERROR, view check constraint, 0 rows inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE TEMP_SS "
					+ "SET GRADE = NULL " + "WHERE EMPNUM = 'E3';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0443 If ERROR, view check constraint, 0 rows updated?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE TEMP_SS "
					+ "SET GRADE = NULL " + "WHERE EMPNUM = 'E5';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table TEMP_SS");
			}
		}
		// PASS:0443 If ERROR, view check constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE GRADE IS NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0443 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_SS;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            2
		//
		rs.next();
		assertEquals(2, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0443 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0443 If count = 5?

		conn.rollback();

		// END TEST >>> 0443 <<< END TEST
		// *********************************************

		// NO_TEST:0444 Updatable cursor, modify value selected on!

		// NOTE:0444 Testing cursors.
		// *********************************************

		// NO_TEST:0445 Values not assigned to targets for SQLCODE=100 !

		// NOTE:0445 Testing host variables.

		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_079
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml079.
	//
	// ORIGIN: NIST file sql/dml079.sql
	//	
	public void testDml_079() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0451 UNIQUEness is case sensitive!
		rowCount = stmt.executeUpdate("UPDATE STAFF SET EMPNUM = 'e2' "
				+ "WHERE EMPNUM = 'E4';");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row updated?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO STAFF(EMPNUM) VALUES ('E1');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_2 on table STAFF");
			}
		}
		// PASS:0451 If ERROR, unique constraint, 0 rows inserted?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF(EMPNUM) "
				+ "VALUES ('e1');");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row inserted?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("UPDATE STAFF SET EMPNUM = 'E1' WHERE EMPNUM = 'e1';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_2 on table STAFF");
			}
		}
		// PASS:0451 If ERROR, unique constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT * FROM STAFF;");
		//
		// Expected output:
		//
		// EMPNUM EMPNAME GRADE CITY
		// ====== ==================== ============ ===============
		//
		// E1 Alice 12 Deale
		// E2 Betty 10 Vienna
		// E3 Carmen 13 Vienna
		// e1 <null> <null> <null>
		// e2 Don 12 Deale
		// E5 Ed 13 Akron
		//
		// TODO: The Firebird result set differs from what is
		//        expected.
		//        "E1", "E2", "E3", "e2", "E5", "e1"
		//        Is this acceptable?
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("Alice               ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals("Deale          ", rs.getString(4));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("Betty               ", rs.getString(2));
		assertEquals(10, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("Carmen              ", rs.getString(2));
		assertEquals(13, rs.getInt(3));
		assertEquals("Vienna         ", rs.getString(4));

		rs.next();
		assertEquals("e2 ", rs.getString(1));
		assertEquals("Don                 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));
		assertEquals("Deale          ", rs.getString(4));

		rs.next();
		assertEquals("E5 ", rs.getString(1));
		assertEquals("Ed                  ", rs.getString(2));
		assertEquals(13, rs.getInt(3));
		assertEquals("Akron          ", rs.getString(4));

		rs.next();
		assertEquals("e1 ", rs.getString(1));
		assertEquals(null, rs.getString(2));
		assertEquals(0, rs.getInt(3));
		assertEquals(null, rs.getString(4));

		rowCount = 6;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(6, rowCount);
		// PASS:0451 If 6 rows are selected?
		// PASS:0451 If EMPNUMs are 'e1','e2','E1','E2','E3','E5'?

		rowCount = stmt.executeUpdate("INSERT INTO WORKS (EMPNUM,PNUM) "
				+ "VALUES ('e1','p2');");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row inserted?

		rowCount = stmt.executeUpdate("INSERT INTO WORKS (EMPNUM,PNUM) "
				+ "VALUES ('E1','p2');");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row inserted?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO WORKS (EMPNUM,PNUM) VALUES ('E1','P2');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_7 on table WORKS");
			}
		}
		// PASS:0451 If ERROR, unique constraint, 0 rows inserted?

		rowCount = stmt.executeUpdate("INSERT INTO WORKS (EMPNUM,PNUM) "
				+ "VALUES ('e1', 'P2');");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row inserted?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("UPDATE WORKS SET EMPNUM = 'E1' WHERE PNUM = 'P5' "
							+ "AND EMPNUM = 'E4';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_7 on table WORKS");
			}
		}
		// PASS:0451 If ERROR, unique constraint, 0 rows updated?

		rowCount = stmt.executeUpdate("UPDATE WORKS SET EMPNUM = 'e1 ' "
				+ "WHERE PNUM = 'P5' AND EMPNUM = 'E4';");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row updated?

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("UPDATE WORKS SET PNUM = 'P4' WHERE PNUM = 'P2' "
							+ "AND EMPNUM = 'E4';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544665) {
				fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
						+ "INTEG_7 on table WORKS");
			}
		}
		// PASS:0451 If ERROR, unique constraint, 0 rows updated?

		rowCount = stmt.executeUpdate("UPDATE WORKS SET PNUM = 'p4' "
				+ "WHERE PNUM = 'P2' AND EMPNUM = 'E4';");
		assertEquals(1, rowCount);
		// PASS:0451 If 1 row updated?

		rs = stmt.executeQuery("SELECT * FROM WORKS ORDER BY EMPNUM, PNUM;");
		//
		// Expected output:
		//
		// EMPNUM PNUM HOURS
		// ====== ====== ============
		//
		// E1 P1 40
		// E1 P2 20
		// E1 P3 80
		// E1 P4 20
		// E1 P5 12
		// E1 P6 12
		// E1 p2 <null>
		// E2 P1 40
		// E2 P2 80
		// E3 P2 20
		// E4 P4 40
		// E4 p4 20
		// e1 P2 <null>
		// e1 P5 80
		// e1 p2 <null>
		//  
		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P3 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("P6 ", rs.getString(2));
		assertEquals(12, rs.getInt(3));

		rs.next();
		assertEquals("E1 ", rs.getString(1));
		assertEquals("p2 ", rs.getString(2));
		assertEquals(0, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P1 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E2 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("P4 ", rs.getString(2));
		assertEquals(40, rs.getInt(3));

		rs.next();
		assertEquals("E4 ", rs.getString(1));
		assertEquals("p4 ", rs.getString(2));
		assertEquals(20, rs.getInt(3));

		rs.next();
		assertEquals("e1 ", rs.getString(1));
		assertEquals("P2 ", rs.getString(2));
		assertEquals(0, rs.getInt(3));

		rs.next();
		assertEquals("e1 ", rs.getString(1));
		assertEquals("P5 ", rs.getString(2));
		assertEquals(80, rs.getInt(3));

		rs.next();
		assertEquals("e1 ", rs.getString(1));
		assertEquals("p2 ", rs.getString(2));
		assertEquals(0, rs.getInt(3));

		rowCount = 15;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(15, rowCount);
		// PASS:0451 If 15 rows are selected?
		// PASS:0451 If EMPNUM/PNUM values include ?
		// PASS:0451 e1/p2, E1/p2, e1/P2, e1/P5, E4/p4 ?
		// PASS:0451 If no EMPNUM/PNUM values are duplicates ?

		conn.rollback();

		// END TEST >>> 0451 <<< END TEST
		// *********************************************

		// TEST:0452 Order of precedence, left-to-right in UNION [ALL]!
		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF UNION "
				+ "SELECT EMPNAME FROM STAFF UNION ALL "
				+ "SELECT EMPNAME FROM STAFF;");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		// Betty
		// Carmen
		// Don
		// Ed
		// Alice
		// Betty
		// Carmen
		// Don
		// Ed
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rs.next();
		assertEquals("Carmen              ", rs.getString(1));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));

		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rs.next();
		assertEquals("Carmen              ", rs.getString(1));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));

		rowCount = 10;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(10, rowCount);
		// PASS:0452 If 10 rows selected?

		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF UNION ALL "
				+ "SELECT EMPNAME FROM STAFF UNION "
				+ "SELECT EMPNAME FROM STAFF;");
		//
		// Expected output:
		//
		// EMPNAME
		// ====================
		//
		// Alice
		// Betty
		// Carmen
		// Don
		// Ed
		//
		rs.next();
		assertEquals("Alice               ", rs.getString(1));

		rs.next();
		assertEquals("Betty               ", rs.getString(1));

		rs.next();
		assertEquals("Carmen              ", rs.getString(1));

		rs.next();
		assertEquals("Don                 ", rs.getString(1));

		rs.next();
		assertEquals("Ed                  ", rs.getString(1));

		rowCount = 5;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(5, rowCount);
		// PASS:0452 If 5 rows selected?

		// END TEST >>> 0452 <<< END TEST
		// *********************************************

		// TEST:0453 NULL with empty subquery of ALL, SOME, ANY!

		rowCount = stmt.executeUpdate("UPDATE PROJ SET CITY = NULL "
				+ "WHERE PNAME = 'IRM';");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ WHERE CITY IS NULL;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ WHERE CITY = ALL "
				+ "(SELECT CITY FROM STAFF WHERE EMPNUM = 'E8');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            6
		//
		rs.next();
		assertEquals(6, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 6?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ WHERE CITY <> "
				+ "ALL (SELECT CITY FROM STAFF WHERE EMPNUM = 'E8');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            6
		//
		rs.next();
		assertEquals(6, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 6?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM PROJ "
						+ "WHERE CITY = ANY (SELECT CITY FROM STAFF WHERE EMPNUM = 'E8');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 0?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM PROJ "
						+ "WHERE CITY <> ANY (SELECT CITY FROM STAFF WHERE EMPNUM = 'E8');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 0?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM PROJ "
						+ "WHERE CITY = SOME (SELECT CITY FROM STAFF WHERE EMPNUM = 'E8');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 0?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM PROJ "
						+ "WHERE CITY <> SOME (SELECT CITY FROM STAFF WHERE EMPNUM = 'E8');");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0453 If count = 0?

		conn.rollback();

		// END TEST >>> 0453 <<< END TEST

		//*************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_080
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml080.
	//
	// ORIGIN: NIST file sql/dml080.sql
	//
	public void testDml_080() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// NOTE Direct support for SQLCODE or SQLSTATE is not required
		// NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
		// NOTE ********************* instead ***************************
		// NOTE If a statement raises an exception condition,
		// NOTE then the system shall display a message indicating that
		// NOTE the statement failed, giving a textual description
		// NOTE of the failure.
		// NOTE If a statement raises a completion condition that is a
		// NOTE "warning" or "no data", then the system shall display
		// NOTE a message indicating that the statement completed,
		// NOTE giving a textual description of the "warning" or "no data."

		// TEST:0462 SQLCODE 100: DELETE with no data!

		rowCount = stmt.executeUpdate("DELETE FROM STAFF "
				+ "WHERE EMPNUM = 'E7';");
		assertEquals(0, rowCount);
		// PASS:0462 If SQLCODE = 100 or no data, 0 rows deleted?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0462 If 5 rows deleted?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF;");
		assertEquals(0, rowCount);
		// PASS:0462 If SQLCODE = 100 or no data, 0 rows deleted?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF "
				+ "WHERE EMPNUM = 'E1';");
		assertEquals(0, rowCount);
		// PASS:0462 If SQLCODE = 100 or no data, 0 rows deleted?

		// restore
		conn.rollback();

		// END TEST >>> 0462 <<< END TEST
		// *********************************************

		// TEST:0463 SQLCODE 100: UPDATE with no data!

		rowCount = stmt.executeUpdate("UPDATE STAFF " + "SET CITY = 'NOWHERE' "
				+ "WHERE EMPNAME = 'NOBODY';");
		assertEquals(0, rowCount);
		// PASS:0463 If SQLCODE = 100 or no data, 0 rows updated?

		rowCount = stmt.executeUpdate("UPDATE STAFF " + "SET GRADE = 11;");
		assertEquals(5, rowCount);
		// PASS:0463 If 5 rows updated?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF;");
		assertEquals(5, rowCount);
		// PASS:0463 If 5 rows deleted?

		rowCount = stmt
				.executeUpdate("UPDATE STAFF " + "SET CITY = 'NOWHERE';");
		assertEquals(0, rowCount);
		// PASS:0463 If SQLCODE = 100 or no data, 0 rows updated?

		rowCount = stmt.executeUpdate("UPDATE STAFF " + "SET CITY = 'NOWHERE' "
				+ "WHERE EMPNAME = 'NOBODY';");
		assertEquals(0, rowCount);
		// PASS:0463 If SQLCODE = 100 or no data, 0 rows updated?

		// restore
		conn.rollback();

		// END TEST >>> 0463 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	/*
	 * 
	 * testDml_081_Sqlstate00000()
	 * 
	 * TEST:0487 SQLSTATE 00000: successful completion!
	 *  
	 */
	public void testDml_081_Sqlstate00000() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);

		// NOTE Direct support for SQLCODE or SQLSTATE is not required
		// NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
		// NOTE ********************* instead ***************************
		// NOTE If a statement raises an exception condition,
		// NOTE then the system shall display a message indicating that
		// NOTE the statement failed, giving a textual description
		// NOTE of the failure.
		// NOTE If a statement raises a completion condition that is a
		// NOTE "warning" or "no data", then the system shall display
		// NOTE a message indicating that the statement completed,
		// NOTE giving a textual description of the "warning" or "no data."

		// TEST:0487 SQLSTATE 00000: successful completion!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:0487 If count = 12?
		// PASS:0487 OR SQLSTATE = 00000: successful completion?

		// END TEST >>> 0487 <<< END TEST
		// *********************************************

		// TEST:0488 SQLSTATE 21000: cardinality violation!
		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS "
					+ "WHERE PNUM = (SELECT PNUM FROM WORKS "
					+ "WHERE HOURS = 80);");
			fail();
			// PASS:0488 If ERROR, cardinality violation, 0 rows selected?
			// PASS:0488 OR SQLSTATE = 21000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544652, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		// END TEST >>> 0488 <<< END TEST
		// *********************************************

		// TEST:0489 SQLSTATE 02000: no data!

		rs = stmt.executeQuery("SELECT GRADE FROM STAFF "
				+ "WHERE EMPNUM = 'xx';");
		assertFalse(rs.next());
		// PASS:0489 If 0 rows selected?
		// PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

		rowCount = stmt.executeUpdate("DELETE FROM STAFF WHERE GRADE = 11;");
		assertEquals(0, rowCount);
		// PASS:0489 If 0 rows deleted?
		// PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

		rowCount = stmt.executeUpdate("INSERT INTO STAFF (EMPNUM,GRADE) "
				+ "SELECT EMPNUM, 9 FROM WORKS WHERE PNUM = 'X9';");
		assertEquals(0, rowCount);
		// PASS:0489 If 0 rows inserted?
		// PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

		rowCount = stmt.executeUpdate("UPDATE STAFF SET CITY = 'Ho' "
				+ "WHERE GRADE = 15;");
		assertEquals(0, rowCount);
		// PASS:0489 If 0 rows updated?
		// PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

		// END TEST >>> 0489 <<< END TEST
		// *********************************************
	}

	/*
	 * 
	 * testDml_081_DataExceptionDivisionByZero()
	 * 
	 * TEST:0490 SQLSTATE 22012: data exception/division by zero!
	 *  
	 */
	public void testDml_081_DataExceptionDivisionByZero() throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// TEST:0490 SQLSTATE 22012: data exception/division by zero!
		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E6','Fidel',0,'Havana');");

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE EMPNAME = 'Fidel' AND 16/GRADE > 2;");
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544321, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			rs = stmt.executeQuery("SELECT 16/GRADE FROM STAFF "
					+ "WHERE EMPNAME = 'Fidel';");
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544321, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}

		}

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "GROUP BY CITY HAVING SUM(GRADE/0) > 44;");
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
			fail();
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544321, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE GRADE = (SELECT 16/GRADE FROM STAFF "
					+ "WHERE EMPNUM = 'E6');");
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544321, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			stmt.executeUpdate("UPDATE STAFF SET GRADE = GRADE/0 "
					+ "WHERE GRADE = 12;");
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// updated?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544321, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			stmt.executeUpdate("INSERT INTO STAFF "
					+ "SELECT 'X','Y',HOURS/0,'z' FROM WORKS "
					+ "WHERE PNUM = 'P6';");
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// inserted?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544321, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		// END TEST >>> 0490 <<< END TEST
		// *********************************************
	}

	/*
	 * 
	 * testDml_082_Sqlstate22019DataException()
	 * 
	 * TEST:0492 SQLSTATE 22019: data exception/invalid escape char!
	 *  
	 */
	public void testDml_082_Sqlstate22019DataException() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		stmt.executeUpdate("CREATE TABLE CPBASE " + "(KC INT NOT NULL, "
				+ "JUNK1 CHAR (10), " + "PRIMARY KEY (KC));");
		stmt.executeUpdate("CREATE TABLE BASE_WCOV (C1 INT);");
		stmt.executeUpdate("CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE "
				+ "C1 > 0 WITH CHECK OPTION;");

		// NOTE Direct support for SQLCODE or SQLSTATE is not required
		// NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
		// NOTE ********************* instead ***************************
		// NOTE If a statement raises an exception condition,
		// NOTE then the system shall display a message indicating that
		// NOTE the statement failed, giving a textual description
		// NOTE of the failure.
		// NOTE If a statement raises a completion condition that is a
		// NOTE "warning" or "no data", then the system shall display
		// NOTE a message indicating that the statement completed,
		// NOTE giving a textual description of the "warning" or "no data."

		// TEST:0492 SQLSTATE 22019: data exception/invalid escape char!
		stmt.executeUpdate("UPDATE STAFF SET CITY = 'Percent%Xunder_' "
				+ "WHERE EMPNUM = 'E1';");

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE CITY LIKE '%XX%X_%' ESCAPE 'XX';");
			fail();
			// PASS:0492 If ERROR, data exception/invalid escape char?
			// PASS:0492 0 rows selected OR SQLSTATE = 22019 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE CITY LIKE '%XX%X_%' ESCAPE 'X';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0492 If count = 1?

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE CITY LIKE '%XX_%' ESCAPE 'XX';");
			fail();
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE CITY LIKE '%XX_%' ESCAPE 'XX';");
			fail();
			// PASS:0492 If ERROR, data exception/invalid escape char?
			// PASS:0492 0 rows selected OR SQLSTATE = 22019 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF WHERE CITY "
				+ "LIKE '%XX_%' ESCAPE 'X';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0492 If count = 1?

		// END TEST >>> 0492 <<< END TEST
		// *********************************************
	}
	/*
	 * 
	 * testDml_082_Sqlstate22025DataException()
	 * 
	 * TEST:0492 SQLSTATE 22025: data exception/invalid escape char!
	 *  
	 */
	public void testDml_082_Sqlstate22025DataException() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		// TEST:0493 SQLSTATE 22025: data exception/invalid escape seq.!

		stmt.executeUpdate("CREATE TABLE CPBASE " + "(KC INT NOT NULL, "
				+ "JUNK1 CHAR (10), " + "PRIMARY KEY (KC));");
		stmt.executeUpdate("CREATE TABLE BASE_WCOV (C1 INT);");
		stmt.executeUpdate("CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE "
				+ "C1 > 0 WITH CHECK OPTION;");
		rowCount = stmt.executeUpdate("DELETE FROM CPBASE;");
		rowCount = stmt.executeUpdate("INSERT INTO CPBASE "
				+ "VALUES(82,'Per%X&und_');");

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM CPBASE WHERE JUNK1 LIKE 'P%X%%X' ESCAPE 'X';");
			fail();
			// PASS:0493 If ERROR, data exception/invalid escape seq.?
			// PASS:0493 0 rows selected OR SQLSTATE = 22025 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}

		}
		rs = stmt.executeQuery("SELECT COUNT(*) FROM CPBASE WHERE JUNK1 "
				+ "LIKE 'P%X%%' ESCAPE 'X';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0493 If count = 1?

		try {
			rowCount = stmt.executeUpdate("INSERT INTO STAFF "
					+ "SELECT 'E12','ff',KC,'gg' FROM CPBASE "
					+ "WHERE JUNK1 LIKE '%X%%Xd_' ESCAPE 'X';");
			fail();
			// PASS:0493 If ERROR, data exception/invalid escape seq.?
			// PASS:0493 0 rows inserted OR SQLSTATE = 22025 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}

		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF "
				+ "SELECT 'E13','ff',KC,'gg' FROM CPBASE "
				+ "WHERE JUNK1 LIKE '%X%%X_' ESCAPE 'X';"));
		// PASS:0493 If 1 row is inserted?

		try {
			rowCount = stmt.executeUpdate("UPDATE CPBASE SET KC = -1 "
					+ "WHERE JUNK1 LIKE '%?X%' ESCAPE '?';");
			fail();
			// PASS:0493 If ERROR, data exception/invalid escape seq.?
			// PASS:0493 0 rows updated OR SQLSTATE = 22025 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}

		}

		rowCount = stmt.executeUpdate("UPDATE CPBASE SET KC = -1 "
				+ "WHERE JUNK1 LIKE '%?%X%' ESCAPE '?';");
		assertEquals(1, rowCount);
		// PASS:0493 If 1 row is updated?

		try {
			stmt.executeUpdate("DELETE FROM CPBASE "
					+ "WHERE JUNK1 LIKE '_e%&u%' ESCAPE '&';");
			fail();
			// PASS:0493 If ERROR, data exception/invalid escape seq.?
			// PASS:0493 0 rows deleted OR SQLSTATE = 22025 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544702, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}

		}

		assertEquals(1, stmt.executeUpdate("DELETE FROM CPBASE "
				+ "WHERE JUNK1 LIKE '_e%&&u%' ESCAPE '&';"));
		// PASS:0493 If 1 row is deleted?
		// END TEST >>> 0493 <<< END TEST
		// *********************************************
	}

	/*
	 * 
	 * testDml_082_Sqlstate22003DataExceptionNumericValueOutOfRange()
	 * 
	 * TEST:0494 SQLSTATE 22003: data exception/numeric value out of range!
	 *  
	 */
	public void testDml_082_Sqlstate22003DataExceptionNumericValueOutOfRange()
			throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		stmt.executeUpdate("CREATE TABLE CPBASE " + "(KC INT NOT NULL, "
				+ "JUNK1 CHAR (10), " + "PRIMARY KEY (KC));");
		stmt.executeUpdate("CREATE TABLE BASE_WCOV (C1 INT);");
		stmt.executeUpdate("CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE "
				+ "C1 > 0 WITH CHECK OPTION;");

		// TEST:0494 SQLSTATE 22003: data exception/numeric value out of range!
		rowCount = stmt.executeUpdate("DELETE FROM HH;");

		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (10);");
		assertEquals(1, rowCount);
		// PASS:0494 If 1 row is inserted?
		// PASS:0494 OR ERROR, data exception/numeric value out of range?
		// PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (100);");
		assertEquals(1, rowCount);
		// PASS:0494 If 1 row is inserted?
		// PASS:0494 OR ERROR, data exception/numeric value out of range?
		// PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (1000);");
		assertEquals(1, rowCount);
		// PASS:0494 If 1 row is inserted?
		// PASS:0494 OR ERROR, data exception/numeric value out of range?
		// PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (10000);");
		assertEquals(1, rowCount);
		// PASS:0494 If 1 row is inserted?
		// PASS:0494 OR ERROR, data exception/numeric value out of range?
		// PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (100000);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544321) {
				fail("Should return arithmetic exception, numeric overflow, or string truncation.");
			}
		}
		// PASS:0494 If 1 row is inserted?
		// PASS:0494 OR ERROR, data exception/numeric value out of range?
		// PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (1000000);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544321) {
				fail("Should return arithmetic exception, numeric overflow, or string truncation.");
			}
		}
		// PASS:0494 If 1 row is inserted?
		// PASS:0494 OR ERROR, data exception/numeric value out of range?
		// PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		// END TEST >>> 0494 <<< END TEST
		// *********************************************

	}
	/*
	 * 
	 * testDml_082_Sqlstate44000WithCheckOptionViolation()
	 * 
	 * TEST:0505 SQLSTATE 44000: with check option violation!
	 *  
	 */
	public void testDml_082_Sqlstate44000WithCheckOptionViolation()
			throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		stmt.executeUpdate("CREATE TABLE CPBASE " + "(KC INT NOT NULL, "
				+ "JUNK1 CHAR (10), " + "PRIMARY KEY (KC));");
		stmt.executeUpdate("CREATE TABLE BASE_WCOV (C1 INT);");
		stmt.executeUpdate("CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE "
				+ "C1 > 0 WITH CHECK OPTION;");

		// TEST:0505 SQLSTATE 44000: with check option violation!

		try {
			rowCount = stmt.executeUpdate("INSERT INTO WCOV VALUES (0);");
			fail();
			// PASS:0505 If ERROR, with check option violation?
			// PASS:0505 0 rows inserted OR SQLSTATE = 44000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			String fbError = "GDS Exception. 335544558. Operation violates CHECK constraint  on view or table WCOV";
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
				assertEquals(fbError, sqle.getMessage().substring(0,
						fbError.length()));
		}

		stmt.executeUpdate("INSERT INTO WCOV VALUES (75);");
		// PASS:0505 If 1 row is inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE WCOV SET C1 = -C1 "
					+ "WHERE C1 = 75;");

		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544558) {
				fail("Should return Operation violates CHECK constraint on view or table WCOV");
			}
		}
		// PASS:0505 If ERROR, with check option violation?
		// PASS:0505 0 rows updated OR SQLSTATE = 44000 OR SQLCODE < 0?

		// END TEST >>> 0505 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_083
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml083.
	//
	// ORIGIN: NIST file sql/dml083.sql
	//
	public void testDml_083() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		conn.setAutoCommit(false);

		// NOTE Direct support for SQLCODE or SQLSTATE is not required
		// NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
		// NOTE ********************* instead ***************************
		// NOTE If a statement raises an exception condition,
		// NOTE then the system shall display a message indicating that
		// NOTE the statement failed, giving a textual description
		// NOTE of the failure.
		// NOTE If a statement raises a completion condition that is a
		// NOTE "warning" or "no data", then the system shall display
		// NOTE a message indicating that the statement completed,
		// NOTE giving a textual description of the "warning" or "no data."

		// NO_TEST:0496 SQLSTATE 22002: data exception/null, value, no indic.!

		// Testing indicators

		// *********************************************

		// TEST:0498 SQLSTATE 22001: data exception/string right trunc.!

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO STAFF "
							+ "VALUES ('E6','Earl Brown',11,'Claggetsville Maryland');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544321) {
				fail("Should return arithmetic exception, numeric overflow, or string truncation.");
			}
		}
		// PASS:0498 If ERROR, data exception/string right trunc.?
		// PASS:0498 If 0 rows inserted OR SQLSTATE = 22001 OR SQLCODE < 0?

		try {
			rowCount = stmt
					.executeUpdate("INSERT INTO STAFF "
							+ "VALUES ('E7','Ella Brown',12,'Claggetsville Maryland');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544321) {
				fail("Should return arithmetic exception, numeric overflow, or string truncation.");
			}
		}
		// PASS:0498 If ERROR, data exception/string right trunc.?
		// PASS:0498 If 0 rows inserted OR SQLSTATE = 22001 OR SQLCODE < 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0498 If count = 5?

		// restore
		conn.rollback();

		// END TEST >>> 0498 <<< END TEST
		// *********************************************

		// TEST:0500 SQLSTATE 01003: warning/null value elim. in set function!

		// setup
		rowCount = stmt.executeUpdate("DELETE FROM HH;");
		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (3);");
		rowCount = stmt.executeUpdate("INSERT INTO HH VALUES (NULL);");

		rs = stmt.executeQuery("SELECT AVG(SMALLTEST) FROM HH;");
		//
		// Expected output:
		//
		//                   AVG
		// =====================
		//
		//                     3
		//
		rs.next();
		assertEquals(3, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0500 If WARNING, null value eliminated in set function?
		// PASS:0500 OR SQLSTATE = 01003?

		// setup
		rowCount = stmt.executeUpdate("UPDATE STAFF " + "SET GRADE = NULL "
				+ "WHERE GRADE = 13;");

		rs = stmt.executeQuery("SELECT AVG(GRADE) " + "FROM STAFF "
				+ "WHERE CITY = 'Vienna';");
		//
		// Expected output:
		//
		//                   AVG
		// =====================
		//
		//                    10
		//
		rs.next();
		assertEquals(10, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0500 If WARNING, null value eliminated in set function?
		// PASS:0500 OR SQLSTATE = 01003?

		rs = stmt.executeQuery("SELECT SUM(DISTINCT GRADE) " + "FROM STAFF;");
		//
		// Expected output:
		//
		//                   SUM
		// =====================
		//
		//                    22
		//
		rs.next();
		assertEquals(22, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0500 If WARNING, null value eliminated in set function?
		// PASS:0500 OR SQLSTATE = 01003?

		rowCount = stmt.executeUpdate("INSERT INTO HH " + "SELECT MAX(GRADE) "
				+ "FROM STAFF;");
		// PASS:0500 If WARNING, null value eliminated in set function?
		// PASS:0500 OR SQLSTATE = 01003?

		rowCount = stmt.executeUpdate("DELETE FROM HH "
				+ "WHERE SMALLTEST < (SELECT MIN(GRADE) " + "FROM STAFF "
				+ "WHERE CITY = 'Vienna');");
		// PASS:0500 If WARNING, null value eliminated in set function?
		// PASS:0500 OR SQLSTATE = 01003?

		rs = stmt.executeQuery("SELECT CITY, COUNT(DISTINCT GRADE) "
				+ "FROM STAFF " + "GROUP BY CITY " + "ORDER BY CITY DESC;");
		//
		// Expected output:
		//
		// CITY COUNT
		// =============== ============
		//
		// Vienna 1
		// Deale 1
		// Akron 0
		//
		rs.next();
		assertEquals("Vienna         ", rs.getString(1));
		assertEquals(1, rs.getInt(2));

		rs.next();
		assertEquals("Deale          ", rs.getString(1));
		assertEquals(1, rs.getInt(2));

		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertEquals(0, rs.getInt(2));

		rowCount = 3;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(3, rowCount);
		// PASS:0500 If 3 rows are selected with the following order?
		// PASS:0500 CITY COUNT(DISTINCT GRADE)?
		// PASS:0500 'Vienna' 1?
		// PASS:0500 'Deale' 1?
		// PASS:0500 'Akron' 0?
		// PASS:0500 OR SQLSTATE = 01003?

		// restore
		conn.rollback();

		// END TEST >>> 0500 <<< END TEST
		// *********************************************

		// NO_TEST:0501 SQLSTATE 01004: warning/string right truncation!

		// Testing host variables

		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_084
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml084.
	//
	// ORIGIN: NIST file sql/dml084.sql
	//
	public void testDml_084() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// NOTE Direct support for SQLCODE or SQLSTATE is not required
		// NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
		// NOTE ********************* instead ***************************
		// NOTE If a statement raises an exception condition,
		// NOTE then the system shall display a message indicating that
		// NOTE the statement failed, giving a textual description
		// NOTE of the failure.
		// NOTE If a statement raises a completion condition that is a
		// NOTE "warning" or "no data", then the system shall display
		// NOTE a message indicating that the statement completed,
		// NOTE giving a textual description of the "warning" or "no data."

		// TEST:0503 SQLSTATE 42000: syntax error or access rule vio.1!

		rs = stmt.executeQuery("SELECT COL2 " + "FROM UPUNIQ "
				+ "WHERE NUMKEY = 1;");
		//
		// Expected output:
		//
		// COL2
		// ======
		//
		// A
		//
		rs.next();
		assertEquals("A ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// TODO: What is correct?
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows selected OR SQLSTATE = 42000 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("UPDATE UPUNIQ SET COL2 = 'xx';");
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows updated OR SQLSTATE = 42000 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("DELETE FROM UPUNIQ;");
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows deleted OR SQLSTATE = 42000 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("INSERT INTO UPUNIQ VALUES (9,'M');");
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows inserted OR SQLSTATE = 42000 OR SQLCODE < 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE GRADE < " + "(SELECT MAX(HOURS) FROM WORKS) "
				+ "OR    GRADE > " + "(SELECT MAX(NUMKEY) FROM UPUNIQ) "
				+ "OR    GRADE + 100 > " + "(SELECT MIN(HOURS) FROM WORKS);");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            5
		//
		rs.next();
		assertEquals(5, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows selected OR SQLSTATE = 42000 OR SQLCODE < 0?

		rowCount = stmt
				.executeUpdate("INSERT INTO UPUNIQ " + "VALUES (13,44);");
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows inserted OR SQLSTATE = 42000 OR SQLCODE < 0?

		errorCode = 0;
		try {
			stmt.executeUpdate("INSERT INTO UPUNIQ " + "VALUES (555666777);");
			fail();
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				// error message changed between firebird and vulcan but will
				// always contain the following substring.
				String fbError = "Count of read-write columns does not equal count of values";
				assertTrue(sqle.getMessage().indexOf(fbError) != -1);
			}
		}
		// PASS:0503 If ERROR, syntax error or access rule violation?
		// PASS:0503 0 rows inserted OR SQLSTATE = 42000 OR SQLCODE < 0?

		// restore
		conn.rollback();

		// END TEST >>> 0503 <<< END TEST
		// *********************************************

		// TEST:0504 SQLSTATE 42000: syntax error or access rule vio.2!

		// NOTE: OPTIONAL test
		// NOTE: This test is passed by default if SQLSTATE
		//       is not supported in the Interactive SQL interface

		// NOTE: Test numbers 0503 and 0504 check for SQLSTATE
		// NOTE: 42000 on syntax errors and access violations.
		// NOTE: SQL-92 permits, but does not require, an
		// NOTE: implementation to achieve a high level of security
		// NOTE: by returning the same error for an access
		// NOTE: violation as for a reference to a non-existent
		// NOTE: table. This test exercises several different
		// NOTE: types of syntax errors and access violations. If
		// NOTE: you are trying for a high security level, please
		// NOTE: insure that the behavior of all these errors are
		// NOTE: indistinguishable.
		// NOTE:
		// NOTE: For minimal SQL-92 conformance, each run time
		// NOTE: error must produce SQLSTATE 42000 or 42 with some
		// NOTE: implementor-defined subclass. The subclass can
		// NOTE: be different for each error.

		// setup, note SQLSTATE value, if supported in Interactive SQL
		rs = stmt.executeQuery("SELECT COL2 " + "FROM UPUNIQ "
				+ "WHERE NUMKEY = 1;");
		rs.next();
		assertEquals("A ", rs.getString(1));
		assertFalse(rs.next());

		try {
			rs = stmt.executeQuery("SELECT COL2 " + "FROM UPUPUP "
					+ "WHERE NUMKEY = 1;");
			fail();
			// PASS:0504 If the SQLSTATE value is the same as?
			// PASS:0504 the SQLSTATE value of the previous SELECT?
		} catch (SQLException sqle) {
		}
		stmt.executeUpdate("UPDATE UPUNIQ " + "SET COL2 = 'xx';");

		try {
			rowCount = stmt
					.executeUpdate("UPDATE UPUPUP " + "SET COL2 = 'xx';");
			fail();
			// PASS:0504 If the SQLSTATE value is the same as?
			// PASS:0504 the SQLSTATE value of the previous UPDATE?
		} catch (SQLException sqle) {
			// error handling changed from FB to vulcan, but the error should
			// contain the string "Table Unknown"

			String fbError = "Table unknown";
			assertTrue(sqle.getMessage().indexOf(fbError) != -1);
			assertEquals("HY000", sqle.getSQLState());
		}

		stmt.executeUpdate("DELETE FROM UPUNIQ;");

		try {
			stmt.executeUpdate("DELETE FROM UPUPUP;");
			fail();
			// PASS:0504 If the SQLSTATE value is the same as?
			// PASS:0504 the SQLSTATE value of the previous DELETE?
		} catch (SQLException sqle) {
			// error handling changed from FB to vulcan, but the error should
			// contain the string "Table Unknown"
			String fbError = "Table unknown";
			assertTrue(sqle.getMessage().indexOf(fbError) != -1);
			assertEquals("HY000", sqle.getSQLState());
		}

		stmt.executeUpdate("INSERT INTO UPUNIQ " + "VALUES (9,'M');");
		try {
			stmt.executeUpdate("INSERT INTO UPUPUP " + "VALUES (9,'M');");
			fail();
			// PASS:0504 If the SQLSTATE value is the same as?
			// PASS:0504 the SQLSTATE value of the previous INSERT?
		} catch (SQLException sqle) {
			// error handling changed from FB to vulcan, but the error should
			// contain the string "Table Unknown"
			String fbError = "Table unknown";
			assertTrue(sqle.getMessage().indexOf(fbError) != -1);
			assertEquals("HY000", sqle.getSQLState());
		}

		// setup, note SQLSTATE value, if supported in Interactive SQL
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE GRADE < " + "(SELECT MAX(HOURS) FROM WORKS) "
				+ "OR    GRADE > " + "(SELECT MAX(NUMKEY) FROM UPUNIQ) "
				+ "OR    GRADE + 100 > " + "(SELECT MIN(HOURS) FROM WORKS);");
		rs.next();
		assertEquals(5, rs.getInt(1));

		errorCode = 0;
		try {
			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
					+ "WHERE GRADE < " + "(SELECT MAX(HOURS) FROM WORKS) "
					+ "OR    GRADE > " + "(SELECT MAX(NUMKEY) FROM UPUPUP) "
					+ "OR    GRADE + 100 > "
					+ "(SELECT MIN(HOURS) FROM WORKS);");
			fail();
			// PASS:0504 If the SQLSTATE value is the same as?
			// PASS:0504 the SQLSTATE value of the previous SELECT?
		} catch (SQLException sqle) {
			// error handling changed from FB to vulcan, but the error should
			// contain the string "Table Unknown"
			String fbError = "Table unknown";
			assertTrue(sqle.getMessage().indexOf(fbError) != -1);
			assertEquals("HY000", sqle.getSQLState());
		}
		// END TEST >>> 0504 <<< END TEST
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_085
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml085.
	//
	// ORIGIN: NIST file sql/dml085.sql
	//
	public void testDml_085() throws SQLException {
		int errorCode; // Error code.
		int rowCount;
		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);

		// TEST:0508 Delimited identifers!

		// Support for schemas not implemented.
		//		rs = stmt.executeQuery("SELECT COUNT(DISTINCT \"sullivan.select\") "
		//				+ "FROM \"FLATER\".\"SULLIVAN.SELECT\";");
		//		rs.next();
		//		assertEquals(2, rs.getInt(1));
		// PASS:0508 If count = 2?

		// Support for schemas not implemented
		//		rs = stmt.executeQuery("SELECT \"A<a\".\"sullivan.select\" "
		//				+ "FROM \"FLATER\".\"SULLIVAN.SELECT\" \"A<a\";");
		//		int rowCount = 0;
		//		while (rs.next())
		//			rowCount++;
		//		assertEquals(4, rowCount);
		// PASS:0508 If 4 rows selected?
		// PASS:0508 If for each row, "sullivan.select" = 0 OR 1 ?

		rs = stmt.executeQuery("SELECT \"A < a\".CITY "
				+ "FROM STAFF \"A < a\" " + "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"0\".CITY " + "FROM STAFF \"0\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"\"\"\".CITY "
				+ "FROM STAFF  \"\"\"\" " + "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"%\".CITY " + "FROM STAFF  \"%\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"&\".CITY " + "FROM STAFF  \"&\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"'\".CITY " + "FROM STAFF  \"'\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"(\".CITY " + "FROM STAFF  \"(\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \")\".CITY " + "FROM STAFF  \")\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"*\".CITY " + "FROM STAFF  \"*\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"+\".CITY " + "FROM STAFF  \"+\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \",\".CITY " + "FROM STAFF  \",\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"-\".CITY " + "FROM STAFF  \"-\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \".\".CITY " + "FROM STAFF  \".\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"/\".CITY " + "FROM STAFF  \"/\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \":\".CITY " + "FROM STAFF  \":\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \";\".CITY " + "FROM STAFF  \";\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"<\".CITY " + "FROM STAFF  \"<\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"=\".CITY " + "FROM STAFF  \"=\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \">\".CITY " + "FROM STAFF  \">\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"?\".CITY " + "FROM STAFF  \"?\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"_\".CITY " + "FROM STAFF  \"_\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"|\".CITY " + "FROM STAFF  \"|\" "
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		rs = stmt.executeQuery("SELECT \"|_?=;:/. -,+*)'&\"\"%\".CITY "
				+ "FROM STAFF  \"|_?=;:/. -,+*)'&\"\"%\""
				+ "WHERE EMPNUM = 'E5';");
		rs.next();
		assertEquals("Akron          ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0508 If 1 row selected and CITY = 'Akron'?

		conn.rollback();
		// END TEST >>> 0508 <<< END TEST
		// *********************************************

		// TEST:0509 Renaming columns with AS for ORDER BY!
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			// firebird doesn't support the renamed columns in the order by 
			rs = stmt
					.executeQuery("SELECT GRADE AS PROVOLONE, EMPNAME AS EDAM "
							+ "FROM STAFF " + "ORDER BY PROVOLONE, EDAM DESC;");
			rs.next();
			assertEquals(10, rs.getInt(1)); 
			rs.next();
			assertEquals(12, rs.getInt(1));
			rs.next();
			assertEquals(12, rs.getInt(1));
			rs.next();
		    assertEquals(13, rs.getInt(1)); 
			rs.next(); 
			assertEquals(13, rs.getInt(1));
			assertFalse(rs.next()); 
			// PASS:0509 If 5 rows are selected with the following order?
			// PASS:0509 PROVOLONE EDAM ?
			// PASS:0509 10 'Betty' ?
			// PASS:0509 12 'Don' ?
			// PASS:0509 12 'Alice' ?
			// PASS:0509 13 'Ed' ?
			// PASS:0509 13 'Carmen' ?
		}
		// restore

		// END TEST >>> 0509 <<< END TEST
		// *********************************************

		// NO_TEST:0510 <parameter name> = <column name> (OK with SQL-92)!

		// Testing embedded variables

		// *********************************************

		// TEST:0554 More column renaming!

		rs = stmt.executeQuery("SELECT PROJ.CITY AS PCITY, STAFF.CITY SCITY, "
				+ "BUDGET + GRADE * HOURS * 100  REAL_BUDGET "
				+ "FROM STAFF, PROJ, WORKS "
				+ "WHERE WORKS.EMPNUM = STAFF.EMPNUM "
				+ "AND WORKS.PNUM = PROJ.PNUM " + "AND EMPNAME = 'Alice' "
				+ "AND PROJ.PNUM = 'P3';");
		//
		// Expected output:
		//
		// PCITY SCITY REAL_BUDGET
		// =============== =============== =====================
		//
		// Tampa Deale 126000
		//
		rs.next();
		assertEquals("Tampa          ", rs.getString(1));
		assertEquals("Deale          ", rs.getString(2));
		assertEquals(126000, rs.getInt(3));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0554 If PCITY = 'Tampa' AND SCITY = 'Deale'?
		// PASS:0554 AND REAL_BUDGET = 126000?

		// restore
		conn.rollback();

		// END TEST >>> 0554 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_086
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml086.
	//
	// ORIGIN: NIST file sql/dml086.sql
	//
	public void testDml_086() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE BASE_VS1 (C1 INT, C2 INT);");
		stmt
				.executeUpdate("CREATE VIEW VS1 AS SELECT * FROM BASE_VS1 WHERE C1 = 0;");
		stmt.executeUpdate("CREATE VIEW VS2 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE EXISTS "
				+ "(SELECT B.C2 FROM BASE_VS1 B WHERE B.C2 = A.C1);");
		stmt.executeUpdate("CREATE VIEW VS3 AS "
				+ "SELECT A.C2 FROM BASE_VS1 A WHERE A.C2 IN "
				+ "(SELECT B.C1 FROM BASE_VS1 B WHERE B.C1 < A.C2);");
		stmt.executeUpdate("CREATE VIEW VS4 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < ALL "
				+ "(SELECT B.C2 FROM BASE_VS1 B);");
		stmt.executeUpdate("CREATE VIEW VS5 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < SOME "
				+ "(SELECT B.C2 FROM BASE_VS1 B);");
		stmt.executeUpdate("CREATE VIEW VS6 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < ANY "
				+ "(SELECT B.C2 FROM BASE_VS1 B);");

		stmt.executeUpdate("CREATE VIEW V_WORKS1 " + "AS SELECT * FROM WORKS "
				+ "WHERE HOURS > 15 " + "WITH CHECK OPTION;");
		stmt.executeUpdate("CREATE VIEW V_WORKS2 "
				+ "AS SELECT * FROM V_WORKS1 " + "WHERE EMPNUM = 'E1' "
				+ "OR EMPNUM = 'E6';");
		stmt.executeUpdate("CREATE VIEW V_WORKS3 "
				+ "AS SELECT * FROM V_WORKS2 " + "WHERE PNUM = 'P2' "
				+ "OR PNUM = 'P7' " + "WITH CHECK OPTION;");

		conn.setAutoCommit(false);

		// TEST:0511 CHECK clauses in nested views (clarified in SQL-92)!

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO V_WORKS2 "
					+ "VALUES('E9','P7',13);");
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
				assertTrue (sqle.getMessage().indexOf("Operation violates CHECK constraint")!= -1 ); 
		}
		// PASS:0511 If ERROR, view check constraint, 0 rows inserted?

		stmt.executeUpdate("INSERT INTO V_WORKS2 " + "VALUES('E7','P4',95);");
		// PASS:0511 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO V_WORKS3 " + "VALUES('E8','P2',85);");
		// PASS:0511 If ERROR, view check constraint, 0 rows inserted?

		// NOTE:0511 SQL-92 GR11a of 11.19 requires implicit CASCADE of
		// checking.
		// NOTE:0511 ERROR (failure to insert row) means outer check option
		// (checking)
		// NOTE:0511 implies inner check option (checking), thus implicit
		// NOTE:0511 CASCADE of checking.
		// NOTE:0511 Successful insertion of row means outer check option
		// (checking)
		// NOTE:0511 does not imply inner check option (checking).

		stmt.executeUpdate("INSERT INTO V_WORKS3	" + "VALUES('E1','P7',90);");
		// PASS:0511 If 1 row is inserted?

		try {
			rowCount = stmt.executeUpdate("INSERT INTO V_WORKS3 "
					+ "VALUES('E9','P2',10);");
			fail();
			// PASS:0511 If ERROR, view check constraint, 0 rows inserted?
		} catch (SQLException sqle) {

			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				String fbError = "GDS Exception. 335544558. Operation violates CHECK constraint  on view or table V_WORKS1";
				String foo = sqle.getMessage(); 
				
				assertEquals(fbError, sqle.getMessage().substring(0,
						fbError.length()));
			}
		}

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM  WORKS "
				+ "WHERE EMPNUM = 'E9';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0511 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM  WORKS "
				+ "WHERE HOURS > 85;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0511 If count = 2?

		// TODO: Comment does match result! RowCount is 2?
		stmt.executeUpdate("UPDATE V_WORKS3 "
				+ "SET EMPNUM = 'E12', HOURS = 222 "
				+ "WHERE EMPNUM = 'E1' AND PNUM = 'P2';");
		// PASS:0511 If ERROR, view check constraint, 0 rows updated?

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO WORKS "
				+ "VALUES('E6','P2',55);");
		assertEquals(1, rowCount);

		stmt.executeUpdate("UPDATE V_WORKS3 "
				+ "SET EMPNUM = 'E13', HOURS = 222 "
				+ "WHERE EMPNUM = 'E6' AND PNUM = 'P2';");
		// PASS:0511 If ERROR, view check constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
				+ "WHERE HOURS = 222;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// TODO: Comment indicates count should be 0?
		// PASS:0511 If count = 0?
		// END TEST >>> 0511 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_087
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml087.
	//
	// ORIGIN: NIST file sql/dml087.sql
	//
	public void testDml_087() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE VIEW DV1 AS "
				+ "SELECT DISTINCT HOURS FROM WORKS;");
		stmt.executeUpdate("CREATE TABLE BASE_VS1 (C1 INT, C2 INT);");
		stmt
				.executeUpdate("CREATE VIEW VS1 AS SELECT * FROM BASE_VS1 WHERE C1 = 0;");
		stmt.executeUpdate("CREATE VIEW VS2 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE EXISTS "
				+ "(SELECT B.C2 FROM BASE_VS1 B WHERE B.C2 = A.C1);");
		stmt.executeUpdate("CREATE VIEW VS3 AS "
				+ "SELECT A.C2 FROM BASE_VS1 A WHERE A.C2 IN "
				+ "(SELECT B.C1 FROM BASE_VS1 B WHERE B.C1 < A.C2);");
		stmt.executeUpdate("CREATE VIEW VS4 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < ALL "
				+ "(SELECT B.C2 FROM BASE_VS1 B);");
		stmt.executeUpdate("CREATE VIEW VS5 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < SOME "
				+ "(SELECT B.C2 FROM BASE_VS1 B);");
		stmt.executeUpdate("CREATE VIEW VS6 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < ANY "
				+ "(SELECT B.C2 FROM BASE_VS1 B);");

		// TEST:0518 CREATE VIEW with DISTINCT!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM DV1;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0518 If count = 4?

		rs = stmt.executeQuery("SELECT HOURS FROM DV1 "
				+ "ORDER BY HOURS DESC;");
		rs.next();
		assertEquals(80, rs.getInt(1));

		rs.next();
		assertEquals(40, rs.getInt(1));

		rs.next();
		assertEquals(20, rs.getInt(1));

		rs.next();
		assertEquals(12, rs.getInt(1));

		assertFalse(rs.next());
		// PASS:0518 If 4 rows selected AND first HOURS = 80?
		// PASS:0518 AND second HOURS = 40 AND third HOURS = 20?
		// PASS:0518 AND fourth HOURS = 12?

		// END TEST >>> 0518 <<< END TEST;
		// *********************************************;

		// TEST:0519 CREATE VIEW with subqueries!

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VS2 "
				+ "WHERE C1 = 0;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0519 If count = 2?
		// TODO: NIST says result should be 2, but we get back 0. wbo

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VS2 "
				+ "WHERE C1 = 1;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0519 If count = 2?
		// TODO: wbo - we get 0, but NIST says we should get 2.

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VS3;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0519 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VS4;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0519 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VS5;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0519 If count = 2?
		// TODO: NIST says we should get 2, but we got 0. wbo

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM VS6;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0519 If count = 2?
		// TODO: NIST says result should be 2, but firebird returns 0

		// END TEST >>> 0519 <<< END TEST;
		// *********************************************;

		// TEST:0520 Underscores are legal an significant!
		stmt.executeUpdate("CREATE TABLE CONCATBUF (ZZ CHAR(240));");
		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE U_SIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("INSERT INTO USIG VALUES (0,2);");
		stmt.executeUpdate("INSERT INTO USIG VALUES (1,3);");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG "
				+ "WHERE C1 = 0;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0520 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG "
				+ "WHERE C1 = 2;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0520 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG "
				+ "WHERE C_1 = 0;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            0
		//
		rs.next();
		assertEquals(0, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0520 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG "
				+ "WHERE C_1 = 2;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0520 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG "
				+ "WHERE C1 = 4;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0520 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM U_SIG "
				+ "WHERE C1 = 0;");
		rs.next();
		assertEquals(0, rs.getInt(1));

		stmt.executeUpdate("DELETE FROM U_SIG;");
		stmt.executeUpdate("INSERT INTO U_SIG VALUES (4,6);");
		stmt.executeUpdate("INSERT INTO U_SIG VALUES (5,7);");

		// TODO: need to compare against original - wbo
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM U_SIG "
				+ "WHERE C1 = 4;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0520 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF U_CN "
				+ "WHERE U_CN.GRADE IN " + "(SELECT UCN.GRADE "
				+ "FROM STAFF UCN " + "WHERE UCN.GRADE > 10);");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0520 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE GRADE > 10;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0520 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE GRADE < 10;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0520 If count = 0?
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_090
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml090.
	//
	// ORIGIN: NIST file sql/dml090.sql
	//
	public void testDml_090() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		stmt
				.executeUpdate("CREATE TABLE TEMP_OBSERV "
						+ "(YEAR_OBSERV  NUMERIC(4), "
						+ "CITY         CHAR(10), "
						+ "MAX_TEMP     NUMERIC(5,2), "
						+ "MIN_TEMP     NUMERIC(5,2));");

		conn.setAutoCommit(false);

		// TEST:0512 <value expression> for IN predicate!

		rs = stmt.executeQuery("SELECT MIN(PNAME) "
				+ "FROM PROJ, WORKS, STAFF " + "WHERE PROJ.PNUM = WORKS.PNUM "
				+ "AND WORKS.EMPNUM = STAFF.EMPNUM "
				+ "AND BUDGET - GRADE * HOURS * 100 IN "
				+ "(-4400, -1000, 4000);");
		//
		// Expected output:
		//
		// MIN
		// ====================
		//
		// CALM
		//
		rs.next();
		assertEquals("CALM                ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0512 If PNAME = 'CALM'?

		rs = stmt.executeQuery("SELECT CITY, COUNT(*) " + "FROM PROJ "
				+ "GROUP BY CITY " + "HAVING (MAX(BUDGET) - MIN(BUDGET)) / 2 "
				+ "IN (2, 20000, 10000) " + "ORDER BY CITY DESC;");
		//
		// Expected output:
		//
		// CITY COUNT
		// =============== ============
		//
		// Vienna 2
		// Deale 3
		//
		rs.next();
		assertEquals("Vienna         ", rs.getString(1));
		assertEquals(2, rs.getInt(2));

		rs.next();
		assertEquals("Deale          ", rs.getString(1));
		assertEquals(3, rs.getInt(2));

		rowCount = 2;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(2, rowCount);
		// PASS:0512 If in first row: CITY = 'Vienna' AND count = 2?
		// PASS:0512 AND in second row: CITY = 'Deale' AND count = 3?

		// restore
		conn.rollback();

		// END TEST >>> 0512 <<< END TEST
		// *********************************************;

		// TEST:0513 NUMERIC(4) implies CHECK BETWEEN -9999 AND 9999!

		// setup
		rowCount = stmt.executeUpdate("DELETE FROM TEMP_OBSERV;");

		rowCount = stmt.executeUpdate("INSERT INTO TEMP_OBSERV (YEAR_OBSERV) "
				+ "VALUES (9999);");
		assertEquals(1, rowCount);
		// PASS:0513 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO TEMP_OBSERV (YEAR_OBSERV)"
				+ "VALUES (10000);");
		// TODO: Comment doesn't match result!
		// PASS:0513 If ERROR, constraint violation, 0 rows inserted?

		rowCount = stmt.executeUpdate("UPDATE TEMP_OBSERV "
				+ "SET YEAR_OBSERV = -10000 " + "WHERE YEAR_OBSERV = 9999;");
		// TODO: Comment doesn't match result!
		// PASS:0513 If ERROR, constraint violation, 0 rows updated?

		rowCount = stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV (YEAR_OBSERV, MAX_TEMP) "
						+ "VALUES (-9999, 123.4517);");
		// PASS:0513 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_OBSERV "
				+ "WHERE MAX_TEMP = 123.45 "
				+ "AND MAX_TEMP NOT BETWEEN 123.4516 AND 123.4518;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            1
		//
		rs.next();
		assertEquals(1, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0513 If count = 1?

		rowCount = stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV (YEAR_OBSERV, MAX_TEMP) "
						+ "VALUES (-9999, 1234.51);");
		// TODO: Comment doesn't match result!
		// PASS:0513 If ERROR, constraint violation, 0 rows inserted?

		// restore
		conn.rollback();

		// END TEST >>> 0513 <<< END TEST
		// *********************************************;

		// TEST:0523 <value expression> for BETWEEN predicate!

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM PROJ "
				+ "WHERE 24 * 1000 BETWEEN BUDGET - 5000 AND 50000 / 1.7;");
		//
		// Expected output:
		//
		//        COUNT
		// ============
		//
		//            3
		//
		rs.next();
		assertEquals(3, rs.getInt(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0523 If count = 3?

		rs = stmt.executeQuery("SELECT PNAME " + "FROM PROJ "
				+ "WHERE 'Tampa' NOT BETWEEN CITY AND 'Vienna' "
				+ "AND PNUM > 'P2';");
		//
		// Expected output:
		//
		// PNAME
		// ====================
		//
		// IRM
		//
		rs.next();
		assertEquals("IRM                 ", rs.getString(1));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0523 If PNAME = 'IRM'?

		rs = stmt.executeQuery("SELECT CITY, COUNT(*) " + "FROM PROJ "
				+ "GROUP BY CITY "
				+ "HAVING 50000 + 2 BETWEEN 33000 AND SUM(BUDGET) - 20;");
		//
		// Expected output:
		//
		// CITY COUNT
		// =============== ============
		//
		// Deale 3
		//
		rs.next();
		assertEquals("Deale          ", rs.getString(1));
		assertEquals(3, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0523 If CITY = 'Deale' and count = 3?

		// restore
		conn.rollback();

		// END TEST >>> 0523 <<< END TEST
		// *********************************************;

		// TEST:0564 Outer ref. directly contained in HAVING clause!

		rs = stmt.executeQuery("SELECT EMPNUM, GRADE*1000 "
				+ "FROM STAFF WHERE GRADE * 1000 > ANY "
				+ "(SELECT SUM(BUDGET) FROM PROJ " + "GROUP BY CITY, PTYPE "
				+ "HAVING PROJ.CITY = STAFF.CITY);");
		//
		// Expected output:
		//
		// EMPNUM
		// ====== =====================
		//
		// E3 13000
		//
		rs.next();
		assertEquals("E3 ", rs.getString(1));
		assertEquals(13000, rs.getInt(2));

		rowCount = 1;
		while (rs.next()) {
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0564 If EMPNUM = E3 and GRADE * 1000 = 13000?

		// END TEST >>> 0564 <<< END TEST
		// *************************************************////END-OF-MODULE;
	}

	//-----------------------------------------------------------------*
	// NAME: testDml_091
	//-----------------------------------------------------------------*
	//
	// Statements used to exercise test sql/dml091.
	//
	// ORIGIN: NIST file sql/dml091.sql
	//	
	public void testDml_091() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		BaseTab.setupBaseTab(stmt);
		// NOTE Direct support for SQLCODE or SQLSTATE is not required
		// NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
		// NOTE ********************* instead ***************************
		// NOTE If a statement raises an exception condition,
		// NOTE then the system shall display a message indicating that
		// NOTE the statement failed, giving a textual description
		// NOTE of the failure.
		// NOTE If a statement raises a completion condition that is a
		// NOTE "warning" or "no data", then the system shall display
		// NOTE a message indicating that the statement completed,
		// NOTE giving a textual description of the "warning" or "no data."

		// TEST:0497 SQLSTATE 22003: data exception/numeric val.range 2!

		stmt.executeUpdate("CREATE TABLE P1 (NUMTEST  NUMERIC(1));");
		stmt.executeUpdate("CREATE TABLE P7 (NUMTEST  NUMERIC(7));");
		stmt.executeUpdate("CREATE TABLE P12 (NUMTEST  NUMERIC(12));");
		stmt.executeUpdate("CREATE TABLE P15 (NUMTEST  NUMERIC(15));");

		stmt.executeUpdate("CREATE TABLE FOUR_TYPES " + "(T_INT     INTEGER, "
				+ "T_CHAR    CHAR(10), " + "T_DECIMAL DECIMAL(10,2), "
				+ "T_REAL    REAL);");

		rowCount = stmt.executeUpdate("DELETE FROM P1;");
		rowCount = stmt.executeUpdate("DELETE FROM FOUR_TYPES;");

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("INSERT INTO P1 VALUES (100000);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544321) {
				fail("Should return arithmetic exception, numeric overflow, or string truncation.");
			}
		}
		// PASS:0497 If 1 row is inserted?
		// PASS:0497 OR ERROR, data exception/numeric value out of range?
		// PASS:0497 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		try {
			stmt.executeUpdate("INSERT INTO P1 VALUES (-1000000);");
			if (DB_DIALECT.equalsIgnoreCase("firebirdsq"))
				fail();
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsq")) {
				String fbError = "GDS Exception. 335544321. arithmetic exception, numeric overflow, or string truncation";
				assertEquals(fbError, sqle.getMessage().substring(0,
						fbError.length()));
			}
		}
		// PASS:0497 If 1 row is inserted?
		// PASS:0497 OR ERROR, data exception/numeric value out of range?
		// PASS:0497 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

		rowCount = stmt.executeUpdate("INSERT INTO P1 VALUES (-9);");
		assertEquals(1, rowCount);
		// PASS:0497 If 1 row is inserted?

		rowCount = stmt.executeUpdate("INSERT INTO P1 VALUES (9);");
		assertEquals(1, rowCount);
		// PASS:0497 If 1 row is inserted?

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("UPDATE P1 "
					+ "SET NUMTEST = NUMTEST + 100000 WHERE NUMTEST = 9;");
			// PASS:0497 If 1 row is updated?
			// PASS:0497 OR ERROR, data exception/numeric value out of range?
			// PASS:0497 OR 0 rows updated OR SQLSTATE = 22003 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
				assertTrue (sqle.getMessage().indexOf("arithmetic exception, numeric overflow, or string truncation") != -1);
		}

		try {
			rowCount = stmt.executeUpdate("UPDATE P1 SET NUMTEST = "
					+ "((1 + NUMTEST) * 100000) "
					+ "WHERE NUMTEST = 100009 OR "
					+ "NUMTEST IN (SELECT GRADE - 4 FROM STAFF);");
			// PASS:0497 If 1 row is updated?
			// PASS:0497 OR ERROR, data exception/numeric value out of range?
			// PASS:0497 OR 0 rows updated OR SQLSTATE = 22003 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
				assertTrue (sqle.getMessage().indexOf("arithmetic exception, numeric overflow, or string truncation") != -1);
		}

		errorCode = 0;
		try {
			rowCount = stmt
					.executeUpdate("UPDATE P1 "
							+ "SET NUMTEST = NUMTEST * 200000 "
							+ "WHERE NUMTEST = -9;");
			// PASS:0497 If 1 row is updated?
			// PASS:0497 OR ERROR, data exception/numeric value out of range?
			// PASS:0497 OR 0 rows updated OR SQLSTATE = 22003 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
				assertTrue (sqle.getMessage().indexOf("arithmetic exception, numeric overflow, or string truncation") != -1);
		}

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO FOUR_TYPES "
				+ "VALUES (1,'X',11112222.00,.000003E-25);");

		errorCode = 0;
		try {
			rs = stmt.executeQuery("SELECT T_DECIMAL / .000000001 "
					+ "FROM FOUR_TYPES " + "WHERE T_CHAR = 'X';");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544321) {
				fail("Should return arithmetic exception, numeric overflow, or string truncation.");
			}
		}
		// PASS:0497 If 1 row is selected and T_DECIMAL = 1.1112222E+16 ?
		// PASS:0497 OR ERROR, data exception/numeric value out of range?
		// PASS:0497 OR 0 rows selected OR SQLSTATE = 22003 OR SQLCODE < 0?

		// NOTE:0497 If the following values are too large (not supported),
		// NOTE:0497 use TEd to decrease them to maximum allowed.
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");
		rowCount = stmt
				.executeUpdate("INSERT INTO FOUR_TYPES (T_REAL) VALUES (-1.555222E+38);");

		// NOTE:0497 If we have not inserted enough big values into FOUR_TYPES,
		// NOTE:0497 to cause an ERROR on the SELECT SUM(T_REAL) below, then
		// NOTE:0497 use TEd to enlarge the above values for T_REAL to the
		// NOTE:0497 maximum allowed by your implementation.
		// NOTE:0497 If that is not enough, add more INSERTs.

		rs = stmt.executeQuery("SELECT SUM(T_REAL) FROM FOUR_TYPES;");
		//
		// Expected output:
		//
		//                     SUM
		// =======================
		//
		// -2.488355210669293e+039
		rs.next();
		assertEquals(-2.488355210669293e+039, rs.getDouble(1), 0.0);
		// PASS:0497 If ERROR, data exception/numeric value out of range?
		// PASS:0497 OR 0 rows selected OR SQLSTATE = 22003 OR SQLCODE < 0?

		// END TEST >>> 0497 <<< END TEST
		// *************************************************////END-OF-MODULE
	}

/*
 * 
 * testDml_099_ImplicitNumericCasting () 
 * 
 * TEST:0582 Implicit numeric casting (feature 9) static!
 * 
 */
	public void testDml_099_ImplicitNumericCasting () throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		// TEST:0582 Implicit numeric casting (feature 9) static!
		stmt.executeUpdate("CREATE TABLE ICAST2 (C1 INT, C2 DOUBLE PRECISION, C3 NUMERIC(5,3));");

		stmt
				.executeUpdate("INSERT INTO ICAST2 VALUES (.31416E+1, 3, .3142293E+1);");
		// PASS:0582 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT C1, C2, C3 FROM ICAST2;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(3.000000000000000, rs.getDouble(2), 0.0);
		assertEquals(3.142, rs.getFloat(3), 0.0001);
		assertFalse(rs.next());
		// PASS:0582 If 1 row is selected with C1 = 3 and C3 = 3.142?

		assertEquals(
				1,
				stmt
						.executeUpdate("UPDATE ICAST2 SET C1 = 5.2413E+0, C2 = 5, C3 = 5.2413E+0;"));
		// PASS:0582 If 1 row is updated?

		rs = stmt.executeQuery("SELECT C1, C2, C3 FROM ICAST2;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		assertEquals(5.000000000000000, rs.getDouble(2), 0.0);
		assertEquals(5.241, rs.getFloat(3), 0.0001);
		assertFalse(rs.next());
		// PASS:0582 If 1 row is selected with C1 = 5 and C3 = 5.241?

		assertEquals(
				1,
				stmt
						.executeUpdate("UPDATE ICAST2 SET C1 = 6.28E+0, C2 = 2.1E+0, C3 = .07E+2;"));
		// PASS:0582 If 1 row is updated?

		assertEquals(1, stmt
				.executeUpdate("UPDATE ICAST2 SET C1 = C2, C3 = C3 + C2;"));
		// PASS:0582 If 1 row is updated?

		rs = stmt.executeQuery("SELECT C1, C2, C3 FROM ICAST2;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(2.100000000000000, rs.getDouble(2), 0.0);
		assertEquals(9.100, rs.getFloat(3), 0.0001);
		assertFalse(rs.next()); 
		// PASS:0582 If 1 row is selected with C1 = 2 and C3 = 9.100?

		// END TEST >>> 0582 <<< END TEST
		// *********************************************
	}
}