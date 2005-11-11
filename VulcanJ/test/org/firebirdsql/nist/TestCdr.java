/* $Id$ */
/*
 * Author: bioliv Created on: Aug 9, 2004
 * 
 * The CDR suite tests "integrity enhancement" to SQL - Check clause, Default
 * column value, and Referential integrity.
 * 
 * Some of these tests failed when initially run against Firebird 1.5.1. Those
 * tests have been moved to the file TestQueriesThatFailWithFB15.java, so that
 * TestCDR can run clean as a regression test for future Firebird development.
 *  
 */
package org.firebirdsql.nist;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TestCdr extends NistTestBase {
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

	public TestCdr(String arg0) {
		super(arg0);
	}

	/*
	 * Name: testCdr_001
	 * 
	 * TEST:0300 DEFAULT value literal & number in a table!
	 *  
	 */
	public void testCdr_001() throws SQLException {

		stmt.execute("CREATE TABLE STAFF4 (EMPNUM CHAR(3) NOT NULL, "
				+ "EMPNAME CHAR(20) DEFAULT NULL, "
				+ "GRADE DECIMAL(4) DEFAULT 0, "
				+ "CITY CHAR(15) DEFAULT '               ');");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 (EMPNUM,GRADE) "
				+ "VALUES ('E1',40)"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 "
				+ "(EMPNUM,EMPNAME) VALUES ('E2','HUFFMAN')"));

		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF4 WHERE GRADE=0 ");
		rs.next();
		assertEquals("HUFFMAN             ", rs.getString("EMPNAME"));
		assertFalse("Too many rows are present in this ResultSet.", rs.next());
		// PASS:0300 If 1 row selected and GRADE = 40?",
		// PASS:0300 If 1 row selected and EMPNAME = 'HUFFMAN'?

		rs = stmt.executeQuery("SELECT GRADE FROM STAFF4 "
				+ "WHERE (EMPNAME IS NULL) " + "AND CITY = ' ';");
		rs.next();
		assertEquals(40, rs.getInt(1));
		assertFalse("Too many rows are present in this ResultSet.", rs.next());
		// PASS:0300 If 1 row selected and GRADE = 40?

		// TEST:0301 DEFAULT value USER in a table!
		try {
			stmt.execute("CREATE TABLE STAFF14 (EMPNUM CHAR(3) "
					+ "NOT NULL, EMPNAME  CHAR(20) DEFAULT USER, "
					+ "GRADE DECIMAL(4), CITY CHAR(15));");
		} catch (SQLException sqle) {
			// do nothing, if already present
		}

		stmt.execute("INSERT INTO STAFF14 (EMPNUM,GRADE) VALUES ('E1',40)");
		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF14 "
				+ "WHERE EMPNAME = 'SYSDBA'");
		rs.next();
		assertEquals("SYSDBA              ", rs.getString(1));
		assertFalse("Too many rows are present in this ResultSet.", rs.next());
		// // PASS:0300 If 1 row selected and GRADE = 40?",
		// PASS:0301 If 1 row selected and EMPNAME = 'SYSDBA'?

		// TEST:0377 DEFAULT value with explicit NULL!
		stmt.execute("CREATE TABLE STAFF16 (EMPNUM CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20) DEFAULT NULL, "
				+ "GRADE DECIMAL(4) NOT NULL CHECK (GRADE IN (100,150,200)), "
				+ "CITY CHAR(15), PRIMARY KEY (GRADE,EMPNUM));");
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO STAFF16 (EMPNUM,GRADE) "
						+ "VALUES ('E1',150)"));
		try {
			stmt.execute("INSERT INTO STAFF16 (EMPNUM,GRADE) "
					+ "VALUES ('E1',150)");
			fail("INSERT should violate unique constraint");
		} catch (SQLException success) {
		}

		// PASS:0377 If ERROR, unique constraint, 0 rows inserted?
		rs = stmt.executeQuery("SELECT COUNT(*) as rowCount FROM STAFF16 "
				+ "WHERE EMPNAME IS NULL");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0377 If count = 1?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF16 "
				+ "(EMPNUM,EMPNAME,GRADE) " + "VALUES ('E2','Tom',100)"));

		rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF16 "
				+ "WHERE EMPNUM = 'E2'");
		rs.next();
		assertEquals("Tom                 ", rs.getString(1));
		// PASS:0377 If EMPNAME = 'Tom'?

		try {
			stmt.executeUpdate("INSERT INTO STAFF16 (EMPNUM,EMPNAME,GRADE) "
					+ "VALUES ('E3','Bill',151)");
			fail("INSERT should violate check constraint");
			// PASS:0377 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException success) {
		}

		rs = stmt.executeQuery("SELECT GRADE  FROM STAFF16 "
				+ "WHERE EMPNUM = 'E3'");
		assertFalse(rs.next());
		// PASS:0377 If 0 rows selected, SQLCODE = 100, end of data?
		// END TEST >>> 0377 <<< END TEST
	}

	/*
	 * Name: testCdr_002
	 * 
	 * TEST:0302 CHECK <comp. predicate> in <tab. cons.>, insert!
	 *  
	 */
	public void testCdr_002() throws SQLException {
		int rowCount;

		stmt.executeUpdate("CREATE TABLE STAFF5 (EMPNUM    CHAR(3) NOT NULL,"
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15), " + "PRIMARY KEY (EMPNUM), "
				+ "CHECK (GRADE > 0 AND GRADE < 20));");

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
					+ "VALUES('E1','Alice',0,'Deale');"));
			fail("INSERT should violate check constraint");
			// PASS:0302 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
					+ "VALUES('E2','Tom',22,'Newyork');"));
			fail("INSERT should violate check constraint");
			// PASS:0302 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF5;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0302 If count = 1?
		// END TEST >>> 0302 <<< END TEST

	}
	/*
	 * Name: testCdr_003
	 * 
	 * Notes: CHECK tests...
	 *  
	 */
	public void testCdr_003() throws SQLException {
		stmt.executeUpdate("CREATE TABLE STAFF13 ("
				+ "EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15), "
				+ "PRIMARY KEY (EMPNUM), " + "CHECK (NOT EMPNAME IS NULL) )");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF13 "
				+ "VALUES('E1','Alice',36,'Deale');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF13;");
		// PASS:0306 If count = 1?

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF13 "
					+ "VALUES('E2',NULL,36,'Newyork');"));
			// PASS:0306 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF13;");
		// PASS:0306 If count = 1?
		// END TEST >>> 0306 <<< END TEST

		// TEST:0307 CHECK <like predicate> in <tab. cons>, insert!
		stmt.executeUpdate("CREATE TABLE STAFF9 "
				+ "(EMPNUM CHAR(3) NOT NULL PRIMARY KEY, "
				+ "EMPNAME CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15), " + "CHECK (EMPNAME NOT LIKE 'T%'));");

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF9 "
					+ "VALUES('E1','Thomas',0,'Deale');"));
			fail();
			// PASS:0307 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF9 "
					+ "VALUES('E2','Tom',22,'Newyork');"));
			// PASS:0307 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF9 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF9;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0307 If count = 1?

		// END TEST >>> 0307 <<< END TEST

		// TEST:0308 CHECK <in predicate> in <tab. cons.>, insert!
		stmt.executeUpdate("CREATE TABLE STAFF10 ("
				+ "EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15), "
				+ "PRIMARY KEY (EMPNUM), " + "CHECK (GRADE NOT IN (5,22)));");

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF10 "
					+ "VALUES('E1','Thomas',5,'Deale');"));
			// PASS:0308 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF10 "
					+ "VALUES('E2','Tom',22,'Newyork');"));
			// PASS:0308 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF10 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF10;");
		// PASS:0308 If count = 1?

		// END TEST >>> 0308 <<< END TEST

		// *************************************************
		// NO_TEST:0373 insert with embeded var. & indic. var. CHECK clause!
		// Testing Embedded Variable & Indicator Variable
		// *************************************************

		// TEST:0374 computation in update, CHECK clause!
		stmt.executeUpdate("CREATE TABLE STAFF5 (EMPNUM    CHAR(3) NOT NULL,"
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15), " + "PRIMARY KEY (EMPNUM), "
				+ "CHECK (GRADE > 0 AND GRADE < 20));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
				+ "VALUES('R9','Alice',15,'Deale');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF5;");
		// PASS:0374 If count = 1?

		try {
			stmt.executeUpdate("UPDATE STAFF5 "
					+ "SET GRADE = 10 * 10 / 5 + 1 " + "WHERE EMPNUM = 'R9';");
			// PASS:0374 If ERROR, check constraint, 0 rows updated?
			fail("UPDATE should violate check constraint");
		} catch (SQLException sqle) {
		}
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF5 "
				+ "WHERE GRADE = 15;");
		// PASS:0374 If count = 1?
		// END TEST >>> 0374 <<< END TEST
	}

	/*
	 * Name: testCdr_004
	 * 
	 * Notes: More CHECK tests...
	 *  
	 */
	public void testCdr_004() throws SQLException {
		// TEST:0309 CHECK combination predicates in <tab. cons.>, insert!

		stmt.executeUpdate("CREATE TABLE STAFF11 ("
				+ "EMPNUM CHAR(3) NOT NULL PRIMARY KEY, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15), "
				+ "CHECK (GRADE NOT IN (5,22) AND EMPNAME NOT LIKE 'T%'));");
		try {
			stmt.executeUpdate("INSERT INTO STAFF11"
					+ "VALUES('E1','Thomas',0,'Deale');");
			// PASS:0309 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO STAFF11 "
					+ "VALUES('E2','Tom',22,'Newyork');");
			// PASS:0309 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF11 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF11;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0309 If count = 1?
		// END TEST >>> 0309 <<< END TEST

		// TEST:0310 CHECK if X NOT IN, NOT X IN equivalent, insert!
		stmt.executeUpdate("CREATE TABLE STAFF12 ("
				+ "EMPNUM    CHAR(3) NOT NULL," + "EMPNAME  CHAR(20),"
				+ "GRADE DECIMAL(4)," + "CITY   CHAR(15),"
				+ "PRIMARY KEY (EMPNUM)," + "CHECK (NOT GRADE IN (5,22)"
				+ "AND NOT EMPNAME LIKE 'T%'));");
		try {
			stmt.executeUpdate("INSERT INTO STAFF12 "
					+ "VALUES('E1','Thomas',0,'Deale');");
			// PASS:0310 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO STAFF12 "
					+ "VALUES('E2','Tom',22,'Newyork'); ");
			// PASS:0310 If ERROR, check constraint, 0 rows inserted?
			fail();
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF12 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF12;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0310 If count = 1?
		// END TEST >>> 0310 <<< END TEST

		// TEST:0311 CHECK NOT NULL in col.cons., insert, null explicit!

		// setup
		stmt.executeUpdate("CREATE TABLE STAFF15 (" + "EMPNUM CHAR(3), "
				+ "EMPNAME  CHAR(20) NOT NULL, " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF15 "
				+ "VALUES('E1','Alice',52,'Deale');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF15;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0311 If count = 1?

		try {
			stmt.executeUpdate("INSERT INTO STAFF15 "
					+ "VALUES('E2',NULL,52,'Newyork');");
			fail();
			// PASS:0311 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF15;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0311 If count = 1?
		// END TEST >>> 0311 <<< END TEST

		// TEST:0312 CHECK NOT NULL in col.cons., insert, null implicit!

		stmt.executeUpdate("DELETE FROM STAFF15;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF15 "
				+ "VALUES('E1','Alice',52,'Deale');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF15;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0312 If count = 1?

		try {
			stmt.executeUpdate("INSERT INTO STAFF15(EMPNUM,GRADE,CITY) "
					+ "VALUES('E2',52,'Newyork'); ");
			fail();
			// PASS:0312 If ERROR, check constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF15;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0312 If count = 1?
		// END TEST >>> 0312 <<< END TEST

	}

	/*
	 * Name: testCdr_005
	 * 
	 * Notes: Even more CHECK tests...
	 *  
	 */

	public void testCdr_005() throws SQLException {
		// TEST:0313 CHECK <comp. predicate> in <tab. cons.>, update!

		stmt.executeUpdate("CREATE TABLE STAFF5 (EMPNUM    CHAR(3) NOT NULL,"
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15), " + "PRIMARY KEY (EMPNUM), "
				+ "CHECK (GRADE > 0 AND GRADE < 20));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF5 "
				+ "VALUES('E2','Tom',14,'Newyork');"));

		try {
			stmt.executeUpdate("UPDATE STAFF5 " + "SET GRADE = 20;");
			fail("UPDATE should violate check constraint");
			// PASS:0313 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF5 "
				+ "WHERE GRADE = 14; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0313 If count = 1?
		// END TEST >>> 0313 <<< END TEST

		// TEST:0314 CHECK <comp. predicate> in <col. cons.>, update!
		stmt.executeUpdate("CREATE TABLE STAFF6 ("
				+ "EMPNUM CHAR(3) NOT NULL, " + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4) CHECK (GRADE > 0 AND GRADE < 20), "
				+ "CITY CHAR(15));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF6 "
				+ "VALUES('E2','Tom',14,'Newyork');"));

		try {
			stmt.executeUpdate("UPDATE STAFF6 " + "SET GRADE = 20;");
			fail();
			// PASS:0314 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF6 "
				+ "WHERE GRADE = 14;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0314 If count = 1?
		// END TEST >>> 0314 <<< END TEST

		// TEST:0315 CHECK <between predicate> in <tab. cons.>, update!
		stmt
				.executeUpdate("CREATE TABLE STAFF7 ("
						+ "EMPNUM CHAR(3) NOT NULL, " + "EMPNAME CHAR(20), "
						+ "GRADE DECIMAL(4), " + "CITY CHAR(15), "
						+ "PRIMARY KEY (EMPNUM), "
						+ "CHECK (GRADE BETWEEN 1 AND 20));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF7 "
				+ "VALUES('E2','Tom',14,'Newyork');"));
		try {
			stmt.executeUpdate("UPDATE STAFF7 " + "SET GRADE = 21; ");
			fail("UPDATE should violate check constraint");
			// PASS:0315 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF7 "
				+ "WHERE GRADE = 14;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0315 If count = 1?
		// END TEST >>> 0315 <<< END TEST

	}
	/*
	 * Name: testCdr_006
	 * 
	 * Notes: Even more CHECK tests...
	 *  
	 */

	public void testCdr_006() throws SQLException {
		// TEST:0316 CHECK <null predicate> in <tab. cons.>, update!

		stmt.executeUpdate("CREATE TABLE STAFF8 ("
				+ "EMPNUM CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15), "
				+ "PRIMARY KEY (EMPNUM), " + "CHECK (EMPNAME IS NOT NULL));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF8 "
				+ "VALUES('E1','Alice',34,'Deale');"));

		try {
			stmt.executeUpdate("UPDATE STAFF8 " + "SET EMPNAME = NULL "
					+ "WHERE EMPNUM = 'E1';");
			// PASS:0316 If ERROR, check constraint, 0 rows updated?
			fail("UPDATE should violate check constraint");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF8 "
				+ "WHERE EMPNAME = 'Alice';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0316 If count = 1?
		// END TEST >>> 0316 <<< END TEST

		// TEST:0317 CHECK X IS NOT NULL, NOT X IS NULL same, by update!
		stmt.executeUpdate("CREATE TABLE STAFF13 ("
				+ "EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15), "
				+ "PRIMARY KEY (EMPNUM), " + "CHECK (NOT EMPNAME IS NULL) )");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF13 "
				+ "VALUES('E1','Alice',36,'Deale');"));

		try {
			stmt.executeUpdate("UPDATE STAFF13 " + "SET EMPNAME = NULL "
					+ "WHERE EMPNUM = 'E1';");
			fail("UPDATE should violate check constraint");
		} catch (SQLException sqle) {
		}
		// PASS:0317 If ERROR, check constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF13 "
				+ "WHERE EMPNAME = 'Alice';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0317 If count = 1?
		// END TEST >>> 0317 <<< END TEST

		// TEST:0318 CHECK <like predicate> in <tab. cons.>, update!

		stmt.executeUpdate("CREATE TABLE STAFF9 "
				+ "(EMPNUM    CHAR(3) NOT NULL PRIMARY KEY, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY   CHAR(15), " + "CHECK (EMPNAME NOT LIKE 'T%'));");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF9 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		try {
			stmt.executeUpdate("UPDATE STAFF9 " + "SET EMPNAME = 'Thomas' "
					+ "WHERE EMPNUM = 'E3';");
			fail("UPDATE should violate check constraint");
			// PASS:0318 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF9 "
				+ "WHERE EMPNAME = 'Susan';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0318 If count = 1?

		// END TEST >>> 0318 <<< END TEST
	}
	/*
	 * Name: testCdr_007
	 * 
	 * Notes: Check constraints... what else?
	 *  
	 */

	public void testCdr_007() throws SQLException {
		// TEST:0319 CHECK <in predicate> in <tab. cons.>, update!

		stmt.executeUpdate("CREATE TABLE STAFF10 ("
				+ "EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15), "
				+ "PRIMARY KEY (EMPNUM), " + "CHECK (GRADE NOT IN (5,22)));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF10 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		try {
			stmt.executeUpdate("UPDATE STAFF10 " + "SET GRADE = 5"
					+ "WHERE EMPNUM = 'E3';");
			fail("UPDATE should violate check constraint");
			// PASS:0319 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF10 "
				+ "WHERE GRADE = 11;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0319 If count = 1?
		// END TEST >>> 0319 <<< END TEST

		// TEST:0320 CHECK combination pred. in <tab. cons.>, update!

		stmt.executeUpdate("CREATE TABLE STAFF11 ("
				+ "EMPNUM CHAR(3) NOT NULL PRIMARY KEY, "
				+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15), "
				+ "CHECK (GRADE NOT IN (5,22) AND EMPNAME NOT LIKE 'T%'));");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF11 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		try {
			stmt.executeUpdate("UPDATE STAFF11 " + "SET GRADE = 5 "
					+ "WHERE EMPNUM = 'E3';");
			// PASS:0320 If ERROR, check constraint, 0 rows updated?
			fail("UPDATE should violate check constraint");
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("UPDATE STAFF11 " + "SET EMPNAME = 'Tom' "
					+ "WHERE EMPNUM = 'E3'; ");
			// PASS:0320 If ERROR, check constraint, 0 rows updated?
			fail("UPDATE should violate check constraint");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF11 "
				+ "WHERE EMPNAME = 'Susan' AND GRADE = 11;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0320 If count = 1?
		// END TEST >>> 0320 <<< END TEST

		// TEST:0321 CHECK if X NOT LIKE/IN, NOT X LIKE/IN same, update!
		stmt.executeUpdate("CREATE TABLE STAFF12 ("
				+ "EMPNUM    CHAR(3) NOT NULL," + "EMPNAME  CHAR(20),"
				+ "GRADE DECIMAL(4)," + "CITY   CHAR(15),"
				+ "PRIMARY KEY (EMPNUM)," + "CHECK (NOT GRADE IN (5,22)"
				+ "AND NOT EMPNAME LIKE 'T%'));");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF12 "
				+ "VALUES('E3','Susan',11,'Hawaii');"));

		try {
			stmt.executeUpdate("UPDATE STAFF12 " + "SET GRADE = 5 "
					+ "WHERE EMPNUM = 'E3';");
			fail("UPDATE should violate check constraint");
			// PASS:0321 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF12 "
				+ "WHERE GRADE = 11;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0321 If count = 1?
		// END TEST >>> 0321 <<< END TEST

		// TEST:0322 CHECK <null predicate> in <col. cons>, update!

		stmt.executeUpdate("CREATE TABLE STAFF15 (" + "EMPNUM CHAR(3), "
				+ "EMPNAME  CHAR(20) NOT NULL, " + "GRADE DECIMAL(4), "
				+ "CITY CHAR(15));");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF15 "
				+ "VALUES('E1','Alice',52,'Deale');"));

		try {
			stmt.executeUpdate("UPDATE STAFF15 " + "SET EMPNAME = NULL "
					+ "WHERE EMPNUM = 'E1';");
			fail("UPDATE should violate check constraint");
			// PASS:0322 If ERROR, check constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF15 "
				+ "WHERE EMPNAME = 'Alice';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0322 If count = 1?
		// END TEST >>> 0322 <<< END TEST
	}

	/*
	 * Name: testCdr_008
	 * 
	 * Notes: PK/FK tests
	 *  
	 */

	public void testCdr_008() throws SQLException {
		// TEST:0323 (2 pr.,1 son),both P.K e, F.K e,insert another F.K!
		BaseTab.setupBaseTab(stmt);
		stmt
				.executeUpdate("CREATE TABLE PROJ3 ("
						+ "PNUM CHAR(3) NOT NULL, " + "PNAME CHAR(20), "
						+ "PTYPE CHAR(6), " + "BUDGET DECIMAL(9), "
						+ "CITY CHAR(15), " + "UNIQUE (PNUM));");

		stmt.executeUpdate("CREATE TABLE WORKS3 ("
				+ "EMPNUM CHAR(3) NOT NULL, " + "PNUM CHAR(3) NOT NULL, "
				+ "HOURS DECIMAL(5),"
				+ "FOREIGN KEY (EMPNUM) REFERENCES STAFF3(EMPNUM), "
				+ "FOREIGN KEY (PNUM) REFERENCES PROJ3(PNUM));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO PROJ3 "
				+ "VALUES ('P1','MASS','Design',10000,'Deale');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "VALUES ('E1','P1',40);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "VALUES ('E2','P1',40);"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS3;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0323 If count = 2?
		// END TEST >>> 0323 <<< END TEST

		// TEST:0324 (2 pr.,1 son),1 P.K exist,another not. insert F.K!
		// setup
		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");
		stmt.executeUpdate("DELETE  FROM PROJ3;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO PROJ3 "
				+ "VALUES ('P1','MASS','Design',10000,'Deale'); "));

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "SELECT * FROM STAFF;"));

		assertEquals(1, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "VALUES ('E1','P1',40);"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS3;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0324 If count = 1?

		try {
			stmt
					.executeUpdate("INSERT INTO WORKS3 "
							+ "VALUES ('E2','P2',40);");
			// PASS:0324 If RI ERROR, parent missing, 0 rows inserted?
			fail("INSERT should fail with FOREIGN KEY violation");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS3 "
				+ "WHERE PNUM = 'P2';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0324 If count = 0?
		// END TEST >>> 0324 <<< END TEST

		// TEST:0325 (2 pr.,1 son),both P.K e, F.K e, delete 1 P.K!

		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");
		stmt.executeUpdate("DELETE  FROM PROJ3;");

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "SELECT * FROM STAFF;"));

		assertEquals(6, stmt.executeUpdate("INSERT INTO PROJ3 "
				+ "SELECT * FROM PROJ;"));

		assertEquals(12, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "SELECT * FROM WORKS ;"));

		try {
			stmt.executeUpdate("DELETE FROM STAFF3 " + "WHERE EMPNUM='E1';");
			fail("DELETE should fail with key violation");
			// PASS:0325 If RI ERROR, children exist, 0 rows deleted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF3 "
				+ "WHERE EMPNUM = 'E1';");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		// PASS:0325 If 1 row selected and EMPNUM = E1?

		// END TEST >>> 0325 <<< END TEST

		// TEST:0326 (2 pr.,1 son),P.K e, no F.K, modify P.K!
		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "SELECT * FROM STAFF;"));

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF3 "
				+ "WHERE EMPNUM = 'E1';");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertFalse("too many rows", rs.next());
		// PASS:0326 If 1 row selected and EMPNUM = E1?

		stmt.executeUpdate("UPDATE STAFF3 " + "SET EMPNUM = 'E9' "
				+ "WHERE EMPNUM = 'E1';");

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF3 "
				+ "WHERE EMPNUM = 'E1'; ");
		assertFalse(rs.next());
		// PASS:0326 If 0 rows selected, SQLCODE = 100, end of data?
		// END TEST >>> 0326 <<< END TEST

	}
	/*
	 * Name: testCdr_009
	 * 
	 * Notes: More PK/FK tests
	 *  
	 */

	public void testCdr_009() throws SQLException {
		BaseTab.setupBaseTab(stmt);

		// TEST:0327 (2 pr.,1 son),check PRIMARY KEY unique via insert!

		stmt.executeUpdate("DELETE FROM STAFF3");
		stmt
				.executeUpdate("CREATE TABLE PROJ3 ("
						+ "PNUM CHAR(3) NOT NULL, " + "PNAME CHAR(20), "
						+ "PTYPE CHAR(6), " + "BUDGET DECIMAL(9), "
						+ "CITY CHAR(15), " + "UNIQUE (PNUM));");

		stmt.executeUpdate("CREATE TABLE WORKS3 ("
				+ "EMPNUM CHAR(3) NOT NULL, " + "PNUM CHAR(3) NOT NULL, "
				+ "HOURS DECIMAL(5),"
				+ "FOREIGN KEY (EMPNUM) REFERENCES STAFF3(EMPNUM), "
				+ "FOREIGN KEY (PNUM) REFERENCES PROJ3(PNUM));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "VALUES('E1','Alice',12,'Deale');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF3;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0327 If count = 1?

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES('E1','Tom',12,'Newyork');"));
			// PASS:0327 If ERROR, unique constraint, 0 rows inserted?
			fail("INSERT should produce Unique Constraint violation");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF3;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0327 If count = 1?
		// END TEST >>> 0327 <<< END TEST

		// TEST:0328 (2 pr.,1 son),F.K exist,modify P.K!

		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM PROJ3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "SELECT * FROM STAFF;"));

		assertEquals(6, stmt.executeUpdate("INSERT INTO PROJ3 "
				+ "SELECT * FROM PROJ;"));

		assertEquals(12, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "SELECT * FROM WORKS;"));

		try {
			stmt.executeUpdate("UPDATE STAFF3 " + "SET EMPNUM = 'E9' "
					+ "WHERE EMPNUM = 'E2';");
			fail("UPDATE should produce Referential Integrity violation");
			// PASS:0328 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF3 "
				+ "WHERE EMPNUM = 'E2';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0328 If count = 1?
		// END TEST >>> 0328 <<< END TEST

		// TEST:0329 (2 pr.,1 son),check PRIMARY KEY unique via modify!

		// setup
		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "VALUES('E1','Alice',45,'Deale');"));

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "VALUES('E2','Tom',45,'Deale'); "));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF3;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0329 If count = 2?

		try {
			stmt.executeUpdate("UPDATE STAFF3 " + "SET EMPNUM = 'E1' "
					+ "WHERE EMPNUM = 'E2';");
			fail("UPDATE should produce Unique Constraint violation");
		} catch (SQLException sqle) {
		}
		// PASS:0329 If ERROR, unique constraint, 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF3 "
				+ "WHERE EMPNUM = 'E2';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0329 If count = 1?
		// END TEST >>> 0329 <<< END TEST

		// TEST:0330 (2 pr.,1 son),modify F.K to no P.K corr.!

		// setup
		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM PROJ3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "SELECT * FROM STAFF;"));

		assertEquals(6, stmt.executeUpdate("INSERT INTO PROJ3 "
				+ "SELECT * FROM PROJ;"));

		assertEquals(12, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "SELECT * FROM WORKS;"));

		try {
			stmt.executeUpdate("UPDATE WORKS3 " + "SET EMPNUM = 'E9' "
					+ "WHERE EMPNUM = 'E2'; ");
			fail("UPDATE should produce Referential Integrity violation");
		} catch (SQLException sqle) {
		}
		// PASS:0330 If RI ERROR, parent missing, 0 rows updated?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS3 "
				+ "WHERE EMPNUM = 'E2';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0330 If count = 2?
		// END TEST >>> 0330 <<< END TEST

		// TEST:0331 (2 pr.,1 son),modify F.K to P.K corr. value!

		// setup
		stmt.executeUpdate("DELETE  FROM WORKS3;");
		stmt.executeUpdate("DELETE  FROM PROJ3;");
		stmt.executeUpdate("DELETE  FROM STAFF3;");

		assertEquals(5, stmt.executeUpdate("INSERT INTO STAFF3 "
				+ "SELECT * FROM STAFF;"));

		assertEquals(6, stmt.executeUpdate("INSERT INTO PROJ3 "
				+ "SELECT * FROM PROJ;"));

		assertEquals(12, stmt.executeUpdate("INSERT INTO WORKS3 "
				+ "SELECT * FROM WORKS;"));
		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS "
				+ "WHERE EMPNUM = 'E1';");
		// PASS:0331 If count = 6?

		assertEquals(6, stmt.executeUpdate("UPDATE WORKS3 "
				+ "SET EMPNUM = 'E2' " + "WHERE EMPNUM = 'E1';"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS3 "
				+ "WHERE EMPNUM = 'E1';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0331 If count = 0?
		// END TEST >>> 0331 <<< END TEST
	}

	/*
	 * Name: testCdr_010
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */

	public void testCdr_010() throws SQLException {
		SunTab.setupSunTab1(stmt);

		// TEST:0332 (self ref.), P.K exist, insert a F.K!
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E8','Alice',12,'Deale','E1');"));
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_C "
				+ "WHERE MGR = 'E1';");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0332 If count = 3?

		// restore. original test used rollback, but this is ok, too.
		stmt.executeUpdate("delete from staff_c where empnum = 'e8'");
		// END TEST >>> 0332 <<< END TEST

		// *************************************************
		// TEST:0333 (self ref.), delete P.K but F.K exist.!

		try {
			stmt.executeUpdate("DELETE FROM STAFF_C WHERE EMPNUM='E1';");
			fail("DELETE should cause Referential Integrity Error");
		} catch (SQLException sqle) {
		}
		// PASS:0333 If RI ERROR, children exist, 0 rows deleted?

		rs = stmt
				.executeQuery("SELECT EMPNUM FROM STAFF_C WHERE EMPNUM = 'E1';");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0333 If 1 row selected and EMPNUM = E1?

		// END TEST >>> 0333 <<< END TEST

		// *************************************************
		// TEST:0334 (self ref.), update P.K, no corr. F.K!

		stmt.executeUpdate("UPDATE STAFF_C SET MGR = NULL;");
		stmt.executeUpdate("DELETE FROM STAFF_C;");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E1','Alice',12,'Deale',NULL);");
		stmt.executeUpdate("UPDATE STAFF_C " + "SET EMPNUM = 'E9' "
				+ "WHERE EMPNUM = 'E1'; ");
		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF_C "
				+ "WHERE EMPNUM = 'E9';");
		rs.next();
		assertEquals("E9", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0334 If 1 row selected and EMPNUM = E9?

		// END TEST >>> 0334 <<< END TEST

	}
	/*
	 * Name: testCdr_011
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */

	public void testCdr_011() throws SQLException {
		SunTab.setupSunTab1(stmt);
		// TEST:0335 (self ref.), insert a F.K but no corr. P.K!

		try {
			stmt.executeUpdate("INSERT INTO STAFF_C "
					+ "VALUES('E8','Alice',12,'Deale','E9');");
			fail("INSERT should cause Referential Integrity violation");
		} catch (SQLException sqle) {
		}
		// PASS:0335 If RI ERROR, parent missing, 0 rows inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_C "
				+ "WHERE MGR = 'E9';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0335 If count = 0?
		// END TEST >>> 0335 <<< END TEST

		// *************************************************
		// TEST:0336 (self ref.), update P.K, but corr. F.K e.!

		try {
			stmt.executeUpdate("UPDATE STAFF_C " + "SET EMPNUM='E9' "
					+ "WHERE EMPNUM='E1';");
			// PASS:0336 If RI ERROR, children exist, 0 rows updated?
			fail("UPDATE should cause Referential Integrity violation");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF_C "
				+ "WHERE EMPNUM = 'E1';");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0336 If 1 row selected and EMPNUM = E1?

		// END TEST >>> 0336 <<< END TEST

		// *************************************************

		// TEST:0337 (self ref.), update P.K, check P.K unique via var.!

		try {
			stmt.executeUpdate("UPDATE STAFF_C " + "SET EMPNUM = 'E5' "
					+ "WHERE EMPNUM = 'E6';");
			fail("UPDATE should cause Referential Integrity violation");
			// PASS:0337 If ERROR, unique constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_C "
				+ "WHERE EMPNUM ='E6';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0337 If count = 1?
		// END TEST >>> 0337 <<< END TEST

		// *************************************************
		// TEST:0338 (self ref.), update F.K and no corr. P.K!

		try {
			stmt.executeUpdate("UPDATE STAFF_C " + "SET MGR= 'E9' "
					+ "WHERE MGR = 'E1';");
			fail("UPDATE should cause Referential Integrity violation");
			// PASS:0338 If RI ERROR, parent missing, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_C "
				+ "WHERE MGR = 'E1';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0338 If count = 2?
		// END TEST >>> 0338 <<< END TEST

		// *************************************************
		// TEST:0339 (self ref.), update F.K and corr. P.K exist!

		stmt.executeUpdate("UPDATE STAFF_C " + "SET MGR = 'E5' "
				+ "WHERE MGR = 'E7';");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_C "
				+ "WHERE MGR = 'E5';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0339 If count = 2?

		// END TEST >>> 0339 <<< END TEST}
	}

	/*
	 * Name: testCdr_012
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */

	public void testCdr_012() throws SQLException {
		SunTab.setupSunTab1(stmt);
		// TEST:0340 (ref. each other), insert F.K and corr. P.K e!

		assertEquals(1, stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES ('P7','IAC','Head',30000,'Alexdra','E5');"));

		assertEquals(1, stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES ('P8','IBM','Head',30000,'Alexdra','E5');"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ_M "
				+ "WHERE MGR = 'E5';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0340 If count = 2?

		// restore. deleting the rows because i can't assume a rollback
		// operation.
		stmt.executeUpdate("delete from proj_m where pNum in ('p7', 'p8') ");
		// END TEST >>> 0340 <<< END TEST

		// *************************************************
		// TEST:0341 (ref. each other), delete P.K but corr. F.K e!

		try {
			stmt.executeUpdate("DELETE FROM STAFF_M " + "WHERE EMPNUM='E2';");
			// PASS:0341 If RI ERROR, children exist, 0 rows deleted?
			fail("DELETE should cause Referential Integrity violation");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF_M "
				+ "WHERE EMPNUM = 'E2';");
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0341 If 1 row selected and EMPNUM = E2?
		// END TEST >>> 0341 <<< END TEST

		// *************************************************
		// TEST:0342 (ref. each other), update P.K and no corr. F.K!

		stmt.executeUpdate("UPDATE STAFF_M " + "SET EMPNUM = 'E9' "
				+ "WHERE EMPNUM = 'E1';");

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF_M "
				+ "WHERE EMPNUM = 'E9'; ");
		rs.next();
		assertEquals("E9", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0342 If 1 row selected and EMPNUM = E9?

		// END TEST >>> 0342 <<< END TEST

	}

	/*
	 * Name: testCdr_013
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_013() throws SQLException {
		SunTab.setupSunTab1(stmt);

		// TEST:0343 (ref. each other), update P.K and corr. F.K e!

		try {
			stmt.executeUpdate("UPDATE STAFF_M " + "SET EMPNUM = 'E9' "
					+ "WHERE EMPNUM = 'E2'; ");
			// PASS:0343 If RI ERROR, children exist, 0 rows updated?
			fail("UPDATE should cause Referential Integrity violation");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_M "
				+ "WHERE EMPNUM = 'E2';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0343 If count = 1?

		// END TEST >>> 0343 <<< END TEST

		// *************************************************
		// TEST:0344 (ref. each other), update F.K to no corr. P.K!

		try {
			stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E9' "
					+ "WHERE MGR = 'E3';");
			// PASS:0344 If RI ERROR, parent missing, 0 rows updated?
			fail("UPDATE should cause Referential Integrity violation");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ_M "
				+ "WHERE MGR = 'E3';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0344 If count = 1?
		// END TEST >>> 0344 <<< END TEST

		// *************************************************
		// TEST:0345 (ref. each other), update F.K to corr. P.K e!

		stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E5' "
				+ "WHERE MGR = 'E3';");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ_M "
				+ "WHERE MGR = 'E5';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0345 If count = 1?
		// END TEST >>> 0345 <<< END TEST

		// *************************************************
		// TEST:0346 (ref. each other), insert F.K and no corr. P.K!

		try {
			stmt.executeUpdate("INSERT INTO STAFF_M "
					+ "VALUES('E8','Alice',12,'Deale','P9');");
			fail("INSERT should cause Referential Integrity violation");
			// PASS:0346 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_M "
				+ "WHERE PRI_WK = 'P9';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0346 If count = 0?

		// END TEST >>> 0346 <<< END TEST
	}

	/*
	 * Name: testCdr_014
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_014() throws SQLException {
		SunTab.setupSunTab2(stmt);
		SunTab.refreshSunTab2(stmt);

		// TEST:0347 FIPS sz. (comb.keys=6), P.K unique,insert!
		// FIPS sizing TEST

		try {
			stmt.executeUpdate("INSERT INTO SIZ1_P "
					+ "VALUES('E1','TTT',1,'SSS',10,'RRR','HHH','ZZZ',6);");
			fail("INSERT should cause Unique Constraint violation");
			// PASS:0347 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_P "
				+ "WHERE S1 = 'E1';");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0347 If count = 3?

		// END TEST >>> 0347 <<< END TEST

		// *************************************************

		// TEST:0348 FIPS sz. (comb.keys=6), insert F.K & no corr. P.K!
		// FIPS sizing TEST

		try {
			stmt.executeUpdate("INSERT INTO SIZ1_F "
					+ "VALUES('E1','TTT',1,'SSS',19,'RRS','TTT',5,6);");
			// PASS:0348 If RI ERROR, parent missing, 0 rows inserted?
			fail("INSERT should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_F "
				+ "WHERE F6 = 'RRS';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0348 If count = 0?

		// END TEST >>> 0348 <<< END TEST

	}
	/*
	 * Name: testCdr_015
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_015() throws SQLException {
		SunTab.setupSunTab2(stmt);
		SunTab.refreshSunTab2(stmt);

		// TEST:0349 FIPS sz. (comb.keys=6), delete P.K & corr. F.K e!
		// FIPS sizing TEST

		try {
			stmt.executeUpdate("DELETE FROM SIZ1_P "
					+ "WHERE S1 = 'E1' OR S6 = 'RRR';");
			fail("DELETE should violate Referential Integrity");

			// PASS:0349 If RI ERROR, children exist, 0 rows deleted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT S1 FROM SIZ1_P "
				+ "WHERE S1 = 'E1' AND S2 = 'TTT';");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0349 If 1 row selected and S1 = E1?

		// END TEST >>> 0349 <<< END TEST
		// *************************************************

		// TEST:0350 FIPS sz. (comb.keys=6), update P.K & no corr. F.K!
		// FIPS sizing TEST

		SunTab.refreshSunTab2(stmt);
		stmt.executeUpdate("DELETE FROM SIZ1_F " + "WHERE F1 = 'E1';");

		stmt.executeUpdate("UPDATE SIZ1_P " + "SET S1 = 'E9' "
				+ "WHERE S1 = 'E1' AND S2 = 'TTT';");

		rs = stmt.executeQuery("SELECT S1 FROM SIZ1_P " + "WHERE S1 = 'E9';");
		rs.next();
		assertEquals("E9", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0350 If 1 row selected and S1 = E9?
		// END TEST >>> 0350 <<< END TEST

		// *************************************************
		// TEST:0351 FIPS sz. (comb.keys=6), update P.K & corr. P.K e!
		// FIPS sizing TEST
		SunTab.refreshSunTab2(stmt);

		try {
			stmt.executeUpdate("UPDATE SIZ1_P " + "SET S1 = 'E9' "
					+ "WHERE S1 = 'E1' AND S2 = 'TTS' AND S3 =1;");
			fail("UPDATE should cause Referential Integrity error");
			// PASS:0351 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_P "
				+ "WHERE S1 = 'E1' AND S2 = 'TTS' AND S3 =1;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0351 If count = 1?
		// END TEST >>> 0351 <<< END TEST

	}
	/*
	 * Name: testCdr_016
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_016() throws SQLException {
		SunTab.setupSunTab2(stmt);
		SunTab.refreshSunTab2(stmt);
		// TEST:0352 FIPS sz. (comb.keys=6), P.K unique, update!
		// FIPS sizing TEST

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_P "
				+ "WHERE S1 = 'E1' AND S2 = 'TTS' AND S3 = 1;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0352 If count = 1?

		try {
			stmt.executeUpdate("UPDATE SIZ1_P " + "SET S2 = 'TTT' "
					+ "WHERE S1 = 'E1' AND S2 = 'TTS' AND S3 = 1;");
			fail("UPDATE should violate Unique Constraint");
			// PASS:0352 If ERROR, unique constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}
		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_P "
				+ "WHERE S1 = 'E1' AND S2 = 'TTT' AND S3 = 1;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0352 If count = 1?
		// END TEST >>> 0352 <<< END TEST

		// *************************************************
		// TEST:0353 FIPS sz. (comb.keys=6), update F.K to no corr. P.K!
		// FIPS sizing TEST

		SunTab.refreshSunTab2(stmt);
		try {
			stmt.executeUpdate("UPDATE SIZ1_F " + "SET F1 = 'E9' "
					+ "WHERE F1 = 'E2';");
			fail("UPDATE should violate Referential Integrity constraint");
			// PASS:0353 If RI ERROR, parent missing, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_F "
				+ "WHERE F1 = 'E2'; ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0353 If count = 3?
		// END TEST >>> 0353 <<< END TEST

		// *************************************************
		// TEST:0354 FIPS sz. (comb.keys=6), update F.K to corr. P.K e!
		// FIPS sizing TEST

		SunTab.refreshSunTab2(stmt);
		stmt.executeUpdate("UPDATE SIZ1_F " + "SET F1 = 'E1' "
				+ "WHERE F1 = 'E2' AND F6 = 'RRR'; ");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ1_F "
				+ "WHERE F1 = 'E1';");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0354 If count = 4?
		// END TEST >>> 0354 <<< END TEST

	}
	/*
	 * Name: testCdr_017
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_017() throws SQLException {
		SunTab.setupSunTab2(stmt);
		SunTab.refreshSunTab2(stmt);

		// TEST:0355 FIPS sz. (1 pr.,6 son), insert F.K & no corr. P.K!
		// FIPS sizing TEST

		try {
			stmt
					.executeUpdate("INSERT INTO SIZ2_F1 "
							+ "VALUES ('  E','AAA');");
			fail("INSERT should violate Integrity Constraints");
			// PASS:0355 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO SIZ2_F10 " + "VALUES (9,'AAB');");
			// PASS:0355 If RI ERROR, parent missing, 0 rows inserted?
			fail("INSERT should violate Integrity Constraints");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ2_F10;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0355 If count = 4?
		// END TEST >>> 0355 <<< END TEST

		// *************************************************
		// TEST:0356 FIPS sz. (1 pr.,6 son), delete P.K & corr. F.K e!
		// FIPS sizing TEST

		SunTab.refreshSunTab2(stmt);
		try {
			stmt.executeUpdate("DELETE FROM SIZ2_P " + "WHERE P1 = '  A';");
			fail("DELETE should violate Referential Integrity");
			// PASS:0356 If RI ERROR, children exist, 0 rows deleted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT P1 FROM SIZ2_P " + "WHERE P1 = '  A';");
		rs.next();
		assertEquals("  A", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0356 If 1 row selected and P1 = ' A'?
		// END TEST >>> 0356 <<< END TEST

		// *************************************************
		// TEST:0357 FIPS sz. (1 pr.,6 son), update P.K but corr. F.K e!
		// FIPS sizing TEST
		SunTab.refreshSunTab2(stmt);

		try {
			stmt.executeUpdate("UPDATE SIZ2_P " + "SET P1 = '  Z' "
					+ "WHERE P1 = '  A';");
			fail("UPDATE should violate Referential Integrity");
			// PASS:0357 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("UPDATE SIZ2_P " + "SET P10 = 100 "
					+ "WHERE P10 = 8;");
			fail("UPDATE should violate Referential Integrity");
			// PASS:0357 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ2_P "
				+ "WHERE P1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0357 If count = 1?
		// END TEST >>> 0357 <<< END TEST

		// *************************************************
		// TEST:0358 FIPS sz. (1 pr.,6 son), check key unique, update!
		// FIPS sizing TEST

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ2_P "
				+ "WHERE P1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0358 If count = 1?

		try {
			stmt.executeUpdate("UPDATE SIZ2_P " + "SET P1 = 'B  ' "
					+ "WHERE P1 = '  A';");
			fail("UPDATE should violate Unique Constraint");
			// PASS:0358 If ERROR, unique constraint, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ2_P "
				+ "WHERE P1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0358 If count = 1?
		// END TEST >>> 0358 <<< END TEST

		// *************************************************
		// TEST:0359 FIPS sz. (1 pr.,6 son), update F.K to no corr. P.K!
		// FIPS sizing TEST

		try {
			stmt.executeUpdate("UPDATE SIZ2_F1 " + "SET F1 = '  Z' "
					+ "WHERE F1 = '  A'; ");
			fail("UPDATE should violate Referential Integrity");
			// PASS:0359 If RI ERROR, parent missing, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ2_F1 "
				+ "WHERE F1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0359 If count = 1?
		// END TEST >>> 0359 <<< END TEST

		// *************************************************
		// TEST:0375 ref. integrity with computation!
		SunTab.refreshSunTab2(stmt);

		try {
			stmt.executeUpdate("UPDATE SIZ3_P5 "
					+ "SET F1 = 10 * 10 / 20 - 10 + 16 " + "WHERE  F1 = 5;");
			fail("UPDATE should violate Referential Integrity");
			// PASS:0375 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_P5 "
				+ "WHERE F1 = 11;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0375 If count = 0?

		stmt.executeUpdate("DELETE FROM SIZ3_F " + "WHERE P5 = 5;");

		stmt.executeUpdate("UPDATE SIZ3_P5 "
				+ "SET F1 = 10 * 10 / 20 - 10 + 16 " + "WHERE  F1 = 5; ");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_P5 "
				+ "WHERE F1 = 11;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0375 If count = 1?
		// END TEST >>> 0375 <<< END TEST

		// *************************************************
		// TEST:0376 ref. integrity with join!
		SunTab.refreshSunTab2(stmt);

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM SIZ3_F,SIZ3_P1,SIZ3_P2,SIZ3_P3,SIZ3_P4, "
				+ "SIZ3_P5,SIZ3_P6 " + "WHERE P1 = SIZ3_P1.F1 "
				+ "AND P2 = SIZ3_P2.F1 " + "AND P3 = SIZ3_P3.F1 "
				+ "AND P4 = SIZ3_P4.F1 " + "AND P5 = SIZ3_P5.F1 "
				+ "AND P6 = SIZ3_P6.F1 " + "AND SIZ3_P3.F1 BETWEEN 1 AND 2;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0376 If count = 4?

		// END TEST >>> 0376 <<< END TEST
	}

	/*
	 * Name: testCdr_018
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_018() throws SQLException {
		SunTab.setupSunTab2(stmt);
		SunTab.refreshSunTab2(stmt);
		// TEST:0360 FIPS sz. (6 pr.,1 son), insert F.K, without P.K!
		// FIPS sizing TEST

		try {
			stmt
					.executeUpdate("INSERT INTO SIZ3_F "
							+ "VALUES ('  F','  D',3,'  E',4,'  F','  G',5,6,7,'TTT');");
			fail("INSERT should violate Referntial Integrity");
			// PASS:0360 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt
					.executeUpdate("INSERT INTO SIZ3_F "
							+ "VALUES ('  D','  E',4,'  F',5,'  G','  H',6,7,100,'TTT');");
			// PASS:0360 If RI ERROR, parent missing, 0 rows inserted?
			fail("INSERT should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_F;");
		rs.next();
		assertEquals(8, rs.getInt(1));
		// PASS:0360 If count = 8?

		// END TEST >>> 0360 <<< END TEST

		// *************************************************

		// TEST:0361 FIPS sz. (6 pr.,1 son), delete P.K, but corr.F.K e!
		// FIPS sizing TEST
		SunTab.refreshSunTab2(stmt);

		try {
			stmt.executeUpdate("DELETE FROM SIZ3_P1 " + "WHERE F1 = '  A';");
			// PASS:0361 If RI ERROR, children exist, 0 rows deleted?
			fail("DELETE should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT F1 FROM SIZ3_P1 " + "WHERE F1 = '  A';");
		// PASS:0361 If 1 row selected and F1 = ' A'?
		rs.next();
		assertEquals("A", rs.getString(1).trim());
		assertFalse(rs.next());

		// END TEST >>> 0361 <<< END TEST

		// *************************************************
		// TEST:0362 FIPS sz. (6 pr.,1 son), update P.K, but corr.F.K e!
		// FIPS sizing TEST

		try {
			stmt.executeUpdate("UPDATE SIZ3_P1 " + "SET F1 = '  Z' "
					+ "WHERE F1 = '  A'; ");
			// PASS:0362 If RI ERROR, children exist, 0 rows updated?
			fail("UPDATE should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("UPDATE SIZ3_P10 " + "SET F1 = 100 "
					+ "WHERE F1 = 8; ");
			// PASS:0362 If RI ERROR, children exist, 0 rows updated?
			fail("UPDATE should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_P1 "
				+ "WHERE F1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0362 If count = 1?
		// END TEST >>> 0362 <<< END TEST

		// *************************************************

		// TEST:0363 FIPS sz. (6 pr.,1 son), check key unique ,update!
		// FIPS sizing TEST

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_P1 "
				+ "WHERE F1 = '  A'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0363 If count = 1?

		try {
			stmt.executeUpdate("UPDATE SIZ3_P1 " + "SET F1 = '  B'"
					+ "WHERE F1 = '  A';");
			// PASS:0363 If ERROR, unique constraint, 0 rows updated?
			fail("UPDATE should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_P1 "
				+ "WHERE F1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0363 If count = 1?
		// END TEST >>> 0363 <<< END TEST

		// *************************************************
		// TEST:0364 FIPS sz. (6 pr.,1 son), update F.K to no corr. P.K!
		// FIPS sizing TEST

		try {
			stmt.executeUpdate("UPDATE SIZ3_F " + "SET P1 = '  Z' "
					+ "WHERE P1 = '  A';");
			// PASS:0364 If RI ERROR, parent missing, 0 rows updated?
			fail("UPDATE should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SIZ3_F "
				+ "WHERE P1 = '  A';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0364 If count = 1?

		// END TEST >>> 0364 <<< END TEST

	}
	/*
	 * Name: testCdr_019
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_019() throws SQLException {

		// TEST:0365 (3-level schema), check insert F.K!
		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);
		try {
			stmt.executeUpdate("INSERT INTO EMP VALUES "
					+ "(41,'Tom','China Architecture', "
					+ "20,'Architecture',040553);");
			// PASS:0365 If RI ERROR, parent missing, 0 rows inserted?
			fail("INSERT should cause Referential Integrity violation");
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("INSERT INTO DEPT VALUES "
				+ "(20,'Architecture','Richard');");

		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(41,'Tom','China Architecture', "
				+ "20,'Architecture',040553);");

		rs = stmt.executeQuery("SELECT COUNT(*)  FROM EMP "
				+ "WHERE ENO = 41; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0365 If count = 1?
		// END TEST >>> 0365 <<< END TEST

		// *************************************************
		// TEST:0366 (3-level schema), check delete P.K!
		SunTab.refreshSunTab3(stmt);

		try {
			stmt.executeUpdate("DELETE FROM EMP " + "WHERE ENO = 21;");
			fail("DELETE should produce Referential Integrity violation");
			// PASS:0366 If RI ERROR, children exist, 0 rows deleted?
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("DELETE FROM EXPERIENCE "
				+ "WHERE EXP_NAME = 'Tom' AND BTH_DATE = 040523; ");

		stmt.executeUpdate("DELETE FROM EMP " + "WHERE ENO = 21;");

		rs = stmt
				.executeQuery("SELECT COUNT(*)  FROM EMP " + "WHERE ENO = 21;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0366 If count = 0?
		// END TEST >>> 0366 <<< END TEST

		// *************************************************
		// TEST:0367 (3-level schema), update mid. tab. check P.K & F.K!
		SunTab.refreshSunTab3(stmt);
		try {
			stmt.executeUpdate("UPDATE EMP " + "SET ENAME = 'Thomas' "
					+ "WHERE ENO = 21; ");
			fail("UPDATE should produce Referential Integrity violation");
			// PASS:0367 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("UPDATE EMP " + "SET DNAME = 'Agriculture' "
					+ "WHERE  ENO = 21; ");
			// PASS:0367 If RI ERROR, parent missing, 0 rows updated?
			fail("UPDATE should produce Referential Integrity violation");
		} catch (SQLException sqle) {
		}
		stmt.executeUpdate("UPDATE EMP " + "SET DNAME = 'Education' "
				+ "WHERE  ENO = 21;");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM EMP "
				+ "WHERE DNO = 12 AND DNAME = 'Education' "
				+ "AND ENO = 21 AND ENAME = 'Tom';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0367 If count = 1?
		// END TEST >>> 0367 <<< END TEST

		// *************************************************
		// TEST:0368 (3-level schema), check update P.K!
		SunTab.refreshSunTab3(stmt);

		try {
			stmt.executeUpdate("UPDATE EMP " + "SET ENAME = 'Thomas' "
					+ "WHERE ENO = 21;");
			fail("UPDATE should produce Referential Integrity violation");
			// PASS:0368 If RI ERROR, children exist, 0 rows updated?
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(30,'Thomas','Languages & Operating System', "
				+ "12,'Computer',040523); ");

		stmt.executeUpdate("UPDATE EXPERIENCE " + "SET EXP_NAME = 'Thomas' "
				+ "WHERE EXP_NAME = 'Tom' AND BTH_DATE = 040523; ");

		stmt.executeUpdate("DELETE FROM EMP " + "WHERE  ENO = 21;");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM EMP "
				+ "WHERE DNO = 12 AND ENO = 21 " + "AND ENAME = 'Tom'; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0368 If count = 0?

		// END TEST >>> 0368 <<< END TEST
	}
	/*
	 * Name: testCdr_021
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_021() throws SQLException {

		// TEST:0378 (ref. acr. sch.) delete P.K and corr. F.K e.!
		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);
		SulTab.setupSulTab1(stmt);
		SulTab.refreshSulTab1(stmt);

		try {
			stmt.executeUpdate("DELETE FROM STAFF_P " + "WHERE EMPNUM='E1';");
			fail("DELETE should violate Referential Integrity");
			// PASS:0378 If RI ERROR, children exist, 0 rows deleted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT EMPNUM FROM STAFF_P "
				+ "WHERE EMPNUM = 'E1'; ");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0378 If 1 row selected and EMPNUM = E1?
		// END TEST >>> 0378 <<< END TEST

		// *************************************************
		// TEST:0379 (ref. acr. sch.) update P.K and corr. F.K e.!

		try {
			stmt.executeUpdate("UPDATE STAFF_P " + "SET EMPNUM = 'E9' "
					+ "WHERE EMPNUM = 'E2';");
			// PASS:0379 If RI ERROR, children exist, 0 rows updated?
			fail("UPDATE should violate Referential Integrity");
		} catch (SQLException sqle) {
		}
		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_P "
				+ "WHERE EMPNUM = 'E2';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0379 If count = 1?
		// END TEST >>> 0379 <<< END TEST
	}

	/*
	 * Name: testCdr_022
	 * 
	 * Notes: Primary key / foreigh key tests
	 *  
	 */
	public void testCdr_022() throws SQLException {
		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);
		SulTab.setupSulTab1(stmt);
		SulTab.refreshSulTab1(stmt);

		// TEST:0380 (ref. acr. sch.) insert F.K and no corr. P.K!

		try {
			stmt.executeUpdate("INSERT INTO WORKS_P "
					+ "VALUES ('E9','P2',20); ");
			// PASS:0380 If RI ERROR, parent missing, 0 rows inserted?
			fail("INSERT should violate Referential Integrity");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS_P "
				+ "WHERE EMPNUM = 'E9'; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0380 If count = 0?

		// END TEST >>> 0380 <<< END TEST

		// *************************************************

		// TEST:0381 (ref. acr. sch.) update F.K to no P.K corr.!
		try {
			stmt.executeUpdate("UPDATE WORKS_P " + "SET EMPNUM = 'E9' "
					+ "WHERE EMPNUM = 'E2'; ");
			fail("UPDATE should violate Referential Integrity");
			// PASS:0381 If RI ERROR, parent missing, 0 rows updated?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS_P "
				+ "WHERE EMPNUM = 'E2'; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0381 If count = 2?

		// END TEST >>> 0381 <<< END TEST
	}
	/*
	 * Name: testCdr_023
	 * 
	 * Notes: Primary key / foreign key tests with GRANT permissions.
	 * 
	 * TODO: Need ability to CREATE USER and GRANT/REVOKE to execute these
	 * tests.
	 *  
	 */
	public void testCdr_023() throws SQLException {
		//		SunTab.setupSunTab2(stmt);
		//		SunTab.refreshSunTab2(stmt);
		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);
		stmt.executeUpdate("CREATE TABLE TTT(P1 DECIMAL(4) NOT NULL UNIQUE,"
				+ "P2 CHAR(4));");
		stmt.executeUpdate("GRANT SELECT ON TTT TO sun;");

		stmt.executeUpdate("CREATE TABLE ACR_SCH_F(F1 DECIMAL(4), "
				+ "F2 CHAR(4), " + "FOREIGN KEY (F1) "
				+ "REFERENCES ACR_SCH_P(P1));");

		// TEST:0382 (ref. acr. sch.) with GRANT OPTION, insert!

		stmt.executeUpdate("INSERT INTO ACR_SCH_F " + "VALUES(1,'DOG');");

		try {
			stmt.executeUpdate("INSERT INTO ACR_SCH_F " + "VALUES(2,'PIG');");
			fail("INSERT should violate Referential Integrity");
			// PASS:0382 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM ACR_SCH_F; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0382 If count = 1?
		// END TEST >>> 0382 <<< END TEST

		// tests that depend on grant/revoke moved to
		// TestQueriesThatUseGrantRevoke.java

		// END TEST >>> 0384 <<< END TEST
	}

	/*
	 * Name: testCdr_024
	 * 
	 * Notes: character default column values, exact numeric default column
	 * values, approximate numeric default column values.
	 *  
	 */
	public void testCdr_024() throws SQLException {

		// TEST:0385 character default column values!
		stmt.executeUpdate("CREATE TABLE CHAR_DEFAULT "
				+ "(SEX_CODE  CHAR(1)  DEFAULT 'F', "
				+ "NICKNAME  CHAR(20) DEFAULT 'No nickname given', "
				+ "INSURANCE1 CHAR(5)  DEFAULT 'basic');");

		// setup
		assertEquals(1, stmt.executeUpdate("INSERT INTO CHAR_DEFAULT ( "
				+ "SEX_CODE) VALUES ('M');"));
		// PASS:0385 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT NICKNAME, INSURANCE1 "
				+ "FROM CHAR_DEFAULT " + "WHERE SEX_CODE = 'M';");
		rs.next();
		assertEquals("No nickname given", rs.getString(1).trim());
		assertEquals("basic", rs.getString(2).trim());
		// PASS:0385 If NICKNAME = 'No nickname given', INSURANCE1 = 'basic'?

		stmt.executeUpdate("INSERT INTO CHAR_DEFAULT(NICKNAME, INSURANCE1) "
				+ "VALUES ('Piggy', 'Kaise');");
		// PASS:0385 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT SEX_CODE " + "FROM CHAR_DEFAULT "
				+ "WHERE INSURANCE1 = 'Kaise'; ");
		rs.next();
		assertEquals("F", rs.getString(1).trim());
		// PASS:0385 If SEX_CODE = 'F'?

		// END TEST >>> 0385 <<< END TEST
		// *************************************************************

		// TEST:0386 exact numeric default column values!

		// setup
		stmt.executeUpdate("CREATE TABLE EXACT_DEF "
				+ "(BODY_TEMP NUMERIC(4,1) DEFAULT 98.6, "
				+ "MAX_NUM   NUMERIC(5) DEFAULT -55555, "
				+ "MIN_NUM   DEC(6,6) DEFAULT .000001);");

		assertEquals(1, stmt.executeUpdate("INSERT INTO EXACT_DEF "
				+ "VALUES (98.3, -55556, .000001);"));
		// PASS:0386 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EXACT_DEF(BODY_TEMP) "
				+ "VALUES (99.0);"));
		// PASS:0386 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EXACT_DEF"
				+ "(MAX_NUM, MIN_NUM) " + "VALUES (100, .2);"));
		// PASS:0386 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM EXACT_DEF "
				+ "WHERE BODY_TEMP = 99.0 AND "
				+ "MAX_NUM = -55555 AND MIN_NUM = .000001 "
				+ "OR BODY_TEMP = 98.6 AND MAX_NUM = 100 AND MIN_NUM = .2;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0386 If count = 2?
		// END TEST >>> 0386 <<< END TEST
		// *************************************************************

		// TEST:0387 approximate numeric default column values!

		// setup
		stmt.executeUpdate("CREATE TABLE APPROX_DEF "
				+ "(X_COUNT REAL DEFAULT 1.78E12, "
				+ "Y_COUNT REAL DEFAULT -9.99E10, "
				+ "Z_COUNT REAL DEFAULT 3.45E-11, "
				+ "ZZ_COUNT REAL DEFAULT -7.6777E-7);");

		assertEquals(1, stmt.executeUpdate("INSERT INTO APPROX_DEF(X_COUNT) "
				+ "VALUES (5.0E5);"));
		// PASS:0387 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO APPROX_DEF "
				+ "VALUES (1.78E11, -9.9E10, 3.45E-10, 7.6777E-7);"));
		// PASS:0387 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO APPROX_DEF("
				+ "Y_COUNT, Z_COUNT, ZZ_COUNT) "
				+ "VALUES (1.0E3, 2.0E4, 3.8E6);"));
		// PASS:0387 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM APPROX_DEF "
				+ "WHERE (Y_COUNT BETWEEN -9.991E10 AND -9.989E10) AND "
				+ "(Z_COUNT BETWEEN 3.44E-11 AND 3.46E-11) AND "
				+ "(ZZ_COUNT BETWEEN -7.6778E-7 AND -7.6776E-7) OR "
				+ "(X_COUNT BETWEEN 1.77E12 AND 1.79E12); ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0387 If count = 2?
		// END TEST >>> 0387 <<< END TEST

		// *************************************************************
		// TEST:0388 FIPS sz. default column values!
		// setup
		stmt.executeUpdate("CREATE TABLE SIZE_TAB "
				+ "(COL1 CHAR(75)  DEFAULT "
				+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
				+ "abcdefghijklmnopqrstuvwxyz0123456789012',"
				+ "COL2 INTEGER   DEFAULT -999888777, "
				+ "COL3 DEC(15,6) DEFAULT 987654321.123456, "
				+ "COL4 REAL      DEFAULT -1.048576E22); ");
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO SIZE_TAB(COL1) VALUES( "
						+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
						+ "abcdefghijklmnopqrstuvwxyz0123456789012');"));
		// PASS:0388 If 1 row is inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO SIZE_TAB(COL2, COL3, COL4) "
						+ "VALUES (-999888777, 987654321.123456, -1.45E22);"));
		// PASS:0388 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO SIZE_TAB "
				+ "VALUES('ABCDEFG',7,7,-1.49E22);"));
		// PASS:0388 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*)  FROM SIZE_TAB "
				+ "WHERE COL4 BETWEEN -1.46E22 AND -1.048575E22 "
				+ " GROUP BY COL1, COL2, COL3;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0388 If count = 2?
		// END TEST >>> 0388 <<< END TEST
	}
	/*
	 * Name: testCdr_025
	 * 
	 * Notes:
	 *  
	 */
	public void testCdr_025() throws SQLException {
		stmt.executeUpdate("CREATE TABLE COMMODITY "
				+ "(C_NUM INTEGER NOT NULL, "
				+ "C_NAME CHAR(7) NOT NULL UNIQUE, " + "PRIMARY KEY (C_NUM));");

		stmt.executeUpdate("CREATE TABLE CURRENCY_TABLE "
				+ "(CURRENCY CHAR(10) NOT NULL, "
				+ "DOLLAR_EQUIV NUMERIC(5, 2), " + "PRIMARY KEY (CURRENCY));");

		stmt.executeUpdate("CREATE TABLE MEASURE_TABLE "
				+ "(MEASURE CHAR(8) NOT NULL, " + "POUND_EQUIV NUMERIC(8,2), "
				+ "PRIMARY KEY (MEASURE));");

		stmt.executeUpdate("CREATE TABLE C_TRANSACTION "
				+ "(COMMOD_NO INTEGER, " + "TOT_PRICE     DECIMAL(12,2), "
				+ "CURRENCY  CHAR(10), " + "UNITS     INTEGER, "
				+ "MEASURE   CHAR(8), " + "T_DATE    INTEGER, "
				+ "FOREIGN KEY (COMMOD_NO)  " + "REFERENCES COMMODITY, "
				+ "FOREIGN KEY (CURRENCY) " + "REFERENCES CURRENCY_TABLE, "
				+ "FOREIGN KEY (MEASURE) " + "REFERENCES MEASURE_TABLE); ");

		stmt
				.executeUpdate("CREATE VIEW DOLLARS_PER_POUND (COMMODITY, UNIT_PRICE, FROM_DATE, TO_DATE) "
						+ "AS SELECT COMMODITY.C_NAME,  "
						+ "SUM(TOT_PRICE * DOLLAR_EQUIV) / SUM(UNITS * POUND_EQUIV), "
						+ "MIN(T_DATE), MAX(T_DATE) "
						+ "FROM C_TRANSACTION, COMMODITY, CURRENCY_TABLE, MEASURE_TABLE "
						+ "WHERE C_TRANSACTION.COMMOD_NO = COMMODITY.C_NUM "
						+ "AND C_TRANSACTION.CURRENCY = CURRENCY_TABLE.CURRENCY "
						+ "AND C_TRANSACTION.MEASURE  = MEASURE_TABLE.MEASURE "
						+ "GROUP BY COMMODITY.C_NAME "
						+ "HAVING SUM(TOT_PRICE * DOLLAR_EQUIV) > 10000; ");

		stmt
				.executeUpdate("CREATE VIEW COST_PER_UNIT "
						+ "(COMMODITY, UNIT_PRICE, CURRENCY, MEASURE) "
						+ "AS SELECT COMMODITY, UNIT_PRICE * POUND_EQUIV / DOLLAR_EQUIV, "
						+ "CURRENCY, MEASURE "
						+ "FROM DOLLARS_PER_POUND, CURRENCY_TABLE, MEASURE_TABLE; ");
		// setup
		stmt.executeUpdate("INSERT INTO COMMODITY VALUES (17, 'Wheat'); ");
		stmt.executeUpdate("INSERT INTO COMMODITY VALUES (14, 'Saffron'); ");
		stmt.executeUpdate("INSERT INTO COMMODITY VALUES (23, 'Alfalfa'); ");

		stmt.executeUpdate("INSERT INTO CURRENCY_TABLE "
				+ "VALUES ('DOLLAR', 1.00); ");
		stmt.executeUpdate("INSERT INTO CURRENCY_TABLE "
				+ "VALUES ('POUND', 1.91); ");
		stmt.executeUpdate("INSERT INTO CURRENCY_TABLE "
				+ "VALUES ('DM', .45); ");

		stmt.executeUpdate("INSERT INTO MEASURE_TABLE "
				+ "VALUES ('POUND', 1.00); ");
		stmt.executeUpdate("INSERT INTO MEASURE_TABLE "
				+ "VALUES ('OUNCE', .06); ");
		stmt.executeUpdate("INSERT INTO MEASURE_TABLE "
				+ "VALUES ('KILO', 2.20); ");
		stmt
				.executeUpdate("INSERT INTO MEASURE_TABLE VALUES ('TON', 2000.00); ");

		stmt.executeUpdate("INSERT INTO C_TRANSACTION "
				+ "VALUES (17, 1411.5, 'DM', 4000, 'KILO', 871212); ");
		stmt.executeUpdate("INSERT INTO C_TRANSACTION "
				+ "VALUES (17, 7000.0, 'POUND', 100, 'TON', 871012); ");
		stmt.executeUpdate("INSERT INTO C_TRANSACTION "
				+ "VALUES (23, 20000.0, 'DOLLAR', 40000, 'POUND', 880707); ");
		stmt.executeUpdate("INSERT INTO C_TRANSACTION "
				+ "VALUES (14, 10000.0, 'DM', 900, 'OUNCE', 880606); ");
		stmt.executeUpdate("INSERT INTO C_TRANSACTION "
				+ "VALUES (14, 10000.0, 'DM', 900, 'OUNCE', 880707); ");

		// TEST:0402 Computed GROUP BY view over referencing tables!

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM C_TRANSACTION WHERE COMMOD_NO = 17; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0402 If count = 2?

		rs = stmt
				.executeQuery("SELECT UNIT_PRICE, FROM_DATE, TO_DATE, COMMODITY "
						+ " FROM DOLLARS_PER_POUND "
						+ "ORDER BY COMMODITY DESC; ");
		rs.next();
		assertTrue((rs.getDouble(1) > .06) && (rs.getDouble(1) < .07));
		assertEquals(871012, rs.getInt(2));
		assertEquals(871212, rs.getInt(3));
		// PASS:0402 If the first row has the following values?
		// PASS:0402 If UNIT_PRICE is between 0.06 and 0.07?
		// PASS:0402 If FROM_DATE = 871012 and TO_DATE = 871212?

		// END TEST >>> 0402 <<< END TEST
		// *************************************************************

		// TEST:0403 View on computed GROUP BY view with join!
		// NOTE: OPTIONAL test

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM COST_PER_UNIT;");
		rs.next();
		assertEquals(24, rs.getInt(1));
		// PASS:0403 If count = 24?
		rs = stmt
				.executeQuery("SELECT CURRENCY, MEASURE, UNIT_PRICE, COMMODITY "
						+ "FROM COST_PER_UNIT;");
		while (rs.next()) {
			if (rs.getString(1).trim().equals("DM")
					&& rs.getString(2).trim().equals("KILO")
					&& rs.getString(3).trim().equals("Alfalfa")) {
				assertTrue((rs.getDouble(4) >= 2.42)
						&& (rs.getDouble(4) <= 2.47));
				// PASS:0403 If for values CURRENCY = 'DM' and?
				// PASS:0403 MEASURE = 'KILO' and COMMODITY = 'Alfalfa'?
				// PASS:0403 UNIT_PRICE is between 2.42 and 2.47?

			}
		}
		// END TEST >>> 0403 <<< END TEST

		// *************************************************************
		// TEST:0413 Computed SELECT on computed VIEW!
		rs = stmt
				.executeQuery("SELECT (100 + 7) * UNIT_PRICE * 700 / 100, COMMODITY "
						+ "FROM DOLLARS_PER_POUND " + "ORDER BY COMMODITY;");
		rs.next();
		assertTrue((rs.getDouble(1) >= 374.4) && (rs.getDouble(1) <= 374.6));
		assertEquals("Alfalfa", rs.getString(2).trim());

		// PASS:0413 If the first row has the following values?
		// PASS:0413 If Answer is between 374.4 and 374.6?
		// PASS:0413 If COMMODITY = 'Alfalfa'?

		// END TEST >>> 0413 <<< END TEST

	}

	/*
	 * Name: testCdr_026
	 * 
	 * Notes: tests for partial-null foreign key inserts, updates, deletes.
	 *  
	 */
	public void testCdr_026() throws SQLException {
		// TEST:0438 (partial-NULL F.K.) F.K. INSERT supported!
		SunTab.setupSunTab3(stmt);
		SunTab.refreshSunTab3(stmt);

		// Making sure the table is empty
		stmt.executeUpdate("DELETE FROM EXPERIENCE "
				+ "WHERE DESCR = 'Car Mechanic'; ");
		// Various combinations of partial-NULL F.K.
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('Tom',NULL,NULL,'Car Mechanic');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('Yolanda',NULL,NULL,'Car Mechanic');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES(NULL,112156,NULL,'Car Mechanic');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES(NULL,062068,NULL,'Car Mechanic');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES(NULL,NULL,NULL,'Car Mechanic');"));
		// Fully matching F.K.
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('Lilian',112156,NULL,'Car Mechanic');"));

		// Partial mis-match F.K.
		try {
			stmt.executeUpdate("INSERT INTO EXPERIENCE "
					+ "VALUES('Tom',052744,NULL,'Car Mechanic');");
			fail();
			// PASS:0438 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		// Partial mis-match F.K.
		try {
			stmt.executeUpdate("INSERT INTO EXPERIENCE "
					+ "VALUES('Yolanda',040523,NULL,'Car Mechanic');");
			fail();
			// PASS:0438 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		// Full mis-match F.K.
		try {
			stmt.executeUpdate("INSERT INTO EXPERIENCE "
					+ "VALUES('Yolanda',062968,NULL,'Car Mechanic');");
			fail();
			// PASS:0438 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT EXP_NAME, BTH_DATE "
				+ "FROM EXPERIENCE "
				+ "WHERE EXP_NAME IS NOT NULL AND BTH_DATE IS NOT NULL "
				+ "AND DESCR = 'Car Mechanic'; ");
		rs.next();
		assertEquals("Lilian", rs.getString(1).trim());
		assertEquals(112156, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0438 If 1 row is seleced with values?
		// PASS:0438 EXP_NAME = 'Lilian' and BTH_DATE = 112156?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM EXPERIENCE "
				+ "WHERE DESCR = 'Car Mechanic';");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:0438 If count = 6?
		// END TEST >>> 0438 <<< END TEST
		// *************************************************************

		// TEST:0439 (partial-NULL F.K.) F.K. UPDATE supported!
		SunTab.refreshSunTab3(stmt);
		// setup
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('Lilian',NULL,NULL,'Soccer Player');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('David',NULL,NULL,'Monk');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES(NULL,NULL,NULL,'Fireman');"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES(NULL,NULL,NULL,'Artist');"));

		try {
			stmt.executeUpdate("UPDATE EXPERIENCE " + "SET BTH_DATE = 040523 "
					+ "WHERE EXP_NAME = 'Lilian' AND DESCR = 'Soccer Player';");
			fail();
			// PASS:0439 If RI ERROR, parent missing, 0 rows updated?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("UPDATE EXPERIENCE "
				+ "SET EXP_NAME = NULL " + "WHERE DESCR = 'Photographer';"));
		// PASS:0439 If 1 row is updated?

		assertEquals(1, stmt.executeUpdate("UPDATE EXPERIENCE "
				+ "SET EXP_NAME = NULL ,BTH_DATE = NULL "
				+ "WHERE DESCR = 'Fashion Model'; "));
		// PASS:0439 If 1 row is updated?

		assertEquals(1, stmt.executeUpdate("UPDATE EXPERIENCE "
				+ "SET BTH_DATE = 101024 "
				+ "WHERE EXP_NAME = 'David' AND DESCR = 'Monk'; "));
		// PASS:0439 If 1 row is updated?

		assertEquals(1, stmt.executeUpdate("UPDATE EXPERIENCE "
				+ "SET EXP_NAME = 'Mary', BTH_DATE = 121245 "
				+ "WHERE DESCR = 'Fireman'; "));
		// PASS:0439 If 1 row is updated?

		try {
			stmt.executeUpdate("UPDATE EXPERIENCE "
					+ "SET EXP_NAME = 'Dick' , BTH_DATE = 020454 "
					+ "WHERE DESCR = 'Artist'; ");
			fail();
		}
		// PASS:0439 If RI ERROR, parent missing, 0 rows updated?
		catch (SQLException sqle) {
		}
		rs = stmt.executeQuery("SELECT EXP_NAME, DESCR, BTH_DATE "
				+ "FROM EXPERIENCE " + "ORDER BY EXP_NAME, BTH_DATE; ");
		int rowCount = 0;
		while (rs.next())
			rowCount++;
		assertEquals(17, rowCount);
		// PASS:0439 If 17 rows are selected?
		rs = stmt.executeQuery("SELECT EXP_NAME, DESCR, BTH_DATE "
				+ "FROM EXPERIENCE " + "ORDER BY EXP_NAME, BTH_DATE; ");
		
		
		java.sql.DatabaseMetaData dbmd = conn.getMetaData();
		System.out.println ("low"+dbmd.nullsAreSortedLow()); 
		if (dbmd.nullsAreSortedAtEnd()) {
			rs.next();
			assertEquals("David", rs.getString(1).trim());
			assertEquals("Monk", rs.getString(2).trim());
			assertEquals(101024, rs.getInt(3));
			rs.next();
			rs.next();
			rs.next();
			rs.next();
			rs.next();
			rs.next();
			assertEquals("Lilian", rs.getString(1).trim());
			assertTrue((rs.getInt(3) == 0) || (rs.getInt(3) == 112156));
			rs.next();
			assertEquals("Lilian", rs.getString(1).trim());
			assertTrue((rs.getInt(3) == 0) || (rs.getInt(3) == 112156));
			rs.next();
			assertEquals("Lilian", rs.getString(1).trim());
			assertTrue((rs.getInt(3) == 0) || (rs.getInt(3) == 112156));
			rs.next();
			assertEquals("Mary", rs.getString(1).trim());
			assertEquals(121245, rs.getInt(3));
			rs.next(); // position on Peter
			rs.next(); // position on tom
			rs.next(); // position on tom (2)
			rs.next(); // position on tom (3)
			rs.next(); // position on null, photographer, 20434
			assertEquals(null, rs.getString(1));
			rs.next(); // position on null, fashion model, null
			assertEquals(null, rs.getString(1));
			assertEquals(0, rs.getInt(3));
			rs.next(); // position on null, artist, null
			assertEquals(null, rs.getString(1));
		} else if (dbmd.nullsAreSortedLow()) {
			rs.next();
			// fashion model
			rs.next();
			// artist
			rs.next();
			//photographer
			rs.next();
			// david, monk, 101024
			assertEquals("David", rs.getString(1).trim());
			// next assert is indeterminate
			// assertEquals("Monk", rs.getString(2).trim());
			assertEquals(101024, rs.getInt(3));
			rs.next(); 
			// david, porter, 10124
			rs.next();
			// david, farmer, 10124
			rs.next();
			// john, actor, 30542
			rs.next();
			// joseph, teacher
			rs.next();
			// joseph, sportsman
			rs.next();
			//lilian, soccer player, null
			assertEquals("Lilian", rs.getString(1).trim());
			assertTrue((rs.getInt(3) == 0) || (rs.getInt(3) == 112156));
			rs.next();
			// lilian, nurse, 112156
			assertEquals("Lilian", rs.getString(1).trim());
			assertTrue((rs.getInt(3) == 0) || (rs.getInt(3) == 112156));
			rs.next();
			// lilian, baby sitter, 112156
			assertEquals("Lilian", rs.getString(1).trim());
			assertTrue((rs.getInt(3) == 0) || (rs.getInt(3) == 112156));
			rs.next();
			// mary
			assertEquals("Mary", rs.getString(1).trim());
			assertEquals(121245, rs.getInt(3));
			rs.next();
			// peter
			rs.next();
			// tom
			rs.next();
			// tom
			rs.next();
			// tom ();
			assertFalse(rs.next());

		}

		// PASS:0439 If 'David' the 'Monk' has BTH_DATE = 101024 ?
		// PASS:0439 If 3 rows with 'Lilian' have BTH_DATE = 112156 or NULL?
		// PASS:0439 If 'Mary' is a 'Fireman' with BTH_DATE = 121245?
		// PASS:0439 If EXP_NAME is NULL for 'Photographer'?
		// PASS:0439 If EXP_NAME and BTH_DATE are NULL for 'Fashion Model'?
		// PASS:0439 If EXP_NAME is NULL for 'Artist'?
		// END TEST >>> 0439 <<< END TEST

		// *************************************************************
		// TEST:0440 (partial-NULL F.K.) no restrict P.K. update/delete!

		// setup
		stmt.executeUpdate("DELETE FROM EXPERIENCE "
				+ "WHERE EXP_NAME = 'Joseph' OR EXP_NAME = 'John'; ");
		stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('John',NULL,NULL,'Gardener');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE "
				+ "VALUES('Joseph',NULL,NULL,'Snake Charmer');");

		// Delete only parent partially matching partial-NULL F.K.
		assertEquals(1, stmt.executeUpdate("DELETE FROM EMP "
				+ "WHERE ENAME = 'Joseph';"));
		// PASS:0440 If 1 row is deleted?

		// Update only parent partially matching partial-NULL F.K.
		assertEquals(1, stmt.executeUpdate("UPDATE EMP "
				+ "SET ENAME = 'Joan' " + "WHERE EDESC = 'Fraction'; "));
		// PASS:0440 If 1 row is updated?

		rs = stmt.executeQuery("SELECT ENAME " + "FROM EMP "
				+ "WHERE DNAME = 'Education'; ");
		assertFalse(rs.next());
		// PASS:0440 If 0 rows are selected?

		rs = stmt.executeQuery("SELECT DNAME " + "FROM EMP "
				+ "WHERE ENAME = 'Joan'; ");
		rs.next();
		assertEquals("Physics", rs.getString(1).trim());
		// PASS:0440 If 1 row is selected with value DNAME = 'Physics'?
		// END TEST >>> 0440 <<< END TEST

	}

	/*
	 * Name: testCdr_028
	 * 
	 * Notes:
	 * 
	 * TODO: revisit this once we have permissions working properly
	 *  
	 */
	public void testCdr_028() throws SQLException {
		stmt.executeUpdate("CREATE TABLE COMMODITY "
				+ "(C_NUM INTEGER NOT NULL, "
				+ "C_NAME CHAR(7) NOT NULL UNIQUE, " + "PRIMARY KEY (C_NUM));");
		stmt.executeUpdate("CREATE TABLE RASTER " + "(C1 INT NOT NULL, "
				+ "FOREIGN KEY (C1) " + "REFERENCES COMMODITY (C_NUM));");
		stmt.executeUpdate("CREATE TABLE REFRESH " + "(C1 CHAR (7), "
				+ "FOREIGN KEY (C1) " + "REFERENCES COMMODITY (C_NAME)); ");

		stmt.executeUpdate("INSERT INTO COMMODITY VALUES (17, 'Wheat'); ");
		stmt.executeUpdate("INSERT INTO COMMODITY VALUES (14, 'Saffron'); ");
		stmt.executeUpdate("INSERT INTO COMMODITY VALUES (23, 'Alfalfa'); ");
		// TEST:0486 Priv.violation: illegal REFERENCES!

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM RASTER;");
		rs.next();
		assertTrue(rs.getInt(1) >= 0);
		// PASS:0486 If count >= 0, no ERROR message ?

		// setup, note ERROR message or successful COMPLETION
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM REFRESH;");

		try {
			stmt.execute("INSERT INTO REFRESH " + "VALUES ('cabbage');");
			fail();
		} catch (SQLException sqle) {
		}
		// PASS:0486 Consider BOTH current INSERT AND previous SELECT COUNT?
		// PASS:0486 ( If ERROR, COUNT not selected ?
		// PASS:0486 AND 0 rows inserted in current INSERT) ?
		// PASS:0486 OR ?
		// PASS:0486 ( successful COMPLETION, count >= 0 ?
		// PASS:0486 AND 1 row inserted in current INSERT) ?

		// END TEST >>> 0486 <<< END TEST
	}

	/*
	 * Name: testCdr_031
	 * 
	 * Notes:
	 *  
	 */
	public void testCdr_031() throws SQLException {
		// TEST:0526 FIPS sizing: Length of FOREIGN KEY column list = 120!

		// NOTE:0526 The last string has been shortened to fit into 80 columns.
		// NOTE:0526 This test will be somewhat less effective as a result.
		stmt.executeUpdate("CREATE TABLE T6118REF ( "
				+ "COL1 CHAR(20) NOT NULL, COL2 CHAR(20) NOT NULL, "
				+ "COL3 CHAR(20) NOT NULL, COL4 CHAR(20) NOT NULL, "
				+ "COL5 CHAR(23) NOT NULL, COL6 NUMERIC (4) NOT NULL, "
				+ "STR118 CHAR(118) NOT NULL UNIQUE, "
				+ "UNIQUE (COL1, COL2, COL4, COL3, COL5, COL6));");

		assertEquals(
				1,
				stmt
						.executeUpdate("INSERT INTO T6118REF VALUES "
								+ "('AAAAAAAAAAAAAAAAAAAA', 'BBBBBBBBBBBBBBBBBBBB', "
								+ "'CCCCCCCCCCCCCCCCCCCC', 'DDDDDDDDDDDDDDDDDDDD', "
								+ "'EEEEEEEEEEEEEEEEEEEEEEE', 9999, "
								+ "'This test is trying to test the limit on the total length of');"));
		// PASS:0526 If 1 row is inserted?

		stmt.executeUpdate("CREATE TABLE T6 (COL1 CHAR(20), COL2 CHAR(20), "
				+ "COL3 CHAR(20), COL4 CHAR(20), "
				+ "COL5 CHAR(23), COL6 NUMERIC (4), "
				+ "FOREIGN KEY (COL1, COL2, COL4, COL3, COL5, COL6) "
				+ "REFERENCES T6118REF (COL1, COL2, COL4, COL3, COL5, COL6));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO T6 VALUES "
				+ "('AAAAAAAAAAAAAAAAAAAA', 'BBBBBBBBBBBBBBBBBBBB', "
				+ "'CCCCCCCCCCCCCCCCCCCC', 'DDDDDDDDDDDDDDDDDDDD', "
				+ "'EEEEEEEEEEEEEEEEEEEEEEE', 9999);"));
		// PASS:0526 If 1 row is inserted?

		stmt
				.executeUpdate("CREATE TABLE T118(STR118 CHAR(118) NOT NULL UNIQUE, "
						+ "FOREIGN KEY (STR118) REFERENCES T6118REF (STR118)); ");

		assertEquals(
				1,
				stmt
						.executeUpdate("INSERT INTO T118 VALUES ( "
								+ "'This test is trying to test the limit on the total length of');"));
		// PASS:0526 If 1 row is inserted?

		try {
			stmt.executeUpdate("INSERT INTO T6 VALUES "
					+ "('AAAAAAAAAAAAAAAAAAAA', 'BBBBBBBBBBBBBBBBBBBB', "
					+ "'CCCCCCCCCCCCCCCCCCCC', 'DDDDDDDDDDDDDDDDDDDD', "
					+ "'EEEEEEEEEEEEEEEEEEEEEEE', 0);");

			fail();
			// PASS:0526 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO T118 VALUES ('Hamlet');");
			fail();
			// PASS:0526 If RI ERROR, parent missing, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		// END TEST >>> 0526 <<< END TEST
	}
}