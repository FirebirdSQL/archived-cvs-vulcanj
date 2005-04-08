/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 17, 2004
 * 
 * GRANT/REVOKE isn't initially supported by SAS Table Server. So these test cases
 * have been factored out of the NIST suite, such that they can be skipped over in 
 * Junit runs.
 * 
 * These tests depend on other users being setup ahead of time. Ensure these users are present:
 * userid   password
 * //////   ////////
 * User1 masterkey
 * User2 masterkey
 * User3 masterkey
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
public class TestQueriesThatUseGrantRevoke extends NistTestBase {
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

	public TestQueriesThatUseGrantRevoke(String arg0) {
		super(arg0);
	}

	/*
	 * Name: testFkWithGrantSelectOnly
	 *  
	 */
	public void testFkWithGrantSelectOnly() throws SQLException {
		// *************************************************
		// NOTE:0383 Either TAB5 does not exist -OR-
		// NOTE:0383 TAB5 exists but allows orphans (F.K without P.K)

		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			stmtUser1
					.executeUpdate("CREATE TABLE TTT(P1 DECIMAL(4) NOT NULL UNIQUE, "
							+ "P2 CHAR(4));");
			stmtUser1.executeUpdate("GRANT SELECT ON TTT TO SCHANZLE");
			connUser1.close(); // ensure we don't reuse by mistake

			try {
				stmtUser2.executeUpdate("CREATE TABLE TAB6(F16 DECIMAL(4), "
						+ "F6 CHAR(4), " + "FOREIGN KEY (F16) "
						+ "REFERENCES TTT(P1)); ");
				fail();
			} catch (SQLException anticipatedError) {
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testGrantAllPrivelegesToPublicSelectInsert
	 *  
	 */
	public void testGrantAllPrivelegesToPublicSelectInsert()
			throws SQLException {
		// *************************************************
		// NOTE:0383 Either TAB5 does not exist -OR-
		// NOTE:0383 TAB5 exists but allows orphans (F.K without P.K)

		Connection connUser2 = null;
		Statement stmtUser2 = null;
		Connection connUser1 = null;
		Statement stmtUser1 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM   CHAR(3) NOT NULL UNIQUE, "
					+ "EMPNAME  CHAR(20), " + "GRADE    DECIMAL(4), "
					+ "CITY     CHAR(15))");
			stmtUser1.executeUpdate("GRANT ALL PRIVILEGES ON STAFF TO PUBLIC");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');");
			connUser1.close(); // ensure we don't reuse by mistake

			// TEST:0138 GRANT ALL Privileges to Public (SELECT, INSERT) !
			rs = stmtUser2.executeQuery("SELECT EMPNUM,EMPNAME,USER "
					+ "FROM STAFF " + "WHERE EMPNUM = 'E3'; ");
			rs.next();
			assertEquals("Carmen", rs.getString(2).trim());
			assertEquals("USER2", rs.getString(3).trim());

			// PASS:0138 If EMPNAME = 'Carmen' and USER = 'User1'?

			stmtUser2.executeUpdate("INSERT INTO STAFF "
					+ "VALUES('E7','User1',15,'Gaithersburg');");
			// PASS:0138 If 1 row is inserted?

			rs = stmtUser2.executeQuery("SELECT EMPNUM,EMPNAME "
					+ "FROM STAFF " + "WHERE EMPNUM='E7';");
			// PASS:0138 If EMPNAME = 'User1'?

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testGrantAllPrivelegesToPublicSelectUpdate
	 *  
	 */
	public void testGrantAllPrivelegesToPublicSelectUpdate()
			throws SQLException {
		// *************************************************
		// NOTE:0383 Either TAB5 does not exist -OR-
		// NOTE:0383 TAB5 exists but allows orphans (F.K without P.K)

		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			stmtUser1.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM   CHAR(3) NOT NULL UNIQUE, "
					+ "EMPNAME  CHAR(20), " + "GRADE    DECIMAL(4), "
					+ "CITY     CHAR(15))");
			stmtUser1.executeUpdate("GRANT ALL PRIVILEGES ON STAFF TO PUBLIC");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');");
			connUser1.close();

			// TEST:0138 GRANT ALL Privileges to Public (SELECT, INSERT) !
			rs = stmtUser2.executeQuery("SELECT EMPNUM,EMPNAME,USER "
					+ "FROM STAFF " + "WHERE EMPNUM = 'E3'; ");
			rs.next();
			assertEquals("Carmen", rs.getString(2).trim());
			assertEquals("USER2", rs.getString(3).trim());

			// PASS:0138 If EMPNAME = 'Carmen' and USER = 'User1'?

			stmtUser2
					.executeUpdate("UPDATE STAFF SET  EMPNUM='E8',EMPNAME='SCHANZLE' "
							+ "WHERE EMPNUM='E3';");
			// PASS:0139 If 1 row is updated?

			rs = stmtUser2.executeQuery("SELECT EMPNUM,EMPNAME "
					+ "FROM STAFF " + "WHERE EMPNUM='E8';");
			rs.next();
			assertEquals("SCHANZLE", rs.getString(2).trim());
			// PASS:0138 If EMPNAME = 'User2'?

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testGrantSelectToPublicNoInsert
	 *  
	 */
	public void testGrantSelectToPublicNoInsert() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			stmtUser1.executeUpdate("CREATE TABLE PROJ "
					+ "(PNUM     CHAR(3) NOT NULL UNIQUE,"
					+ "PNAME    CHAR(20)," + "PTYPE    CHAR(6),"
					+ "BUDGET   DECIMAL(9)," + "CITY     CHAR(15));");
			stmtUser1.executeUpdate("GRANT SELECT ON PROJ TO PUBLIC;");
			stmtUser1
					.executeUpdate("INSERT INTO PROJ VALUES  ('P3','SDP','Test',30000,'Tampa');");
			connUser1.close();

			// TEST:0140 Priv. violation: GRANT SELECT to Public, No INSERT!

			rs = stmtUser2.executeQuery("SELECT PNUM,PNAME,USER FROM PROJ "
					+ "WHERE PNUM = 'P3';");
			rs.next();
			assertEquals("SDP", rs.getString(2).trim());
			// PASS:0140 If PNAME = 'SDP'

			try {
				stmtUser2
						.executeUpdate("INSERT INTO PROJ "
								+ "VALUES('P7','PROGRAM','RISC',15000,'Gaithersburg');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE PROJ";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// PASS:0140 If ERROR, syntax error/access violation, 0 rows
			// inserted?

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testGrantSelectUpdate
	 *  
	 */
	public void testGrantSelectUpdate() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE WORKS ("
					+ "EMPNUM CHAR(3) NOT NULL," + "PNUM CHAR(3) NOT NULL, "
					+ "HOURS DECIMAL(5), " + "UNIQUE(EMPNUM,PNUM));");

			stmtUser1
					.executeUpdate("INSERT INTO WORKS VALUES  ('E3','P2',20);");
			stmtUser1.executeUpdate("GRANT SELECT ON WORKS TO PUBLIC;");
			stmtUser1
					.executeUpdate("GRANT SELECT,UPDATE ON WORKS TO User2 with grant option");
			connUser1.close();

			// try the select
			rs = stmtUser2.executeQuery("SELECT EMPNUM,PNUM,USER FROM WORKS "
					+ "WHERE EMPNUM = 'E3';");
			rs.next();
			assertEquals("P2", rs.getString(2).trim());
			// PASS:0141 If PNUM = 'P2'

			// try the update
			stmtUser2
					.executeUpdate("UPDATE WORKS SET EMPNUM = 'E8',PNUM = 'P8' "
							+ "WHERE EMPNUM = 'E3';");

			// verify the update
			rs = stmtUser2.executeQuery("SELECT EMPNUM,PNUM " + "FROM WORKS "
					+ "WHERE EMPNUM='E8';");
			rs.next();
			assertEquals("P8", rs.getString(2).trim());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testGrantSelectUpdateWithGrant
	 *  
	 */
	public void testGrantSelectUpdateWithGrant() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;
		Connection connUser3 = null;
		Statement stmtUser3 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			connUser3 = DriverManager.getConnection(getUrl(), "User3",
					"masterkey");
			stmtUser3 = connUser3.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE WORKS ("
					+ "EMPNUM CHAR(3) NOT NULL," + "PNUM CHAR(3) NOT NULL, "
					+ "HOURS DECIMAL(5), " + "UNIQUE(EMPNUM,PNUM));");

			stmtUser1
					.executeUpdate("INSERT INTO WORKS VALUES  ('E3','P2',20);");
			stmtUser1.executeUpdate("GRANT SELECT ON WORKS TO PUBLIC;");
			stmtUser1
					.executeUpdate("GRANT SELECT,UPDATE ON WORKS TO User2 with grant option");
			connUser1.close();

			stmtUser2.executeUpdate("GRANT SELECT, UPDATE ON WORKS TO User3");
			stmtUser2.close();
			connUser2.close();

			// try the select
			rs = stmtUser3.executeQuery("SELECT EMPNUM,PNUM,USER FROM WORKS "
					+ "WHERE EMPNUM = 'E3';");
			rs.next();
			assertEquals("P2", rs.getString(2).trim());
			// PASS:0141 If PNUM = 'P2'

			// try the update
			stmtUser3
					.executeUpdate("UPDATE WORKS SET EMPNUM = 'E8',PNUM = 'P8' "
							+ "WHERE EMPNUM = 'E3';");

			// verify the update
			rs = stmtUser3.executeQuery("SELECT EMPNUM,PNUM " + "FROM WORKS "
					+ "WHERE EMPNUM='E8';");
			rs.next();
			assertEquals("P8", rs.getString(2).trim());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser3.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	} /*
	   * Name: testGrantSelectUpdateOnView
	   *  
	   */
	public void testGrantSelectUpdateOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			stmtUser1.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM CHAR(3) NOT NULL UNIQUE, "
					+ "EMPNAME CHAR(20), " + "GRADE    DECIMAL(4), "
					+ "CITY CHAR(15))");
			stmtUser1.executeUpdate("CREATE VIEW STAFF2 " + "AS SELECT * "
					+ "FROM STAFF ");
			stmtUser1.executeUpdate("GRANT ALL PRIVILEGES ON STAFF TO PUBLIC");
			stmtUser1.executeUpdate("GRANT SELECT, UPDATE ON STAFF2 TO User2");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');");
			stmtUser1.close();
			connUser1.close();

			// try the select on the view
			rs = stmtUser2.executeQuery("SELECT EMPNUM,EMPNAME,USER "
					+ "FROM STAFF2 WHERE  EMPNUM = 'E3';");
			rs.next();
			assertEquals("Carmen", rs.getString(2).trim());

			// try the update on the view
			assertEquals(1, stmtUser2.executeUpdate("UPDATE STAFF2 "
					+ "SET EMPNUM = 'E8',EMPNAME = 'Ling' "
					+ "WHERE EMPNUM = 'E3';"));
			// PASS:0143 If 1 row is updated ?

			stmtUser2.close();
			connUser2.close();

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}

	// testGrantSelectToPublicNoDelete()
	// was TestSdl.testSdl_021()
	public void testGrantSelectToPublicNoDelete() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE PROJ "
					+ "(PNUM CHAR(3) NOT NULL UNIQUE," + "PNAME CHAR(20),"
					+ "PTYPE    CHAR(6)," + "BUDGET DECIMAL(9),"
					+ "CITY     CHAR(15));");
			stmtUser1.executeUpdate("GRANT SELECT ON PROJ TO PUBLIC;");
			stmtUser1
					.executeUpdate("INSERT INTO PROJ VALUES  ('P3','SDP','Test',30000,'Tampa');");
			stmtUser1.close();
			connUser1.close();

			// test the select
			rs = stmtUser2.executeQuery("SELECT PNUM,PNAME,USER FROM PROJ "
					+ "WHERE PNUM = 'P3';");
			rs.next();
			assertEquals("SDP", rs.getString(2).trim());
			// PASS:0140 If PNAME = 'SDP'

			// try the delete
			try {
				stmtUser2.executeUpdate("DELETE FROM PROJ WHERE PNUM = 'P3';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE PROJ";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			stmtUser2.close();
			connUser2.close();

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	// testGrantInsertNoSelect()
	// was TestSdl.testSdl_021()
	public void testGrantInsertNoSelect() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE UPUNIQ ("
					+ "NUMKEY  DECIMAL(3) NOT NULL UNIQUE,"
					+ "COL2    CHAR(2));");
			stmtUser1.executeUpdate("INSERT INTO UPUNIQ VALUES(3,'C');");
			stmtUser1.executeUpdate("GRANT INSERT ON UPUNIQ TO USER2 ;");
			stmtUser1.close();
			connUser1.close();

			// test the insert
			assertEquals(1, stmtUser2.executeUpdate("INSERT INTO UPUNIQ "
					+ "VALUES (10,'X');"));

			// test the select. Should fail.
			try {
				rs = stmtUser2.executeQuery("SELECT * " + "FROM UPUNIQ "
						+ "WHERE NUMKEY = 3;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE UPUNIQ";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// test insert with duplicate value. should fail
			try {
				assertEquals(1, stmtUser2.executeUpdate("INSERT INTO UPUNIQ "
						+ "VALUES (10,'X');"));
				fail();
			} catch (SQLException sqle) {
				// we only need the 1st part of this message. rest of message
				// contains the named integrity constraint...
				String UNIQUE_ERROR = "GDS Exception. 335544665. violation of PRIMARY or UNIQUE KEY constraint";
				assertEquals(UNIQUE_ERROR, sqle.getMessage().substring(0,
						UNIQUE_ERROR.length()));
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantWithoutGrantOption
	 *  
	 */
	public void testGrantWithoutGrantOption() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;
		Connection connUser3 = null;
		Statement stmtUser3 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			connUser3 = DriverManager.getConnection(getUrl(), "User3",
					"masterkey");
			stmtUser3 = connUser3.createStatement();
			stmtUser1.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM CHAR(3) NOT NULL UNIQUE, "
					+ "EMPNAME CHAR(20), " + "GRADE    DECIMAL(4), "
					+ "CITY CHAR(15))");
			stmtUser1.executeUpdate("GRANT ALL PRIVILEGES ON STAFF TO PUBLIC");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');");
			stmtUser1
					.executeUpdate("CREATE TABLE STAFF4 (EMPNUM    CHAR(3) NOT NULL, "
							+ "EMPNAME  CHAR(20), "
							+ "GRADE DECIMAL(4), "
							+ "CITY   CHAR(15)); ");

			// note missing "with grant" option
			stmtUser1
					.executeUpdate("GRANT SELECT,INSERT, DELETE ON STAFF4 TO User2;");
			connUser1.close();

			try {
				stmtUser2
						.executeUpdate("GRANT SELECT,INSERT, DELETE ON STAFF4 TO User3;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
					assertEquals(
							"GDS Exception. 335544351. unsuccessful metadata update\n"
									+ "no grant option for privilege S on table/view STAFF4",
							sqle.getMessage());
			}
			stmtUser2.close();
			connUser2.close();

			// try the insert
			try {
				stmtUser3.executeUpdate("INSERT INTO STAFF4 " + "SELECT * "
						+ "FROM STAFF;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE STAFF4";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// try the select
			try {
				stmtUser3.executeQuery("SELECT EMPNUM,EMPNAME,USER "
						+ "FROM STAFF4 " + "WHERE EMPNUM = 'E3'; ");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE STAFF4";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// try the delete
			try {
				stmtUser3.executeUpdate("DELETE FROM STAFF4;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE STAFF4";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// try a count(*)
			try {
				rs = stmtUser3.executeQuery("SELECT COUNT(*) FROM STAFF4;");
				rs.next();
				assertEquals(0, rs.getInt(1));
				fail(); // failure here is dependent on the database. Firebird
				// fails.
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE STAFF4";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser3.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testGrantOnlySelectToIndividual
	 * 
	 * Notes: Select test from SDL_0029
	 *  
	 */
	public void testGrantOnlySelectToIndividual() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE AA (CHARTEST CHAR(20));");
			stmtUser1
					.executeUpdate("INSERT INTO AA VALUES('Twenty Characters...');");
			stmtUser1.executeUpdate("GRANT SELECT ON AA TO user2");
			// GRANT SELECT ON VAA TO SCHANZLE;

			stmtUser1.close();
			connUser1.close();

			// select should work
			rs = stmtUser2.executeQuery("select chartest from aa");
			rs.next();
			assertEquals("Twenty Characters...", rs.getString(1));
			assertFalse(rs.next());

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO aa "
						+ "VALUES ('this should not work');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE AA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM AA;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE AA  "
						+ "SET CHARTEST = 'This should not work';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE AA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM AA;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM AA;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE AA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM AA;");
			rs.next();
			assertEquals(1, rs.getInt(1));
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyInsertToIndividual
	 * 
	 * Notes: INSERT test from SDL_0029
	 *  
	 */
	public void testGrantOnlyInsertToIndividual() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");
			stmtUser1.executeUpdate("GRANT Insert ON bb TO user2");
			// GRANT SELECT ON VAA TO SCHANZLE;

			stmtUser1.close();
			connUser1.close();

			// insert should work
			assertEquals(1, stmtUser2
					.executeUpdate("INSERT INTO BB VALUES ('A');"));

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from bb");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("Firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE BB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("update bb set chartest = 'b';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE BB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM bb;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE BB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyUpdateToIndividual
	 * 
	 * Notes: UPDATE test from SDL_0029
	 *  
	 */
	public void testGrantOnlyUpdateToIndividual() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1
					.executeUpdate("CREATE TABLE CC (CHARTEST CHARACTER(20));");
			stmtUser1
					.executeUpdate("INSERT INTO cc VALUES('Twenty Characters...');");
			stmtUser1.executeUpdate("GRANT Update ON cc TO user2");

			stmtUser1.close();
			connUser1.close();

			// update should work
			stmtUser2.executeUpdate("UPDATE cc  "
					+ "SET CHARTEST = 'This --should-- work';");

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from cc");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE CC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO cc "
						+ "VALUES ('this should not work');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE CC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM cc;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE CC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyDeleteToIndividual
	 * 
	 * Notes: DELETE test from SDL_0029
	 *  
	 */
	public void testGrantOnlyDeleteToIndividual() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE dd (CHARTEST character);");
			stmtUser1.executeUpdate("INSERT INTO DD VALUES('a');");
			stmtUser1.executeUpdate("GRANT delete ON dd TO user2");
			// GRANT SELECT ON VAA TO SCHANZLE;

			stmtUser1.close();
			connUser1.close();

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from dd");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE DD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO dd " + "VALUES ('e');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE DD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE dd  " + "SET CHARTEST = 'e';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE DD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should work
			stmtUser2.executeUpdate("DELETE FROM dd;");

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlySelectToPublic
	 * 
	 * Notes: Select test from SDL_0030
	 *  
	 */
	public void testGrantOnlySelectToPublic() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE AA (CHARTEST CHAR(20));");
			stmtUser1
					.executeUpdate("INSERT INTO AA VALUES('Twenty Characters...');");
			stmtUser1.executeUpdate("GRANT SELECT ON AA TO PUBLIC");
			// GRANT SELECT ON VAA TO SCHANZLE;

			stmtUser1.close();
			connUser1.close();

			// select should work
			rs = stmtUser2.executeQuery("select chartest from aa");
			rs.next();
			assertEquals("Twenty Characters...", rs.getString(1));
			assertFalse(rs.next());

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO aa "
						+ "VALUES ('this should not work');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE AA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM AA;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE AA  "
						+ "SET CHARTEST = 'This should not work';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE AA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM AA;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM AA;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE AA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM AA;");
			rs.next();
			assertEquals(1, rs.getInt(1));
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyInsertToPublic
	 * 
	 * Notes: INSERT test from SDL_0030
	 *  
	 */
	public void testGrantOnlyInsertToPublic() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");
			stmtUser1.executeUpdate("GRANT Insert ON bb TO public");

			stmtUser1.close();
			connUser1.close();

			// insert should work
			assertEquals(1, stmtUser2
					.executeUpdate("INSERT INTO BB VALUES ('A');"));

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from bb");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE BB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("update bb set chartest = 'b';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE BB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM bb;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE BB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyUpdateToPublic
	 * 
	 * Notes: UPDATE test from SDL_0030
	 *  
	 */
	public void testGrantOnlyUpdateToPublic() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1
					.executeUpdate("CREATE TABLE CC (CHARTEST CHARACTER(20));");
			stmtUser1
					.executeUpdate("INSERT INTO cc VALUES('Twenty Characters...');");
			stmtUser1.executeUpdate("GRANT Update ON cc TO public");

			stmtUser1.close();
			connUser1.close();

			// update should work
			stmtUser2.executeUpdate("UPDATE cc  "
					+ "SET CHARTEST = 'This --should-- work';");

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from cc");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE CC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO cc "
						+ "VALUES ('this should not work');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE CC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM cc;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE CC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyDeleteToPublic
	 * 
	 * Notes: DELETE test from SDL_0030
	 *  
	 */
	public void testGrantOnlyDeleteToPublic() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE dd (CHARTEST character);");
			stmtUser1.executeUpdate("INSERT INTO DD VALUES('a');");
			stmtUser1.executeUpdate("GRANT delete ON dd TO public");

			stmtUser1.close();
			connUser1.close();

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from dd");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE DD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO dd " + "VALUES ('e');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE DD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE dd  " + "SET CHARTEST = 'e';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE DD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should work
			stmtUser2.executeUpdate("DELETE FROM dd;");

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testIndividualWithoutAnyPrivileges
	 * 
	 * Notes: Individual with no privileges test from SDL_0031
	 *  
	 */
	public void testIndividualWithoutAnyPrivileges() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE VTABLE " + "(COL1  INTEGER, "
					+ "COL2  INTEGER, " + "COL3  INTEGER, " + "COL4  INTEGER, "
					+ "COL5  DECIMAL(7,2));");
			// user2 gets NO privileges for this test

			stmtUser1.close();
			connUser1.close();

			// delete should fail
			try {
				stmtUser2.executeUpdate("DELETE FROM vtable;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for delete/write "
							+ "access to TABLE VTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO vtable "
						+ "VALUES (0,1,2,3,4.25);");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE VTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// select should fail
			try {
				rs = stmtUser2
						.executeQuery("select col2 from vtable where col2 = 0");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE VTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE vtable  " + "SET col2 = 2;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE VTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	} /*
	   * Name: testGrantAllPrivilegesToIndividual
	   * 
	   * Notes: Individual with ALL privileges test from SDL_0031
	   *  
	   */
	public void testGrantAllPrivilegesToIndividual() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("  CREATE TABLE II (C1 INT); ");
			stmtUser1.executeUpdate("  grant all privileges on II to user2; ");

			// user2 gets ALL privileges for this test

			stmtUser1.close();
			connUser1.close();

			// insert should work
			assertEquals(1, stmtUser2
					.executeUpdate("INSERT INTO II VALUES (2);"));

			// update should work
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE II SET C1 = 3 WHERE C1 = 2;"));

			// select should work
			rs = stmtUser2.executeQuery("select c1 from II");
			rs.next();
			assertEquals(3, rs.getInt(1));

			// delete should work
			assertEquals(1, stmtUser2.executeUpdate("DELETE FROM II;"));

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantAllPrivilegesToPublic
	 * 
	 * Notes: Individual (through PUBLIC grant) with ALL privileges test from
	 * SDL_0031
	 *  
	 */
	public void testGrantAllPrivilegesToPublic() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("    CREATE TABLE JJ (C1 INT);");
			stmtUser1.executeUpdate("  grant all privileges on JJ to public; ");

			// user2 gets ALL privileges for this test, through PUBLIC GRANT

			stmtUser1.close();
			connUser1.close();

			// insert should work
			assertEquals(1, stmtUser2
					.executeUpdate("INSERT INTO JJ VALUES (2)"));

			// update should work
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE JJ SET C1 = 3 WHERE C1 = 2;"));

			// select should work
			rs = stmtUser2.executeQuery("SELECT C1 FROM JJ;");
			rs.next();
			assertEquals(3, rs.getInt(1));

			// delete should work
			assertEquals(1, stmtUser2.executeUpdate("DELETE FROM JJ;"));

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testIndividualSelectColumnUpdate
	 * 
	 * Notes: Column privileges test from SDL_0032
	 *  
	 */
	public void testIndividualSelectColumnUpdate() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		System.out.println("test.safe : " + System.getProperty("test.safe"));
		if (System.getProperty("test.safe") != null)
			if (System.getProperty("test.safe").equalsIgnoreCase("true"))
				assertTrue("This test didn't run because safe mode was on.",
						false);

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1
					.executeUpdate("CREATE TABLE STAFF3 (EMPNUM CHAR(3) NOT NULL,"
							+ " EMPNAME  CHAR(20), "
							+ " GRADE DECIMAL(4),  "
							+ "CITY   CHAR(15));");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E1','Alice',12,'Deale');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E2','Betty',10,'Vienna');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E3','Carmen',13,'Vienna');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E4','Don',12,'Deale');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E5','Ed',13,'Akron');");

			stmtUser1
					.executeUpdate("GRANT SELECT,UPDATE(EMPNUM,EMPNAME) ON STAFF3 "
							+ "TO user2 WITH GRANT OPTION;");

			// user2 has select privileges on the whole table, but update just
			// on empnum, empname

			stmtUser1.close();
			connUser1.close();

			// select should work
			rs = stmtUser2.executeQuery("SELECT EMPNUM, EMPNAME, GRADE, CITY "
					+ "FROM STAFF3 WHERE EMPNUM = 'E1';");
			rs.next();
			assertEquals("E1 ", rs.getString(1));
			assertEquals("Alice", rs.getString(2).trim());
			assertEquals(12, rs.getInt(3));
			assertEquals("Deale", rs.getString(4).trim());
			assertFalse(rs.next());

			// update should work, for empnum,empname
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE STAFF3 SET EMPNUM = 'E0' "
							+ "WHERE EMPNUM = 'E1';"));

			// update should work, for empnum,empname
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE STAFF3 SET EMPNAME = 'Larry'"
							+ "WHERE EMPNUM = 'E0';"));

			// update on grade should fail
			try {
				stmtUser2.executeUpdate("UPDATE STAFF3 SET GRADE = 15;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for update/write "
							+ "access to COLUMN GRADE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update on city should fail
			try {
				stmtUser2.executeUpdate("UPDATE STAFF3 "
						+ "SET CITY = 'Greenmount';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for update/write "
							+ "access to COLUMN CITY";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify update didn't take
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM STAFF3 "
					+ "WHERE CITY = 'Greenmount' OR GRADE = 15;");
			rs.next();
			assertEquals(0, rs.getInt(1));

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO STAFF3 "
						+ "VALUES ('E6','Mickey',12,'Nice');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					int FBErrorCode = sqle.getErrorCode();
					assertEquals(
							"shouldn't be able to update this column or table.",
							FBErrorCode, FBErrorCode);
				}
			}

			// verify insert didn't take
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM STAFF3 ");
			rs.next();
			assertEquals(5, rs.getInt(1));

			// delete should fail
			try {
				stmtUser2.executeUpdate("DELETE FROM STAFF3;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for delete/write "
							+ "access to TABLE STAFF3";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify delete didn't take
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM STAFF3 ");
			rs.next();
			assertEquals(5, rs.getInt(1));

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testIndividualSelectColumnUpdateOnView
	 * 
	 * Notes: Column privileges test (on a view) from SDL_0032
	 *  
	 */
	public void testIndividualSelectColumnUpdateOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		if (System.getProperty("test.safe") != null)
			if (System.getProperty("test.safe").equalsIgnoreCase("true"))
				assertTrue("This test didn't run because safe mode was on.",
						false);

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1
					.executeUpdate("CREATE TABLE STAFF3 (EMPNUM CHAR(3) NOT NULL,"
							+ " EMPNAME  CHAR(20), "
							+ " GRADE DECIMAL(4),  "
							+ "CITY   CHAR(15));");
			stmtUser1
					.executeUpdate("CREATE VIEW VSTAFF3 AS SELECT * FROM STAFF3;");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E1','Alice',12,'Deale');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E2','Betty',10,'Vienna');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E3','Carmen',13,'Vienna');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E4','Don',12,'Deale');");
			stmtUser1
					.executeUpdate("INSERT INTO STAFF3 VALUES ('E5','Ed',13,'Akron');");

			stmtUser1
					.executeUpdate("GRANT SELECT,UPDATE(EMPNUM,EMPNAME) ON vSTAFF3 "
							+ "TO user2 WITH GRANT OPTION;");

			// user2 has select privileges on the whole table, but update just
			// on empnum, empname

			stmtUser1.close();
			connUser1.close();

			// select should work
			rs = stmtUser2.executeQuery("SELECT EMPNUM, EMPNAME, GRADE, CITY "
					+ "FROM vSTAFF3 WHERE EMPNUM = 'E1';");
			rs.next();
			assertEquals("E1 ", rs.getString(1));
			assertEquals("Alice", rs.getString(2).trim());
			assertEquals(12, rs.getInt(3));
			assertEquals("Deale", rs.getString(4).trim());
			assertFalse(rs.next());

			// update should work, for empnum,empname
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE vSTAFF3 SET EMPNUM = 'E0' "
							+ "WHERE EMPNUM = 'E1';"));

			// update should work, for empnum,empname
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE vSTAFF3 SET EMPNAME = 'Larry'"
							+ "WHERE EMPNUM = 'E0';"));

			// update on grade should fail
			try {
				stmtUser2.executeUpdate("UPDATE vSTAFF3 SET GRADE = 15;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for update/write "
							+ "access to COLUMN GRADE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update on city should fail
			try {
				stmtUser2.executeUpdate("UPDATE STAFF3 "
						+ "SET CITY = 'Greenmount';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for update/write "
							+ "access to TABLE STAFF3";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// verify update didn't take
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM vSTAFF3 "
					+ "WHERE CITY = 'Greenmount' OR GRADE = 15;");
			rs.next();
			assertEquals(0, rs.getInt(1));

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO vSTAFF3 "
						+ "VALUES ('E6','Mickey',12,'Nice');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					assertEquals(
							"Shouldn't be able to update this table/column",
							335544352, sqle.getErrorCode());
				}
			}

			// verify insert didn't take
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM vSTAFF3 ");
			rs.next();
			assertEquals(5, rs.getInt(1));

			// delete should fail
			try {
				stmtUser2.executeUpdate("DELETE FROM vSTAFF3;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					assertEquals(
							"Shouldn't be able to update this view",
							335544352, sqle.getErrorCode());
				}
			}
			// verify delete didn't take
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM vSTAFF3 ");
			rs.next();
			assertEquals(5, rs.getInt(1));

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantAllOnViewButNotTable
	 * 
	 * Notes: SDL_0033
	 *  
	 */
	public void testGrantAllOnViewButNotTable() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE BASE_VS1 (C1 INT, C2 INT);");
			stmtUser1.executeUpdate("CREATE VIEW VS1 AS "
					+ "SELECT * FROM BASE_VS1 WHERE C1 = 0;");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,1);");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (1,0);");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,0);");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (1,1);");

			stmtUser1.executeUpdate("GRANT ALL PRIVILEGES on vs1 to user2");

			// user2 has all privileges on the view, but none on the base table.

			stmtUser1.close();
			connUser1.close();

			// delete should work
			assertEquals(1, stmtUser2.executeUpdate("DELETE FROM VS1 "
					+ "WHERE C2 = 1;"));

			// select should work
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM VS1;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// insert should work
			assertEquals(1, stmtUser2.executeUpdate("INSERT INTO VS1 "
					+ "VALUES (0,7);"));

			// update should work
			assertEquals(1, stmtUser2.executeUpdate("UPDATE VS1 "
					+ "SET C2 = 8 " + "WHERE C2 = 7;"));

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

		// reconnect and repopulate the database....
		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("DELETE FROM BASE_VS1");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,1);");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (1,0);");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,0);");
			stmtUser1.executeUpdate("INSERT INTO BASE_VS1 VALUES (1,1);");

			stmtUser1.close();
			connUser1.close();

			// should not be able to modify base tables

			// delete shouldn't work
			try {
				stmtUser2.executeUpdate("DELETE FROM BASE_VS1;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for delete/write "
							+ "access to TABLE BASE_VS1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert shouldn't work
			try {
				stmtUser2.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,7);");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for insert/write "
							+ "access to TABLE BASE_VS1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update shouldn't work
			try {
				stmtUser2.executeUpdate("UPDATE BASE_VS1 SET C2 = 1;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for update/write "
							+ "access to TABLE BASE_VS1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// select shouldn't work
			try {
				stmtUser2.executeUpdate("SELECT COUNT(*) FROM BASE_VS1;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for read/select "
							+ "access to TABLE BASE_VS1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}

	}
	/*
	 * Name: testSelectRequiredForSearchedStatements
	 * 
	 * Notes: select required for searched select and searched update (from
	 * SDL_034).
	 *  
	 */
	public void testSelectRequiredForSearchedStatements() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE SRCH1 (C1 INT);");
			stmtUser1.executeUpdate("INSERT INTO SRCH1 VALUES (0);");
			stmtUser1.executeUpdate("INSERT INTO SRCH1 VALUES (1);");
			stmtUser1
					.executeUpdate("GRANT INSERT, UPDATE, DELETE ON SRCH1 TO user2 ");

			stmtUser1.close();
			connUser1.close();

			// user2 doesn't have select privileges

			// this update should fail, since there is a select clause
			try {
				stmtUser2.executeUpdate("UPDATE SRCH1 "
						+ "SET C1 = 2 WHERE C1 = 0;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for read/select "
							+ "access to TABLE SRCH1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// this delete should fail, since there is a select clause
			try {
				stmtUser2.executeUpdate("DELETE FROM SRCH1 " + "WHERE C1 = 0;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for read/select "
							+ "access to TABLE SRCH1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantOptionsOnUser2ButNotUser3
	 * 
	 * Notes: strange cases with 3-user setups (from SDL_034).
	 *  
	 */
	public void testGrantOptionsOnUser2ButNotUser3() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;
		Connection connUser3 = null;
		Statement stmtUser3 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();
			connUser3 = DriverManager.getConnection(getUrl(), "User3",
					"masterkey");
			stmtUser3 = connUser3.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE BADG1 (C1 INT);");
			stmtUser1.executeUpdate("CREATE TABLE BADG2 (C1 INT);");
			stmtUser1
					.executeUpdate("CREATE VIEW VBADG2 AS SELECT * FROM BADG2;");
			stmtUser1.executeUpdate("INSERT INTO BADG1 VALUES (2);");
			stmtUser1.executeUpdate("INSERT INTO BADG2 VALUES (2);");
			stmtUser1.executeUpdate("GRANT all privileges on badg1 to user2 ");
			stmtUser1
					.executeUpdate("GRANT SELECT ON VBADG2 TO user2 WITH GRANT OPTION;");

			stmtUser1.close();
			connUser1.close();

			// user3 has not privileges on badg1. select should fail
			try {
				stmtUser3.executeQuery("SELECT COUNT(*) FROM BADG1;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for read/select "
							+ "access to TABLE BADG1";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// user3 has not privileges on badg2. select should fail
			try {
				stmtUser3.executeQuery("SELECT COUNT(*) FROM BADG2;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for read/select "
							+ "access to TABLE BADG2";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser3.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantOnlySelectOnView
	 * 
	 * Notes: Select test from SDL_0029
	 *  
	 */
	public void testGrantOnlySelectOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE AA (CHARTEST CHAR(20));");
			stmtUser1.executeUpdate("CREATE VIEW VAA AS SELECT * FROM AA;");
			stmtUser1
					.executeUpdate("INSERT INTO AA VALUES('Twenty Characters...');");
			stmtUser1.executeUpdate("GRANT SELECT ON VAA TO user2");

			// GRANT SELECT ON VAA TO SCHANZLE;

			stmtUser1.close();
			connUser1.close();

			// select should work
			rs = stmtUser2.executeQuery("select chartest from vaa");
			rs.next();
			assertEquals("Twenty Characters...", rs.getString(1));
			assertFalse(rs.next());

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO vaa "
						+ "VALUES ('this should not work');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE VAA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM vAA;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE VAA  "
						+ "SET CHARTEST = 'This should not work';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE VAA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM VAA;");
			rs.next();
			assertEquals(1, rs.getInt(1));

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM VAA;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE VAA";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// verify only 1 record in table
			rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM VAA;");
			rs.next();
			assertEquals(1, rs.getInt(1));
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantOnlyInsertOnView
	 * 
	 * Notes: INSERT test from SDL_0035
	 *  
	 */
	public void testGrantOnlyInsertOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");
			stmtUser1.executeUpdate("CREATE VIEW vbb AS SELECT * FROM bb;");
			stmtUser1.executeUpdate("GRANT Insert ON vbb TO user2");

			stmtUser1.close();
			connUser1.close();

			// insert should work
			assertEquals(1, stmtUser2
					.executeUpdate("INSERT INTO vBB VALUES ('A');"));

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from vbb");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE VBB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("update vbb set chartest = 'b';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE VBB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM vbb;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE VBB";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyUpdateOnView
	 * 
	 * Notes: UPDATE test from SDL_0035
	 *  
	 */
	public void testGrantOnlyUpdateOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1
					.executeUpdate("CREATE TABLE CC (CHARTEST CHARACTER(20));");
			stmtUser1
					.executeUpdate("INSERT INTO cc VALUES('Twenty Characters...');");
			stmtUser1.executeUpdate("create view vcc as select * from cc;");
			stmtUser1.executeUpdate("GRANT Update ON vcc TO user2");

			stmtUser1.close();
			connUser1.close();

			// update should work
			stmtUser2.executeUpdate("UPDATE vcc  "
					+ "SET CHARTEST = 'This --should-- work';");

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from vcc");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE VCC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO vcc "
						+ "VALUES ('this should not work');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE VCC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// delete should also fail
			try {
				stmtUser2.executeUpdate("DELETE FROM vcc;");
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for delete/write access to TABLE VCC";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

	/*
	 * Name: testGrantOnlyDeleteOnView
	 * 
	 * Notes: DELETE test from SDL_0035
	 *  
	 */
	public void testGrantOnlyDeleteOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE dd (CHARTEST character);");
			stmtUser1.executeUpdate("create view vdd as select * from dd;");
			stmtUser1.executeUpdate("INSERT INTO DD VALUES('a');");
			stmtUser1.executeUpdate("GRANT delete ON vdd TO user2");

			stmtUser1.close();
			connUser1.close();

			// select should fail
			try {
				rs = stmtUser2.executeQuery("select chartest from vdd");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE VDD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO vdd " + "VALUES ('e');");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE VDD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE vdd  " + "SET CHARTEST = 'e';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE VDD";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}
			// delete should work
			stmtUser2.executeUpdate("DELETE FROM vdd;");

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testIndividualWithoutAnyPrivilegesOnView
	 * 
	 * Notes: Individual with no privileges on view test from SDL_0036
	 *  
	 */
	public void testIndividualWithoutAnyPrivilegesOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE VTABLE " + "(COL1  INTEGER, "
					+ "COL2  INTEGER, " + "COL3  INTEGER, " + "COL4  INTEGER, "
					+ "COL5  DECIMAL(7,2));");
			stmtUser1
					.executeUpdate("create view vvtable as select * from vtable");

			// user2 gets NO privileges for this test

			stmtUser1.close();
			connUser1.close();

			// delete should fail
			try {
				stmtUser2.executeUpdate("DELETE FROM vvtable;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. "
							+ "no permission for delete/write "
							+ "access to TABLE VVTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// insert should fail
			try {
				stmtUser2.executeUpdate("INSERT INTO vvtable "
						+ "VALUES (0,1,2,3,4.25);");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for insert/write access to TABLE VVTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// select should fail
			try {
				rs = stmtUser2
						.executeQuery("select col2 from vvtable where col2 = 0");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE VVTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

			// update should fail
			try {
				stmtUser2.executeUpdate("UPDATE vvtable  " + "SET col2 = 2;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for update/write access to TABLE VVTABLE";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	} /*
	   * Name: testGrantAllPrivilegesToIndividualOnView
	   * 
	   * Notes: Individual with ALL privileges test from SDL_0036
	   *  
	   */
	public void testGrantAllPrivilegesToIndividualOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("  CREATE TABLE II (C1 INT); ");
			stmtUser1.executeUpdate("create view vii as select * from ii");
			stmtUser1.executeUpdate("  grant all privileges on vII to user2; ");

			// user2 gets ALL privileges for this test

			stmtUser1.close();
			connUser1.close();

			// insert should work
			assertEquals(1, stmtUser2
					.executeUpdate("INSERT INTO VII VALUES (2);"));

			// update should work
			assertEquals(1, stmtUser2
					.executeUpdate("UPDATE VII SET C1 = 3 WHERE C1 = 2;"));

			// select should work
			rs = stmtUser2.executeQuery("select c1 from VII");
			rs.next();
			assertEquals(3, rs.getInt(1));

			// delete should work
			assertEquals(1, stmtUser2.executeUpdate("DELETE FROM VII;"));

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testGrantUpdateNotGrantableOnView
	 * 
	 * Notes: From SDL_0036
	 *  
	 */
	public void testGrantUpdateNotGrantableOnView() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE STAFF3 ("
					+ "EMPNUM CHAR(3) NOT NULL," + " EMPNAME CHAR(20), "
					+ " GRADE DECIMAL(4), " + "CITY CHAR(15));");
			stmtUser1
					.executeUpdate("create view vstaff3 as select * from staff3");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E1','Alice',12,'Deale');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E2','Betty',10,'Vienna');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E3','Carmen',13,'Vienna');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E4','Don',12,'Deale');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E5','Ed',13,'Akron');");

			// user2 gets ALL privileges for this test

			stmtUser1.close();
			connUser1.close();

			// user2 doesn't have privileges on staff3 or vstaff3
			try {
				stmtUser2
						.executeUpdate("GRANT SELECT, UPDATE ON VSTAFF3 TO user3;");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
					assertEquals(
							"GDS Exception. 335544351. unsuccessful metadata update\n"
									+ "no S privilege with grant option on table/view VSTAFF3",
							sqle.getMessage());

			}

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testIllegalGrantToSelf()
	 * 
	 * Notes: From SDL_0037
	 *  
	 */
	public void testIllegalGrantToSelf() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE STAFF3 ("
					+ "EMPNUM CHAR(3) NOT NULL," + " EMPNAME CHAR(20), "
					+ " GRADE DECIMAL(4), " + "CITY CHAR(15));");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E1','Alice',12,'Deale');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E2','Betty',10,'Vienna');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E3','Carmen',13,'Vienna');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E4','Don',12,'Deale');");
			stmtUser1.executeUpdate("INSERT INTO STAFF3 "
					+ "VALUES ('E5','Ed',13,'Akron');");

			stmtUser1.close();
			connUser1.close();

			// user2 shouldn't be able to grant self privileges
			try {
				stmtUser2.executeUpdate("GRANT SELECT,UPDATE ON "
						+ "STAFF3 TO USER2 WITH GRANT OPTION");

				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql"))
					assertEquals(
							"GDS Exception. 335544351. unsuccessful metadata update\n"
									+ "no S privilege with grant option on table/view STAFF3",
							sqle.getMessage());
			}
			// user2 REALLY shouldn't be able to do an update
			try {
				stmtUser2.executeUpdate("UPDATE STAFF3 "
						+ "SET GRADE = 15 WHERE EMPNUM = 'E2';");
				fail();
			} catch (SQLException sqle) {
				if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
					String FBError = "GDS Exception. 335544352. no permission for read/select access to TABLE STAFF3";
					assertEquals(FBError, sqle.getMessage().substring(0,
							FBError.length()));
				}
			}

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	} /*
	   * Name: testDml_139_DropBehaviourOnRevoke
	   *  
	   */
	public void testDml_139_DropBehaviourOnRevoke() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE WORKS "
					+ "(EMPNUM CHAR(3) NOT NULL, " + "PNUM CHAR(3) NOT NULL, "
					+ "HOURS DECIMAL(5), " + "UNIQUE(EMPNUM,PNUM));");

			stmtUser1.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM   CHAR(3) NOT NULL UNIQUE, "
					+ "EMPNAME  CHAR(20), " + "GRADE    DECIMAL(4), "
					+ "CITY     CHAR(15));");

			stmtUser1.executeUpdate("CREATE TABLE PROJ "
					+ "(PNUM     CHAR(3) NOT NULL UNIQUE, "
					+ "PNAME    CHAR(20), " + "PTYPE    CHAR(6), "
					+ "BUDGET   DECIMAL(9), " + "CITY     CHAR(15));");

			stmtUser1.executeUpdate("GRANT ALL PRIVILEGES ON STAFF TO PUBLIC;");
			stmtUser1.executeUpdate("GRANT SELECT ON WORKS TO PUBLIC;");
			stmtUser1.executeUpdate("GRANT SELECT ON PROJ TO PUBLIC;");
			stmtUser1.executeUpdate("GRANT SELECT,UPDATE ON WORKS "
					+ "TO USER2 WITH GRANT OPTION;");

			stmtUser2.executeUpdate("GRANT SELECT,UPDATE ON WORKS "
					+ "TO user3;");

			if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
				// firebird doesn't support revoke restrict
				stmtUser1
						.executeUpdate("REVOKE GRANT OPTION FOR SELECT ON WORKS FROM USER2 ;");
				// PASS:0699 If ERROR, syntax error/access violation?
				// note - without restrict it works

				stmtUser1.executeUpdate("REVOKE GRANT OPTION FOR SELECT "
						+ "ON WORKS FROM USER2 ;");
				// PASS:0699 If successful completion?

				stmtUser1.executeUpdate("REVOKE SELECT ON WORKS "
						+ "FROM PUBLIC ;");
				// PASS:0699 If successful completion?

				stmtUser2.executeUpdate("REVOKE SELECT ON STAFF "
						+ "FROM PUBLIC ;");
				// PASS:0699 If ERROR, syntax error/access violation?
				// this now works without the revoke / restrict

				stmtUser2.executeUpdate("REVOKE SELECT ON PROJ "
						+ "FROM PUBLIC ;");
				// PASS:0699 If successful completion?
			} else {
				try {
					stmtUser1
							.executeUpdate("REVOKE GRANT OPTION FOR SELECT ON WORKS FROM USER2 RESTRICT;");
					fail();
					// PASS:0699 If ERROR, syntax error/access violation?
				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}

				stmtUser1.executeUpdate("REVOKE GRANT OPTION FOR SELECT "
						+ "ON WORKS FROM USER2 CASCADE;");
				// PASS:0699 If successful completion?

				stmtUser1.executeUpdate("REVOKE SELECT ON WORKS "
						+ "FROM PUBLIC RESTRICT;");
				// PASS:0699 If successful completion?

				try {
					stmtUser2.executeUpdate("REVOKE SELECT ON STAFF "
							+ "FROM PUBLIC RESTRICT;");
					// PASS:0699 If ERROR, syntax error/access violation?
					fail();
				} catch (SQLException sqle) {
				}

				stmtUser2.executeUpdate("REVOKE SELECT ON PROJ "
						+ "FROM PUBLIC RESTRICT;");
				// PASS:0699 If successful completion?
			}
			// END TEST >>> 0699 <<< END TEST
			stmtUser1.close();
			connUser1.close();

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testDml_142_PrivilegeViolation
	 *  
	 */
	public void testDml_142_PrivilegeViolation() throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");

			try {
				rs = stmtUser2.executeQuery("SELECT COUNT(*) FROM USIG;");
				fail();
				// PASS:0527 If ERROR, syntax error/access violation, 0 rows
				// selected?
			} catch (SQLException sqle) {
			}

			try {
				stmtUser2.executeUpdate("INSERT INTO USIG VALUES (2, 4);");
				fail();
				// PASS:0527 If ERROR, syntax error/access violation, 0 rows
				// inserted?
			} catch (SQLException sqle) {
			}

			try {
				stmtUser2.executeUpdate("UPDATE USIG SET C1 = 0;");
				fail();
				// PASS:0527 If ERROR, syntax error/access violation, 0 rows
				// updated?
			} catch (SQLException sqle) {
			}

			try {
				stmtUser2.executeUpdate("DELETE FROM USIG;");
				// PASS:0527 If ERROR, syntax error/access violation, 0 rows
				// deleted?
				fail();
			} catch (SQLException sqle) {
			}

			// END TEST >>> 0527 <<< END TEST
			stmtUser1.close();
			connUser1.close();

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
	/*
	 * Name: testDml_143_PrivilegeViolationSelectInInsertStatement
	 *  
	 */
	public void testDml_143_PrivilegeViolationSelectInInsertStatement()
			throws SQLException {
		Connection connUser1 = null;
		Statement stmtUser1 = null;
		Connection connUser2 = null;
		Statement stmtUser2 = null;

		try {
			connUser1 = DriverManager.getConnection(getUrl(), "User1",
					"masterkey");
			stmtUser1 = connUser1.createStatement();
			connUser2 = DriverManager.getConnection(getUrl(), "User2",
					"masterkey");
			stmtUser2 = connUser2.createStatement();

			stmtUser1.executeUpdate("CREATE TABLE CONCATBUF (ZZ CHAR(240));");
			stmtUser1.executeUpdate("CREATE TABLE BASE_TESTREPORT "
					+ "(TESTNO   CHAR(4), " + "RESULT   CHAR(4), "
					+ "TESTTYPE  CHAR(3));");

			stmtUser1.executeUpdate("CREATE VIEW TESTREPORT AS "
					+ "SELECT TESTNO, RESULT, TESTTYPE "
					+ "FROM BASE_TESTREPORT;");

			stmtUser1.executeUpdate("GRANT INSERT ON TESTREPORT TO PUBLIC "
					+ "WITH GRANT OPTION;");

			try {
				stmtUser2.executeUpdate("INSERT INTO CONCATBUF "
						+ "SELECT TESTNO FROM TESTREPORT;");
				fail();
				// PASS:0527 If ERROR, syntax error/access violation, 0 rows
				// selected?
			} catch (SQLException sqle) {
			}

			stmtUser1.close();
			connUser1.close();

		} finally {
			try {
				connUser1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				connUser2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}
}