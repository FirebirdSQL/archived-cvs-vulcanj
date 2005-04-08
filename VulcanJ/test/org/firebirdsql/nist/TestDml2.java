/* $Id$ */
/*
 * Author: sasjwm
 * Created on: Aug 26, 2004
 * 
 * This is the 2nd part of the DML series. It was broken into pieces 
 * for convenience in coding.
 * 
 */
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * @author sasjwm, Aug 26, 2004
 *  
 */
public class TestDml2 extends NistTestBase {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private String query;

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

	public TestDml2(String arg0) {
		super(arg0);
	}

	/*
	 * 
	 * firebird does support the read-only attribute on the JDBC driver.
	 *  
	 */
	public void testDml_100() throws SQLException {
		// Change connection to READ ONLY
		conn.setReadOnly(true);

		try {
			assertEquals(0, stmt
					.executeUpdate("CREATE TABLE SLACK (SLACK_FACTOR FLOAT) "));
			fail("Attempted update during read-only transaction");
		} catch (SQLException sqle) {
		}

		conn.setReadOnly(false);
		assertEquals(0, stmt
				.executeUpdate("CREATE TABLE SLACK (SLACK_FACTOR FLOAT) "));

		conn.setReadOnly(true);
		try {
			assertEquals(0, stmt
					.executeUpdate("INSERT INTO SLACK VALUES (2.4)"));
			fail("Attempted update during read-only transaction");
		} catch (SQLException sqle) {
		}

		conn.setReadOnly(false);
		assertEquals(1, stmt.executeUpdate("INSERT INTO SLACK VALUES (2.4)"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO SLACK VALUES (2.0)"));

		conn.setReadOnly(true);
		try {
			assertEquals(0, stmt
					.executeUpdate("UPDATE SLACK SET SLACK_FACTOR = 5.0 "
							+ "WHERE SLACK_FACTOR < 5.0 ;"));
			fail("Attempted update during read-only transaction");
		} catch (SQLException sqle) {
		}

		conn.setReadOnly(false);
		assertEquals(2, stmt
				.executeUpdate("UPDATE SLACK SET SLACK_FACTOR = 5.0 "
						+ "WHERE SLACK_FACTOR < 5.0 ;"));

		conn.setReadOnly(true);
		try {
			stmt.executeUpdate("DELETE FROM SLACK "
					+ "WHERE SLACK_FACTOR < 6.0 ;");
			fail();
		} catch (SQLException sqle) {
		}

		conn.setReadOnly(false);
		assertEquals(2, stmt.executeUpdate("DELETE FROM SLACK "
				+ "WHERE SLACK_FACTOR < 6.0 ;"));

		assertEquals(1, stmt.executeUpdate("INSERT INTO SLACK VALUES (2.4) ;"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO SLACK VALUES (2.0) ;"));

	}

	/*
	 * @author sasjwm
	 * 
	 * FirebirdSQL doesn't support natural join, so there is nothing to do in
	 * this test care for FirebirdSQL.
	 *  
	 */
	public void testDml_104() throws SQLException {

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		BaseTab.setupBaseTab(stmt);

		// TEST:0591 NATURAL JOIN (feature 4) (static)!

		stmt.executeUpdate("CREATE TABLE GROUPS1 " + "(EMPNUM INT, GRP INT); ");
		stmt.executeUpdate("CREATE TABLE NAMES1 "
				+ "(EMPNUM INT, NAME CHAR(5));");

		stmt.executeUpdate("CREATE VIEW NAMGRP1 AS "
				+ "SELECT * FROM NAMES1 NATURAL JOIN GROUPS1; ");
		// PASS:0591 If view is created?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO GROUPS1 VALUES (0, 10);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO GROUPS1 VALUES (1, 20);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO GROUPS1 VALUES (2, 30);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO GROUPS1 VALUES (3, 40);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO NAMES1 VALUES (5, 'HARRY');"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO NAMES1 VALUES (1, 'MARY');"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO NAMES1 VALUES (7, 'LARRY');"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO NAMES1 VALUES (0, 'KERI');"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO NAMES1 VALUES (9, 'BARRY');"));

		rs = stmt.executeQuery("SELECT EMPNUM " + "FROM NAMGRP1 "
				+ "WHERE NAME = 'KERI' " + "AND GRP = 10;");
		rs.next();

		assertEquals(0, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0591 If 1 row is selected and EMPNUM = 0?

		rs = stmt.executeQuery("SELECT EMPNUM " + "FROM NAMGRP1 "
				+ "WHERE NAME = 'MARY' " + "AND GRP = 20;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0591 If 1 row is selected and EMPNUM = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM NAMGRP1 "
				+ "WHERE (NAME <> 'MARY' " + "AND NAME <> 'KERI') "
				+ "OR GRP <> 20 " + "AND GRP <> 10 " + "OR EMPNUM <> 0 "
				+ "AND EMPNUM <> 1 " + "OR NAME IS NULL " + "OR GRP IS NULL "
				+ "OR EMPNUM IS NULL; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0591 If count = 0?

	}

	/*
	 * 
	 * Dml_106
	 *  
	 */
	public void testDml_106() throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		assertEquals(1, stmt.executeUpdate("INSERT INTO USIG VALUES (0,2);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO USIG VALUES (1,3);"));

		// TEST:0599 UNION in views (feature 8) (static)!
		stmt.executeUpdate("CREATE VIEW UUSIG (U1) AS "
				+ "SELECT C1 FROM USIG UNION SELECT C_1 FROM USIG; ");
		// PASS:0599 If view is created?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM UUSIG;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0599 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(DISTINCT U1) FROM UUSIG;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0599 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM UUSIG WHERE U1 < 0 "
				+ "OR U1 > 3 OR U1 IS NULL;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0599 If count = 0?

		stmt.executeUpdate("CREATE VIEW ABOVE_AVERAGE ("
				+ "COLUMN_1, COLUMN_2, COLUMN_3) "
				+ "AS SELECT PNUM, BUDGET, CITY " + "FROM PROJ OUTER_REF "
				+ "WHERE BUDGET >= (SELECT AVG(BUDGET) FROM PROJ INNER_REF "
				+ "WHERE OUTER_REF.CITY = INNER_REF.CITY) " + "UNION "
				+ "SELECT 'MAX', MAX(BUDGET), MIN(CITY) " + "FROM PROJ  "
				+ "WHERE CITY > 'Deale'; ");
		// PASS:0599 If view is created?

		rs = stmt
				.executeQuery("SELECT * FROM ABOVE_AVERAGE ORDER BY COLUMN_1;");

		rowCount = 0;
		while (rs.next()) {
			switch (rowCount) {
				case 0 :
					assertEquals("MAX", rs.getString(1).trim());
					assertEquals(30000, rs.getInt(2));
					assertEquals("Tampa", rs.getString(3).trim());
					break;
				case 1 :
					assertEquals("P2", rs.getString(1).trim());
					assertEquals(30000, rs.getInt(2));
					assertEquals("Vienna", rs.getString(3).trim());
					break;
				case 2 :
					assertEquals("P3", rs.getString(1).trim());
					assertEquals(30000, rs.getInt(2));
					assertEquals("Tampa", rs.getString(3).trim());
					break;
				case 3 :
					assertEquals("P6", rs.getString(1).trim());
					assertEquals(50000, rs.getInt(2));
					assertEquals("Deale", rs.getString(3).trim());
					break;
				default :
					System.out.println("We should not be here.");
			}
			rowCount++;
		}
		assertEquals(4, rowCount);
		// PASS:0599 If 4 rows selected with ordered rows and column values: ?
		// assertEquals("MAX",rs.getString(1));
		// PASS:0599 MAX 30000 Tampa ?
		// PASS:0599 P2 30000 Vienna ?
		// PASS:0599 P3 30000 Tampa ?
		// PASS:0599 P6 50000 Deale ?

		// no UNION ALL in View!! Skip for Firebirdsql
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			query = "CREATE VIEW STAFF_DUP AS "
					+ "SELECT EMPNUM, EMPNAME, GRADE, CITY " + "FROM STAFF "
					+ "UNION ALL SELECT * FROM STAFF3;";

			assertEquals(1, stmt.executeUpdate(query));
			// PASS:0599 If view is created?

			rowCount = 0;
			rs = stmt.executeQuery("SELECT * FROM STAFF_DUP ORDER BY CITY;");
			while (rs.next()) {
				switch (rowCount) {
					case 0 :
						assertEquals("E5", rs.getString(1).trim());
						assertEquals("Akron", rs.getString(2).trim());
						break;
					case 1 :
						assertEquals("E5", rs.getString(1).trim());
						assertEquals("Akron", rs.getString(2).trim());
						break;
				}
				rowCount++;
			}
			assertEquals(10, rowCount);
			// PASS:0599 If 10 rows selected ?
			// PASS:0599 If first row contains EMPNUM/CITY values E5 / Akron ?
			// PASS:0599 If second row contains EMPNUM/CITY values E5 / Akron ?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF_DUP;");
			rs.next();
			assertEquals(10, rs.getInt(1));
			// PASS:0599 If count = 10 ?

		}
		stmt
				.executeUpdate("CREATE VIEW FOUR_CITIES (C1, C2, C3) AS "
						+ "SELECT 'P', CITY, 666 " + "  FROM PROJ "
						+ " WHERE BUDGET <> 30000 " + "UNION "
						+ "SELECT 'S', CITY, 777 " + " FROM STAFF "
						+ " WHERE EMPNAME <> 'Ed' " + "UNION "
						+ "SELECT 'T', CITY, -999 " + "  FROM STAFF3 "
						+ "  WHERE CITY NOT LIKE 'V%' " + "UNION "
						+ "SELECT 'X', CITY, -1 " + " FROM STAFF3 "
						+ " WHERE CITY = 'Vienna'; ");
		// PASS:0599 If view is created?

		rs = stmt
				.executeQuery("SELECT C2, C1, C3 FROM FOUR_CITIES ORDER BY C3, C2;");
		rowCount = 0;
		while (rs.next()) {
			switch (rowCount) {
				case 0 :
					assertEquals("Akron", rs.getString(1).trim());
					assertEquals("T", rs.getString(2).trim());
					assertEquals(-999, rs.getInt(3));
					break;
				case 1 :
					assertEquals("Deale", rs.getString(1).trim());
					assertEquals("T", rs.getString(2).trim());
					assertEquals(-999, rs.getInt(3));
					break;
				case 2 :
					assertEquals("Vienna", rs.getString(1).trim());
					assertEquals("X", rs.getString(2).trim());
					assertEquals(-1, rs.getInt(3));
					break;
				case 3 :
					assertEquals("Deale", rs.getString(1).trim());
					assertEquals("P", rs.getString(2).trim());
					assertEquals(666, rs.getInt(3));
					break;
				case 4 :
					assertEquals("Vienna", rs.getString(1).trim());
					assertEquals("P", rs.getString(2).trim());
					assertEquals(666, rs.getInt(3));
					break;
				case 5 :
					assertEquals("Deale", rs.getString(1).trim());
					assertEquals("S", rs.getString(2).trim());
					assertEquals(777, rs.getInt(3));
					break;
				case 6 :
					assertEquals("Vienna", rs.getString(1).trim());
					assertEquals("S", rs.getString(2).trim());
					assertEquals(777, rs.getInt(3));
					break;

			}
			rowCount++;
		}
		assertEquals(7, rowCount);

		// PASS:0599 If 7 rows selected with ordered rows and column values ?
		// PASS:0599 Akron T -999 ?
		// PASS:0599 Deale T -999 ?
		// PASS:0599 Vienna X -1 ?
		// PASS:0599 Deale P 666 ?
		// PASS:0599 Vienna P 666 ?
		// PASS:0599 Deale S 777 ?
		// PASS:0599 Vienna S 777 ?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM FOUR_CITIES;");
		rs.next();
		assertEquals(7, rs.getInt(1));
		// PASS:0599 If count = 7 ?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM FOUR_CITIES WHERE C3 > 0; ");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0599 If count = 4 ?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM FOUR_CITIES WHERE C2 = 'Vienna';");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0599 If count = 3 ?
		// END TEST >>> 0599 <<< END TEST

		// *********************************************
		// TEST:0601 DATETIME data types (feature 5) (static)!

		// Note: INTERVAL keyword not supported by Firebird.
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE TABLE TEMPUS (TDATE DATE, TTIME TIME, "
					+ "TTIMESTAMP TIMESTAMP, TINT1 INTERVAL YEAR TO MONTH, "
					+ "TINT2 INTERVAL DAY TO SECOND);");
			// PASS:0601 If table is created?

			assertEquals(1, stmt.executeUpdate("INSERT INTO TEMPUS VALUES ( "
					+ "DATE '1993-08-24', " + "TIME '16:03:00', "
					+ "TIMESTAMP '1993-08-24 16:03:00', "
					+ "INTERVAL -'1-6' YEAR TO MONTH, "
					+ "INTERVAL '13 0:10' DAY TO SECOND); "));
			// PASS:0601 If 1 row is inserted?

			rs = stmt.executeQuery("SELECT EXTRACT (DAY FROM TDATE) "
					+ "FROM TEMPUS;");
			assertTrue(rs.next());
			assertEquals(24, rs.getInt(1));
			// PASS:0601 If 1 row selected and value is 24?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMPUS "
					+ "WHERE (TTIMESTAMP - TIMESTAMP '1995-02-24 16:03:00') "
					+ "YEAR TO MONTH = TINT1; ");
			rs.next();
			assertEquals(1, rs.getInt(1));
			// PASS:0601 If count = 1?

			rs = stmt
					.executeQuery("SELECT COUNT(*) FROM TEMPUS "
							+ "WHERE (TTIMESTAMP, TINT1) OVERLAPS "
							+ "(TIMESTAMP '1995-02-24 16:03:00', INTERVAL '1-6' YEAR TO MONTH);");
			rs.next();
			assertEquals(0, rs.getInt(1));
			// PASS:0601 If count = 0?
			// END TEST >>> 0601 <<< END TEST
		}

		// TEST:0611 FIPS sizing, DATETIME data types (static)!
		// INTERVAL keyword not supported by Firebird.
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE TABLE TSFIPS ( " + "FIPS1 TIME, "
					+ "FIPS2 TIMESTAMP, "
					+ "FIPS3 INTERVAL YEAR (2) TO MONTH, "
					+ "FIPS4 INTERVAL DAY (2) TO SECOND (6)); ");
			// PASS:0611 If table is created?

			assertEquals(1, stmt.executeUpdate("INSERT INTO TSFIPS VALUES ( "
					+ "TIME '16:03:00', "
					+ "TIMESTAMP '1996-08-24 16:03:00.999999', "
					+ "INTERVAL -'99-6' YEAR (2) TO MONTH, "
					+ "INTERVAL '99 0:10:00.999999' DAY (2) TO SECOND (6)); "));
			// PASS:0611 If 1 row is inserted?

			rs = stmt.executeQuery("SELECT EXTRACT (SECOND FROM FIPS2) "
					+ "* 1000000 - 999990 FROM TSFIPS;");
			assertTrue(rs.next());
			assertEquals(9, rs.getInt(1));
			assertFalse(rs.next());
			// PASS:0611 If 1 row selected and value is 9?

			rs = stmt.executeQuery("SELECT EXTRACT (YEAR FROM FIPS3), "
					+ "EXTRACT (MONTH FROM FIPS3) " + "FROM TSFIPS;");
			assertTrue(rs.next());
			assertEquals(-99, rs.getInt(1));
			assertEquals(-6, rs.getInt(2));
			assertFalse(rs.next());
			// PASS:0611 If 1 row selected and values are -99 and -6?

			rs = stmt.executeQuery("SELECT EXTRACT (DAY FROM FIPS4), "
					+ "EXTRACT (SECOND FROM FIPS4) * 1000000 - 999990 "
					+ "FROM TSFIPS;");
			assertTrue(rs.next());
			assertEquals(99, rs.getInt(1));
			assertEquals(9, rs.getInt(2));
			assertFalse(rs.next());
			// PASS:0611 If 1 row selected and values are 99 and 9?
			// END TEST >>> 0611 <<< END TEST
		}

		// TEST:0613 <datetime value function> (static)!
		stmt.executeUpdate("CREATE TABLE TSSMALL ( " + "SMALLD DATE, "
				+ "SMALLT TIME, " + "SMALLTS TIMESTAMP);");
		assertEquals(1, stmt.executeUpdate("INSERT INTO TSSMALL VALUES ( "
				+ "CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP); "));

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TSSMALL WHERE "
				+ "EXTRACT (YEAR FROM SMALLD) = EXTRACT (YEAR FROM SMALLTS);");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0613 If count = 1?

		rs = stmt
				.executeQuery("SELECT COUNT(*) "
						+ "FROM TSSMALL WHERE "
						+ "EXTRACT (MONTH FROM SMALLD) = EXTRACT (MONTH FROM SMALLTS); ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0613 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TSSMALL WHERE "
				+ "EXTRACT (DAY FROM SMALLD) = EXTRACT (DAY FROM SMALLTS); ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0613 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TSSMALL WHERE "
				+ "EXTRACT (HOUR FROM SMALLT) = EXTRACT (HOUR FROM SMALLTS); ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0613 If count = 1?

		rs = stmt
				.executeQuery("SELECT COUNT(*) "
						+ "FROM TSSMALL WHERE "
						+ "EXTRACT (MINUTE FROM SMALLT) = EXTRACT (MINUTE FROM SMALLTS); ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0613 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TSSMALL WHERE "
				+ "EXTRACT (SECOND FROM SMALLT) - "
				+ "EXTRACT (SECOND FROM SMALLTS) > -1 "
				+ "AND EXTRACT (SECOND FROM SMALLT) - "
				+ "EXTRACT (SECOND FROM SMALLTS) < 1; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0613 If count = 1?
		// END TEST >>> 0613 <<< END TEST */

		// TEST:0615 DATETIME-related SQLSTATE codes (static)!
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE TABLE TSERR ( "
					+ "BADINT INTERVAL YEAR (2) TO MONTH, " + "BADDATE DATE);");
			try {
				stmt
						.executeUpdate("INSERT INTO TSERR VALUES ( "
								+ "INTERVAL '0-11' YEAR TO MONTH, "
								+ "DATE '9999-01-01' + INTERVAL '1-00' YEAR TO MONTH);");
				fail("Datetime field overflow");
				// PASS:0615 If ERROR, datetime field overflow, 0 rows inserted?
			} catch (SQLException sqle) {
			}

			try {
				stmt.executeUpdate("INSERT INTO TSERR VALUES ( "
						+ "INTERVAL '9999-11' YEAR TO MONTH, "
						+ "DATE '1984-01-01'); ");
				fail("Datetime field overflow");
				// PASS:0615 If ERROR, interval field overflow, 0 rows inserted?
			} catch (SQLException sqle) {
			}

			try {
				stmt.executeUpdate("INSERT INTO TSERR VALUES ( "
						+ "INTERVAL '1-11' YEAR TO MONTH, "
						+ "CAST ('DATE ''1993-02-30''' AS DATE)); ");
				fail("Invalid datetime format");
				// PASS:0615 If ERROR, invalid datetime format, 0 rows inserted?
			} catch (SQLException sqle) {
			}

			try {
				stmt.executeUpdate("INSERT INTO TSERR VALUES ( "
						+ "INTERVAL '1-11' YEAR TO MONTH, "
						+ "CAST ('1993-02-30' AS DATE)); ");
				fail("Invalid datetime format");
				// PASS:0615 If ERROR, invalid datetime format, 0 rows inserted?
			} catch (SQLException sqle) {
			}
		}
	}
	/*
	 * @author sasjwm
	 * 
	 * TEST:0617 DATETIME with predicates, set fns (static)!
	 *  
	 */
	public void testDml_108() throws SQLException {

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		int rowCount;

		stmt.executeUpdate("CREATE TABLE TEMPS ( " + "ENTERED  TIMESTAMP, "
				+ "START    DATE, " + "APPT     INTERVAL DAY, "
				+ "HOUR_IN  TIME, " + "HOUR_OUT TIME, "
				+ "LUNCH    INTERVAL HOUR TO MINUTE); ");
		// PASS:0617 If table is created?

		stmt.executeUpdate("CREATE VIEW SUBQ1 AS "
				+ "SELECT MIN (HOUR_IN) AS TOO_EARLY, "
				+ "MAX (ALL START) AS LATEST, " + "AVG (LUNCH) AS AVGLUNCH, "
				+ "AVG (DISTINCT LUNCH) AS D_AVGLUNCH, "
				+ "SUM (APPT) AS SUMAPPT " + "FROM TEMPS; ");
		// PASS:0617 If view is created?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TEMPS VALUES ( "
				+ "TIMESTAMP '1993-11-10 12:25:14', " + "DATE '1993-11-12', "
				+ "INTERVAL '4' DAY, " + "TIME '08:30:00', "
				+ "TIME '16:30:00', " + "INTERVAL '1:00' HOUR TO MINUTE); "));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TEMPS VALUES ( "
				+ "TIMESTAMP '1993-11-10 13:15:14', " + "DATE '1993-11-15', "
				+ "INTERVAL '5' DAY, " + "TIME '08:30:00', "
				+ "TIME '17:30:00', " + "INTERVAL '0:30' HOUR TO MINUTE); "));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TEMPS VALUES ( "
				+ "TIMESTAMP '1993-11-17 09:56:48', " + "DATE '1994-11-18', "
				+ "INTERVAL '3' DAY, " + "TIME '09:00:00', "
				+ "TIME '17:00:00', " + "INTERVAL '1:00' HOUR TO MINUTE); "));

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "LUNCH < INTERVAL '1:00' HOUR TO MINUTE; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0617 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "LUNCH <= INTERVAL '1:00' HOUR TO MINUTE; ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0617 If count = 3?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "START <> DATE '1993-11-15' AND "
				+ "START <> DATE '1993-11-12';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0617 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "START = DATE '1993-11-15' OR "
				+ "START = DATE '1993-11-12'; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "HOUR_OUT > TIME '17:00:00'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0617 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "HOUR_OUT >= TIME '17:00:00'; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "ENTERED BETWEEN TIMESTAMP '1993-11-10 00:00:00' AND "
				+ "TIMESTAMP '1993-11-10 23:59:59'; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "HOUR_OUT IN "
				+ "(SELECT HOUR_IN + INTERVAL '8' HOUR FROM TEMPS); ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "(START, APPT) OVERLAPS "
				+ "(DATE '1993-11-14', INTERVAL '2' DAY); ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "HOUR_OUT = ANY "
				+ "(SELECT HOUR_IN + INTERVAL '8' HOUR FROM TEMPS); ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "EXTRACT (YEAR FROM ENTERED) <> SOME "
				+ "(SELECT EXTRACT (YEAR FROM START) " + "FROM TEMPS); ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0617 If count = 3?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMPS WHERE "
				+ "EXTRACT (YEAR FROM START) <> ALL "
				+ "(SELECT EXTRACT (YEAR FROM ENTERED) " + "FROM TEMPS); ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0617 If count = 1?

		rs = stmt.executeQuery("SELECT EXTRACT (HOUR FROM TOO_EARLY) "
				+ "* 100 + EXTRACT (MINUTE FROM TOO_EARLY) " + "FROM SUBQ1; ");
		assertTrue(rs.next());
		assertEquals(830, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0617 If 1 row selected and value is 830?

		rs = stmt.executeQuery("SELECT EXTRACT (YEAR FROM LATEST), "
				+ "EXTRACT (MONTH FROM LATEST) * 100 +  "
				+ "EXTRACT (DAY FROM LATEST) " + "FROM SUBQ1; ");
		assertTrue(rs.next());
		assertEquals(1994, rs.getInt(1));
		assertEquals(1118, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0617 If 1 row selected and values are 1994, 1118?

		rs = stmt.executeQuery("SELECT EXTRACT (HOUR FROM AVGLUNCH) 100 + "
				+ "EXTRACT (MINUTE FROM AVGLUNCH) FROM SUBQ1;");
		assertTrue(rs.next());
		assertTrue("Returned value is not in the acceptable range", (rs
				.getInt(1) >= 49));
		assertFalse(rs.next());
		// PASS:0617 If 1 row selected and value is 49 or 50?
		// NOTE:0617 50 is better but 49 is acceptable.

		rs = stmt.executeQuery("SELECT EXTRACT (HOUR FROM D_AVGLUNCH) 100 + "
				+ "EXTRACT (MINUTE FROM D_AVGLUNCH) FROM SUBQ1; ");
		assertTrue(rs.next());
		assertEquals(45, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0617 If 1 row selected and value is 45?

		rs = stmt
				.executeQuery("SELECT EXTRACT (DAY FROM SUMAPPT) FROM SUBQ1; ");
		assertTrue(rs.next());
		assertEquals(12, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0617 If 1 row selected and value is 12?

		rs = stmt.executeQuery("SELECT COUNT (DISTINCT LUNCH) FROM TEMPS; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0617 If count = 2?
	} /* end of dml108 */

	/*
	 * 
	 * Test: DATETIME NULLs!
	 *  
	 */

	public void testDml_112() throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);

		stmt.executeUpdate("CREATE TABLE MERCH ( "
				+ "ITEMKEY INT, " + "ORDERED DATE, " + "RDATE DATE, "
				+ "RTIME TIME, " + "SOLD TIMESTAMP); ");
		// PASS:0621 If table is created?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE TABLE TURNAROUND ( " + "ITEMKEY INT, "
					+ "MWAIT INTERVAL MONTH, "
					+ "DWAIT INTERVAL DAY TO HOUR); ");
			// PASS:0621 If table is created?

			stmt.executeUpdate("CREATE VIEW INVENTORY AS "
					+ "SELECT MERCH.ITEMKEY AS ITEMKEY, ORDERED, "
					+ "MWAIT, DWAIT FROM MERCH, TURNAROUND COR1 WHERE RDATE "
					+ "IS NOT NULL AND SOLD IS NULL AND "
					+ "MERCH.ITEMKEY = COR1.ITEMKEY " + " UNION "
					+ "SELECT ITEMKEY, ORDERED, "
					+ "CAST (NULL AS INTERVAL MONTH), "
					+ "CAST (NULL AS INTERVAL DAY TO HOUR) FROM "
					+ "MERCH WHERE RDATE IS NOT NULL AND SOLD IS NULL "
					+ "AND MERCH.ITEMKEY NOT IN (SELECT ITEMKEY "
					+ "FROM TURNAROUND); ");
			// PASS:0621 If view is created?
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO MERCH VALUES "
				+ "(0, DATE '1993-11-23', NULL, NULL, NULL);"));
		// PASS:0621 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO MERCH VALUES "
				+ "(1, DATE '1993-12-10', DATE '1994-01-03', "
				+ "CAST (NULL AS TIME), NULL); "));
		// PASS:0621 If 1 row is inserted?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals(
					1,
					stmt
							.executeUpdate("INSERT INTO MERCH VALUES "
									+ "(2, DATE '1993-12-11', NULL, NULL, "
									+ "CAST ('TIMESTAMP ''1993-12-11 13:00:00''' AS TIMESTAMP)); "));
		else
			assertEquals(1, stmt.executeUpdate("INSERT INTO MERCH VALUES "
					+ "(2, DATE '1993-12-11', NULL, NULL, "
					+ "CAST (TIMESTAMP '1993-12-11 13:00:00' AS TIMESTAMP)); "));
		// PASS:0621 If 1 row is inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO MERCH VALUES "
						+ "(4, DATE '1993-01-26', DATE '1993-01-27', "
						+ "NULL, NULL);"));
		// PASS:0621 If 1 row is inserted?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {

			assertEquals(1, stmt.executeUpdate("INSERT INTO TURNAROUND VALUES "
					+ "(2, INTERVAL '1' MONTH, "
					+ "INTERVAL '20:0' DAY TO HOUR);"));
			// PASS:0621 If 1 row is inserted?

			assertEquals(1, stmt.executeUpdate("INSERT INTO TURNAROUND VALUES "
					+ "(5, INTERVAL '5' MONTH, "
					+ "CAST (NULL AS INTERVAL DAY TO HOUR)); "));
			// PASS:0621 If 1 row is inserted?

			assertEquals(1, stmt
					.executeUpdate("INSERT INTO TURNAROUND VALUES ("
							+ "6, INTERVAL '2' MONTH, NULL);"));
			// PASS:0621 If 1 row is inserted?
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM "
				+ "MERCH A, MERCH B WHERE A.SOLD = B.SOLD;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0621 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM "
				+ "MERCH A, MERCH B WHERE A.RTIME = B.RTIME;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0621 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM "
				+ "MERCH WHERE RDATE IS NULL;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0621 If count = 2?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM "
					+ "TURNAROUND WHERE DWAIT IS NOT NULL;");
			rs.next();
			assertEquals(1, rs.getInt(1));
			// PASS:0621 If count = 1?
			rs = stmt
					.executeQuery("SELECT EXTRACT (DAY FROM RDATE) "
							+ "FROM MERCH, TURNAROUND WHERE MERCH.ITEMKEY = TURNAROUND.ITEMKEY; ");
			rowCount = 0;
			while (rs.next()) {
				switch (rowCount) {
					case 0 :
						assertEquals(1, rs.getInt(1));
						break;
				}
				rowCount++;
			}
			assertEquals(1, rowCount);
			// PASS:0621 If 1 row selected and value is NULL?
		}

		rs = stmt
				.executeQuery("SELECT ITEMKEY FROM MERCH WHERE SOLD IS NOT NULL; ");
		rowCount = 0;
		while (rs.next()) {
			switch (rowCount) {
				case 0 :
					assertEquals(2, rs.getInt(1));
					break;
			}
			rowCount++;
		}
		assertEquals(1, rowCount);
		// PASS:0621 If 1 row selected and ITEMKEY is 2?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT EXTRACT (HOUR FROM AVG (DWAIT)) "
					+ "FROM MERCH, TURNAROUND WHERE "
					+ "MERCH.ITEMKEY = TURNAROUND.ITEMKEY OR "
					+ "TURNAROUND.ITEMKEY NOT IN "
					+ "(SELECT ITEMKEY FROM MERCH); ");
			rowCount = 0;
			while (rs.next()) {
				switch (rowCount) {
					case 0 :
						assertEquals(0, rs.getInt(1));
						break;
				}
				rowCount++;
			}
			assertEquals(1, rowCount);
			// PASS:0621 If 1 row selected and value is 0?
			stmt.executeUpdate("DROP TABLE TURNAROUND ;");
		}

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM INVENTORY WHERE MWAIT IS NULL "
					+ "AND DWAIT IS NULL; ");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:0621 If count = 2?
		}

		stmt.executeUpdate("DROP TABLE MERCH ;");
		// PASS:0621 If table is dropped?

		// END TEST >>> 0621 <<< END TEST

		// TEST:0623 OUTER JOINs with NULLs and empty tables!

		stmt
				.executeUpdate("CREATE TABLE JNULL1 (C1 INT, C2 INT);");
		stmt
				.executeUpdate("CREATE TABLE JNULL2 (D1 INT, D2 INT); ");
		stmt.executeUpdate("CREATE VIEW JNULL3 AS "
				+ "SELECT C1, D1, D2 FROM JNULL1 LEFT OUTER JOIN JNULL2 "
				+ "ON C2 = D2; ");

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE VIEW JNULL4 AS "
					+ "SELECT D1, D2 FROM JNULL2; ");
		else {
			// these view create statements don't work for firebird...
			stmt.executeUpdate("CREATE VIEW JNULL4 AS "
					+ "SELECT D1, D2 AS C2 FROM JNULL2; ");
			stmt.executeUpdate("CREATE VIEW JNULL5 AS "
					+ "SELECT C1, D1, C2 FROM JNULL1 RIGHT OUTER JOIN JNULL4 "
					+ "USING (C2) ; ");
			stmt.executeUpdate("CREATE VIEW JNULL6 AS "
					+ "SELECT * FROM JNULL1 LEFT OUTER JOIN JNULL4 "
					+ "USING (C2);");
		}

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO JNULL1 VALUES(NULL,NULL);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO JNULL1 VALUES (1, NULL);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO JNULL1 VALUES (NULL, 1);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO JNULL1 VALUES (1, 1);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO JNULL1 VALUES (2, 2);"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM JNULL3;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0623 If count = 5?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM JNULL3 "
				+ "WHERE D2 IS NOT NULL OR D1 IS NOT NULL; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0623 If count = 0?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM JNULL5; ");
			rs.next();
			assertEquals(0, rs.getInt(1));
			// PASS:0623 If count = 0?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM JNULL6 "
					+ "WHERE C2 IS NOT NULL; ");
			rs.next();
			assertEquals(3, rs.getInt(1));
			// PASS:0623 If count = 3?
		}

		assertEquals(5, stmt.executeUpdate("INSERT INTO JNULL2 "
				+ "SELECT * FROM JNULL1; "));
		assertEquals(1, stmt.executeUpdate("UPDATE JNULL2 "
				+ "SET D2 = 1 WHERE D2 = 2; "));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM JNULL3;");
		rs.next();
		assertEquals(9, rs.getInt(1));
		// PASS:0623 If count = 9?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM JNULL3 WHERE C1 IS NULL;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0623 If count = 4?

		rs = stmt.executeQuery("SELECT COUNT(*)  "
				+ "FROM JNULL3 WHERE D1 IS NULL; ");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0623 If count = 5?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM JNULL3 WHERE D2 IS NULL; ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		//  PASS:0623 If count = 3?

		// In Firebird avg(d1) shows up as 10 because of integer rounding
		// original test called for value of 15.
		rs = stmt.executeQuery("SELECT AVG(D1) * 10 " + "FROM JNULL3;");
		rs.next();
		assertEquals(10, rs.getInt(1));

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(*)  " + "FROM JNULL6  "
					+ "WHERE C2 = 1;");
			rs.next();
			assertEquals(5, rs.getInt(1));
			//  PASS:0623 If count = 6?

			rs = stmt.executeQuery("SELECT COUNT(*)  " + "FROM JNULL6  "
					+ "WHERE C2 IS NULL;");
			rs.next();
			assertEquals(5, rs.getInt(1));
			//  PASS:0623 If count = 2?

			rs = stmt.executeQuery("SELECT COUNT(*)  " + "FROM JNULL6 "
					+ "WHERE C2 = C1 " + "AND D1 IS NULL;");
			rs.next();
			assertEquals(5, rs.getInt(1));
			// PASS:0623 If count = 2?
		}

		// END TEST >>> 0623 <<< END TEST
		// *********************************************
		// TEST:0625 ADD COLUMN and DROP COLUMN!
		stmt.executeUpdate("CREATE TABLE CHANGG "
				+ "(NAAM CHAR (14) NOT NULL PRIMARY KEY, AGE INT); ");
		// PASS:0625 If table is created?

		stmt.executeUpdate("CREATE VIEW CHANGGVIEW AS "
				+ "SELECT * FROM CHANGG; ");
		// PASS:0625 If view is created?

		try {
			stmt.executeUpdate("ALTER TABLE CHANGG " + "DROP NAAM RESTRICT; ");
			fail("NAAM from table CHANGG is referenced in veiw CHANGGVIEW");
			// PASS:0625 If ERROR, view references NAAM?
		} catch (SQLException sqle) {
		}
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO CHANGG VALUES ('RALPH', 22);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO CHANGG VALUES ('RUDOLPH', 54);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO CHANGG VALUES ('QUEEG', 33); "));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO CHANGG VALUES ('BESSIE', 106);"));

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM CHANGG WHERE DIVORCES IS NULL; ");
			fail("Column DIVORCES unknown");
			// PASS:0625 If ERROR, column does not exist?
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("ALTER TABLE CHANGG ADD NUMBRR CHAR(11);");
		// PASS:0625 If column is added?

		rs = stmt.executeQuery("SELECT MAX(AGE) FROM CHANGGVIEW; ");
		rs.next();
		assertEquals(106, rs.getInt(1));
		// PASS:0625 If value is 106?

		try {
			rs = stmt.executeQuery("SELECT MAX(NUMBRR) FROM CHANGGVIEW;");
			// PASS:0625 If ERROR, column does not exist ?
			fail("Column NUMBER unknown");
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("DROP VIEW CHANGGVIEW ; ");
		// PASS:0625 If view is dropped?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE CHANGG "
					+ "ADD DIVORCES INT DEFAULT 0; ");
		else
			stmt.executeUpdate("ALTER TABLE CHANGG "
					+ "ADD COLUMN DIVORCES INT DEFAULT 0; ");

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM CHANGG WHERE NUMBRR IS NOT NULL "
				+ "OR DIVORCES <> 0; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0625 If count = 0?

		assertEquals(1, stmt.executeUpdate("UPDATE CHANGG "
				+ "SET NUMBRR = '837-47-1847', DIVORCES = 3 "
				+ "WHERE NAAM = 'RUDOLPH';"));
		// PASS:0625 If 1 row is updated?

		assertEquals(1, stmt.executeUpdate("UPDATE CHANGG "
				+ "SET NUMBRR = '738-47-1847', DIVORCES = NULL "
				+ "WHERE NAAM = 'QUEEG';"));
		// PASS:0625 If 1 row is updated?

		assertEquals(2, stmt.executeUpdate("DELETE FROM CHANGG "
				+ "WHERE NUMBRR IS NULL;"));
		// PASS:0625 If 2 rows are deleted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO CHANGG (NAAM, AGE, NUMBRR) "
						+ "VALUES ('GOOBER', 16, '000-10-0001');"));
		// PASS:0625 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CHANGG "
				+ "VALUES ('OLIVIA', 20, '111-11-1111', 0);"));
		// PASS:0625 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT AGE, NUMBRR, DIVORCES " + "FROM CHANGG "
				+ "WHERE NAAM = 'RUDOLPH'; ");
		rs.next();
		assertEquals(54, rs.getInt(1));
		assertEquals("837-47-1847", rs.getString(2));
		assertEquals(3, rs.getInt(3));
		assertFalse(rs.next());
		// PASS:0625 If 1 row selected with values 54, 837-47-1847, 3 ?

		rs = stmt.executeQuery("SELECT AGE, NUMBRR, DIVORCES " + "FROM CHANGG "
				+ "WHERE NAAM = 'QUEEG'; ");
		rs.next();
		assertEquals(33, rs.getInt(1));
		assertEquals("738-47-1847", rs.getString(2));
		assertEquals(0, rs.getInt(3));
		assertFalse(rs.next());
		// PASS:0625 If 1 row selected with values 33, 738-47-1847, NULL ?

		rs = stmt.executeQuery("SELECT AGE, NUMBRR, DIVORCES " + "FROM CHANGG "
				+ "WHERE NAAM = 'GOOBER';");
		rs.next();
		assertEquals(16, rs.getInt(1));
		assertEquals("000-10-0001", rs.getString(2));
		assertEquals(0, rs.getInt(3));
		assertFalse(rs.next());
		// PASS:0625 If 1 row selected with values 16, 000-10-0001, 0 ?

		rs = stmt.executeQuery("SELECT AGE, NUMBRR, DIVORCES " + "FROM CHANGG "
				+ "WHERE NAAM = 'OLIVIA';");
		rs.next();
		assertEquals(20, rs.getInt(1));
		assertEquals("111-11-1111", rs.getString(2));
		assertEquals(0, rs.getInt(3));
		assertFalse(rs.next());
		// PASS:0625 If 1 row selected with values 20, 111-11-1111, 0 ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM CHANGG; ");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0625 If count = 4?

		stmt.executeUpdate("ALTER TABLE CHANGG DROP AGE ;");
		// PASS:0625 If column is dropped?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt
					.executeUpdate("ALTER TABLE CHANGG DROP COLUMN DIVORCES RESTRICT; ");
		else
			stmt.executeUpdate("ALTER TABLE CHANGG DROP DIVORCES RESTRICT; ");
		// PASS:0625 If column is dropped?

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM CHANGG WHERE AGE > 30; ");
			fail("Age column unknown");
			// PASS:0625 If ERROR, column does not exist?
		} catch (SQLException sqle) {
		}

		try {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM CHANGG WHERE DIVORCES IS NULL; ");
			fail("Divorces column unknown");
			// PASS:0625 If ERROR, column does not exist?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT NAAM  " + "FROM CHANGG "
				+ "WHERE NUMBRR LIKE '%000%'; ");
		rs.next();
		assertEquals("GOOBER", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0625 If 1 row selected with value GOOBER ?

		stmt.executeUpdate("CREATE TABLE REFERENCE_CHANGG ( "
				+ "NAAM CHAR (14) NOT NULL PRIMARY KEY "
				+ "REFERENCES CHANGG); ");
		// PASS:0625 If table is created?

		try {
			stmt
					.executeUpdate("INSERT INTO REFERENCE_CHANGG VALUES ('NO SUCH NAAM');");
			// PASS:0625 If RI ERROR, parent missing, 0 rows inserted?
			fail("Violation of Foreign Key constrain");
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("ALTER TABLE CHANGG DROP NAAM RESTRICT; ");
			// PASS:0625 If ERROR, referential constraint exists?
		} catch (SQLException sqle) {
		}

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {

			try {
				stmt.executeUpdate("ALTER TABLE CHANGG DROP NAAM restrict; ");
				fail();
			} catch (SQLException sqle) {
			}

			stmt.executeUpdate("ALTER TABLE CHANGG DROP NAAM CASCADE;");
			stmt.executeUpdate("INSERT INTO REFERENCE_CHANGG "
					+ "VALUES ('NO SUCH NAAM'); ");
			stmt.executeUpdate("ALTER TABLE CHANGG DROP NUMBRR RESTRICT; ");
			// PASS:0625 If ERROR, last column may not be dropped?

			stmt.executeUpdate("DROP TABLE CHANGG CASCADE;");
			stmt.executeUpdate("DROP TABLE REFERENCE_CHANGG cascade;");
		}
		// PASS:0625 If table is dropped?
		// END TEST >>> 0625 <<< END TEST

		// *********************************************
		// TEST:0631 Datetimes in a <default clause>!

		stmt.executeUpdate("CREATE TABLE OBITUARIES ( "
				+ "NAAM CHAR (14) NOT NULL PRIMARY KEY, "
				+ "BORN DATE DEFAULT DATE '1880-01-01', "
				+ "DIED DATE DEFAULT CURRENT_DATE, "
				+ "ENTERED TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
				+ "TESTING1 DATE, " + "TESTING2 TIMESTAMP); ");
		// PASS:0631 If table is created?

		// do we support INTERVAL types?
		boolean supportsIntervalType = false;
		rs = conn.getMetaData().getTypeInfo();
		while (rs.next()) {
			if (rs.getString(1).toUpperCase().matches(".*INTERVAL.*"))
				supportsIntervalType = true;

		}

		boolean supportsExtract = false;
		if (conn.getMetaData().getTimeDateFunctions().toUpperCase().matches(
				".*EXTRACT"))
			supportsExtract = true;

		if (supportsIntervalType) {
			stmt.executeUpdate("CREATE TABLE BIRTHS ( "
					+ "NAAM CHAR (14) NOT NULL PRIMARY KEY, "
					+ "CHECKIN TIME (0) " + " DEFAULT TIME '00:00:00', "
					+ "LABOR INTERVAL HOUR " + " DEFAULT INTERVAL '4' HOUR, "
					+ "CHECKOUT TIME " + "DEFAULT CURRENT_TIME, "
					+ "TESTING TIME); ");
			// PASS:0631 If table is created?

			assertEquals(1, stmt.executeUpdate("INSERT INTO OBITUARIES ("
					+ "NAAM, TESTING1, TESTING2) " + "VALUES ("
					+ "'KEITH', CURRENT_DATE, CURRENT_TIMESTAMP); "));
			// PASS:0631
			// If 1 row is inserted?

			assertEquals(1, stmt
					.executeUpdate("INSERT INTO BIRTHS (NAAM, TESTING) "
							+ "VALUES ('BJORN', CURRENT_TIME); "));
			// PASS:0631 If 1 row is inserted?

			if (supportsExtract) {
				rs = stmt.executeQuery("SELECT EXTRACT (HOUR FROM CHECKIN) + "
						+ "EXTRACT (MINUTE FROM CHECKIN) +  "
						+ "EXTRACT (SECOND FROM CHECKIN) FROM BIRTHS; ");
				rs.next();
				assertEquals(0, rs.getInt(1));
				assertFalse(rs.next());
				// PASS:0631 If 1 row selected with value 0?
				rs = stmt
						.executeQuery("SELECT EXTRACT (HOUR FROM LABOR) FROM BIRTHS; ");
				rs.next();
				assertEquals(4, rs.getInt(1));
				assertFalse(rs.next());
				// PASS:0631 If 1 row selected with value 4?
			}
			rs = stmt.executeQuery("SELECT COUNT (*) FROM BIRTHS "
					+ "WHERE TESTING <> CHECKOUT OR CHECKOUT IS  NULL;");
			rs.next();
			assertEquals(0, rs.getInt(1));
			// PASS:0631 If count = 0?

			rs = stmt
					.executeQuery("SELECT COUNT (*) FROM OBITUARIES "
							+ "WHERE BORN <> DATE '1880-01-01' OR BORN IS NULL OR DIED <> TESTING1 OR DIED IS NULL OR ENTERED <> TESTING2 OR ENTERED IS NULL; ");
			rs.next();
			assertEquals(0, rs.getInt(1));
			// PASS:0631 If count = 0?

			stmt.executeQuery("DROP TABLE BIRTHS");
			// PASS:0631 If table is dropped?

			stmt.executeUpdate("DROP TABLE OBITUARIES CASCADE;");
			// PASS:0631 If table is dropped?
		}

		// END TEST >>> 0631 <<< END TEST
		// *********************************************
		// TEST:0633 TRIM function!
		if (conn.getMetaData().getStringFunctions().toUpperCase().matches(
				".*TRIM.*")) {
			stmt.executeUpdate("CREATE TABLE WEIRDPAD ( " + "NAAM CHAR (14), "
					+ "SPONSOR CHAR (14), " + "PADCHAR CHAR (1)); ");
			// PASS:0633 If table is created?

			assertEquals(1, stmt
					.executeUpdate("INSERT INTO WEIRDPAD (NAAM, SPONSOR) "
							+ "VALUES ('KATEBBBBBBBBBB', '000000000KEITH'); "));
			// PASS:0633 If 1 row is inserted?

			assertEquals(1, stmt
					.executeUpdate("INSERT INTO WEIRDPAD (NAAM, SPONSOR) "
							+ "VALUES ('    KEITH     ', 'XXXXKATEXXXXXX');"));
			// PASS:0633 If 1 row is inserted?

			rs = stmt.executeQuery("SELECT TRIM ('X' FROM SPONSOR) "
					+ "FROM WEIRDPAD " + "WHERE TRIM (NAAM) = 'KEITH';");
			rs.next();
			assertEquals("KATE", rs.getString(1).trim());
			assertFalse(rs.next());
			// PASS:0633 If 1 row selected with value KATE ?

			rs = stmt.executeQuery("SELECT TRIM (LEADING 'X' FROM SPONSOR) "
					+ "FROM WEIRDPAD "
					+ "WHERE TRIM (TRAILING FROM NAAM) = '    KEITH'; ");
			rs.next();
			assertEquals("KATEXXXXXX", rs.getString(1).trim());
			assertFalse(rs.next());
			// PASS:0633 If 1 row selected with value KATEXXXXXX ?

			rs = stmt.executeQuery("SELECT TRIM (LEADING 'X' FROM SPONSOR) "
					+ "FROM WEIRDPAD "
					+ "WHERE TRIM (TRAILING 'X' FROM SPONSOR) = 'XXXXKATE'; ");
			rs.next();
			assertEquals("KATEXXXXXX", rs.getString(1).trim());
			assertFalse(rs.next());
			// PASS:0633 If 1 row selected with value KATEXXXXXX ?

			rs = stmt
					.executeQuery("SELECT TRIM (LEADING FROM B.NAAM)  FROM WEIRDPAD A, "
							+ "WEIRDPAD B WHERE TRIM (BOTH 'B' FROM A.NAAM) "
							+ "= TRIM (BOTH 'X' FROM B.SPONSOR); ");
			rs.next();
			assertEquals("KATEXXXXXX", rs.getString(1).trim());
			assertFalse(rs.next());

			rs = stmt.executeQuery("SELECT COUNT(*) FROM WEIRDPAD A, "
					+ "WEIRDPAD B WHERE TRIM (LEADING '0' FROM A.SPONSOR) "
					+ " = TRIM (' ' FROM B.NAAM); ");
			rs.next();
			assertEquals(1, rs.getInt(1));
			// PASS:0633 If count = 1?

			try {
				rs = stmt.executeQuery("SELECT TRIM ('BB' FROM NAAM) "
						+ "FROM WEIRDPAD WHERE NAAM LIKE 'KATE%'; ");
				fail("");
				// PASS:0633 If ERROR, length of trim character must be 1 ?
			} catch (SQLException sqle) {
			}

			assertEquals(
					3,
					stmt
							.executeUpdate("INSERT INTO WEIRDPAD (NAAM, SPONSOR) "
									+ "SELECT DISTINCT TRIM (LEADING 'D' FROM STAFF.CITY),  "
									+ "TRIM (TRAILING 'n' FROM PTYPE) "
									+ "FROM STAFF, PROJ "
									+ "WHERE EMPNAME = 'Alice'; "));
			// PASS:0633 If 3 rows are inserted?
			rs = stmt.executeQuery("SELECT COUNT(*) FROM WEIRDPAD;");
			rs.next();
			assertEquals(5, rs.getInt(1));
			// PASS:0633 If count = 5?

			assertEquals(2, stmt.executeUpdate("UPDATE WEIRDPAD "
					+ "SET SPONSOR = TRIM ('X' FROM SPONSOR), "
					+ "NAAM = TRIM ('B' FROM NAAM); "));
			// PASS:0633 If 2 rows are updated?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM WEIRDPAD "
					+ "WHERE NAAM = 'KATE' OR SPONSOR = 'KATE'; ");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:0633 If count = 2?

			assertEquals(
					1,
					stmt
							.executeUpdate("DELETE FROM WEIRDPAD WHERE "
									+ "TRIM(LEADING 'K' FROM 'Kest') = TRIM('T' FROM SPONSOR);"));
			// PASS:0633 If 1 row is deleted?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM WEIRDPAD; ");
			rs.next();
			assertEquals(4, rs.getInt(1));
			// PASS:0633 If count = 4?

			assertEquals(3, stmt.executeUpdate("UPDATE WEIRDPAD "
					+ "SET PADCHAR = '0' "
					+ "WHERE SPONSOR = '000000000KEITH' "
					+ "OR NAAM    = 'eale'; "));
			// PASS:0633 If 3 rows are updated?

			assertEquals(3, stmt.executeUpdate("UPDATE WEIRDPAD "
					+ "SET SPONSOR = NULL " + "WHERE SPONSOR = 'Desig'; "));
			// PASS:0633 If 1 row is updated?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM WEIRDPAD "
					+ "WHERE TRIM (PADCHAR FROM SPONSOR) IS NULL; ");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:0633 If count = 2?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM WEIRDPAD "
					+ "WHERE TRIM (PADCHAR FROM SPONSOR) = 'KEITH'; ");
			rs.next();
			assertEquals(1, rs.getInt(1));
			// PASS:0633 If count = 1?

			stmt.executeUpdate("DROP TABLE WEIRDPAD ;");
			// PASS:0633 If table is dropped?

		}
		// END TEST >>> 0633 <<< END TEST

		//*************************************************////END-OF-MODULE
	}
	public void testDml_114() throws SQLException {
		int rowCount;

		BaseTab.setupBaseTab(stmt);
		// TEST:0635 Feature 13, grouped operations (static)!

		// Firebird doesn't support GROUP BY in view...
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE VIEW WORKWEEK AS "
					+ "SELECT EMPNUM, HOURS FROM WORKS "
					+ "GROUP BY HOURS, EMPNUM; ");
			// PASS:0635 If table is created?

			rs = stmt.executeQuery("SELECT EMPNUM, SUM (HOURS) "
					+ "FROM WORKWEEK " + "WHERE HOURS > 20 "
					+ "GROUP BY EMPNUM " + "HAVING EMPNUM = 'E1'; ");
			assertTrue(rs.next());
			assertEquals("E1", rs.getString(1).trim());
			assertEquals(120, rs.getInt(2));
			assertFalse(rs.next());
			// PASS:0635 If 1 row selected and EMPNUM = 'E1' and SUM(HOURS)

			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM WORKWEEK WHERE HOURS > 40; ");
			rs.next();
			assertEquals(3, rs.getInt(1));
			// PASS:0635 If count = 3?

			rs = stmt.executeQuery("SELECT EMPNAME " + "FROM STAFF, WORKWEEK "
					+ "WHERE STAFF.EMPNUM = WORKWEEK.EMPNUM "
					+ "AND HOURS = 12; ");
			assertTrue(rs.next());
			assertEquals("Alice", rs.getString(1).trim());
			assertFalse(rs.next());
			// PASS:0635 If 1 row selected and EMPNAME = 'Alice'?

			rs = stmt
					.executeQuery("SELECT COUNT(*), MAX(EMPNUM), MIN(EMPNUM), AVG(HOURS) "
							+ "FROM WORKWEEK; ");
			assertTrue(rs.next());
			assertEquals(10, rs.getInt(1));
			assertEquals("E4", rs.getString(2).trim());
			assertEquals("E1", rs.getString(3).trim());
			assertEquals(43, rs.getInt(4));
			assertFalse(rs.next());
			// PASS:0635 If 1 row selected and count =
			// 10 and MAX(EMPNUM) = 'E4'?
			// PASS:0635 AND MIN(EMPNUM) = 'E1' and AVG(HOURS) = 43
			// (approximately)?

			rs = stmt.executeQuery("SELECT EMPNAME "
					+ "FROM STAFF WHERE EMPNUM = "
					+ "(SELECT EMPNUM FROM WORKWEEK " + "WHERE HOURS = 12); ");
			rs.next();
			assertEquals("Alice", rs.getString(1).trim());
			assertFalse(rs.next());
			// PASS:0635 If 1 row selected and EMPNAME = 'Alice'? }

			rs = stmt.executeQuery("SELECT EMPNAME "
					+ "FROM STAFF WHERE EMPNUM = "
					+ "(SELECT EMPNUM FROM WORKS " + "GROUP BY EMPNUM, HOURS "
					+ "HAVING HOURS = 12);");
			rs.next();
			assertEquals("Alice", rs.getString(1).trim());
			assertFalse(rs.next());
			// PASS:0635 If 1 row selected and EMPNAME = 'Alice'?

			// NOTE:0635 Cursor subtest deleted.

			stmt.executeUpdate("DROP VIEW WORKWEEK ;");

			// END TEST >>> 0635 <<< END TEST
			// *********************************************

		} // group by in view

		// TEST:0637 Feature 14, Qualified * in select list (static)!
		stmt.executeUpdate("CREATE VIEW QUALSTAR AS "
				+ "SELECT STAFF.*, HOURS FROM STAFF, WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM; ");
		// PASS:0637 If view is created?

		stmt.executeUpdate("CREATE VIEW CORRQUALSTAR AS "
				+ "SELECT BLAH.*, HOURS FROM STAFF BLAH, WORKS "
				+ "WHERE BLAH.EMPNUM = WORKS.EMPNUM; ");
		// PASS:0637 If view is created?

		stmt.executeUpdate("CREATE VIEW SUBQ2 AS "
				+ "SELECT DISTINCT * FROM QUALSTAR;");
		// PASS:0637 If view is created?

		stmt.executeUpdate("CREATE VIEW CORRSUBQ2 AS "
				+ "SELECT DISTINCT * FROM CORRQUALSTAR;");
		// PASS:0637 If view is created?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM QUALSTAR;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:0637 If count = 12?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SUBQ2;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		// PASS:0637 If count = 10?

		rs = stmt.executeQuery("SELECT EMPNUM, GRADE, CITY, HOURS "
				+ "FROM QUALSTAR WHERE EMPNAME = 'Carmen'; ");
		assertTrue(rs.next());
		assertEquals("E3", rs.getString(1).trim());
		assertEquals(13, rs.getInt(2));
		assertEquals("Vienna", rs.getString(3).trim());
		assertEquals(20, rs.getInt(4));
		assertFalse(rs.next());
		// PASS:0637 If 1 row selected and EMPNUM = 'E3' and GRADE = 13?
		// PASS:0637 AND CITY = 'Vienna' and HOURS = 20?

		// NOTE:0637 Cursor subtest deleted.

		rs = stmt.executeQuery("SELECT STAFF.*, HOURS " + "FROM STAFF, WORKS "
				+ "WHERE STAFF.EMPNUM = WORKS.EMPNUM "
				+ "AND EMPNAME = 'Carmen'; ");
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals("Carmen", rs.getString(2).trim());
		assertEquals(13, rs.getInt(3));
		assertEquals("Vienna", rs.getString(4).trim());
		assertEquals(20, rs.getInt(5));
		assertFalse(rs.next());
		// PASS:0637 If 1 row selected and EMPNUM = 'E3' and EMPNAME = 'Carmen'?
		// PASS:0637 AND GRADE = 13 and CITY = 'Vienna' and HOURS = 20?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM CORRQUALSTAR; ");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:0637 If count = 12?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM CORRSUBQ2; ");
		rs.next();
		assertEquals(10, rs.getInt(1));
		// PASS:0637 If count = 10?

		rs = stmt.executeQuery("SELECT EMPNUM, GRADE, CITY, HOURS "
				+ "FROM CORRQUALSTAR WHERE EMPNAME = 'Carmen'; ");
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals(13, rs.getInt(2));
		assertEquals("Vienna", rs.getString(3).trim());
		assertEquals(20, rs.getInt(4));
		assertFalse(rs.next());
		// PASS:0637 If 1 row selected and EMPNUM = 'E3'?
		// PASS:0637 AND GRADE = 13 and CITY = 'Vienna' and HOURS = 20?

		try {
			stmt.executeUpdate("DROP VIEW QUALSTAR ; ");
			fail("Can not delete column empnum.");
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("DROP VIEW CORRQUALSTAR ; ");
			fail("Can not delete column empnum.");
		} catch (SQLException sqle) {
		}

		// END TEST >>> 0637 <<< END TEST
		// *********************************************

		// if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
		// TEST:0639 Feature 15, Lowercase Identifiers (static)!
		if (conn.getMetaData().storesMixedCaseIdentifiers()) {
			stmt.executeUpdate("create view Staff ("
					+ "Empnum, empname, Grade, City) as "
					+ "select empnum, EMPNAME, Grade, cItY from Staff; ");
			// PASS:0639 If view is created?

			rs = stmt.executeQuery("SELECT EMPNUM as WhatsHisNumber, "
					+ "GRADE, CITY " + "FROM Staff "
					+ "WHERE EMPNAME = 'Carmen' "
					+ "AND FLATERstaff_fLATER.whatshisnumber = 'E3'; ");
			rs.next();
			assertEquals("E3", rs.getString(1).trim());
			assertEquals(13, rs.getInt(2));
			assertEquals("Vienna", rs.getString(3).trim());
			assertEquals(20, rs.getInt(4));
			assertFalse(rs.next());
			// PASS:0639 If 1 row selected and EMPNUM = 'E3'?
			// PASS:0639 AND GRADE = 13 and CITY = 'Vienna'?
		}

		// TEST:0641 Feature 16, PRIMARY KEY enhancement (static)!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE FEAT16 ( "
					+ "EMPNUM INT NOT NULL PRIMARY KEY, "
					+ "PNUM   INT UNIQUE); ");
		else
			stmt.executeUpdate("CREATE TABLE FEAT16 ( "
					+ "EMPNUM INT PRIMARY KEY, " + "PNUM   INT UNIQUE); ");

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE BARNO ( "
					+ "P1 INT NOT NULL, P2 CHAR NOT NULL, X1 INT, X2 CHAR, "
					+ "UNIQUE (X2, X1), " + "PRIMARY KEY (P1, P2)); ");
		else
			stmt.executeUpdate("CREATE TABLE BARNO ( "
					+ "P1 INT, P2 CHAR, X1 INT, X2 CHAR, "
					+ "UNIQUE (X2, X1), " + "PRIMARY KEY (P1, P2)); ");
		// PASS:0641 If view is created?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO FEAT16 VALUES (1, 10); "));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO FEAT16 VALUES (2, 20); "));

		try {
			stmt.executeUpdate("INSERT INTO FEAT16 VALUES (1, 30); ");
			fail("Violation of Primary key");
			// PASS:0641 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO FEAT16 VALUES (3, 20);");
			fail("Violation of Primary key");
			// PASS:0641 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO FEAT16 "
				+ "VALUES (3, NULL);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO FEAT16 "
				+ "VALUES (4, NULL);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO FEAT16 "
				+ "VALUES (5, NULL);"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO BARNO VALUES (1, 'A', 10, 'a');"));
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO BARNO VALUES (2, 'A', 20, 'a');"));

		try {
			stmt.executeUpdate("INSERT INTO BARNO VALUES (1, 'A', 30, 'a');");
			fail("Violation of Primary key");
			// PASS:0641 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO BARNO VALUES (3, 'A', 20, 'a');");
			fail("Violation of Primary key");
			// PASS:0641 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO BARNO VALUES (3, NULL, 30, 'a');");
			fail("Validation error ***null***");
			// PASS:0641 If ERROR, PRIMARY KEY constraint, 0 rows inserted?
			// PASS:0641 OR ERROR, NOT NULL constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO BARNO VALUES (3, NULL, 30, 'b');");
			fail("Validation error ***null***");
			// PASS:0641 If ERROR, PRIMARY KEY constraint, 0 rows inserted?
			// PASS:0641 OR ERROR, NOT NULL constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("INSERT INTO BARNO VALUES ("
				+ "3, 'A', 30, NULL);"));
		// PASS:0641 If 1 row is inserted?

		try {
			assertEquals(
					1,
					stmt
							.executeUpdate("INSERT INTO BARNO VALUES (3, 'B', 30, NULL);"));
			// test doc says this should pass, but we already have a value of
			// 30, null for x1, x2 in the database. I think it should fail. It
			// does fail for Firebird 1.5.
			// PASS:0641 If 1 row is inserted?
		} catch (SQLException sqle) {
		}
		assertEquals(1, stmt.executeUpdate("INSERT INTO BARNO VALUES ("
				+ "4, 'B', NULL, NULL);"));
		// PASS:0641 If 1 row is inserted?
		// END TEST >>> 0641 <<< END TEST
	} /* end of testDml_114 */

	/*
	 * 
	 * testDml_117_ReferentialDeleteActionsStatic
	 * 
	 * TEST:0645 Feature 19, Referential delete actions (static)!
	 *  
	 */
	public void testDml_117_ReferentialDeleteActionsStatic()
			throws SQLException {

		// big difference between NIST and Firebird is that Firebird wants the
		// table constraints LAST, not FIRST.
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE LUSERS ( " + "NAAM CHAR (10), "
					+ "LUSER_ID int not null primary key, "
					+ "FILE_QUOTA INT, " + "FILE_USAGE INT NOT NULL, "
					+ "CHECK (FILE_USAGE >= 0 AND "
					+ "(FILE_QUOTA IS NULL OR FILE_QUOTA >= FILE_USAGE)));");
		else
			stmt.executeUpdate("CREATE TABLE LUSERS ( "
					+ "PRIMARY KEY (LUSER_ID), " + "NAAM CHAR (10), "
					+ "LUSER_ID INT, " + "FILE_QUOTA INT, "
					+ "FILE_USAGE INT NOT NULL, "
					+ "CHECK (FILE_USAGE >= 0 AND "
					+ "(FILE_QUOTA IS NULL OR FILE_QUOTA >= FILE_USAGE)));");
		// PASS:0645 If table is created?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt
					.executeUpdate("CREATE TABLE LUSER_DATA ( "
							+ "FILE_NAME     CHAR (8) NOT NULL, "
							+ "LUSER_ID int  NOT NULL  , "
							+ "LUSER_DATA   CHAR (30), "
							+ "primary key (FILE_NAME, LUSER_ID) ,  "
							+ "FOREIGN KEY (LUSER_ID) REFERENCES LUSERS on delete cascade)");
		else
			stmt
					.executeUpdate("CREATE TABLE LUSER_DATA ( "
							+ "FOREIGN KEY (LUSER_ID) REFERENCES LUSERS ON DELETE CASCADE, "
							+ "PRIMARY KEY (FILE_NAME, LUSER_ID), "
							+ "FILE_NAME     CHAR (8) NOT NULL, "
							+ "LUSER_ID     INT NOT NULL, "
							+ "LUSER_DATA   CHAR (30));");
		// PASS:0645 If table is created?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt
					.executeUpdate("CREATE TABLE AUDIT_CODES ( "
							+ "ACTION_KEY INT not null PRIMARY KEY, "
							+ "LUSER_ACTION CHAR (6) NOT NULL, "
							+ "CHECK (LUSER_ACTION = 'INSERT' OR LUSER_ACTION = 'ACCVIO' "
							+ "OR LUSER_ACTION = 'DELETE'));");
		else
			stmt
					.executeUpdate("CREATE TABLE AUDIT_CODES ( "
							+ "ACTION_KEY INT PRIMARY KEY, "
							+ "LUSER_ACTION CHAR (6) NOT NULL, "
							+ "CHECK (LUSER_ACTION = 'INSERT' OR LUSER_ACTION = 'ACCVIO' "
							+ "OR LUSER_ACTION = 'DELETE'));");
		// PASS:0645 If table is created?

		stmt.executeUpdate("CREATE TABLE ALL_USER_IDS (LUSER_ID INT UNIQUE);");
		// PASS:0645 If table is created?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt
					.executeUpdate("CREATE TABLE AUDIT_RECORDS ( "
							+ "LUSER_ID INT, "
							+ "SAVED_LUSER_ID INT NOT NULL REFERENCES ALL_USER_IDS (LUSER_ID) ON DELETE NO ACTION, "
							+ "ACTION_KEY INT DEFAULT 0 NOT NULL  REFERENCES AUDIT_CODES ON DELETE SET DEFAULT, "
							+ "FOREIGN KEY (LUSER_ID) REFERENCES LUSERS ON DELETE SET NULL);");
		else
			stmt
					.executeUpdate("CREATE TABLE AUDIT_RECORDS ( "
							+ "FOREIGN KEY (LUSER_ID) REFERENCES LUSERS ON DELETE SET NULL, "
							+ "LUSER_ID         INT, "
							+ "SAVED_LUSER_ID   INT NOT NULL "
							+ "REFERENCES ALL_USER_IDS (LUSER_ID) ON DELETE NO ACTION, "
							+ "ACTION_KEY       INT DEFAULT 0 NOT NULL "
							+ "REFERENCES AUDIT_CODES ON DELETE SET DEFAULT);");
		// PASS:0645 If table is created?

		stmt.executeUpdate("INSERT INTO AUDIT_CODES VALUES (0, 'ACCVIO');");
		stmt.executeUpdate("INSERT INTO AUDIT_CODES VALUES (1, 'INSERT');");
		stmt.executeUpdate("INSERT INTO AUDIT_CODES VALUES (2, 'DELETE');");
		stmt.executeUpdate("INSERT INTO LUSERS VALUES ('root', 0, NULL, 2);");
		stmt.executeUpdate("INSERT INTO LUSERS VALUES ('BIFF', 1, 0, 0);");
		stmt.executeUpdate("INSERT INTO LUSERS VALUES ('Kibo', 2, 1, 1);");
		stmt.executeUpdate("INSERT INTO ALL_USER_IDS VALUES (0);");
		stmt.executeUpdate("INSERT INTO ALL_USER_IDS VALUES (1);");

		stmt.executeUpdate("INSERT INTO ALL_USER_IDS VALUES (2);");

		stmt.executeUpdate("INSERT INTO LUSER_DATA VALUES ('ROOT1', "
				+ "0, 'BIFF is a total loser');");
		stmt.executeUpdate("INSERT INTO LUSER_DATA VALUES ('ROOT2', "
				+ "0, 'Kibo wastes disk space');");

		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (0, 0, 1);");

		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (0, 0, 1);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (2, 2, 1);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (1, 1, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (1, 1, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (1, 1, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (1, 1, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (1, 1, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (1, 1, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (2, 2, 0);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (2, 2, 2);");
		stmt.executeUpdate("INSERT INTO AUDIT_RECORDS VALUES (2, 2, 1);");
		stmt.executeUpdate("INSERT INTO LUSER_DATA VALUES ('HAHA', "
				+ "2, 'I G0T KIB0Z PASSW0RD!!!');");

		assertEquals(2, stmt
				.executeUpdate("DELETE FROM LUSERS WHERE NAAM <> 'root';"));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM LUSER_DATA;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0645 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM LUSERS;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0645 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM AUDIT_RECORDS;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:0645 If count = 12?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM AUDIT_RECORDS "
				+ "WHERE LUSER_ID IS NULL;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		// PASS:0645 If count = 10?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM AUDIT_RECORDS "
				+ "WHERE SAVED_LUSER_ID IS NULL;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0645 If count = 0?

		assertEquals(1, stmt.executeUpdate("DELETE FROM AUDIT_CODES "
				+ "WHERE LUSER_ACTION = 'DELETE';"));
		// PASS:0645 If 1 row is deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM AUDIT_RECORDS "
				+ "WHERE ACTION_KEY = 2;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0645 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM AUDIT_RECORDS "
				+ "WHERE ACTION_KEY = 0;");
		rs.next();
		assertEquals(8, rs.getInt(1));
		// PASS:0645 If count = 8?

		try {
			stmt.executeUpdate("DELETE FROM ALL_USER_IDS;");
			fail();
			// PASS:0645 If RI ERROR, children exist, 0 rows deleted?
		} catch (SQLException sqle) {

		}
		stmt.executeUpdate("DROP TABLE AUDIT_RECORDS ;");
		// PASS:0645 If table is dropped?

		stmt.executeUpdate("DROP TABLE ALL_USER_IDS ;");
		// PASS:0645 If table is dropped?

		stmt.executeUpdate("DROP TABLE AUDIT_CODES ;");
		// PASS:0645 If table is dropped?

		stmt.executeUpdate("DROP TABLE LUSER_DATA ;");
		// PASS:0645 If table is dropped?
		// stmt.executeUpdate("DROP TABLE LUSERS ;");
		// drop won't work in FB until all integrity constraints are removed.
		// PASS:0645 If table is dropped?

		// END TEST >>> 0645 <<< END TEST
	}

	/*
	 * 
	 * testDml_119_CastFunctionsStatic
	 * 
	 * Notes: The view here is a convenience view. Firebird doesn't like the
	 * CAST in the view operation. Therefore, we'll do all work directly against
	 * the table, instead of the view when using Firebird.
	 *  
	 */
	public void testDml_119_CastFunctionsStatic() throws SQLException {

		stmt.executeUpdate("CREATE TABLE USER_INPUT ( "
				+ "USER_ID INT, USER_TYPED CHAR (10), "
				+ "CASH_BALANCE NUMERIC (5, 2));");
		// PASS:0647 If table is created?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE VIEW STANDARD_INPUT AS "
					+ "SELECT CAST (USER_ID AS CHAR (10)) AS USER_NAME, "
					+ "CAST (USER_TYPED AS NUMERIC (5, 2)) AS USER_INPUT, "
					+ "CAST (CASH_BALANCE AS REAL) AS RECEIVABLE "
					+ "FROM USER_INPUT;");
		}

		stmt.executeUpdate("INSERT INTO USER_INPUT VALUES "
				+ "(0, '999.99', 999.99);");
		stmt.executeUpdate("INSERT INTO USER_INPUT VALUES "
				+ "(1, '-999.99', -999.99);");
		stmt.executeUpdate("INSERT INTO USER_INPUT VALUES "
				+ "(2, '  54.', 54);");
		stmt.executeUpdate("INSERT INTO USER_INPUT VALUES "
				+ "(CAST ('3' AS INT), CAST (-7.02 AS CHAR (10)), "
				+ "CAST (' -.702E+1' AS NUMERIC (5, 2)));");

		rs = stmt
				.executeQuery("SELECT CAST (AVG (CAST (USER_TYPED AS INT)) AS INT) "
						+ "FROM USER_INPUT;");
		rs.next();
		assertTrue((11 == rs.getInt(1)) || (12 == rs.getInt(1)));
		assertFalse(rs.next());
		// PASS:0647 If 1 row selected and value is 11 or 12?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))

			rs = stmt
					.executeQuery("SELECT AVG (cast (USER_TYPED as numeric (5,2))) "
							+ "FROM USER_INPUT;");

		else
			rs = stmt.executeQuery("SELECT AVG (USER_INPUT) "
					+ "FROM STANDARD_INPUT;");
		rs.next();
		assertTrue((11.74 <= rs.getDouble(1)) && (11.75 >= rs.getDouble(1)));
		assertFalse(rs.next());
		// PASS:0647 If 1 row selected and value is 11.745 +- 0.01?

		assertEquals(
				1,
				stmt
						.executeUpdate("UPDATE USER_INPUT "
								+ "SET USER_TYPED = CAST (0 AS CHAR (10)), "
								+ "CASH_BALANCE = CASH_BALANCE - CAST ('500' AS NUMERIC (5, 2)) "
								+ "WHERE USER_ID = CAST ('-0' AS INT);"));
		// PASS:0647 If 1 row is updated?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			rs = stmt
					.executeQuery("SELECT SUM (USER_TYPED) * 100, SUM (cast (CASH_BALANCE as real)) "
							+ "FROM USER_INPUT;");
		else
			rs = stmt
					.executeQuery("SELECT SUM (USER_INPUT) * 100, SUM (RECEIVABLE) "
							+ "FROM STANDARD_INPUT;");
		rs.next();
		assertTrue((-95305 <= rs.getDouble(1)) && (-95297 >= rs.getDouble(1)));
		assertTrue((-453.06 <= rs.getDouble(2)) && (-452.98 >= rs.getDouble(2)));
		assertFalse(rs.next());
		// PASS:0647 If 1 row selected and first value is -95301 +- 4?
		// PASS:0647 AND second value is -453.02 +- 0.04?

		stmt.executeUpdate("DELETE FROM USER_INPUT;");

		stmt.executeUpdate("INSERT INTO USER_INPUT VALUES "
				+ "(CAST ('3' AS INT), CAST (-7.02 AS CHAR (10)), "
				+ "CAST (' -.702E+1' AS NUMERIC (5, 2)));");
		// PASS:0647 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO USER_INPUT VALUES "
				+ "(CAST ('3' AS SMALLINT), CAST (-7.02 AS CHAR (5)), "
				+ "CAST (' -.702E+1' AS DECIMAL (3, 2)));");
		// PASS:0647 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT USER_ID " + "FROM USER_INPUT "
				+ "GROUP BY USER_ID, USER_TYPED, CASH_BALANCE "
				+ "HAVING COUNT(*) = 2; ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0647 If 1 row selected and USER_ID = 3?

		stmt.executeUpdate("DROP TABLE USER_INPUT ;");

		// END TEST >>> 0647 <<< END TEST
	}

	/*
	 * 
	 * testDml_121_ExplicitDefaultsStatic
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_121_ExplicitDefaultsStatic() throws SQLException {

		// this test is for explicit use of DEFAULT keyword inside of insert
		// statements. Firebird doesn't seem to support this - firebird only
		// seems to support DEFAULT on table creation.
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		stmt.executeUpdate("CREATE TABLE SSSLOG ( "
				+ "ENTERED_BY CHAR (128) DEFAULT USER,"
				+ "SEVERITY INT DEFAULT 1,"
				+ "PROBLEM CHAR (40) DEFAULT NULL); ");
		// PASS:0649 If table is created?
		stmt.executeUpdate("INSERT INTO SSSLOG DEFAULT VALUES;");
		// PASS:0649 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO SSSLOG VALUES "
				+ " (DEFAULT, DEFAULT, DEFAULT); ");
		// PASS:0649 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO SSSLOG VALUES "
				+ "(DEFAULT, 3, 'Cross-linked inode');");
		// PASS:0649 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO SSSLOG VALUES "
				+ "('system', DEFAULT, 'Freed a free frag'); ");
		// PASS:0649 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO SSSLOG VALUES "
				+ "('nobody', 6, DEFAULT); ");
		// PASS:0649 If 1 row is inserted?

		stmt.executeUpdate("UPDATE SSSLOG SET SEVERITY = DEFAULT "
				+ "WHERE PROBLEM LIKE '%inode%'; ");
		// PASS:0649 If 1 row is updated?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SSSLOG WHERE "
				+ "ENTERED_BY = 'FLATER' AND SEVERITY = 1 "
				+ "AND PROBLEM IS NULL; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0649 If count = 2?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SSSLOG WHERE "
				+ "ENTERED_BY = 'FLATER' AND SEVERITY = 1 "
				+ "AND PROBLEM = 'Cross-linked inode';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0649 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SSSLOG WHERE "
				+ "ENTERED_BY = 'system' AND SEVERITY = 1 "
				+ "AND PROBLEM = 'Freed a free frag'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0649 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM SSSLOG WHERE "
				+ "ENTERED_BY = 'nobody' AND SEVERITY = 6 "
				+ "AND PROBLEM IS NULL; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0649 If count = 1?

		stmt.executeUpdate("DROP TABLE SSSLOG ;");

		// END TEST >>> 0649 <<< END TEST
	}
	/*
	 * 
	 * testDml_121_KeywordRelaxationsStatic
	 * 
	 * Notes:TEST:0651 Feature 24, Keyword relaxations (static)!
	 *  
	 */
	public void testDml_121_KeywordRelaxationsStatic() throws SQLException {

		BaseTab.setupBaseTab(stmt);

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE VIEW VERBOSE_PEOPLE AS "
					+ "SELECT EMPNAME FROM STAFF " + "WHERE staff.empname  IN "
					+ "(SELECT EMPNUM FROM PROJ , WORKS "
					+ "WHERE PTYPE = 'Design' "
					+ "AND proj.pnum = WORKS.PNUM);");
		else
			stmt.executeUpdate("CREATE VIEW VERBOSE_PEOPLE AS "
					+ "SELECT EMPNAME FROM STAFF AS EMPLOYEES_OF_HU "
					+ "WHERE EMPLOYEES_OF_EMPNUM IN "
					+ "(SELECT EMPNUM FROM PROJ AS HUPROJ, WORKS "
					+ "WHERE PTYPE = 'Design' "
					+ "AND HUPROJ.PNUM = WORKS.PNUM);");
		// PASS:0651 If view is created?

		//		stmt.executeUpdate("GRANT SELECT ON TABLE "+
		//		 "VERBOSE_PEOPLE TO PUBLIC;");
		// PASS:0651 If successful completion?

		stmt.executeUpdate("DROP VIEW VERBOSE_PEOPLE ");
		// END TEST >>> 0651 <<< END TEST

		// *********************************************
		// TEST:0661 Errata: datetime casting (static)!
		// do we support INTERVAL types?
		boolean supportsIntervalType = false;
		rs = conn.getMetaData().getTypeInfo();
		while (rs.next()) {
			if (rs.getString(1).toUpperCase().matches(".*INTERVAL.*"))
				supportsIntervalType = true;
		}

		if (supportsIntervalType) {
			stmt
					.executeUpdate("CREATE TABLE LOTSA_DATETIMES ( "
							+ "C1 DATE, C2 TIME, C3 TIMESTAMP, "
							+ "C4 INTERVAL YEAR, C5 INTERVAL MONTH, C6 INTERVAL DAY, "
							+ "C7 INTERVAL HOUR, C8 INTERVAL MINUTE, C9 INTERVAL SECOND, "
							+ "C10 INTERVAL YEAR TO MONTH, "
							+ "C11 INTERVAL DAY TO HOUR, "
							+ "C12 INTERVAL DAY TO MINUTE, "
							+ "C13 INTERVAL DAY TO SECOND, "
							+ "C14 INTERVAL HOUR TO MINUTE, "
							+ "C15 INTERVAL HOUR TO SECOND, "
							+ "C16 INTERVAL MINUTE TO SECOND);");
			// PASS:0661 If table is created?

			stmt.executeUpdate("INSERT INTO LOTSA_DATETIMES VALUES ( "
					+ "CAST ('1976-06-21' AS DATE), "
					+ "CAST ('13:24:00' AS TIME), "
					+ "CAST ('1927-11-30 07:10:00' AS TIMESTAMP), "
					+ "CAST ('-1' AS INTERVAL YEAR), "
					+ "CAST ('+2' AS INTERVAL MONTH), "
					+ "CAST ('-3' AS INTERVAL DAY), "
					+ "CAST ('4' AS INTERVAL HOUR), "
					+ "CAST ('-5' AS INTERVAL MINUTE), "
					+ "CAST ('6.333333' AS INTERVAL SECOND), "
					+ "CAST ('-5-11' AS INTERVAL YEAR TO MONTH), "
					+ "CAST ('2 15' AS INTERVAL DAY TO HOUR), "
					+ "CAST ('-3 4:05' AS INTERVAL DAY TO MINUTE), "
					+ "CAST ('+6 17:08:09' AS INTERVAL DAY TO SECOND), "
					+ "CAST ('-10:45' AS INTERVAL HOUR TO MINUTE), "
					+ "CAST ('11:23:45.75' AS INTERVAL HOUR TO SECOND), "
					+ "CAST ('-20:00' AS INTERVAL MINUTE TO SECOND));");
			// PASS:0661 If 1 row is inserted?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM LOTSA_DATETIMES "
					+ "WHERE C1 = DATE '1976-06-21' AND "
					+ "C2 = TIME '13:24:00' AND "
					+ "C3 = TIMESTAMP '1927-11-30 07:10:00.000000' AND "
					+ "C4 = INTERVAL -'1' YEAR AND "
					+ "C5 = INTERVAL -'-2' MONTH AND "
					+ "C6 = INTERVAL '-3' DAY AND "
					+ "C7 = INTERVAL '4' HOUR AND "
					+ "C8 = INTERVAL -'5' MINUTE AND "
					+ "C9 = INTERVAL '6.333333' SECOND AND "
					+ "C10 = INTERVAL -'5-11' YEAR TO MONTH AND "
					+ "C11 = INTERVAL +'2 15' DAY TO HOUR AND "
					+ "C12 = INTERVAL '-3 4:05' DAY TO MINUTE AND "
					+ "C13 = INTERVAL '+6 17:08:09.000000' DAY TO SECOND AND "
					+ "C14 = INTERVAL '-10:45' HOUR TO MINUTE AND "
					+ "C15 = INTERVAL '11:23:45.750000' HOUR TO SECOND;");
			// PASS:0661 If count = 1?

			stmt.executeUpdate("DROP TABLE LOTSA_DATETIMES ;");

			// END TEST >>> 0661 <<< END TEST

			// *********************************************

			// TEST:0663 Errata: datetime SQLSTATEs (static)!

			stmt.executeUpdate("CREATE TABLE WOODCHUCK ( "
					+ "OBSERVATION DATE, "
					+ "WOOD_AGE INTERVAL YEAR TO MONTH);");
			// PASS:0663 If table is created?

			try {
				stmt.executeUpdate("INSERT INTO WOODCHUCK VALUES ( "
						+ "CAST ('1994-02-30' AS DATE), NULL); ");
				fail();
				// PASS:0663 If ERROR, invalid datetime format, 0 rows
				// inserted?
			} catch (SQLException sqle) {
			}

			try {
				stmt.executeUpdate("INSERT INTO WOODCHUCK VALUES ( "
						+ "NULL, CAST ('1-12' AS INTERVAL YEAR TO MONTH)); ");
				fail();
				// PASS:0663 If ERROR, invalid interval format, 0 rows
				// inserted?
			} catch (SQLException sqle) {
			}

			stmt.executeUpdate("DROP TABLE WOODCHUCK;");

			// END TEST >>> 0663 <<< END TEST
		}
	}
	/*
	 * 
	 * testDml_130_DataTypeSemanticsWithNullNotNull
	 * 
	 * Notes: Only the 1st part of DML_130 was usable. The rest of the test used
	 * the INFORMATION_SCHEMA views heavily.
	 *  
	 */
	public void testDml_130_DataTypeSemanticsWithNullNotNull()
			throws SQLException {
		// TEST:0678 Data type semantics with NULL / NOT NULL!

		stmt.executeUpdate("CREATE TABLE CH1 ( "
				+ "CH1A CHARACTER (10) NOT NULL, "
				+ "CH1B CHARACTER NOT NULL, " + "CH1C CHAR (10) NOT NULL);");
		// PASS:0678 If table is created?

		stmt.executeUpdate("CREATE TABLE NUM1 ( "
				+ "NUM1C1 NUMERIC (3, 2) NOT NULL, "
				+ "NUM1C2 NUMERIC (2) NOT NULL, "
				+ "NUM1C3 NUMERIC NOT NULL); ");
		// PASS:0678 If table is created?

		stmt.executeUpdate("INSERT INTO CH1 VALUES ('FOO', '', '0123456789');");
		// PASS:0678 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CH1A, CH1B, CH1C " + " FROM CH1; ");
		rs.next();
		assertEquals("FOO       ", rs.getString(1));
		assertEquals(" ", rs.getString(2));
		assertEquals("0123456789", rs.getString(3));
		assertFalse(rs.next());
		// PASS:0678 If 1 row selected and CH1A = 'FOO '?
		// PASS:0678 AND CH1B = ' ' and CH1C = '0123456789'?

		// NOTE:0678 One subtest deleted.

		try {
			stmt.executeUpdate("INSERT INTO CH1 VALUES ('FOO', "
					+ "'F', 'LITTLETOOLONG');");
			fail();
			// PASS:0678 If ERROR, string data, right truncation, 0 rows
			// selected?
		} catch (SQLException sqle) {
		}

		assertEquals(1, stmt.executeUpdate("DELETE FROM CH1;"));

		assertEquals(1, stmt.executeUpdate("INSERT INTO CH1 VALUES ('FOO', "
				+ "'F', 'BLANKS       ');"));
		// PASS:0678 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT CH1A, CH1B, CH1C " + "FROM CH1; ");
		rs.next();
		assertEquals("FOO       ", rs.getString(1));
		assertEquals("F", rs.getString(2));
		assertEquals("BLANKS    ", rs.getString(3));
		// PASS:0678 If 1 row selected and CH1A = 'FOO '?
		// PASS:0678 AND CH1B = 'F' and CH1C = 'BLANKS '?

		stmt.executeUpdate("INSERT INTO NUM1 VALUES (9.99, -99, 9);");
		// PASS:0678 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT NUM1C1 * 100, NUM1C2, NUM1C3 "
				+ "FROM NUM1;");
		rs.next();
		assertEquals(999, rs.getInt(1));
		assertEquals(-99, rs.getInt(2));
		assertEquals(9, rs.getInt(3));
		assertFalse(rs.next());
		// PASS:0678 If 1 row selected and values are 999, -99, 9?

		stmt.executeUpdate("DELETE FROM NUM1;");

		//			   try {
		//			   	stmt.executeUpdate("INSERT INTO NUM1 VALUES (-10, 0, 0);");
		//			   	fail();
		//			   // PASS:0678 If ERROR, string data, numeric value out of range?
		//			// PASS:0678 AND 0 rows inserted?
		//			   } catch (SQLException sqle) {}

		//			   try {
		//			   	stmt.executeUpdate("INSERT INTO NUM1 VALUES (0, 100, 0);");
		//			   	fail();
		//			// PASS:0678 If ERROR, string data, numeric value out of range?
		//			// PASS:0678 AND 0 rows inserted?
		//			   } catch (SQLException sqle) {}

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO NUM1 VALUES (0, 0, 0.1); "));
		// PASS:0678 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT NUM1C1 * 100, NUM1C2, NUM1C3 "
				+ "FROM NUM1;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertFalse(rs.next());
		// PASS:0678 If 1 row selected and values are 0, 0, 0?

		stmt.executeUpdate("DROP TABLE NUM1 ;");
		stmt.executeUpdate("DROP TABLE CH1 ;");

		// END TEST >>> 0678 <<< END TEST
	}

	// test_dml_131 skipped - no support for information_schema views
	// TEST:0683 INFO_SCHEM: Changes are visible!

	/*
	 * 
	 * testDml_132_FipsSizingNumericPrecision
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_132_FipsSizingNumericPrecision() throws SQLException {

		// TEST:0515 FIPS sizing: NUMERIC (15) decimal precision!
		stmt.executeUpdate("CREATE TABLE P15 (NUMTEST  NUMERIC(15));");
		stmt.executeUpdate("INSERT INTO P15 VALUES (999999999999999);");
		// PASS:0515 If 1 row is inserted?

		stmt.executeUpdate("INSERT INTO P15 VALUES (-999999999999999);");
		// PASS:0515 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT NUMTEST - 999999999999990, "
				+ "NUMTEST / 9999999 " + "FROM P15 WHERE NUMTEST > 0;");
		rs.next();
		assertTrue((8.9 <= rs.getDouble(1)) && (9.1 >= rs.getDouble(1)));
		assertTrue((100000009 <= rs.getDouble(2))
				&& (100000011 >= rs.getDouble(2)));
		// PASS:0515 If 1 row selected and values are 9 and 100000010 +- 1?

		rs = stmt.executeQuery("SELECT NUMTEST + 999999999999990, "
				+ "NUMTEST / 9999999 " + "FROM P15 WHERE NUMTEST < 0; ");
		rs.next();
		assertTrue((-9.1 <= rs.getDouble(1)) && (-8.9 >= rs.getDouble(1)));
		assertTrue((-100000011 <= rs.getDouble(2))
				&& (-100000009 >= rs.getDouble(2)));
		// PASS:0515 If 1 row selected and values are -9 and -100000010 +- 1?

		stmt.executeUpdate("DELETE FROM P15;");
		// PASS:0515 If 2 rows are deleted?

		stmt.executeUpdate("INSERT INTO P15 VALUES (562949953421313);");
		// PASS:0515 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM P15 WHERE NUMTEST = 562949953421312;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0515 If count = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM P15 WHERE NUMTEST = 562949953421313;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0515 If count = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM P15 WHERE NUMTEST = 562949953421314;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0515 If count = 0?
		// END TEST >>> 0515 <<< END TEST

		// *********************************************

		// TEST:0524 FIPS sizing: 100 Items in a SELECT list!

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

		stmt
				.executeUpdate("INSERT INTO T100 VALUES ('00', '01', '02', "
						+ "'03', '04', '05', '06', '07', '08', '09', '0a', '0b', '0c', "
						+ "'0d', '0e', '0f', '10', '11', '12', '13', '14', '15', '16', "
						+ "'17', '18', '19', '1a', '1b', '1c', '1d', '1e', '1f', '20', "
						+ "'21', '22', '23', '24', '25', '26', '27', '28', '29', '2a', "
						+ "'2b', '2c', '2d', '2e', '2f', '30', '31', '32', '33', '34', "
						+ "'35', '36', '37', '38', '39', '3a', '3b', '3c', '3d', '3e', "
						+ "'3f', '40', '41', '42', '43', '44', '45', '46', '47', '48', "
						+ "'49', '4a', '4b', '4c', '4d', '4e', '4f', '50', '51', '52', "
						+ "'53', '54', '55', '56', '57', '58', '59', '5a', '5b', '5c', "
						+ "'5d', '5e', '5f', '60', '61', '62', '63');");
		// PASS:0524 If 1 row is inserted?

		rs = stmt
				.executeQuery("SELECT "
						+ "C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, C11, C12, C13, C14, "
						+ "C15, C16, C17, C18, C19, C20, C21, C22, C23, C24, C25, C26, "
						+ "C27, C28, C29, C30, C31, C32, C33, C34, C35, C36, C37, C38, "
						+ "C39, C40, C41, C42, C43, C44, C45, C46, C47, C48, C49, C50, "
						+ "C51, C52, C53, C54, C55, C56, C57, C58, C59, C60, C61, C62, "
						+ "C63, C64, C65, C66, C67, C68, C69, C70, C71, C72, C73, C74, "
						+ "C75, C76, C77, C78, C79, C80, C81, C82, C83, C84, C85, C86, "
						+ "C87, C88, C89, C90, C91, C92, C93, C94, C95, C96, C97, C98, "
						+ "C99, C100 " + "FROM T100;");
		rs.next();
		assertEquals("00", rs.getString(1));
		assertEquals("31", rs.getString(50));
		assertEquals("42", rs.getString(67));
		assertEquals("63", rs.getString(100));
		assertFalse(rs.next());
		// PASS:0524 If 1 row selected?
		// PASS:0524 AND C1 is '00'?
		// PASS:0524 AND C50 is '31'?
		// PASS:0524 AND C67 is '42'?
		// PASS:0524 AND C100 is '63'?
		// END TEST >>> 0524 <<< END TEST

		// *********************************************

		// TEST:0525 FIPS sizing: 15 Table references in SQL statement!

		stmt.executeUpdate("CREATE TABLE BASE_WCOV (C1 INT);");

		stmt.executeUpdate("INSERT INTO BASE_WCOV VALUES (1);");
		// PASS:0525 If 1 row is inserted?

		BaseTab.setupBaseTab(stmt);
		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE U_SIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE BASE_VS1 (C1 INT, C2 INT);");
		stmt
				.executeUpdate("CREATE VIEW VS1 AS SELECT * FROM BASE_VS1 WHERE C1 = 0;");
		stmt.executeUpdate("CREATE VIEW VS2 AS "
				+ "SELECT A.C1 FROM BASE_VS1 A WHERE EXISTS "
				+ "(SELECT B.C2 FROM BASE_VS1 B WHERE B.C2 = A.C1);");
		stmt.executeUpdate("CREATE VIEW VSTAFF3 AS SELECT * FROM STAFF3;");
		assertEquals(3, stmt
				.executeUpdate("DELETE FROM STAFF WHERE EMPNUM > 'E2';"));
		// PASS:0525 If 3 rows are deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM "
				+ "WORKS T01, PROJ T02, STAFF T03, "
				+ "USIG T04, U_SIG T05, BASE_VS1 T06, VS1 T07, "
				+ "VS2 T08, VSTAFF3 T09, BASE_WCOV T10 "
				+ "WHERE T03.EMPNUM > 'E1';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0525 If count = 46080?

		// END TEST >>> 0525 <<< END TEST
		// *************************************************////END-OF-MODULE
	}
	// test_dml_133 skipped - no support for schemas
	// TEST:0643 Feature 17, Multiple schemas per user!

}