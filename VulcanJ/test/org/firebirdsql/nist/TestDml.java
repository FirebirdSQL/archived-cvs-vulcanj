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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		}
		assertEquals(1, rowCount);
		// PASS:0025 If count = 2?

		// restore
		rowCount++;
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
			System.out.println(errorCode);
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
						+ "FROM STAFF, PROJ "
						+ "WHERE STAFF.CITY = PROJ.CITY "
						+ "order by EMPNAME, PNAME;");

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
						+ "FROM STAFF, PROJ "
						+ "WHERE STAFF.CITY = PROJ.CITY "
						+ "order by EMPNAME, PNAME;");

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
			rs.next();
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			assertEquals("Should return multiple rows in singleton select.",
					335544652, errorCode);
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
			rs.next(); 
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
}