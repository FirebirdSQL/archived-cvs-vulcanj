/* $Id$ */
/*
 * Author: sasjwm
 * Created on: Aug 26, 2004
 * 
 * This is the 3rd part of the DML series. It was broken into pieces 
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
public class TestDml3 extends NistTestBase {
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

	public TestDml3(String arg0) {
		super(arg0);
	}

	/*
	 * 
	 * testDml_134_FipsSizingNumericPrecision
	 * 
	 * Notes: The 2nd and 3rd parts part of this test TEST:0690 Many Trans SQL
	 * features #2: talk show schedule! and TEST:0691 INFO_SCHEM: SQLSTATEs for
	 * length overruns! were not implemented since they involve interval times
	 * and information schema.
	 *  
	 */
	public void testDml_134_FipsSizingNumericPrecision() throws SQLException {
		// TEST:0689 Many Trans SQL features #1: inventory system!

		stmt
				.executeUpdate("CREATE TABLE COST_CODES ( "
						+ "COSTCODE INT UNIQUE, "
						+ "COSTTEXT VARCHAR (50) NOT NULL); ");
		// PASS:0689 If table is created?

		stmt
				.executeUpdate("CREATE TABLE CONDITION_CODES ( "
						+ "CONDCODE INT UNIQUE, "
						+ "CONDTEXT VARCHAR (50) NOT NULL); ");
		// PASS:0689 If table is created?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE ITEM_CODES ( "
					+ "ITEMCODE INT NOT NULL PRIMARY KEY,"
					+ "ITEMTEXT VARCHAR (50) NOT NULL);");
		else
			stmt.executeUpdate("CREATE TABLE ITEM_CODES ( "
					+ "ITEMCODE INT PRIMARY KEY,"
					+ "ITEMTEXT VARCHAR (50) NOT NULL);");
		// PASS:0689 If table is created?

		stmt.executeUpdate("CREATE TABLE INVENTORY ( "
				+ "COSTCODE INT REFERENCES COST_CODES (COSTCODE), "
				+ "CONDCODE INT REFERENCES CONDITION_CODES (CONDCODE), "
				+ "ITEMCODE INT REFERENCES ITEM_CODES);");
		// PASS:0689 If table is created?

		// FB SQL doesn't have natural join
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE VIEW COMPLETES AS "
					+ "SELECT ITEMTEXT, CONDTEXT, COSTTEXT "
					+ "FROM INVENTORY NATURAL JOIN COST_CODES "
					+ "NATURAL JOIN CONDITION_CODES "
					+ "NATURAL JOIN ITEM_CODES; ");
			// PASS:0689 If view is created?
			stmt
					.executeUpdate("CREATE VIEW VERBOSE_INV AS "
							+ "SELECT * FROM COMPLETES UNION SELECT * FROM INCOMPLETES;");
			// PASS:0689 If view is created?
		}

		stmt.executeUpdate("CREATE VIEW INCOMPLETES AS "
				+ "SELECT ITEMTEXT, CONDTEXT, COSTTEXT "
				+ "FROM INVENTORY, COST_CODES, CONDITION_CODES, ITEM_CODES "
				+ "WHERE INVENTORY.ITEMCODE = ITEM_CODES.ITEMCODE "
				+ "AND ((INVENTORY.CONDCODE = CONDITION_CODES.CONDCODE "
				+ "AND INVENTORY.COSTCODE IS NULL "
				+ "AND COST_CODES.COSTCODE IS NULL) "
				+ "OR (INVENTORY.COSTCODE = COST_CODES.COSTCODE "
				+ "AND INVENTORY.CONDCODE IS NULL "
				+ "AND CONDITION_CODES.CONDCODE IS NULL));");
		// PASS:0689 If view is created?

		// rest of these functions require TRIM operator
		if (conn.getMetaData().getStringFunctions().toUpperCase().matches(
				".*TRIM.*")) {
			stmt
					.executeUpdate("INSERT INTO COST_CODES VALUES ( "
							+ "NULL, "
							+ "TRIM ('No cost code assigned                             '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO COST_CODES VALUES ("
							+ "0, "
							+ "TRIM ('Expensive                                         '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO COST_CODES VALUES ( "
							+ "1,  "
							+ "TRIM ('Absurdly expensive                                '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO COST_CODES VALUES ( "
							+ "2, "
							+ "TRIM ('Outrageously expensive                            '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO COST_CODES VALUES ( "
							+ "3, "
							+ "TRIM ('Robbery; a complete and total rip-off             '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO CONDITION_CODES VALUES ( "
							+ "NULL, "
							+ "TRIM ('Unknown                                           '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO CONDITION_CODES VALUES ( "
							+ "1, "
							+ "TRIM ('Slightly used                                     '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO CONDITION_CODES VALUES ( "
							+ "2,"
							+ "TRIM ('Returned as defective                             '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO CONDITION_CODES VALUES ( "
							+ "3, "
							+ "TRIM ('Visibly damaged (no returns)                      '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO ITEM_CODES VALUES ( "
							+ "1, "
							+ "TRIM ('Lousy excuse for a tape deck                      ')); ");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO ITEM_CODES VALUES ( "
							+ "3, "
							+ "TRIM ('World''s worst VCR                                 '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO ITEM_CODES VALUES ( "
							+ "4, "
							+ "TRIM ('Irreparable intermittent CD player                '));");
			// PASS:0689 If 1 row is inserted?

			stmt
					.executeUpdate("INSERT INTO ITEM_CODES VALUES ( "
							+ "7, "
							+ "TRIM ('Self-destruct VGA monitor w/ critical need detect '));");
			// PASS:0689 If 1 row is inserted?

			stmt.executeUpdate("INSERT INTO INVENTORY VALUES (3, NULL, 4);");
			// PASS:0689 If 1 row is inserted?

			stmt.executeUpdate(" INSERT INTO INVENTORY VALUES (1, 2, 3);");
			// PASS:0689 If 1 row is inserted?

			stmt.executeUpdate("INSERT INTO INVENTORY VALUES (2, 3, 7);");
			// PASS:0689 If 1 row is inserted?

			stmt.executeUpdate("INSERT INTO INVENTORY VALUES (0, 3, 1);");
			// PASS:0689 If 1 row is inserted?

			stmt.executeUpdate("INSERT INTO INVENTORY VALUES (3, 1, 7);");
			// PASS:0689 If 1 row is inserted?

			rs = stmt.executeQuery("SELECT COUNT(*) FROM INCOMPLETES;");
			rs.next();
			assertEquals(1, rs.getInt(1));
			// PASS:0689 If count = 1?

			if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				rs = stmt.executeQuery("SELECT COUNT(*) FROM COMPLETES;");
				rs.next();
				assertEquals(4, rs.getInt(1));
				// PASS:0689 If count = 4?

				rs = stmt.executeQuery("SELECT COUNT(*) FROM VERBOSE_INV; ");
				rs.next();
				assertEquals(5, rs.getInt(1));
				// PASS:0689 If count = 5?

				rs = stmt
						.executeQuery("SELECT COUNT(*) FROM VERBOSE_INV "
								+ "WHERE ITEMTEXT = 'Irreparable intermittent CD player' "
								+ "AND CONDTEXT = 'Unknown' "
								+ "AND COSTTEXT = 'Robbery; a complete and total rip-off'; ");
				// PASS:0689 If count = 1?

				rs = stmt.executeQuery("SELECT COUNT(*) FROM VERBOSE_INV "
						+ "WHERE ITEMTEXT = 'Lousy excuse for a tape deck' "
						+ "AND CONDTEXT = 'Visibly damaged (no returns)' "
						+ "AND COSTTEXT = 'Expensive'; ");
				// PASS:0689 If count = 1?

				rs = stmt
						.executeQuery("SELECT COUNT(*) FROM VERBOSE_INV "
								+ "WHERE ITEMTEXT = "
								+ "'Self-destruct VGA monitor w/ critical need detect' "
								+ "AND CONDTEXT = 'Slightly used' "
								+ "AND COSTTEXT = 'Robbery; a complete and total rip-off';");
				// PASS:0689 If count = 1?

				rs = stmt
						.executeQuery("SELECT COUNT(*) FROM VERBOSE_INV "
								+ "WHERE ITEMTEXT = "
								+ "'Self-destruct VGA monitor w/ critical need detect' "
								+ "AND CONDTEXT = 'Visibly damaged (no returns)' "
								+ "AND COSTTEXT = 'Outrageously expensive';");
				// PASS:0689 If count = 1?

				rs = stmt.executeQuery("SELECT COUNT(*) FROM VERBOSE_INV "
						+ "WHERE ITEMTEXT = 'World''s worst VCR' "
						+ "AND CONDTEXT = 'Returned as defective' "
						+ "AND COSTTEXT = 'Absurdly expensive';");
				// PASS:0689 If count = 1?
			}
		}
		// END TEST >>> 0689 <<< END TEST
	}
	/*
	 * 
	 * testDml_135_FipsSizingNumericPrecision
	 * 
	 * Notes: Many TSQL features #3: enhanced proj/works! Note that the 2nd and
	 * third part of this test were not implemented, as they use information
	 * schema and interval arithmetic.
	 *  
	 */
	public void testDml_135_EnhancedProjWorks() throws SQLException {
		// TEST:0692 Many TSQL features #3: enhanced proj/works!

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE \"Proj\" ( "
					+ "PNUM     CHAR(3) NOT NULL PRIMARY KEY, "
					+ "PNAME    CHAR(20), "
					+ "PTYPE    CHAR(6)    DEFAULT 'Code', "
					+ "BUDGET   DECIMAL(9) DEFAULT 10000, "
					+ "CITY     CHAR(15)   DEFAULT 'Berlin');");
		else
			stmt.executeUpdate("CREATE TABLE \"Proj\" ( "
					+ "PNUM     CHAR(3) PRIMARY KEY, " + "PNAME    CHAR(20), "
					+ "PTYPE    CHAR(6)    DEFAULT 'Code', "
					+ "BUDGET   DECIMAL(9) DEFAULT 10000, "
					+ "CITY     CHAR(15)   DEFAULT 'Berlin');");
		// PASS:0692 If table is created?

		stmt
				.executeUpdate("CREATE VIEW \"PTypes\" (\"TYPE\", NUM) AS "
						+ " SELECT PTYPE, COUNT(*) FROM \"Proj\" "
						+ " GROUP BY PTYPE;");
		// PASS:0692 If view is created?

		stmt.executeUpdate(" CREATE VIEW PTYPES AS "
				+ "SELECT * FROM \"PTypes\" " + "WHERE NUM > 1;");
		// PASS:0692 If view is created?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE \"Works\" ( "
					+ "EMPNUM   CHAR(3) NOT NULL, "
					+ "PNUM     CHAR(3) NOT NULL "
					+ "REFERENCES \"Proj\" ON DELETE CASCADE, "
					+ "HOURS    DECIMAL(5), " + "PRIMARY KEY (EMPNUM,PNUM)); ");
		else
			stmt.executeUpdate("CREATE TABLE \"Works\" ( "
					+ "EMPNUM   CHAR(3), " + "PNUM     CHAR(3) "
					+ "REFERENCES \"Proj\" ON DELETE CASCADE, "
					+ "HOURS    DECIMAL(5), " + "PRIMARY KEY (EMPNUM,PNUM)); ");
		// PASS:0692 If table is created?

		stmt.executeUpdate("CREATE VIEW \"PStaff\" (PNUM, NUM) AS "
				+ "SELECT PNUM, COUNT(*) FROM \"Works\" "
				+ "WHERE HOURS >= 20 " + "GROUP BY PNUM; ");
		// PASS:0692 If view is created?

		BaseTab.setupBaseTab(stmt);
		assertEquals(6, stmt.executeUpdate("INSERT INTO \"Proj\" "
				+ "SELECT * FROM Proj; "));
		// PASS:0692 If 6 rows are inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO \"Proj\" (PNUM, PNAME, BUDGET) "
						+ "VALUES ('P7', 'FROB', 10000);"));
		// PASS:0692 If 1 row is inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO \"Proj\" (PNUM, PNAME, BUDGET) "
						+ "VALUES ('P8', 'BORF', 15000); "));
		// PASS:0692 If 1 row is inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO \"Proj\" (PNUM, PNAME, PTYPE) "
						+ "VALUES ('P9', 'FORB', 'Code'); "));
		// PASS:0692 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO \"Proj\" VALUES "
				+ "('P10', 'ROBF', 'Docs', 1000, 'Sofia');"));
		// PASS:0692 If 1 row is inserted?

		assertEquals(12, stmt.executeUpdate("INSERT INTO \"Works\" "
				+ "SELECT * FROM Works;"));
		// PASS:0692 If 12 rows are inserted?

		rs = stmt.executeQuery("SELECT * FROM PTYPES ORDER BY NUM;");
		rs.next();
		assertEquals("Test", rs.getString(1).trim());
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals("Design", rs.getString(1));
		assertEquals(3, rs.getInt(2));
		rs.next();
		assertEquals("Code", rs.getString(1).trim());
		assertEquals(4, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0692 If 3 rows selected with ordered rows and column values ?
		// PASS:0692 Test 2 ?
		// PASS:0692 Design 3 ?
		// PASS:0692 Code 4 ?

		rs = stmt.executeQuery("SELECT NUM, COUNT(*) FROM \"PStaff\" "
				+ "GROUP BY NUM ORDER BY NUM;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals(4, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0692 If 3 rows selected with ordered rows and column values ?
		// PASS:0692 1 2 ?
		// PASS:0692 2 2 ?
		// PASS:0692 4 1 ?

		assertEquals(3, stmt
				.executeUpdate("DELETE FROM \"Proj\" WHERE PTYPE = 'Design';"));
		// PASS:0692 If 3 rows from "Proj" and 5 rows from "Works" are deleted?

		rs = stmt.executeQuery("SELECT * FROM PTYPES ORDER BY NUM;");
		rs.next();
		assertEquals("Test", rs.getString(1).trim());
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals("Code", rs.getString(1).trim());
		assertEquals(4, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0692 If 2 rows selected with ordered rows and column values ?
		// PASS:0692 Test 2 ?
		// PASS:0692 Code 4 ?

		rs = stmt.executeQuery("SELECT NUM, COUNT(*) FROM \"PStaff\" "
				+ "GROUP BY NUM ORDER BY NUM; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals(4, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0692 If 2 rows selected with ordered rows and column values ?
		// PASS:0692 1 2 ?
		// PASS:0692 4 1 ?

		// END TEST >>> 0692 <<< END TEST

	}
	// test_dml_136 skipped - no support for interval types
	// TEST:0696 Many TSQL features #5: Video Game Scores!
	/*
	 * 
	 * testDml_137_DropBehaviourConstraints
	 * 
	 * Notes: This test was implemented, but in the firebird case there's not
	 * much to do since we only support a simple "DROP TABLE" without restrict
	 * option. Many TSQL features #3: enhanced proj/works! Note that the 2nd and
	 * third part of this test were not implemented, as they use information
	 * schema and interval arithmetic.
	 *  
	 */
	public void testDml_137_DropBehaviourConstraints() throws SQLException {
		// TEST:0697 Erratum: drop behavior, constraints (static)!

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("CREATE TABLE UNDROPPABLE ( "
					+ "C1 INT NOT NULL PRIMARY KEY ); ");
			// PASS:0697 If table is created?
			stmt.executeUpdate("CREATE TABLE DROPPABLE ( "
					+ "C1 INT NOT NULL PRIMARY KEY REFERENCES UNDROPPABLE, "
					+ "C2 INT, CHECK (C1 < C2));");
		} else {
			stmt.executeUpdate("CREATE TABLE UNDROPPABLE ( "
					+ "C1 INT PRIMARY KEY NOT NULL); ");
			// PASS:0697 If table is created?
			stmt.executeUpdate("CREATE TABLE DROPPABLE ( "
					+ "C1 INT PRIMARY KEY NOT NULL REFERENCES UNDROPPABLE, "
					+ "C2 INT, CHECK (C1 < C2));");
			// PASS:0697 If table is created?
		}
		try {
			stmt.executeUpdate("DROP TABLE UNDROPPABLE ; ");
			fail();
			// PASS:0697 If ERROR, syntax error/access violation, table not
			// dropped?
		} catch (SQLException sqle) {
		}

		// stmt.executeUpdate("DROP TABLE DROPPABLE ;");
		// PASS:0697 If table is dropped?

		// stmt.executeUpdate("DROP TABLE UNDROPPABLE ;");
		// PASS:0697 If table is dropped?

		// END TEST >>> 0697 <<< END TEST

		// *************************************************////END-OF-MODULE
	}

	// dml139 is a grant/revoke test, testDml_139_DropBehaviourOnRevoke

	/*
	 * 
	 * testDml_141_SqlStateIntegrityConstraintViolation
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_141_SqlStateIntegrityConstraintViolation()
			throws SQLException {

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

		// TEST:0514 SQLSTATE 23000: integrity constraint violation!
		stmt.executeUpdate("CREATE TABLE STAFF "
				+ "(EMPNUM   CHAR(3) NOT NULL UNIQUE, " + "EMPNAME  CHAR(20), "
				+ "GRADE    DECIMAL(4), " + "CITY     CHAR(15));");
		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E1','Alice',12,'Deale');");

		// NOT NULL constraint violated
		try {
			stmt.executeUpdate("INSERT INTO STAFF VALUES ("
					+ "NULL, NULL, NULL, NULL);");
			fail();
			// PASS:0514 If ERROR, integrity constraint violation, 0 rows
			// inserted?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				String fbError = "GDS Exception. 335544347. validation error for column EMPNUM, value \"*** null ***\""; 
				assertEquals(fbError, sqle.getMessage().substring(0, fbError.length())); 
			}
		}

		// UNIQUE constraint violated
		try {
			stmt.executeUpdate("INSERT INTO STAFF VALUES ("
					+ "'E1', 'Agnetha', 12, 'Paris');");
			// PASS:0514 If ERROR, integrity constraint violation, 0 rows
			// inserted?
			fail();
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				String errMsg = "GDS Exception. 335544665. violation of "
						+ "PRIMARY or UNIQUE KEY constraint";
				assertEquals(errMsg.substring(0, errMsg.length()), sqle
						.getMessage().substring(0, errMsg.length()));
			}
		}

		// END TEST >>> 0514 <<<
	}

	/*
	 * 
	 * testDml_144_LengthExpressionStatic()
	 * 
	 * Notes: TEST:0834 <length expression> (static)!
	 *  
	 */
	public void testDml_144_LengthExpressionStatic() throws SQLException {
		String myStringFunctions = conn.getMetaData().getStringFunctions()
				.toUpperCase();

		// database must support char_length, character_length, and
		// octet_length. Firebird doesn't support any of these, by the way
		if (myStringFunctions.matches(".*CHAR_LENGTH.*")
				&& myStringFunctions.matches(".*CHARACTER_LENGTH.*")
				&& myStringFunctions.matches(".*OCTET_LENGTH.*")) {

			stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2)); ");
			stmt.executeUpdate("CREATE TABLE GRUB (C1 VARCHAR (10));");
			// PASS:0834 If table is created?
			BaseTab.setupStaff(stmt);

			rs = stmt.executeQuery("SELECT CHAR_LENGTH (EMPNAME) "
					+ "FROM STAFF WHERE GRADE = 10;");
			rs.next();
			assertEquals(10, rs.getInt(1));
			// PASS:0834 If 1 row selected and value is 20?

			rs = stmt.executeQuery("SELECT CHARACTER_LENGTH ('HI' || 'THERE') "
					+ "FROM ECCO;");
			rs.next();
			assertEquals(7, rs.getInt(1));
			// PASS:0834 If 1 row selected and value is 7?

			stmt.executeUpdate("INSERT INTO GRUB VALUES ('Hi  ');");
			// PASS:0834 If 1 row is inserted?

			rs = stmt.executeQuery("SELECT CHARACTER_LENGTH (C1) "
					+ "FROM GRUB;");
			rs.next();
			assertEquals(4, rs.getInt(1));
			// PASS:0834 If 1 row selected and value is 4?

			rs = stmt.executeQuery("SELECT OCTET_LENGTH (C1) " + "FROM GRUB;");
			rs.next();
			assertTrue(rs.getInt(1) > 2);
			// PASS:0834 If 1 row selected and value is > 2?

			assertEquals(1, stmt.executeUpdate("UPDATE GRUB SET C1 = NULL;"));
			// PASS:0834 If 1 row is updated?

			rs = stmt.executeQuery("SELECT CHARACTER_LENGTH (C1) "
					+ "FROM GRUB;");
			rs.next();
			assertEquals(0, rs.getInt(1));
			// PASS:0834 If 1 row selected and value is NULL?

			rs = stmt.executeQuery("SELECT OCTET_LENGTH (C1) " + "FROM GRUB;");
			rs.next();
			assertEquals(0, rs.getInt(1));
			// PASS:0834 If 1 row selected and value is NULL?

			stmt.executeUpdate("DROP TABLE GRUB ");
		}
		// END TEST >>> 0834 <<< END TEST
	}

	/*
	 * 
	 * testDml_144_SubstringFunctionStatic
	 * 
	 * Notes: TEST:0835 <character substring function> (static)!
	 *  
	 */
	public void testDml_144_SubstringFunctionStatic() throws SQLException {

		stmt.executeUpdate("CREATE TABLE MOREGRUB (C1 VARCHAR (10), ID INT); ");
		// PASS:0835 If table is created?

		stmt.executeUpdate("CREATE VIEW X4 (S1, S2, ID) AS "
				+ "SELECT SUBSTRING (C1 FROM 6), "
				+ "SUBSTRING (C1 FROM 2 FOR 4), ID " + "FROM MOREGRUB;");
		// PASS:0835 If view is created?

		BaseTab.setupStaff(stmt);

		rs = stmt.executeQuery("SELECT SUBSTRING (CITY FROM 4 FOR 10) "
				+ "FROM STAFF WHERE EMPNAME = 'Ed';");
		rs.next();
		assertEquals("on             ", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0835 If 1 row selected and value is 'on '?

		// NOTE:0835 Right truncation subtest deleted.

		try {
			rs = stmt.executeQuery("SELECT SUBSTRING (CITY FROM 4 FOR -1) "
					+ "FROM STAFF WHERE EMPNAME = 'Ed';");
			rs.next(); 
			fail();
			// PASS:0835 If ERROR, substring error, 0 rows selected?
		} catch (SQLException sqle) {
		}

		// following test fails (erronously) in Firebird
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT SUBSTRING (CITY FROM 0 FOR 10) "
					+ "FROM STAFF WHERE EMPNAME = 'Ed'; ");
			rs.next();
			assertEquals("Akron ", rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is 'Akron '?
		}

		// NOTE:0835 Host language variable subtest deleted.

		// next test also fails for Firebird. Returns value "A "
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT SUBSTRING (CITY FROM 1 FOR 1) "
					+ "FROM STAFF WHERE EMPNAME = 'Ed';");
			rs.next();
			assertEquals("A", rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is 'A'?
		}

		// next test fails for FB. Returns value " "
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT SUBSTRING (CITY FROM 1 FOR 0) "
					+ "FROM STAFF WHERE EMPNAME = 'Ed';");
			rs.next();
			assertEquals("", rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is ''?
		}

		// next test fails for FB. Returns value " "
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT SUBSTRING (CITY FROM 12 FOR 1) "
					+ "FROM STAFF WHERE EMPNAME = 'Ed';");
			rs.next();
			assertEquals("", rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is ''?
		}

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO MOREGRUB VALUES ('Pretzels', 1);"));
		// PASS:0835 If 1 row is inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO MOREGRUB VALUES (NULL, 2);"));
		// PASS:0835 If 1 row is inserted?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO MOREGRUB VALUES ('Chips', 3);"));
		// PASS:0835 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT S1 FROM X4 WHERE ID = 1;");
		rs.next();
		assertEquals("els", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0835 If 1 row selected and S1 = 'els'?

		rs = stmt.executeQuery("SELECT S1 FROM X4 WHERE ID = 3;");
		rs.next();
		assertEquals("", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0835 If 1 row selected and S1 = ''?

		rs = stmt.executeQuery("SELECT S2 FROM X4 WHERE ID = 1; ");
		rs.next();
		assertEquals("retz", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0835 If 1 row selected and S2 = 'retz'?

		rs = stmt.executeQuery("SELECT S2 FROM X4 WHERE ID = 3;");
		rs.next();
		assertEquals("hips", rs.getString(1));
		assertFalse(rs.next());
		// PASS:0835 If 1 row selected and S2 = 'hips'?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			// firebird doesn't support a column name in the substring function.
			rs = stmt.executeQuery("SELECT SUBSTRING (C1 FROM ID) "
					+ "FROM MOREGRUB " + "WHERE C1 LIKE 'Ch%'; ");
			rs.next();
			assertEquals("ips", rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is 'ips'?

			rs = stmt.executeQuery("SELECT SUBSTRING (C1 FROM 1 FOR ID) "
					+ "FROM MOREGRUB " + "WHERE C1 LIKE 'Ch%'; ");
			rs.next();
			assertEquals("Chi", rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is 'Chi'?
		}

		rs = stmt.executeQuery("SELECT S1 FROM X4 WHERE ID = 2;");
		rs.next();
		assertEquals(null, rs.getString(1));
		assertFalse(rs.next());
		// PASS:0835 If 1 row selected and S1 is NULL?

		stmt.executeUpdate("DELETE FROM MOREGRUB;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO MOREGRUB VALUES ("
				+ "'Tacos', NULL);"));
		// PASS:0835 If 1 row is inserted?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT SUBSTRING (C1 FROM 1 FOR ID) "
					+ "FROM MOREGRUB;");
			rs.next();
			assertEquals(null, rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is NULL?

			rs = stmt.executeQuery("SELECT SUBSTRING (C1 FROM ID FOR 1) "
					+ "FROM MOREGRUB;");
			rs.next();
			assertEquals(null, rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is NULL?

			stmt.executeUpdate("UPDATE MOREGRUB SET C1 = NULL;");

			rs = stmt.executeQuery("SELECT SUBSTRING (C1 FROM ID FOR ID) "
					+ "FROM MOREGRUB;");
			rs.next();
			assertEquals(null, rs.getString(1));
			assertFalse(rs.next());
			// PASS:0835 If 1 row selected and value is NULL?
		}
		// stmt.executeUpdate("DROP TABLE MOREGRUB ");
		// END TEST >>> 0835 <<< END TEST
	}
	/*
	 * 
	 * testDml_144_ComposedLengthExpressionAndSubstring
	 * 
	 * Notes: TEST:0839 Composed <length expression> and SUBSTRING!
	 *  
	 */
	public void testDml_144_ComposedLengthExpressionAndSubstring()
			throws SQLException {

		String myStringFunctions = conn.getMetaData().getStringFunctions()
				.toUpperCase();

		// database must support char_length, and character_length.
		if (myStringFunctions.matches(".*CHAR_LENGTH.*")
				&& myStringFunctions.matches(".*CHARACTER_LENGTH.*")) {
			BaseTab.setupStaff(stmt);

			rs = stmt.executeQuery("SELECT CHAR_LENGTH (SUBSTRING "
					+ "(CITY FROM 4 FOR 4)) "
					+ "FROM STAFF WHERE EMPNAME = 'Ed'; ");
			rs.next();
			assertEquals(4, rs.getInt(1));
			assertFalse(rs.next());
			// PASS:0839 If 1 row selected and value is 4?

			rs = stmt
					.executeQuery("SELECT CHARACTER_LENGTH (SUBSTRING "
							+ "(EMPNUM FROM 1)) "
							+ "FROM STAFF WHERE EMPNAME = 'Ed'; ");
			rs.next();
			assertEquals(3, rs.getInt(1));
			assertFalse(rs.next());
			// PASS:0839 If 1 row selected and value is 3?
		}
		// END TEST >>> 0839 <<< END TEST
	}
	/*
	 * 
	 * testDml_147_ComposedLengthExpressionAndSubstring
	 * 
	 * Notes: TEST:0840 Roll back schema manipulation !
	 *  
	 */
	public void testDml_147_RollBackSchemaManipulation() throws SQLException {

		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("INSERT INTO USIG VALUES (0,2);");
		stmt.executeUpdate("INSERT INTO USIG VALUES (1,3);");
		if (conn.getMetaData().supportsTransactions()) {
			conn.setAutoCommit(false);
			stmt.executeUpdate("CREATE TABLE NOT_THERE (C1 CHAR (10));");
			// PASS:0840 If table is created?
			conn.rollback();

			try {
				stmt
						.executeUpdate("INSERT INTO NOT_THERE VALUES ('1234567890');");
				fail();
				// PASS:0840 If ERROR, syntax error/access violation, 0 rows
				// selected?
			} catch (SQLException sqle) {
			}

			conn.rollback();

			stmt.executeUpdate("CREATE VIEW NOT_HERE AS SELECT * FROM USIG;");
			// PASS:0840 If view is created?

			conn.rollback();

			try {
				rs = stmt.executeQuery("SELECT COUNT (*) FROM NOT_HERE;");
				fail();
				// PASS:0840 If ERROR, syntax error/access violation, 0 rows
				// selected?
			} catch (SQLException sqle) {
			}

			conn.rollback();

			if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
				stmt.executeUpdate("ALTER TABLE USIG ADD  NUL INT;");
			else
				stmt.executeUpdate("ALTER TABLE USIG ADD COLUMN NUL INT;");
			// PASS:0840 If column is added?

			conn.rollback();

			try {
				rs = stmt.executeQuery("SELECT COUNT (*) "
						+ "FROM USIG WHERE NUL IS NULL;");
				fail();
				// PASS:0840 If ERROR, syntax error/access violation, 0 rows
				// selected?
			} catch (SQLException sqle) {
			}

			conn.rollback();

			stmt.executeUpdate("DROP TABLE USIG ;");
			// PASS:0840 If table is dropped?

			conn.rollback();

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG;");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:0840 If count = 2?

			conn.rollback();

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM USIG;");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:0840 If count = 2?

			conn.rollback();
			conn.setAutoCommit(true);
			// END TEST >>> 0840 <<< END TEST
		}
	}

	/*
	 * 
	 * testDml_148_OrderingOfColumnNamesInJoins()
	 * 
	 * Notes: Natural join's not supported by any anticipated targets.
	 *  
	 */
	public void testDml_148_OrderingOfColumnNamesInJoins() throws SQLException {
		int rowCount = 0;
		// TEST:0843 Ordering of column names in joins !
		//
		// REFERENCE: 7.5 sr 6 f
		// NOTE:0843 ordering of column names in NATURAL JOIN

		BaseTab.setupBaseTab(stmt);
		// SELECT *
		// FROM WORKS NATURAL LEFT JOIN PROJ
		// ORDER BY EMPNUM DESC, PNUM;
		// PASS:0843 If 12 rows selected?
		// PASS:0843 If ordered row and column values for first two rows are: ?
		// PASS:0843 P2 E4 20 CALM Code 30000 Vienna ?
		// PASS:0843 P4 E4 40 SDP Design 20000 Deale ?
		//
		//
		// NOTE:0843 ordering of column names in JOIN ... USING
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT * "
					+ "FROM WORKS JOIN PROJ USING (PNUM) "
					+ "ORDER BY EMPNUM DESC, PNUM;");
			rs.next();
			assertEquals("P2", rs.getString(1).trim());
			assertEquals("E4", rs.getString(2).trim());
			assertEquals(20, rs.getInt(3));
			assertEquals("CALM", rs.getString(4).trim());
			assertEquals("Code", rs.getString(5).trim());
			assertEquals(30000, rs.getInt(6));
			assertEquals("Vienna", rs.getString(7).trim());
			rs.next();
			assertEquals("P4", rs.getString(1).trim());
			assertEquals("E4", rs.getString(2).trim());
			assertEquals(40, rs.getInt(3));
			assertEquals("SDP", rs.getString(4).trim());
			assertEquals("Design", rs.getString(5).trim());
			assertEquals(20000, rs.getInt(6));
			assertEquals("Deale", rs.getString(7).trim());
			rowCount = 2;
			while (rs.next())
				rowCount++;
			assertEquals(12, rowCount);
		}
		// PASS:0843 If 12 rows selected?
		// PASS:0843 If ordered row and column values for first two rows are: ?
		// PASS:0843 P2 E4 20 CALM Code 30000 Vienna ?
		// PASS:0843 P4 E4 40 SDP Design 20000 Deale ?
		// NOTE:0843 Same answer as above
		//
		// NOTE:0843 ordering of column names in NATURAL JOIN
		// REFERENCE: 7.5 sr 5

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT * " + "FROM WORKS RIGHT JOIN PROJ "
					+ "ON WORKS.PNUM = PROJ.PNUM " + "ORDER BY 1 DESC, 2;");
			rs.next();
			assertEquals("E4", rs.getString(1).trim());
			assertEquals("P2", rs.getString(2).trim());
			assertEquals(20, rs.getInt(3));
			assertEquals("P2", rs.getString(4).trim());
			assertEquals("CALM", rs.getString(5).trim());
			assertEquals("Code", rs.getString(6).trim());
			assertEquals(30000, rs.getInt(7));
			assertEquals("Vienna", rs.getString(8).trim());
			rs.next();
			assertEquals("E4", rs.getString(1).trim());
			assertEquals("P4", rs.getString(2).trim());
			assertEquals(40, rs.getInt(3));
			assertEquals("P4", rs.getString(4).trim());
			assertEquals("SDP", rs.getString(5).trim());
			assertEquals("Design", rs.getString(6).trim());
			assertEquals(20000, rs.getInt(7));
			assertEquals("Deale", rs.getString(8).trim());
			rowCount = 2;
			while (rs.next())
				rowCount++;
			assertEquals(12, rowCount);
			// PASS:0843 If 12 rows selected?
			// PASS:0843 If ordered row and column values for first two rows
			// are: ?
			// PASS:0843 E4 P2 20 P2 CALM Code 30000 Vienna ?
			// PASS:0843 E4 P4 40 P4 SDP Design 20000 Deale ?
			// END TEST >>> 0843 <<< END TEST
		}

		// *********************************************
		// TEST:0844 Outer join predicates !
		stmt.executeUpdate("CREATE TABLE SEVEN_TYPES ( " + "T_INT INTEGER, "
				+ "T_CHAR CHAR(10), " + "T_SMALL SMALLINT, "
				+ "T_DECIMAL DECIMAL(10,2), " + "T_REAL REAL, "
				+ "T_FLOAT FLOAT, " + "T_DOUBLE DOUBLE PRECISION);");
		stmt
				.executeUpdate("INSERT INTO SEVEN_TYPES VALUES (1, 'E1',-11, 2, 3, 4, 5);");
		stmt
				.executeUpdate("INSERT INTO SEVEN_TYPES VALUES (2, 'E2', -5, 13, 33,-444, -55);");
		stmt
				.executeUpdate("INSERT INTO SEVEN_TYPES VALUES (3, 'E6', -3,-222,333, 44, 555);");
		stmt
				.executeUpdate("INSERT INTO SEVEN_TYPES VALUES (12,'DUP', 0, 0, -1, 1,1E+1);");
		stmt
				.executeUpdate("INSERT INTO SEVEN_TYPES VALUES (12,'DUP', 0, 0, -1, 1,1E+1);");

		// NOTE:0844 BETWEEN predicate
		rs = stmt.executeQuery("SELECT EMPNAME, CITY, T_DECIMAL "
				+ "FROM STAFF LEFT OUTER JOIN SEVEN_TYPES "
				+ "ON -GRADE / 11 BETWEEN T_REAL AND T_DECIMAL "
				+ "ORDER BY EMPNAME; ");
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs.next();
			assertEquals("Alice", rs.getString(1).trim());
			assertEquals("Deale", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Alice", rs.getString(1).trim());
			assertEquals("Deale", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Betty", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Betty", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Carmen", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Carmen", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Don", rs.getString(1).trim());
			assertEquals("Deale", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Don", rs.getString(1).trim());
			assertEquals("Deale", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Ed", rs.getString(1).trim());
			assertEquals("Akron", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Ed", rs.getString(1).trim());
			assertEquals("Akron", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertFalse(rs.next());
		} else {
			rs.next();
			assertEquals("Alice", rs.getString(1).trim());
			assertEquals("Deale", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertTrue(rs.wasNull());
			rs.next();
			assertEquals("Betty", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Betty", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			rs.next();
			assertEquals("Carmen", rs.getString(1).trim());
			assertEquals("Vienna", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertTrue(rs.wasNull());
			rs.next();
			assertEquals("Don", rs.getString(1).trim());
			assertEquals("Deale", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertTrue(rs.wasNull());
			rs.next();
			assertEquals("Ed", rs.getString(1).trim());
			assertEquals("Akron", rs.getString(2).trim());
			assertEquals(0, rs.getInt(3));
			assertTrue(rs.wasNull());
			assertFalse(rs.next());
		}
		// PASS:0844 If 6 rows selected with ordered rows and column values ?
		// PASS:0844 Alice Deale NULL ?
		// PASS:0844 Betty Vienna 0 ?
		// PASS:0844 Betty Vienna 0 ?
		// PASS:0844 Carmen Vienna NULL ?
		// PASS:0844 Don Deale NULL ?
		// PASS:0844 Ed Akron NULL ?

		// NOTE:0844 comparable CHAR types
		// NOTE:0844 IN predicate, with literals and variable value
		rs = stmt.executeQuery("SELECT T_INT, T_CHAR, EMPNAME, EMPNUM, GRADE "
				+ "FROM SEVEN_TYPES RIGHT JOIN STAFF "
				+ "ON GRADE IN (10, 11, 13) AND EMPNUM = T_CHAR "
				+ "ORDER BY EMPNAME, T_INT; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(2));
		assertEquals("Alice", rs.getString(3).trim());
		assertEquals("E1", rs.getString(4).trim());
		assertEquals(12, rs.getInt(5));
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals("E2", rs.getString(2).trim());
		assertEquals("Betty", rs.getString(3).trim());
		assertEquals("E2", rs.getString(4).trim());
		assertEquals(10, rs.getInt(5));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(2));
		assertEquals("Carmen", rs.getString(3).trim());
		assertEquals("E3", rs.getString(4).trim());
		assertEquals(13, rs.getInt(5));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(2));
		assertEquals("Don", rs.getString(3).trim());
		assertEquals("E4", rs.getString(4).trim());
		assertEquals(12, rs.getInt(5));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(2));
		assertEquals("Ed", rs.getString(3).trim());
		assertEquals("E5", rs.getString(4).trim());
		assertEquals(13, rs.getInt(5));
		assertFalse(rs.next());
		// PASS:0844 If 5 rows selected with ordered rows and column values ?
		// PASS:0844 NULL NULL Alice E1 12 ?
		// PASS:0844 2 E2 Betty E2 10 ?
		// PASS:0844 NULL NULL Carmen E3 13 ?
		// PASS:0844 NULL NULL Don E4 12 ?
		// PASS:0844 NULL NULL Ed E5 13 ?

		// NOTE:0844 subquery with outer reference and correlation names
		//
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			rs = stmt.executeQuery("SELECT XX.PNUM, BUDGET, HOURS, EMPNUM "
					+ "FROM PROJ XX LEFT JOIN WORKS YY "
					+ "ON XX.PNUM = YY.PNUM AND "
					+ "HOURS * BUDGET / 160000 > (SELECT GRADE FROM STAFF "
					+ "WHERE YY.EMPNUM = STAFF.EMPNUM) " + "ORDER BY xx.PNUM;");
		else
			rs = stmt.executeQuery("SELECT XX.PNUM, BUDGET, HOURS, EMPNUM "
					+ "FROM PROJ XX LEFT JOIN WORKS YY "
					+ "ON XX.PNUM = YY.PNUM AND "
					+ "HOURS * BUDGET / 160000 > (SELECT GRADE FROM STAFF "
					+ "WHERE YY.EMPNUM = STAFF.EMPNUM) " + "ORDER BY PNUM;");
		rs.next();
		assertEquals("P1", rs.getString(1).trim());
		assertEquals(10000, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(4));
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals("P2", rs.getString(1).trim());
		assertEquals(30000, rs.getInt(2));
		assertEquals(80, rs.getInt(3));
		assertEquals("E2", rs.getString(4).trim());
		rs.next();
		assertEquals("P3", rs.getString(1).trim());
		assertEquals(30000, rs.getInt(2));
		assertEquals(80, rs.getInt(3));
		assertEquals("E1", rs.getString(4).trim());
		rs.next();
		assertEquals("P4", rs.getString(1).trim());
		assertEquals(20000, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(4));
		rs.next();
		assertEquals("P5", rs.getString(1).trim());
		assertEquals(10000, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(4));
		rs.next();
		assertEquals("P6", rs.getString(1).trim());
		assertEquals(50000, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertTrue(rs.wasNull());
		assertEquals(null, rs.getString(4));
		// rs.next();
		assertFalse(rs.next());
		// PASS:0844 If 6 rows selected with ordered rows and column values ?
		// PASS:0844 P1 10000 NULL NULL ?
		// PASS:0844 P2 30000 80 E2 ?
		// PASS:0844 P3 30000 80 E1 ?
		// PASS:0844 P4 20000 NULL NULL ?
		// PASS:0844 P5 10000 NULL NULL ?
		// PASS:0844 P6 50000 NULL NULL ?
		//
		rs = stmt.executeQuery("SELECT STAFF.CITY,EMPNAME,PNAME,BUDGET "
				+ "FROM STAFF LEFT JOIN PROJ " + "ON STAFF.CITY = PROJ.CITY "
				+ "AND STAFF.CITY <> 'Vienna' " + "AND EMPNAME <> 'Don' "
				+ "WHERE BUDGET > 15000 OR BUDGET IS NULL "
				+ "ORDER BY STAFF.CITY, EMPNAME, BUDGET; ");
		rs.next();
		assertEquals("Akron", rs.getString(1).trim());
		assertEquals("Ed", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals("Deale", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals("SDP", rs.getString(3).trim());
		assertEquals(20000, rs.getInt(4));
		rs.next();
		assertEquals("Deale", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals("PAYR", rs.getString(3).trim());
		assertEquals(50000, rs.getInt(4));
		rs.next();
		assertEquals("Deale", rs.getString(1).trim());
		assertEquals("Don", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals("Vienna", rs.getString(1).trim());
		assertEquals("Betty", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals("Vienna", rs.getString(1).trim());
		assertEquals("Carmen", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		assertFalse(rs.next());
		// PASS:0844 If 6 rows selected with ordered rows and column values ?
		// PASS:0844 Akron Ed NULL NULL ?
		// PASS:0844 Deale Alice SDP 20000 ?
		// PASS:0844 Deale Alice PAYR 50000 ?
		// PASS:0844 Deale Don NULL NULL ?
		// PASS:0844 Vienna Betty NULL NULL ?
		// PASS:0844 Vienna Carmen NULL NULL ?

		// NOTE:0844 difference between WHERE and ON
		rs = stmt.executeQuery("SELECT STAFF.CITY,EMPNAME,PNAME,BUDGET "
				+ "FROM STAFF LEFT JOIN PROJ " + "ON STAFF.CITY = PROJ.CITY "
				+ "AND STAFF.CITY <> 'Vienna' "
				+ "WHERE (BUDGET > 15000 OR BUDGET IS NULL) "
				+ "AND EMPNAME <> 'Don' "
				+ "ORDER BY STAFF.CITY, EMPNAME, BUDGET; ");
		rs.next();
		assertEquals("Akron", rs.getString(1).trim());
		assertEquals("Ed", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals("Deale", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals("SDP", rs.getString(3).trim());
		assertEquals(20000, rs.getInt(4));
		rs.next();
		assertEquals("Deale", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals("PAYR", rs.getString(3).trim());
		assertEquals(50000, rs.getInt(4));
		rs.next();
		assertEquals("Vienna", rs.getString(1).trim());
		assertEquals("Betty", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals("Vienna", rs.getString(1).trim());
		assertEquals("Carmen", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		assertEquals(0, rs.getInt(4));
		assertTrue(rs.wasNull());
		assertFalse(rs.next());
		// PASS:0844 If 5 rows selected with ordered rows and column values ?
		// PASS:0844 Akron Ed NULL NULL ?
		// PASS:0844 Deale Alice SDP 20000 ?
		// PASS:0844 Deale Alice PAYR 50000 ?
		// PASS:0844 Vienna Betty NULL NULL ?
		// PASS:0844 Vienna Carmen NULL NULL ?

		// NOTE:0844 correlation name with self-JOIN
		rs = stmt.executeQuery("SELECT XX.T_INT, YY.T_INT "
				+ "FROM SEVEN_TYPES XX RIGHT OUTER JOIN SEVEN_TYPES YY "
				+ "ON XX.T_INT = YY.T_INT +1 " + "ORDER BY YY.T_INT; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(3, rs.getInt(2));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(12, rs.getInt(2));
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		assertEquals(12, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0844 If 5 rows selected with ordered rows and column values ?
		// PASS:0844 2 1 ?
		// PASS:0844 3 2 ?
		// PASS:0844 NULL 3 ?
		// PASS:0844 NULL 12 ?
		// PASS:0844 NULL 12 ?

		// NOTE:0844 nested booleans
		//NOTE:0844 data types are merely comparable
		rs = stmt.executeQuery("SELECT GRADE, T_FLOAT, T_DOUBLE "
				+ "FROM STAFF LEFT JOIN SEVEN_TYPES T7 "
				+ "ON GRADE * -40 > T7.T_FLOAT "
				+ "OR (T_DOUBLE -542.5 < GRADE AND T_DOUBLE -541.5 > GRADE) "
				+ "ORDER BY GRADE; ");
		rs.next();
		assertEquals(10, rs.getInt(1));
		assertTrue((-444.1 < rs.getDouble(2)) && (-443.9 > rs.getDouble(2)));
		assertTrue((-55.1 < rs.getDouble(3)) && (-54.9 > rs.getDouble(3)));
		rs.next();
		assertEquals(12, rs.getInt(1));
		rs.getDouble(2);
		assertTrue(rs.wasNull());
		rs.getDouble(3);
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals(12, rs.getInt(1));
		rs.getDouble(2);
		assertTrue(rs.wasNull());
		rs.getDouble(3);
		assertTrue(rs.wasNull());
		rs.next();
		assertEquals(13, rs.getInt(1));
		assertTrue((43.9 < rs.getDouble(2)) && (555.1 > rs.getDouble(2)));
		assertTrue((43.9 < rs.getDouble(3)) && (555.1 > rs.getDouble(3)));
		rs.next();
		assertEquals(13, rs.getInt(1));
		assertTrue((43.9 < rs.getDouble(2)) && (555.1 > rs.getDouble(2)));
		assertTrue((43.9 < rs.getDouble(3)) && (555.1 > rs.getDouble(3)));
		assertFalse(rs.next());

		// PASS:0844 If 5 rows selected with ordered rows and column values ?
		// PASS:0844 10 -444 (approximately) -55 (approximately) ?
		// PASS:0844 12 NULL NULL ?
		// PASS:0844 12 NULL NULL ?
		// PASS:0844 13 44 (approximately) 555 (approximately) ?
		// PASS:0844 13 44 (approximately) 555 (approximately) ?

		// END TEST >>> 0844 <<< END TEST

	}
	/*
	 * 
	 * testDml_149_DoubleSetTransaction
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_149_DoubleSetTransaction() throws SQLException {
		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2));");
		// TEST:0561 Double SET TRANSACTION!
		conn.setAutoCommit(false);
		conn.rollback();
		// PASS:0561 If successful completion?

		conn.setReadOnly(true);
		// PASS:0561 If successful completion?

		conn.setReadOnly(false);
		// PASS:0561 If successful completion?

		stmt.executeUpdate("INSERT INTO USIG VALUES (10, 20);");
		// PASS:0561 If 1 row is inserted?

		conn.rollback();
		// PASS:0561 If successful completion?

		conn.setReadOnly(false);
		// PASS:0561 If successful completion?

		conn.setReadOnly(true);
		// PASS:0561 If successful completion?

		try {
			stmt.executeUpdate("INSERT INTO USIG VALUES (10, 20);");
			fail();
		} catch (SQLException sqle) {
		}
		// PASS:0561 If ERROR, invalid transaction state, 0 rows inserted?

		conn.rollback();
		// PASS:0561 If successful completion?

		// END TEST >>> 0561 <<< END TEST
		// *********************************************
	}

	/*
	 * 
	 * testDml_149_CastFunctionsStaticNits
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_149_CastFunctionsStaticNits() throws SQLException {
		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2));");
		stmt.executeUpdate(" INSERT INTO ECCO VALUES ('NL');");
		// TEST:0846 Feature 20, CAST functions (static) nits!

		stmt.executeUpdate("CREATE TABLE NO_DUCK ( "
				+ "GOOSE       NUMERIC (4, 2), " + "ALBATROSS   FLOAT, "
				+ "SEAGULL     INT, " + "OSPREY      CHAR (10));");
		// PASS:0846 If table is created?

		// NOTE:0846 CAST (100 AS NUMERIC (2)) loses the leading significant
		// digit
		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			// firebird1.5 doesn't show correct behaviour here.
			try {
				rs = stmt.executeQuery("SELECT CAST (100 AS NUMERIC (2)) "
						+ "FROM ECCO;");
				rs.next();
				System.out.println("149:" + rs.getString(1));
				fail();
				// PASS:0846 If ERROR, numeric value out of range, 0 rows
				// selected?
			} catch (SQLException sqle) {
			}
		}
		rs = stmt.executeQuery("SELECT CAST (100.5 AS DECIMAL (3)) FROM ECCO;");
		rs.next();
		assertTrue((100 == rs.getInt(1)) || (101 == rs.getInt(1)));
		// PASS:0846 If 1 row selected and value is 100 or 101?

		stmt
				.executeUpdate("INSERT INTO NO_DUCK VALUES ( "
						+ "CAST ('  23.23 ' AS NUMERIC (4, 2)), 1.57E-1, -9, 'QUACK');");
		// PASS:0846 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM NO_DUCK WHERE GOOSE = 23.23;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0846 If count = 1?

		assertEquals(1, stmt.executeUpdate("DELETE FROM NO_DUCK "
				+ "WHERE ALBATROSS - CAST ('   15.5E0    ' AS FLOAT) < 3E-1;"));
		// PASS:0846 If 1 row is deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM NO_DUCK; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0846 If count = 0?

		try {
			stmt.executeUpdate("INSERT INTO NO_DUCK "
					+ "SELECT 22.22, CAST (C1 AS FLOAT), 0, C1 FROM ECCO;");
			fail();
		} catch (SQLException sqle) {
		}
		// PASS:0846 If ERROR, invalid character value for cast, 0 rows
		// inserted?

		try {
			stmt
					.executeUpdate("INSERT INTO NO_DUCK "
							+ "SELECT 22.22, 2.222E1, CAST (C1 AS INT), 'QUACK!' FROM ECCO;");
			fail();
		} catch (SQLException sqle) {
		}
		// PASS:0846 If ERROR, invalid character value for cast, 0 rows
		// inserted?

		rs = stmt
				.executeQuery("SELECT CAST (CAST (3 AS DEC (5, 3)) AS CHAR (5)) "
						+ " FROM ECCO; ");
		rs.next();
		assertTrue((2.9 < rs.getDouble(1)) && (3.1 > rs.getDouble(1)));
		// PASS:0846 If 1 row selected and value is '3.000'?

		assertEquals(1, stmt.executeUpdate("INSERT INTO NO_DUCK VALUES ( "
				+ "12.00, -10.5E0, 12, 'QUACK!');"));
		// PASS:0846 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK "
				+ "SET OSPREY = CAST (GOOSE AS CHAR (10)) "
				+ "WHERE SEAGULL = CAST (GOOSE AS DEC);"));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT OSPREY " + "FROM NO_DUCK;");
		rs.next();
		assertEquals("12.00", rs.getString(1).trim());
		// PASS:0846 If 1 row selected and OSPREY = '12.00 '?

		rs = stmt.executeQuery("SELECT OSPREY " + "FROM NO_DUCK "
				+ "WHERE OSPREY < CAST (SEAGULL + 1 AS CHAR (10)) "
				+ "AND OSPREY = CAST (GOOSE * 1 AS CHAR (10)); ");
		rs.next();
		assertEquals("12.00     ", rs.getString(1));
		// PASS:0846 If 1 row selected and OSPREY = '12.00 '?

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK "
				+ "SET OSPREY = CAST (-SEAGULL AS CHAR (10));"));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT OSPREY " + "FROM NO_DUCK;");
		rs.next();
		assertEquals("-12       ", rs.getString(1));
		// PASS:0846 If 1 row selected and OSPREY = '-12 '?

		// NOTE:0846 Expected value -12.00 is too long for CHAR (5) cast
		try {
			stmt.executeQuery("SELECT CAST (-GOOSE AS CHAR (5)) "
					+ "FROM NO_DUCK;");
			rs.next(); 
			fail();
		} catch (SQLException sqle) {
			// PASS:0846 If ERROR, string data, right truncation, 0 rows
			// selected?
		}

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK "
				+ "SET ALBATROSS = 0.0;"));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT CAST (-ALBATROSS AS CHAR (5)) "
				+ "FROM NO_DUCK;");
		rs.next();
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals("0.00 ", rs.getString(1));
		else
			assertEquals("0E0  ", rs.getString(1));
		// PASS:0846 If 1 row selected and value is '0E0 '?

		rs = stmt.executeQuery("SELECT CAST (0230E-1 AS CHAR (10)) "
				+ "FROM ECCO;");
		rs.next();
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals("23.000000 ", rs.getString(1));
		else
			assertEquals("2.3E1", rs.getString(1));
		// PASS:0846 If 1 row selected and value is '2.3E1 '?

		rs = stmt.executeQuery("SELECT CAST (0230E+1 AS CHAR (10)) "
				+ "FROM ECCO;");
		rs.next();
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals("2300.0000 ", rs.getString(1));
		else
			assertEquals("2.3E3", rs.getString(1));
		// PASS:0846 If 1 row selected and value is '2.3E3 '?

		stmt.executeUpdate("DELETE FROM NO_DUCK;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO NO_DUCK VALUES ( "
				+ "0.00, -10.5E0, -0, 'QUACK!');"));
		// PASS:0846 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK "
				+ "SET OSPREY = CAST (ALBATROSS AS CHAR (10)) "
				+ "WHERE GOOSE = CAST (SEAGULL AS NUMERIC (2));"));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT OSPREY " + "FROM NO_DUCK;");
		rs.next();
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals("-10.500000", rs.getString(1));
		else
			assertEquals("-1.05E1 ", rs.getString(1));
		// PASS:0846 If 1 row selected and OSPREY = '-1.05E1 '?

		assertEquals(1, stmt
				.executeUpdate("UPDATE NO_DUCK SET ALBATROSS = -0.5;"));
		// PASS:0846 If 1 row is updated?

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK "
				+ "SET OSPREY = CAST (ALBATROSS AS CHAR (10)); "));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT OSPREY " + "FROM NO_DUCK; ");
		rs.next();
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals("-0.5000000", rs.getString(1));
		else
			assertEquals("-5E-1 ", rs.getString(1));

		// PASS:0846 If 1 row selected and OSPREY = '-5E-1 '?

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK "
				+ "SET OSPREY = CAST (-ALBATROSS AS CHAR (10)); "));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT OSPREY " + "FROM NO_DUCK;");
		rs.next();
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals("0.5000000 ", rs.getString(1));
		else
			assertEquals("5E-1 ", rs.getString(1));
		// PASS:0846 If 1 row selected and OSPREY = '5E-1 '?

		// NOTE:0846 Expected value -5E-1 is too long for CHAR (4) cast
		try {
			stmt.executeQuery("SELECT CAST (ALBATROSS AS CHAR (4)) "
					+ "FROM NO_DUCK;");
			rs.next(); 
			fail();
			// PASS:0846 If ERROR, string data, right truncation, 0 rows
			// selected?
		} catch (SQLException sqle) {
		}

		rs = stmt
				.executeQuery("SELECT CAST (NULL AS CHAR (10)), GOOSE FROM NO_DUCK "
						+ "WHERE SEAGULL = 0 "
						+ "UNION "
						+ "SELECT OSPREY, CAST (SEAGULL AS NUMERIC (4, 2)) FROM NO_DUCK "
						+ "WHERE GOOSE > 10000; ");
		// PASS:0846 If 1 row selected and first value is NULL?

		assertEquals(1, stmt.executeUpdate("UPDATE NO_DUCK SET GOOSE = "
				+ "CAST (NULL AS NUMERIC (2, 2)); "));
		// PASS:0846 If 1 row is updated?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM NO_DUCK WHERE GOOSE IS NULL;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0846 If count = 1?

		rs = stmt
				.executeQuery("SELECT CAST (GOOSE AS INT) " + "FROM NO_DUCK; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		assertTrue(rs.wasNull());
		// PASS:0846 If 1 row selected and value is NULL?
		// END TEST >>> 0846 <<< END TEST
		// *************************************************////END-OF-MODULE

	}
	/*
	 * 
	 * testDml_153_QuerySpectWithSubqueryUpdateable
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_153_QuerySpectWithSubqueryUpdateable()
			throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// TEST:0848 Query spec with subquery is now updatable!

		stmt.executeUpdate("CREATE VIEW EXON AS "
				+ "SELECT * FROM STAFF WHERE CITY IN "
				+ "(SELECT CITY FROM PROJ);");
		// PASS:0848 If view is created?

		assertEquals(1, stmt.executeUpdate("DELETE FROM EXON "
				+ "WHERE GRADE = 10;"));
		// PASS:0848 If 1 row is deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF; ");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0848 If count = 4?

		assertEquals(1, stmt.executeUpdate("UPDATE EXON "
				+ "SET EMPNAME = 'Heathen' " + "WHERE EMPNAME = 'Alice'; "));
		// PASS:0848 If 1 row is updated?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE EMPNAME LIKE 'H%';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0848 If count = 1?

		// END TEST >>> 0848 <<< END TEST
	}
	/*
	 * 
	 * testDml_154_MixingSdlAndDml()
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_154_MixingSdlAndDml() throws SQLException {

		// TEST:0854 Informational: mixing SDL and DML!
		// NOTE: OPTIONAL test

		stmt.executeUpdate("CREATE TABLE TRANSIENT (WINDOW_ID INT);");
		stmt.executeUpdate("INSERT INTO TRANSIENT VALUES (1);");
		stmt.executeUpdate("CREATE VIEW CTRANS (WIN_COUNT) AS "
				+ "SELECT COUNT(*) FROM TRANSIENT;");
		stmt.executeUpdate("INSERT INTO TRANSIENT VALUES (2);");

		rs = stmt.executeQuery("SELECT * FROM CTRANS;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0854 If WIN_COUNT = 2?

		rs = stmt.executeQuery("SELECT * FROM TRANSIENT "
				+ "ORDER BY WINDOW_ID;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:0854 If 2 rows are selected with the following order?
		//               window_id
		//               =========
		// PASS:0854 If 1 ?
		// PASS:0854 If 2 ?

		// END TEST >>> 0854 <<< END TEST
	}
	/*
	 * 
	 * testDml_155_ComparingCharVsVarCharStrings
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_155_ComparingCharVsVarCharStrings() throws SQLException {

		// TEST:0850 Comparing fixed vs. variable length caracter strings!

		stmt.executeUpdate("CREATE TABLE T850 ( "
				+ "T850KEY INT NOT NULL UNIQUE, " + "T850C   CHAR (11), "
				+ "T850VC  VARCHAR (10), " + "T850LVC VARCHAR (20)); ");
		// PASS:0850 If table created successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO T850 VALUES ( "
				+ "10, '1234567890', '1234567890', '1234567890   ');"));
		// PASS:0850 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO T850 VALUES ( "
				+ "5, '12345     ', '12345', '12345');"));
		// PASS:0850 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO T850 VALUES ( "
				+ "0, '          ', '', '                    ');"));
		// PASS:0850 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850C = T850VC;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0850 If COUNT = 3?

		rs = stmt.executeQuery("SELECT T850KEY "
				+ "FROM T850 WHERE T850VC = '1234567890     ';");
		rs.next();
		assertEquals(10, rs.getInt(1));
		// PASS:0850 If T850KEY = 10?

		rs = stmt.executeQuery("SELECT T850KEY "
				+ "FROM T850 WHERE T850VC = '12345  ';");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0850 If T850KEY = 5?

		rs = stmt.executeQuery("SELECT T850KEY "
				+ "FROM T850 WHERE T850VC = '';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0850 If T850KEY = 0?
		// actually the nist test was wrong here, should be T850VC=''

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850C = '1234567890';");
		rs.next();
		// INFORMATIONAL

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850C = '12345';");
		rs.next();
		// INFORMATIONAL

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850VC = T850LVC;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0850 If COUNT = 3?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850VC = '12345          ';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0850 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850VC = '12345  '; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0850 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850LVC = '12345          ';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0850 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850LVC = '12345  ';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0850 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850C = '12345          ';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0850 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM T850 WHERE T850C = '12345  ';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0850 If COUNT = 1?
		// END TEST >>> 0850 <<< END TEST

	}

	// TestDml_157_TransitionalSchemaDefinition not implemented, as it requires
	// EXTRACT (HOUR FROM MERIDIAN)

	/*
	 * 
	 * testDml_158_JoinConditionSetFunctionOuterReference()
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_158_JoinConditionSetFunctionOuterReference()
			throws SQLException {

		// TEST:0857 <join condition> set function, outer reference!
		BaseTab.setupBaseTab(stmt);
		conn.setAutoCommit(false);
		assertEquals(6, stmt.executeUpdate("DELETE FROM WORKS "
				+ "WHERE EXISTS " + "(SELECT * FROM PROJ JOIN STAFF "
				+ "ON PROJ.CITY <> STAFF.CITY " + "AND EMPNUM = WORKS.EMPNUM "
				+ "AND PNUM = WORKS.PNUM);"));
		// PASS:0857 If delete completed successfully?

		rs = stmt.executeQuery("SELECT EMPNUM, PNUM FROM WORKS "
				+ "ORDER BY EMPNUM, PNUM;");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals("P1", rs.getString(2).trim());
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals("P4", rs.getString(2).trim());
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals("P6", rs.getString(2).trim());
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals("P2", rs.getString(2).trim());
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals("P2", rs.getString(2).trim());
		rs.next();
		assertEquals("E4", rs.getString(1).trim());
		assertEquals("P4", rs.getString(2).trim());
		// PASS:0857 If 6 rows are returned in the following order?
		//                 empnum pnum
		//                 ====== ====
		// PASS:0857 If E1 P1 ?
		// PASS:0857 If E1 P4 ?
		// PASS:0857 If E1 P6 ?
		// PASS:0857 If E2 P2 ?
		// PASS:0857 If E3 P2 ?
		// PASS:0857 If E4 P4 ?

		conn.rollback();
		rs = stmt.executeQuery("SELECT EMPNUM, SUM (HOURS) FROM WORKS OWORKS "
				+ "GROUP BY EMPNUM " + "HAVING EMPNUM IN ( "
				+ "SELECT WORKS.EMPNUM FROM WORKS JOIN STAFF "
				+ "ON WORKS.EMPNUM = STAFF.EMPNUM "
				+ "AND HOURS < SUM (OWORKS.HOURS) / 3 " + "AND GRADE > 10) "
				+ "ORDER BY EMPNUM;");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals(184, rs.getInt(2));
		rs.next();
		assertEquals("E4", rs.getString(1).trim());
		assertEquals(140, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0857 If 2 rows are returned in the following order?
		//               empnum sum(hours)
		//               ====== ==========
		// PASS:0857 If E1 184 ?
		// PASS:0857 If E4 140 ?

		rs = stmt.executeQuery("SELECT EMPNUM, SUM (HOURS) FROM WORKS OWORKS "
				+ "GROUP BY EMPNUM " + "HAVING EMPNUM IN ( "
				+ "SELECT WORKS.EMPNUM FROM WORKS JOIN STAFF "
				+ "ON WORKS.EMPNUM = STAFF.EMPNUM "
				+ "AND HOURS >= 10 + AVG (OWORKS.HOURS) "
				+ "AND CITY = 'Deale') " + "ORDER BY EMPNUM;");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals(184, rs.getInt(2));
		rs.next();
		assertEquals("E4", rs.getString(1).trim());
		assertEquals(140, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0857 If 2 rows are returned in the following order?
		//               empnum sum(hours)
		//               ====== ==========
		// PASS:0857 If E1 184 ?
		// PASS:0857 If E4 140 ?
		// END TEST >>> 0857 <<< END TEST

		conn.commit();
	}
	/*
	 * 
	 * testDml_160_JoinedTableInSelectList
	 * 
	 * Notes: TEST:0859 <joined table> contained in <select list>!
	 *  
	 */
	public void testDml_160_JoinedTableInSelectList() throws SQLException {

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;
		BaseTab.setupBaseTab(stmt);
		rs = stmt
				.executeQuery("SELECT EMPNUM, (SELECT COUNT(*) FROM WORKS JOIN PROJ "
						+ "ON WORKS.PNUM = PROJ.PNUM "
						+ "AND BUDGET > AVG (OSTAFF.GRADE) * 1000 "
						+ "WHERE WORKS.EMPNUM = OSTAFF.EMPNUM) FROM STAFF AS OSTAFF "
						+ "ORDER BY 2, 1;");
		rs.next();
		assertEquals("E5", rs.getString(1).trim());
		assertEquals(0, rs.getInt(2));
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals(1, rs.getInt(2));
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals(1, rs.getInt(2));
		rs.next();
		assertEquals("E4", rs.getString(1).trim());
		assertEquals(2, rs.getInt(2));
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals(4, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0859 If 5 rows are returned in the following order?
		//               empnum count
		//               ====== =====
		// PASS:0859 If E5 0 ?
		// PASS:0859 If E2 1 ?
		// PASS:0859 If E3 1 ?
		// PASS:0859 If E4 2 ?
		// PASS:0859 If E1 4 ?

		// END TEST >>> 0859 <<< END TEST
		// *********************************************

	}
	/*
	 * 
	 * testDml_160_JoinedTableInSelectList
	 * 
	 * Notes: TEST:0860 Domains over various data types!
	 * 
	 * This test requires EXTRACT to work on time types, along with INTERVAL
	 * math. As such, we've skipped over these parts.
	 *  
	 */
	public void testDml_160_DomainsOverVariousDataTypes() throws SQLException {
		// 
		stmt.executeUpdate("CREATE DOMAIN EPOCH_NOT_NORM AS DECIMAL (5, 2);");
		// PASS:0860 If domain created successfully?

		stmt.executeUpdate("CREATE DOMAIN RAD_EPOCH_TYPE FLOAT (20) "
				+ "CHECK (VALUE BETWEEN 0E0 AND 2E0 * 3.1416E0); ");
		// PASS:0860 If domain created successfully?

		stmt.executeUpdate("CREATE DOMAIN RAD_EPOCH_NOT_NORM REAL;");
		// PASS:0860 If domain created successfully?

		stmt
				.executeUpdate("CREATE DOMAIN TIDEDATE AS DATE "
						+ "CHECK (VALUE BETWEEN DATE '1994-01-01' AND DATE '2025-12-31'); ");
		// PASS:0860 If domain created successfully?

		stmt
				.executeUpdate("CREATE DOMAIN DINNERTIME AS TIME "
						+ "CHECK (VALUE BETWEEN TIME '17:30:00' AND TIME '19:00:00'); ");
		// PASS:0860 If domain created successfully?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt
					.executeUpdate("CREATE VIEW CONST_RAD_NOT_NORM (LOC_ID, CONST_ID, "
							+ "AMPLITUDE, EPOCH) AS "
							+ "SELECT LOC_ID, CONST_ID, AMPLITUDE, "
							+ "CAST (EPOCH * 3.14159265358979E0 / 180E0 AS RAD_EPOCH_NOT_NORM) "
							+ "FROM CONST_NOT_NORM; ");
		// PASS:0860 If view created successfully?

		stmt.executeUpdate("CREATE TABLE DINNER_CLUB ( "
				+ "LOC_ID DEC (7) NOT NULL, " + "DINNER DINNERTIME); ");
		// PASS:0860 If table created successfully?
		stmt.executeUpdate("INSERT INTO DINNER_CLUB VALUES "
				+ "(0, TIME '17:30:00'); ");
		// PASS:0860 If 1 row inserted successfully?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("INSERT INTO DINNER_CLUB VALUES "
					+ "(1, CAST (TIME '18:00:00' AS DINNERTIME));");
		// PASS:0860 If 1 row inserted successfully?

		try {
			stmt.executeUpdate("INSERT INTO DINNER_CLUB VALUES "
					+ "(2, TIME '19:30:00');");
			fail();
			// PASS:0860 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		// END TEST >>> 0860 <<< END TEST
	}

	// testDml_161 skipped - none of our databases support SET SESSION
	// CURRENT_USER etc. with set session authid!

	// testDml_162 skipped - uses a natural join
	// <joined table> directly contained in cursor,view!

	/*
	 * 
	 * testDml_163_CaseExpressionsInOtherThanSelect
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_163_CaseExpressionsInOtherThanSelect()
			throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// TEST:0866 Case expressions in other than SELECT!

		stmt.executeUpdate("CREATE VIEW V0866 (EMPNUM, HOURS) AS "
				+ "SELECT EMPNUM, " + "CASE "
				+ "WHEN PNUM = 'P2' THEN HOURS + 30 " + "ELSE HOURS " + "END "
				+ "FROM WORKS;");
		// PASS:0866 If view created successfully?

		stmt.executeUpdate("UPDATE STAFF "
				+ "SET CITY = NULLIF (CITY, 'Deale'); ");
		// PASS:0866 If update completed successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CITY IS NULL; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0866 If COUNT = 2?

		stmt.executeUpdate("INSERT INTO STAFF VALUES ( " + "'E8', 'Wally', "
				+ "CASE WHEN USER = 'SYSDBA' THEN 15 ELSE 10 END, "
				+ "'Monash');");
		// PASS:0866 If insert completed successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE GRADE = 15; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0866 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
				+ "WHERE CASE GRADE " + "WHEN 10 THEN 12 " + "WHEN 13 THEN 12 "
				+ "END = 12;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0866 If COUNT = 3?

		rs = stmt.executeQuery("SELECT SUM(HOURS) " + "FROM V0866;");
		rs.next();
		assertEquals(584, rs.getInt(1));
		// PASS:0866 If SUM(HOURS) = 584?

		rs = stmt.executeQuery("SELECT COALESCE (CITY, EMPNUM) FROM STAFF "
				+ "ORDER BY 1;");
		rs.next();
		assertEquals("Akron", rs.getString(1).trim());
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		rs.next();
		assertEquals("E4", rs.getString(1).trim());
		rs.next();
		assertEquals("Monash", rs.getString(1).trim());
		rs.next();
		assertEquals("Vienna", rs.getString(1).trim());
		rs.next();
		assertEquals("Vienna", rs.getString(1).trim());
		assertFalse(rs.next());
		// PASS:0866 If 6 rows are returned in the following order?
		//               coalesce(city,empnum)
		//               =====================
		// PASS:0866 If Akron?
		// PASS:0866 If E1 ?
		// PASS:0866 If E4 ?
		// PASS:0866 If Monash?
		// PASS:0866 If Vienna?
		// PASS:0866 If Vienna?

		// END TEST >>> 0866 <<< END TEST

	}
	/*
	 * 
	 * testDml_165_NonIdenticalDescriptorsInUnion()
	 * 
	 * Notes: this test just doesn't work for firebird
	 *  
	 */
	public void testDml_165_NonIdenticalDescriptorsInUnion()
			throws SQLException {

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		BaseTab.setupBaseTab(stmt);
		// TEST:0870 Non-identical descriptors in UNION!

		stmt.executeUpdate("CREATE TABLE APPLES ( " + "KEY1 INT, "
				+ "APPLE_NAME CHAR (15)); ");
		// PASS:0870 If table created successfully?

		stmt.executeUpdate("CREATE TABLE ORANGES ( " + "KEY2 FLOAT, "
				+ "ORANGE_NAME VARCHAR (10));");
		// PASS:0870 If table ceated successfully?

		stmt.executeUpdate("INSERT INTO APPLES VALUES ( "
				+ "1, 'Granny Smith');");
		// PASS:0870 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO APPLES VALUES ( "
				+ "2, 'Red Delicious');");
		// PASS:0870 If 1 row inserted successfully?

		stmt
				.executeUpdate("INSERT INTO ORANGES VALUES ( "
						+ "1.5E0, 'Navel');");
		// PASS:0870 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO ORANGES VALUES ( "
				+ "2.5E0, 'Florida');");
		// PASS:0870 If 1 row inserted successfully?

		System.exit(1);
		rs = stmt.executeQuery("SELECT * FROM " + "APPLES UNION ALL "
				+ "SELECT * FROM ORANGES " + "ORDER BY 1; ");
		rs.next();
		assertTrue((.99 < rs.getDouble(1) && (1.01 > rs.getDouble(1))));
		assertEquals("Granny Smith", rs.getString(1));
		rs.next();
		assertTrue((1.54 < rs.getDouble(1) && (1.56 > rs.getDouble(1))));
		assertEquals("Navel", rs.getString(1));
		rs.next();
		assertTrue((1.99 < rs.getDouble(1) && (2.01 > rs.getDouble(1))));
		assertEquals("Red Delicious", rs.getString(1));
		rs.next();
		assertTrue((2.49 < rs.getDouble(1) && (2.51 > rs.getDouble(1))));
		assertEquals("Florida", rs.getString(1));
		// PASS:0870 If 4 rows returned in the following order?
		//                col1 col2
		//                ==== ====
		// PASS:0870 If 1.0 (+ or - 0.01) Granny Smith?
		// PASS:0870 If 1.5 (+ or - 0.01) Navel?
		// PASS:0870 If 2.0 (+ or - 0.01) Red Delicious?
		// PASS:0870 If 2.5 (+ or - 0.01) Florida?

		// END TEST >>> 0870 <<< END TEST

	}
	// testDml_168 is all about information schema and has been skipped
	// TEST:0874 INFORMATION SCHEMA catalog columns!

	/*
	 * 
	 * testDml_168_KeywordColumnInAlterTableIsOptional
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_168_KeywordColumnInAlterTableIsOptional()
			throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// TEST:0878 Keyword COLUMN in ALTER TABLE is optional!

		stmt.executeUpdate("CREATE TABLE T0878 (C1 INT);");
		// PASS:0878 If table created successfully?

		stmt.executeUpdate("ALTER TABLE T0878 ADD C2 CHAR (4);");
		// PASS:0878 If table altered successfully?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE T0878 "
					+ "ALTER C2 SET DEFAULT 'ABCD';");
		// PASS:0878 If table altered successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE T0878 " + "DROP C1 ; ");
		else
			stmt.executeUpdate("ALTER TABLE T0878 " + "DROP C1 CASCADE; ");
		// PASS:0878 If table altered successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("INSERT INTO T0878 VALUES ('ABCD'); ");
		else
			stmt.executeUpdate("INSERT INTO T0878 VALUES (DEFAULT); ");
		// PASS:0878 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT * FROM T0878;");
		rs.next();
		assertEquals("ABCD", rs.getString(1));
		// PASS:0878 If answer = 'ABCD'?
		// END TEST >>> 0878 <<< END TEST
		// *********************************************
	}
	/*
	 * 
	 * testDml_168_DropTableConstraintDefinition
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_168_DropTableConstraintDefinition() throws SQLException {

		// TEST:0879 <drop table constraint definition>!

		stmt.executeUpdate("CREATE TABLE T0879 ( " + "C1 INT, " + "C2 INT, "
				+ "CONSTRAINT DELME CHECK (C1 > 0), "
				+ "CONSTRAINT REFME UNIQUE (C2)); ");
		// PASS:0879 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE U0879 ( C1 INT "
					+ "constraint mycons  REFERENCES T0879 (C2)) ");
		else
			stmt.executeUpdate("CREATE TABLE U0879 ( "
					+ "C1 INT ) REFERENCES T0879 (C2)); ");
		// PASS:0879 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE T0879 "
					+ "DROP CONSTRAINT DELME ; ");
		else
			stmt.executeUpdate("ALTER TABLE T0879 "
					+ "DROP CONSTRAINT DELME RESTRICT; ");

		// PASS:0879 If table altered successfully?

		stmt.executeUpdate("INSERT INTO T0879 VALUES (0, 0);");
		// PASS:0879 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO T0879 VALUES (-1, -1);");
		// PASS:0879 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM T0879;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0879 If COUNT = 2?

		try {
			stmt.executeUpdate("INSERT INTO U0879 VALUES (20);");
			fail();
			// PASS:0879 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO T0879 VALUES (2, 0);");
			fail();
			// PASS:0879 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("ALTER TABLE T0879 "
					+ "DROP CONSTRAINT REFME RESTRICT;");
			fail();
			// PASS:0879 If ERROR - syntax error or access rule violation?
		} catch (SQLException sqle) {
		}

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("alter table u0879 drop constraint mycons");
			stmt
					.executeUpdate("ALTER TABLE T0879 "
							+ "DROP CONSTRAINT REFME ;");
			// TODO: drop the references constraint on U0879
		} else
			stmt.executeUpdate("ALTER TABLE T0879 "
					+ "DROP CONSTRAINT REFME CASCADE;");

		stmt.executeUpdate("INSERT INTO U0879 VALUES (20);");
		// PASS:0879 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO T0879 VALUES (0, 0);");
		// PASS:0879 If 1 row inserted successfully?

		// END TEST >>> 0879 <<< END TEST

	}
	// testDml_169 is about FIPS flagging and is to be skipped
	// TEST:0877 Intermediate DB, Flag at Entry level!
	/*
	 * 
	 * testDml_170_LongConstraintNames()
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_170_LongConstraintNames() throws SQLException {

		// TEST:0880 Long constraint names, cursor names!

		stmt.executeUpdate("CREATE TABLE T0880 ( "
				+ "C1 INT not null, C2 INT not null, " + "CONSTRAINT "
				+ "\"It was the best of times; it wa\""
				+ "PRIMARY KEY (C1, C2));");
		// PASS:0880 If table created successfully?

		stmt.executeUpdate("INSERT INTO T0880 VALUES (0, 1);");
		// PASS:0880 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO T0880 VALUES (1, 2);");
		// PASS:0880 If 1 row inserted successfully?

		try {
			stmt.executeUpdate("INSERT INTO T0880 VALUES (1, 2);");
			fail();
			// PASS:0880 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT C1 FROM T0880 ORDER BY C1;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0880 If 2 rows are returned in the following order?
		//                c1
		//                ==
		// PASS:0880 If 0 ?
		// PASS:0880 If 1 ?

		stmt.executeUpdate("ALTER TABLE T0880 " + "DROP CONSTRAINT "
				+ "\"It was the best of times; it wa\" " + ";");
		// PASS:0880 If table altered successfully?

		stmt.executeUpdate("INSERT INTO T0880 VALUES (0, 1);");
		// PASS:0880 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM T0880;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:0880 If COUNT = 3?

		// END TEST >>> 0880 <<< END TEST
		// *********************************************

		// TEST:0881 Long character set names, domain names!
	}
	/*
	 * 
	 * testDml_170_LongDomainNames()
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_170_LongDomainNames() throws SQLException {

		stmt.executeUpdate("CREATE DOMAIN "
				+ "\"Little boxes on the hillside, L\"" + "CHAR (4) ");
		// PASS:0881 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE T0881 ( C1 "
				+ "\"Little boxes on the hillside, L\" )");
		// PASS:0881 If table created successfully?

		stmt.executeUpdate("INSERT INTO T0881 VALUES ('ABCD');");
		// PASS:0881 If insert completed successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM T0881 "
				+ "WHERE C1 = 'ABCD';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0881 If COUNT = 1?

		stmt.executeUpdate("DROP TABLE T0881");

		stmt.executeUpdate("DROP DOMAIN "
				+ "\"Little boxes on the hillside, L\"");
		// PASS:0881 If domain dropped successfully?

		// END TEST >>> 0881 <<< END TEST}
	}
	/*
	 * 
	 * testDml_171_MoreFullOuterJoins
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_171_MoreFullOuterJoins() throws SQLException {
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;
		BaseTab.setupBaseTab(stmt);
		// TEST:0882 More full outer join!

		stmt.executeUpdate("CREATE TABLE STAFF66 ( " + "SALARY   INTEGER, "
				+ "EMPNAME CHAR(20), " + "GRADE   DECIMAL, "
				+ "EMPNUM  CHAR(3));");
		// PASS:0882 If table created successfully?

		stmt.executeUpdate("INSERT INTO STAFF66 "
				+ "SELECT GRADE*1000, EMPNAME, GRADE, EMPNUM "
				+ "FROM STAFF3 WHERE EMPNUM > 'E2';");
		// PASS:0882 If insert completed successfully?

		stmt.executeUpdate("UPDATE STAFF3 SET EMPNUM = 'E6' "
				+ "WHERE EMPNUM = 'E5';");
		// PASS:0882 If update completed successfully?

		stmt.executeUpdate("UPDATE STAFF3 SET EMPNAME = 'Ali' "
				+ "WHERE GRADE = 12;");
		// PASS:0882 If update completed successfully?

		rs = stmt.executeQuery("SELECT EMPNUM, CITY, SALARY "
				+ "FROM STAFF3 FULL OUTER JOIN STAFF66 USING (EMPNUM) "
				+ "ORDER BY EMPNUM;");
		// PASS:0882 If 6 rows are returned in the following order?
		//               empnum city salary
		//               ====== ==== ======
		// PASS:0882 If E1 Deale NULL ?
		// PASS:0882 If E2 Vienna NULL ?
		// PASS:0882 If E3 Vienna 13000 ?
		// PASS:0882 If E4 Deale 12000 ?
		// PASS:0882 If E5 NULL 13000 ?
		// PASS:0882 If E6 Akron NULL ?

		// END TEST >>> 0882 <<< END TEST

	}
	/*
	 * 
	 * testDml_172_SqlTextInColumnDefinition
	 * 
	 * Notes: TEST:0884 ASCII_FULL and SQL_TEXT in column definition!
	 *  
	 */
	public void _testDml_172_SqlTextInColumnDefinition() throws SQLException {
		
		// 2005/03/30 commented out, since this doesn't work for FB2, or FB1.5
		// but it does work for Vulcan. 

		// firebird doesn't support ASCII_FULL, but we do have ISO8859_1
		stmt.executeUpdate("CREATE TABLE T0884 ( "
				+ "C1 CHAR (4) CHARACTER SET ISO8859_1, "
				+ "C2 CHAR (4) CHARACTER SET SQL_TEXT);");
		// PASS:0884 If table created successfully?

		stmt.executeUpdate("INSERT INTO T0884 VALUES (_ISO8859_1 '^#|~', "
				+ "_SQL_TEXT 'This');");
		// PASS:0884 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM T0884 "
				+ "WHERE C1 = _ISO8859_1 '^#|~' "
				+ "AND C2 = _SQL_TEXT 'This';");
		// PASS:0884 If COUNT = 1?

		// END TEST >>> 0884 <<< END TEST

	}
	/*
	 * 
	 * testDml_173_FipsSizingChar1000
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_173_FipsSizingChar1000() throws SQLException {

		// TEST:0885 FIPS sizing, CHAR (1000)!
		stmt.executeUpdate("CREATE TABLE CONTACTS ( " + "NAME CHAR (20), "
				+ "DESCRIPTION CHAR (1000), " + "KEYWORDS CHAR (1000)); ");
		// PASS:0885 If table created successfully?

		String query = "insert into contacts values ('Harry', ";
		query = query
				+ "'Harry works in the Redundancy Automation Division of the ' || "
				+ "'Materials ' || "
				+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
				+ "'Project of ' || "
				+ "'lower Michigan.  His job is to document the trajectory of ' || "
				+ "'cattle and ' || "
				+ "'correlate the loft and acceleration versus the quality of ' || "
				+ "'materials ' || "
				+ "'used in the trebuchet.  He served ten years as the ' || "
				+ "'vice-president in ' || "
				+ "'charge of marketing in the now defunct milk trust of the ' || "
				+ "'Pennsylvania ' || "
				+ "'Coalition of All Things Bovine.  Prior to that he ' || "
				+ "'established himself ' || "
				+ "'as a world-class graffiti artist and source of all good ' || "
				+ "'bits related ' || "
				+ "'to channel dredging in poor weather.  He is author of over ' || "
				+ "'ten thousand ' || "
				+ "'paperback novels, including such titles as \"How Many ' || "
				+ "'Pumpkins will Fit ' || "
				+ "'on the Head of a Pin,\" \"A Whole Bunch of Useless Things ' || "
				+ "'that you Don''t ' || "
				+ "'Want to Know,\" and \"How to Lift Heavy Things Over your ' || "
				+ "'Head without ' || "
				+ "'Hurting Yourself or Dropping Them.\"  He attends ANSI and ' || "
				+ "'ISO standards ' || "
				+ "'meetings in his copious free time and funds the development ' || "
				+ "'of test ' || " + "'suites with his pocket change.', "
				+ "'aardvark albatross nutmeg redundancy ' || "
				+ "'automation materials blasting ' || "
				+ "'cattle acceleration trebuchet catapult ' || "
				+ "'loft coffee java sendmail SMTP ' || "
				+ "'FTP HTTP censorship expletive senility ' || "
				+ "'extortion distortion conformity ' || "
				+ "'conformance nachos chicks goslings ' || "
				+ "'ducklings honk quack melatonin tie ' || "
				+ "'noose circulation column default ' || "
				+ "'ionic doric chlorine guanine Guam ' || "
				+ "'invasions rubicon helmet plastics ' || "
				+ "'recycle HDPE nylon ceramics plumbing ' || "
				+ "'parachute zeppelin carbon hydrogen ' || "
				+ "'vinegar sludge asphalt adhesives ' || "
				+ "'tensile magnetic Ellesmere Greenland ' || "
				+ "'Knud Rasmussen precession ' || "
				+ "'navigation positioning orbit altitude ' || "
				+ "'resistance radiation levitation ' || "
				+ "'yoga demiurge election violence ' || "
				+ "'collapsed fusion cryogenics gravity ' || "
				+ "'sincerity idiocy budget accounting ' || "
				+ "'auditing titanium torque pressure ' || "
				+ "'fragile hernia muffler cartilage ' || "
				+ "'graphics deblurring headache eyestrain ' || "
				+ "'interlace bandwidth resolution ' || "
				+ "'determination steroids barrel oak wine ' || "
				+ "'ferment yeast brewing bock siphon ' || "
				+ "'clarity impurities SQL RBAC data ' || "
				+ "'warehouse security integrity feedback'";
		query = query + ");";
		// PASS:0885 If 1 row inserted successfully?

		stmt.executeUpdate(query);

		rs = stmt
				.executeQuery("SELECT COUNT(*) "
						+ "FROM CONTACTS "
						+ "WHERE DESCRIPTION = "
						+ "'Harry works in the Redundancy Automation Division of the ' || "
						+ "'Materials ' || "
						+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
						+ "'Project of ' || "
						+ "'lower Michigan.  His job is to document the trajectory of ' || "
						+ "'cattle and ' || "
						+ "'correlate the loft and acceleration versus the quality of ' || "
						+ "'materials ' || "
						+ "'used in the trebuchet.  He served ten years as the ' || "
						+ "'vice-president in ' || "
						+ "'charge of marketing in the now defunct milk trust of the ' || "
						+ "'Pennsylvania ' || "
						+ "'Coalition of All Things Bovine.  Prior to that he ' || "
						+ "'established himself ' || "
						+ "'as a world-class graffiti artist and source of all good ' || "
						+ "'bits related ' || "
						+ "'to channel dredging in poor weather.  He is author of over ' || "
						+ "'ten thousand ' || "
						+ "'paperback novels, including such titles as \"How Many ' || "
						+ "'Pumpkins will Fit ' || "
						+ "'on the Head of a Pin,\" \"A Whole Bunch of Useless Things ' || "
						+ "'that you Don''t ' || "
						+ "'Want to Know,\" and \"How to Lift Heavy Things Over your ' || "
						+ "'Head without ' || "
						+ "'Hurting Yourself or Dropping Them.\"  He attends ANSI and ' || "
						+ "'ISO standards ' || "
						+ "'meetings in his copious free time and funds the development ' || "
						+ "'of test ' || "
						+ "'suites with his pocket change.' "
						+ "AND KEYWORDS = "
						+ "'aardvark albatross nutmeg redundancy ' || "
						+ "'automation materials blasting ' || "
						+ "'cattle acceleration trebuchet catapult ' || "
						+ "'loft coffee java sendmail SMTP ' || "
						+ "'FTP HTTP censorship expletive senility ' || "
						+ "'extortion distortion conformity ' || "
						+ "'conformance nachos chicks goslings ' || "
						+ "'ducklings honk quack melatonin tie ' || "
						+ "'noose circulation column default ' || "
						+ "'ionic doric chlorine guanine Guam ' || "
						+ "'invasions rubicon helmet plastics ' || "
						+ "'recycle HDPE nylon ceramics plumbing ' || "
						+ "'parachute zeppelin carbon hydrogen ' || "
						+ "'vinegar sludge asphalt adhesives ' || "
						+ "'tensile magnetic Ellesmere Greenland ' || "
						+ "'Knud Rasmussen precession ' || "
						+ "'navigation positioning orbit altitude ' || "
						+ "'resistance radiation levitation ' || "
						+ "'yoga demiurge election violence ' || "
						+ "'collapsed fusion cryogenics gravity ' || "
						+ "'sincerity idiocy budget accounting ' || "
						+ "'auditing titanium torque pressure ' || "
						+ "'fragile hernia muffler cartilage ' || "
						+ "'graphics deblurring headache eyestrain ' || "
						+ "'interlace bandwidth resolution ' || "
						+ "'determination steroids barrel oak wine ' || "
						+ "'ferment yeast brewing bock siphon ' || "
						+ "'clarity impurities SQL RBAC data ' || "
						+ "'warehouse security integrity feedback';");
		// PASS:0885 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM CONTACTS "
				+ "WHERE DESCRIPTION LIKE '%change.' "
				+ "AND KEYWORDS LIKE '%feedback'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0885 If COUNT = 1?

		// END TEST >>> 0885 <<< END TEST
	}
	/*
	 * 
	 * testDml_174_FipsSizingVarChar1000
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_174_FipsSizingVarChar1000() throws SQLException {

		// TEST:0885 FIPS sizing, CHAR (1000)!
		stmt
				.executeUpdate("CREATE TABLE CONTACTS ( " + "NAME CHAR (20), "
						+ "DESCRIPTION VARCHAR (1000), "
						+ "KEYWORDS VARCHAR (1000)); ");
		// PASS:0885 If table created successfully?

		String query = "insert into contacts values ('Harry', ";
		query = query
				+ "'Harry works in the Redundancy Automation Division of the ' || "
				+ "'Materials ' || "
				+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
				+ "'Project of ' || "
				+ "'lower Michigan.  His job is to document the trajectory of ' || "
				+ "'cattle and ' || "
				+ "'correlate the loft and acceleration versus the quality of ' || "
				+ "'materials ' || "
				+ "'used in the trebuchet.  He served ten years as the ' || "
				+ "'vice-president in ' || "
				+ "'charge of marketing in the now defunct milk trust of the ' || "
				+ "'Pennsylvania ' || "
				+ "'Coalition of All Things Bovine.  Prior to that he ' || "
				+ "'established himself ' || "
				+ "'as a world-class graffiti artist and source of all good ' || "
				+ "'bits related ' || "
				+ "'to channel dredging in poor weather.  He is author of over ' || "
				+ "'ten thousand ' || "
				+ "'paperback novels, including such titles as \"How Many ' || "
				+ "'Pumpkins will Fit ' || "
				+ "'on the Head of a Pin,\" \"A Whole Bunch of Useless Things ' || "
				+ "'that you Don''t ' || "
				+ "'Want to Know,\" and \"How to Lift Heavy Things Over your ' || "
				+ "'Head without ' || "
				+ "'Hurting Yourself or Dropping Them.\"  He attends ANSI and ' || "
				+ "'ISO standards ' || "
				+ "'meetings in his copious free time and funds the development ' || "
				+ "'of test ' || " + "'suites with his pocket change.', "
				+ "'aardvark albatross nutmeg redundancy ' || "
				+ "'automation materials blasting ' || "
				+ "'cattle acceleration trebuchet catapult ' || "
				+ "'loft coffee java sendmail SMTP ' || "
				+ "'FTP HTTP censorship expletive senility ' || "
				+ "'extortion distortion conformity ' || "
				+ "'conformance nachos chicks goslings ' || "
				+ "'ducklings honk quack melatonin tie ' || "
				+ "'noose circulation column default ' || "
				+ "'ionic doric chlorine guanine Guam ' || "
				+ "'invasions rubicon helmet plastics ' || "
				+ "'recycle HDPE nylon ceramics plumbing ' || "
				+ "'parachute zeppelin carbon hydrogen ' || "
				+ "'vinegar sludge asphalt adhesives ' || "
				+ "'tensile magnetic Ellesmere Greenland ' || "
				+ "'Knud Rasmussen precession ' || "
				+ "'navigation positioning orbit altitude ' || "
				+ "'resistance radiation levitation ' || "
				+ "'yoga demiurge election violence ' || "
				+ "'collapsed fusion cryogenics gravity ' || "
				+ "'sincerity idiocy budget accounting ' || "
				+ "'auditing titanium torque pressure ' || "
				+ "'fragile hernia muffler cartilage ' || "
				+ "'graphics deblurring headache eyestrain ' || "
				+ "'interlace bandwidth resolution ' || "
				+ "'determination steroids barrel oak wine ' || "
				+ "'ferment yeast brewing bock siphon ' || "
				+ "'clarity impurities SQL RBAC data ' || "
				+ "'warehouse security integrity feedback'";
		query = query + ");";
		// PASS:0885 If 1 row inserted successfully?

		stmt.executeUpdate(query);

		rs = stmt
				.executeQuery("SELECT COUNT(*) "
						+ "FROM CONTACTS "
						+ "WHERE DESCRIPTION = "
						+ "'Harry works in the Redundancy Automation Division of the ' || "
						+ "'Materials ' || "
						+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
						+ "'Project of ' || "
						+ "'lower Michigan.  His job is to document the trajectory of ' || "
						+ "'cattle and ' || "
						+ "'correlate the loft and acceleration versus the quality of ' || "
						+ "'materials ' || "
						+ "'used in the trebuchet.  He served ten years as the ' || "
						+ "'vice-president in ' || "
						+ "'charge of marketing in the now defunct milk trust of the ' || "
						+ "'Pennsylvania ' || "
						+ "'Coalition of All Things Bovine.  Prior to that he ' || "
						+ "'established himself ' || "
						+ "'as a world-class graffiti artist and source of all good ' || "
						+ "'bits related ' || "
						+ "'to channel dredging in poor weather.  He is author of over ' || "
						+ "'ten thousand ' || "
						+ "'paperback novels, including such titles as \"How Many ' || "
						+ "'Pumpkins will Fit ' || "
						+ "'on the Head of a Pin,\" \"A Whole Bunch of Useless Things ' || "
						+ "'that you Don''t ' || "
						+ "'Want to Know,\" and \"How to Lift Heavy Things Over your ' || "
						+ "'Head without ' || "
						+ "'Hurting Yourself or Dropping Them.\"  He attends ANSI and ' || "
						+ "'ISO standards ' || "
						+ "'meetings in his copious free time and funds the development ' || "
						+ "'of test ' || "
						+ "'suites with his pocket change.' "
						+ "AND KEYWORDS = "
						+ "'aardvark albatross nutmeg redundancy ' || "
						+ "'automation materials blasting ' || "
						+ "'cattle acceleration trebuchet catapult ' || "
						+ "'loft coffee java sendmail SMTP ' || "
						+ "'FTP HTTP censorship expletive senility ' || "
						+ "'extortion distortion conformity ' || "
						+ "'conformance nachos chicks goslings ' || "
						+ "'ducklings honk quack melatonin tie ' || "
						+ "'noose circulation column default ' || "
						+ "'ionic doric chlorine guanine Guam ' || "
						+ "'invasions rubicon helmet plastics ' || "
						+ "'recycle HDPE nylon ceramics plumbing ' || "
						+ "'parachute zeppelin carbon hydrogen ' || "
						+ "'vinegar sludge asphalt adhesives ' || "
						+ "'tensile magnetic Ellesmere Greenland ' || "
						+ "'Knud Rasmussen precession ' || "
						+ "'navigation positioning orbit altitude ' || "
						+ "'resistance radiation levitation ' || "
						+ "'yoga demiurge election violence ' || "
						+ "'collapsed fusion cryogenics gravity ' || "
						+ "'sincerity idiocy budget accounting ' || "
						+ "'auditing titanium torque pressure ' || "
						+ "'fragile hernia muffler cartilage ' || "
						+ "'graphics deblurring headache eyestrain ' || "
						+ "'interlace bandwidth resolution ' || "
						+ "'determination steroids barrel oak wine ' || "
						+ "'ferment yeast brewing bock siphon ' || "
						+ "'clarity impurities SQL RBAC data ' || "
						+ "'warehouse security integrity feedback';");
		// PASS:0885 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM CONTACTS "
				+ "WHERE DESCRIPTION LIKE '%change.' "
				+ "AND KEYWORDS LIKE '%feedback'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0885 If COUNT = 1?

		// END TEST >>> 0885 <<< END TEST
	}
	/*
	 * 
	 * testDml_175_FipsSizingNChar500
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_175_FipsSizingNChar500() throws SQLException {

		// TEST:0885 FIPS sizing, CHAR (1000)!
		
		// Firebird doesn't support the N' prefix for unicode literals
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return; 

		stmt.executeUpdate("CREATE TABLE CONTACTS ( " + "NAME CHAR (20), "
				+ "DESCRIPTION NCHAR (500), " + "KEYWORDS NCHAR (500)); ");
		// PASS:0885 If table created successfully?

		String query = "insert into contacts values ('Harry', ";
		query = query
				+ "N'Harry works in the Redundancy Automation Division of the ' || "
				+ "'Materials ' || "
				+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
				+ "'Project of ' || "
				+ "'lower Michigan.  His job is to document the trajectory of ' || "
				+ "'cattle and ' || "
				+ "'correlate the loft and acceleration versus the quality of ' || "
				+ "'materials ' || "
				+ "'used in the trebuchet.  He served ten years as the ' || "
				+ "'vice-president in ' || "
				+ "'charge of marketing in the now defunct milk trust of the ' || "
				+ "'Pennsylvania ' || "
				+ "'Coalition of All Things Bovine.  Prior to that he ' || "
				+ "'established himself ' || " + "'as a world-class gra', "
				+ "N'aardvark albatross nutmeg redundancy ' || "
				+ "'automation materials blasting ' || "
				+ "'cattle acceleration trebuchet catapult ' || "
				+ "'loft coffee java sendmail SMTP ' || "
				+ "'FTP HTTP censorship expletive senility ' || "
				+ "'extortion distortion conformity ' || "
				+ "'conformance nachos chicks goslings ' || "
				+ "'ducklings honk quack melatonin tie ' || "
				+ "'noose circulation column default ' || "
				+ "'ionic doric chlorine guanine Guam ' || "
				+ "'invasions rubicon helmet plastics ' || "
				+ "'recycle HDPE nylon ceramics plumbing ' || "
				+ "'parachute zeppelin carbon hydrogen ' || "
				+ "'vinegar sludge asphalt adhesives ' || "
				+ "'tensile magnetic'";
		query = query + ");";
		// PASS:0885 If 1 row inserted successfully?

		stmt.executeUpdate(query);

		rs = stmt
				.executeQuery("SELECT COUNT(*) "
						+ "FROM CONTACTS "
						+ "WHERE DESCRIPTION = "
						+ "N'Harry works in the Redundancy Automation Division of the ' || "
						+ "'Materials ' || "
						+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
						+ "'Project of ' || "
						+ "'lower Michigan.  His job is to document the trajectory of ' || "
						+ "'cattle and ' || "
						+ "'correlate the loft and acceleration versus the quality of ' || "
						+ "'materials ' || "
						+ "'used in the trebuchet.  He served ten years as the ' || "
						+ "'vice-president in ' || "
						+ "'charge of marketing in the now defunct milk trust of the ' || "
						+ "'Pennsylvania ' || "
						+ "'Coalition of All Things Bovine.  Prior to that he ' || "
						+ "'established himself ' || "
						+ "'as a world-class gra' " + "AND KEYWORDS = "
						+ "N'aardvark albatross nutmeg redundancy ' || "
						+ "'automation materials blasting ' || "
						+ "'cattle acceleration trebuchet catapult ' || "
						+ "'loft coffee java sendmail SMTP ' || "
						+ "'FTP HTTP censorship expletive senility ' || "
						+ "'extortion distortion conformity ' || "
						+ "'conformance nachos chicks goslings ' || "
						+ "'ducklings honk quack melatonin tie ' || "
						+ "'noose circulation column default ' || "
						+ "'ionic doric chlorine guanine Guam ' || "
						+ "'invasions rubicon helmet plastics ' || "
						+ "'recycle HDPE nylon ceramics plumbing ' || "
						+ "'parachute zeppelin carbon hydrogen ' || "
						+ "'vinegar sludge asphalt adhesives ' || "
						+ "'tensile magnetic';");
		// PASS:0885 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM CONTACTS "
				+ "WHERE DESCRIPTION LIKE N'%change.' "
				+ "AND KEYWORDS LIKE N'%feedback'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0885 If COUNT = 1?

		// END TEST >>> 0885 <<< END TEST
	}
	/*
	 * 
	 * testDml_176_FipsSizingNCharVarying500
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_176_FipsSizingNCharVarying500() throws SQLException {

		// TEST:0885 FIPS sizing, CHAR (1000)!

		// Firebird doesn't support the N' prefix for unicode literals
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return; 

		stmt.executeUpdate("CREATE TABLE CONTACTS ( " + "NAME CHAR (20), "
				+ "DESCRIPTION NCHAR VARYING (500), "
				+ "KEYWORDS NCHAR VARYING (500)); ");
		// PASS:0885 If table created successfully?

		String query = "insert into contacts values ('Harry', ";
		query = query
				+ "N'Harry works in the Redundancy Automation Division of the ' || "
				+ "'Materials ' || "
				+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
				+ "'Project of ' || "
				+ "'lower Michigan.  His job is to document the trajectory of ' || "
				+ "'cattle and ' || "
				+ "'correlate the loft and acceleration versus the quality of ' || "
				+ "'materials ' || "
				+ "'used in the trebuchet.  He served ten years as the ' || "
				+ "'vice-president in ' || "
				+ "'charge of marketing in the now defunct milk trust of the ' || "
				+ "'Pennsylvania ' || "
				+ "'Coalition of All Things Bovine.  Prior to that he ' || "
				+ "'established himself ' || " + "'as a world-class gra', "
				+ "N'aardvark albatross nutmeg redundancy ' || "
				+ "'automation materials blasting ' || "
				+ "'cattle acceleration trebuchet catapult ' || "
				+ "'loft coffee java sendmail SMTP ' || "
				+ "'FTP HTTP censorship expletive senility ' || "
				+ "'extortion distortion conformity ' || "
				+ "'conformance nachos chicks goslings ' || "
				+ "'ducklings honk quack melatonin tie ' || "
				+ "'noose circulation column default ' || "
				+ "'ionic doric chlorine guanine Guam ' || "
				+ "'invasions rubicon helmet plastics ' || "
				+ "'recycle HDPE nylon ceramics plumbing ' || "
				+ "'parachute zeppelin carbon hydrogen ' || "
				+ "'vinegar sludge asphalt adhesives ' || "
				+ "'tensile magnetic'";
		query = query + ");";
		// PASS:0885 If 1 row inserted successfully?

		stmt.executeUpdate(query);

		rs = stmt
				.executeQuery("SELECT COUNT(*) "
						+ "FROM CONTACTS "
						+ "WHERE DESCRIPTION = "
						+ "N'Harry works in the Redundancy Automation Division of the ' || "
						+ "'Materials ' || "
						+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
						+ "'Project of ' || "
						+ "'lower Michigan.  His job is to document the trajectory of ' || "
						+ "'cattle and ' || "
						+ "'correlate the loft and acceleration versus the quality of ' || "
						+ "'materials ' || "
						+ "'used in the trebuchet.  He served ten years as the ' || "
						+ "'vice-president in ' || "
						+ "'charge of marketing in the now defunct milk trust of the ' || "
						+ "'Pennsylvania ' || "
						+ "'Coalition of All Things Bovine.  Prior to that he ' || "
						+ "'established himself ' || "
						+ "'as a world-class gra' " + "AND KEYWORDS = "
						+ "N'aardvark albatross nutmeg redundancy ' || "
						+ "'automation materials blasting ' || "
						+ "'cattle acceleration trebuchet catapult ' || "
						+ "'loft coffee java sendmail SMTP ' || "
						+ "'FTP HTTP censorship expletive senility ' || "
						+ "'extortion distortion conformity ' || "
						+ "'conformance nachos chicks goslings ' || "
						+ "'ducklings honk quack melatonin tie ' || "
						+ "'noose circulation column default ' || "
						+ "'ionic doric chlorine guanine Guam ' || "
						+ "'invasions rubicon helmet plastics ' || "
						+ "'recycle HDPE nylon ceramics plumbing ' || "
						+ "'parachute zeppelin carbon hydrogen ' || "
						+ "'vinegar sludge asphalt adhesives ' || "
						+ "'tensile magnetic';");
		// PASS:0885 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM CONTACTS "
				+ "WHERE DESCRIPTION LIKE N'%change.' "
				+ "AND KEYWORDS LIKE N'%feedback'; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0885 If COUNT = 1?

		// END TEST >>> 0885 <<< END TEST
	}
	/*
	 * 
	 * testDml_177_FipsSizingIntegerBinaryPrecision
	 *  
	 */
	public void testDml_177_FipsSizingIntegerBinaryPrecision()
			throws SQLException {

		// TEST:0889 FIPS sizing, INTEGER binary prec >= 31!

		stmt.executeUpdate("CREATE TABLE NOMAIL (C1 INT);");
		// PASS:0889 If table created successfully?

		stmt.executeUpdate("INSERT INTO NOMAIL VALUES (2147483647);");
		// PASS:0889 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO NOMAIL VALUES (-2147483647);");
		// PASS:0889 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT C1 " + "FROM NOMAIL WHERE C1 > 0; ");
		rs.next();
		assertEquals(2147483647, rs.getInt(1));
		// PASS:0889 If C1 = 2147483647?

		rs = stmt.executeQuery("SELECT C1 " + "FROM NOMAIL WHERE C1 < 0; ");
		rs.next();
		assertEquals(-2147483647, rs.getInt(1));
		// PASS:0889 If C1 = -2147483647?

		rs = stmt.executeQuery("SELECT C1 - 2147483646 " + "FROM NOMAIL "
				+ "WHERE C1 > 0; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0889 If value = 1?

		rs = stmt.executeQuery("SELECT C1 + 2147483646 " + "FROM NOMAIL "
				+ "WHERE C1 < 0;");
		rs.next();
		assertEquals(-1, rs.getInt(1));
		// PASS:0889 If value = -1?

		stmt.executeUpdate("UPDATE NOMAIL " + "SET C1 = C1 + 2147483646 "
				+ "WHERE C1 < 0; ");
		// PASS:0889 If update completed successfully?

		rs = stmt.executeQuery("SELECT C1 " + "FROM NOMAIL WHERE C1 < 0; ");
		rs.next();
		assertEquals(-1, rs.getInt(1));
		// PASS:0889 If C1 = -1?

		stmt.executeUpdate("UPDATE NOMAIL " + "SET C1 = C1 - 1;");
		// PASS:0889 If update completed successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM NOMAIL "
				+ "WHERE C1 = 2147483645;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0889 If COUNT = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM NOMAIL "
				+ "WHERE C1 = 2147483646; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0889 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM NOMAIL "
				+ "WHERE C1 = 2147483647; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0889 If COUNT = 0?

		// END TEST >>> 0889 <<< END TEST
		// *********************************************

	}
	/*
	 * 
	 * testDml_177_FipsSizingSmallIntBinaryPrecision
	 *  
	 */
	public void testDml_177_FipsSizingSmallIntBinaryPrecision()
			throws SQLException {
		// TEST:0890 FIPS sizing, SMALLINT binary prec >= 15!

		stmt.executeUpdate("CREATE TABLE YESMAIL (C1 SMALLINT); ");
		// PASS:0890 If table created successfully?

		stmt.executeUpdate("INSERT INTO YESMAIL VALUES (32767);");
		// PASS:0890 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO YESMAIL VALUES (-32767); ");
		// PASS:0890 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT C1 " + "FROM YESMAIL WHERE C1 > 0; ");
		rs.next();
		assertEquals(32767, rs.getInt(1));
		// PASS:0890 If C1 = 32767?

		rs = stmt.executeQuery("SELECT C1 " + "FROM YESMAIL WHERE C1 < 0; ");
		rs.next();
		assertEquals(-32767, rs.getInt(1));
		// PASS:0890 If C1 = -32767?

		rs = stmt.executeQuery("SELECT C1 - 32766 " + "FROM YESMAIL "
				+ "WHERE C1 > 0; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0890 If value = 1?

		rs = stmt.executeQuery("SELECT C1 + 32766 " + "FROM YESMAIL "
				+ "WHERE C1 < 0; ");
		rs.next();
		assertEquals(-1, rs.getInt(1));
		// PASS:0890 If value = -1?

		stmt.executeUpdate("UPDATE YESMAIL " + "SET C1 = C1 + 32766 "
				+ "WHERE C1 < 0;");
		// PASS:0890 If update completed successfully?

		rs = stmt.executeQuery("SELECT C1 " + "FROM YESMAIL WHERE C1 < 0; ");
		rs.next();
		assertEquals(-1, rs.getInt(1));
		// PASS:0890 If C1 = -1?

		stmt.executeUpdate("UPDATE YESMAIL " + "SET C1 = C1 - 1;");
		// PASS:0890 If update completed successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM YESMAIL "
				+ "WHERE C1 = 32765; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0890 If COUNT = 0?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM YESMAIL "
				+ "WHERE C1 = 32766; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0890 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM YESMAIL "
				+ "WHERE C1 = 32767; ");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0890 If COUNT = 0?

		// END TEST >>> 0890 <<< END TEST

	}

	/*
	 * 
	 * testDml_178_FipsSizing250Columns
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_178_FipsSizing250Columns() throws SQLException {

		// TEST:0891 FIPS sizing, 250 columns, 4000 char data statement!

		stmt
				.executeUpdate("CREATE TABLE L1 ( "
						+ "C1 INT, C2 INT, C3 INT, C4 INT, C5 INT, C6 INT,  "
						+ "C7 INT, C8 INT, C9 INT, C10 INT, C11 INT, C12 INT,  "
						+ "C13 INT, C14 INT, C15 INT, C16 INT, C17 INT, C18 INT,  "
						+ "C19 INT, C20 INT, C21 INT, C22 INT, C23 INT, C24 INT,  "
						+ "C25 INT, C26 INT, C27 INT, C28 INT, C29 INT, C30 INT,  "
						+ "C31 INT, C32 INT, C33 INT, C34 INT, C35 INT, C36 INT,  "
						+ "C37 INT, C38 INT, C39 INT, C40 INT, C41 INT, C42 INT,  "
						+ "C43 INT, C44 INT, C45 INT, C46 INT, C47 INT, C48 INT,  "
						+ "C49 INT, C50 INT, C51 INT, C52 INT, C53 INT, C54 INT,  "
						+ "C55 INT, C56 INT, C57 INT, C58 INT, C59 INT, C60 INT,  "
						+ "C61 INT, C62 INT, C63 INT, C64 INT, C65 INT, C66 INT,  "
						+ "C67 INT, C68 INT, C69 INT, C70 INT, C71 INT, C72 INT,  "
						+ "C73 INT, C74 INT, C75 INT, C76 INT, C77 INT, C78 INT,  "
						+ "C79 INT, C80 INT, C81 INT, C82 INT, C83 INT, C84 INT,  "
						+ "C85 INT, C86 INT, C87 INT, C88 INT, C89 INT, C90 INT,  "
						+ "C91 INT, C92 INT, C93 INT, C94 INT, C95 INT, C96 INT,  "
						+ "C97 INT, C98 INT, C99 INT, C100 INT, C101 INT, C102 INT,  "
						+ "C103 INT, C104 INT, C105 INT, C106 INT, C107 INT, C108 INT,  "
						+ "C109 INT, C110 INT, C111 INT, C112 INT, C113 INT, C114 INT,  "
						+ "C115 INT, C116 INT, C117 INT, C118 INT, C119 INT, C120 INT,  "
						+ "C121 INT, C122 INT, C123 INT, C124 INT, C125 INT, C126 INT,  "
						+ "C127 INT, C128 INT, C129 INT, C130 INT, C131 INT, C132 INT,  "
						+ "C133 INT, C134 INT, C135 INT, C136 INT, C137 INT, C138 INT,  "
						+ "C139 INT, C140 INT, C141 INT, C142 INT, C143 INT, C144 INT,  "
						+ "C145 INT, C146 INT, C147 INT, C148 INT, C149 INT, C150 INT,  "
						+ "C151 INT, C152 INT, C153 INT, C154 INT, C155 INT, C156 INT,  "
						+ "C157 INT, C158 INT, C159 INT, C160 INT, C161 INT, C162 INT,  "
						+ "C163 INT, C164 INT, C165 INT, C166 INT, C167 INT, C168 INT,  "
						+ "C169 INT, C170 INT, C171 INT, C172 INT, C173 INT, C174 INT,  "
						+ "C175 INT, C176 INT, C177 INT, C178 INT, C179 INT, C180 INT,  "
						+ "C181 INT, C182 INT, C183 INT, C184 INT, C185 INT, C186 INT,  "
						+ "C187 INT, C188 INT, C189 INT, C190 INT, C191 INT, C192 INT,  "
						+ "C193 INT, C194 INT, C195 INT, C196 INT, C197 INT, C198 INT,  "
						+ "C199 INT, C200 INT, C201 INT, C202 INT, C203 INT, C204 INT,  "
						+ "C205 INT, C206 INT, C207 INT, C208 INT, C209 INT, C210 INT,  "
						+ "C211 INT, C212 INT, C213 INT, C214 INT, C215 INT, C216 INT,  "
						+ "C217 INT, C218 INT, C219 INT, C220 INT, C221 INT, C222 INT,  "
						+ "C223 INT, C224 INT, C225 INT, C226 INT, C227 INT, C228 INT,  "
						+ "C229 INT, C230 INT, C231 INT, C232 INT, C233 INT, C234 INT,  "
						+ "C235 INT, C236 INT, C237 INT, C238 INT, C239 INT, C240 INT,  "
						+ "C241 INT, C242 INT, C243 INT, C244 INT, C245 INT, C246 INT,  "
						+ "C247 INT, C248 INT, C249 INT, C250 INT);");
		// PASS:0891 If table created successfully?

		stmt.executeUpdate("INSERT INTO L1 VALUES ( " + "1, 2, 3, 4, 5, 6, "
				+ "7, 8, 9, 10, 11, 12, " + "13, 14, 15, 16, 17, 18, "
				+ "19, 20, 21, 22, 23, 24, " + "25, 26, 27, 28, 29, 30, "
				+ "31, 32, 33, 34, 35, 36, " + "37, 38, 39, 40, 41, 42, "
				+ "43, 44, 45, 46, 47, 48, " + "49, 50, 51, 52, 53, 54, "
				+ "55, 56, 57, 58, 59, 60, " + "61, 62, 63, 64, 65, 66, "
				+ "67, 68, 69, 70, 71, 72, " + "73, 74, 75, 76, 77, 78, "
				+ "79, 80, 81, 82, 83, 84, " + "85, 86, 87, 88, 89, 90, "
				+ "91, 92, 93, 94, 95, 96, " + "97, 98, 99, 100, 101, 102, "
				+ "103, 104, 105, 106, 107, 108, "
				+ "109, 110, 111, 112, 113, 114, "
				+ "115, 116, 117, 118, 119, 120, "
				+ "121, 122, 123, 124, 125, 126, "
				+ "127, 128, 129, 130, 131, 132, "
				+ "133, 134, 135, 136, 137, 138, "
				+ "139, 140, 141, 142, 143, 144, "
				+ "145, 146, 147, 148, 149, 150, "
				+ "151, 152, 153, 154, 155, 156, "
				+ "157, 158, 159, 160, 161, 162, "
				+ "163, 164, 165, 166, 167, 168, "
				+ "169, 170, 171, 172, 173, 174, "
				+ "175, 176, 177, 178, 179, 180, "
				+ "181, 182, 183, 184, 185, 186, "
				+ "187, 188, 189, 190, 191, 192, "
				+ "193, 194, 195, 196, 197, 198, "
				+ "199, 200, 201, 202, 203, 204, "
				+ "205, 206, 207, 208, 209, 210, "
				+ "211, 212, 213, 214, 215, 216, "
				+ "217, 218, 219, 220, 221, 222, "
				+ "223, 224, 225, 226, 227, 228, "
				+ "229, 230, 231, 232, 233, 234, "
				+ "235, 236, 237, 238, 239, 240, "
				+ "241, 242, 243, 244, 245, 246, " + "247, 248, 249, 250);");
		// PASS:0891 If 1 row inserted successfully?

		stmt.executeUpdate("UPDATE L1 SET "
				+ "C1 = C1 + 1, C2 = C2 + 1, C3 = C3 + 1,"
				+ "C4 = C4 + 1, C5 = C5 + 1, C6 = C6 + 1, "
				+ "C7 = C7 + 1, C8 = C8 + 1, C9 = C9 + 1, "
				+ "C10 = C10 + 1, C11 = C11 + 1, C12 = C12 + 1, "
				+ "C13 = C13 + 1, C14 = C14 + 1, C15 = C15 + 1,  "
				+ "C16 = C16 + 1, C17 = C17 + 1, C18 = C18 + 1,  "
				+ "C19 = C19 + 1, C20 = C20 + 1, C21 = C21 + 1,  "
				+ "C22 = C22 + 1, C23 = C23 + 1, C24 = C24 + 1,  "
				+ "C25 = C25 + 1, C26 = C26 + 1, C27 = C27 + 1,  "
				+ "C28 = C28 + 1, C29 = C29 + 1, C30 = C30 + 1,  "
				+ "C31 = C31 + 1, C32 = C32 + 1, C33 = C33 + 1,  "
				+ "C34 = C34 + 1, C35 = C35 + 1, C36 = C36 + 1,  "
				+ "C37 = C37 + 1, C38 = C38 + 1, C39 = C39 + 1,  "
				+ "C40 = C40 + 1, C41 = C41 + 1, C42 = C42 + 1,  "
				+ "C43 = C43 + 1, C44 = C44 + 1, C45 = C45 + 1,  "
				+ "C46 = C46 + 1, C47 = C47 + 1, C48 = C48 + 1,  "
				+ "C49 = C49 + 1, C50 = C50 + 1, C51 = C51 + 1,  "
				+ "C52 = C52 + 1, C53 = C53 + 1, C54 = C54 + 1,  "
				+ "C55 = C55 + 1, C56 = C56 + 1, C57 = C57 + 1,  "
				+ "C58 = C58 + 1, C59 = C59 + 1, C60 = C60 + 1,  "
				+ "C61 = C61 + 1, C62 = C62 + 1, C63 = C63 + 1,  "
				+ "C64 = C64 + 1, C65 = C65 + 1, C66 = C66 + 1,  "
				+ "C67 = C67 + 1, C68 = C68 + 1, C69 = C69 + 1,  "
				+ "C70 = C70 + 1, C71 = C71 + 1, C72 = C72 + 1,  "
				+ "C73 = C73 + 1, C74 = C74 + 1, C75 = C75 + 1,  "
				+ "C76 = C76 + 1, C77 = C77 + 1, C78 = C78 + 1,  "
				+ "C79 = C79 + 1, C80 = C80 + 1, C81 = C81 + 1,  "
				+ "C82 = C82 + 1, C83 = C83 + 1, C84 = C84 + 1,  "
				+ "C85 = C85 + 1, C86 = C86 + 1, C87 = C87 + 1,  "
				+ "C88 = C88 + 1, C89 = C89 + 1, C90 = C90 + 1,  "
				+ "C91 = C91 + 1, C92 = C92 + 1, C93 = C93 + 1,  "
				+ "C94 = C94 + 1, C95 = C95 + 1, C96 = C96 + 1,  "
				+ "C97 = C97 + 1, C98 = C98 + 1, C99 = C99 + 1,  "
				+ "C100 = C100 + 1, C101 = C101 + 1, C102 = C102 + 1, "
				+ "C103 = C103 + 1, C104 = C104 + 1, C105 = C105 + 1,  "
				+ "C106 = C106 + 1, C107 = C107 + 1, C108 = C108 + 1,  "
				+ "C109 = C109 + 1, C110 = C110 + 1, C111 = C111 +1,  "
				+ "C112 = C112 +1, C113 = C113 +1, C114 = C114 +1,  "
				+ "C115 = C115 +1, C116 = C116 +1, C117 = C117 +1,  "
				+ "C118 = C118 +1, C119 = C119 +1, C120 = C120 +1,  "
				+ "C121 = C121 +1, C122 = C122 +1, C123 = C123 +1,  "
				+ "C124 = C124 +1, C125 = C125 +1, C126 = C126 +1,  "
				+ "C127 = C127 +1, C128 = C128 +1, C129 = C129 +1,  "
				+ "C130 = C130 +1, C131 = C131 +1, C132 = C132 +1,  "
				+ "C133 = C133 +1, C134 = C134 +1, C135 = C135 +1,  "
				+ "C136 = C136 +1, C137 = C137 +1, C138 = C138 +1,  "
				+ "C139 = C139 +1, C140 = C140 +1, C141 = C141 +1,  "
				+ "C142 = C142 +1, C143 = C143 +1, C144 = C144 +1,  "
				+ "C145 = C145 +1, C146 = C146 +1, C147 = C147 +1,  "
				+ "C148 = C148 +1, C149 = C149 +1, C150 = C150 +1,  "
				+ "C151 = C151 +1, C152 = C152 +1, C153 = C153 +1,  "
				+ "C154 = C154 +1, C155 = C155 +1, C156 = C156 +1,  "
				+ "C157 = C157 +1, C158 = C158 +1, C159 = C159 +1,  "
				+ "C160 = C160 +1, C161 = C161 +1, C162 = C162 +1,  "
				+ "C163 = C163 +1, C164 = C164 +1, C165 = C165 +1,  "
				+ "C166 = C166 +1, C167 = C167 +1, C168 = C168 +1,  "
				+ "C169 = C169 +1, C170 = C170 +1, C171 = C171 +1,  "
				+ "C172 = C172 +1, C173 = C173 +1, C174 = C174 +1,  "
				+ "C175 = C175 +1, C176 = C176 +1, C177 = C177 +1,  "
				+ "C178 = C178 +1, C179 = C179 +1, C180 = C180 +1,  "
				+ "C181 = C181 +1, C182 = C182 +1, C183 = C183 +1,  "
				+ "C184 = C184 +1, C185 = C185 +1, C186 = C186 +1,  "
				+ "C187 = C187 +1, C188 = C188 +1, C189 = C189 +1,  "
				+ "C190 = C190 +1, C191 = C191 +1, C192 = C192 +1,  "
				+ "C193 = C193 +1, C194 = C194 +1, C195 = C195 +1,  "
				+ "C196 = C196 +1, C197 = C197 +1, C198 = C198 +1,  "
				+ "C199 = C199 +1, C200 = C200 +1, C201 = C201 +1,  "
				+ "C202 = C202 +1, C203 = C203 +1, C204 = C204 +1,  "
				+ "C205 = C205 +1, C206 = C206 +1, C207 = C207 +1,  "
				+ "C208 = C208 +1, C209 = C209 +1, C210 = C210 +1,  "
				+ "C211 = C211 +1, C212 = C212 +1, C213 = C213 +1,  "
				+ "C214 = C214 +1, C215 = C215 +1, C216 = C216 +1,  "
				+ "C217 = C217 +1, C218 = C218 +1, C219 = C219 +1,  "
				+ "C220 = C220 +1, C221 = C221 +1, C222 = C222 +1,  "
				+ "C223 = C223 +1, C224 = C224 +1, C225 = C225 +1,  "
				+ "C226 = C226 +1, C227 = C227 +1, C228 = C228 +1,  "
				+ "C229 = C229 +1, C230 = C230 +1, C231 = C231 +1,  "
				+ "C232 = C232 +1, C233 = C233 +1, C234 = C234 +1,  "
				+ "C235 = C235 +1, C236 = C236 +1, C237 = C237 +1,  "
				+ "C238 = C238 +1, C239 = C239 +1, C240 = C240 +1,  "
				+ "C241 = C241 +1, C242 = C242 +1, C243 = C243 +1,  "
				+ "C244 = C244 +1, C245 = C245 +1, C246 = C246 +1,  "
				+ "C247 = C247 +1, C248 = C248 +1, C249 = C249 +1,  "
				+ "C250 = C250 +1; ");
		// PASS:0891 If update completed successfully?

		rs = stmt.executeQuery("SELECT " + "C1, C2, C3, C4, C5, C6,  "
				+ "C7, C8, C9, C10, C11, C12,  "
				+ "C13, C14, C15, C16, C17, C18,  "
				+ "C19, C20, C21, C22, C23, C24,  "
				+ "C25, C26, C27, C28, C29, C30,  "
				+ "C31, C32, C33, C34, C35, C36,  "
				+ "C37, C38, C39, C40, C41, C42,  "
				+ "C43, C44, C45, C46, C47, C48,  "
				+ "C49, C50, C51, C52, C53, C54,  "
				+ "C55, C56, C57, C58, C59, C60,  "
				+ "C61, C62, C63, C64, C65, C66,  "
				+ "C67, C68, C69, C70, C71, C72,  "
				+ "C73, C74, C75, C76, C77, C78,  "
				+ "C79, C80, C81, C82, C83, C84,  "
				+ "C85, C86, C87, C88, C89, C90,  "
				+ "C91, C92, C93, C94, C95, C96,  "
				+ "C97, C98, C99, C100, C101, C102,  "
				+ "C103, C104, C105, C106, C107, C108,  "
				+ "C109, C110, C111, C112, C113, C114,  "
				+ "C115, C116, C117, C118, C119, C120,  "
				+ "C121, C122, C123, C124, C125, C126,  "
				+ "C127, C128, C129, C130, C131, C132,  "
				+ "C133, C134, C135, C136, C137, C138,  "
				+ "C139, C140, C141, C142, C143, C144,  "
				+ "C145, C146, C147, C148, C149, C150,  "
				+ "C151, C152, C153, C154, C155, C156,  "
				+ "C157, C158, C159, C160, C161, C162,  "
				+ "C163, C164, C165, C166, C167, C168,  "
				+ "C169, C170, C171, C172, C173, C174,  "
				+ "C175, C176, C177, C178, C179, C180,  "
				+ "C181, C182, C183, C184, C185, C186,  "
				+ "C187, C188, C189, C190, C191, C192,  "
				+ "C193, C194, C195, C196, C197, C198,  "
				+ "C199, C200, C201, C202, C203, C204,  "
				+ "C205, C206, C207, C208, C209, C210,  "
				+ "C211, C212, C213, C214, C215, C216,  "
				+ "C217, C218, C219, C220, C221, C222,  "
				+ "C223, C224, C225, C226, C227, C228,  "
				+ "C229, C230, C231, C232, C233, C234,  "
				+ "C235, C236, C237, C238, C239, C240,  "
				+ "C241, C242, C243, C244, C245, C246,  "
				+ "C247, C248, C249, C250 " + "FROM L1; ");
		rs.next();
		for (int i = 1; i < 250; i++) {
			assertEquals(i+1, rs.getInt(i));
		}

		// PASS:0891 If 250 values are returned with values from 2 thru 251?
		// END TEST >>> 0891 <<< END TEST
	}

	/*
	 * 
	 * testDml_179_FipsSizingRowLenGreaterThan8000
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_179_FipsSizingRowLenGreaterThan8000()
			throws SQLException {
		// TEST:0892 FIPS sizing, rowlen >= 8000, statement var >= 4000!

		stmt.executeUpdate("CREATE TABLE T0892 ( "
				+ "INTKEY NUMERIC (3) not null PRIMARY KEY, "
				+ "NAAM VARCHAR (1000), " + "ADDRESS VARCHAR (1000), "
				+ "KEYWORDS VARCHAR (1000), " + "FUNCTION1 VARCHAR (1000), "
				+ "FUNCTION2 VARCHAR (1000), " + "DESCRIPT1 VARCHAR (1000), "
				+ "DESCRIPT2 VARCHAR (1000), " + "DESCRIPT3 VARCHAR (978)); ");
		// PASS:0892 If table created successfully?

		String val2 = "'John                                                               ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                            Smith',  ";
		String val3 = "'1313 Osprey Alley                                                  ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'Box 35B Sector 28 Quadrant 3                                        ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'Rural Route 29837-39234234324-XRZ                                   ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'Beverly Hills, CA                                                   ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                            90210',  ";
		String val4 = "'aardvark osprey          metrology                                 ' ||  "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'synthetic             SQL RDA PDP                                   ' || "
				+ "'                                                                    ' || "
				+ "'          antelope gnu yak bison quadruped cattle                   ' || "
				+ "'                                                                    ' || "
				+ "'           CORBA IDL       Amsterdam                                ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'production  crystal growth                                          ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                          gravity',   ";
		String val5 = "'filler filler filler blah blah blah                                ' ||  "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                    ' || "
				+ "'                                                                 ' || "
				+ "'this is going to be overwritten' ";

		stmt
				.executeUpdate("INSERT INTO T0892 (INTKEY, NAAM, ADDRESS, KEYWORDS, DESCRIPT3) "
						+ "VALUES (0, " + val2 + val3 + val4 + val5 + "); ");
		// PASS:0892 If 1 row inserted successfully?

		stmt
				.executeUpdate("UPDATE T0892 "
						+ "SET FUNCTION1 = "
						+ "'Harry works in the Redundancy Automation Division of the ' || "
						+ "'Materials ' || "
						+ "'Blasting Laboratory in the National Cattle Acceleration ' || "
						+ "'Project of ' || "
						+ "'lower Michigan.  His job is to document the trajectory of ' || "
						+ "'cattle and ' || "
						+ "'correlate the loft and acceleration versus the quality of ' || "
						+ "'materials ' || "
						+ "'used in the trebuchet.  He served ten years as the ' || "
						+ "'vice-president in ' || "
						+ "'charge of marketing in the now defunct milk trust of the ' || "
						+ "'Pennsylvania ' || "
						+ "'Coalition of All Things Bovine.  Prior to that he ' || "
						+ "'established himself ' || "
						+ "'as a world-class graffiti artist and source of all good ' || "
						+ "'bits related ' || "
						+ "'to channel dredging in poor weather.  He is author of over ' || "
						+ "'ten thousand ' || "
						+ "'paperback novels, including such titles as \"How Many ' || "
						+ "'Pumpkins will Fit ' || "
						+ "'on the Head of a Pin,\" \"A Whole Bunch of Useless Things ' || "
						+ "'that you Don''t ' || "
						+ "'Want to Know,\" and \"How to Lift Heavy Things Over your ' || "
						+ "'Head without ' || "
						+ "'Hurting Yourself or Dropping Them.\"  He attends ANSI and ' || "
						+ "'ISO standards ' || "
						+ "'meetings in his copious free time and funds the development ' || "
						+ "'of test ' || "
						+ "'suites with his pocket change.' "
						+ "WHERE INTKEY = 0; ");
		// PASS:0892 If update completed successfully?

		stmt.executeUpdate("UPDATE T0892 " + "SET FUNCTION2 = FUNCTION1, "
				+ "DESCRIPT1 = FUNCTION1, " + "DESCRIPT2 = FUNCTION1, "
				+ "DESCRIPT3 = SUBSTRING (FUNCTION1 FROM 1 FOR 978);");
		// PASS:0892 If update completed successfully?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT CHAR_LENGTH (NAAM) +  "
					+ "CHAR_LENGTH (ADDRESS) + " + "CHAR_LENGTH (KEYWORDS) + "
					+ "CHAR_LENGTH (FUNCTION1) + "
					+ "CHAR_LENGTH (FUNCTION2) + "
					+ "CHAR_LENGTH (DESCRIPT1) + "
					+ "CHAR_LENGTH (DESCRIPT2) + "
					+ "CHAR_LENGTH (DESCRIPT3) + 22 " + "FROM T0892 "
					+ "WHERE INTKEY = 0; ");
			rs.next();
			assertEquals(8000, rs.getInt(1));
			// PASS:0892 If CHAR_LENGTH = 8000?
		}

		// END TEST >>> 0892 <<< END TEST
		// ********************************************* }
	}

	/*
	 * 
	 * testDml_181_FipsSizingLengthOfColumnListGreatherThan750
	 * 
	 * Notes: This test was altered for Firebird - the primary key reference for
	 * long_named_people was removed, and the foreign key reference on ORDERS
	 * was removed. The original test wouldn't work under firebird, since the
	 * primary key in table LONG_NAMED_PEOPLE produces a "key size too big"
	 * error. This is because the primary key is a compound key of two long
	 * varchars. Without the primary key, we can't create the references
	 * constraint either. This change is ok, because we're really testing the
	 * ability to insert long character strings into columns here, and the
	 * primary key / foreign key isn't a requirement for this test.
	 *  
	 */
	public void testDml_181_FipsSizingLengthOfColumnListGreatherThan750()
			throws SQLException {

		// TEST:0894 FIPS sizing, length of column lists >= 750!

		stmt.executeUpdate("CREATE TABLE LONG_NAMED_PEOPLE ( "
				+ "FIRSTNAME VARCHAR (373), " + "LASTNAME VARCHAR (373), "
				+ "AGE INT );  ");
		// PASS:0894 If table created successfully?

		stmt.executeUpdate("CREATE TABLE ORDERS ( "
				+ "FIRSTNAME VARCHAR (373), " + "LASTNAME VARCHAR (373), "
				+ "TITLE VARCHAR (80), " + "COST NUMERIC(5,2) );  ");
		// PASS:0894 If table created successfully?

		// not supported in FB - we'll form the query without a view.
		//			   stmt.executeUpdate("CREATE VIEW PEOPLE_ORDERS AS "+
		//			     "SELECT * FROM LONG_NAMED_PEOPLE JOIN ORDERS "+
		//			     "USING (FIRSTNAME, LASTNAME)");
		//			// PASS:0894 If view created successfully?

		stmt
				.executeUpdate("INSERT INTO LONG_NAMED_PEOPLE VALUES ( "
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaa', "
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbb', " + "20); ");
		// PASS:0894 If 1 row inserted successfully?

		stmt
				.executeUpdate(" INSERT INTO LONG_NAMED_PEOPLE VALUES ( "
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccc',  "
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddd', " + "25); ");
		// PASS:0894 If 1 row inserted successfully?

		stmt
				.executeUpdate("INSERT INTO ORDERS VALUES ( "
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaa', "
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbb', "
						+ "'Gidget Goes Skiing', " + "29.95); ");
		// PASS:0894 If 1 row inserted successfully?

		stmt
				.executeUpdate("INSERT INTO ORDERS VALUES ( "
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'  ||"
						+ "'aaaaaaaaaaaaaaaaaaaaaaaaa', "
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'  ||"
						+ "'bbbbbbbbbbbbbbbbbbbbbbbbb', "
						+ "'Barney Goes Hawaiian', " + "19.95); ");
		// PASS:0894 If 1 row inserted successfully?

		stmt
				.executeUpdate("INSERT INTO ORDERS VALUES ( "
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccccccccccccccccccccccccccccc'  ||"
						+ "'ccccccccccccccccccccccccccccccc', "
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd'  ||"
						+ "'ddddddddddddddddddddddddddddddd', "
						+ "'Invasion of the Smurfs', " + "9.95); ");
		// PASS:0894 If 1 row inserted successfully?

//		rs = stmt.executeQuery("SELECT a.FIRSTNAME, a.LASTNAME, AVG(COST) "
//		+ "FROM  long_named_people a  join orders "
//		+ "on long_named_people.firstname=orders.firstname "
//		+ "and long_named_people.lastname = orders.lastname "
//		+ "GROUP BY a.LASTNAME, a.FIRSTNAME  "
//		+ "ORDER BY a.LASTNAME, a.FIRSTNAME; ");
		rs = stmt.executeQuery("SELECT a.FIRSTNAME, a.LASTNAME, AVG(COST) "
		+ "FROM  long_named_people a  join orders "
		+ "on a.firstname=orders.firstname "
		+ "and a.lastname = orders.lastname "
		+ "GROUP BY a.LASTNAME, a.FIRSTNAME  "
		+ "ORDER BY a.LASTNAME, a.FIRSTNAME; ");
		//			   SELECT FIRSTNAME, LASTNAME, AVG(COST)
		//			     FROM PEOPLE_ORDERS
		//			     GROUP BY LASTNAME, FIRSTNAME
		//			     ORDER BY LASTNAME, FIRSTNAME;

		rs.next();
		assertEquals(373, rs.getString(1).length());
		assertEquals(373, rs.getString(2).length());
		assertTrue((24.94 <= rs.getDouble(3)) && (24.96 > rs.getDouble(3)));
		rs.next();
		assertEquals(373, rs.getString(1).length());
		assertEquals(373, rs.getString(2).length());
		assertTrue((9.94 <= rs.getDouble(3)) && (9.96 > rs.getDouble(3)));
		assertFalse(rs.next());

		// PASS:0894 If 2 rows are returned in the following order?
		// NOTE: Columns c1 and c2 are 373 characters each!
		//                   c1 c2 c3
		//                   == == ==
		// PASS:0894 If aaaaaaaa.... bbbbbbbb.... 24.95 (+ or - 0.01)?
		// PASS:0894 If cccccccc.... dddddddd.... 9.95 (+ or - 0.01)?

		// END TEST >>> 0894 <<< END TEST
	} /*
	   * 
	   * testDml_182_FipsSizingColumnsInListGreaterThan15
	   * 
	   * Notes:
	   *  
	   */
	public void testDml_182_FipsSizingColumnsInListGreaterThan15()
			throws SQLException {

		stmt.executeUpdate("CREATE TABLE ID_CODES ( " + "CODE1 INT NOT NULL, "
				+ "CODE2 INT NOT NULL, " + "CODE3 INT NOT NULL, "
				+ "CODE4 INT NOT NULL, " + "CODE5 INT NOT NULL, "
				+ "CODE6 INT NOT NULL, " + "CODE7 INT NOT NULL, "
				+ "CODE8 INT NOT NULL, " + "CODE9 INT NOT NULL, "
				+ "CODE10 INT NOT NULL, " + "CODE11 INT NOT NULL, "
				+ "CODE12 INT NOT NULL, " + "CODE13 INT NOT NULL, "
				+ "CODE14 INT NOT NULL, " + "CODE15 INT NOT NULL, "
				+ "PRIMARY KEY (CODE1, CODE2, CODE3, CODE4, CODE5, "
				+ "CODE6, CODE7, CODE8, CODE9, CODE10, "
				+ "CODE11, CODE12, CODE13, CODE14, CODE15)); ");
		// PASS:0895 If table created successfully?

		stmt.executeUpdate("CREATE TABLE ORDERS ( " + "CODE1 INT, "
				+ "CODE2 INT, " + "CODE3 INT, " + "CODE4 INT, " + "CODE5 INT, "
				+ "CODE6 INT, " + "CODE7 INT, " + "CODE8 INT, " + "CODE9 INT, "
				+ "CODE10 INT, " + "CODE11 INT, " + "CODE12 INT, "
				+ "CODE13 INT, " + "CODE14 INT, " + "CODE15 INT, "
				+ "TITLE VARCHAR (80), " + "COST NUMERIC(5,2), "
				+ "FOREIGN KEY (CODE1, CODE2, CODE3, CODE4, CODE5, "
				+ "CODE6, CODE7, CODE8, CODE9, CODE10, "
				+ "CODE11, CODE12, CODE13, CODE14, CODE15) "
				+ "REFERENCES ID_CODES); ");
		// PASS:0895 If table created successfully?

		// can't create a view with the the join for firebirdsql... that's it!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		stmt.executeUpdate("CREATE VIEW ID_ORDERS AS "
				+ "SELECT * FROM ID_CODES JOIN ORDERS "
				+ "USING (CODE1, CODE2, CODE3, CODE4, CODE5, "
				+ "CODE6, CODE7, CODE8, CODE9, CODE10, "
				+ "CODE11, CODE12, CODE13, CODE14, CODE15);");
		// PASS:0895 If view created successfully

		stmt.executeUpdate("INSERT INTO ID_CODES VALUES ( "
				+ "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);");
		stmt.executeUpdate("INSERT INTO ID_CODES VALUES ( "
				+ "1, 2, 3, 4, 5, 6, 7, 9, 8, 10, 11, 12, 13, 14, 15);");
		stmt.executeUpdate("INSERT INTO ORDERS VALUES ( "
				+ "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, "
				+ "'Gidget Goes Skiing'," + "29.95);");
		stmt.executeUpdate("INSERT INTO ORDERS VALUES ( "
				+ "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, "
				+ "'Barney Goes Hawaiian', " + "19.95);");
		stmt.executeUpdate("INSERT INTO ORDERS VALUES ( "
				+ "1, 2, 3, 4, 5, 6, 7, 9, 8, 10, 11, 12, 13, 14, 15, "
				+ "'Invasion of the Smurfs', " + "9.95); ");
		// PASS:0895 If 5 rows inserted successfully in previous 5 inserts?

		rs = stmt.executeQuery("SELECT CODE1, CODE2, CODE3, CODE4, CODE5, "
				+ "CODE6, CODE7, CODE8, CODE9, CODE10, "
				+ "CODE11, CODE12, CODE13, CODE14, CODE15, " + "AVG(COST) "
				+ "FROM ID_ORDERS "
				+ "GROUP BY CODE1, CODE2, CODE3, CODE4, CODE5, "
				+ "CODE6, CODE7, CODE8, CODE9, CODE10, "
				+ "CODE11, CODE12, CODE13, CODE14, CODE15 "
				+ "ORDER BY CODE1, CODE2, CODE3, CODE4, CODE5, "
				+ "CODE6, CODE7, CODE8, CODE9, CODE10, "
				+ "CODE11, CODE12, CODE13, CODE14, CODE15; ");
		// PASS:0895 If 2 rows are returned?
		//                 avg(cost)
		//                 =========
		// PASS:0895 If 24.95 (+ or - 0.01) ?
		// PASS:0895 If 9.95 (+ or - 0.01) ?

		// END TEST >>> 0895 <<< END TEST
	}

	/*
	 * 
	 * testDml_183_FipsSizing50WhenClausesInCase
	 * 
	 * Notes:
	 *  
	 */
	public void testDml_183_FipsSizing50WhenClausesInCase() throws SQLException {
		// TEST:0896 FIPS sizing, 50 WHEN clauses in a CASE expression!
		BaseTab.setupBaseTab(stmt);
		rs = stmt.executeQuery(" SELECT EMPNUM, " + "CASE GRADE "
				+ "WHEN 0 THEN 1000 " + "WHEN 1 THEN 997 " + "WHEN 2 THEN 994 "
				+ "WHEN 3 THEN 991 " + "WHEN 4 THEN 988 " + "WHEN 5 THEN 985 "
				+ "WHEN 6 THEN 982 " + "WHEN 7 THEN 979 " + "WHEN 8 THEN 976 "
				+ "WHEN 9 THEN 973 " + "WHEN 10 THEN 970 "
				+ "WHEN 11 THEN 967 " + "WHEN 12 THEN 964 "
				+ "WHEN 13 THEN 961 " + "WHEN 14 THEN 958 "
				+ "WHEN 15 THEN 955 " + "WHEN 16 THEN 952 "
				+ "WHEN 17 THEN 949 " + "WHEN 18 THEN 946 "
				+ "WHEN 19 THEN 943 " + "WHEN 20 THEN 940 "
				+ "WHEN 21 THEN 937 " + "WHEN 22 THEN 934 "
				+ "WHEN 23 THEN 931 " + "WHEN 24 THEN 928 "
				+ "WHEN 25 THEN 925 " + "WHEN 26 THEN 922 "
				+ "WHEN 27 THEN 919 " + "WHEN 28 THEN 916 "
				+ "WHEN 29 THEN 913 " + "WHEN 30 THEN 910 "
				+ "WHEN 31 THEN 907 " + "WHEN 32 THEN 904 "
				+ "WHEN 33 THEN 901 " + "WHEN 34 THEN 898 "
				+ "WHEN 35 THEN 895 " + "WHEN 36 THEN 892 "
				+ "WHEN 37 THEN 889 " + "WHEN 38 THEN 886 "
				+ "WHEN 39 THEN 883 " + "WHEN 40 THEN 880 "
				+ "WHEN 41 THEN 877 " + "WHEN 42 THEN 874 "
				+ "WHEN 43 THEN 871 " + "WHEN 44 THEN 868 "
				+ "WHEN 45 THEN 865 " + "WHEN 46 THEN 862 "
				+ "WHEN 47 THEN 859 " + "WHEN 48 THEN 856 "
				+ "WHEN 49 THEN 853 " + "END " + "FROM STAFF "
				+ "WHERE EMPNAME = 'Betty';");
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals(970, rs.getInt(2));
		// PASS:0896 If empnum = 'E2' and casgrd = 970?

		rs = stmt.executeQuery("SELECT EMPNUM, " + "CASE "
				+ "WHEN GRADE = 0 THEN 1000 " + "WHEN GRADE = 1 THEN 997 "
				+ "WHEN GRADE = 2 THEN 994 " + "WHEN GRADE = 3 THEN 991 "
				+ "WHEN GRADE = 4 THEN 988 " + "WHEN GRADE = 5 THEN 985 "
				+ "WHEN GRADE = 6 THEN 982 " + "WHEN GRADE = 7 THEN 979 "
				+ "WHEN GRADE = 8 THEN 976 " + "WHEN GRADE = 9 THEN 973 "
				+ "WHEN GRADE = 11 THEN 967 " + "WHEN GRADE = 12 THEN 964 "
				+ "WHEN GRADE = 13 THEN 961 " + "WHEN GRADE = 14 THEN 958 "
				+ "WHEN GRADE = 15 THEN 955 " + "WHEN GRADE = 16 THEN 952 "
				+ "WHEN GRADE = 17 THEN 949 " + "WHEN GRADE = 18 THEN 946 "
				+ "WHEN GRADE = 19 THEN 943 " + "WHEN GRADE = 20 THEN 940 "
				+ "WHEN GRADE = 21 THEN 937 " + "WHEN GRADE = 22 THEN 934 "
				+ "WHEN GRADE = 23 THEN 931 " + "WHEN GRADE = 24 THEN 928 "
				+ "WHEN GRADE = 25 THEN 925 " + "WHEN GRADE = 26 THEN 922 "
				+ "WHEN GRADE = 27 THEN 919 " + "WHEN GRADE = 28 THEN 916 "
				+ "WHEN GRADE = 29 THEN 913 " + "WHEN GRADE = 30 THEN 910 "
				+ "WHEN GRADE = 31 THEN 907 " + "WHEN GRADE = 32 THEN 904 "
				+ "WHEN GRADE = 33 THEN 901 " + "WHEN GRADE = 34 THEN 898 "
				+ "WHEN GRADE = 35 THEN 895 " + "WHEN GRADE = 36 THEN 892 "
				+ "WHEN GRADE = 37 THEN 889 " + "WHEN GRADE = 38 THEN 886 "
				+ "WHEN GRADE = 39 THEN 883 " + "WHEN GRADE = 40 THEN 880 "
				+ "WHEN GRADE = 41 THEN 877 " + "WHEN GRADE = 42 THEN 874 "
				+ "WHEN GRADE = 43 THEN 871 " + "WHEN GRADE = 44 THEN 868 "
				+ "WHEN GRADE = 45 THEN 865 " + "WHEN GRADE = 46 THEN 862 "
				+ "WHEN GRADE = 47 THEN 859 " + "WHEN GRADE = 48 THEN 856 "
				+ "WHEN GRADE = 49 THEN 853 " + "WHEN GRADE = 10 THEN 970 "
				+ "END " + "FROM STAFF " + "WHERE EMPNAME = 'Betty';");
		// PASS:0896 If empnum = 'E2' and casgrd = 970?

		// END TEST >>> 0896 <<< END TEST

	}

	// testDml_184 skipped - information schema
	// TEST:0897 Constraint usage redux!

	// testDml_185 skipped - information schema
	// TEST:0898 COLUMN_DEFAULT interpretation!

	// testDml_186 skipped - interval times
	// TEST:0899 FIPS sizingt, INTERVAL decimal leading field precision!

}