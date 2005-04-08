/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 17, 2004
 * 
 * Tests in this suite test the "FIPS Flagger" functionality. If the 
 * database has this feature, the user can set a flag, and those SQL 
 * extensions that are not SQL-92 (or SQL-99) compatible will produce 
 * a warning. Neither SAS nor Firebird offers this feature. With SAS 
 * and Firebird the query either works or it doesn't.  
 * 
 * Firebird's support for SQL in this module is spotty at best.
 *
 */
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * @author bioliv, Aug 17, 2004
 *  
 */
public class TestFlg extends NistTestBase {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		conn = getConnectionViaDriverManager();
		stmt = conn.createStatement();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		stmt.close();
		conn.close();
		super.tearDown();
	}

	/**
	 * Constructor for TestFlg.
	 * 
	 * @param arg0
	 */
	public TestFlg(String arg0) {
		super(arg0);
	}
	/*
	 * Name: testFlg_005
	 * 
	 * Notes: ABS, SUBSTR, other stuff
	 *  
	 */
	public void testFlg_005() throws SQLException {

		// TEST:0296 FIPS Flagger - vendor provided character function!
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.

		// NOTE:0296 If the vendor does not pass this test, as written,
		// NOTE:0296 the vendor should replace the SUBSTR(...) syntax below
		// NOTE:0296 with a vendor extension which selects exactly 1 row.

		setupBaseTables();
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");
		if (!dialect.equalsIgnoreCase("FirebirdSQL")) {
			// Firebird supports SUBSTR through a user-defined library, but not
			// natively.
			rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
					+ "WHERE SUBSTR(EMPNAME,1,3) = 'Ali';");
			rs.next();
			assertEquals(1, rs.getInt(1));
			// PASS:0296 If count = 1?
		}
		// END TEST >>> 0296 <<< END TEST
		//TEST:0297 FIPS Flagger - vendor provided integer function!
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.

		stmt.executeUpdate("UPDATE STAFF SET GRADE = -GRADE;");

		// NOTE:0297 If the vendor does not pass this test, as written,
		// NOTE:0297 the vendor should replace the ABS(...) syntax below
		// NOTE:0297 with a vendor extension which selects 2 rows.

		if (!dialect.equalsIgnoreCase("FirebirdSQL")) {
			// Firebird supports ABS through a user-defined library, but not
			// natively.
			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF "
					+ "WHERE ABS(GRADE) = 12;");
			// PASS:0297 If count = 2?
			rs.next();
			assertEquals(2, rs.getInt(1));
			// END TEST >>> 0297 <<< END TEST
		}
	}

	/*
	 * Name: testFlg_006
	 * 
	 * Notes: Support for identifiers with length > 18
	 *  
	 */

	public void testFlg_006() throws SQLException {

		// TEST:0299 FIPS Flagger - identifier length > 18!
		// NOTE: OPTIONAL FIPS Flagger test
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.

		// NOTE:0299 Delete any SQL statement which causes
		// NOTE:0299 this procedure to abort. But, there
		// NOTE:0299 is no need to remove a statement with a warning.
		stmt.executeUpdate("CREATE TABLE TABLEFGHIJKLMNOPQ19 (COL2 INTEGER);");

		stmt.executeUpdate("CREATE TABLE SHORTTABLE "
				+ "(COLUMN123456789IS19  INTEGER);");
		stmt.executeUpdate("CREATE TABLE BASETABLE1 " + "(COL1  INTEGER);");
		stmt.executeUpdate("CREATE VIEW VIEWABCDEFGHIKLMN19 "
				+ "(COL3) AS SELECT COL1 FROM BASETABLE1;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABLEFGHIJKLMNOPQ19 "
				+ "VALUES (299);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO SHORTTABLE "
				+ "VALUES (299);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO BASETABLE1 "
				+ "VALUES (299);"));

		rs = stmt.executeQuery("SELECT COL2 FROM TABLEFGHIJKLMNOPQ19;");
		rs.next();
		assertEquals(299, rs.getInt(1));
		rs = stmt.executeQuery("SELECT COLUMN123456789IS19 FROM SHORTTABLE;");
		rs.next();
		assertEquals(299, rs.getInt(1));
		rs = stmt.executeQuery("SELECT COL3 FROM VIEWABCDEFGHIKLMN19;");
		rs.next();
		assertEquals(299, rs.getInt(1));
		// PASS:0299 If the value 299 is selected by any of SQL SELECTs above?

		// END TEST >>> 0299 <<< END TEST

	}

	/*
	 * Name: testFlg_008
	 * 
	 * Notes: SELECT nonGROUP column in GROUP BY!
	 *  
	 */

	public void testFlg_008() throws SQLException {

		// TEST:0454 SELECT nonGROUP column in GROUP BY!
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.
		setupBaseTables();
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");

		// Unfortunately, Firebird doesn't like this query. :-(
		if (!dialect.equalsIgnoreCase("FirebirdSQL")) {
			rs = stmt
					.executeQuery("SELECT PTYPE, CITY, SUM (BUDGET), COUNT(*) "
							+ "FROM PROJ GROUP BY CITY ORDER BY CITY;");
			int recordCount = 0;
			while (rs.next()) {
			}
			int pass = 0;
			if ((recordCount == 3) || (recordCount == 4) || (recordCount == 5))
				pass = 1;
			assertEquals(1, pass);
		}
		// PASS:0454 If either 3, 4, or 6 rows are selected?
		// NOTE:0454 If 3 rows, then note whether sample CITY is given.
		// NOTE:0454 If 4 or 6 rows, then note whether SUM and COUNT
		// NOTE:0454 are for CITY or for PTYPE within CITY.

		// END TEST >>> 0454 <<< END TEST
	}

	/*
	 * Name: testFlg_009
	 * 
	 * Notes: Relaxed union compatability rules for columns!
	 * 
	 * Firebird doesn't have a UNION operator...
	 *  
	 */

	public void testFlg_009() throws SQLException {

		// TEST:0455 Relaxed union compatability rules for columns!
		// NOTE: OPTIONAL FIPS Flagger test
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.

		// Sad to say, we can't do squat if we're Firebird here.
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");
		if (dialect.equalsIgnoreCase("FirebirdSQL"))
			return;

		setupBaseTables();
		rs = stmt.executeQuery("SELECT EMPNUM, CITY FROM STAFF "
				+ " UNION SELECT PTYPE, CITY FROM PROJ;");
		int recordCount = 0;
		while (rs.next())
			recordCount++;
		assertEquals(9, recordCount);
		// PASS:0455 If 9 rows are selected?
		// NOTE:0455 Shows support for UNION of CHAR columns
		// NOTE:0455 with different lengths.

		rs = stmt.executeQuery("SELECT EMPNUM, CITY FROM STAFF "
				+ "UNION SELECT 'e1', CITY FROM PROJ;");
		recordCount = 0;
		while (rs.next())
			recordCount++;
		assertEquals(8, recordCount);

		// PASS:0455 If 8 rows selected?
		// NOTE:0455 Shows support for UNION of Char column
		// NOTE:0455 with shorter CHAR literal.

		rs = stmt.executeQuery("SELECT EMPNUM, GRADE FROM STAFF "
				+ "UNION SELECT EMPNUM, HOURS FROM WORKS;");
		recordCount = 0;
		while (rs.next())
			recordCount++;
		assertEquals(14, recordCount);

		// PASS:0455 If 14 rows selected?
		// NOTE:0455 Shows support for UNION of DECIMAL columns
		// NOTE:0455 with different precision.

		// END TEST >>> 0455 <<< END TEST
	}

	/*
	 * Name: testFlg_011
	 * 
	 * Notes: FIPS Flagger - ADD (column, ...)!
	 *  
	 */

	public void testFlg_011() throws SQLException {

		// TEST:0831 FIPS Flagger - ADD (column, ...)!
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.

		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");

		// Firebird won't let us add 2 columns, but we can add 1 at a time
		String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");
		if (dialect.equalsIgnoreCase("FirebirdSQL")) {
			stmt.executeUpdate("ALTER TABLE USIG ADD COL3 INTEGER");
			stmt.executeUpdate("ALTER TABLE USIG ADD COL4 SMALLINT");
		} else
			stmt.executeUpdate("ALTER TABLE USIG "
					+ "ADD(COL3 INTEGER, COL4 SMALLINT);");
		// PASS:0831 If 2 columns added?
		// NOTE:0831 Shows support for X/Open ADD (column, ...) extension

		stmt.executeUpdate("DROP TABLE USIG");
		// END TEST >>> 0831 <<< END TEST

	}

	/*
	 * Name: testFlg_012
	 * 
	 * Notes: FIPS Flagger - CREATE INDEX!
	 *  
	 */

	public void testFlg_012() throws SQLException {

		// TEST:0832 FIPS Flagger - CREATE INDEX!
		// FIPS Flagger Test. Support for this feature is not required.
		// If supported, this feature must be flagged as an extension to the
		// standard.

		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");

		// Firebird won't let us add 2 columns, but we can add 1 at a time
		// String dialect = System.getProperty("DB_DIALECT", "FirebirdSQL");
		// if (dialect.equalsIgnoreCase("FirebirdSQL")) {

		stmt.executeUpdate("CREATE INDEX II1 ON USIG(C1);");
		// PASS:0832 If index created?
		// NOTE:0832 Shows support for CREATE INDEX extension

		stmt.executeUpdate("CREATE UNIQUE INDEX II2 ON USIG(C_1);");
		// PASS:0832 If index created?
		// NOTE:0832 Shows support for CREATE INDEX extension

		stmt.executeUpdate("DROP TABLE USIG");

		// END TEST >>> 0832 <<< END TEST

	}

	private void setupBaseTables() throws SQLException {
		try {
			stmt.executeUpdate("drop table staff");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop table proj");
		} catch (SQLException dontcare) {
			// do nothing
		}
		stmt.executeUpdate("CREATE TABLE STAFF "
				+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
		stmt.executeUpdate("CREATE TABLE PROJ "
				+ "(PNUM CHAR(3) NOT NULL UNIQUE, " + "PNAME CHAR(20), "
				+ "PTYPE CHAR(6), " + "BUDGET   DECIMAL(9), "
				+ "CITY CHAR(15));");

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
		stmt.executeUpdate("INSERT INTO PROJ "
				+ "VALUES  ('P1','MXSS','Design',10000,'Deale');");
		stmt.executeUpdate("INSERT INTO PROJ "
				+ "VALUES  ('P2','CALM','Code',30000,'Vienna');");
		stmt.executeUpdate("INSERT INTO PROJ "
				+ "VALUES  ('P3','SDP','Test',30000,'Tampa');");
		stmt.executeUpdate("INSERT INTO PROJ "
				+ "VALUES  ('P4','SDP','Design',20000,'Deale');");
		stmt.executeUpdate("INSERT INTO PROJ "
				+ "VALUES  ('P5','IRM','Test',10000,'Vienna');");
		stmt.executeUpdate("INSERT INTO PROJ "
				+ "VALUES  ('P6','PAYR','Design',50000,'Deale');");
	}
}