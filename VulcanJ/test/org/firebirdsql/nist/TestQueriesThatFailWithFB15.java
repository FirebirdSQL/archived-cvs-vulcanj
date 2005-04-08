/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 17, 2004
 * 
 * All of the tests in this file will fail when run under Firebird 1.5.1.
 * To be precise, these tests provide results that are different from the
 * NIST output.  
 * 
 */
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bioliv, Aug 17, 2004
 *  
 */
public class TestQueriesThatFailWithFB15 extends NistTestBase {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

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

	public TestQueriesThatFailWithFB15(String arg0) {
		super(arg0);
	}
	/*
	 * Name: testCdr_020
	 * 
	 * Notes: This test failed against Firebird 1.5.1 on cases where an interim
	 * key violation occurred.
	 *  
	 */
	public void testCdr_020() throws SQLException {
		SunTab.setupSunTab2(stmt);
		SunTab.refreshSunTab2(stmt);
		// TEST:0369 update P. K, set F1=F1+1, interim violation!

		//		stmt.executeUpdate("CREATE TABLE SIZ3_P3 "
		//				+ "(F1 DECIMAL(4) NOT NULL, " + "F2 CHAR(8), "
		//				+ "UNIQUE (F1));");

		stmt.executeUpdate("INSERT INTO SIZ3_P3 " + "VALUES (0,'CC');");

		try {
			stmt.executeUpdate("UPDATE SIZ3_P3 " + "SET F1 = F1 + 1; ");
		} catch (SQLException sqle) {
			assertTrue("UPDATE statement with interim UNIQUE violation fails. "
					+ "NIST says this should pass.", false);
		}

		rs = stmt.executeQuery("SELECT MAX(F1),MIN(F1) FROM SIZ3_P3; ");
		rs.next();
		assertEquals(5, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		// PASS:0369 If MAX(F1) = 5 and MIN(F1) = 1?
		// END TEST >>> 0369 <<< END TEST

		// *************************************************
		// TEST:0370 update F. K, set F1=F1+1, interim violation!
		SunTab.refreshSunTab2(stmt);

		stmt.executeUpdate("UPDATE SIZ2_F3 " + "SET F1 = F1 + 1;");

		rs = stmt.executeQuery("SELECT MAX(F1),MIN(F1) FROM SIZ2_F3; ");
		rs.next();
		assertEquals(4, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		// PASS:0370 If MAX(F1) = 4 and MIN(F1) = 2?
		// END TEST >>> 0370 <<< END TEST

		// *************************************************
		// TEST:0371 update self-ref table, interim violation!

		SunTab.refreshSunTab2(stmt);
		stmt.executeUpdate("INSERT INTO MID1 " + "VALUES(1,1);");

		stmt.executeUpdate("INSERT INTO MID1 " + "VALUES(2,1);");

		stmt.executeUpdate("INSERT INTO MID1 " + "VALUES(3,2);");

		stmt.executeUpdate("INSERT INTO MID1 " + "VALUES(4,3);");

		stmt.executeUpdate("INSERT INTO MID1 " + "VALUES(5,1);");

		stmt.executeUpdate("UPDATE MID1 " + "SET P_KEY = P_KEY + 1, "
				+ "F_KEY = F_KEY + 1;");

		rs = stmt
				.executeQuery("SELECT MAX(F_KEY),MIN(F_KEY),MAX(P_KEY),MIN(P_KEY) "
						+ "FROM MID1;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		assertEquals(6, rs.getInt(3));
		assertEquals(2, rs.getInt(4));
		// PASS:0371 If MAX(F_KEY) = 4 and MIN(F_KEY) = 2 and?
		// PASS:0371 If MAX(P_KEY) = 6 and MIN(P_KEY) = 2?

		// END TEST >>> 0371 <<< END TEST

		// *************************************************
		// TEST:0372 delete self-ref table, interim violation!
		SunTab.refreshSunTab2(stmt);

		stmt.executeUpdate("DELETE FROM STAFF_C "
				+ "WHERE EMPNUM = 'E2' OR MGR = 'E2';");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_C;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0372 If count = 4?
		// END TEST >>> 0372 <<< END TEST

	}
	/*
	 * Name: testCdr_027
	 * 
	 * Notes: check constraint tests
	 *  
	 */
	public void testCdr_027() throws SQLException {
		BaseTab.setupBaseTab(stmt); // staff table...
		// TEST:0446 Table CHECK constraint allows unknown (NULL)!

		stmt.executeUpdate("CREATE TABLE STAFF5 (EMPNUM CHAR(3) NOT NULL,"
				+ "EMPNAME CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15), " + "PRIMARY KEY (EMPNUM), "
				+ "CHECK (GRADE > 0 AND GRADE < 20));");

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
					+ "VALUES('E7','Mimi',NULL,'Miami'); "));
			// PASS:0446 If 1 row inserted?}
		} catch (SQLException sqle) {
			assertTrue(
					"Failed when trying to insert a NULL value into column with CHECK "
							+ "(field > 0 AND filed < 20) constraint. NIST says this should work.",
					false);
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
				+ "VALUES('E8','Joe',NULL,'Boston');"));
		// PASS:0446 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5(EMPNUM) "
				+ "VALUES('E9');"));
		// PASS:0446 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("UPDATE STAFF "
				+ "SET GRADE = NULL " + "WHERE EMPNUM = 'E1';"));
		// PASS:0446 If 1 row updated?

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF5 " + "SELECT * "
				+ "FROM STAFF;"));
		// PASS:0446 If 5 rows inserted?

		assertEquals(1, stmt.executeUpdate("UPDATE STAFF5 "
				+ "SET GRADE = NULL " + "WHERE EMPNUM = 'E2';"));
		// PASS:0446 If 1 row updated?

		assertEquals(1, stmt.executeUpdate("UPDATE STAFF5 "
				+ "SET GRADE = NULL " + "WHERE EMPNUM = 'E4'; "));
		// PASS:0446 If 1 row updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF5;");
		rs.next();
		assertEquals(8, rs.getInt(1));
		// PASS:0446 If count = 8?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF5 "
				+ "WHERE GRADE IS NULL; ");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:0446 If count = 6?

		// END TEST >>> 0446 <<< END TEST
		// *********************************************

		// TEST:0447 NULLs with check constraint and check option!

		stmt.executeUpdate("CREATE TABLE STAFF6 ("
				+ "EMPNUM CHAR(3) NOT NULL, " + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4) CHECK (GRADE > 0 AND GRADE < 20), "
				+ "CITY CHAR(15));");
		stmt.executeUpdate("CREATE VIEW STAFF6_WITH_GRADES AS "
				+ "SELECT EMPNUM,EMPNAME,GRADE,CITY " + "FROM STAFF6 "
				+ "WHERE GRADE > 0 AND GRADE < 20 " + "WITH CHECK OPTION; ");

		try {
			stmt.executeUpdate("INSERT INTO STAFF6_WITH_GRADES "
					+ "VALUES('X1','Vicki',NULL,'Houston'); ");
			fail();
			// PASS:0447 If ERROR, view check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF6 "
				+ "VALUES('X2','Tina',NULL,'Orlando'); "));
		// PASS:0447 If 1 row inserted?

		rs = stmt
				.executeQuery("SELECT COUNT(*) " + " FROM STAFF6_WITH_GRADES;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0447 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + " FROM STAFF6;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0447 If count = 1?

		rs = stmt.executeQuery("SELECT EMPNAME " + "FROM STAFF6 "
				+ "WHERE GRADE IS NULL;");
		rs.next();
		assertEquals("Tina", rs.getString(1).trim());
		// PASS:0447 If EMPNAME is Tina?

		// END TEST >>> 0447 <<< END TEST
		// *********************************************

		// TEST:0448 PRIMARY KEY implies UNIQUE!

		stmt.executeUpdate("CREATE TABLE STAFF9 "
				+ "(EMPNUM CHAR(3) NOT NULL PRIMARY KEY, "
				+ "EMPNAME CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15), " + "CHECK (EMPNAME NOT LIKE 'T%'));");

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO STAFF9(EMPNUM,EMPNAME) "
						+ "VALUES('D1','Muddley'); "));
		// PASS:0448 If 1 row inserted?

		try {
			stmt.executeUpdate("INSERT INTO STAFF9(EMPNUM,EMPNAME) "
					+ "VALUES('D1','Muddley');");
			fail();
			// PASS:0448 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO STAFF9(EMPNUM,EMPNAME) "
						+ "VALUES('d1','Muddley');"));
		// PASS:0448 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF9;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0448 If count = 2?
		// END TEST >>> 0448 <<< END TEST
		// *********************************************

		// TEST:0449 Constraint definition is case sensitive!

		stmt.executeUpdate(" DELETE FROM STAFF9;");

		try {
			stmt.executeUpdate("INSERT INTO STAFF9(EMPNUM,EMPNAME) "
					+ "VALUES('Z1','Tina');");
			fail();
			// PASS:0449 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO STAFF9(EMPNUM,EMPNAME) "
						+ "VALUES('Z2','tina');"));
		// PASS:0449 If 1 row inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO STAFF9(EMPNUM,EMPNAME) "
						+ "VALUES('Z3','ANTHONY');"));
		// PASS:0449 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF9;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0449 If count = 2?
		// END TEST >>> 0449 <<< END TEST
		// *********************************************

		// TEST:0450 Referential integrity is case sensitive!
		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);

		stmt.executeUpdate("INSERT INTO DEPT "
				+ "VALUES(11,'VOLLEYBALL','VICKI');");
		// PASS:0450 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO DEPT "
				+ "VALUES(10,'volleyball','vicki'); "));
		// PASS:0450 If 1 row inserted?
		// NOTE:0450 insert lower case value of above.

		rs = stmt.executeQuery("SELECT COUNT(*) FROM DEPT WHERE DNO = 10;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0450 If count = 1?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMP "
				+ "VALUES(13,'MARY','Dancer',15,'VOLLEYBALL',010101);"));
		// PASS:0450 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("DELETE FROM DEPT "
				+ "WHERE DNO = 10;"));
		// PASS:0450 If 1 row deleted?

		try {
			stmt.executeUpdate("UPDATE DEPT " + "SET DNAME = 'EDUCATION' "
					+ "WHERE DNAME = 'Education'; ");
			fail();
			// PASS:0450 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT * FROM DEPT ORDER BY DNO;");
		rs.next();
		assertEquals(11, rs.getInt(1));
		rs.next();
		assertEquals(12, rs.getInt(1));
		rs.next();
		assertEquals(13, rs.getInt(1));
		rs.next();
		assertEquals(14, rs.getInt(1));
		assertEquals("Education", rs.getString(2).trim());
		rs.next();
		assertEquals(15, rs.getInt(1));
		// PASS:0450 If 5 rows selected?
		// PASS:0450 If DNO values are 11, 12, 13, 14, 15?
		// PASS:0450 If DNAME = 'Education' (not 'EDUCATION') for DNO = 14?

		try {
			stmt.executeUpdate(" INSERT INTO EMP "
					+ "VALUES(28,'BARBARA','Jogger',14,'EDUCATION',010101);");
			fail();
			// PASS:0450 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("UPDATE EMP " + "SET DNAME = 'PHYSICS' "
					+ "WHERE ENO = 25; ");
			fail();
			// PASS:0450 If RI ERROR, parent missing, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT ENO, ENAME, DNO, DNAME "
				+ "FROM EMP ORDER BY ENO;");
		rs.next();
		assertEquals(13, rs.getInt(1));
		rs.next();
		assertEquals(21, rs.getInt(1));
		rs.next();
		assertEquals(22, rs.getInt(1));
		rs.next();
		assertEquals(23, rs.getInt(1));
		rs.next();
		assertEquals(24, rs.getInt(1));
		rs.next();
		assertEquals(25, rs.getInt(1));
		assertEquals("Physics", rs.getString(4).trim());
		rs.next();
		assertEquals(26, rs.getInt(1));
		rs.next();
		assertEquals(27, rs.getInt(1));
		// PASS:0450 If 8 rows selected?
		// PASS:0450 If ENO values are 13, 21 through 27?
		// PASS:0450 If DNAME = 'Physics' (not 'PHYSICS') for ENO = 25?

		// END TEST >>> 0450 <<< END TEST
	}
	/*
	 * Name: testCdr_029
	 * 
	 * Notes:
	 *  
	 */
	public void testCdr_029() throws SQLException {
		// TEST:0522 No implied natural join on FROM T1, T2!

		stmt.executeUpdate("CREATE TABLE CPBASE " + "(KC INT NOT NULL, "
				+ "JUNK1 CHAR (10), " + "PRIMARY KEY (KC)); ");

		stmt
				.executeUpdate("CREATE TABLE CPREF " + "(KCR INT, "
						+ "JUNK2 CHAR (10), "
						+ "FOREIGN KEY (KCR) REFERENCES CPBASE);");

		stmt.executeUpdate("INSERT INTO CPBASE VALUES (0, 'Zero');");
		stmt.executeUpdate("INSERT INTO CPBASE VALUES (1, 'One');");
		stmt.executeUpdate("INSERT INTO CPREF VALUES (0, 'Zero 2');");
		stmt.executeUpdate("INSERT INTO CPREF VALUES (1, 'One 2');");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM CPBASE, CPREF;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0522 If count = 4?

		rs = stmt.executeQuery("SELECT KC, JUNK2 " + "FROM CPBASE, CPREF "
				+ "ORDER BY JUNK2, KC;");
		int rowCount = 0;
		while (rs.next())
			rowCount++;
		assertEquals(4, rowCount);
		// PASS:0522 If 4 rows selected?
		// END TEST >>> 0522 <<< END TEST
		// *********************************************

		// TEST:0537 Table check constraint: column vs. column!
		stmt.executeUpdate("CREATE TABLE RET_CATALOG ( " + "VENDOR_ID INT, "
				+ "PRODUCT_ID INT, " + "WHOLESALE NUMERIC (10,2), "
				+ "RETAIL NUMERIC (10,2), " + "MARKUP NUMERIC (10,2), "
				+ "EXPORT_CODE CHAR(2), " + "EXPORT_LICNSE_DATE CHAR(20), "
				+ "CHECK (EXPORT_LICNSE_DATE IS NULL OR ( "
				+ "EXPORT_CODE = 'F1' OR " + "EXPORT_CODE = 'F2' OR "
				+ "EXPORT_CODE = 'F3' )), "
				+ "CHECK (EXPORT_CODE <> 'F2' OR WHOLESALE > 10000.00), "
				+ "CHECK (RETAIL >= WHOLESALE), "
				+ "CHECK (RETAIL = WHOLESALE + MARKUP));");
		try {
			stmt.executeUpdate("INSERT INTO RET_CATALOG "
					+ "VALUES (0, 0, NULL, 100.00, NULL, 'D1', NULL);");
		} catch (SQLException sqle) {
			assertTrue(
					"Firebird 1.5 is known to fail this INSERT TABLE statment - doesn't seem to like the NULL values",
					false);
		}
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0537 If count = 1?
		//
		// restore
		stmt.executeUpdate("DELETE FROM RET_CATALOG;");

		// setup
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 0, 80.00, 100.00, 20.00, 'D1', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 1, 80.00, 100.00, 20.00, NULL, 'Jan 20 1993'); ");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 2, 80.00, 100.00, 20.00, 'D1', NULL); ");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 3, 80.00, 100.00, 20.00, NULL, NULL); ");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 4, 80.00, 100.00, 20.00, 'F1', 'Jan 20 1993');");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG; ");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0537 If count = 4?
		//
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG "
				+ "WHERE PRODUCT_ID = 0;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0537 If count = 0?
		//
		// restore
		stmt.executeUpdate("DELETE FROM RET_CATALOG;");

		// setup
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 0, 80.00, 100.00, 20.00, 'F2', 'Jan 20 1993');");

		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 1, 80.00, 100.00, 20.00, NULL, 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 2, NULL, 100.00, NULL, 'F2', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 3, NULL, 100.00, NULL, NULL, 'Jan 20 1993');");
		stmt
				.executeUpdate("INSERT INTO RET_CATALOG "
						+ "VALUES (0, 4, 10010.00, 10110.00, 100.00, 'F2', 'Jan 20 1993');");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0537 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG "
				+ "WHERE PRODUCT_ID = 0; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0537 If count = 0?

		stmt.executeUpdate("DELETE FROM RET_CATALOG;");
		// setup
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 0, 100.01, 100.00, -0.01, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 1, 80.00, NULL, NULL, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 2, NULL, 100.00, NULL, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 3, NULL, NULL, NULL, 'F3', 'Jan 20 1993');");
		stmt
				.executeUpdate("INSERT INTO RET_CATALOG "
						+ "VALUES (0, 4, 10010.00, 10110.00, 100.00, 'F3', 'Jan 20 1993');");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0537 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG "
				+ "WHERE PRODUCT_ID = 0;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0537 If count = 0?

		// restore
		stmt.executeUpdate("DELETE FROM RET_CATALOG;");

		// setup
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 0, 100.01, 100.00, 0.00, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 1, NULL, 100.00, 20.00, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 2, 80.00, NULL, 20.00, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 3, 80.00, 100.00, NULL, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 4, NULL, NULL, 20.00, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 5, 80.00, NULL, NULL, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 6, NULL, 100.00, NULL, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 7, NULL, NULL, NULL, 'F3', 'Jan 20 1993');");
		stmt.executeUpdate("INSERT INTO RET_CATALOG "
				+ "VALUES (0, 8, 80.00, 100.00, 20.00, 'F3', 'Jan 20 1993');");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG;");
		rs.next();
		assertEquals(8, rs.getInt(1));
		// PASS:0537 If count = 8?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG "
				+ "WHERE PRODUCT_ID = 0;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0537 If count = 0?

		// END TEST >>> 0537 <<< END TEST
		// *********************************************

		// TEST:0538 With check option: column vs. column!
		stmt.executeUpdate("DELETE FROM RET_CATALOG;");

		stmt.executeUpdate("CREATE VIEW SALE_ITEMS AS "
				+ "SELECT * FROM RET_CATALOG "
				+ "WHERE MARKUP < WHOLESALE / 10.0 " + "WITH CHECK OPTION; ");

		stmt.executeUpdate("INSERT INTO SALE_ITEMS "
				+ "VALUES (0, 0, NULL, 100.00, NULL, 'D1', NULL);");
		// PASS:0538 If ERROR, check option violation, 0 rows inserted?
		// PASS:0538 OR SQLSTATE = 44000 OR SQLCODE < 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0538 If count = 0?

		try {
			stmt.executeUpdate("INSERT INTO SALE_ITEMS "
					+ "VALUES (0, 0, 80.00, 100.00, 20.00, 'D1', NULL);");
			fail();
			// PASS:0538 If ERROR, check option violation, 0 rows inserted?
			// PASS:0538 OR SQLSTATE = 44000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0538 If count = 0?

		assertEquals(1, stmt.executeUpdate("INSERT INTO SALE_ITEMS "
				+ "VALUES (0, 0, 99.00, 100.00, 1.00, 'D1', NULL);"));

		// PASS:0538 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RET_CATALOG;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0538 If count = 1?

		// END TEST >>> 0538 <<< END TEST
	}
	/*
	 * Name: testCdr_030
	 * 
	 * Notes:
	 *  
	 */
	public void testCdr_030() throws SQLException {
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

		// TEST:0516 SQLSTATE 23000: integrity constraint violation!

		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);
		try {
			stmt.executeUpdate("INSERT INTO EMP "
					+ "VALUES (41,'Tom','China Architecture',"
					+ "20,'Architecture',040553);");
			fail();
			// PASS:0516 If ERROR, integrity constraint violation, 0 rows
			// inserted?
			// PASS:0516 OR RI ERROR, parent missing, 0 rows inserted?
			// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("DELETE FROM EMP " + "WHERE ENO = 21;");
			fail();
			// PASS:0516 If ERROR, integrity constraint violation, 0 rows
			// deleted?
			// PASS:0516 OR RI ERROR, children exist, 0 rows deleted?
			// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("UPDATE EMP " + "SET ENAME = 'Thomas' "
					+ "WHERE ENO = 21; ");
			// PASS:0516 If ERROR, integrity constraint violation, 0 rows
			// updated?
			// PASS:0516 OR RI ERROR, chldren exist, 0 rows updated?
			// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?
			fail();
		} catch (SQLException sqle) {
		}

		stmt
				.executeUpdate("CREATE TABLE STAFF7 ("
						+ "EMPNUM CHAR(3) NOT NULL, " + "EMPNAME CHAR(20), "
						+ "GRADE DECIMAL(4), " + "CITY CHAR(15), "
						+ "PRIMARY KEY (EMPNUM), "
						+ "CHECK (GRADE BETWEEN 1 AND 20));");

		// PRIMARY KEY (EMPNUM)
		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF7 (EMPNUM) "
					+ "VALUES ('XXX');"));
			// PASS:0516 If 1 row inserted?
		} catch (SQLException sqle) {
			assertTrue(
					"Firebird 1.5 fails when an implied NULL value is inserted into"
							+ " column with CHECK constraint -"
							+ " CHECK (GRADE BETWEEN 1 and 20) .", false);
		}

		try {
			stmt.executeUpdate("INSERT INTO STAFF7 (EMPNUM) "
					+ "VALUES ('XXX');");
			fail();
			// PASS:0516 If ERROR, integrity constraint violation, 0 rows
			// inserted?
			// PASS:0516 OR ERROR, unique constraint, 0 rows inserted?
			// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
		}

		stmt
				.executeUpdate("CREATE TABLE PROJ3 ("
						+ "PNUM CHAR(3) NOT NULL, " + "PNAME CHAR(20), "
						+ "PTYPE CHAR(6), " + "BUDGET DECIMAL(9), "
						+ "CITY CHAR(15), " + "UNIQUE (PNUM));");

		// UNIQUE (PNUM)
		stmt.executeUpdate("INSERT INTO PROJ3 (PNUM) VALUES ('787'); ");

		stmt.executeUpdate("INSERT INTO PROJ3 (PNUM) VALUES ('789'); ");
		// PASS:0516 If 1 row inserted?

		try {
			stmt.executeUpdate("UPDATE PROJ3 SET PNUM = '787' "
					+ "WHERE PNUM = '789';");
			fail();
			// PASS:0516 If ERROR, integrity constraint violation, 0 rows
			// updated?
			// PASS:0516 OR ERROR, unique constraint, 0 rows updated?
			// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("CREATE TABLE STAFF11 ("
				+ "EMPNUM CHAR(3) NOT NULL PRIMARY KEY, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15), "
				+ "CHECK (GRADE NOT IN (5,22) AND EMPNAME NOT LIKE 'T%'));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF11 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));
		// PASS:0516 If 1 row inserted?

		// (CHECK GRADE NOT IN (5,22))
		try {
			stmt.executeUpdate("UPDATE STAFF11 " + "SET GRADE = 5 "
					+ "WHERE EMPNUM = 'E3';");
			fail();
			// PASS:0516 If ERROR, integrity constraint violation, 0 rows
			// updated?
			// PASS:0516 OR ERROR, check constraint, 0 rows updated?
			// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?
		} catch (SQLException sqle) {
		}

		// (CHECK NOT EMPNAME LIKE 'T%')
		try {
			stmt.executeUpdate("UPDATE STAFF11 " + "SET EMPNAME = 'Tom' "
					+ "WHERE EMPNUM = 'E3'; ");
			fail();
		} catch (SQLException sqle) {
		}
		// PASS:0516 If ERROR, integrity constraint violation, 0 rows updated?
		// PASS:0516 OR ERROR, check constraint, 0 rows updated?
		// PASS:0516 OR SQLSTATE = 23000 OR SQLCODE < 0?

		// END TEST >>> 0516 <<< END TEST
	}
	/*
	 * Name: testXts_733
	 * 
	 * Notes: FULL OUTER JOIN <table ref> ON <search condition>!
	 * 
	 * This test produces 2 results that differ from NIST and appear wrong. See
	 * comments below. This test will fail when run against Firebird 1.5.1.
	 *  
	 */

	public void testXts_733() throws SQLException {

		// TEST:7033 FULL OUTER JOIN <table ref> ON <search condition>!
		stmt.executeUpdate("CREATE TABLE TEST6840A "
				+ "(NUM_A NUMERIC(4), CH_A CHAR(10));");
		stmt.executeUpdate("CREATE TABLE TEST6840B "
				+ "(NUM_B NUMERIC(4), CH_B CHAR(10));");
		stmt.executeUpdate("CREATE TABLE TEST6840C "
				+ "(NUM_C1 NUMERIC(4),CH_C1 CHAR(10), "
				+ "NUM_C2 NUMERIC(4), CH_C2 CHAR(10));");
		assertEquals(1, stmt.executeUpdate("INSERT INTO TEST6840A "
				+ "VALUES (1,'A');"));
		// PASS:7033 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TEST6840A "
				+ "VALUES (2,'B');"));
		// PASS:7033 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TEST6840B "
				+ "VALUES (2,'C');"));
		// PASS:7033 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TEST6840B "
				+ "VALUES (3,'A');"));
		// PASS:7033 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT * FROM TEST6840A "
				+ "FULL OUTER JOIN TEST6840B "
				+ "ON NUM_A = NUM_B ORDER BY NUM_A;");
		// Jaybird returns true for nullsAreSortedAtEnd()
		if (conn.getMetaData().nullsAreSortedAtEnd()) {
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals("A", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertEquals(null, rs.getString(4));
			rs.next();
			assertEquals(2, rs.getInt(1));
			assertEquals("B", rs.getString(2).trim());
			assertEquals(2, rs.getInt(3));
			assertEquals("C", rs.getString(4).trim());
			rs.next();
			assertEquals(0, rs.getInt(1));
			assertEquals(null, rs.getString(2));
			assertEquals(3, rs.getInt(3));
			assertEquals("A", rs.getString(4).trim());
		} else
			assertTrue("Test Not Implemented for this sortOrder...", false);
		// NOTE: The sorting of NULLs above or below non-NULLs is
		//        implementation-defined!
		// PASS:7033 If 3 rows selected in one of the following orders?
		//                 c1 c2 c3 c4 c1 c2 c3 c4
		//                 === === === === === === === ===
		// PASS:7033 If 1 A NULL NULL? or NULL NULL 3 A ?
		// PASS:7033 If 2 B 2 C ? or 1 A NULL NULL?
		// PASS:7033 If NULL NULL 3 A ? or 2 B 2 C ?

		rs = stmt.executeQuery("SELECT * FROM TEST6840A FULL JOIN TEST6840B "
				+ "ON CH_A = CH_B ORDER BY NUM_A;");
		if (conn.getMetaData().nullsAreSortedAtEnd()) {
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals("A", rs.getString(2).trim());
			assertEquals(3, rs.getInt(3));
			assertEquals("A", rs.getString(4).trim());
			rs.next();
			assertEquals(2, rs.getInt(1));
			assertEquals("B", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertEquals(null, rs.getString(4));
			rs.next();
			assertEquals(0, rs.getInt(1));
			assertEquals(null, rs.getString(2));
			assertEquals(2, rs.getInt(3));
			assertEquals("C", rs.getString(4).trim());
		} else
			assertTrue("Test Not Implemented for this sortOrder...", false);
		// PASS:7033 If 3 rows selected in one of the following orders?
		//                 c1 c2 c3 c4 c1 c2 c3 c4
		//                 === === === === === === === ===
		// PASS:7033 If 1 A 3 A ? or NULL NULL 2 C ?
		// PASS:7033 If 2 B NULL NULL? or 1 A 3 A ?
		// PASS:7033 If NULL NULL 2 C ? or 2 B NULL NULL?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			// needed select * from for firebird
			assertEquals(3, stmt.executeUpdate("INSERT INTO TEST6840C "
					+ "select * from "
					+ "TEST6840A FULL OUTER JOIN TEST6840B ON NUM_A = 2;"));
		// PASS:7033 If 3 rows inserted successfully?
		else
			assertEquals(3, stmt.executeUpdate("INSERT INTO TEST6840C "
					+ "TEST6840A FULL OUTER JOIN TEST6840B ON NUM_A = 2;"));

		rs = stmt.executeQuery("SELECT  COUNT(*) FROM TEST6840C;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7033 If COUNT = 3?

		rs = stmt.executeQuery("SELECT  COUNT(*) FROM TEST6840C "
				+ "WHERE NUM_C1 = 1 AND CH_C1 = 'A' AND "
				+ "NUM_C2 IS NULL AND CH_C2 IS NULL;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7033 If COUNT = 1?

		rs = stmt.executeQuery("SELECT  COUNT(*) FROM TEST6840C "
				+ "WHERE NUM_C1 = 2 AND CH_C1 = 'B' AND "
				+ "NUM_C2 = 2 AND CH_C2 = 'C';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7033 If COUNT = 1?

		rs = stmt.executeQuery("SELECT  COUNT(*) FROM TEST6840C "
				+ "WHERE NUM_C1 = 2 AND CH_C1 = 'B' AND "
				+ "NUM_C2 = 3  AND CH_C2 = 'A';");
		// PASS:7033 If COUNT = 1?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			// Firebird sql doesn't like the AS in the JOIN
			rs = stmt.executeQuery("SELECT * FROM "
					+ "(TEST6840B FULL JOIN TEST6840A ON "
					+ "TEST6840B.CH_B = TEST6840A.CH_A) "
					+ "FULL JOIN TEST6840A ON "
					+ "TEST6840B.NUM_B = TEST6840A.NUM_A "
					+ "ORDER BY TEST6840B.NUM_B, TEST6840A.NUM_A;");
		else
			rs = stmt.executeQuery("SELECT * FROM "
					+ "(TEST6840B FULL JOIN TEST6840A AS CN1 ON "
					+ "TEST6840B.CH_B = CN1.CH_A) "
					+ "FULL JOIN TEST6840A AS CN2 ON "
					+ "TEST6840B.NUM_B = CN2.NUM_A "
					+ "ORDER BY TEST6840B.NUM_B, CN1.NUM_A;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals("C", rs.getString(2).trim());
		assertEquals(0, rs.getInt(3));
		assertEquals(null, rs.getString(4));
		assertEquals(2, rs.getInt(5));
		assertEquals("B", rs.getString(6).trim());
		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals("A", rs.getString(2).trim());
		assertEquals(1, rs.getInt(3));
		assertEquals("A", rs.getString(4).trim());
		assertEquals(0, rs.getInt(5));
		assertEquals(null, rs.getString(6));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertEquals(null, rs.getString(2));
		assertEquals(2, rs.getInt(3));
		assertEquals("B", rs.getString(4).trim());
		assertEquals(0, rs.getInt(5));
		assertEquals(null, rs.getString(6));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertEquals(null, rs.getString(2));
		assertEquals(0, rs.getInt(3));
		assertEquals(null, rs.getString(4));
		assertEquals(1, rs.getInt(5));
		assertEquals("A", rs.getString(6).trim());

		// Firebird SQL output appears to be wrong. We're supposed to get:
		//                ==== ==== ==== ==== ==== ====
		// PASS:7033 If 2 C NULL NULL 2 B ?
		// PASS:7033 If 3 A 1 A NULL NULL?
		// PASS:7033 If NULL NULL 2 B NULL NULL?
		// PASS:7033 If NULL NULL NULL NULL 1 A ?
		//
		// We got:
		// PASS:7033 If 2 C NULL NULL 2 B ?
		// PASS:7033 If 3 A 1 A NULL NULL?
		// PASS:7033 If NULL NULL NULL NULL 1 A ?
		// PASS:7033 If NULL NULL 2 B NULL NULL?

		// PASS:7033 If 4 rows selected in one of the following orders?
		// PASS:7033 If nulls last?
		//                ==== ==== ==== ==== ==== ====
		// PASS:7033 If 2 C NULL NULL 2 B ?
		// PASS:7033 If 3 A 1 A NULL NULL?
		// PASS:7033 If NULL NULL 2 B NULL NULL?
		// PASS:7033 If NULL NULL NULL NULL 1 A ?
		//
		// PASS:7033 If nulls first?
		//                ==== ==== ==== ==== ==== ====
		// PASS:7033 If NULL NULL NULL NULL 1 A ?
		// PASS:7033 If NULL NULL 2 B NULL NULL?
		// PASS:7033 If 2 C NULL NULL 2 B ?
		// PASS:7033 If 3 A 1 A NULL NULL?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			rs = stmt.executeQuery("SELECT * FROM "
					+ "(TEST6840A FULL OUTER JOIN TEST6840A "
					+ "ON TEST6840A.NUM_A = TEST6840A.NUM_A) "
					+ "FULL OUTER JOIN "
					+ "(TEST6840B FULL OUTER JOIN TEST6840B "
					+ "ON TEST6840B.CH_B = TEST6840B.CH_B) "
					+ "ON TEST6840A.NUM_A = TEST6840B.NUM_B "
					+ "ORDER BY TEST6840A.NUM_A;");
		else
			rs = stmt.executeQuery("SELECT * FROM "
					+ "(TEST6840A AS CN3 FULL OUTER JOIN TEST6840A AS CN4 "
					+ "ON CN3.NUM_A = CN4.NUM_A) " + "FULL OUTER JOIN "
					+ "(TEST6840B AS CN5 FULL OUTER JOIN TEST6840B AS CN6 "
					+ "ON CN5.CH_B = CN6.CH_B) " + "ON CN3.NUM_A = CN5.NUM_B "
					+ "ORDER BY CN3.NUM_A;");

		int rowCount = 0;
		while (rs.next())
			rowCount++;
		assertEquals(3, rowCount);
		// Output from firebird appears wrong!!! We got 8 rows back!

		// PASS:7033 If 3 rows selected in one of the following orders?

		// PASS:7033 If nulls last?
		//              ==== ==== ==== ==== ==== ==== ==== ====
		// PASS:7033 If 1 A 1 A NULL NULL NULL NULL?
		// PASS:7033 If 2 B 2 B 2 C 2 C ?
		// PASS:7033 If NULL NULL NULL NULL 3 A 3 A ?

		// PASS:7033 If nulls first?
		//              ==== ==== ==== ==== ==== ==== ==== ====
		// PASS:7033 If NULL NULL NULL NULL 3 A 3 A ?
		// PASS:7033 If 1 A 1 A NULL NULL NULL NULL?
		// PASS:7033 If 2 B 2 B 2 C 2 C ?

		// END TEST >>> 7033 <<< END TEST
		stmt.executeUpdate("drop TABLE TEST6840A ;");
		stmt.executeUpdate("drop  TABLE TEST6840B ;");
		stmt.executeUpdate("drop  TABLE TEST6840C ;");

	}

	/*
	 * 
	 * This problem was originally identified in test testSdl_016. Inserting a
	 * record into a view returns an incorrect row count.
	 *  
	 */

	public void testInserRecordIntoViewAndGetRowCount() throws SQLException {
		stmt.executeUpdate("CREATE TABLE STAFF "
				+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");

		stmt.executeUpdate("CREATE VIEW STAFFV2 " + "AS SELECT * FROM STAFF "
				+ "WHERE  GRADE >= 12 " + "WITH CHECK OPTION;");

		assertEquals(
				"Inserting 1 record into a veiw should record a rowCount of 1.",
				1, stmt.executeUpdate("INSERT INTO STAFFV2 "
						+ "VALUES('E6','Ling',15,'Xian');"));
		// PASS:0152 If 1 row is inserted?

	}

	/*
	 * This problem was provided by Jim Beesley. He mentions that this problem
	 * was found in Firebird 1.5.1, and is also present in Vulcan.
	 */
	public void testLeftJoinFirebird15Bug() throws SQLException {

		stmt
				.executeUpdate("create table table1(c_key int not null primary key,"
						+ "val char(10))");
		assertEquals(1, stmt
				.executeUpdate("insert into table1 values(0, 'dont care')"));

		assertEquals(1, stmt
				.executeUpdate("insert into table1 values(1, 'value 1')"));
		assertEquals(1, stmt
				.executeUpdate("insert into table1 values(2, 'value 2')"));

		stmt.executeUpdate("create table table2(pk int not null primary key, "
				+ "info char(10), C_KEY int not null)");
		assertEquals(1, stmt
				.executeUpdate("insert into table2 values(3, 'xxx', 0)"));
		assertEquals(1, stmt
				.executeUpdate("insert into table2 values(1, 'yyy', 1)"));
		assertEquals(1, stmt
				.executeUpdate("insert into table2 values(2, 'zzz', 2)"));

		rs = stmt.executeQuery("SELECT A.INFO,B.VAL " + "FROM TABLE2 A "
				+ "LEFT JOIN TABLE1 B ON (B.C_KEY=A.C_KEY) AND (B.C_KEY>0)");
		rs.next();
		assertEquals("yyy", rs.getString(1).trim());
		assertEquals("value 1", rs.getString(2).trim());
		rs.next();
		assertEquals("zzz", rs.getString(1).trim());
		assertEquals("value 2", rs.getString(2).trim());

		assertTrue("ResultSet has only 2 rows, not 3 as it should.", rs.next());
		assertEquals("xxx", rs.getString(1).trim());
		assertEquals(null, rs.getString(2));
		//		Vulcan incorrectly returns the following 2 rows:
		//		  yyy Value 1
		//		  zzz Value 2
		//
		//		The "proper" result set is:
		//		  yyy Value 1
		//		  zzz Value 2
		//		  xxx NULL

	}
	/*
	 * Name: testFkWithGrantReferencesOnly
	 * 
	 * Notes: The error message says "object STAFF_P is in use", which seems
	 * wrong.
	 *  
	 */
	public void testFkWithGrantReferencesOnly() throws SQLException {
		// *************************************************
		// NOTE:0383 Either TAB5 does not exist -OR-
		// NOTE:0383 TAB5 exists but allows orphans (F.K without P.K)

		Connection connSun = null;
		Statement stmtSun = null;
		Connection connSullivan = null;
		Statement stmtSullivan = null;

		try {
			connSun = DriverManager.getConnection(getUrl(), "sun", "masterkey");
			stmtSun = connSun.createStatement();
			connSullivan = DriverManager.getConnection(getUrl(), "sullivan",
					"masterkey");
			stmtSullivan = connSun.createStatement();
			// create staff_p under SUN authorization
			try {
				stmtSun.executeUpdate("CREATE TABLE STAFF_P "
						+ "(EMPNUM   CHAR(3) NOT NULL, "
						+ "EMPNAME  CHAR(20), " + "GRADE    DECIMAL(4), "
						+ "CITY     CHAR(15), " + "UNIQUE  (EMPNUM));");
			} catch (SQLException msql) {
				System.out.println("we should not fail here");
				assertFalse("we should not fail here", true);
			}

			stmtSun.executeUpdate("GRANT REFERENCES ON STAFF_P TO SULLIVAN");

			try {
				stmtSullivan.executeUpdate("CREATE TABLE TAB5(F15 CHAR(3), "
						+ "F5 CHAR(4), " + "FOREIGN KEY (F15) "
						+ "REFERENCES STAFF_P(EMPNUM));");
				// TEST:0383 Priv.violation: GRANT without GRANT OPTION!
				fail();
			} catch (SQLException anticipatedError) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String RELATION_CONSTRAINTS_ERROR = "GDS Exception. 335544351. unsuccessful metadata update\n"
							+ "STORE RDB$RELATION_CONSTRAINTS failed\n"
							+ "no permission for references access to ";
					assertEquals(RELATION_CONSTRAINTS_ERROR, anticipatedError
							.getMessage().substring(0,
									RELATION_CONSTRAINTS_ERROR.length()));
				}
			}

		} finally {
			try {
				connSun.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connSullivan.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
}