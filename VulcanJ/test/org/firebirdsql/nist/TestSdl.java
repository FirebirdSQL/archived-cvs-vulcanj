/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 17, 2004
 * 
 * The first few tests in this series test GRANT/REVOKE priveleges. 
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
public class TestSdl extends NistTestBase {
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
	 * Constructor for TestSdl.
	 * 
	 * @param arg0
	 */
	public TestSdl(String arg0) {
		super(arg0);
	}

	/*
	 * Name: testSdl_001
	 *  
	 */

	public void testSdl_001() throws SQLException {
		stmt.executeUpdate("CREATE TABLE WHICH_SCHEMA1 (C1 CHAR (50))");
		stmt.executeUpdate("INSERT INTO WHICH_SCHEMA1 VALUES "
				+ "('Use of SCHEMA1.STD is required to pass this test.');");
		// PASS:0137 If 1 row inserted?

		rs = stmt.executeQuery("SELECT C1 FROM WHICH_SCHEMA1;");
		rs.next();
		assertEquals("Use of SCHEMA1.STD is required to pass this test. ", rs
				.getString(1));
		assertFalse(rs.next());

		// PASS:0137 If 1 row selected ?
		// PASS:0137 If C1 starts with 'Use of SCHEMA1.STD is required' ?
		// PASS:0137 If C1 ends with ' to pass this test.' ?
	}

	// testSdl_002 moved to
	// TestQueriesThatUseGrantRevoke.testGrantAllPrivelegesToPublicSelectInsert()

	// testSdl_003 moved to
	//	 TestQueriesThatUseGrantRevoke.testGrantAllPrivelegesToPublicSelectUpdate()

	// testSdl_004 moved to
	//	 TestQueriesThatUseGrantRevoke.testGrantSelectToPublicNoInsert()

	// testSdl_005 moved to
	//	 TestQueriesThatUseGrantRevoke.testGrantSelectUpdate()

	// testSdl_006 moved to
	//	 TestQueriesThatUseGrantRevoke.testGrantSelectUpdateWithGrant()

	// testSdl_007 moved to
	//	 TestQueriesThatUseGrantRevoke.testGrantSelectUpdateOnView()

	/*
	 * Name: testSdl_008
	 *  
	 */

	public void testSdl_008() throws SQLException {
		setupBaseTables();

		// TEST:0144 Priv. violation: Column not in UPDATE column list!
		rs = stmt.executeQuery("SELECT EMPNUM,EMPNAME,GRADE " + "FROM STAFF3 "
				+ "WHERE EMPNUM = 'E3';");
		rs.next();
		assertEquals("Carmen", rs.getString(2).trim());
		assertEquals(13, rs.getInt(3));
		// PASS:0144 If EMPNAME = 'Carmen' and GRADE = 13 ?

		/*
		 * This query throws a wrench in the execution of the remain queries in
		 * this test case and therefore is commented out. -JiMcK
		 * 
		 * assertEquals(1,stmt.executeUpdate("UPDATE STAFF3 " + "SET EMPNUM =
		 * 'E8',GRADE = 30 " + "WHERE EMPNUM = 'E3';")); PASS:0144 If ERROR,
		 * syntax error/access violation, 0 rows updated?
		 */

		assertEquals(1, stmt.executeUpdate("UPDATE STAFF3 "
				+ "SET EMPNUM='E8',EMPNAME='Yang' " + "WHERE EMPNUM='E3';"));
		// PASS:0144 If 1 row is updated?

		rs = stmt.executeQuery("SELECT EMPNUM,EMPNAME,GRADE " + "FROM STAFF3 "
				+ "WHERE EMPNUM = 'E8';");
		rs.next();
		assertEquals("Yang", rs.getString(2).trim());
		assertEquals(13, rs.getInt(3));
		// PASS:0144 If EMPNAME = 'Yang' and GRADE = 13?

		// END TEST >>> 0144 <<< END TEST

	}

	/*
	 * Name: testSdl_009
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_009() throws SQLException {
		setupBaseTables();

		// TEST:0145 Fully Qualified Column Spec.!

		//System.exit(1) ;
		rs = stmt.executeQuery("SELECT STAFF.EMPNUM,EMPNAME,HOURS,USER "
				+ "FROM STAFF,WORKS "
				+ "WHERE STAFF.EMPNUM='E1' AND PNUM='P3';");
		rs.next();
		assertEquals("Alice", rs.getString(2).trim());
		assertEquals("SYSDBA", rs.getString(4).trim());
		assertEquals(80, rs.getInt(3));
		// Changed the expected value of user from SULLIVAN to SYSDBA - JiMcK
		// PASS:0145 If STAFF.EMPNAME = 'Alice', USER = 'SULLIVAN', HOURS =80?

		// END TEST >>> 0145 <<< END TEST
	}

	/*
	 * Name: testSdl_010
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_010() throws SQLException {
		setupBaseTables();

		// TEST:0146 GRANT SELECT, INSERT, DELETE!

		// setup
		stmt.executeUpdate("INSERT INTO STAFF4 " + "SELECT * FROM   STAFF;");

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF4 ;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0146 If 5 rows are inserted?
		rs = stmt.executeQuery("SELECT EMPNUM,EMPNAME,USER " + "FROM STAFF4 "
				+ "WHERE EMPNUM = 'E3';");
		rs.next();
		assertEquals("Carmen", rs.getString(2).trim());
		assertEquals("SYSDBA", rs.getString(3).trim());
		// Changed the expected value of user from SULLIVAN to SYSDBA - JiMcK
		// PASS:0146 If EMPNAME = 'Carmen' and USER = 'SULLIVAN'?

		assertEquals(1, stmt.executeUpdate("INSERT INTO STAFF4 "
				+ "VALUES('E6','Ling',11,'Xi an');"));
		// PASS:0146 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT EMPNUM,EMPNAME " + "FROM STAFF4 "
				+ "WHERE EMPNUM = 'E6';");
		rs.next();
		assertEquals("Ling", rs.getString(2).trim());
		// PASS:0146 If EMPNAME = 'Ling'?

		assertEquals(6, stmt.executeUpdate("DELETE FROM STAFF4;"));
		// PASS:0146 If 6 rows are deleted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF4 ;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0146 If count = 0?

		// END TEST >>> 0146 <<< END TEST

	}

	/*
	 * Name: testSdl_012
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_012() throws SQLException {
		setupBaseTables();

		// TEST:0148 CREATE Table with NOT NULL!
		try {
			assertEquals(0, stmt
					.executeUpdate("INSERT INTO STAFF1(EMPNAME,GRADE,CITY) "
							+ "VALUES('Carmen',40,'Boston'); "));
			fail("Validation error for column EMPNUM, value ***not null***");
			// PASS:0148 If ERROR, NOT NULL constraint, 0 rows inserted?
			// NOTE:0148 Not Null Column EMPNUM is missing.
		} catch (SQLException sqle) {

		}

		rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF1;");
		rs.next();
		assertEquals(0, rs.getInt(1));
		// PASS:0148 If count = 0?

		// END TEST >>> 0148 <<< END TEST

	}

	/*
	 * Name: testSdl_013
	 * 
	 * Notes:
	 *  
	 */

	public void testSdl_013() throws SQLException {
		setupBaseTables();
		// TEST:0149 CREATE Table with NOT NULL Unique!
		assertEquals(1, stmt
				.executeUpdate("INSERT INTO PROJ1(PNUM,PNAME,BUDGET) "
						+ "VALUES('P10','IRM',10000);"));
		//PASS:0149 If 1 row is inserted ?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ1;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		//PASS:0149 If count = 1 ?
		try {
			assertEquals(0, stmt
					.executeUpdate("INSERT INTO PROJ1(PNUM,PNAME,PTYPE) "
							+ "VALUES('P10','SDP','Test'); "));
			fail("Violation of PRIMARY or UNIQUE KEY constraint");
			// PASS:0149 If ERROR, unique constraint, 0 rows inserted?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM PROJ1;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0149 If count = 1?

		// END TEST >>> 0149 <<< END TEST

	}

	/*
	 * Name: testSdl_014
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_014() throws SQLException {
		setupBaseTables();

		// TEST:0150 CREATE Table with Unique(...), INSERT Values!

		assertEquals(1, stmt.executeUpdate("INSERT INTO WORKS1 "
				+ "VALUES('E1','P2',20);"));
		// PASS:0150 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("INSERT INTO WORKS1 "
				+ "VALUES('E1','P3',40);"));
		// PASS:0150 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*)FROM  WORKS1;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0150 If count = 2?

		try {
			assertEquals(1, stmt.executeUpdate("INSERT INTO WORKS1 "
					+ "VALUES('E1','P2',80);"));
			// PASS:0150 If ERROR, unique constraint, 0 rows inserted?
			// NOTE:0150 Duplicates for (EMPNUM, PNUM) are not allows.
			fail("Violation of PRIMARY or UNIQUE KEY constraint");
		} catch (SQLException sqle) {

		}

		rs = stmt.executeQuery("SELECT COUNT(*)FROM   WORKS1;");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0150 If count = 2?

		// END TEST >>> 0150 <<< END TEST
	}

	/*
	 * Name: testSdl_015
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_015() throws SQLException {
		setupBaseTables();

		// TEST:0151 CREATE VIEW!

		rs = stmt.executeQuery("SELECT COUNT(*)FROM STAFFV1;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0151 If count = 4?

		// END TEST >>> 0151 <<< END TEST
	}
	/*
	 * Name: testSdl_016
	 *  
	 */

	public void testSdl_016() throws SQLException {
		setupBaseTables();

		stmt.executeUpdate("INSERT INTO STAFFV2 "
				+ "VALUES('E6','Ling',15,'Xian');");
		// PASS:0152 If 1 row is inserted?
		// removed check for 1 row inserted, due to Firebird bug. Test
		// case to reproduce bug is in TestQueriesThatFailWithFB15.java

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFFV2;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0152 If count = 5?

		try {
			stmt.executeUpdate("INSERT INTO STAFFV2 "
					+ "VALUES('E7','Gallagher',10,'Rockville');");
			// PASS:0152 If ERROR, view check constraint, 0 rows inserted?
			fail("INSERT operation violates check constraint");
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFFV2;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0152 If count = 5?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		rs.next();
		assertEquals(6, rs.getInt(1));
		// PASS:0152 If count = 6?

		// END TEST >>> 0152 <<< END TEST

	}

	/*
	 * Name: testSdl_017
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_017() throws SQLException {
		setupBaseTables();
		// TEST:0153 CREATE VIEW Joining 3 tables!

		rs = stmt.executeQuery("SELECT COUNT(*),SUM(COST) "
				+ "FROM STAFF_WORKS_DESIGN; ");
		rs.next();
		assertEquals(5, rs.getInt(1));
		assertEquals(3488, rs.getInt(2));
		// PASS:0153 If count = 5 and SUM(COST) = 3488?

		// END TEST >>> 0153 <<< END TEST

	}
	/*
	 * Name: testSdl_018
	 * 
	 * Notes: Here we test a query of the form INSERT INTO SchemaA.TableName
	 * SELECT * FROM SchemaB.TableName. As this suite doesn't support schemas,
	 * we've left this test commented out.
	 *  
	 */
	//	public void testSdl_018() throws SQLException {
	//		setupBaseTables();
	//		// TEST:0154 Schema def. - Same table name from different Schema!
	//		rs = stmt.executeQuery("SELECT COUNT(*) FROM VTABLE;");
	//		rs.next();
	//		assertEquals(4, rs.getInt(1));
	//		// PASS:0154 If count = 4?
	//
	//		rs = stmt.executeQuery("SELECT COUNT(*) FROM VTABLE;");
	//		rs.next();
	//		assertEquals(0, rs.getInt(1));
	//		// PASS:0154 If count = 0? // JIMB BUG
	//		// BUG - This will hang an Interbase/Firebird/Vulcan server ...
	//		stmt.executeUpdate("INSERT INTO VTABLE SELECT * FROM VTABLE;");
	//		// PASS:0154 If 4 rows are inserted?
	//
	//		rs = stmt.executeQuery("SELECT COUNT(*) FROM VTABLE;");
	//		rs.next();
	//		assertEquals(4, rs.getInt(1));
	//		// PASS:0154 If count = 4?
	//		// END TEST >>> 0154 < < < END TEST
	//
	//	}
	/*
	 * Name: testSdl_019
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_019() throws SQLException {
		setupBaseTables();

		// TEST:0155 CREATE Table with Unique (...), INSERT via SELECT!

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:0155 If count = 12?

		try {
			assertEquals(0, stmt.executeUpdate("INSERT INTO WORKS "
					+ "SELECT 'E3',PNUM,100 " + "FROM PROJ;"));
			fail("Violation of PRIMARY or UNIQUE KEY constraint");
			// PASS:0155 If ERROR, unique constraint, 0 rows inserted?

		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		rs.next();
		assertEquals(12, rs.getInt(1));
		// PASS:0155 If count = 12?

		// END TEST >>> 0155 <<< END TEST

	}
	/*
	 * Name: testSdl_020
	 * 
	 * Notes:
	 *  
	 */

	public void testSdl_020() throws SQLException {
		setupBaseTables();

		// TEST:0156 Tables(Multi-sets), duplicate rows allowed!
		stmt.executeUpdate("CREATE TABLE TEMP_S "
				+ "(EMPNUM  CHAR(3), GRADE DECIMAL(4), CITY CHAR(15));");

		assertEquals(5, stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "SELECT EMPNUM,GRADE,CITY " + "FROM STAFF"));
		// PASS:0156 If 5 rows are inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0156 If count = 5?

		assertEquals(5, stmt.executeUpdate("INSERT INTO TEMP_S "
				+ "SELECT EMPNUM,GRADE,CITY " + "FROM STAFF;"));
		// PASS:0156 If 5 rows are inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM TEMP_S;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		// PASS:0156 If count = 10?

		rs = stmt.executeQuery("SELECT COUNT(DISTINCT EMPNUM) FROM TEMP_S;");
		rs.next();
		assertEquals(5, rs.getInt(1));
		// PASS:0156 If count = 5?

		// END TEST >>> 0156 <<< END TEST

	}
	
	
	// testSdl_021 moved to
	//	 TestQueriesThatUseGrantRevoke.testGrantSelectToPublicNoDelete()

	// testSdl_022 moved to
	// TestQueriesThatUseGrantRevoke.testGrantInsertNoSelect()
	
	// testSdl_023 moved to
	// TestQueriesThatUseGrantRevoke.testGrantWithoutGrantOption()
		
	/*
	 * Name: testSdl_024
	 * 
	 * Notes:TODO: This test also shows the Firebird bug where inserting a
	 * record into a view causes stmt.executeUpdate() to return 2, not 1.
	 *  
	 */
	public void testSdl_024() throws SQLException {
		setupBaseTables();

		// TEST:0203 CREATE VIEW On a VIEW !

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFFV2_VIEW;");
		rs.next();
		assertEquals(1, rs.getInt(1));
		// PASS:0203 If count = 1?

		rs = stmt.executeQuery("SELECT EMPNUM, GRADE " + "FROM STAFFV2_VIEW "
				+ "WHERE EMPNUM = 'E3'; ");
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals(13, rs.getInt(2));
		// PASS:0203 If EMPNUM = 'E3' and GRADE = 13 ?

		stmt.executeUpdate("INSERT INTO STAFFV2_VIEW "
				+ "VALUES('E7','Gallagher',17,'Vienna'); ");
		// PASS:0203 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFFV2_VIEW; ");
		rs.next();
		assertEquals(2, rs.getInt(1));
		// PASS:0203 If count = 2 ?

		// END TEST >>> 0203 <<< END TEST

	}

	/*
	 * Name: testSdl_025
	 * 
	 * Notes: TODO: This test also shows the Firebird bug where inserting a
	 * record into a view causes stmt.executeUpdate() to return 2, not 1.
	 *  
	 */

	public void testSdl_025() throws SQLException {

		// TEST:0204 Updatable VIEW with compound conditions in CHECK!
		setupBaseTables();
		stmt.executeUpdate("CREATE VIEW DOMAIN_VIEW "
				+ "AS SELECT * FROM WORKS "
				+ "WHERE  EMPNUM = 'E1' AND HOURS = 80 "
				+ "OR EMPNUM = 'E2' AND HOURS = 40 "
				+ "OR EMPNUM = 'E4' AND HOURS = 20 " + "WITH CHECK OPTION;");

		rs = stmt.executeQuery("SELECT EMPNUM, HOURS " + "FROM DOMAIN_VIEW "
				+ "WHERE PNUM = 'P3';");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertEquals(80, rs.getInt(2));
		// PASS:0204 If EMPNUM = 'E1' and HOURS = 80?

		stmt.executeUpdate("INSERT INTO DOMAIN_VIEW "
				+ "VALUES('E1', 'P7', 80);");
		// PASS:0204 If 1 row is inserted?

		rs = stmt.executeQuery("SELECT COUNT(*) FROM DOMAIN_VIEW;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0204 If count = 4?

		try {
			stmt.executeUpdate("INSERT INTO DOMAIN_VIEW "
					+ "VALUES('E2', 'P4', 80);");
			fail("INSERT should violate CHECK option");
			// PASS:0204 If 0 rows are inserted - Violation of check option?
		} catch (SQLException sqle) {
		}

		try {
			stmt.executeUpdate("INSERT INTO DOMAIN_VIEW "
					+ "VALUES('E5', 'P5', 20);");
			fail("INSERT should violate CHECK option");
			// PASS:0204 If 0 rows are inserted - Violation of check option?
		} catch (SQLException sqle) {
		}

		rs = stmt.executeQuery("SELECT COUNT(*) FROM DOMAIN_VIEW;");
		rs.next();
		assertEquals(4, rs.getInt(1));
		// PASS:0204 If count = 4

		rs = stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		rs.next();
		assertEquals(13, rs.getInt(1));

		stmt.executeUpdate("DROP view DOMAIN_VIEW");
		// PASS:0204 If count = 13
		// END TEST >>> 0204 <<< END TEST
	}

	/*
	 * Name: testSdl_026
	 * 
	 * Notes: TODO: check with Bill on this
	 *  
	 */
	public void testSdl_026() throws SQLException {
		setupBaseTables();

		// TEST:0237 FIPS sizing - identifier length 18!
		// setup

		stmt
				.executeUpdate("CREATE TABLE CHARACTER18TABLE18 (CHARS18NAME18CHARS CHAR(4)); ");
		stmt
				.executeUpdate("CREATE VIEW  CHARACTERS18VIEW18 (LONGNAME18LONGNAME) "
						+ "AS SELECT CHARS18NAME18CHARS "
						+ "FROM CHARACTER18TABLE18 WHERE CHARS18NAME18CHARS <> 'long'; ");

		stmt.executeUpdate("DELETE FROM CHARACTER18TABLE18;");

		assertEquals(1, stmt.executeUpdate("INSERT INTO CHARACTERS18VIEW18 "
				+ "VALUES ('VALU');"));
		// PASS:0237 If 1 row is inserted?

		assertEquals(1, stmt.executeUpdate("UPDATE CHARACTER18TABLE18 "
				+ "SET CHARS18NAME18CHARS = 'VAL4' "
				+ "WHERE CHARS18NAME18CHARS = 'VALU'; "));
		// PASS:0237 If 1 row is updated?

		rs = stmt.executeQuery("SELECT * " + "FROM CHARACTERS18VIEW18; ");
		rs.next();
		assertEquals("VAL4", rs.getString(1).trim());
		// PASS:0237 If LONGNAME18LONGNAME = 'VAL4' ?

		rs = stmt.executeQuery("SELECT CORRELATIONNAMES18.CHARS18NAME18CHARS "
				+ "FROM CHARACTER18TABLE18 CORRELATIONNAMES18 "
				+ "WHERE CORRELATIONNAMES18.CHARS18NAME18CHARS = 'VAL4';");
		rs.next();
		assertEquals("VAL4", rs.getString(1).trim());
		// PASS:0237 If CORRELATIONNAMES18.CHARS18NAME18CHARS = 'VAL4'?

		// restore
		stmt.executeUpdate("DELETE FROM CHARACTER18TABLE18;");

		// END TEST >>> 0237 <<< END TEST */
	}

	/*
	 * Name: testSdl_027
	 * 
	 * Notes: Check this one!!!!
	 *  
	 */
	public void testSdl_027() throws SQLException {
		setupBaseTables();
		// setup

		stmt.executeUpdate("DELETE FROM TEMP_OBSERV;");
		stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV VALUES (1984, 'Sun City', 110, 44);");
		stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV VALUES (1984, 'Iceburg', 45, -90);");
		stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV VALUES (1984, 'Abeland', 101, 10);");
		stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV VALUES (1985, 'Sun City', 105, 50);");
		stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV VALUES (1985, 'Iceburg', 47, -82);");
		stmt
				.executeUpdate("INSERT INTO TEMP_OBSERV VALUES (1985, 'Abeland', 98, -3);");

		stmt
				.executeUpdate("CREATE VIEW CELSIUS_OBSERV (CITY, YEAR_OBSERV, MIN_C, MAX_C) "
						+ "AS SELECT CITY, YEAR_OBSERV, (MIN_TEMP - 32) * 5 / 9, "
						+ "(MAX_TEMP - 32) * 5 / 9 FROM TEMP_OBSERV; ");

		stmt.executeUpdate("CREATE VIEW MULTI_YEAR_OBSERV (CITY, HIGH, LOW) "
				+ "AS SELECT CITY, AVG(MAX_TEMP), AVG(MIN_TEMP) "
				+ "FROM TEMP_OBSERV GROUP BY CITY;");

		stmt
				.executeUpdate("CREATE VIEW EXTREME_TEMPS (YEAR_OBSERV, HIGH, LOW) "
						+ "AS SELECT YEAR_OBSERV, MAX(MAX_TEMP), MIN(MIN_TEMP) "
						+ "FROM TEMP_OBSERV " + "GROUP BY YEAR_OBSERV; ");

		// TEST:0391 Correlation names used in self-join of view!

		rs = stmt.executeQuery("SELECT X.CITY, X.MAX_C, Y.MAX_C, "
				+ "(X.MAX_C + Y.MAX_C) / 2 "
				+ "FROM CELSIUS_OBSERV X, CELSIUS_OBSERV Y "
				+ "WHERE X.YEAR_OBSERV = 1984 AND "
				+ "Y.YEAR_OBSERV = 1985 AND " + "X.CITY = Y.CITY "
				+ "ORDER BY 4 DESC; ");
		rs.next();
		assertEquals("Sun City", rs.getString(1).trim());
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(2) >= 43.31)
				&& (rs.getFloat(2) <= 43.35));
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(3) >= 40.54)
				&& (rs.getFloat(3) <= 40.57));
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(4) >= 41.93)
				&& (rs.getFloat(4) <= 41.96));

		// PASS:0391 If for the first row X.CITY = 'Sun City',?
		// PASS:0391 X.MAX_C is between 43.31 and 43.35,?
		// PASS:0391 Y.MAX_C is between 40.54 and 40.57?
		// PASS:0391 and (X.MAX_C + Y.MAX_C) /2 is between 41.93 and 41.96?

		// END TEST >>> 0391 <<< END TEST
		// *************************************************************

		// TEST:0401 VIEW with computed columns!

		rs = stmt.executeQuery("SELECT CITY, YEAR_OBSERV, MIN_C, MAX_C "
				+ "FROM CELSIUS_OBSERV "
				+ "WHERE YEAR_OBSERV = 1984 AND MIN_C > 5; ");
		rs.next();
		assertEquals("Sun City", rs.getString(1).trim());
		assertEquals(1984, rs.getInt(2));
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(3) >= 6.65)
				&& (rs.getFloat(3) <= 6.68));
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(4) >= 43.31)
				&& (rs.getFloat(4) <= 43.35));
		// PASS:0401 If CITY = 'Sun City' and YEAR_OBSERV = 1984?
		// PASS:0401 If MIN_C is between 6.65 and 6.68?
		// PASS:0401 If MAX_C is between 43.31 and 43.35?

		rs = stmt.executeQuery("SELECT CITY, HIGH, LOW "
				+ "FROM MULTI_YEAR_OBSERV " + "ORDER BY CITY ASC; ");
		rs.next();
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(2) >= 99.3)
				&& (rs.getFloat(2) <= 99.7));
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(3) >= 3.3)
				&& (rs.getFloat(3) <= 3.7));
		// PASS:0401 If for the first row HIGH is between 99.3 and 99.7?
		// PASS:0401 and LOW is between 3.3 and 3.7?

		rs = stmt.executeQuery("SELECT HIGH, YEAR_OBSERV, LOW  "
				+ "FROM EXTREME_TEMPS " + "ORDER BY YEAR_OBSERV DESC; ");
		rs.next();
		assertEquals(105, rs.getInt(1));
		assertEquals(-82, rs.getInt(3));

		// PASS:0401 If for the first row HIGH = 105 and LOW = -82?

		// END TEST >>> 0401 <<< END TEST */
	}
	/*
	 * Name: testSdl_028
	 * 
	 * Notes:
	 *  
	 */
	public void testSdl_028() throws SQLException {
		setupBaseTables();
		// setup

		stmt.executeUpdate("CREATE VIEW SET_TEST (EMP1, EMP_AVG, EMP_MAX) AS "
				+ "SELECT STAFF.EMPNUM, AVG(HOURS), MAX(HOURS) "
				+ "FROM STAFF, WORKS " + "GROUP BY STAFF.EMPNUM; ");

		stmt
				.executeUpdate("CREATE VIEW DUP_COL (EMP1, PNO, HOURS, HOURS_2) AS "
						+ "SELECT EMPNUM, PNUM, HOURS, HOURS * 2 FROM WORKS;");

		// TEST:0397 Grouped view!

		rs = stmt.executeQuery(" SELECT EMP1, EMP_AVG, EMP_MAX  "
				+ "FROM SET_TEST ORDER BY EMP1; ");
		rs.next();
		assertEquals("E1", rs.getString(1).trim());
		assertTrue("Returned value is not in the acceptable range", (rs
				.getFloat(2) >= 38)
				&& (rs.getFloat(2) <= 39));
		assertEquals(80, rs.getInt(3));
		// PASS:0397 If for the first row EMP1 = 'E1',?
		// PASS:0397 EMP_AVG is between 38 and 39, and EMP_MAX = 80?

		// END TEST >>> 0397 <<< END TEST
		// *************************************************************

		// TEST:0420 View with multiple SELECT of same column!

		rs = stmt.executeQuery("SELECT EMP1, HOURS, HOURS_2 "
				+ "FROM DUP_COL WHERE EMP1 = 'E3';");
		rs.next();
		assertEquals("E3", rs.getString(1).trim());
		assertEquals(20, rs.getInt(2));
		assertEquals(40, rs.getInt(3));

		// PASS:0420 If EMP1 = 'E3', HOURS = 20 and HOURS_2 = 40?

		// END TEST >>> 0420 <<< END TEST
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
		try {
			stmt.executeUpdate("drop table proj1");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop table staff4");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop table staff3");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop table works");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop table works1");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop table temp_observ");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop view staffv1");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop view staffv2");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop view staff_works_design");
		} catch (SQLException dontcare) {
			// do nothing
		}
		try {
			stmt.executeUpdate("drop view STAFFV2_VIEW");
		} catch (SQLException dontcare) {
			// do nothing
		}

		stmt.executeUpdate("CREATE TABLE STAFF "
				+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");

		stmt.executeUpdate("CREATE TABLE STAFF1 "
				+ "(EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15)); ");

		stmt
				.executeUpdate("CREATE TABLE STAFF3 "
						+ "(EMPNUM CHAR(3) NOT NULL ," + "EMPNAME CHAR(20), "
						+ "GRADE DECIMAL(4), " + "CITY CHAR(15),"
						+ "UNIQUE (EMPNUM));");

		stmt.executeUpdate("CREATE TABLE STAFF4 "
				+ "(EMPNUM    CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE PROJ "
				+ "(PNUM CHAR(3) NOT NULL UNIQUE, " + "PNAME CHAR(20), "
				+ "PTYPE CHAR(6), " + "BUDGET   DECIMAL(9), "
				+ "CITY CHAR(15));");

		stmt
				.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
						+ "PNAME  CHAR(20),"
						+ "PTYPE  CHAR(6),"
						+ "BUDGET DECIMAL(9)," + "CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE WORKS " + "(EMPNUM CHAR(3) NOT NULL, "
				+ "PNUM CHAR(3) NOT NULL, " + "HOURS DECIMAL(5), "
				+ "UNIQUE(EMPNUM,PNUM));");

		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM CHAR(3) NOT NULL, "
				+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
				+ "UNIQUE(EMPNUM, PNUM));");

		stmt
				.executeUpdate("CREATE TABLE TEMP_OBSERV "
						+ "(YEAR_OBSERV  NUMERIC(4), "
						+ "CITY         CHAR(10), "
						+ "MAX_TEMP     NUMERIC(5,2), "
						+ "MIN_TEMP     NUMERIC(5,2));");

		stmt.executeUpdate(" CREATE VIEW STAFFV1 " + "AS SELECT * FROM STAFF "
				+ "WHERE  GRADE >= 12;");

		stmt.executeUpdate("CREATE VIEW STAFFV2 " + "AS SELECT * FROM STAFF "
				+ "WHERE  GRADE >= 12 " + "WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE VIEW STAFFV2_VIEW " + "AS SELECT * "
				+ "FROM   STAFFV2 " + "WHERE  CITY = 'Vienna';");

		stmt
				.executeUpdate("CREATE VIEW STAFF_WORKS_DESIGN (NAME,COST,PROJECT) "
						+ "AS SELECT EMPNAME,HOURS*2*GRADE,PNAME "
						+ "FROM   PROJ,STAFF,WORKS "
						+ "WHERE  STAFF.EMPNUM=WORKS.EMPNUM "
						+ "AND WORKS.PNUM=PROJ.PNUM " + "AND PTYPE='Design';");

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

		stmt.executeUpdate("INSERT INTO STAFF3 SELECT * FROM   STAFF;");

	}
}