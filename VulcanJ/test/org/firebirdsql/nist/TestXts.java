/* $Id$ */
/*
 * Author: bioliv Created on: Aug 9, 2004
 * 
 * Description: This file implements the NIST tests described in xts700-799.
 *  
 */
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestXts extends NistTestBase {
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

	public TestXts(String arg0) {
		super(arg0);
	}

	/*
	 * Name: testXts_700
	 * 
	 * Notes: Test of NULLIF operator.
	 *  
	 */
	public void testXts_700_NullIfProducingNull() throws SQLException {

		// TEST:7001 NULLIF producing NULL!

		try {
			stmt.executeUpdate("drop table staffb");
		} catch (SQLException sqle) {
		}

		if (DB_DIALECT.equalsIgnoreCase("tssql"))
			stmt.executeUpdate("CREATE TABLE STAFFb "
					+ "(SALARY double precision, " + "EMPNAME CHAR(20), "
					+ "HOURS double precision, " + "PNUM CHAR(3), "
					+ "CITY CHAR(15), " + "SEX CHAR);");
		else
			stmt.executeUpdate("CREATE TABLE STAFFb " + "(SALARY DECIMAL(6), "
					+ "EMPNAME CHAR(20), " + "HOURS INTEGER, "
					+ "PNUM CHAR(3), " + "CITY CHAR(15), " + "SEX CHAR);");
		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10000,'Kilroy',10000,'P4','Athens','M');"));
		// PASS:7001 If 1 row is inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(15000,'Nickos',20000,'P6','Nickos','M');"));
		// PASS:7001 If 1 row is inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(NULL,'Nickos',NULL,'P5','Rhodes','M');"));
		// PASS:7001 If 1 row is inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10010,'George',NULL,'P7','Georgia','M');"));
		// PASS:7001 If 1 row is inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10005,NULL,30000,'P8',NULL,'M');"));
		// PASS:7001 If 1 row is inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10001,'Gregory',12000,'P9',NULL,'M');"));
		// PASS:7001 If 1 row is inserted successfully?

		rs = stmt.executeQuery("SELECT SALARY, EMPNAME, HOURS, CITY "
				+ "FROM STAFFb " + "WHERE NULLIF(SALARY,HOURS) IS NULL "
				+ "ORDER BY EMPNAME; ");
		rs.next();
		assertEquals(10000, rs.getInt(1));
		assertEquals("Kilroy", rs.getString(2).trim());
		assertEquals(10000, rs.getInt(3));
		assertEquals("Athens", rs.getString(4).trim());
		rs.next();
		// rs.getInt() returns 0 if SQLNULL
		assertEquals(0, rs.getInt(1));
		assertEquals("Nickos", rs.getString(2).trim());
		assertEquals(0, rs.getInt(3));
		assertEquals("Rhodes", rs.getString(4).trim());

		// PASS:7001 If 2 rows are selected with the following order?
		//             SALARY EMPNAME HOURS CITY
		//             ====== ======= ===== ====
		// PASS:7001 10000 Kilroy 10000 Athens?
		// PASS:7001 NULL Nickos NULL Rhodes?

		rs = stmt.executeQuery("SELECT SALARY,PNUM,HOURS,NULLIF(EMPNAME,CITY) "
				+ "FROM STAFFb " + "WHERE EMPNAME = CITY OR EMPNAME IS NULL "
				+ "ORDER BY PNUM;");
		rs.next();
		assertEquals(15000, rs.getInt(1));
		assertEquals("P6", rs.getString(2).trim());
		assertEquals(20000, rs.getInt(3));
		assertTrue(rs.getString(4) == null);
		rs.next();
		assertEquals(10005, rs.getInt(1));
		assertEquals("P8", rs.getString(2).trim());
		assertEquals(30000, rs.getInt(3));
		assertTrue(rs.getString(4) == null);
		// PASS:7001 If 2 rows are selected with the following order?
		//             SALARY PNUM HOURS NULLIF(EMPNAME,CITY)
		//             ====== ==== ===== ====================
		// PASS:7001 15000 P6 20000 NULL?
		// PASS:7001 10005 P8 30000 NULL?

		rs = stmt
				.executeQuery("SELECT SUM(NULLIF(NULLIF(SALARY,10000),20000)) as MySum "
						+ "FROM STAFFb; ");
		rs.next();
		assertEquals(45016, rs.getInt(1));
		// todo: per comments, the result should have been 195016, but I get
		// 45016, and that seems correct. Why the discrepancy?

		// PASS:7001 If SUM = 195016?

		// END TEST >>> 7001 <<< END TEST
		// ******************************************
		// *************************************************////END-OF-MODULE
	}

	/*
	 * Name: testXts_701
	 * 
	 * Notes: Test of String concatenation. Per nist, you should be able to
	 * concat character strings without an operator (ex: 'abc' 'def' =
	 * 'abcdef'). Firebird wants the concat operator || (ex: 'abc' || 'def' =
	 * 'abcdef').
	 *  
	 */

	public void testXts_701_StringConcatenation() throws SQLException {
		int rowCount;
		String query;

		try {
			stmt.execute("DROP TABLE T4");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.execute("DROP TABLE ECCO");
		} catch (SQLException sqle) {
		}

		if (DB_DIALECT.equalsIgnoreCase("tssql"))
			stmt.executeUpdate("CREATE TABLE T4 "
					+ "(STR110 CHAR(110) NOT NULL, "
					+ "NUM6 double precision NOT NULL, " + "COL3 CHAR(10), "
					+ "COL4 CHAR(20), " + "UNIQUE(STR110,NUM6));");
		else
			stmt.executeUpdate("CREATE TABLE T4 "
					+ "(STR110 CHAR(110) NOT NULL, "
					+ "NUM6 NUMERIC(6) NOT NULL, " + "COL3 CHAR(10), "
					+ "COL4 CHAR(20), " + "UNIQUE(STR110,NUM6));");

		stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2));");
		stmt.executeUpdate("INSERT INTO ECCO VALUES ('NL');");

		assertEquals(1, stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES ('This is the first compound character literal.',"
				+ "1,NULL,NULL);"));
		// PASS:7004 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES('Second character literal.'," + "2,NULL,NULL);"));
		// PASS:7004 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO T4 "
				+ "VALUES('Third character literal.',3,NULL,NULL);"));
		// PASS:7004 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  STR110 = 'This is the compound ' || 'character literal.';";
		else
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  STR110 = 'This is the compound ' 'character literal.';";
		rs = stmt.executeQuery(query);
		assertFalse(rs.next());
		// PASS:7004 If 0 rows selected - no data condition?

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT COUNT(*) as myCount FROM T4 "
					+ "WHERE  STR110 <> 'This is the first compound ' || 'character literal.';";
		else
			query = "SELECT COUNT(*) as myCount FROM T4 "
					+ "WHERE  STR110 <> 'This is the first compound ' 'character literal.';";
		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7004 If COUNT = 2?

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  NUM6 = 2 AND STR110 <= 'Second character '--Comments here\n || 'literal.';";
		else
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  NUM6 = 2 AND STR110 <= 'Second character '--Comments here\n 'literal.';";
		rs = stmt.executeQuery(query);
		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}

		assertEquals(1, rowCount);
		// PASS:7004 If NUM6 = 2?
		// note original test was value= 2!!! But the value should only be 1

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  STR110 = 'Third character literal.'--Comments here \n||'second fragment' || 'third fragment.';";
		else
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  STR110 = 'Third character literal.'--Comments here \n'second fragment' 'third fragment.';";
		rs = stmt.executeQuery(query);
		assertFalse(rs.next());
		// PASS:7004 If 0 rows selected - no data condition?

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE STR110 = 'First fragment'\n || 'another fragment'\n--Comments \n ||'Second character literal.'\n--Comments here\n || 'fourth fragment.';";
		else
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE STR110 = 'First fragment'\n'another fragment'\n--Comments \n'Second character literal.'\n--Comments here\n'fourth fragment.';";
		rs = stmt.executeQuery(query);
		assertFalse(rs.next());
		// PASS:7004 If 0 rows selected - no data condition?

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT NUM6 FROM T4 WHERE STR110 <= 'Second ' || 'chara'\n--Comments \n||'cter liter'\n--Comments here\n ||'al.' || '     ';";
		else
			query = "SELECT NUM6 FROM T4 WHERE STR110 <= 'Second ' 'chara'\n--Comments \n'cter liter'\n--Comments here\n'al.' '     ';";

		rs = stmt.executeQuery(query);
		rowCount = 0;
		while (rs.next()) {
			rowCount++;
		}

		assertEquals(1, rowCount);
		// PASS:7004 If NUM6 = 2?
		// again, the script has expected value of 2, but ours is 1. Why?

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			query = "SELECT COUNT(*) as myCount FROM T4 WHERE STR110 < 'An indifferent'--Comments\n||' charac'\n||'ter literal.';";
		else
			query = "SELECT COUNT(*) as myCount FROM T4 WHERE STR110 < 'An indifferent'--Comments\n' charac'\n'ter literal.';";

		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7004 If sum of this COUNT and the next COUNT = 3?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "SELECT COUNT(*) as myCount FROM T4 WHERE STR110 >= 'An indifferent'--Comments\n||' charac'\n||'ter literal.';";
		else
			query = "SELECT COUNT(*) as myCount FROM T4 WHERE STR110 >= 'An indifferent'--Comments\n' charac'\n'ter literal.';";
		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7004 If sum of this COUNT and the previous COUNT = 3?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  STR110 = 'Second '\n||'chara'--Comments\n||'cter liter'--Comments here\n||'al.'\n||'     '\n--Comments\n||'      '; ";
		else
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  STR110 = 'Second '\n'chara'--Comments\n'cter liter'--Comments here\n'al.'\n'     '\n--Comments\n'      '; ";

		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7004 If NUM6 = 2?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  NUM6 = 2 AND STR110 < 'Second '\n||'chara'--Comments\n||'cter literal.';";
		else
			query = "SELECT NUM6 FROM T4 "
					+ "WHERE  NUM6 = 2 AND STR110 < 'Second '\n'chara'--Comments\n'cter literal.';";
		rs = stmt.executeQuery(query);
		assertFalse(rs.next());
		// PASS:7004 If 0 rows selected - no data condition?
		// END TEST >>> 7004 <<< END TEST

		// *********************************************
		// TEST:7005 Compound character literal as inserted value!
		stmt.execute("delete from t4");
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "INSERT INTO T4 "
					+ "VALUES ('This is the first fragment of a compound character literal,'--Comments\n||' and this is the second part.',11,NULL,'Compound '\n--Comments\n\n||'literal.');";
		else
			query = "INSERT INTO T4 "
					+ "VALUES ('This is the first fragment of a compound character literal,'--Comments\n' and this is the second part.',11,NULL,'Compound '\n--Comments\n\n'literal.');";
		assertEquals(1, stmt.executeUpdate(query));
		// PASS:7005 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "INSERT INTO T4 "
					+ "VALUES('This is a comp'\n||'ound character literal,'\n||' in the second table row.',12,NULL,NULL);";
		else
			query = "INSERT INTO T4 "
					+ "VALUES('This is a comp'\n'ound character literal,'\n' in the second table row.',12,NULL,NULL);";
		assertEquals(1, stmt.executeUpdate(query));
		// PASS:7005 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "INSERT INTO T4 "
					+ "VALUES('This is '\n||'a comp'\n||'ound '\n||'char'\n||'acter lit'\n-- Comments\n||'eral, '\n-- Comments\n||'in the th'\n||'ird '\n||'table '\n||'row.',13,NULL,NULL);";
		else
			query = "INSERT INTO T4 "
					+ "VALUES('This is '\n'a comp'\n'ound '\n'char'\n'acter lit'\n-- Comments\n'eral, '\n-- Comments\n'in the th'\n'ird '\n'table '\n'row.',13,NULL,NULL);";
		assertEquals(1, stmt.executeUpdate(query));
		// PASS:7005 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT STR110, COL4 FROM T4 WHERE NUM6 = 11;");
		assertTrue(rs.next());
		// PASS:7005 If STR110 = 'This is the first fragment of a compound
		//                character literal, and this is the second part.'?
		// PASS:7005 If COL4 = 'Compound literal.'?

		rs = stmt.executeQuery("SELECT STR110 FROM T4 WHERE  NUM6 = 12;");
		assertTrue(rs.next());
		// PASS:7005 If STR110 = 'This is a compound character literal, in
		//                        the second table row.'?

		rs = stmt.executeQuery("SELECT STR110 FROM T4 WHERE  NUM6 = 13;");
		assertTrue(rs.next());
		// PASS:7005 If STR110 = 'This is a compound character literal, in
		//                        the third table row.'?

		stmt.execute("DELETE from T4");
		// END TEST >>> 7005 <<< END TEST
		// *********************************************

		// TEST:7006 Compound character literal in a <select list>!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			query = "SELECT 'First fragment of a compound character literal, '\n--Comment1\n--Comment2\n||'and second part.', 'This is the first fragment of a compound character literal,'--...\n||' this is the second,'\n\n||' and this is the third part.' FROM ECCO;";
		else
			query = "SELECT 'First fragment of a compound character literal, '\n--Comment1\n--Comment2\n'and second part.', 'This is the first fragment of a compound character literal,'--...\n' this is the second,'\n\n' and this is the third part.' FROM ECCO;";
		rs = stmt.executeQuery(query);
		rs.next();
		assertEquals(
				"First fragment of a compound character literal, and second part.",
				rs.getString(1));
		assertEquals(
				"This is the first fragment of a compound character literal, this is the second, and this is the third part.",
				rs.getString(2));
		// PASS:7006 If 1st value = 'First fragment of a compound character
		//                           literal, and second part.'?
		// PASS:7006 If 2nd value = 'This is the first fragment of a compound
		//                         character literal, this is the second, and
		//                         this is the third part.'?

	} /*
	   * Name: testXts_702
	   * 
	   * Notes: Test of LIKE operator, with ESCAPE clause, including zero-length
	   * ESCAPE clause.
	   * 
	   * TODO: Firebird wouldn't process the ESCPAE clause as written, this has
	   * been commented out for now. Test is only partially implemented.
	   *  
	   */
	public void testXts_702_LikeOperatorWithEscape() throws SQLException {
		int rowCount;

		// need to run setupStaff() to populate STAFF table.
		BaseTab.setupStaff(stmt);

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM STAFF WHERE  'Alice' LIKE 'Alice';");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7007 If COUNT = 5?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM STAFF WHERE  'Equal_literal' NOT LIKE 'Eq_alS_literal%' ESCAPE 'S';");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7007 If COUNT = 0?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM STAFF  WHERE  USER LIKE 'SYSDBA%';");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7007 If COUNT = 5?

		// END TEST >>> 7007 <<< END TEST

		// TEST:7008 LIKE with general char. value for pattern & escape!

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM STAFF WHERE  EMPNAME LIKE EMPNAME;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7008 If COUNT = 5?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('E6','Theodora_FL',14,'T%S_FL%%%%%%%%%');"));
		// PASS:7008 If 1 row is inserted successfully?

		rs = stmt
				.executeQuery("SELECT COUNT(*) FROM STAFF WHERE EMPNAME LIKE CITY ESCAPE 'S';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7008 If COUNT = 1?

		stmt.execute("DELETE FROM STAFF;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF "
				+ "VALUES('S','Dana%ELFT',14,'D%S%%%%%%%%%%%%');"));
		// PASS:7008 If 1 row inserted successfully?

		//		rs = stmt
		//				.executeQuery("SELECT COUNT(*) FROM STAFF WHERE EMPNAME LIKE CITY
		// ESCAPE EMPNUM;");
		//		rs.next();
		//		assertEquals(1, rs.getInt(1));
		//		// PASS:7008 If COUNT = 1?

		//		rs = stmt
		//				.executeQuery("SELECT COUNT(*) FROM STAFF WHERE 'Del%' LIKE CITY
		// ESCAPE EMPNUM;");
		//		rs.next();
		//		assertEquals(1, rs.getInt(1));
		// PASS:7008 If COUNT = 1?

		// END TEST >>> 7008 <<< END TEST

		// TEST:7009 LIKE with zero-length escape!

		stmt.executeUpdate("DELETE FROM STAFF;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF VALUES"
				+ "('   ','Dana%ELFT',14,'D%0%%%%%%%%%%%%');"));
		// PASS:7009 If 1 row inserted successfully?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			try {
				// No TRIM() operator in Firebird SQL.
				rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
						+ "WHERE EMPNAME LIKE CITY ESCAPE TRIM(EMPNUM);");
				fail("Should have produced invalid escape character");
				// PASS:7009 If ERROR - data exception; invalid escape
				// character?
			} catch (SQLException sqle) {
			}
		}

		// END TEST >>> 7009 <<< END TEST

	}

	/*
	 * Name: testXts_703
	 * 
	 * Notes: Test of UNIQUE predicate, single table, all values distinct!
	 * 
	 * TODO: Not all of the UNIQUE predicates worked under FB. See below.
	 *  
	 */

	public void testXts_703_UniquePredicateSingleTable() throws SQLException {

		BaseTab.setupStaff(stmt);
		try {
			stmt.executeUpdate("DROP TABLE STAFFc");
		} catch (SQLException dontcare) {
		}
		stmt.executeUpdate("CREATE TABLE STAFFc "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15), " + "MGR CHAR(3), "
				+ "UNIQUE (EMPNUM));");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF "
				+ "WHERE SINGULAR(SELECT * FROM STAFF);");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7010 If COUNT = 0?

		// threw error - FB doesn't like the first AS
		//		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF AS X "
		//				+ "WHERE UNIQUE (SELECT * FROM STAFF AS Y "
		//				+ "WHERE X.EMPNUM = Y.EMPNUM " + "AND X.EMPNAME = Y.EMPNAME "
		//				+ "AND X.GRADE = Y.GRADE " + "AND X.CITY = Y.CITY);");
		//		rs.next();
		//		assertEquals(5, rs.getInt(1));
		// PASS:7010 If COUNT = 5?

		// END TEST >>> 7010 <<< END TEST
		// *********************************************

		// TEST:7011 UNIQUE PREDICATE, table subquery with non-null duplicates!

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM   STAFF "
				+ "WHERE SINGULAR(SELECT GRADE FROM STAFF);");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7011 If COUNT = 0?

		rs = stmt.executeQuery("SELECT EMPNUM, GRADE FROM STAFF X "
				+ "WHERE (SINGULAR(SELECT GRADE FROM STAFF Y "
				+ "WHERE X.GRADE = Y.GRADE));");
		rs.next();
		assertEquals("E2", rs.getString(1).trim());
		assertEquals(10, rs.getInt(2));
		// PASS:7011 If EMPNUM = E2?
		// PASS:7011 If GRADE = 10?

		rs = stmt.executeQuery("SELECT COUNT (*) FROM STAFF X "
				+ "WHERE NOT SINGULAR (SELECT GRADE FROM STAFF Y "
				+ "WHERE X.GRADE = Y.GRADE);");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:7011 If COUNT = 4?

		// END TEST >>> 7011 <<< END TEST
		// *********************************************

		// TEST:7012 UNIQUE predicate, duplicates containing null!

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFC "
				+ "(EMPNUM,EMPNAME,GRADE,CITY) "
				+ "VALUES('E9','Terry',13,NULL);"));
		// PASS:7012 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFC "
				+ "(EMPNUM,EMPNAME,GRADE,CITY) "
				+ "VALUES('E8','Nick',13,NULL);"));
		// PASS:7012 If 1 row inserted successfully?

		// TODO: Couldn't recode this to work against Firebird...
		//		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFFC AS X "
		//				+ "WHERE UNIQUE(SELECT CITY, MGR FROM STAFFC AS Y "
		//				+ "WHERE X.GRADE = Y.GRADE);");
		//		rs.next();
		//		assertEquals(9, rs.getInt(1));
		// PASS:7012 If COUNT = 9?

		// TODO: Couldn't recode this to work against Firebird...
		//		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFFC AS X "
		//				+ "WHERE NOT UNIQUE(SELECT GRADE, CITY FROM STAFFC AS Y "
		//				+ "WHERE X.GRADE = Y.GRADE);");
		//		rs.next();
		//		assertEquals(3, rs.getInt(1));

		stmt.executeUpdate("DROP TABLE STAFFc");
		// PASS:7012 If COUNT = 3?

		// END TEST >>> 7012 <<< END TEST
	}

	// testXts_713 : skipped!
	// TEST:7013 Schema definition in an SQL statement-single table!

	// testXts_714 : skipped!
	// TEST:7014 Schema definition named schema, implicit auth-id!

	// testXts_715 : skipped!
	// TEST:7015 Schema definition - explicit name and auth-id!

	// testXts_716 : skipped!
	// TEST:7016 SET SESSION AUTHORIZATION to current auth-id!

	// testXts_717 : skipped!
	// TEST:7017 SET SESSION AUTH. to current auth-id in. transaction!

	// testXts_718 : skipped!
	// TEST:7018 SET SESSION AUTHORIZATION to different value!

	// testXts_719 : skipped!
	// TEST:7019 Access to KEY_COLUMN_USAGE view! (INFORMATION_SCHEMA)

	// testXts_720 : skipped!
	// TEST:7020 Access to VIEW_TABLE USAGE view!

	// testXts_721 : skipped!
	// TEST:7021 Access to VIEW_COLUMN_USAGE view!

	// testXts_722 : skipped!
	// TEST:7022 Access to CONSTRAINT_TABLE_USAGE view!

	// testXts_723 : skipped!
	// TEST:7023 Access to CONSTRAINT_COLUMN_USAGE view!

	/*
	 * Name: testXts_724
	 * 
	 * Notes: Tests of INFORMATION_SCHEMA.COLUMN_DOMAIN_USAGE.
	 * INFORMATION_SCHEMA isn't supported in Firebird, but we do have the RDB$
	 * system tables.
	 * 
	 * TODO: This test could be added back later when support for
	 * INFORMATION_SCHEMA.COLUMN_DOMAIN is added to Firebird. statements had to
	 *  
	 */
	public void testXts_724() throws SQLException {
		int rowCount = 0;

		// TEST:7024 Access to COLUMN_DOMAIN_USAGE view!
		//		rs=stmt.executeQuery("SELECT COUNT(*) "+"FROM
		// INFORMATION_SCHEMA.COLUMN_DOMAIN_USAGE "+"WHERE DOMAIN_SCHEMA =
		// 'CTS4';");
		//		assertEquals(0, rs.getInt(1));
		// PASS:7024 If COUNT = 0?

		// original syntax is below...
		//		stmt.executeUpdate("CREATE DOMAIN TESTDOM AS "
		//				+ "NUMERIC(5) CONSTRAINT CONSD724 CHECK (VALUE > 500); ");
		// modified syntax (had to remove CONSTRAINT keyword, followed by name
		//		stmt
		//				.executeUpdate("CREATE DOMAIN TESTDOM AS NUMERIC(5) CHECK (VALUE >
		// 500);");
		// PASS:7024 If domain created successfully?

		//		stmt
		//				.executeUpdate("CREATE TABLE TAB724a "
		//						+ "(COLNUM1 TESTDOM,COLNUM2 TESTDOM,COLNUM3 TESTDOM,COLNUM4
		// TESTDOM);");
		// PASS:7024 If table created successfully?

		//		   rs=stmt.executeQuery("SELECT DOMAIN_SCHEMA,COLUMN_NAME, TABLE_NAME "+
		//		         "FROM INFORMATION_SCHEMA.COLUMN_DOMAIN_USAGE "+
		//		         "WHERE DOMAIN_NAME = 'TESTDOM' "+
		//		         "ORDER BY COLUMN_NAME;");
		//		   int i =1;
		//		   while (rs.next()) {
		//		   	assertEquals ("CTS4", rs.getString(1));
		//		   	assertEquals("COLUMN"+i, rs.getString(2));
		//		   	assertEquals("TAB724A", rs.getString(3).trim());
		//		   }
		// PASS:7024 If 4 rows are selected in following order?
		//                 c1 c2 c3
		//                 == == ==
		// PASS:7024 If CTS4 COLNUM1 TAB724A?
		// PASS:7024 If CTS4 COLNUM2 TAB724A?
		// PASS:7024 If CTS4 COLNUM3 TAB724A?
		// PASS:7024 If CTS4 COLNUM4 TAB724A?

		// original statement below - no cascade operator in firebird
		// stmt.executeUpdate("DROP DOMAIN TESTDOM CASCADE;");
		//		try {
		//			stmt.executeUpdate("DROP DOMAIN TESTDOM;");
		//			fail("Can't drop a domain that is in use.");
		//		} catch (SQLException sqle) {
		//		}

		// PASS:7024 If domain dropped successfully?

		// original statement below - no cascade operator in firebird
		// stmt.executeUpdate("DROP TABLE TAB724a CASCADE;");
		//		stmt.executeUpdate("DROP TABLE TAB724a ;");
		// PASS:7024 If table dropped successfully?

		// this should work, now that the table that used it is dropped
		//		stmt.executeUpdate("DROP DOMAIN TESTDOM;");
		//		rs = stmt.executeQuery("SELECT COUNT(*) "
		//				+ "FROM INFORMATION_SCHEMA.COLUMN_DOMAIN_USAGE"
		//				+ "WHERE DOMAIN_SCHEMA = 'CTS4';");
		//		rowCount = 0;
		//		while (rs.next())
		//			rowCount++;
		//		assertEquals(0, rowCount);
		// PASS:7024 If COUNT = 0?

		// END TEST >>> 7024 <<< END TEST

	}

	/*
	 * Name: testXts_727
	 * 
	 * TEST:7027 Flagging - Full SQL - <explicit table> in <qry expprssn>!
	 *  
	 */

	public void testXts_727_FlaggingExplicitTableInQuery() throws SQLException {

		try {
			stmt.executeUpdate("DROP TABLE CL_STANDARD");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP TABLE TEST6840A");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("drop table TEST6840B");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP TABLE TEST6840C");
		} catch (SQLException dontcare) {
		}

		if (DB_DIALECT.equalsIgnoreCase("tssql")) {
			stmt.executeUpdate("CREATE TABLE CL_STANDARD "
					+ "( COL_NUM1 double precision, " + "COL_CH1  CHAR(10), "
					+ "COL_NUM2 double precision, " + "COL_CH2  CHAR(10));");
			stmt.executeUpdate("CREATE TABLE TEST6840A "
					+ "(NUM_A double precision, " + "CH_A CHAR(10)); ");
			stmt
					.executeUpdate("CREATE TABLE TEST6840B (NUM_B double precision, "
							+ "CH_B CHAR(10));");

			stmt.executeUpdate("CREATE TABLE TEST6840C "
					+ "(NUM_C1 double precision, " + "CH_C1 CHAR(10), "
					+ "NUM_C2 double precision, CH_C2 CHAR(10));");

		} else {
			stmt.executeUpdate("CREATE TABLE CL_STANDARD "
					+ "( COL_NUM1 NUMERIC(4), " + "COL_CH1  CHAR(10), "
					+ "COL_NUM2 NUMERIC(4), " + "COL_CH2  CHAR(10));");
			stmt.executeUpdate("CREATE TABLE TEST6840A "
					+ "(NUM_A NUMERIC(4), " + "CH_A CHAR(10)); ");
			stmt.executeUpdate("CREATE TABLE TEST6840B (NUM_B NUMERIC(4), "
					+ "CH_B CHAR(10));");

			stmt.executeUpdate("CREATE TABLE TEST6840C "
					+ "(NUM_C1 NUMERIC(4), " + "CH_C1 CHAR(10), "
					+ "NUM_C2 NUMERIC(4), CH_C2 CHAR(10));");

		}

		stmt.executeUpdate("INSERT INTO CL_STANDARD "
				+ "VALUES (1000,'NICKOS',4000,'ATHENS');");

		stmt.executeUpdate("INSERT INTO CL_STANDARD "
				+ "VALUES (1001,'MARIA',4001,'RHODES');");

		stmt.executeUpdate("INSERT INTO CL_STANDARD "
				+ "VALUES (1002,'MAKIS',4002,'HANIA');");

		stmt.executeUpdate("INSERT INTO CL_STANDARD "
				+ "VALUES (1003,'GEORGE',4003,'ARTA');");

		stmt.executeUpdate("INSERT INTO CL_STANDARD "
				+ "VALUES (1004,'MORRIS',4004,'PARGA');");

		// was stmt.executeUpdate("INSERT INTO TEST6840C TABLE CL_STANDARD;");
		stmt.executeUpdate("INSERT INTO TEST6840C select * from CL_STANDARD;");
		// PASS:7027 If 5 rows inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEST6840C;");
		// PASS:7027 If COUNT = 5?
		rs.next();
		assertEquals(5, rs.getInt(1));

		rs = stmt.executeQuery("SELECT NUM_C1,CH_C1,NUM_C2,CH_C2 "
				+ "FROM TEST6840C WHERE NUM_C1 = 1000;");
		rs.next();
		assertEquals(1000, rs.getInt(1));
		assertEquals("NICKOS", rs.getString(2).trim());
		assertEquals(4000, rs.getInt(3));
		assertEquals("ATHENS", rs.getString(4).trim());
		// PASS:7027 If 1000, NICKOS, 4000, ATHENS?

		rs = stmt.executeQuery("SELECT NUM_C1,CH_C1,NUM_C2,CH_C2 "
				+ "FROM TEST6840C WHERE NUM_C1 = 1001;");
		rs.next();
		assertEquals(1001, rs.getInt(1));
		assertEquals("MARIA", rs.getString(2).trim());
		assertEquals(4001, rs.getInt(3));
		assertEquals("RHODES", rs.getString(4).trim());
		// PASS:7027 If values = 1001, MARIA, 4001, RHODES?

		rs = stmt.executeQuery("SELECT NUM_C1,CH_C1,NUM_C2,CH_C2 "
				+ "FROM TEST6840C WHERE NUM_C1 = 1002;");
		rs.next();
		assertEquals(1002, rs.getInt(1));
		assertEquals("MAKIS", rs.getString(2).trim());
		assertEquals(4002, rs.getInt(3));
		assertEquals("HANIA", rs.getString(4).trim());
		// PASS:7027 If values = 1002, MAKIS, 4002, HANIA?

		rs = stmt.executeQuery("SELECT NUM_C1,CH_C1,NUM_C2,CH_C2 "
				+ "FROM TEST6840C WHERE NUM_C1 = 1003; ");
		rs.next();
		assertEquals(1003, rs.getInt(1));
		assertEquals("GEORGE", rs.getString(2).trim());
		assertEquals(4003, rs.getInt(3));
		assertEquals("ARTA", rs.getString(4).trim());
		// PASS:7027 If values = 1003, GEORGE, 4003, ARTA?

		rs = stmt.executeQuery("SELECT NUM_C1,CH_C1,NUM_C2,CH_C2 "
				+ "FROM TEST6840C WHERE NUM_C1 = 1004;");
		rs.next();
		assertEquals(1004, rs.getInt(1));
		assertEquals("MORRIS", rs.getString(2).trim());
		assertEquals(4004, rs.getInt(3));
		assertEquals("PARGA", rs.getString(4).trim());
		// PASS:7027 If values = 1004, MORRIS, 4004, PARGA?

		stmt.executeUpdate("DROP TABLE CL_STANDARD");
		stmt.executeUpdate("DROP TABLE TEST6840A");
		stmt.executeUpdate("drop table TEST6840B");
		stmt.executeUpdate("DROP TABLE TEST6840C");
		// END TEST >>> 7027 <<< END TEST

	}

	public void testXts_728_FlaggingFullSQLWith2ColRow() throws SQLException {
		try {
			stmt.executeUpdate("drop table table728a");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("drop table table728b");
		} catch (SQLException sqle) {
		}

		stmt
				.executeUpdate("CREATE TABLE TABLE728a  (C1 CHAR(10), C2 CHAR(10));");
		stmt
				.executeUpdate("CREATE TABLE TABLE728b (COL_1 CHAR(10), COL_2 CHAR(10));");

		stmt.executeUpdate("INSERT INTO TABLE728a VALUES ('NICKOS','GEORGE');");
		stmt.executeUpdate("INSERT INTO TABLE728a VALUES ('HARRY','TANIA');");
		stmt.executeUpdate("INSERT INTO TABLE728a VALUES ('KILLER',NULL);");
		stmt.executeUpdate("INSERT INTO TABLE728a VALUES (NULL,NULL);");

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt.executeUpdate("INSERT INTO TABLE728b "
					+ "SELECT * FROM TABLE728a "
					+ "WHERE c1 is not null and c2 IS NOT NULL;");
		} else {
			stmt
					.executeUpdate("INSERT INTO TABLE728b "
							+ "SELECT * FROM TABLE728a "
							+ "WHERE (C1,C2) IS NOT NULL;");
		}
		// PASS:7028 If INSERT completed successfully?

		rs = stmt.executeQuery("SELECT COL_1,COL_2 FROM TABLE728b "
				+ "WHERE COL_1 = 'NICKOS' AND COL_2 = 'GEORGE';");
		rs.next();
		assertEquals("NICKOS", rs.getString(1).trim());
		assertEquals("GEORGE", rs.getString(2).trim());
		// PASS:7028 If COL_1 = NICKOS and COL_2 = GEORGE?

		rs = stmt.executeQuery("SELECT COL_1,COL_2 " + "FROM TABLE728b "
				+ "WHERE COL_1 = 'HARRY' AND COL_2 = 'TANIA'; ");
		rs.next();
		assertEquals("HARRY", rs.getString(1).trim());
		assertEquals("TANIA", rs.getString(2).trim());

		// PASS:7028 If COL_1 = HARRY and COL_2 = TANIA?
	}

	// testXts_729: skipped!
	// TEST:7029 Column name with 19, 72 and 128 characters!

	// TEST:7030 Table name with 19 characters - delimited!

	public void testXts_730_ColumnNameWith19CharactersDelimited()
			throws SQLException {

		try {
			stmt.executeUpdate("drop table \"LONGIDENTIFIERSAAAA\"");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("drop table \"longidentifiersaaab\"");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("drop table \"0\"\"LONGIDENTIFIERS_1\"");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("drop table \"0\"\"LONGIDENTIFIERS_2\"");
		} catch (SQLException sqle) {
		}
		try {
			stmt.executeUpdate("drop table \"lngIDENTIFIER% .,()\"");
		} catch (SQLException sqle) {
		}

		stmt
				.executeUpdate("CREATE TABLE \"LONGIDENTIFIERSAAAA\" (TNUM double precision);");
		//  PASS:7030 If table created successfully?

		stmt
				.executeUpdate("CREATE TABLE \"longidentifiersaaab\" (TNUM double precision);");
		// PASS:7030 If table created successfully?

		stmt
				.executeUpdate("CREATE TABLE \"0\"\"LONGIDENTIFIERS_1\" (TNUM double precision);");
		// PASS:7030 If table created successfully?

		stmt
				.executeUpdate("CREATE TABLE \"0\"\"LONGIDENTIFIERS_2\" (TNUM double precision);");
		// PASS:7030 If table created successfully?

		stmt
				.executeUpdate("CREATE TABLE \"lngIDENTIFIER% .,()\" (TNUM double precision);");
		// PASS:7030 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			//was
			//	rs=smt.executeQuery("SELECT COUNT(*) "+
			//    "FROM INFORMATION_SCHEMA.TABLES "+
			//    "WHERE TABLE_SCHEMA = 'CTS1' "+
			//    "AND TABLE_TYPE = 'BASE TABLE' "+
			//    "AND ( TABLE_NAME = 'LONGIDENTIFIERSAAAA' "+
			//       "OR TABLE_NAME = 'longidentifiersaaab' "+
			//       "OR TABLE_NAME = '0"LONGIDENTIFIERS_1' "+
			//       "OR TABLE_NAME = '0"LONGIDENTIFIERS_2' "+
			//       "OR TABLE_NAME = 'lngIDENTIFIER% .,()' );");
			//	rs.next();
			//assertEquals(0, rs.getInt(1));
			// PASS:7030 If COUNT = 5?

			// was
			//	SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES
			//    WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_TYPE = 'BASE TABLE'
			//    AND ( TABLE_NAME = 'LONGIDENTIFIERSAAAA'
			//       OR TABLE_NAME = 'longidentifiersaaab'
			//       OR TABLE_NAME = '0"LONGIDENTIFIERS_1'
			//       OR TABLE_NAME = '0"LONGIDENTIFIERS_2'
			//       OR TABLE_NAME = 'lngIDENTIFIER% .,()' )
			//    ORDER BY TABLE_NAME;
			rs = stmt
					.executeQuery("SELECT RDB$RELATION_NAME FROM RDB$RELATIONS "
							+ "WHERE RDB$RELATION_NAME = 'LONGIDENTIFIERSAAAA' "
							+ "OR RDB$RELATION_NAME = 'longidentifiersaaab' "
							+ "OR RDB$RELATION_NAME = '0\"LONGIDENTIFIERS_1' "
							+ "OR RDB$RELATION_NAME = '0\"LONGIDENTIFIERS_2' "
							+ "OR RDB$RELATION_NAME = 'lngIDENTIFIER% .,()'  "
							+ "ORDER BY RDB$RELATION_NAME; ");
			rs.next();
			assertEquals("0\"LONGIDENTIFIERS_1", rs.getString(1).trim());
			rs.next();
			assertEquals("0\"LONGIDENTIFIERS_2", rs.getString(1).trim());
			rs.next();
			assertEquals("LONGIDENTIFIERSAAAA", rs.getString(1).trim());
			rs.next();
			assertEquals("lngIDENTIFIER% .,()", rs.getString(1).trim());
			rs.next();
			assertEquals("longidentifiersaaab", rs.getString(1).trim());

			//PASS:7030 If 5 rows are selected in following order?
			//                    table_name
			//                    ==========
			// PASS:7030 If 0"LONGIDENTIFIERS_1?
			// PASS:7030 If 0"LONGIDENTIFIERS_2?
			// PASS:7030 If LONGIDENTIFIERSAAAA?
			// PASS:7030 If lngIDENTIFIER% .,()?
			// PASS:7030 If longidentifiersaaab?
		}
		stmt.executeUpdate("DROP TABLE \"LONGIDENTIFIERSAAAA\" ");
		// PASS:7030 If table dropped successfully?

		stmt.executeUpdate("DROP TABLE \"longidentifiersaaab\" ");
		// PASS:7030 If table dropped successfully?

		stmt.executeUpdate("DROP TABLE \"0\"\"LONGIDENTIFIERS_1\" ");
		// PASS:7030 If table dropped successfully?

		stmt.executeUpdate("DROP TABLE \"0\"\"LONGIDENTIFIERS_2\" ");
		// PASS:7030 If table dropped successfully?

		stmt.executeUpdate("DROP TABLE \"lngIDENTIFIER% .,()\" ");
		// PASS:7030 If table dropped successfully?

		// END TEST >>> 7030 <<< END TEST
	}

	/*
	 * Name: testXts_731
	 * 
	 * Notes: firebird only supports view names of 31 characters, not 69 and 128
	 * as specified by this test. Therefore, I've re-written this test to create
	 * views with 31-character names, and kept a couple of tests that had >32
	 * character view names for exception handling.
	 * 
	 * TODO: This test could be modified when support for longer column names is
	 * added. For now it serves to test Firebird's 32-character column name
	 * limit.
	 *  
	 */
	public void testXts_731_ViewsWithLongNames() throws SQLException {

		BaseTab.setupBaseTab(stmt);
		// boring, just long
		String string31A = "A234597890123459789012345678901";
		// double-quote the name
		String string31B = "\"a234597890123459789012345678901\"";
		// containing special characters
		String string31C = "\"longIDENTIFIERSJJJJJJJJWW% .,()\"";

		String string128 = "LONGIDENTIFIERSAAAAABBBBBBBBBBCCCCCCCCCC"
				+ "DDDDDDDDDDEEEEEEEEEEFFFFFFFFFFGGGGGGGGGGHHHHHHHHHH"
				+ "IIIIIIIIIIJJJJJJJJJJKKKKKKKKKKAAAAAAAA";

		try {
			stmt.executeUpdate("DROP VIEW " + string31A);
		} catch (SQLException dontcare) {
		}

		try {
			stmt.executeUpdate("DROP VIEW " + string31B);
		} catch (SQLException dontcare) {
		}

		try {
			stmt.executeUpdate("DROP VIEW " + string31C);
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("CREATE VIEW " + string31A
				+ " AS SELECT * FROM STAFF;");

		stmt.executeUpdate("CREATE VIEW " + string31B
				+ " AS SELECT * FROM WORKS;");

		stmt.executeUpdate("CREATE VIEW " + string31C
				+ " AS SELECT * FROM VTABLE;");

		try {
			stmt.executeUpdate("CREATE VIEW " + string128
					+ " as select * from vtable");
			fail("creating a view with a 128 long name should have failed");
		} catch (SQLException sqle) {
		}

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT  COUNT(*) FROM rdb$view_relations "
					+ " WHERE RDB$RELATION_NAME = 'STAFF' AND rdb$VIEW_name = "
					+ "'" + string31A + "'");
			rs.next();
			assertEquals(1, rs.getInt(1));

			rs = stmt.executeQuery("SELECT  COUNT(*) FROM rdb$view_relations "
					+ " WHERE RDB$RELATION_NAME = 'WORKS' AND rdb$VIEW_name = "
					+ "'a234597890123459789012345678901'");
			rs.next();
			assertEquals(1, rs.getInt(1));

			rs = stmt
					.executeQuery("SELECT  COUNT(*) FROM rdb$view_relations "
							+ " WHERE RDB$RELATION_NAME = 'VTABLE' AND rdb$VIEW_name = "
							+ "'longIDENTIFIERSJJJJJJJJWW% .,()\'");
			rs.next();
			assertEquals(1, rs.getInt(1));
		}

		stmt.executeUpdate("DROP VIEW " + string31A);
		stmt.executeUpdate("DROP VIEW " + string31B);
		stmt.executeUpdate("DROP VIEW " + string31C);

	}

	/*
	 * Name: testXts_732
	 * 
	 * Notes: TEST:7032 NATURAL FULL OUTER JOIN <table ref> -- static!
	 * 
	 * Firebird SQL doesn't have the NATURAL operator, so we'll just use FULL
	 * OUTER JOIN. This makes for different results, unfortunately.
	 *  
	 */

	public void testXts_732_NaturalFullOuterJoin() throws SQLException {
		// TEST:7032 NATURAL FULL OUTER JOIN <table ref> -- static!

		stmt.executeUpdate("CREATE TABLE TEST6740A (TNUM NUMERIC(4), "
				+ "TCHARA CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6740B " + "(TNUM NUMERIC(4), "
				+ "TCHARB CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6740C " + "(TNUMERIC NUMERIC(4), "
				+ "TCHAR CHAR(10));");
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			rs = stmt.executeQuery("SELECT  COUNT(*)  "
					+ "FROM TEST6740A FULL OUTER JOIN TEST6740B ON "
					+ "test6740a.tnum=test6740b.tnum and "
					+ "test6740a.tchara=test6740b.tcharb");
		else
			rs = stmt.executeQuery("SELECT  COUNT(*)  "
					+ "FROM TEST6740A NATURAL FULL OUTER JOIN TEST6740B;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7032 If COUNT = 0?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM TEST6740A FULL JOIN TEST6740B on "
					+ "test6740a.tnum=test6740b.tnum and "
					+ "test6740a.tchara=test6740b.tcharb");
		else
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM TEST6740A NATURAL FULL JOIN TEST6740B;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7032 If COUNT = 0?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TEST6740A VALUES (1,'AA');"));
		// PASS:7032 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			rs = stmt
					.executeQuery("SELECT * FROM TEST6740A FULL JOIN TEST6740B on "
							+ "test6740a.tnum=test6740b.tnum and "
							+ "test6740a.tchara=test6740b.tcharb");
		else
			rs = stmt.executeQuery("SELECT * FROM TEST6740A "
					+ "NATURAL FULL JOIN TEST6740B " + "FOR READ ONLY;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals("AA", rs.getString(2).trim());
		assertEquals(null, rs.getString(3));
		// PASS:7032 If 1 AA NULL?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt
					.executeQuery("SELECT * FROM TEST6740B FULL JOIN TEST6740A  on "
							+ "test6740b.tnum=test6740a.tnum and "
							+ "test6740b.tcharb=test6740a.tchara");
			rs.next();
			assertEquals(null, rs.getString(1));
			assertEquals(null, rs.getString(2));
			assertEquals(1, rs.getInt(3));
		} else {
			rs = stmt.executeQuery("SELECT * FROM TEST6740B "
					+ "NATURAL FULL JOIN TEST6740A  " + "FOR READ ONLY;");
			rs.next();
			assertEquals(1, rs.getInt(1));
			assertEquals(null, rs.getString(2));
			assertEquals("AA", rs.getString(3).trim());
			// PASS:7032 If 1 NULL AA?
		}
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TEST6740B VALUES (1,'WW');"));
		// PASS:7032 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TEST6740B VALUES (3,'ZZ');"));
		// PASS:7032 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT  COUNT(*)  "
					+ "FROM TEST6740A FULL OUTER JOIN TEST6740B ON "
					+ "test6740a.tnum=test6740b.tnum and "
					+ "test6740a.tchara=test6740b.tcharb");
			rs.next();
			assertEquals(3, rs.getInt(1));
		} else {
			rs = stmt.executeQuery("SELECT  COUNT(*)  "
					+ "FROM TEST6740A NATURAL FULL OUTER JOIN TEST6740B;");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:7032 If COUNT = 2?
		}

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM TEST6740B FULL JOIN TEST6740A on "
					+ "test6740b.tnum=test6740a.tnum and "
					+ "test6740b.tcharb=test6740a.tchara");
			rs.next();
			assertEquals(3, rs.getInt(1));
		} else {
			rs = stmt.executeQuery("SELECT COUNT(*) "
					+ "FROM TEST6740B NATURAL FULL JOIN TEST6740A;");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:7032 If COUNT = 2?
		}

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TEST6740C VALUES(6,'PP');"));
		// PASS:7032 If 1 row inserted successfully?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TEST6740C VALUES(7,'QQ');"));
		// PASS:7032 If 1 row inserted successfully?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			stmt
					.executeUpdate("CREATE VIEW TESTV6740 (VNUM1, VCHAR1, VNUM2, VCHAR2) "
							+ " AS TEST6740C NATURAL FULL OUTER JOIN TEST6740A;");
			// PASS:7032 If view created successfully?
			rs = stmt.executeQuery("SELECT * FROM TESTV6740 ORDER BY VNUM1;");
			rs.next();
			assertEquals(6, rs.getInt(1));
			assertEquals("PP", rs.getString(2).trim());
			assertEquals(1, rs.getInt(3));
			assertEquals("AA", rs.getString(4).trim());
			rs.next();
			assertEquals(7, rs.getInt(1));
			assertEquals("QQ", rs.getString(2).trim());
			assertEquals(1, rs.getInt(3));
			assertEquals("AA", rs.getString(4).trim());
			// PASS:7032 If 2 rows selected in following order?
			//                c1 c2 c3 c4
			//                == == == ==
			// PASS:7032 If 6 PP 1 AA?
			// PASS:7032 If 7 QQ 1 AA?
		}

		// END TEST >>> 7032 <<< END TEST

	}

	/*
	 * Name: testXts_734
	 * 
	 * Notes: Test for unicode (NCHAR) processing
	 *  
	 */

	public void testXts_734_UnicodeProcessing() throws SQLException {

		// oddly, this test only works on Vulcan.
		// exit if Firebird 1.5 or Firebird 2.0
		if ((conn.getMetaData().getDatabaseProductVersion().indexOf(
				"Firebird 2.0") != -1)
				|| (conn.getMetaData().getDatabaseProductVersion().indexOf(
						"Firebird 1.5") != -1))
			return;

		try {
			stmt.executeUpdate("DROP TABLE TAB734");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("CREATE TABLE TAB734 "
				+ "( CSTR1 NCHAR(10), CSTR2 NCHAR VARYING(12));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB734 "
				+ "VALUES ('   !','*  *');"));

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TAB734 VALUES (' * ','+ +');"));

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TAB734 VALUES ('+ +','+ +');"));

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TAB734 VALUES (NULL,' + ');"));

		// TEST:7034 National Character data type in comparison predicate!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TAB734 "
				+ "WHERE CSTR1 = CSTR2;");
		// PASS:7034 If COUNT = 1?
		rs.next();
		assertEquals(1, rs.getInt(1));

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TAB734 "
				+ "WHERE CSTR1 <>  _UNICODE_FSS '   !';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7034 If COUNT = 2?

		rs = stmt
				.executeQuery("SELECT COUNT(CSTR2) FROM TAB734 WHERE CSTR2 =  _UNICODE_FSS '*  *';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7034 If COUNT = 1?

		rs = stmt.executeQuery("SELECT COUNT(*)FROM TAB734 "
				+ "WHERE NOT CSTR1 <>  _UNICODE_FSS '   !';");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:7034 If COUNT = 1?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(CSTR1) FROM TAB734 "
					+ "WHERE CSTR1 <> N' * ';");
			rs.next();
			assertEquals(2, rs.getInt(1));
			// PASS:7034 If COUNT = 2?
		}
		rs = stmt.executeQuery("SELECT COUNT(*) FROM TAB734 "
				+ "WHERE NOT  _UNICODE_FSS '*  *' = CSTR2;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7034 If COUNT = 3?

		if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
			rs = stmt.executeQuery("SELECT COUNT(CSTR2) FROM TAB734 "
					+ "WHERE N'++++' <> CSTR2 ");
			rs.next();
			assertEquals(4, rs.getInt(1));
			// PASS:7034 If COUNT = 4?
		}
	} /*
	   * Name: testXts_735
	   * 
	   * Notes: INSERT National character literal in NCHAR column!
	   *  
	   */

	public void testXts_735_InsertNcharLiteral() throws SQLException {

		if (DB_DIALECT.equalsIgnoreCase("firebirdSQL"))
			return;

		// TEST:7035 INSERT National character literal in NCHAR column!

		stmt.executeUpdate("CREATE TABLE TAB735 " + "(C1 NUMERIC(5) UNIQUE, "
				+ "C2 NCHAR(12));");
		// PASS:7035 If table created successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB735 "
				+ "VALUES(1,NULL);"));
		// PASS:7035 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB735 "
				+ "VALUES(2, N'!');"));
		// PASS:7035 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB735 "
				+ "VALUES(3, N'  !');"));
		// PASS:7035 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT C1,C2 " + "FROM TAB735 "
				+ "ORDER BY C1;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals(null, rs.getString(2));
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals("!", rs.getString(2));
		rs.next();
		assertEquals(3, rs.getInt(1));
		assertEquals("!", rs.getString(2));
		// PASS:7035 If 3 rows are selected with the following order?
		//                 c1 c2
		//                ---- ----
		// PASS:7035 If 1 NULL?
		// PASS:7035 If 2 ! ?
		// PASS:7035 If 3 ! ?

		stmt.executeUpdate("DROP TABLE TAB735");
		// PASS:7035 If table dropped successfully?

		// END TEST >>> 7035 <<< END TEST
	}

	/*
	 * Name: testXts_736
	 * 
	 * Notes: Update NCHAR Varying column with value from NCHAR domain!
	 * 
	 * Original test used the _VANGELIS domain, which is a user-defined
	 * character set. It should be ok to use a system-provided character set, so
	 * we're using UNICODE_FSS for all dialects.
	 * 
	 * Firebird won't let you specify DEFAULT explicitly, either.
	 *  
	 */

	public void testXts_736_NcharVaryingColumnWithNcharDomain()
			throws SQLException {
		// TEST:7036 Update NCHAR Varying column with value from NCHAR domain!

		// oddly, this test only works on Vulcan.
		// exit if Firebird 1.5 or Firebird 2.0
		if ((conn.getMetaData().getDatabaseProductVersion().indexOf(
				"Firebird 2.0") != -1)
				|| (conn.getMetaData().getDatabaseProductVersion().indexOf(
						"Firebird 1.5") != -1))
			return;


		stmt
				.executeUpdate("CREATE DOMAIN DOM1 AS NATIONAL CHARACTER VARYING(10) "
						+ "DEFAULT _UNICODE_FSS 'KILLER';");
		// PASS:7036 If domain created successfully?

		stmt.executeUpdate("CREATE DOMAIN DOM2 AS NATIONAL CHAR VARYING(12) "
				+ "DEFAULT _UNICODE_FSS 'HELLAS';");
		// PASS:7036 If domain created successfully?

		stmt
				.executeUpdate("CREATE DOMAIN DOM3 AS NCHAR VARYING(16) "
						+ "CHECK (VALUE IN (_UNICODE_FSS 'NEW YORK', _UNICODE_FSS 'ATHENS', "
						+ "_UNICODE_FSS 'ZANTE'));");
		// PASS:7036 If domain created successfully?

		stmt.executeUpdate("CREATE TABLE TAB736 "
				+ "(COLK DECIMAL(4) not null PRIMARY KEY, " + "COL1 DOM1, "
				+ "COL2 DOM2, " + "COL3 DOM3);");
		// PASS:7036 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals(1, stmt
					.executeUpdate("INSERT INTO TAB736 (colk, col3)"
							+ "VALUES(1,'ATHENS');"));
		else
			assertEquals(1, stmt.executeUpdate("INSERT INTO TAB736 "
					+ "VALUES(1,DEFAULT,DEFAULT,N'ATHENS');"));
		// PASS:7036 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals(1, stmt
					.executeUpdate("INSERT INTO TAB736 (colk, col3)"
							+ "VALUES(2,'ZANTE');"));
		else
			assertEquals(1, stmt.executeUpdate("INSERT INTO TAB736 "
					+ "VALUES(2,DEFAULT,DEFAULT,N'ZANTE');"));
		// PASS:7036 If 1 row inserted successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			assertEquals(1, stmt.executeUpdate("UPDATE TAB736 "
					+ "SET COL1 = 'KILLER', " + "COL2 = 'HELLAS', "
					+ "COL3 = 'NEW YORK' " + "WHERE COLK = 2;"));
		else
			assertEquals(1, stmt.executeUpdate("UPDATE TAB736 "
					+ "SET COL1 = DEFAULT, " + "COL2 = DEFAULT, "
					+ "COL3 = N'NEW YORK' " + "WHERE COLK = 2;"));
		// PASS:7036 If 1 row updated successfully?

		rs = stmt.executeQuery("SELECT COLK,COL1,COL2,COL3 " + "FROM TAB736 "
				+ "ORDER BY COLK DESC;");
		// PASS:7036 If 2 rows selected in the following order?
		//                 colk col1 col2 col3
		//                 ==== ==== ==== ====
		// PASS:7036 If 2 KILLER HELLAS NEW YORK?
		// PASS:7036 If 1 KILLER HELLAS ATHENS ?
		rs.next();
		assertEquals(2, rs.getInt(1));
		assertEquals("KILLER", rs.getString(2).trim());
		assertEquals("HELLAS", rs.getString(3).trim());
		assertEquals("NEW YORK", rs.getString(4).trim());
		rs.next();
		assertEquals(1, rs.getInt(1));
		assertEquals("KILLER", rs.getString(2).trim());
		assertEquals("HELLAS", rs.getString(3).trim());
		assertEquals("ATHENS", rs.getString(4).trim());

		stmt.executeUpdate("DROP TABLE TAB736");
		// PASS:7036 If table dropped successfully?
		stmt.executeUpdate("DROP DOMAIN DOM1 ");
		// PASS:7036 If domain dropped successfully?
		stmt.executeUpdate("DROP DOMAIN DOM2 ");
		// PASS:7036 If domain dropped successfully?

		stmt.executeUpdate("DROP DOMAIN DOM3 ;");
		// PASS:7036 If domain dropped successfully?

		// END TEST >>> 7036 <<< END TEST
	}

	/*
	 * Name: testXts_740
	 * 
	 * Notes: COUNT(ALL <column name>) with Nulls in column!
	 */
	public void testXts_740_CountAllColumnWithNulls() throws SQLException {

		try {
			stmt.executeUpdate("drop table empty740");
		} catch (SQLException sqle) {
		}
		// TEST:7040 COUNT(ALL <column name>) with Nulls in column!
		stmt.executeUpdate("CREATE TABLE EMPTY740 " + "(COL_1   CHAR(10), "
				+ "COL_2   VARCHAR(5), " + "COL_3 NUMERIC(5), "
				+ "COL_4   NUMERIC(6), " + "COL_5 TIME);");

		rs = stmt.executeQuery("SELECT COUNT(ALL COL_1) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7040 If COUNT = 0?

		rs = stmt.executeQuery("SELECT COUNT(COL_2) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7040 If COUNT = 0?

		rs = stmt.executeQuery("SELECT COUNT(COL_3) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7040 If COUNT = 0?

		rs = stmt.executeQuery("SELECT COUNT(COL_4) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7040 If COUNT = 0?

		rs = stmt.executeQuery("SELECT COUNT(ALL COL_5) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7040 If COUNT = 0?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740 "
				+ "VALUES('NICKOS','NICK',NULL,116,TIME'09:30:30');"));
		// PASS:7040 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('MARIA',NULL,NULL,NULL,TIME'15:43:52');"));
		// PASS:7040 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('KILLER','BUCK',NULL,127,TIME'15:43:52');"));
		// PASS:7040 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('JOYCE',NULL,NULL,17,TIME'12:53:13');"));
		// PASS:7040 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('ANGIE','TREE',NULL,7,TIME'16:29:22');"));
		// PASS:7040 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(COL_1) FROM EMPTY740;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7040 If COUNT = 5?
		rs = stmt.executeQuery("SELECT COUNT(ALL COL_2) FROM EMPTY740;");
		// we don't get an error for firebird... probably should...
		//		SQLWarning warning = stmt.getWarnings();
		//		if (warning != null) {
		//			System.out.println("\n---Warning---\n");
		//			while (warning != null) {
		//				System.out.println("Message: " + warning.getMessage());
		//				System.out.println("SQLState: " + warning.getSQLState());
		//				System.out.print("Vendor error code: ");
		//				System.out.println(warning.getErrorCode());
		//				System.out.println("");
		//				warning = warning.getNextWarning();
		//			}
		//		}
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7040 If COUNT = 3 and ?
		// PASS:7040 WARNING - null value eliminated in set function ?

		rs = stmt.executeQuery("SELECT COUNT(ALL COL_3) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7040 If COUNT = 0 and ?
		// PASS:7040 WARNING - null value eliminated in set function ?

		rs = stmt.executeQuery("SELECT COUNT(ALL COL_4) FROM EMPTY740;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:7040 If COUNT = 4 and ?
		// PASS:7040 WARNING - null value eliminated in set function ?

		rs = stmt.executeQuery("SELECT COUNT(ALL COL_5) FROM EMPTY740;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7040 If COUNT = 5?

		stmt.executeUpdate("DROP TABLE EMPTY740;");
		// PASS:7040 If deleted completed successfully?

		// END TEST >>> 7040 <<< END TEST
	}

	/*
	 * Name: testXts_741
	 * 
	 * COUNT(ALL NULLIF...) with generated Nulls!
	 *  
	 */
	public void testXts_741_CountAllNullIfGeneratedNulls() throws SQLException {
		// TEST:7041 COUNT(ALL NULLIF...) with generated Nulls!

		stmt.executeUpdate("CREATE TABLE EMPTY740 " + "(COL_1   CHAR(10), "
				+ "COL_2   VARCHAR(5), " + "COL_3 NUMERIC(5), "
				+ "COL_4   DECIMAL(6), " + "COL_5 TIME);");

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740 "
				+ "VALUES('NICKOS','NICK',NULL,116,{t'18:00:00'});"));
		// PASS:7041 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('MARIA',NULL,NULL,NULL,{t'12:00:00'});"));
		// PASS:7041 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('KILLER','BUCK',NULL,127,{t'09:30:30'});"));
		// PASS:7041 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('JOYCE',NULL,NULL,17,{t'15:43:52'});"));
		// PASS:7041 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('ANGIE','TREE',NULL,7,{t'12:53:13'});"));
		// PASS:7041 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO EMPTY740  "
				+ "VALUES('BUCK','BUCK',NULL,12,{t'16:29:22'});"));
		// PASS:7041 If 1 row inserted successfully?

		rs = stmt
				.executeQuery("SELECT COUNT(ALL NULLIF ('Nickos','Nickos   ')) FROM EMPTY740;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7041 If COUNT = 0 and ?
		// PASS:7041 WARNING - null value eliminated in set function ?

		rs = stmt
				.executeQuery("SELECT COUNT(ALL NULLIF (COL_1,'JANET')) FROM EMPTY740;");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:7041 If COUNT = 6?

		rs = stmt
				.executeQuery("SELECT COUNT(ALL NULLIF ('NICKOS',COL_1)) FROM EMPTY740;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7041 If COUNT = 5 and ?
		// PASS:7041 WARNING - null value eliminated in set function ?

		rs = stmt
				.executeQuery("SELECT COUNT(ALL NULLIF (COL_2,COL_1)) FROM EMPTY740;");
		rs.next();
		assertEquals(3, rs.getInt(1));
		// PASS:7041 If COUNT = 3 and ?
		// PASS:7041 WARNING - null value eliminated in set function ?

		rs = stmt
				.executeQuery("SELECT COUNT(ALL NULLIF (COL_4,COL_3)) FROM EMPTY740;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7041 If COUNT = 5 and ?
		// PASS:7041 WARNING - null value eliminated in set function ?

		rs = stmt
				.executeQuery("SELECT COUNT(ALL NULLIF (COL_5,TIME'12:00:00')) FROM EMPTY740;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:7041 If COUNT = 5 and ?
		// PASS:7041 WARNING - null value eliminated in set function ?

		stmt.executeUpdate("DROP TABLE EMPTY740;");
		// PASS:7041 If delete completed successfully?

		// END TEST >>> 7041 <<< END TEST

	}
	/*
	 * Name: testXts_742
	 * 
	 * TEST:7042 COUNT ALL <literal>!
	 *  
	 */
	public void testXts_742_CountAllLiteral() throws SQLException {
		// TEST:7042 COUNT ALL <literal>!
		stmt.executeUpdate("CREATE TABLE CL_DATA_TYPE " + "(CL_CHAR CHAR(10), "
				+ "CL_NUM NUMERIC, " + "CL_DEC DECIMAL, " + "CL_REAL REAL);");
		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES ('GEORGE',1,10,100);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES ('NICK',2,20,200);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES ('PAUL',3,30,300);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES ('PETER',4,40,400);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES ('KEVIN',5,50,500);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES ('JASON',6,60,600);"));
		rs = stmt.executeQuery("SELECT COUNT(ALL 115.5), "
				+ "COUNT(ALL 'ATHINA'), COUNT(ALL 255), "
				+ "COUNT(*) FROM CL_DATA_TYPE;");
		// PASS:7042 If COUNTs are 6, 6, 6, 6?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES(NULL,55,225,10);"));
		// PASS:7042 If 1 row inserted successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES(NULL,15,140,NULL);"));
		// PASS:7042 If 1 row inserted successfully?

		rs = stmt
				.executeQuery("SELECT COUNT(*),COUNT(ALL 119), COUNT(ALL 'GIORGOS') , "
						+ "COUNT(CL_CHAR), "
						+ "COUNT(CL_REAL) FROM CL_DATA_TYPE;");
		// PASS:7042 If COUNTs are 8, 8, 8, 6, 7?
		// PASS:7042 If WARNING - null value eliminated in set function?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_DATA_TYPE "
				+ "VALUES(NULL,0,0,NULL);"));
		// PASS:7042 If 1 row inserted successfully?

		rs = stmt.executeQuery("SELECT COUNT(*), COUNT(ALL 1000), "
				+ "COUNT(ALL 'STEFOS'), " + "COUNT(CL_CHAR),"
				+ "COUNT(CL_REAL) FROM CL_DATA_TYPE;");
		// PASS:7042 If COUNTs = 9, 9, 9, 6, 7?
		// PASS:7042 If WARNING - null value eliminated in set function?

		// END TEST >>> 7042 <<< END TEST

	}

	// testXts_744 : skipped!
	// TEST:7044 Presence of SQL_CHARACTER in CHARACTER_SETS view!

	// testXts_745 : skipped!
	// TEST:7045 Presence of ASCII_GRAPHIC in CHARACTER_SETS view!

	// testXts_746 : skipped!
	// TEST:7046 Presence of LATIN1 in CHARACTER_SETS view!

	// testXts_747 : skipped!
	// TEST:7047 Presence of ASCII FULL in CHARACTER_SETS view!

	// testXts_748 : skipped!
	// TEST:7048 Named constraint in column definition in schema definition!

	// testXts_749 : skipped!
	// TEST:7049 Named table constraint in table definition!

	// testXts_750 : skipped!
	// TEST:7050 Named domain constraint! (visible in information schema)

	/*
	 * Name: testXts_752
	 * 
	 * Notes: ALTER TABLE ADD TABLE CONSTRAINT!
	 * 
	 * Can't add a NOT NULL constraint to a column once it's been created in
	 * Firebird, so we have to do this up front.
	 * 
	 * References to INFORMATION_SCHEMA are commented out until we have a data
	 * store that will stupport these views.
	 */
	public void testXts_752_AlterTableAddConstraint() throws SQLException {
		// TEST:7052 ALTER TABLE ADD TABLE CONSTRAINT!

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE TAB752a "
					+ "(COL1 NUMERIC(5) not null, "
					+ "COL2 CHAR(15) NOT NULL UNIQUE, " + "COL3 CHAR(15));");
		// PASS:7052 If table created successfully?
		else
			stmt.executeUpdate("CREATE TABLE TAB752a " + "(COL1 NUMERIC(5), "
					+ "COL2 CHAR(15) NOT NULL UNIQUE, " + "COL3 CHAR(15));");

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE TAB752b  "
					+ "(C1 NUMERIC(5) not null PRIMARY KEY, " + "C2 CHAR(15), "
					+ "C3 CHAR(15));");
		else
			stmt.executeUpdate("CREATE TABLE TAB752b  "
					+ "(C1 NUMERIC(5) PRIMARY KEY, " + "C2 CHAR(15), "
					+ "C3 CHAR(15));");
		// PASS:7052 If table created successfully?
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE TAB752a ADD PRIMARY KEY (COL1)");
		else
			stmt.executeUpdate("ALTER TABLE TAB752a COL1  "
					+ "ADD CONSTRAINT TA752a_PRKEY PRIMARY KEY(COL1);");
		// PASS:7052 If table altered successfully?

		//		   SELECT COUNT(*)
		//		         FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
		//		         WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'TA752A'
		//		         AND CONSTRAINT_NAME = 'TA752A_PRKEY' AND COLUMN_NAME = 'COL1';
		//		// PASS:7052 If COUNT = 1?

		stmt.executeUpdate("ALTER TABLE TAB752b "
				+ "ADD CONSTRAINT TA752b_FKEY FOREIGN KEY(C2) "
				+ "REFERENCES TAB752a(COL2);");
		// PASS:7052 If table altered successfully?

		//		   SELECT COUNT(*) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
		//		       WHERE TABLE_SCHEMA = 'CTS1'
		//		       AND TABLE_NAME = 'TAB752B'
		//		       AND CONSTRAINT_NAME = 'TA752B_FKEY'
		//		       AND COLUMN_NAME = 'C2';
		// PASS:7052 If COUNT = 1?

		stmt.executeUpdate("ALTER TABLE TAB752a "
				+ "ADD CONSTRAINT COL3_CHECK CHECK  "
				+ "(COL3 IN ('ATHENS','CORFU','PYLOS'));");
		// PASS:7052 If table altered successfully?

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO TAB752a "
					+ "VALUES(1000,'KILLER','PAROS');"));
			fail("Insert should produce integrity constraint violation");
		} catch (SQLException dontcare) {
		}

		// PASS:7052 If ERROR - integrity constraint violation?

		stmt.executeUpdate("DROP TABLE TAB752b");
		// PASS:7052 If table dropped successfully?
		stmt.executeUpdate("DROP TABLE TAB752a");
		// PASS:7052 If table dropped successfully?

		// END TEST >>> 7052 <<< END TEST
	}

	/*
	 * Name: testXts_753
	 *  
	 */
	public void testXts_753_AlterTableAddColumn() throws SQLException {

		// TEST:7053 ALTER TABLE ADD COLUMN WITH <data type>!

		try {
			stmt.executeUpdate("drop table tab753");
		} catch (SQLException sqle) {
		}
		stmt.executeUpdate("CREATE TABLE TAB753 " + "(COL1 double precision, "
				+ "COL2 CHAR(10), " + "COL3 CHAR(10));");
		// PASS:7053 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE TAB753 ADD  COL4 NUMERIC(7);");
		else
			stmt
					.executeUpdate("ALTER TABLE TAB753 ADD COLUMN COL4 double precision;");
		// PASS:7053 If table altered successfully?

		//	   		SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
		//	         WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'TAB753'
		//	         AND COLUMN_NAME = 'COL4';
		// PASS:7053 If COUNT = 1?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE TAB753 ADD COL5 CHAR(7);");
		else
			stmt.executeUpdate("ALTER TABLE TAB753 ADD COLUMN COL5 CHAR(7);");
		// PASS:7053 If table altered successfully?

		//	   SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
		//	         WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'TAB753'
		//	         AND COLUMN_NAME = 'COL5';
		// PASS:7053 If COUNT = 1?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB753 "
				+ "VALUES(1000,'PHONE','NICKOS',12000,'blue');"));
		// PASS:7053 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB753 "
				+ "VALUES(1001,'HULME','CHEADLE',12001,'velvet');"));
		// PASS:7053 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COL1,COL2,COL3,COL4,COL5 "
				+ "FROM TAB753 " + "ORDER BY COL1;");
		rs.next();
		assertEquals(1000, rs.getInt(1));
		assertEquals("PHONE", rs.getString(2).trim());
		assertEquals("NICKOS", rs.getString(3).trim());
		assertEquals(12000, rs.getInt(4));
		assertEquals("blue", rs.getString(5).trim());
		rs.next();
		assertEquals(1001, rs.getInt(1));
		assertEquals("HULME", rs.getString(2).trim());
		assertEquals("CHEADLE", rs.getString(3).trim());
		assertEquals(12001, rs.getInt(4));
		assertEquals("velvet", rs.getString(5).trim());
		// PASS:7053 If 2 rows are selected in the following order?
		//               col1 col2 col3 col4 col5
		//               ==== ==== ==== ==== ====
		// PASS:7053 If 1000 PHONE NICKOS 12000 blue?
		// PASS:7053 If 1001 HULME CHEADLE 12001 velvet?

		stmt.executeUpdate("DROP TABLE TAB753");
		// PASS:7053 If table dropped successfully?

		// END TEST >>> 7053 <<< END TEST
	}

	/*
	 * Name: testXts_754
	 *  
	 */
	public void testXts_754_AlterTableAddColumnWithDomainAndConstraint()
			throws SQLException {

		// TEST:7054 ALTER TABLE ADD COLUMN WITH domain and constraint!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE DOMAIN DOM6138 AS INTEGER "
					+ "CHECK (VALUE > 1000 and VALUE < 2000);");
		else
			stmt.executeUpdate("CREATE DOMAIN DOM6138 AS INTEGER "
					+ "CHECK (VALUE > 1000) " + "CHECK (VALUE < 2000);");
		// PASS:7054 If domain created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE TAB754 " + "(C1 NUMERIC(5), "
					+ "C2 DECIMAL(4));");
		else
			stmt.executeUpdate("CREATE TABLE TAB754 " + "(C1 NUMERIC(5), "
					+ "C2 DECIMAL(4));");
		// PASS:7054 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE TAB754 ADD  COLDOM DOM6138 "
					+ "CONSTRAINT c3dom_check CHECK (COLDOM <= 1998);");
		// PASS:7054 If table altered successfully?
		else
			stmt.executeUpdate("ALTER TABLE TAB754 ADD COLUMN COLDOM DOM6138 "
					+ "CONSTRAINT c3dom_check CHECK (COLDOM <= 1998);");
		// PASS:7054 If table altered successfully?

		//	   SELECT COUNT(*)
		//	         FROM INFORMATION_SCHEMA.COLUMN_DOMAIN_USAGE
		//	         WHERE DOMAIN_SCHEMA = 'CTS1' AND DOMAIN_NAME = 'DOM6138'
		//	         AND TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'TAB754'
		//	         AND COLUMN_NAME = 'COLDOM';
		// PASS:7054 If COUNT = 1?

		assertEquals(1, stmt
				.executeUpdate("INSERT INTO TAB754 VALUES(1000,766,1990);"));
		// PASS:7054 If 1 row inserted successfully?

		try {
			assertEquals(1, stmt
					.executeUpdate("INSERT INTO TAB754 VALUES(1001,767,1999);"));
			fail("INSERT operation should cause integrity constraint violation");
		} catch (SQLException dontcare) {
		}
		// PASS:7054 If ERROR - integrity constraint violation?

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO TAB754 "
					+ "VALUES(1001,767,0);"));
			fail("INSERT operation should cause integrity constraint violation");
		} catch (SQLException dontcare) {
		}
		// PASS:7054 If ERROR - integrity constraint violation?

		stmt.executeUpdate("DROP TABLE TAB754");
		// PASS:7054 If table dropped successfully?

		stmt.executeUpdate("DROP DOMAIN DOM6138 ");
		// PASS:7054 If domain dropped successfully?

		// END TEST >>> 7054 <<< END TEST
	}

	/*
	 * Name: testXts_755
	 * 
	 * Notes: ALTER TABLE DROP COLUMN RESTRICT!
	 * 
	 * Firebird doesn't support the RESTRICT clause.
	 *  
	 */
	public void testXts_755_AlterTableDropColumnRestrict() throws SQLException {

		// TEST:7055 ALTER TABLE DROP COLUMN RESTRICT!

		stmt.executeUpdate("CREATE TABLE TAB755a "
				+ "(COL1  NUMERIC(7) not null PRIMARY KEY, "
				+ "COL2  CHAR(10), " + "ENAME CHAR(25));");
		// PASS:7055 If table created successfully?

		stmt.executeUpdate("CREATE TABLE TAB755b " + "(COL_1    NUMERIC(7), "
				+ "COL_LEKTIKO1 CHAR(10), "
				+ "FOREIGN KEY(COL_1) REFERENCES TAB755a(COL1));");
		// PASS:7055 If table created successfully?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("ALTER TABLE TAB755b DROP  COL_LEKTIKO1  ");
		else
			stmt
					.executeUpdate("ALTER TABLE TAB755b DROP COLUMN COL_LEKTIKO1 RESTRICT; ");
		// PASS:7055 If table altered successfully?

		//	   SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
		//	         WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'TAB755B'
		//	         AND COLUMN_NAME = 'COL_LEKTIKO1';
		// PASS:7055 If COUNT = 0?

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			try {
				stmt.executeUpdate("ALTER TABLE TAB755a DROP  COL1 ");
				// PASS:7055 If ERROR - syntax error or access rule violation?
				fail("ALTER TABLE should fail with key violation");
			} catch (SQLException dontcare) {
			}

		else
			try {
				stmt.executeUpdate("ALTER TABLE TAB755a DROP "
						+ "COLUMN COL1 RESTRICT;");
				// PASS:7055 If ERROR - syntax error or access rule violation?
				fail("ALTER TABLE should fail with key violation");
			} catch (SQLException dontcare) {
			}

		stmt.executeUpdate("DROP TABLE TAB755b ;");
		// PASS:7055 If table dropped successfully?
		stmt.executeUpdate("DROP TABLE TAB755a ");
		// PASS:7055 If table dropped successfully?

		// END TEST >>> 7055 <<< END TEST
	}

	/*
	 * Name: testXts_756
	 * 
	 * Notes: ALTER TABLE DROP COLUMN CASCADE. FB doesn't support the CASCADE
	 * operator, but at least it supports the DROP COLUMN part. So we've coded
	 * this test without the CASCADE.
	 * 
	 * TODO: This test could be modified when support for the CASCADE operator
	 * is added.
	 * 
	 * TODO : Recoded queries to use the RDB$ system tables instead of
	 * INFORMATION_SCHEMA tables.
	 */
	public void testXts_756_AlterTableDropColumnCascade() throws SQLException {

		try {
			stmt.executeUpdate("DROP VIEW V_756a ;");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP VIEW V_756b ;");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP TABLE TAB756a ;");
		} catch (SQLException dontcare) {
		}
		try {
			stmt.executeUpdate("DROP TABLE TAB756a ;");
		} catch (SQLException dontcare) {
		}

		try {
			stmt.executeUpdate("DROP TABLE TAB756b ;");
		} catch (SQLException dontcare) {
		}

		try {
			stmt.executeUpdate("DROP DOMAIN CHARDOMAIN ;");
		} catch (SQLException dontcare) {
		}

		stmt.executeUpdate("CREATE DOMAIN CHARDOMAIN " + "AS CHAR (15) "
				+ "CHECK (VALUE IN ('ATHENS','CORFU','RHODES')); ");
		// PASS:7056 If domain created successfully?

		// couldn't find query to against RDB$ tables to show DOMAINS
		//		rs = stmt.executeQuery("SELECT COUNT(*) "
		//				+ "FROM INFORMATION_SCHEMA.DOMAINS "
		//				+ "WHERE DOMAIN_SCHEMA = 'CTS1' "
		//				+ "AND DOMAIN_NAME = 'CHARDOMAIN';");
		//		rs.next();
		//		assertEquals(1, rs.getInt(1));
		// PASS:7056 If COUNT = 1?

		stmt.executeUpdate("CREATE TABLE TAB756a ("
				+ "T756a_COL1 NUMERIC(3) NOT NULL PRIMARY KEY, "
				+ "DNAME CHAR(15), " + "LOC CHARDOMAIN UNIQUE);");
		// PASS:7056 If table created successfully?

		stmt.executeUpdate("CREATE TABLE TAB756b ("
				+ "T756b_COL_1 NUMERIC(5) NOT NULL PRIMARY KEY, "
				+ "ENAME CHAR(15) NOT NULL, FOREIGN KEY(ENAME) "
				+ "REFERENCES TAB756a(LOC));");
		// PASS:7056 If table created successfully?

		stmt.executeUpdate("CREATE VIEW V_756a AS "
				+ "SELECT LOC,DNAME FROM TAB756a;");
		// PASS:7056 If view created successfully?

		stmt.executeUpdate("CREATE VIEW V_756b AS "
				+ "SELECT LOC,T756a_COL1 FROM TAB756a;");
		// PASS:7056 If view created successfully?

		// Original query below, but firebird didn't support CASCADE on the
		// ALTER TABLE DROP COLUMN operation
		//		stmt.executeUpdate("ALTER TABLE TAB756a DROP COLUMN LOC CASCADE;");
		try {
			stmt.executeUpdate("ALTER TABLE TAB756a DROP COLUMN LOC ");
			fail("Column drop should fail, as view depends on this col.");
		} catch (SQLException dontcare) {
		}
		// PASS:7056 If table altered successfully?

		// was...
		//		rs = stmt.executeQuery("SELECT COUNT(*) "
		//				+ "FROM INFORMATION_SCHEMA.COLUMNS "
		//				+ "WHERE TABLE_SCHEMA = 'CTS1' "
		//				+ "AND TABLE_NAME = 'TAB756A' " + "AND COLUMN_NAME = 'LOC';");
		rs = stmt.executeQuery("select COUNT(*)  "
				+ "from rdb$relation_fields "
				+ "where rdb$relation_name = 'TAB756A' "
				+ "AND RDB$FIELD_NAME = 'LOC'");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// TODO: This now returns 1 entry, which differs from the test.
		// assertEquals(0, rs.getInt(1));
		// PASS:7056 If COUNT = 0?

		// was...
		//		rs = stmt.executeQuery("SELECT COUNT(*) "
		//				+ "FROM INFORMATION_SCHEMA.VIEWS "
		//				+ "WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'V_756A';");

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM rdb$view_relations "
				+ "WHERE rdb$relation_name = 'V_756A'");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7056 If COUNT = 0?

		// was...
		//		rs = stmt.executeQuery("SELECT COUNT(*) "
		//				+ "FROM INFORMATION_SCHEMA.VIEWS "
		//				+ "WHERE TABLE_SCHEMA = 'CTS1' AND TABLE_NAME = 'V_756B';");
		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM rdb$view_relations "
				+ "WHERE rdb$relation_name = 'V_756B'");

		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:7056 If COUNT = 0?

		stmt.executeUpdate("DROP VIEW V_756A ");
		stmt.executeUpdate("DROP VIEW V_756B ");
		stmt.executeUpdate("DROP TABLE TAB756b ;");
		// PASS:7056 If table dropped successfully?
		stmt.executeUpdate("DROP TABLE TAB756a ;");
		// PASS:7056 If table dropped successfully?
		stmt.executeUpdate("DROP DOMAIN CHARDOMAIN ;");
		// PASS:7056 If domain dropped usccessfully?

	}

	/*
	 * Name: testXts_760
	 * 
	 * Notes: MAX of column derived from <set function specification>!
	 *  
	 */
	public void testXts_760_MaxOfColumnDerivedFromSetFunction()
			throws SQLException {

		// TEST:7060 MAX of column derived from <set function specification>!
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			stmt.executeUpdate("CREATE TABLE TABX760 "
					+ "( DEPTNO  NUMERIC(5) not null unique, "
					+ "EMPNAME CHAR(20) not null unique,  "
					+ "SALARY  DECIMAL(7));");
		else
			stmt.executeUpdate("CREATE TABLE TABX760 "
					+ "( DEPTNO  NUMERIC(5) UNIQUE NOT NULL, "
					+ "EMPNAME CHAR(20) UNIQUE NOT NULL,  "
					+ "SALARY  DECIMAL(7));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABX760 "
				+ "VALUES (10,'SPYROS',25000);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TABX760 "
				+ "VALUES (11,'ALEXIS',18000);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TABX760 "
				+ "VALUES (12,'LAMBIS',9000);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TABX760 "
				+ "VALUES (13,'ELENI',4000);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TABX760 "
				+ "VALUES (14,'MARIOS',47000);"));
		assertEquals(1, stmt.executeUpdate("INSERT INTO TABX760 "
				+ "VALUES (15,'NICKOLAS',78000);"));

		// with firebird we can't (1) include a computed column in the select
		// clause of a view or (2) include a group by expression in the view.
		// So there's not much to do here.

		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;
		stmt.executeUpdate("CREATE VIEW V000V AS "
				+ "SELECT DEPTNO, AVG(SALARY) AS AVSAL "
				+ "FROM TABX760 GROUP BY DEPTNO;");
		// PASS:7060 If view created successfully?

		rs = stmt.executeQuery("SELECT MAX(AVSAL) FROM V000V;");
		rs.next();
		assertEquals(78000, rs.getInt(1));
		// PASS:7060 If MAX(avsal) is 78000?

		stmt.executeUpdate("DROP VIEW V000V CASCADE;");
		// PASS:7060 If view dropped successfully?

		// END TEST >>> 7060 <<< END TEST
	}

	// testXts_761 : skipped
	// TEST:7061 Defined character set in <comparison predicate>!

	// testXts_762 : skipped
	// TEST:7062 Defined character set in <like predicate>!

	// testXts_763 : skipped
	// TEST:7063 Access to CHARACTER_SETS view!

	// testXts_764 : skipped
	// TEST:7064 REVOKE USAGE on character set RESTRICT!

	// testXts_765 : skipped
	// TEST:7065 REVOKE USAGE on character set CASCADE!

	// testXts_766 : skipped
	// TEST:7066 Drop character set no granted privileges!

	// testXts_767 : skipped
	// TEST:7067 DROP character set, outstanding granted privileges!

	// testXts_768 : skipped
	// TEST:7068 Presence of SQL_TEXT in CHARACTER_SETS view!

	/*
	 * Name: testXts_769
	 * 
	 * Notes: Character set specification of LATIN1 in literal. This worked per
	 * NIST.
	 *  
	 */
	public void testXts_769_CharacterSetSpecificationLatin1InLiteral()
			throws SQLException {
		// TEST:7069 <Character set specification> of LATIN1 in <literal>!

		// oddly, this test only works on Vulcan.
		// exit if Firebird 1.5 or Firebird 2.0
		if ((conn.getMetaData().getDatabaseProductVersion().indexOf(
				"Firebird 2.0") != -1)
				|| (conn.getMetaData().getDatabaseProductVersion().indexOf(
						"Firebird 1.5") != -1))
			return;

		try {
			stmt.executeUpdate("DROP TABLE TABLATIN1");
			// PASS:7069 If table dropped successfully?
		} catch (SQLException dontcare) {
		}
		stmt.executeUpdate("CREATE TABLE TABLATIN1 "
				+ "( COL1 CHARACTER(10) CHARACTER SET LATIN1, "
				+ "COL2 CHAR(12)      CHARACTER SET LATIN1, "
				+ "COL3 CHAR(15)   CHARACTER SET LATIN1, "
				+ "COL4 NUMERIC(5));");
		// PASS:7069 If table created successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABLATIN1 VALUES "
				+ "(_LATIN1 'NICKOS', _LATIN1 'VASO', _LATIN1 'BILL',2);"));
		// PASS:7069 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABLATIN1 VALUES "
				+ "(_LATIN1 'HELEN', _LATIN1 'JIM', _LATIN1 'ALLOS',5);"));
		// PASS:7069 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABLATIN1 VALUES "
				+ "(_LATIN1 'LAMIA', _LATIN1 'ISOS', _LATIN1 'ALLOS',3);"));
		// PASS:7069 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABLATIN1 VALUES "
				+ "(_LATIN1 'PAROS', _LATIN1 'MYKONOS', _LATIN1 'ALLOS',4);"));
		// PASS:7069 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TABLATIN1 VALUES "
				+ "(_LATIN1 'HULL', _LATIN1 'MYKONOS', _LATIN1 'OFFERTON',6);"));
		// PASS:7069 If 1 row inserted?

		rs = stmt.executeQuery("SELECT COL1, COL2, COL3, COL4 "
				+ "FROM TABLATIN1 " + "WHERE COL1 = _LATIN1'NICKOS'; ");
		rs.next();
		assertEquals("NICKOS", rs.getString(1).trim());
		assertEquals("VASO", rs.getString(2).trim());
		assertEquals("BILL", rs.getString(3).trim());
		assertEquals(2, rs.getInt(4));
		// PASS:7069 If COL1 = NICKOS?
		// PASS:7069 If COL2 = VASO?
		// PASS:7069 If COL3 = BILL?
		// PASS:7069 If COL4 = 2?

		rs = stmt.executeQuery("SELECT COUNT(COL2) " + "FROM TABLATIN1 "
				+ "WHERE COL2 = _LATIN1'MYKONOS';");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:7069 If COUNT = 2?

		rs = stmt.executeQuery("SELECT COL1, COL2, COL3, COL4 "
				+ "FROM TABLATIN1 WHERE COL3 = _LATIN1'ALLOS' "
				+ "ORDER BY COL4; ");
		rs.next();
		assertEquals("LAMIA", rs.getString(1).trim());
		assertEquals("ISOS", rs.getString(2).trim());
		assertEquals("ALLOS", rs.getString(3).trim());
		assertEquals(3, rs.getInt(4));

		rs.next();
		assertEquals("PAROS", rs.getString(1).trim());
		assertEquals("MYKONOS", rs.getString(2).trim());
		assertEquals("ALLOS", rs.getString(3).trim());
		assertEquals(4, rs.getInt(4));

		rs.next();
		assertEquals("HELEN", rs.getString(1).trim());
		assertEquals("JIM", rs.getString(2).trim());
		assertEquals("ALLOS", rs.getString(3).trim());
		assertEquals(5, rs.getInt(4));
		// PASS:7069 If 3 rows are selected in the following order?
		//                  COL1 COL2 COL3 COL4
		//               ======== ======== ======== ========
		// PASS:7069 If LAMIA ISOS ALLOS 3 ?
		// PASS:7069 If PAROS MYKONOS ALLOS 4 ?
		// PASS:7069 If HELEN JIM ALLOS 5 ?

		stmt.executeUpdate("DROP TABLE TABLATIN1");
		// PASS:7069 If table dropped successfully?

	}

	/*
	 * Name: testXts_770
	 * 
	 * Notes: TEST:7070 <Char set spec> of SQL_CHARACTER in <identifier>!
	 * 
	 * Original test used SQL_CHARACTER character set. Firebird doesn't have
	 * that, so we used ISO8859_1 for FirebirdSQL.
	 *  
	 */
	public void testXts_770_CharacterSetofSqlCharacterInID()
			throws SQLException {
		// TEST:7070 <Char set spec> of SQL_CHARACTER in <identifier>!

		// oddly, this test only works on Vulcan.
		// exit if Firebird 1.5 or Firebird 2.0
		if ((conn.getMetaData().getDatabaseProductVersion().indexOf(
				"Firebird 2.0") != -1)
				|| (conn.getMetaData().getDatabaseProductVersion().indexOf(
						"Firebird 1.5") != -1))
			return;

		String characterSet = null;
		if (DB_DIALECT.equalsIgnoreCase("FirebirdSQL"))
			characterSet = "ISO8859_1";
		else
			characterSet = "SQL_CHARCTER";
		stmt.executeUpdate("CREATE TABLE TAB770 " + "(COLNUM1 NUMERIC(5), "
				+ "COLSTR1 CHAR(10) CHARACTER SET " + characterSet + ")");
		// PASS:7070 If table created successfully?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB770 "
				+ "VALUES(10, _" + characterSet + " 'BARBIE');"));
		// PASS:7070 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB770 "
				+ "VALUES(30, _" + characterSet + " 'KILLER');"));
		// PASS:7070 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO TAB770 "
				+ "VALUES(20, _" + characterSet + " '205 GTi');"));
		// PASS:7070 If 1 row inserted?

		rs = stmt
				.executeQuery("SELECT COLNUM1,COLSTR1 FROM TAB770 ORDER BY COLNUM1;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		assertEquals("BARBIE", rs.getString(2).trim());
		rs.next();
		assertEquals(20, rs.getInt(1));
		assertEquals("205 GTi", rs.getString(2).trim());
		rs.next();
		assertEquals(30, rs.getInt(1));
		assertEquals("KILLER", rs.getString(2).trim());
		// PASS:7070 If 3 rows are selected in the following order?
		//                 COLUMN1 COLSTR1
		//                ========= =========
		// PASS:7070 If 10 BARBIE ?
		// PASS:7070 If 20 205 GTi ?
		// PASS:7070 If 30 KILLER ?

		stmt.executeUpdate("DROP TABLE TAB770");
		// PASS:7070 If table dropped successfully?

		// END TEST >>> 7070 <<< END TEST

	}
	// testXts_771 : skipped!
	// TEST:7071 CHARACTER SET ASCII_GRAPHIC in <data type>!

	/*
	 * Name: testXts_798
	 * 
	 * Notes: TEST:7002 NULLIF producing non-NULL!
	 *  
	 */
	public void testXts_798_NullifProducingNonNull() throws SQLException {

		stmt.executeUpdate("CREATE TABLE STAFFb " + "(SALARY DECIMAL(6), "
				+ "EMPNAME CHAR(20), " + "HOURS INTEGER, " + "PNUM CHAR(3), "
				+ "CITY CHAR(15), " + "SEX CHAR);");

		// TEST:7002 NULLIF producing non-NULL!

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10000,'Kilroy',10000,'P4','Athens','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(15000,'Nickos',20000,'P6','Nickos','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb  "
				+ "VALUES(NULL,'Nickos',NULL,'P5','Rhodes','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb  "
				+ "VALUES(10010,'George',NULL,'P7','Georgia','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb  "
				+ "VALUES(10005,NULL,30000,'P8',NULL,'M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb  "
				+ "VALUES(10001,'Gregory',12000,'P9',NULL,'M');"));
		// PASS:7002 If 1 row inserted?

		rs = stmt.executeQuery("SELECT EMPNAME, NULLIF (SALARY,HOURS) "
				+ "FROM STAFFb "
				+ "WHERE SEX = 'M' AND PNUM NOT IN ('P1','P2','P3','P6','P8') "
				+ "AND (SALARY <> HOURS OR SALARY IS NULL OR HOURS IS NULL) "
				+ "ORDER BY PNUM;");
		rs.next();
		assertEquals("Nickos", rs.getString(1).trim());
		assertEquals(0, rs.getInt(2));
		rs.next();
		assertEquals("George", rs.getString(1).trim());
		assertEquals(10010, rs.getInt(2));
		rs.next();
		assertEquals("Gregory", rs.getString(1).trim());
		assertEquals(10001, rs.getInt(2));
		// PASS:7002 If 3 rows selected in the following order?
		//                EMPNAME SALARY
		//                ======= ======
		// PASS:7002 If Nickos NULL ?
		// PASS:7002 If George 10010 ?
		// PASS:7002 If Gregory 10001 ?

		rs = stmt.executeQuery("SELECT NULLIF (EMPNAME,CITY), SALARY "
				+ "FROM STAFFb "
				+ "WHERE SEX = 'M' AND PNUM NOT IN ('P1','P2','P3','P5','P7') "
				+ "AND (EMPNAME <> CITY OR EMPNAME IS NULL OR CITY IS NULL) "
				+ "ORDER BY PNUM;");
		rs.next();
		assertEquals("Kilroy", rs.getString(1).trim());
		assertEquals(10000, rs.getInt(2));
		rs.next();
		assertEquals(null, rs.getString(1));
		assertEquals(10005, rs.getInt(2));
		rs.next();
		assertEquals("Gregory", rs.getString(1).trim());
		assertEquals(10001, rs.getInt(2));
		assertFalse(
				"There should only be 3 records in this ResulSet. There are more.",
				rs.next());
		// PASS:7002 If 3 row selected in the following order?
		//                EMPNAME SALARY
		//                ======= ======
		// PASS:7002 If Kilroy 10000 ?
		// PASS:7002 If NULL 10005 ?
		// PASS:7002 If Gregory 10001 ?

		stmt.executeUpdate("CREATE TABLE TEMP1426 " + "(SALARY DECIMAL(6), "
				+ "EMPNAME CHAR(20));");
		// PASS:7002 If table created successfully?

		stmt.executeUpdate("DELETE FROM STAFFb");

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10000,'Kilroy',10000,'P4','Athens','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(15000,'Nickos',20000,'P6','Nickos','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(NULL,'Nickos',NULL,'P5','Rhodes','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10010,'George',NULL,'P7','Georgia','M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10005,NULL,30000,'P8',NULL,'M');"));
		// PASS:7002 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFFb "
				+ "VALUES(10001,'Gregory',12000,'P9',NULL,'M');"));
		// PASS:7002 If 1 row inserted?
		assertEquals(3, stmt.executeUpdate("INSERT INTO TEMP1426 "
				+ "SELECT NULLIF (10000,SALARY), EMPNAME FROM STAFFb "
				+ "WHERE  "
				+ "SEX = 'M' AND PNUM NOT IN ('P1','P2','P3','P6','P8','P9');"));
		// PASS:7002 If 3 rows inserted?
		// original test was wrong, said 1 row should be inserted.

		rs = stmt.executeQuery("SELECT * FROM TEMP1426 " + "ORDER BY EMPNAME;");
		rs.next();
		assertEquals("George", rs.getString(2).trim());
		assertEquals(10000, rs.getInt(1));
		rs.next();
		assertEquals("Kilroy", rs.getString(2).trim());
		assertEquals(0, rs.getInt(1));
		rs.next();
		assertEquals("Nickos", rs.getString(2).trim());
		assertEquals(10000, rs.getInt(1));
		assertFalse(rs.next());
		// PASS:7002 If 3 rows selected in the following order?
		//                  EMPNAME SALARY
		//                  ======= ======
		// PASS:7002 If George 10000 ?
		// PASS:7002 If Kilroy NULL ?
		// PASS:7002 If Nickos 10000 ?

		// END TEST >>> 7002 <<< END TEST
	}

	/*
	 * Name: testXts_799
	 * 
	 * Notes: TEST:7003 COALESCE with three <value expression>s!
	 * 
	 * Firebird SQL doesn't support COALESCE, so nothing to do if Firebird.
	 *  
	 */
	public void testXts_799_CoalesceWithThreeValueExpressions()
			throws SQLException {

		//	  TEST:7003 COALESCE with three <value expression>s!

		// Firebird doesn't support COALESCE
		if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
			return;
		stmt.executeUpdate("CREATE TABLE CL_EMPLOYEE "
				+ "(EMPNUM  NUMERIC(5) NOT NULL PRIMARY KEY, "
				+ "DEPTNO  CHAR(3), " + "LOC     CHAR(15), "
				+ "EMPNAME CHAR(20), " + "SALARY  DECIMAL(6), "
				+ "GRADE   DECIMAL(4), " + "HOURS   DECIMAL(5));");

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_EMPLOYEE "
				+ "VALUES(5000,NULL,NULL,NULL,NULL,NULL,NULL);"));
		//	  PASS:7003 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_EMPLOYEE "
				+ "VALUES(6000,NULL,'CRETA','JIM',NULL,4,130);"));
		//	  PASS:7003 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_EMPLOYEE "
				+ "VALUES(7000,'P2',NULL,NULL,NULL,NULL,150);"));
		//	  PASS:7003 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_EMPLOYEE "
				+ "VALUES(8000,'P2','HALKIDA',NULL,30000,6,NULL);"));
		//	  PASS:7003 If 1 row inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO CL_EMPLOYEE "
				+ "VALUES(9000,'P1','SANTORINH','ANDREWS',15000,5,125);"));
		//	  PASS:7003 If 1 row inserted?

		rs = stmt.executeQuery("SELECT EMPNUM, COALESCE(SALARY,GRADE,HOURS), "
				+ "COALESCE(EMPNAME,LOC,DEPTNO)  " + "FROM CL_EMPLOYEE "
				+ "ORDER BY EMPNUM; ");
		rs.next();
		assertEquals(5000, rs.getInt(1));
		assertEquals(0, rs.getInt(2));
		assertEquals(null, rs.getString(3));
		rs.next();
		assertEquals(6000, rs.getInt(1));
		assertEquals(4, rs.getInt(2));
		assertEquals("Jim", rs.getString(3).trim());
		rs.next();
		assertEquals(7000, rs.getInt(1));
		assertEquals(150, rs.getInt(2));
		assertEquals("2", rs.getString(3).trim());
		rs.next();
		assertEquals(8000, rs.getInt(1));
		assertEquals(30000, rs.getInt(2));
		assertEquals("HALDIDA", rs.getString(3).trim());
		rs.next();
		assertEquals(9000, rs.getInt(1));
		assertEquals(15000, rs.getInt(2));
		assertEquals("ANDREWS", rs.getString(3).trim());
		//	 PASS:7003 If 5 rows are selected in the following order?
		//			  
		//	 PASS:7003 If 5000 NULL NULL ?
		//			 PASS:7003 If 6000 4 JIM ?
		//			 PASS:7003 If 7000 150 P2 ?
		//			 PASS:7003 If 8000 30000 HALDIDA ?
		//			 PASS:7003 If 9000 15000 ANDREWS ?

		rs = stmt.executeQuery("SELECT EMPNUM, COALESCE(DEPTNO,LOC,'ATHENS'), "
				+ "COALESCE(SALARY,'50000',GRADE)  " + "FROM CL_EMPLOYEE "
				+ "WHERE EMPNUM = 5000;");
		//	 PASS:7003 If 5000 ATHENS 50000?
		rs.next();
		assertEquals(7000, rs.getInt(1));
		assertEquals("NICKOS", rs.getString(2).trim());
		assertEquals(47000, rs.getInt(3));

		rs = stmt.executeQuery("SELECT EMPNUM, COALESCE('NICKOS',DEPTNO,LOC), "
				+ "COALESCE(SALARY,GRADE,47000)  " + "FROM CL_EMPLOYEE "
				+ "WHERE EMPNUM = 7000;");
		//	 PASS:7003 If 7000 NICKOS 47000?

		rs = stmt
				.executeQuery("SELECT EMPNUM, COALESCE(EMPNAME,'PAGRATI',LOC), "
						+ "COALESCE('12000',SALARY,GRADE) "
						+ "FROM CL_EMPLOYEE " + "WHERE EMPNUM = 8000;");
		rs.next();
		assertEquals(8000, rs.getInt(1));
		assertEquals("PAGRATI", rs.getString(2).trim());
		assertEquals(12000, rs.getInt(3));
		//	 PASS:7003 If 8000 PAGRATI 12000?

		// END TEST >>> 7003 <<< END TEST

	}

}