/* $Id$ */
/*
 * Author: bioliv Created on: Aug 9, 2004
 *  
 */
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestYts extends NistTestBase {
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

	public TestYts(String arg0) {
		super(arg0);
	}

	/*
	 * Name: testYts_750
	 * 
	 * Notes: Firebird doesn't support Information Schema, so we have to use
	 * RDB$ tables.
	 *  
	 */
	public void testYts_750() throws SQLException {

		// TEST:7500 CREATE DOMAIN - SQL Procedure statement, no options!

		stmt.executeUpdate("CREATE DOMAIN INTDOMAIN INTEGER;");
		// PASS:7500 If domain created successfully?

		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt
					.executeQuery("select rdb$field_name, "
							+ "RDB$DEFAULT_SOURCE,  "
							+ "RDB$FIELD_LENGTH,  "
							+ "RDB$FIELD_SCALE,  "
							+ "RDB$FIELD_TYPE,  "
							+ "RDB$Character_Length,  "
							+ "rdb$character_set_id from RDB$fields where rdb$field_name = 'INTDOMAIN' ");
			rs.next();
			assertEquals("INTDOMAIN", rs.getString(1).trim());
			assertEquals(null, rs.getString(2));
			assertEquals(4, rs.getInt(3));
			assertEquals(0, rs.getInt(4));
			assertEquals(8, rs.getInt(5));
			assertEquals(0, rs.getInt(6));
			assertEquals(0, rs.getInt(7));
		} else {
			// we could add a check for information_schema here later.
			//			   SELECT DOMAIN_CATALOG,
			//		         DATA_TYPE, CHARACTER_MAXIMUM_LENGTH,
			//		         CHARACTER_OCTET_LENGTH, COLLATION_CATALOG,
			//		         COLLATION_SCHEMA, COLLATION_NAME,
			//		         CHARACTER_SET_CATALOG, CHARACTER_SET_SCHEMA,
			//		         CHARACTER_SET_NAME, NUMERIC_PRECISION,
			//		         NUMERIC_PRECISION_RADIX, NUMERIC_SCALE,
			//		         DATETIME_PRECISION, DOMAIN_DEFAULT,
			//		         INTERVAL_TYPE, INTERVAL_PRECISION
			//		         FROM INFORMATION_SCHEMA.DOMAINS
			//		         WHERE DOMAIN_NAME = 'INTDOMAIN'
			//		         AND DOMAIN_SCHEMA = 'CTS1';
			//		-- PASS:7500 If DOMAIN_CATALOG = not NULL?
			//		-- PASS:7500 If DATA_TYPE = INTEGER?
			//		-- PASS:7500 If CHARACTER_MAXIMUM_LENGTH = NULL?
			//		-- PASS:7500 If CHARACTER_OCTET_LENGTH = NULL?
			//		-- PASS:7500 If COLLATION_CATALOG = NULL?
			//		-- PASS:7500 If COLLATION_SCHEMA = NULL?
			//		-- PASS:7500 If COLLATION_NAME = NULL?
			//		-- PASS:7500 If CHARACTER_SET_CATALOG = NULL?
			//		-- PASS:7500 If CHARACTER_SET_SCHEMA = NULL?
			//		-- PASS:7500 If CHARACTER_SET_NAME = NULL?
			//		-- PASS:7500 If NUMERIC_PRECISION = not NULL?
			//		-- PASS:7500 If NUMERIC_PRECISION_RADIX = 2 or 10?
			//		-- PASS:7500 If NUMERIC_SCALE = 0?
			//		-- PASS:7500 If DATETIME_PRECISION = NULL?
			//		-- PASS:7500 If DOMAIN_DEFAULT = NULL?
			//		-- PASS:7500 If INTERVAL_TYPE = NULL?
			//		-- PASS:7500 If INTERVAL_PRECISION = NULL?
		}
		stmt.executeUpdate("DROP DOMAIN intdomain ");
		// PASS:7501 If domain dropped successfully?

		// END TEST >>> 7501 <<< END TEST
	}

	/*
	 * Name: testYts_751
	 * 
	 * Notes: TOD: Firebird doesn't support Information Schema, so we have to
	 * use RDB$ tables.
	 *  
	 */
	public void testYts_751() throws SQLException {

		// TEST:7501 CREATE DOMAIN as SQL proc statement with default!
		stmt
				.executeUpdate("CREATE DOMAIN CHARDOMAIN AS CHAR(10) DEFAULT 'MANCHESTER';");
		// PASS:7501 If domain created successfully?

		// for firebird/vulcan we can query the system tables.
		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt
					.executeQuery("select count(*) from RDB$fields where rdb$field_name = 'CHARDOMAIN' ");
			rs.next();
			assertEquals(1, rs.getInt(1));

			rs = stmt.executeQuery("select rdb$field_name, "
					+ "RDB$DEFAULT_SOURCE,  " + "RDB$FIELD_LENGTH,  "
					+ "RDB$FIELD_SCALE,  " + "RDB$FIELD_TYPE,  "
					+ "RDB$Character_Length  "
					+ "from RDB$fields where rdb$field_name = 'CHARDOMAIN' ");
			rs.next();
			assertEquals("CHARDOMAIN", rs.getString(1).trim());
			assertEquals("DEFAULT 'MANCHESTER'", rs.getString(2).trim());
			assertEquals(10, rs.getInt(3));
			assertEquals(0, rs.getInt(4));
			assertEquals(14, rs.getInt(5));
			assertEquals(10, rs.getInt(6));

		} else {
			//			rs = stmt.executeQuery("SELECT COUNT(*) "
			//					+ "FROM INFORMATION_SCHEMA.DOMAINS "
			//					+ " WHERE DOMAIN_NAME = 'CHARDOMAIN' "
			//					+ " AND DOMAIN_SCHEMA = 'CTS1';");
			//			rs.next();
			//			assertEquals(1, rs.getInt(1));
			// PASS:7501 If COUNT = 1?

			//			stmt.executeQuery("SELECT DOMAIN_CATALOG, "
			//					+ "DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, "
			//					+ "CHARACTER_OCTET_LENGTH, COLLATION_CATALOG, "
			//					+ "COLLATION_SCHEMA, COLLATION_NAME, "
			//					+ "CHARACTER_SET_CATALOG, CHARACTER_SET_SCHEMA, "
			//					+ "CHARACTER_SET_NAME, NUMERIC_PRECISION, "
			//					+ "NUMERIC_PRECISION_RADIX, NUMERIC_SCALE, "
			//					+ "DATETIME_PRECISION, DOMAIN_DEFAULT "
			//					+ "FROM INFORMATION_SCHEMA.DOMAINS "
			//					+ "WHERE DOMAIN_NAME = 'CHARDOMAIN' "
			//					+ "AND DOMAIN_SCHEMA = 'CTS1';");

			// PASS:7501 If DOMAIN_CATALOG = not NULL?
			// PASS:7501 If DATA_TYPE = CHARACTER?
			// PASS:7501 If CHARACTER_MAXIMUM_LENGTH = 10?
			// PASS:7501 If CHARACTER_OCTET_LENGTH = not NULL?
			// PASS:7501 If COLLATION_CATALOG = not NULL?
			// PASS:7501 If COLLATION_SCHEMA = not NULL?
			// PASS:7501 If COLLATION_NAME = not NULL?
			// PASS:7501 If CHARACTER_SET_CATALOG = not NULL?
			// PASS:7501 If CHARACTER_SET_SCHEMA = not NULL?
			// PASS:7501 If CHARACTER_SET_NAME = not NULL?
			// PASS:7501 If NUMERIC_PRECISION = NULL?
			// PASS:7501 If NUMERIC_PRECISION_RADIX = NULL?
			// PASS:7501 If NUMERIC_SCALE = NULL?
			// PASS:7501 If DATETIME_PRECISION = NULL?
			// PASS:7501 If DOMAIN_DEFAULT = MANCHESTER?
		}

		// END TEST >>> 7501 <<< END TEST
	}
	/*
	 * Name: testYts_752
	 * 
	 * Notes: Firebird implements multiple CHECK constraints on a column
	 * differently from the SQL specification. The NIST tests had "CREATE DOMAIN
	 * sintdom AS SMALLINT CHECK (VALUE > 5) CHECK (VALUE < 24000)". In
	 * Firebird, we implement this as "CREATE DOMAIN sintdom AS SMALLINT CHECK
	 * (VALUE > 5) AND CHECK (VALUE < 24000)".
	 * 
	 *  
	 */

	public void testYts_752_CreateDomainWithDefault() throws SQLException {
		// TEST:7502 CREATE DOMAIN - SQL proc statement with default!

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE DOMAIN sintdom AS SMALLINT "
					+ "CHECK (VALUE > 5 and VALUE < 24000);");
		// PASS:7502 If domain created successfully?
		else
			stmt.executeUpdate("CREATE DOMAIN sintdom AS SMALLINT "
					+ "CHECK (VALUE > 5) " + "CHECK (VALUE < 24000);");

		stmt.executeUpdate("CREATE TABLE shorttab "
				+ "(keycol integer, domcol sintdom);");
		// PASS:7502 If table created successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO shorttab VALUES (1,6);"));
		// PASS:7502 If 1 row inserted successfully?

		try {
			stmt.executeUpdate("INSERT INTO shorttab VALUES (2,3);");
			fail();
			// PASS:7502 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				String fbError = "GDS Exception. 335544347. validation error for column DOMCOL, value \"3\"";
				assertEquals(fbError, sqle.getMessage().substring(0,
						fbError.length()));
			}

		}

		try {
			stmt.executeUpdate("INSERT INTO shorttab VALUES (3, 123456789);");
			fail("Should fail with numeric value out of range or integrity constraint");
		} catch (SQLException sqle) {
			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				String fbError = "GDS Exception. 335544321. arithmetic exception, numeric overflow, or string truncation";
				assertEquals(fbError, sqle.getMessage().substring(0,
						fbError.length()));
			}
		}
		// PASS:7502 If ERROR -integrity const. violation or ?
		// PASS:7502 numeric value out of range ?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO shorttab VALUES (4,100);"));
		// PASS:7502 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM shorttab;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7502 If COUNT = 2?

		rs = stmt.executeQuery("SELECT domcol FROM shorttab "
				+ "WHERE keycol = 1;");
		rs.next();
		assertEquals(6, rs.getShort(1));
		// PASS:7502 If domcol = 6?

		// extra test for getInt() instead of getShort()
		rs = stmt.executeQuery("SELECT domcol FROM shorttab "
				+ "WHERE keycol = 1;");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:7502 If domcol = 6?

		rs = stmt.executeQuery("SELECT domcol FROM shorttab "
				+ "WHERE keycol = 4;");
		rs.next();
		assertEquals(100, rs.getShort(1));
		// PASS:7502 If domcol = 100?

		stmt.executeUpdate("DROP TABLE shorttab;");
		// PASS:7502 If table dropped successfully?

		stmt.executeUpdate("DROP DOMAIN sintdom;");
		// PASS:7502 If domain dropped successfully?

		// END TEST >>> 7502 <<< END TEST
	}

	/*
	 * Name: testYts_753
	 * 
	 * Note: DROP DOMAIN RESTRICT is the default Firebird SQL behaviour on DROP
	 * DOMAIN. RESTRICT ensures that the DROP DOMAIN won't happen if objects
	 * depend on the domain.
	 *  
	 */

	public void testYts_753_DropDomainRestrict() throws SQLException {

		// TEST:7503 DROP DOMAIN RESTRICT!
		stmt.executeUpdate("CREATE DOMAIN intdomain AS INTEGER;");
		// PASS:7503 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE int_in_use "
				+ "(numerical  intdomain, " + "literary   char(10));");
		// PASS:7503 If table created successfully?

		String query = null;
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "DROP DOMAIN intdomain";
		else
			query = "DROP DOMAIN intdomain RESTRICT";
		try {
			stmt.executeUpdate("DROP DOMAIN intdomain ;");
			fail("Firebird won't let us drop a DOMAIN that is still in use");
			// PASS:7503 If ERROR - syntax error or access rule violation?
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("DROP TABLE int_in_use;");
		// PASS:7503 If table dropped successfully?

		stmt.executeUpdate("DROP DOMAIN intdomain ;");
		// PASS:7503 If domain dropped successfully?

		try {
			stmt.executeUpdate("CREATE TABLE int_in_use "
					+ "(numerical intdomain, " + "literary char(10)); ");
			fail("The domain is no longer present.");
		} catch (SQLException sqle) {
		}
		// PASS:7503 If ERROR - syntax error or access rule violation?
		// END TEST >>> 7503 <<< END TEST
	}

	/*
	 * Name: testYts_754_DropDomainCascade
	 * 
	 * Notes: Firebird doesn't support this functionality.
	 *  
	 */

	public void testYts_754_DropDomainCascade() throws SQLException {

		// TEST:7504 DROP DOMAIN CASCADE - domain definition in use!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		stmt.executeUpdate("CREATE DOMAIN char_dom CHARACTER;");
		// PASS:7504 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE char_in_use "
				+ "(  litt1   char_dom, " + "numm    integer);");
		// PASS:7504 If table created successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO char_in_use VALUES ('a',00);"));
		// PASS:7504 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO char_in_use VALUES ('z',99);"));
		// PASS:7504 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO char_in_use VALUES ('A',999999);"));
		// PASS:7504 If 1 row inserted successfully?

		stmt.executeUpdate("DROP DOMAIN char_dom CASCADE;");
		// PASS:7504 If domain dropped successfully?

		rs = stmt.executeQuery("SELECT litt1 FROM char_in_use ORDER BY numm;");
		rs.next();
		assertEquals("a", rs.getString(1));
		rs.next();
		assertEquals("z", rs.getString(1));
		rs.next();
		assertEquals("A", rs.getString(1));

		// PASS:7504 If 3 rows are selected in the following order?
		//               litt1
		//               =====
		// PASS:7504 If a?
		// PASS:7504 If z?
		// PASS:7504 If A?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO char_in_use VALUES ('p',654);"));
		// PASS:7504 If 1 row inserted successfully?

		stmt.executeUpdate("DROP TABLE char_in_use;");
		// PASS:7504 If table dropped successfully?

		// END TEST >>> 7504 <<< END TEST
	}

	/*
	 * Name: testYts_755
	 * 
	 * Note: Firebird does not support DROP DOMAIN CASCADE.
	 *  
	 */

	public void testYts_755_DropDomainCascadeWithDefaultAndConstraint()
			throws SQLException {

		// TEST:7505 DROP DOMAIN CASCADE - domain with default & constraint!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;

		stmt.executeUpdate("CREATE DOMAIN int_dom INTEGER " + "DEFAULT 15 "
				+ "CHECK(VALUE < 100); ");
		// PASS:7505 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE dom_test " + "(num int_dom, "
				+ "lit char(3));");
		// PASS:7505 If table created successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO dom_test VALUES (00,'a');"));
		// PASS:7505 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO dom_test VALUES (99,'b');"));
		// PASS:7505 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO dom_test VALUES (50,'c');"));
		// PASS:7505 If 1 row inserted successfully?

		stmt.executeUpdate("DROP DOMAIN int_dom CASCADE;");
		// PASS:7505 If domain dropped successfully?

		try {
			stmt.executeUpdate("INSERT INTO dom_test VALUES (101, 'g');");
			fail("INSERT should cause integrity constraint violation");
			// PASS:7505 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) "
				+ "FROM dom_test WHERE num = 101;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7505 If COUNT = 0?

		assertEquals(1, stmt.executeUpdate("INSERT INTO dom_test (lit) "
				+ "VALUES ('Z');"));
		// PASS:7505 If 1 row inserted?

		rs = stmt.executeQuery("SELECT num FROM dom_test WHERE lit = 'Z';");
		rs.next();
		assertEquals(15, rs.getInt(1));
		// PASS:7505 If num = 15?

		stmt.executeUpdate("DROP TABLE dom_test;");
		// PASS:7505 If table dropped successfully?

		// END TEST >>> 7505 <<< END TEST
	}

	/*
	 * Name: testYts_756
	 *  
	 */

	public void testYts_756() throws SQLException {

		// TEST:7506 Domain Constraint Containing VALUE!

		stmt.executeUpdate("CREATE DOMAIN d AS INTEGER "
				+ "CHECK (VALUE IN (3,5,7,9,11));");
		// PASS:7506 If domain created successfully?

		stmt.executeUpdate("CREATE DOMAIN e AS CHAR "
				+ "CHECK (VALUE LIKE 'a')");
		// PASS:7506 If domain created successfully?

		stmt.executeUpdate("CREATE DOMAIN f AS INTEGER "
				+ "CHECK (VALUE * VALUE > 1 + VALUE);");
		// PASS:7506 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE def_chk " + "( d_chk    d, "
				+ "e_chk    e, " + "f_chk    f);");
		// PASS:7506 If table created successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO def_chk VALUES (3,'a',3);"));
		// PASS:7506 If 1 row inserted successfully?

		try {
			stmt.executeUpdate("INSERT INTO def_chk VALUES (2,'a',3);");
			fail("INSERT should violate integrity constraint");
			// PASS:7506 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO def_chk VALUES (3,'z',3);");
			fail("INSERT should violate integrity constraint");
			// PASS:7506 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO def_chk VALUES (3,'a',1);");
			fail("INSERT should violate integrity constraint");
			// PASS:7506 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("DROP TABLE DEF_CHK ;");
		// PASS:7506 If table dropped successfully?

		stmt.executeUpdate("DROP DOMAIN d ;");
		// PASS:7506 If domain dropped successfully?

		stmt.executeUpdate("DROP DOMAIN e ;");
		// PASS:7506 If domain dropped successfully?

		stmt.executeUpdate("DROP DOMAIN f ;");
		// PASS:7506 If domain dropped successfully?

		// END TEST >>> 7506 <<< END TEST

	}

	/*
	 * Name: testYts_757
	 *  
	 */

	public void testYts_757() throws SQLException {

		// TEST:7507 INSERT value in column defined on domain!

		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");

		// subtle difference in SQL on CREATE DOMAIN statement
		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			stmt.executeUpdate("CREATE DOMAIN atom CHARACTER  "
					+ "CHECK ('a' <= VALUE  and 'm' >= VALUE)");
			stmt.executeUpdate("CREATE DOMAIN smint INTEGER "
					+ "CHECK (1<= VALUE and 100 >= VALUE);");
		} else {
			stmt.executeUpdate("CREATE DOMAIN atom CHARACTER "
					+ "CHECK ('a' <= VALUE) CHECK ('m' >= VALUE);");
			stmt.executeUpdate("CREATE DOMAIN smint INTEGER "
					+ "CHECK (1<= VALUE) CHECK (100 >= VALUE);");
		}

		stmt.executeUpdate("CREATE TABLE dom_chk " + "(col1 atom, "
				+ "col2 smint);");

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO dom_chk VALUES ('c',38);"));
		// PASS:7507 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT col1, col2 FROM dom_chk;");
		rs.next();
		assertEquals("c", rs.getString(1).trim());
		assertEquals(38, rs.getInt(2));
		// PASS:7507 If col1 = "c" and col2 = 38?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO dom_chk VALUES ('a',1);"));
		// PASS:7507 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO dom_chk VALUES ('m', 100);"));

		// PASS:7507 If 1 row inserted successfully?

		try {
			stmt.executeUpdate("INSERT INTO dom_chk VALUES ('z', 101);");
			fail("INSERT should violate integrity constraint");
			// PASS:7507 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {

		}

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM dom_chk  WHERE col1 = 'z';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7507 If COUNT = 0?

		try {
			stmt
					.executeUpdate("UPDATE dom_chk SET col1 = 'q' WHERE col2 = 38;");
			fail("UPDATE should violate integrity constraint");
		} catch (SQLException sqle) {
		}
		// PASS:7507 If ERROR - integrity constraint violation?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM dom_chk WHERE col1 = 'q';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7507 If COUNT = 0?

		stmt.executeUpdate("DROP TABLE dom_chk");
		stmt.executeUpdate("DROP domain atom");
		stmt.executeUpdate("DROP domain smint");
		// END TEST >>> 7507 <<< END TEST

	}

	/*
	 * Name: testYts_758
	 * 
	 * Notes:
	 *  
	 */

	public void testYts_758() throws SQLException {
		// TEST:7508 Put value in column defined on domain breading constraint!

		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");

		// subtle difference in SQL on CREATE DOMAIN statement
		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			stmt.executeUpdate("CREATE DOMAIN atom CHARACTER  "
					+ "CHECK ('a' <= VALUE  and 'm' >= VALUE)");
			stmt.executeUpdate("CREATE DOMAIN smint INTEGER "
					+ "CHECK (1<= VALUE and 100 >= VALUE);");
		} else {
			stmt.executeUpdate("CREATE DOMAIN atom CHARACTER "
					+ "CHECK ('a' <= VALUE) CHECK ('m' >= VALUE);");
			stmt.executeUpdate("CREATE DOMAIN smint INTEGER "
					+ "CHECK (1<= VALUE) CHECK (100 >= VALUE);");
		}

		stmt.executeUpdate("CREATE TABLE dom_chk " + "(col1 atom, "
				+ "col2 atom);");
		try {
			stmt.executeUpdate("INSERT INTO dom_chk VALUES ('<', 100);");
			fail("INSERT should violate integrity constraint");
			// PASS:7508 If ERROR - integrity constraint violation?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO dom_chk VALUES ('a', 101);");
			fail("INSERT should violate integrity constraint");
		} catch (SQLException sqle) {
		}
		// PASS:7508 If ERROR - integrity constraint violation?

		stmt.executeUpdate("DROP TABLE dom_chk");
		stmt.executeUpdate("DROP domain atom");
		stmt.executeUpdate("DROP domain smint");
		// END TEST >>> 7508 <<< END TEST

	} /*
	   * Name: testYts_759
	   * 
	   * Notes: For firebird, this test is skipped, since Firebird SQL doesn't
	   * support the ability to grant privileges on DOMAIN objects.
	   *  
	   */

	public void testYts_759() throws SQLException {

		//TEST:7509 GRANT USAGE on a domain!

		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL"))
			return;

		stmt.executeUpdate("CREATE DOMAIN emp_nos INTEGER;");
		// PASS:7509 If domain created successfully?

		stmt.executeUpdate("GRANT USAGE ON emp_nos TO CTS2 WITH GRANT OPTION;");
		// PASS:7509 If usage granted successfully?

		stmt.executeUpdate("SET SESSION AUTHORIZATION 'CTS2'");
		// PASS:7509 If session set successfully?

		stmt.executeUpdate("CREATE TABLE EMP_INFO "
				+ "(  empno       emp_nos, " + "emp_name    char(10), "
				+ "salary      smallint);");
		// PASS:7509 If table created successfully?

		stmt.executeUpdate("INSERT INTO EMP_INFO VALUES (1, 'watters',20000);");
		// PASS:7509 If 1 row inserted successfully?

		stmt.executeUpdate("GRANT USAGE ON emp_nos TO CTS3; ");
		// PASS:7509 If usage granted successfully?

		stmt.executeUpdate("SET SESSION AUTHORIZATION 'CTS3' ");
		// PASS:7509 If session set successfully?

		stmt.executeUpdate("DROP TABLE emp CASCADE; ");
		// PASS:7509 If table dropped successfully?

		stmt.executeUpdate("CREATE TABLE emp " + "(col1    emp_nos, "
				+ "col2    char(10));");
		// PASS:7509 If table created successfully?

		stmt.executeUpdate("INSERT INTO emp VALUES (2, 'pratt');");
		// PASS:7509 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT col1 FROM emp;");
		assertEquals(rs.getInt(1), 2);
		// PASS:7509 If col1 = 2?

		stmt.executeUpdate("DROP TABLE EMP CASCADE; ");
		// PASS:7509 If table dropped successfully?

		stmt.executeUpdate("SET SESSION AUTHORIZATION 'CTS2' ");
		// PASS:7509 If session set successfully?

		stmt.executeUpdate("DROP TABLE EMP_INFO CASCADE; ");
		//PASS:7509 If table dropped successfully?

		stmt.executeUpdate("SET SESSION AUTHORIZATION 'CTS1' ");
		// PASS:7509 If session set successfully?

		stmt.executeUpdate("DROP DOMAIN emp_nos CASCADE; ");
		// PASS:7509 If domain dropped successfully?

		// END TEST >>> 7509 <<< END TEST

	}

	/*
	 * Name: testYts_760
	 * 
	 *  
	 */
	public void testYts_760() throws SQLException {

		// TEST:7534 CASE expression with one simple WHEN!

		try {
			// just to be safe
			stmt.executeUpdate("DROP TABLE STAFFC");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("CREATE TABLE STAFFc "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15), " + "MGR CHAR(3), "
				+ "UNIQUE (EMPNUM));");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E1','Alice',12,'Deale',NULL);");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E2','Betty',10,'Vienna','E1');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E3','Carmen',13,'Vienna','E2');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E4','Don',12,'Deale','E2');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E5','Don',12,'Deale','E1');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E6','Tom',14,'Gettysburg','E5');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E7','Kingdom',18,'Gettysburg','E7');");

		stmt.executeUpdate("UPDATE STAFFc "
				+ "SET GRADE = CASE GRADE WHEN 13 THEN 23 END, "
				+ "MGR = CASE MGR WHEN 'E5' THEN 'E9' ELSE 'E8' END;");
		// PASS:7534 If update completed successfully?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFc WHERE GRADE IS NULL;");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:7534 If COUNT = 6?

		rs = stmt.executeQuery("SELECT GRADE FROM STAFFc WHERE EMPNUM = 'E3';");
		rs.next();
		assertEquals(23, rs.getInt(1));
		// PASS:7534 If GRADE = 23?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFc WHERE MGR = 'E8';");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:7534 If COUNT = 6?

		rs = stmt.executeQuery("SELECT MGR FROM STAFFc WHERE EMPNUM = 'E6';");
		rs.next();
		assertEquals("E9", rs.getString(1).trim());
		// PASS:7534 If MGR = E9?

		stmt.executeUpdate("DROP TABLE STAFFC");

		// END TEST >>> 7534 <<< END TEST

	}

	/*
	 * Name: testYts_761
	 * 
	 *  
	 */
	public void testYts_761() throws SQLException {

		// TEST:7534 CASE expression with one simple WHEN!

		try {
			// just to be safe
			stmt.executeUpdate("DROP TABLE STAFFC");
		} catch (SQLException dontcare) {
		}
		try {
			// just to be safe
			stmt.executeUpdate("DROP TABLE STAFFD");
		} catch (SQLException dontcare) {
		}

		// first have to rebuild staffc table
		stmt.executeUpdate("CREATE TABLE STAFFc "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15), " + "MGR CHAR(3), "
				+ "UNIQUE (EMPNUM));");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E1','Alice',12,'Deale',NULL);");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E2','Betty',10,'Vienna','E1');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E3','Carmen',13,'Vienna','E2');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E4','Don',12,'Deale','E2');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E5','Don',12,'Deale','E1');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E6','Tom',14,'Gettysburg','E5');");

		stmt.executeUpdate("INSERT INTO STAFFc "
				+ "VALUES ('E7','Kingdom',18,'Gettysburg','E7');");

		// OK! ready to get started.

		stmt.executeUpdate("CREATE TABLE STAFFd "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "GRADE DECIMAL(4), "
				+ "MGR CHAR(3));");
		// TEST:7535 CASE expression with searched WHEN!

		assertEquals(7, stmt
				.executeUpdate("INSERT INTO STAFFd (EMPNUM, GRADE, MGR) "
						+ "SELECT EMPNUM, CASE WHEN GRADE = 13 THEN 23 "
						+ "WHEN GRADE = 14 THEN 24 "
						+ "WHEN GRADE = 12 THEN 22 " + "END, "
						+ "CASE WHEN MGR = 'E2' THEN 'E6' "
						+ "WHEN MGR = 'E1' THEN 'E7'" + "ELSE 'E4'" + " END "
						+ "FROM STAFFc;"));
		// PASS:7535 If insert statement completed successfully?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE GRADE = 22;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7535 If COUNT = 3?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE GRADE = 23;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7535 If COUNT = 1?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE GRADE = 24;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7535 If COUNT = 1?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE GRADE IS NULL;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7535 If COUNT = 2?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE MGR = 'E7';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7535 If COUNT = 2?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE MGR = 'E6';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7535 If COUNT = 2?

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM STAFFd WHERE MGR = 'E4';");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7535 If COUNT = 3?

		// END TEST >>> 7535 <<< END TEST
	}

	/*
	 * Name: testYts_762
	 * 
	 * Firebird SQL doesn't support EXCEPT operator for merging select lists.
	 *  
	 */
	public void testYts_762() throws SQLException {

		// TEST:7517 <query expression> with EXCEPT!
		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL"))
			return;

		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM CHAR(3) NOT NULL, "
				+ "EMPNAME  CHAR(20)," + "GRADE DECIMAL(4),"
				+ "CITY CHAR(15));");

		stmt.executeUpdate("CREATE TABLE STAFF4 "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15)); ");

		stmt.executeUpdate("CREATE TABLE ET " + "(col1    CHAR(3), "
				+ "col2    CHAR(20), " + "col3    DECIMAL(4), "
				+ "col4    CHAR(15), " + "col5    INTEGER, "
				+ "col6    INTEGER);");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E7','Grace',10,'Paris');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E7','Grace',10,'Paris');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E7','Grace',10,'Paris');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E7','Grace',10,'Paris');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E7','Grace',10,'Paris');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E7','Grace',10,'Paris');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E8','Henry',20,'Prague');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E8','Henry',20,'Prague');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E8','Henry',20,'Prague');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E8','Henry',20,'Prague');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E8','Henry',20,'Prague');"));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E9','Imogen',10,'Prague'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E9','Imogen',10,'Prague'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E10','John',20,'Brussels'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E10','John',20,'Brussels'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E11','Keith',10,'Vienna'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E11','Keith',10,'Vienna'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E11','Keith',10,'Vienna'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E12','Laura',20,'Deale'); "));
		// PASS:7517 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 VALUES "
				+ "('E13','Mike',30,'Vienna'); "));
		// PASS:7517 If 1 row inserted successfully?

		String query = "SELECT * FROM STAFF1 EXCEPT SELECT * FROM STAFF4;";
		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals("E10", rs.getString(1).trim());
		assertEquals("John", rs.getString(2).trim());
		assertEquals(20, rs.getInt(3));
		assertEquals("Brussels", rs.getString(4).trim());
		rs.next();
		assertEquals("E12", rs.getString(1).trim());
		assertEquals("Laura", rs.getString(2).trim());
		assertEquals(20, rs.getInt(3));
		assertEquals("Deale", rs.getString(4).trim());
		rs.next();
		assertEquals("E9", rs.getString(1).trim());
		assertEquals("Imogen", rs.getString(2).trim());
		assertEquals(10, rs.getInt(3));
		assertEquals("Prague", rs.getString(4).trim());

		// PASS:7517 If 3 rows are selected in any order?
		// PASS:7517 If E9 Imogen 10 Prague?
		// PASS:7517 If E10 John 20 Brussels?
		// PASS:7517 If E12 Laura 20 Deale?

		query = "INSERT INTO ET (col1, col2, col3, col4) "
				+ "SELECT * FROM STAFF1 EXCEPT ALL " + "SELECT * FROM STAFF4; ";
		// PASS:7517 If insert completed successfully?

		rs = stmt.executeQuery("SELECT COUNT (DISTINCT COL1) FROM ET;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:7517 If COUNT = 4?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM ET;");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:7517 If COUNT = 6?

		stmt.executeUpdate("DELETE FROM ET;");

		query = "INSERT INTO ET (col1, col2, col3, col4) "
				+ "SELECT DISTINCT * FROM STAFF1 EXCEPT ALL "
				+ "SELECT * FROM STAFF4;";
		//PASS:7517 If insert completed successfully?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM ET;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7517 If COUNT = 3?

		stmt.executeUpdate("DELETE FROM ET;");

		stmt.executeUpdate("INSERT INTO ET (col1, col2, col3, col4) "
				+ "SELECT * FROM STAFF1 EXCEPT ALL "
				+ "SELECT DISTINCT * FROM STAFF4;");
		// PASS:7517 If insert completed successfully?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM ET;");
		rs.next();
		assertEquals(9, rs.getInt(1));
		// PASS:7517 If COUNT = 9?

		stmt.executeUpdate("DROP TABLE ET");
		stmt.executeUpdate("DROP TABLE STAFF4");
		stmt.executeUpdate("DROP TABLE STAFF1");

		// END TEST >>> 7517 <<< END TEST

	}

	/*
	 * Name: testYts_763
	 * 
	 * Firebird SQL doesn't have an INTERSECT CORRESPONDING operator. This would
	 * let the INTERSECT operation work only on the named columns. In Firebird
	 * SQL the select lists would have to have the same number of columns. So,
	 * we've written the query using EXISTS and an explicit select list.
	 * 
	 * TODO: Revise when Firebird SQL supports INTERSECT CORRESPONDING
	 * 
	 * TODO: Should have my Firebird query inspected. Don't understand why
	 * DISTINCT was required to get the correct resultSet.
	 *  
	 */

	public void testYts_763_QueryWithIntersectCorresponding() throws SQLException {

		// TEST:7518 <query expression> with INTERSECT CORRESPONDING!

		try {
			stmt.executeUpdate("DROP TABLE STAFF_CTS");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("DROP TABLE STAFF");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("DROP TABLE ET");
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("CREATE TABLE STAFF_CTS " + "(  PNUM   CHAR(3), "
				+ "CITY   CHAR(15), " + "GRADE  DECIMAL(4), "
				+ "EMPNAME CHAR(20));");

		stmt.executeUpdate("CREATE TABLE STAFF_CTS2 "
				+ "(EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15));");

		stmt.executeUpdate("  CREATE TABLE ET " + "(col1 CHAR(3), "
				+ "col2 CHAR(20), " + "col3 DECIMAL(4), " + "col4 CHAR(15), "
				+ "col5 INTEGER, " + "col6 INTEGER);");

		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E1','Alice',12,'Deale');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E1','Alice',12,'Deale');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E2','Betty',10,'Vienna');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E3','Carmen',13,'Vienna');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E3','Carmen',13,'Vienna');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E3','Carmen',13,'Vienna');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E4','Don',12,'Deale');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS2 VALUES ('E6','Don',12,'Deale');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS VALUES ('P1','Deale',12,'Don');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS VALUES ('P3','Vienna',10,'Betty');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS VALUES ('P3','Vienna',10,'Betty');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS VALUES ('P4','Vienna',13,'Carmen');");
		stmt
				.executeUpdate("INSERT INTO STAFF_CTS VALUES ('P5','Prague',15,'Ed');");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF_CTS2;");
		rs.next();
		assertEquals(8, rs.getInt(1));
		// PASS:7518 If COUNT = 8?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF_CTS;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7518 If COUNT = 5?

		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");
		if (dialect.equalsIgnoreCase("FirebirdSQL"))
			stmt
					.executeUpdate("INSERT INTO ET(col2, col3, col4) "
							+ "SELECT distinct empname, grade, city "
							+ "FROM staff_cts  cts "
							+ "WHERE EXISTS "
							+ "( "
							+ "SELECT 1 "
							+ "FROM staff_cts2 cts2 "
							+ "WHERE (cts.empname = cts2.empname and cts.grade=cts2.grade and cts.city=cts2.city))");
		else
			stmt.executeQuery("INSERT INTO ET(col2, col3, col4) "
					+ "SELECT * FROM STAFF_CTS2 " + "INTERSECT CORRESPONDING "
					+ "SELECT * FROM STAFF_CTS;");
		// PASS:7518 If insert completed successfully?

		rs = stmt.executeQuery("SELECT col2, col3, col4  " + "FROM ET "
				+ "ORDER BY col3, col4;");
		rs.next();
		assertEquals("Betty", rs.getString(1).trim());
		assertEquals(10, rs.getInt(2));
		assertEquals("Vienna", rs.getString(3).trim());
		rs.next();
		assertEquals("Don", rs.getString(1).trim());
		assertEquals(12, rs.getInt(2));
		assertEquals("Deale", rs.getString(3).trim());
		rs.next();
		assertEquals("Carmen", rs.getString(1).trim());
		assertEquals(13, rs.getInt(2));
		assertEquals("Vienna", rs.getString(3).trim());
		// PASS:7518 If 3 rows are selected in the following order?
		//                col2 col3 col4
		//                ==== ==== ====
		// PASS:7518 If Betty 10 Vienna?
		// PASS:7518 If Don 12 Deale?
		// PASS:7518 If Carmen 13 Vienna?

		// END TEST >>> 7518 <<< END TEST

	}

	/*
	 * Name: testYts_764
	 * 
	 * Notes: <query expression> with UNION ALL CORRESPONDING BY!
	 */
	public void testYts_764() throws SQLException {
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");

		// TEST:7519 <query expression> with UNION ALL CORRESPONDING BY!

		try {
			stmt.executeUpdate("DROP TABLE ET");
		} catch (SQLException sqle) {
		}

		stmt.executeUpdate("CREATE TABLE ET " + "(col1    CHAR(3), "
				+ "col2    CHAR(20), " + "col3    DECIMAL(4), "
				+ "col4    CHAR(15), " + "col5    INTEGER, "
				+ "col6    INTEGER);");

		stmt.executeUpdate("CREATE TABLE STAFFa " + "(HOURS   INTEGER, "
				+ " SALARY  DECIMAL(6), " + "EMPNUM  CHAR(3), "
				+ "PNUM    DECIMAL(4), " + "EMPNAME CHAR(20));");

		stmt.executeUpdate("CREATE TABLE STAFFb " + "( SALARY   DECIMAL(6), "
				+ "EMPNAME  CHAR(20), " + "HOURS    INTEGER, "
				+ "PNUM     CHAR(3), " + "CITY     CHAR(15), "
				+ "SEX      CHAR);");

		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (40,10000,'E6',2,'Fred');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (40,10000,'E6',2,'Fred');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (40,10000,'E1',3,'Alice');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (40,10000,'E1',3,'Alice');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (70,40000,'E4',3,'Don');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (70,40000,'E4',3,'Don');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (70,40000,'E4',3,'Don');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (30,20000,'E2',1,'Betty');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (60,45000,'E7',4,'Grace');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (60,45000,'E7',4,'Grace');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (30,8000,'E8',2,'Henry');");
		stmt
				.executeUpdate("INSERT INTO STAFFa VALUES (15,7000,'E9',1,'Imogen');");

		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (10000,'Fred',40,'P2','Vienna','M');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (10000,'Fred',40,'P2','Vienna','M');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (10000,'Fred',40,'P2','Vienna','M');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (15000,'Carmen',35,'P2','Vienna','F');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (15000,'Carmen',35,'P2','Vienna','F');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (10000,'Alice',40,'P3','Prague','F');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (20000,'Betty',30,'P1','Deale','F');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (20000,'Betty',30,'P1','Deale','F');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (40000,'Don',70,'P3','Prague','M');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (40000,'Don',70,'P3','Prague','M');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (40000,'Don',70,'P3','Prague','M');");
		stmt
				.executeUpdate("INSERT INTO STAFFb VALUES (10000,'Ed',40,'P1','Deale','M');");

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFFb;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:7519 If COUNT = 12?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFFa;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:7519 If COUNT = 12?

		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			// TODO: I could never get the union to work properly on an INSERT
			// INTO clause. Firebird said that UNION was unknown token in this
			// example. Seems like it should have worked. Also, couldn't get an
			// order by select statement, so we'll just count the number of
			// records returned.
			rs = stmt.executeQuery("SELECT hours, salary FROM STAFFB "
					+ "UNION SELECT hours, salary FROM STAFFA");
			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
			}
			assertEquals(7, rowCount);
		} else {
			assertEquals(7, stmt.executeUpdate("INSERT INTO ET (col5, col6) "
					+ "SELECT * FROM STAFFB "
					+ "UNION ALL CORRESPONDING BY (hours, salary) "
					+ "SELECT * FROM STAFFA; "));
			// PASS:7519 If insert completed successfully?
			rs = stmt.executeQuery("SELECT COUNT (*), col5, col6 " + "FROM ET "
					+ "GROUP BY col6, col5 " + "ORDER BY 2,3;");
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals(15, rs.getInt(2));
			assertEquals(7000, rs.getInt(3));
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals(30, rs.getInt(2));
			assertEquals(8000, rs.getInt(3));
			rs.next();
			assertEquals(3, rs.getInt(1));
			assertEquals(30, rs.getInt(2));
			assertEquals(20000, rs.getInt(3));
			rs.next();
			assertEquals(2, rs.getInt(1));
			assertEquals(35, rs.getInt(2));
			assertEquals(15000, rs.getInt(3));
			rs.next();
			assertEquals(9, rs.getInt(1));
			assertEquals(40, rs.getInt(2));
			assertEquals(10000, rs.getInt(3));
			rs.next();
			assertEquals(2, rs.getInt(1));
			assertEquals(60, rs.getInt(2));
			assertEquals(45000, rs.getInt(3));
			rs.next();
			assertEquals(6, rs.getInt(1));
			assertEquals(70, rs.getInt(2));
			assertEquals(40000, rs.getInt(3));
			// PASS:7519 If 7 rows are returned in the following order?
			//               count col5 col6
			//               ===== ==== ====
			// PASS:7519 If 1 15 7000?
			// PASS:7519 If 1 30 8000?
			// PASS:7519 If 3 30 20000?
			// PASS:7519 If 2 35 15000?
			// PASS:7519 If 9 40 10000?
			// PASS:7519 If 2 60 45000?
			// PASS:7519 If 6 70 40000?
		}
		stmt.executeUpdate("DROP TABLE ET");
		stmt.executeUpdate("DROP TABLE STAFFB");
		stmt.executeUpdate("DROP TABLE STAFFA");

		// END TEST >>> 7519 <<< END TEST

	}

	// testYts_765 skipped
	// Explicit table constraints in TABLE_CONSTRAINTS view!

	// testYts_766 skipped
	// Explicit table constraints - REFERENTIAL_CONSTRAINTS!

	// testYts_767 skipped
	// Explicit table constraints in CHECK_CONSTRAINTS view!

	// testYts768 skipped
	// Access to SCHEMATA view!

	// testYts769 skipped
	// Access to DOMAINS view!

	// testYts770 skipped
	// Access to DOMAIN_CONSTRAINTS view!

	// testYts771 skipped
	// Access to CHARACTER_SETS view!

	// testYts772 skipped
	// Access to ASSERTIONS view!

	// testYts773 skipped
	// Access to SQL_LANGUAGES view!

	// testYts774 skipped
	// Access to INFORMATION_SCHEMA_CATALOG_NAME base table!

	// testYts776 skipped
	// DROP SCHEMA - empty schema with restrict!

	// testYts777 skipped
	// DROP SCHEMA - non-empty schema!

	/*
	 * Name: testYts_778
	 * 
	 * Notes: Implementation of this test is poor, as Firebird doesn't support
	 * the ability to drop a default column value.
	 * 
	 * TODO: When Firebird supports ability to "ALTER TABLE ALTER COLUMN
	 * columnName TYPE SET DEFAULT default_value" we can revisit this test.
	 *  
	 */
	public void testYts_778() throws SQLException {
		int rowCount;
		// TEST:7520 ALTER TABLE SET COLUMN DEFAULT!
		try {
			stmt.executeUpdate("DROP DOMAIN INT_DOM2;");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP TABLE ALT_TEST ");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("CREATE DOMAIN int_dom2 AS INTEGER DEFAULT 99;");
		// PASS:7520 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE alt_test "
				+ "( K integer, L integer DEFAULT 50, M integer, N int_dom2);");
		// PASS:7520 If table created successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test VALUES (1,1,1,1);"));
		// PASS:7520 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test (K,L,N) VALUES (2,2,2);"));
		// PASS:7520 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test (K,M) VALUES (3,3);"));
		// PASS:7520 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT * FROM alt_test ORDER BY K;");
		// PASS:7520 If 3 rows are returned in the following order?
		//                 c1 c2 c3 c4
		//                 == == == ==
		// PASS:7520 If 1 1 1 1?
		// PASS:7520 If 2 2 NULL 2?
		// PASS:7520 If 3 50 3 99?
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		assertEquals(1, rs.getInt(3));
		assertEquals(1, rs.getInt(4));

		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertEquals(2, rs.getInt(4));

		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(50, rs.getInt(2));
		assertEquals(3, rs.getInt(3));
		assertEquals(99, rs.getInt(4));

		// ALTER TABLE alt_test ALTER COLUMN L SET DEFAULT 100;
		// PASS:7520 If table altered successfully?

		// ALTER TABLE alt_test ALTER COLUMN M SET DEFAULT 90;
		// PASS:7520 If table altered successfully?

		// ALTER TABLE alt_test ALTER COLUMN N SET DEFAULT 80;
		// PASS:7520 If table altered successfully?

		rs = stmt.executeQuery("SELECT * FROM alt_test ORDER BY K;");
		// PASS:7520 If 3 rows are returned in the following order?
		//                 c1 c2 c3 c4
		//                 == == == ==
		// PASS:7520 If 1 1 1 1?
		// PASS:7520 If 2 2 NULL 2?
		// PASS:7520 If 3 50 3 99?
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		assertEquals(1, rs.getInt(3));
		assertEquals(1, rs.getInt(4));

		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertEquals(2, rs.getInt(4));

		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(50, rs.getInt(2));
		assertEquals(3, rs.getInt(3));
		assertEquals(99, rs.getInt(4));

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test VALUES (4,4,4,4);"));
		// PASS:7520 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test(K,L) VALUES (5,5);"));
		// PASS:7520 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test(K,M) VALUES (6,6);"));
		// PASS:7520 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT * FROM alt_test ORDER BY K;");
		// PASS:7520 If 6 rows are returned in the following order?
		//                 c1 c2 c3 c4
		//                 == == == ==
		// PASS:7520 If 1 1 1 1?
		// PASS:7520 If 2 2 NULL 2?
		// PASS:7520 If 3 50 3 99?
		// PASS:7520 If 4 4 4 4?
		// PASS:7520 If 5 5 90 80?
		// PASS:7520 If 6 100 6 80?

		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(1, rs.getInt(2));
		assertEquals(1, rs.getInt(3));
		assertEquals(1, rs.getInt(4));

		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		assertEquals(2, rs.getInt(4));

		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(50, rs.getInt(2));
		assertEquals(3, rs.getInt(3));
		assertEquals(99, rs.getInt(4));

		rs.next();
		assertEquals(4, rs.getInt(1));
		assertEquals(4, rs.getInt(2));
		//		assertEquals(4, rs.getInt(3));
		//		assertEquals(4, rs.getInt(4));

		rs.next();
		assertEquals(5, rs.getInt(1));
		assertEquals(5, rs.getInt(2));
		//		assertEquals(90, rs.getInt(3));
		//		assertEquals(80, rs.getInt(4));

		rs.next();
		assertEquals(6, rs.getInt(1));
		//		assertEquals(100, rs.getInt(2));
		assertEquals(6, rs.getInt(3));
		//		assertEquals(80, rs.getInt(4));

		// was DROP TABLE ALT_TEST CASCADE;
		stmt.executeUpdate("DROP TABLE ALT_TEST ");
		// PASS:7520 If table dropped successfully?

		// was DROP DOMAIN INT_DOM2 CASCADE;
		stmt.executeUpdate("DROP DOMAIN INT_DOM2;");
		// PASS:7520 If domain dropped successfully?
		// END TEST >>> 7520 <<< END TEST
	}

	/*
	 * Name: testYts_779
	 * 
	 * Notes: This test can't be currently implemented under Firebird, since
	 * Firebird doesn't support the ability to drop a default column value.
	 * 
	 * TODO: When Firebird supports the ability for ALTER TABLE ALTER COLUMN
	 * column DROP DEFAULT, this test can be revised.
	 *  
	 */
	public void testYts_779() throws SQLException {
		// TEST:7521 ALTER TABLE DROP COLUMN DEFAULT!

		try {
			stmt.executeUpdate("DROP TABLE ALT_TEST;");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP DOMAIN INT_DOM2;");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("CREATE DOMAIN int_dom2 AS INTEGER DEFAULT 99;");
		// PASS:7521 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE alt_test" + " ( K integer,"
				+ "L integer DEFAULT 100, " + "M integer default 90, "
				+ "N int_dom2 default 80);");
		// PASS:7521 If table created successfully?

		//stmt.executeUpdate("ALTER TABLE alt_test ALTER COLUMN L DROP
		// DEFAULT;");
		// PASS:7521 If table altered successfully?

		//stmt.executeUpdate("ALTER TABLE alt_test ALTER COLUMN M DROP
		// DEFAULT;");
		// PASS:7521 If table altered successfully?

		//stmt.executeUpdate("ALTER TABLE alt_test ALTER COLUMN N DROP
		// DEFAULT;");
		// PASS:7521 If table altered successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO alt_test (K) VALUES (7);"));
		// PASS:7521 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT * FROM alt_test;");
		// PASS:7521 If column1 = 7?
		// PASS:7521 If column2 is NULL?
		// PASS:7521 If column3 is NULL?
		// PASS:7521 If column4 = 99?
		rs.next();
		assertEquals(7, rs.getInt(1));
		// assertEquals(0, rs.getInt(2));
		// assertEquals(0, rs.getInt(3));
		// assertEquals(99, rs.getInt(4));

		stmt.executeUpdate("DROP TABLE ALT_TEST ;");
		// PASS:7521 If table dropped successfully?

		stmt.executeUpdate("DROP DOMAIN INT_DOM2 ;");
		// PASS:7521 If domain dropped successfully?

		// END TEST >>> 7521 <<< END TEST

	}

	// testYts_780: skipped
	// 7560 <time zone interval> in literal!

	// testYts_781: skipped
	// Set local time zone - valid value!

	// testYts_782: skipped
	// Set local time zone - invalid value, exception!

	// testYts_788: skipped
	// CREATE CHARACTER SET, implicit default collation!

	// testYts_789: skipped
	// CREATE CHAR SET in schema def, COLLATION FROM DEFAULT!

	// Yts790 skipped
	// GRANT USAGE on character set, WITH GRANT OPTION! (1)

	// testYts791 skipped
	// GRANT USAGE on character set, WITH GRANT OPTION! (2)

	// testYts792 skipped
	// GRANT USAGE on character set, WITH GRANT OPTION! (3)

	// testYts793 skipped
	// GRANT USAGE on character set, no WGO!

	// testYts794 skipped
	// TEST:7528 GRANT USAGE on character set, no WGO!

	// testYts795 skipped
	// TEST:7529 GRANT USAGE on character set no WGO!

	/*
	 * Name: testYts_796
	 * 
	 * Notes: <scalar subquery> as first operand in <comp pred>!
	 *  
	 */
	public void testYts_796() throws SQLException {

		// TEST:7530 <scalar subquery> as first operand in <comp pred>!

		stmt.executeUpdate("CREATE TABLE STAFF "
				+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");

		stmt.executeUpdate("CREATE TABLE WORKS " + "(EMPNUM CHAR(3) NOT NULL, "
				+ "PNUM CHAR(3) NOT NULL, " + "HOURS DECIMAL(5), "
				+ "UNIQUE(EMPNUM,PNUM));");

		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E1','P1',40);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E1','P2',20);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E1','P3',80);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E1','P4',20);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E1','P5',12);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E1','P6',12);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E2','P1',40);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E2','P2',80);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E3','P2',20);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E4','P2',20);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E4','P4',40);");
		stmt.executeUpdate("INSERT INTO WORKS VALUES  ('E4','P5',80);");

		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E1','Alice',12,'Deale');");
		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E2','Betty',10,'Vienna');");
		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E3','Carmen',13,'Vienna');");
		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E4','Don',12,'Deale');");
		stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES ('E5','Ed',13,'Akron');");

		rs = stmt.executeQuery("SELECT EMPNAME " + "FROM STAFF WHERE "
				+ "(SELECT EMPNUM FROM WORKS WHERE PNUM = 'P3') = EMPNUM;");
		rs.next();
		assertEquals("Alice", rs.getString(1).trim());
		// PASS:7530 If empname = 'Alice'?

		try {
			rs = stmt.executeQuery("SELECT EMPNAME FROM STAFF WHERE "
					+ "(SELECT EMPNUM FROM WORKS WHERE PNUM = 'P4') = EMPNUM;");
			// PASS:7530 If ERROR - cardinality violation?
			fail("Should produce cardinality violation");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("DROP TABLE STAFF");
		stmt.executeUpdate("DROP TABLE WORKS");

		// END TEST >>> 7530 <<< END TEST
	}

	/*
	 * Name: testYts_797
	 * 
	 * Notes: <scalar subquery> in SET of searched update!
	 * 
	 * TODO: Could not get this working with Firebird SQL.
	 */
	public void testYts_797() throws SQLException {
		// TEST:7558 <scalar subquery> in SET of searched update!
		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL"))
			return;

		stmt.executeUpdate("CREATE TABLE TV (A INTEGER, B CHAR);");
		stmt.executeUpdate("CREATE TABLE TW (D CHAR, E INTEGER);");
		stmt.executeUpdate("INSERT INTO TV VALUES (1,'a');");
		stmt.executeUpdate("INSERT INTO TV VALUES (2,'b');");
		stmt.executeUpdate("INSERT INTO TV VALUES (3,'c');");
		stmt.executeUpdate("INSERT INTO TV VALUES (4,'d');");
		stmt.executeUpdate("INSERT INTO TV VALUES (5,'e');");
		stmt.executeUpdate("INSERT INTO TW VALUES ('b',2);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('g',1);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('f',2);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('h',4);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('i',5);");
		try {
			stmt.executeUpdate("UPDATE TV AS X "
					+ "SET B = (SELECT D FROM TV AS Y, TW AS Z "
					+ "WHERE Y.A = Z.E AND X.A = Y.A);");
			// PASS:7558 If ERROR - cardinality violation?
			fail("UPDATE should violate database cardinality");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("UPDATE TV AS X "
				+ "SET B = (SELECT D FROM TV AS Y, TW AS Z"
				+ "WHERE Y.A = Z.E AND Z.E <> 2 AND X.A = Y.A);");
		// PASS:7558 If UPDATE completed successfully?

		rs = stmt.executeQuery("SELECT B FROM TV WHERE A = 1;");
		rs.next();
		assertEquals("g", rs.getString(1).trim());
		// PASS:7558 If B = 'g'?

		rs = stmt.executeQuery("SELECT B FROM TV WHERE A = 2;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7558 If B = NULL?

		rs = stmt.executeQuery("SELECT B FROM TV WHERE A = 3;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7558 If B = NULL?

		rs = stmt.executeQuery("SELECT B FROM TV WHERE A = 4;");
		rs.next();
		assertEquals("h", rs.getString(1).trim());
		// PASS:7558 If B = 'h'?

		rs = stmt.executeQuery("SELECT B FROM TV WHERE A = 5");
		rs.next();
		assertEquals("i", rs.getString(1).trim());
		// PASS:7558 If B = 'i'?

		// END TEST >>> 7558 <<< END TEST
		stmt.executeUpdate("drop TABLE TV");
		stmt.executeUpdate("drop TABLE TW");
	}

	/*
	 * Name: testYts_798
	 * 
	 * Notes: <scalar subquery> in <select list> of single-row select!
	 * 
	 * FB SQL is slightly different here - we don't like the AS in the select
	 * clause.
	 *  
	 */
	public void testYts_798() throws SQLException {
		// TEST:7559 <scalar subquery> in <select list> of single-row select!
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");
		String query;

		stmt.executeUpdate("CREATE TABLE TV (A INTEGER, B CHAR);");
		stmt.executeUpdate("CREATE TABLE TW (D CHAR, E INTEGER);");
		stmt.executeUpdate("INSERT INTO TV VALUES (1,'a');");
		stmt.executeUpdate("INSERT INTO TV VALUES (2,'b');");
		stmt.executeUpdate("INSERT INTO TV VALUES (3,'c');");
		stmt.executeUpdate("INSERT INTO TV VALUES (4,'d');");
		stmt.executeUpdate("INSERT INTO TV VALUES (5,'e');");
		stmt.executeUpdate("INSERT INTO TW VALUES ('b',2);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('g',1);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('f',2);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('h',4);");
		stmt.executeUpdate("INSERT INTO TW VALUES ('i',5);");
		try {
			if (dialect.equalsIgnoreCase("FirebirdSQL"))
				query = "SELECT DISTINCT A, (SELECT D FROM TW WHERE E = TV.A) "
						+ "FROM TV, TW "
						+ "WHERE 1 < (SELECT COUNT (*) FROM TV, TW "
						+ "WHERE A = TV.A AND A = E);";
			else
				query = "SELECT DISTINCT A, (SELECT D FROM TW WHERE E = X.A) "
						+ "FROM TV AS X, TW AS Y "
						+ "WHERE 1 <(SELECT COUNT (*) FROM TV, TW "
						+ "WHERE A = X.A AND A = E);";

			rs = stmt.executeQuery(query);
			fail("SELECT should product cardinality violation");
			// PASS:7559 If ERROR - cardinality violation?
		} catch (SQLException sqle) {
		}

		if (dialect.equalsIgnoreCase("FirebirdSQL"))
			query = "SELECT DISTINCT A, "
					+ "(SELECT D FROM TW  WHERE E = TV.A) "
					+ "FROM TV , TW   WHERE A = 1;";
		else
			query = "SELECT DISTINCT A, "
					+ "(SELECT D FROM TW  WHERE E = X.A) "
					+ "FROM TV AS X, TW AS Y WHERE A = 1;";
		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals("g", rs.getString(2).trim());
		// PASS:7559 If A = 1 and D = 'g'?

		if (dialect.equalsIgnoreCase("FirebirdSQL"))
			query = "SELECT DISTINCT A, "
					+ "(SELECT D FROM TW  WHERE E = TV.A) "
					+ "FROM TV, TW WHERE A = 3";
		else
			query = "SELECT DISTINCT A, " + "(SELECT D FROM TW WHERE E = X.A) "
					+ "FROM TV AS X, TW AS Y WHERE A = 3;";

		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(null, rs.getString(2));
		// PASS:7559 If A = 3 and D = NULL?
		stmt.executeUpdate("drop TABLE TV");
		stmt.executeUpdate("drop TABLE TW");
		// END TEST >>> 7559 <<< END TEST
	}

	/*
	 * Name: testYts_799
	 * 
	 * Notes: <subquery> as <row val constr> in <null predicate>!
	 *  
	 */
	public void testYts_799() throws SQLException {
		// TEST:7531 <subquery> as <row val constr> in <null predicate>!

		stmt.executeUpdate("CREATE TABLE TT (TTA INTEGER, "
				+ "TTB INTEGER, TTC INTEGER);");
		stmt.executeUpdate("CREATE TABLE TU (TUD CHAR(2), TUE INTEGER);");
		stmt.executeUpdate("INSERT INTO TT (TTA, TTC) VALUES (1, 99);");
		stmt.executeUpdate("INSERT INTO TT (TTA, TTB) VALUES (2, 98);");
		stmt.executeUpdate("INSERT INTO TT VALUES (3, 97, 96);");
		stmt.executeUpdate("INSERT INTO TT (TTA) VALUES (4);");
		stmt.executeUpdate("INSERT INTO TT VALUES (5, 42, 26);");

		stmt.executeUpdate("INSERT INTO TU VALUES ('ab', 3);");
		stmt.executeUpdate("INSERT INTO TU (TUE) VALUES (5);");
		stmt.executeUpdate("INSERT INTO TU VALUES ('cd', 4);");
		stmt.executeUpdate("INSERT INTO TU (TUE) VALUES (11);");
		stmt.executeUpdate("INSERT INTO TU VALUES ('ef', 12);");
		stmt.executeUpdate("INSERT INTO TU VALUES ('gh', 11);");

		rs = stmt.executeQuery("SELECT TTA, TTB, TTC FROM TT "
				+ "WHERE (SELECT TUD FROM TU WHERE TU.TUE = TT.TTA) "
				+ "IS NULL ORDER BY TTA DESC;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		assertEquals(42, rs.getInt(2));
		assertEquals(26, rs.getInt(3));
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals(98, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(99, rs.getInt(3));
		// PASS:7531 If 3 rows are selected in the following order?
		//                  col1 col2 col3
		//                  ==== ==== ====
		// PASS:7531 If 5 42 26 ?
		// PASS:7531 If 2 98 NULL?
		// PASS:7531 If 1 NULL 99 ?

		rs = stmt.executeQuery("SELECT TTA, TTB, TTC FROM TT "
				+ "WHERE (SELECT TUD FROM TU WHERE TU.TUE = TT.TTA) "
				+ "IS NOT NULL ORDER BY TTA;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals(97, rs.getInt(2));
		assertEquals(96, rs.getInt(3));
		rs.next();
		assertEquals(4, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(0, rs.getInt(3));
		// PASS:7531 If 2 rows are selected in the following order?
		//                 col1 col1 col3
		//                 ==== ==== ====
		// PASS:7531 If 3 97 96 ?
		// PASS:7531 If 4 NULL NULL?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM TT "
				+ "WHERE TTB IS NULL OR TTC IS NULL; ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7531 If COUNT = 3?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM TT "
				+ "WHERE TTB IS NOT NULL AND TTC IS NOT NULL;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7531 If COUNT = 2?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM TT "
				+ "WHERE NOT (TTB IS NULL AND TTC IS NULL); ");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:7531 If COUNT = 4?

		stmt.executeUpdate("DROP TABLE TT");
		stmt.executeUpdate("DROP TABLE TU");
		// END TEST >>> 7531 <<< END TEST
	}

	/*
	 * Name: testYts_800
	 * 
	 * Notes: <null predicate> <interval value exp> as <row value cons>!
	 * 
	 * Firebird SQL doesn't support INTERVAL processing on dates, so not much to
	 * do here if Firebird...
	 *  
	 */
	public void testYts_800() throws SQLException {
		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL"))
			return;

		// TEST:7532 <null predicate><interval value exp> as <row value cons>!
		stmt.executeUpdate("CREATE TABLE TT2 (TTA INTEGER, "
				+ "TTB INTERVAL YEAR TO MONTH, " + "TTC DECIMAL(6,0));");
		stmt.executeUpdate("INSERT INTO TT2 "
				+ "VALUES (1,INTERVAL '17-3' YEAR TO MONTH,13);");
		stmt.executeUpdate("INSERT INTO TT2 (TTA,TTB) "
				+ "VALUES (2,INTERVAL '5-6' YEAR TO MONTH);");
		stmt.executeUpdate("INSERT INTO TT2 (TTA) " + "VALUES (3);");
		stmt.executeUpdate("INSERT INTO TT2 (TTA,TTC) " + "VALUES (4,20);");
		stmt.executeUpdate("INSERT INTO TT2 "
				+ "VALUES (5,INTERVAL '60-2' YEAR TO MONTH,19);");
		rs = stmt.executeQuery("SELECT TTA FROM TT2 "
				+ "WHERE TTB+CAST(TTC AS INTERVAL MONTH) IS NULL "
				+ "order by tta ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		rs.next();
		assertEquals(3, rs.getInt(1));
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:7532 If TTA = 2, 3, and 4 (in any order)?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM TT2 "
				+ "WHERE NOT TTB+CAST(TTC AS INTERVAL MONTH) IS NULL;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7532 If COUNT = 2?

		// END TEST >>> 7532 <<< END TEST
	}

	// testYts_802 : skipped
	// TEST:7548 Support of FIPS SQL_FEATURES table in documentation schema!

	// testYts_803 : skipped
	// Test:7549 Support SQL-SIZING table in documentation schema!

	// testYts_805 : skipped
	// TEST:7562 Schema with crossed referential constraints between tables!

	/*
	 * Name: testYts_806
	 * 
	 * Notes: NATURAL FULL OUTER JOIN <table reference> - dynamic!
	 * 
	 * Firebird SQL doesn't support NATURAL keyword.
	 */
	public void testYts_806_NaturalFullOuterJoinDynamic() throws SQLException {

		java.sql.DatabaseMetaData dbmd = conn.getMetaData();
		if ( (dbmd.getDatabaseProductVersion().indexOf("Vulcan 1.0") != -1) ||
				(dbmd.getDatabaseProductVersion().indexOf("Firebird") != -1)) {
			// No firebird variant currently supports natural full outer join
			return; 
		}

		// TEST:7563 NATURAL FULL OUTER JOIN <table reference> - dynamic!
		stmt.executeUpdate("CREATE TABLE STAFF1 "
				+ "(EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15)); ");

		stmt.executeUpdate("CREATE TABLE STAFFa " + "( HOURS   INTEGER, "
				+ "SALARY  DECIMAL(6), " + "EMPNUM  CHAR(3), "
				+ "PNUM    DECIMAL(4), " + "EMPNAME CHAR(20));");

		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E1','Alice',4,'Lyon');");
		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E1','Alice',8,'Lyon');");
		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E1','Alice',12,'Geneva');");
		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E2','Betty',16,'Strasbourg');");
		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E2','Betty',20,'Munich');");
		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E3','Colin',24,'Leuven');");
		stmt.executeUpdate("INSERT INTO STAFF1 VALUES "
				+ "('E4','Daniel',28,'Cologne');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(20,40000,'E1',11,'Alice');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(15,20000,'E2',12,'Betty');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(15,20000,'E2',13,'Betty');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(10,15000,'E3',14,'Colin');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(10,8000,'E3',15,'Colin');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(10,8000,'E3',16,'Colin');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(30,50000,'E5',17,'Edward');");
		// PASS:7563 If 14 rows inserted successfully from previous 14 inserts?

		rs = stmt.executeQuery("SELECT * FROM STAFF1 NATURAL FULL OUTER JOIN "
				+ "STAFFA ORDER BY EMPNUM, EMPNAME, GRADE, PNUM;");

		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals(4, rs.getInt(3));
		assertEquals("Lyon", rs.getString(4).trim());
		assertEquals(20, rs.getInt(5));
		assertEquals(40000, rs.getInt(6));
		assertEquals(11, rs.getInt(7));
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals(8, rs.getInt(3));
		assertEquals("Lyon", rs.getString(4).trim());
		assertEquals(20, rs.getInt(5));
		assertEquals(40000, rs.getInt(6));
		assertEquals(11, rs.getInt(7));
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals(12, rs.getInt(3));
		assertEquals("Geneva", rs.getString(4).trim());
		assertEquals(20, rs.getInt(5));
		assertEquals(40000, rs.getInt(6));
		assertEquals(11, rs.getInt(7));
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals("Betty", rs.getString(2).trim());
		assertEquals(16, rs.getInt(3));
		assertEquals("Strasbourg", rs.getString(4).trim());
		assertEquals(15, rs.getInt(5));
		assertEquals(20000, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals("Betty", rs.getString(2).trim());
		assertEquals(16, rs.getInt(3));
		assertEquals("Strasbourg", rs.getString(4).trim());
		assertEquals(15, rs.getInt(5));
		assertEquals(20000, rs.getInt(6));
		assertEquals(13, rs.getInt(7));
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals("Betty", rs.getString(2).trim());
		assertEquals(20, rs.getInt(3));
		assertEquals("Munich", rs.getString(4).trim());
		assertEquals(15, rs.getInt(5));
		assertEquals(20000, rs.getInt(6));
		assertEquals(12, rs.getInt(7));
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals("Betty", rs.getString(2).trim());
		assertEquals(20, rs.getInt(3));
		assertEquals("Munich", rs.getString(4).trim());
		assertEquals(15, rs.getInt(5));
		assertEquals(20000, rs.getInt(6));
		assertEquals(13, rs.getInt(7));

		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals("Colin", rs.getString(2).trim());
		assertEquals(24, rs.getInt(3));
		assertEquals("Leuven", rs.getString(4).trim());
		assertEquals(10, rs.getInt(5));
		assertEquals(15000, rs.getInt(6));
		assertEquals(14, rs.getInt(7));
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals("Colin", rs.getString(2).trim());
		assertEquals(24, rs.getInt(3));
		assertEquals("Leuven", rs.getString(4).trim());
		assertEquals(10, rs.getInt(5));
		assertEquals(8000, rs.getInt(6));
		assertEquals(15, rs.getInt(7));
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals("Colin", rs.getString(2).trim());
		assertEquals(24, rs.getInt(3));
		assertEquals("Leuven", rs.getString(4).trim());
		assertEquals(10, rs.getInt(5));
		assertEquals(8000, rs.getInt(6));
		assertEquals(16, rs.getInt(7));

		rs.next();
		assertEquals("E4", rs.getString(1).trim());
		assertEquals("Daniel", rs.getString(2).trim());
		assertEquals(28, rs.getInt(3));
		assertEquals("Cologne", rs.getString(4).trim());
		assertEquals(0, rs.getInt(5));
		assertEquals(0, rs.getInt(6));
		assertEquals(0, rs.getInt(7));

		rs.next();

		assertEquals("E5", rs.getString(1).trim());
		assertEquals("Edware", rs.getString(2).trim());
		assertEquals(0, rs.getInt(3));
		assertEquals(null, rs.getString(4));
		assertEquals(30, rs.getInt(5));
		assertEquals(50000, rs.getInt(6));
		assertEquals(17, rs.getInt(7));
		// PASS:7563 If 12 rows selected in the following order?
		//               eno enaam grd cit hrs sal pno
		//               === ===== === === === === ===
		// PASS:7563 If E1 Alice 4 Lyon 20 40000 11 ?
		// PASS:7563 If E1 Alice 8 Lyon 20 40000 11 ?
		// PASS:7563 If E1 Alice 12 Geneva 20 40000 11 ?
		// PASS:7563 If E2 Betty 16 Strasbourg 15 20000 12 ?
		// PASS:7563 If E2 Betty 16 Strasbourg 15 20000 13 ?
		// PASS:7563 If E2 Betty 20 Munich 15 20000 12 ?
		// PASS:7563 If E2 Betty 20 Munich 15 20000 13 ?
		// PASS:7563 If E3 Colin 24 Leuven 10 15000 14 ?
		// PASS:7563 If E3 Colin 24 Leuven 10 8000 15 ?
		// PASS:7563 If E3 Colin 24 Leuven 10 8000 16 ?
		// PASS:7563 If E4 Daniel 28 Cologne NULL NULL NULL?
		// PASS:7563 If E5 Edward NULL NULL 30 50000 17 ?

		// END TEST >>> 7563 <<< END TEST
	}

	// testYts_807 : skipped
	// TEST:7564 TIMEZONE_HOUR & TIMEZONE_MINUTE in <extract exptrssion>!

	/*
	 * Name: testYts_808
	 * 
	 * Notes: LOCAL time zone in <datetime value expression>!
	 * 
	 * Firebird SQL doesn't support AT LOCAL for timezone comparisons.
	 */
	public void testYts_808() throws SQLException {
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");

		// TEST:7565 LOCAL time zone in <datetime value expression>!

		if (dialect.equalsIgnoreCase("FirebirdSQL"))
			stmt.executeUpdate("CREATE TABLE TTIME_BASE "
					+ "(PK INTEGER not null, " + "TT TIME," + "TS TIMESTAMP,"
					+ "TT2 TIME ," + "TS2 TIMESTAMP ," + "PRIMARY KEY (PK));");
		else
			stmt.executeUpdate("CREATE TABLE TTIME_BASE " + "(PK INTEGER, "
					+ "TT TIME," + "TS TIMESTAMP," + "TT2 TIME WITH TIME ZONE,"
					+ "TS2 TIMESTAMP WITH TIME ZONE," + "PRIMARY KEY (PK));");
		stmt.executeUpdate("CREATE VIEW TTIME (PK, TT, TS) AS "
				+ "SELECT PK, TT, TS FROM TTIME_BASE;");

		stmt.executeUpdate("INSERT INTO TTIME VALUES "
				+ "(1, TIME '00:00:00', TIMESTAMP '1995-06-07 00:00:00');");
		// PASS:7565 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TTIME VALUES "
				+ "(2, TIME '10:15:13', TIMESTAMP '1980-04-10 10:15:13');");
		// PASS:7565 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TTIME VALUES "
				+ "(3, TIME '23:14:09', TIMESTAMP '1973-09-22 23:14:09');");
		// PASS:7565 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TTIME VALUES "
				+ "(4, TIME '05:39:42', TIMESTAMP '1999-12-31 05:39:42');");
		// PASS:7565 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TTIME VALUES "
				+ "(5, TIME '17:56:26', TIMESTAMP '1961-10-28 17:56:26');");
		// PASS:7565 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TTIME VALUES "
				+ "(6, TIME '17:56:26', TIMESTAMP '1961-10-28 17:56:26');");
		// PASS:7565 If 1 row inserted successfully?
		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME "
					+ "WHERE TT > TIME '10:30:05'");
			rs.next();
			assertEquals(3, rs.getInt(1));
		} else {
			rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME "
					+ "WHERE TT AT LOCAL > TIME '10:30:05+01:45';");
			rs.next();
			assertEquals(6, rs.getInt(1));
			// PASS:7565 If the sum of this COUNT and next COUNT = 6?
		}

		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME "
					+ "WHERE TIME '06:15:05' >= TT ");
			rs.next();
			assertEquals(2, rs.getInt(1));
		} else {
			rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME "
					+ "WHERE TIME '06:15:05-02:30' >= TT AT LOCAL;");
			rs.next();
			assertEquals(6, rs.getInt(1));
			// PASS:7565 If the sum of this COUNT and previous COUNT = 6?
		}

		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME "
					+ "WHERE TS  > TIMESTAMP '1980-04-10 10:30:05';");
			rs.next();
			assertEquals(2, rs.getInt(1));
		} else {
			rs = stmt
					.executeQuery("SELECT COUNT (*) "
							+ "FROM TTIME "
							+ "WHERE TS AT LOCAL > TIMESTAMP '1980-04-10 10:30:05+01:45';");
			rs.next();
			assertEquals(6, rs.getInt(1));
			// PASS:7565 If the sum of this COUNT and next COUNT = 6?
		}

		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME "
					+ "WHERE TIMESTAMP '1980-04-10 06:15:05' >= TS ");
			rs.next();
			assertEquals(3, rs.getInt(1));
		} else {
			rs = stmt
					.executeQuery("SELECT COUNT (*) "
							+ "FROM TTIME "
							+ "WHERE TIMESTAMP '1980-04-10 06:15:05-02:30' >= TS AT LOCAL;");
			rs.next();
			assertEquals(6, rs.getInt(1));

			// PASS:7565 If the sum of this COUNT and previous COUNT = 6?
		}
		stmt.executeUpdate("DROP VIEW TTIME");
		stmt.executeUpdate("DROP TABLE TTIME_BASE");
		// END TEST >>> 7565 <<< END TEST

	}

	/*
	 * Name: testYts_809
	 * 
	 * Notes: TEST:7566 TIME ZONE in <datetime value expression>!
	 * 
	 * If Firebird, we just skip this test...
	 *  
	 */
	public void testYts_809() throws SQLException {
		// TEST:7566 TIME ZONE in <datetime value expression>!

		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL"))
			return;

		stmt.executeUpdate("CREATE TABLE TTIME_BASE " + "(PK INTEGER, "
				+ "TT TIME," + "TS TIMESTAMP," + "TT2 TIME WITH TIME ZONE,"
				+ "TS2 TIMESTAMP WITH TIME ZONE," + "PRIMARY KEY (PK));");

		stmt.executeUpdate("CREATE VIEW TTIME3 (PK, TT, TT2, TS2) AS "
				+ "SELECT PK, TT, TT2, TS2 " + "FROM TTIME_BASE;");

		stmt.executeUpdate("INSERT INTO TTIME3 VALUES ( "
				+ "1,TIME '23:05:00',TIME '12:15:00-11:45', "
				+ "TIMESTAMP '1995-07-07 08:15:00+03:05'); ");

		stmt.executeUpdate("INSERT INTO TTIME3 VALUES( "
				+ "2,TIME '05:10:00',TIME '00:07:00+08:39', "
				+ "TIMESTAMP '2000-10-09 03:03:00+04:05');");

		stmt.executeUpdate("INSERT INTO TTIME3 VALUES ( "
				+ "3,TIME '12:11:00',TIME '23:19:00+10:32',  "
				+ "TIMESTAMP '1997-01-16 12:17:00-12:16'); ");

		stmt.executeUpdate("INSERT INTO TTIME3 VALUES ( "
				+ "4,TIME '05:10:00',TIME '00:07:00+08:39', "
				+ "TIMESTAMP '2000-10-09 03:03:00+04:05');");

		stmt.executeUpdate("INSERT INTO TTIME3 VALUES ( "
				+ "5,TIME '17:39:00',TIME '08:28:00-11:45', "
				+ "TIMESTAMP '1994-12-31 20:00:00+04:05'); ");

		stmt.executeUpdate("INSERT INTO TTIME3 VALUES ( "
				+ "6,TIME '17:39:00',TIME '08:28:00-11:45', "
				+ "TIMESTAMP '1994-12-31 20:00:00+04:05');");

		rs = stmt.executeQuery("SELECT COUNT (*) " + "FROM TTIME3 "
				+ "WHERE TT2 AT TIME ZONE " + "((SELECT TT2 FROM TTIME3 "
				+ "WHERE PK = 1) - " + "SELECT TT2 FROM TTIME3 "
				+ "WHERE PK = 3)) " + "HOUR TO MINUTE "
				+ "< TIME '02:00:00+10:00';");
		// PASS:7566 If the sum of this COUNT and the next COUNT = 6?

		rs = stmt.executeQuery("SELECT COUNT (*)  " + "FROM TTIME3 "
				+ "WHERE TIME '16:00:00+00:00' " + "<= TT2 AT TIME ZONE ( "
				+ "((SELECT TT2 FROM TTIME3 " + "WHERE PK = 2) - "
				+ "(SELECT TT2 FROM TTIME3 " + "WHERE PK = 5)) "
				+ "HOUR TO MINUTE - INTERVAL '08:02' HOUR TO MINUTE);");
		// PASS:7566 If the sum of this COUNT and the previous COUNT = 6?

		rs = stmt.executeQuery("SELECT COUNT (*)  " + "FROM TTIME3 "
				+ "WHERE TS2 AT TIME ZONE " + "((SELECT TS2 FROM TTIME3 "
				+ " WHERE PK = 5) - " + "(SELECT TS2 FROM TTIME3 "
				+ "WHERE PK = 6)) " + "HOUR TO MINUTE "
				+ "< TIMESTAMP '1995-02-10 23:48:00+10:06';");
		// PASS:7566 If the sum of this COUNT and next COUNT = 6?

		rs = stmt.executeQuery("SELECT COUNT (*)  " + "FROM TTIME3  "
				+ "WHERE TS2 AT TIME ZONE ( " + "((SELECT TS2 FROM TTIME3 "
				+ "WHERE PK = 1) -  " + "(SELECT TS2 FROM TTIME3 "
				+ "WHERE PK = 6)) " + "HOUR TO MINUTE "
				+ "+ INTERVAL -'4500:15' HOUR TO MINUTE) "
				+ ">= TIMESTAMP '1995-02-10 03:12:00-10:30';");
		// PASS:7566 If the sum of this COUNT and previous COUNT = 6?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TTIME3 "
				+ "WHERE TT2 AT TIME ZONE " + "((SELECT TS2 FROM TTIME3 "
				+ "WHERE PK = 1) - " + "(SELECT TS2 FROM TTIME3 "
				+ "WHERE PK = 6)) " + "HOUR TO MINUTE "
				+ "< TIME '02:00:00+10:00';");
		// PASS:7566 If ERROR - invalid time zone displacement value?
		stmt.executeUpdate("DROP VIEW TTIME3");
		stmt.executeUpdate("DROP TABLE TTIME_BASE");

		// END TEST >>> 7566 <<< END TEST

	}

	/*
	 * Name: testYts_810
	 * 
	 * Notes: FULL OUTER JOIN <tab ref> ON <search cond> dynamic!
	 *  
	 */
	public void testYts_810() throws SQLException {
		// TEST:7567 FULL OUTER JOIN <tab ref> ON <search cond> dynamic!

		stmt.executeUpdate("CREATE TABLE STAFFa " + "( HOURS   INTEGER, "
				+ "SALARY  DECIMAL(6), " + "EMPNUM  CHAR(3), "
				+ "PNUM    DECIMAL(4), " + "EMPNAME CHAR(20));");

		stmt.executeUpdate("CREATE TABLE CL_EMPLOYEE "
				+ "(EMPNUM  NUMERIC(5) NOT NULL PRIMARY KEY, "
				+ "DEPTNO  CHAR(3), " + "LOC     CHAR(15), "
				+ "EMPNAME CHAR(20), " + "SALARY  DECIMAL(6), "
				+ "GRADE   DECIMAL(4), " + "HOURS   DECIMAL(5));");

		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(20,40000,'E1',11,'Alice');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(15,20000,'E2',12,'Betty');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(15,20000,'E2',13,'Betty');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(10,15000,'E3',14,'Colin');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(10,8000,'E3',15,'Colin');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(10,8000,'E3',16,'Colin');");
		stmt.executeUpdate("INSERT INTO STAFFa VALUES "
				+ "(30,50000,'E5',17,'Edward');");
		stmt.executeUpdate("CREATE VIEW TA "
				+ "AS SELECT GRADE, DEPTNO, LOC, HOURS " + "FROM CL_EMPLOYEE;");
		// PASS:7567 If view created successfully?

		stmt.executeUpdate("CREATE VIEW TB "
				+ "AS SELECT EMPNAME, HOURS, EMPNUM, SALARY, PNUM "
				+ "FROM STAFFa;");
		// PASS:7567 If view created successfully?

		stmt.executeUpdate("INSERT INTO CL_EMPLOYEE VALUES ( "
				+ "1, 'abc', 'Susan', NULL, NULL, 1, 100);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO CL_EMPLOYEE VALUES ( "
				+ "2, 'abc', 'Matthew', NULL, NULL, 7, 100);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO CL_EMPLOYEE VALUES ( "
				+ "3, 'abc', 'Peter', NULL, NULL, 2, 100);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO CL_EMPLOYEE VALUES ( "
				+ "4, 'abc', 'Rosemary', NULL, NULL, 8, 100);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TB VALUES "
				+ "('Praze-an-beeble    ',1,'aaa',100,3);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TB VALUES "
				+ "('Chy-an-gwel        ',2,'abc',100,4);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TB VALUES "
				+ "('Ponsonooth         ',3,'abc',100,5);");
		// PASS:7567 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO TB VALUES "
				+ "('Tregwedyn          ',4,'abc',100,6);");
		// PASS:7567 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL")) {
			// for some reason, we can't use the CC alias in the ORDER BY
			// clause. Go figure...
			rs = stmt.executeQuery("SELECT GRADE, COUNT (*) AS CC, EMPNUM "
					+ "FROM TA FULL OUTER JOIN TB ON GRADE > PNUM "
					+ "AND EMPNUM = DEPTNO WHERE "
					+ "GRADE IS NOT NULL GROUP BY GRADE, EMPNUM "
					+ "ORDER BY GRADE;");
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(null, rs.getString(3));
			rs.next();
			assertEquals(2, rs.getInt(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(null, rs.getString(3));
			rs.next();
			assertEquals(7, rs.getInt(1));
			assertEquals(3, rs.getInt(2));
			assertEquals("abc", rs.getString(3).trim());
			rs.next();
			assertEquals(8, rs.getInt(1));
			assertEquals(3, rs.getInt(2));
			assertEquals("abc", rs.getString(3).trim());
		} else {
			rs = stmt.executeQuery("SELECT GRADE, COUNT (*) AS CC, EMPNUM "
					+ "FROM TA FULL OUTER JOIN TB ON GRADE > PNUM "
					+ "AND EMPNUM = DEPTNO WHERE "
					+ "GRADE IS NOT NULL GROUP BY GRADE, EMPNUM "
					+ "ORDER BY CC DESC, GRADE;");
			rs.next();
			assertEquals(7, rs.getInt(1));
			assertEquals(3, rs.getInt(2));
			assertEquals("abc", rs.getString(3).trim());
			rs.next();
			assertEquals(8, rs.getInt(1));
			assertEquals(3, rs.getInt(2));
			assertEquals("abc", rs.getString(3).trim());
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(null, rs.getString(3));
			rs.next();
			assertEquals(2, rs.getInt(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(null, rs.getString(3));
			// PASS:7567 If 4 rows returned in the following order?
			//                 grade grpno empnum
			//                 ===== ===== ======
			// PASS:7567 If 7 3 abc ?
			// PASS:7567 If 8 3 abc ?
			// PASS:7567 If 1 1 NULL ?
			// PASS:7567 If 2 1 NULL ?
		}
		stmt.executeUpdate("DROP VIEW TA ");
		// PASS:7567 If view dropped successfully?

		stmt.executeUpdate("DROP VIEW TB ;");
		// PASS:7567 If view dropped successfully?
		stmt.executeUpdate("DROP table staffa;");

		// END TEST >>> 7567 <<< END TEST
	}

	/*
	 * Name: testYts_811
	 * 
	 * Notes: WHERE <search condition> referencing column!
	 * 
	 * TODO: Firebird doesn't support the GROUP BY in a view... Not much we can
	 * do here when we can't create the specified view...
	 */
	public void testYts_811() throws SQLException {
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");

		// TEST:7568 WHERE <search condition> referencing column!

		// DEC is reserved keyword in some systems (like firebird)
		stmt.executeUpdate("CREATE TABLE DATA_TYPE " + "(NUM NUMERIC, "
				+ "myDEC DECIMAL, " + "ING INTEGER, " + "SMA SMALLINT, "
				+ "FLO FLOAT, " + "REA   REAL, " + "DOU DOUBLE PRECISION);");

		stmt.executeUpdate("INSERT INTO DATA_TYPE (ING, SMA) VALUES "
				+ "(1,1); ");
		// PASS:7568 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO DATA_TYPE (NUM, ING, SMA) "
				+ "VALUES (2,2,3); ");
		// PASS:7568 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO DATA_TYPE (NUM, ING, SMA) "
				+ "VALUES (3,4,5); ");
		// PASS:7568 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO DATA_TYPE (NUM, ING, SMA) "
				+ "VALUES (2,2,3); ");
		// PASS:7568 If 1 row inserted successfully?

		stmt.executeUpdate("INSERT INTO DATA_TYPE (NUM, ING, SMA) "
				+ "VALUES (5,4,3); ");
		// PASS:7568 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT SUM(NUM)  " + "FROM DATA_TYPE "
				+ "WHERE NUM IS NOT NULL;");
		// PASS:7568 If SUM = 12?
		rs.next();
		assertEquals(12, rs.getInt(1));

		if (dialect.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("DROP TABLE DATA_TYPE");
			return;
		}

		stmt.executeUpdate("CREATE VIEW V_DATA_TYPE AS "
				+ "SELECT SUM(NUM) AS VT1, ING AS VT2, SMA AS VT3 "
				+ "FROM DATA_TYPE " + "GROUP BY ING, SMA; ");
		// PASS:7568 If view created successfully?
		rs = stmt.executeQuery("SELECT VT1, VT2, VT3 " + "FROM V_DATA_TYPE "
				+ "WHERE NOT VT1 = 0 " + "ORDER BY VT2, VT3;");
		// PASS:7568 If WARNING - null value eliminated in set function?
		// PASS:7568 If 3 rows are returned in the following order?
		//                VT1 VT2 VT3
		//                === === ===
		// PASS:7568 If 4 2 3 ?
		// PASS:7568 If 5 4 3 ?
		// PASS:7568 If 3 4 5 ?

		rs = stmt.executeQuery("SELECT VT1, VT2, VT3 "
				+ "FROM V_DATA_TYPE WHERE VT2 = 1;");
		// PASS:7568 If WARNING - null value eliminated in set function?
		// PASS:7568 If NULL, 1, 1?

		rs = stmt.executeQuery("SELECT SUM(VT1) AS SUNTA1  "
				+ "FROM V_DATA_TYPE AS TB3;");
		// PASS:7568 If WARNING - null value eliminated in set function?
		// PASS:7568 If SUM = 12?

		stmt.executeUpdate("DROP VIEW V_DATA_TYPE;");
		stmt.executeUpdate("DROP TABLE DATA_TYPE");

		// END TEST >>> 7568 <<< END TEST
	}

	/*
	 * Name: testYts_812
	 * 
	 * Notes: <null predicate> with concatenation in <row value constructor>!
	 *  
	 */
	public void testYts_812() throws SQLException {

		// TEST:7569 <null predicate> with concatenation in <row value
		// constructor>!
		stmt.executeUpdate("CREATE TABLE TX (TX1 INTEGER, "
				+ "TX2 CHARACTER(5), TX3 CHARACTER VARYING (10)); ");

		stmt.executeUpdate("INSERT INTO TX (TX1, TX3) VALUES (1, 'Susan');");
		stmt.executeUpdate("INSERT INTO TX (TX1, TX2) VALUES (2, 'lemon');");
		stmt.executeUpdate("INSERT INTO TX VALUES (3, 'apple', '');");
		stmt.executeUpdate("INSERT INTO TX VALUES (4, 'melon', 'Peter');");
		stmt.executeUpdate("INSERT INTO TX VALUES (5, 'peach', 'Matthew');");

		rs = stmt
				.executeQuery("SELECT COUNT (*) FROM TX  WHERE TX2 || TX3 IS NOT NULL; ");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7569 If COUNT = 3?

		rs = stmt.executeQuery("SELECT TX1 FROM TX WHERE TX3 || TX2 IS NULL; ");
		rs.next();
		assertEquals(1, rs.getInt(1));
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7569 If 2 rows returned in any order?
		// PASS:7569 If TX1 = 1?
		// PASS:7569 If TX1 = 2?
	}
}

