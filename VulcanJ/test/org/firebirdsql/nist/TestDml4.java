// ---------------------------------------------------------------------*
//
// Name: TestDml4.java
// Author: sasgsf
// Purpose: Regression tests for NIST ISQL Data Manipulation (DML)
//          tests.
// History:
//      Date Description
//    --------- ------------------------------------------------------
//    04APR2005 Split apart from TestDml.java since it was
//    too slow on MVS.
//    09AUG2004 Initial creation.
//---------------------------------------------------------------------*
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDml4 extends NistTestBase {
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

	public TestDml4(String arg0) {
		super(arg0);
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
			rs.next();
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
			rs.next();
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544778, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			rs = stmt.executeQuery("SELECT 16/GRADE FROM STAFF "
					+ "WHERE EMPNAME = 'Fidel';");
			rs.next();
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544778, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}

		}

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "GROUP BY CITY HAVING SUM(GRADE/0) > 44;");
			rs.next();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
			fail();
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544778, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE GRADE = (SELECT 16/GRADE FROM STAFF "
					+ "WHERE EMPNUM = 'E6');");
			rs.next();
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// selected?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544778, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			stmt.executeUpdate("UPDATE STAFF SET GRADE = GRADE/0 "
					+ "WHERE GRADE = 12;");
			rs.next();
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// updated?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544778, sqle.getErrorCode());
				assertEquals("HY000", sqle.getSQLState());
			}
		}

		try {
			stmt.executeUpdate("INSERT INTO STAFF "
					+ "SELECT 'X','Y',HOURS/0,'z' FROM WORKS "
					+ "WHERE PNUM = 'P6';");
			rs.next();
			fail();
			// PASS:0490 If ERROR, data exception/division by zero, 0 rows
			// inserted?
			// PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				assertEquals(335544778, sqle.getErrorCode());
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
			rs.next();
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
			rs.next();
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
			rs.next();
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
			rs.next();
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
			rs.next();

		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			assertEquals(
					"Should return Operation violates CHECK constraint on view or table WCOV",
					335544558, errorCode);
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
				assertTrue(sqle.getMessage().indexOf(
						"Operation violates CHECK constraint") != -1);
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
				assertTrue(sqle
						.getMessage()
						.indexOf(
								"arithmetic exception, numeric overflow, or string truncation") != -1);
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
				assertTrue(sqle
						.getMessage()
						.indexOf(
								"arithmetic exception, numeric overflow, or string truncation") != -1);
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
				assertTrue(sqle
						.getMessage()
						.indexOf(
								"arithmetic exception, numeric overflow, or string truncation") != -1);
		}

		// setup
		rowCount = stmt.executeUpdate("INSERT INTO FOUR_TYPES "
				+ "VALUES (1,'X',11112222.00,.000003E-25);");

		errorCode = 0;
		try {
			rs = stmt.executeQuery("SELECT T_DECIMAL / .000000001 "
					+ "FROM FOUR_TYPES " + "WHERE T_CHAR = 'X';");
			rs.next();
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
	public void testDml_099_ImplicitNumericCasting() throws SQLException {
		int errorCode; // Error code.
		int rowCount; // Row count.

		// TEST:0582 Implicit numeric casting (feature 9) static!
		stmt
				.executeUpdate("CREATE TABLE ICAST2 (C1 INT, C2 DOUBLE PRECISION, C3 NUMERIC(5,3));");

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