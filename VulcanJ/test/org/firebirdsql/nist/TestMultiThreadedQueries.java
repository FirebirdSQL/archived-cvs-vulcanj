/* $Id$ */
/*
 * Author: bioliv Created on: Sept. 14, 2004
 * 
 * These tests are based on the mp* series of the NIST tests. In that series,
 * the user runs, in order, the mpaxxxi initialization routine, alternates the
 * mpaxxxr/mpaxxxr run routines, and follows with the mapxxxt termination
 * routine.
 * 
 * These tests make heavy use of commit/rollback and require a transactional
 * store. These tests should not be run against SPD/BASE data sets.
 * 
 * Junit doesn't handle multi-threaded tests very well out of the box. To get
 * around this limitation, we're using the GroboUtils package. In a nutshell,
 * Junit doesn't wait for the threads to finish before completing its test. See
 * http://today.java.net/pub/a/today/2003/08/06/multithreadedTests.html, and the
 * groboutils home page, http://groboutils.sourceforge.net.
 *  
 */
package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

public class TestMultiThreadedQueries extends NistTestBase {
	private Connection conn1;
	private Connection conn2;
	private Statement stmt1;
	private Statement stmt2;
	private ResultSet rs1;
	private ResultSet rs2;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public TestMultiThreadedQueries(String arg0) {
		super(arg0);
	}

	/*
	 * 
	 * private test classes and testXXX routines follow
	 *  
	 */

	/*
	 * 
	 * @author bioliv, Sep 16, 2004
	 *  
	 */

	private class TransactionSerializablePhantomReadA extends TestRunnable {
		Statement st;
		Connection cn;
		private TransactionSerializablePhantomReadA(Connection conn,
				Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {

			// try the delete until no exception is thrown
			for (int i = 0; i < 1; i++) {
				try {
					st.executeUpdate("DELETE FROM AA "
							+ "WHERE ANUM BETWEEN 1 AND 5;");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}

			}
			// try the insert until no exception is thrown
			for (int i = 0; i < 1; i++) {
				try {
					st.executeUpdate("INSERT INTO AA VALUES (1)");
					st.executeUpdate("INSERT INTO AA VALUES (2)");
					st.executeUpdate("INSERT INTO AA VALUES (3)");
					st.executeUpdate("INSERT INTO AA VALUES (4)");
					st.executeUpdate("INSERT INTO AA VALUES (5)");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}

			}
			// try the 2nd delete until no exception is thrown
			for (int i = 0; i < 1; i++) {
				try {
					st
							.executeUpdate("DELETE FROM AA WHERE ANUM BETWEEN 6 AND 10");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}

			}
			// try the 2nd insert until no exception is thrown
			for (int i = 0; i < 1; i++) {
				try {
					st.executeUpdate("INSERT INTO AA VALUES (6)");
					st.executeUpdate("INSERT INTO AA VALUES (7)");
					st.executeUpdate("INSERT INTO AA VALUES (8)");
					st.executeUpdate("INSERT INTO AA VALUES (9)");
					st.executeUpdate("INSERT INTO AA VALUES (10)");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}

			}
		}
	}
	private class TransactionSerializablePhantomReadB extends TestRunnable {
		Statement st;
		Connection cn;
		ResultSet rs;
		private TransactionSerializablePhantomReadB(Connection conn,
				Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 6; i++) {
				try {
					rs = st.executeQuery("SELECT * FROM AA;");
					int rowCount = 0;
					while (rs.next())
						rowCount++;
					// System.out.println ("rowcount="+rowCount);
					assertTrue((20 == rowCount) || (25 == rowCount));
					// PASS:0457 If 20 rows selected or 25 rows selected?
					// PASS:0457 or there are errors or warnings about deadlock?

					rs = st.executeQuery("SELECT COUNT(*) FROM AA;");
					rs.next();
					// next line will sometimes fail, unless we set transaction
					// isolation level to serializable. But, with that setting,
					// jaybird will hang.
					// assertTrue((20 == rs.getInt(1)) || (25 == rs.getInt(1)));
					// PASS:0457 If count = 20 or count = 25 ?
					// PASS:0457 or there are errors or warnings about deadlock?

				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}
			}
		}
	}
	public void _testTransactionSerializablePhantomRead() throws SQLException,
			Throwable, ClassNotFoundException {

		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			// conn1.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			// conn2.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			// stmt2 = conn2.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			// ResultSet.TYPE_SCROLL_SENSITIVE);
			stmt2 = conn2.createStatement();
			// initialization code from mpa005i
			stmt1.executeUpdate("CREATE TABLE AA (ANUM NUMERIC(4));");
			for (int i = 0; i < 25; i++) {
				stmt1.executeUpdate("insert into aa values (" + (i + 1) + ");");
			}
			conn1.close();
			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			//instantiate the TestRunnable classes
			TestRunnable tr1, tr2, tr3;
			tr1 = new TransactionSerializablePhantomReadA(conn1, stmt1);
			tr2 = new TransactionSerializablePhantomReadB(conn2, stmt2);
			//pass that instance to the MTTR
			TestRunnable[] trs = {tr1, tr2};
			MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

			//kickstarts the MTTR & fires off threads
			mttr.runTestRunnables();

			// verify results after we come back from threading work...
			ResultSet rs = stmt1.executeQuery("select anum from aa");
			int rowCount = 0;
			while (rs.next())
				rowCount++;
			assertEquals(25, rowCount);

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	}
	private class TransactionSerializableA extends TestRunnable {
		Statement st;
		private TransactionSerializableA(Statement st) {
			this.st = st;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 20; i++) {
				ResultSet rs = st.executeQuery("SELECT Max(anum) from tt");
				rs.next();
				int myMax = rs.getInt(1);
				st.executeUpdate("INSERT INTO TT VALUES (9," + (myMax + 1)
						+ ");");
				//				System.out.println("A: " + i);
			}
		}
	}
	private class TransactionSerializableB extends TestRunnable {
		Statement st;
		private TransactionSerializableB(Statement st) {
			this.st = st;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 20; i++) {
				ResultSet rs = st.executeQuery("SELECT Max(anum) from tt");
				rs.next();
				int myMax = rs.getInt(1);
				st.executeUpdate("INSERT INTO TT VALUES (999," + (myMax + 1)
						+ ");");
				//				System.out.println("B: " + i);
			}
		}
	}

	public void testTransactionsSerializableAssignSequentialKey()
			throws SQLException, ClassNotFoundException {

		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			stmt1
					.executeUpdate("CREATE TABLE TT (DOLLARS NUMERIC(4), ANUM NUMERIC(4));");

			//instantiate the TestRunnable classes
			TestRunnable tr1, tr2, tr3;
			tr1 = new TransactionSerializableA(stmt1);
			tr2 = new TransactionSerializableB(stmt2);
			//pass that instance to the MTTR
			TestRunnable[] trs = {tr1, tr2};
			MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

			//kickstarts the MTTR & fires off threads
			try {
				mttr.runTestRunnables();

			} catch (Throwable t) {
				System.out.println(t.getMessage());
			}

			ResultSet rs = stmt1.executeQuery("select count(*) from tt");
			rs.next();
			assertEquals(40, rs.getInt(1));
			rs.close();
		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	}

	private class TransactionSelectUpdate extends TestRunnable {
		Statement st;
		Connection cn;
		private TransactionSelectUpdate(Connection conn, Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {
			ResultSet rs;
			for (int i = 0; i < 5; i++) {
				try {
					rs = st
							.executeQuery("SELECT DOLLARS FROM TT WHERE ANUM = 25;");
					rs.next();
					double myVal = rs.getDouble(1);
					st.executeUpdate("update tt set dollars = " + (myVal + 10)
							+ " where anum=25");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}
			}
		}
	}

	public void testTransactionsSerializableSelectUpdate() throws SQLException,
			Throwable, ClassNotFoundException {

		if (System.getProperty("test.safe") != null)
			if (System.getProperty("test.safe").equalsIgnoreCase("true"))
				assertTrue("This test didn't run because safe mode was on.",
						false);
		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			// initialization code from mpa002i
			stmt1.executeUpdate("CREATE TABLE TT ("
					+ "DOLLARS NUMERIC(4), ANUM NUMERIC(4));");
			for (int i = 0; i < 50; i++) {
				stmt1
						.executeUpdate("INSERT INTO TT VALUES (500, " + (i)
								+ ");");
			}

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			//instantiate the TestRunnable classes
			TestRunnable tr1, tr2, tr3;
			tr1 = new TransactionSelectUpdate(conn1, stmt1);
			tr2 = new TransactionSelectUpdate(conn2, stmt2);
			//pass that instance to the MTTR
			TestRunnable[] trs = {tr1, tr2};
			MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

			//kickstarts the MTTR & fires off threads
			mttr.runTestRunnables();

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	} /*
	   * 
	   * @author bioliv, Sep 15, 2004
	   * 
	   * Notes - these tests spawn off 2 threads, both updating the same table
	   * with arithmetic expressions.
	   *  
	   */

	private class TransactionSerializableUpdateWithArithmeticA
			extends
				TestRunnable {
		Statement st;
		Connection cn;
		private TransactionSerializableUpdateWithArithmeticA(Connection conn,
				Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 5; i++) {
				try {
					st.executeUpdate("UPDATE TT SET DOLLARS = DOLLARS + 5 "
							+ "WHERE ANUM = 1;");
					st.executeUpdate("UPDATE TT SET DOLLARS = DOLLARS - 5 "
							+ "WHERE ANUM = 25;");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}
			}
		}
	}
	private class TransactionSerializableUpdateWithArithmeticB
			extends
				TestRunnable {
		Statement st;
		Connection cn;
		private TransactionSerializableUpdateWithArithmeticB(Connection conn,
				Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 5; i++) {
				try {
					st.executeUpdate("UPDATE TT SET DOLLARS = DOLLARS + 10 "
							+ "WHERE ANUM = 50;");
					st.executeUpdate("UPDATE TT SET DOLLARS = DOLLARS - 10 "
							+ "WHERE ANUM = 25;");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}
			}
		}
	}
	public void testTransactionsSerializableSelectUpdateWithArithmetic()
			throws SQLException, Throwable, ClassNotFoundException {

		if (System.getProperty("test.safe") != null)
			if (System.getProperty("test.safe").equalsIgnoreCase("true"))
				assertTrue("This test didn't run because safe mode was on.",
						false);
		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			// initialization code from mpa002i
			stmt1
					.executeUpdate("CREATE TABLE TT (DOLLARS NUMERIC(4), ANUM NUMERIC(4));");
			for (int i = 0; i < 50; i++) {
				stmt1
						.executeUpdate("INSERT INTO TT VALUES (500, " + (i)
								+ ");");
			}

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			//instantiate the TestRunnable classes
			TestRunnable tr1, tr2, tr3;
			tr1 = new TransactionSerializableUpdateWithArithmeticA(conn1, stmt1);
			tr2 = new TransactionSerializableUpdateWithArithmeticB(conn2, stmt2);
			//pass that instance to the MTTR
			TestRunnable[] trs = {tr1, tr2};
			MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

			//kickstarts the MTTR & fires off threads
			mttr.runTestRunnables();

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	}

	private class TransactionSerializabilityDeadlockManagementA
			extends
				TestRunnable {
		Statement st;
		Connection cn;
		private TransactionSerializabilityDeadlockManagementA(Connection conn,
				Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 50; i++) {
				try {
					st.executeUpdate("UPDATE AA SET ANUM = ANUM + 3; ");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}
			}
			for (int i = 0; i < 4; i++) {
				try {
					st.executeUpdate("UPDATE BB SET BNUM = BNUM + 5; ");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";
						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}

			}
		}
	}
	private class TransactionSerializabilityDeadlockManagementB
			extends
				TestRunnable {
		Statement st;
		Connection cn;
		ResultSet rs;
		private TransactionSerializabilityDeadlockManagementB(Connection conn,
				Statement st) {
			this.st = st;
			this.cn = conn;
		}

		public void runTest() throws Throwable {
			for (int i = 0; i < 50; i++) {
				try {
					st.executeUpdate("UPDATE BB SET BNUM = BNUM - 3; ");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;
					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";

						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}
			}
			for (int i = 0; i < 4; i++) {
				try {
					st.executeUpdate("UPDATE AA SET ANUM = ANUM - 5; ");
					cn.commit();
				} catch (SQLException sqle) {
					cn.rollback();
					i -= 1;

					if (DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
						String fbError = "GDS Exception. 335544336. deadlock\n"
								+ "update conflicts with concurrent update";

						assertEquals(fbError, sqle.getMessage().substring(0,
								fbError.length()));
					}
				}

			}
		}
	}
	public void testTransactionSerializabilityDeadlockManagement()
			throws SQLException, Throwable, ClassNotFoundException {

		if (System.getProperty("test.safe") != null)
			if (System.getProperty("test.safe").equalsIgnoreCase("true"))
				assertTrue("This test didn't run because safe mode was on.",
						false);
		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			// initialization code from mpa004i
			stmt1.executeUpdate("CREATE TABLE AA (ANUM NUMERIC(4));");
			stmt1.executeUpdate("CREATE TABLE BB (BNUM NUMERIC(4));");
			stmt1.executeUpdate("INSERT INTO AA VALUES (1)");
			stmt1.executeUpdate("INSERT INTO BB VALUES (100)");

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			//instantiate the TestRunnable classes
			TestRunnable tr1, tr2, tr3;
			tr1 = new TransactionSerializabilityDeadlockManagementA(conn1,
					stmt1);
			tr2 = new TransactionSerializabilityDeadlockManagementB(conn2,
					stmt2);
			//pass that instance to the MTTR
			TestRunnable[] trs = {tr1, tr2};
			MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

			//kickstarts the MTTR & fires off threads
			mttr.runTestRunnables();

			// verify results after we come back from threading work...
			ResultSet rs = stmt1.executeQuery("select anum from aa");
			rs.next();
			assertEquals(131, (int) rs.getDouble(1));

			rs = stmt1.executeQuery("select bnum from bb");
			rs.next();
			assertEquals(-30, (int) rs.getDouble(1));

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	}

	/*
	 * 
	 * This "multi-threaded" test is easier to write, since it has user a doing
	 * some work, then userb, then usera. So, we can do all of this on one
	 * thread, but using 2 different connections.
	 * 
	 * 2005/04/01 works with vulcan, fails with FB1.5/FB2.0, so removing from
	 * runs.
	 *  
	 */
	public void _testTransactionRollbackSerialization() throws SQLException,
			Throwable, ClassNotFoundException {

		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			// initialization code from mpa006i
			stmt1.executeUpdate("CREATE TABLE AA (ANUM NUMERIC(4));");
			stmt1.executeUpdate("CREATE TABLE BB (BNUM NUMERIC(4));");
			stmt1.executeUpdate("CREATE TABLE TTT ("
					+ "ANUM NUMERIC(4) NOT NULL UNIQUE, AUTHOR CHAR(1));");
			stmt1.executeUpdate("CREATE TABLE TT ("
					+ "DOLLARS NUMERIC(4), ANUM NUMERIC(4));");
			stmt1.executeUpdate("INSERT INTO AA VALUES (1)");
			stmt1.executeUpdate("INSERT INTO BB VALUES (100)");

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			// from mpa006r
			stmt1.executeUpdate("insert into tt values (1, 1);");
			double aaVal = 0;
			ResultSet rs1 = stmt1.executeQuery("select anum from aa");
			rs1.next();
			aaVal = rs1.getDouble(1);
			stmt1.executeUpdate("update aa set anum = anum + 150");

			// from mpb006r
			stmt2.executeUpdate("INSERT INTO TTT VALUES (2, 'B');");
			ResultSet rs2 = stmt2.executeQuery("select bnum from bb");
			rs2.next();
			double bbVal = rs2.getDouble(1);
			stmt2.executeUpdate("UPDATE bb set bnum = bnum - 150");
			conn2.rollback(); // required or FB hangs.

			// from mpa006r
			rs1 = stmt1.executeQuery("SELECT BNUM  FROM BB;");
			stmt1.executeUpdate("UPDATE BB SET BNUM = BNUM + 20;");
			rs1 = stmt1.executeQuery("SELECT ANUM FROM AA;");
			rs1.next();
			assertEquals(151, rs1.getInt(1));
			rs1 = stmt1.executeQuery("SELECT BNUM FROM BB;");
			rs1.next();
			assertEquals(120, rs1.getInt(1));
			conn1.rollback(); // required or fb hangs...

			// from mpb006r
			rs2 = stmt1.executeQuery("SELECT aNUM  FROM aa;");
			stmt2.executeUpdate("UPDATE aa SET aNUM = aNUM - 20;");
			rs2 = stmt1.executeQuery("SELECT bNUM FROM bb;");
			rs2.next();
			assertEquals(100, rs2.getInt(1));
			rs2 = stmt2.executeQuery("SELECT aNUM FROM aa;");
			rs2.next();
			assertEquals(-19, rs2.getInt(1));

			conn1.commit();
			conn2.commit();

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	}

	public void testTransactionSerializabilityReadDeleteInsert()
			throws SQLException, Throwable, ClassNotFoundException {

		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			// initialization code from mpa007i
			stmt1.executeUpdate("CREATE TABLE AA (ANUM NUMERIC(4));");
			stmt1.executeUpdate("CREATE TABLE BB (BNUM NUMERIC(4));");
			stmt1.executeUpdate("INSERT INTO AA VALUES (1)");
			stmt1.executeUpdate("INSERT INTO BB VALUES (6)");

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			// from mpa007r
			double aaVal = 0;
			ResultSet rs1 = stmt1.executeQuery("select anum from aa");
			rs1.next();
			aaVal = rs1.getDouble(1);

			// from mpb007r
			ResultSet rs2 = stmt2.executeQuery("select bnum from bb");
			rs2.next();
			double bbVal = rs2.getDouble(1);

			// from mpa007r
			stmt1.executeUpdate("INSERT INTO BB VALUES (1);");
			stmt1.executeUpdate("DELETE FROM AA WHERE ANUM = 1;");

			// from mpb007r
			stmt2.executeUpdate("INSERT INTO AA VALUES (6);");
			stmt2.executeUpdate("DELETE FROM BB WHERE BNUM = 6;");

			// from mpa007r
			rs1 = stmt1.executeQuery("SELECT BNUM  FROM BB;");
			rs1.next();
			int bNum = rs1.getInt(1);
			assertEquals(6, rs1.getInt(1));

			// from mpb007r
			rs2 = stmt2.executeQuery("SELECT ANUM FROM AA;");
			rs2.next();
			assertEquals(1, rs2.getInt(1));
			int aNum = rs2.getInt(1);

			// from mpa007r
			// For each value of ?? above, move (insert/delete) the value:
			conn2.rollback(); // or firebird hangs.
			stmt1.executeUpdate("INSERT INTO AA VALUES (" + bNum + ");");
			stmt1.executeUpdate("DELETE FROM BB WHERE BNUM = " + bNum + ";");

			// from mpb007r
			// For each value of ?? above, move (insert/delete) the value:
			stmt1.executeUpdate("INSERT INTO bb VALUES (" + aNum + ");");
			stmt1.executeUpdate("DELETE FROM aa WHERE aNUM = " + aNum + ";");

			conn1.commit();
			conn2.commit();

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}

		}
	}
	public void testTransactionSerializabilityTwinsProblem()
			throws SQLException, Throwable, ClassNotFoundException {

		try {
			Class.forName(System.getProperty("test.driver.name",
					"org.firebirdsql.jdbc.FBDriver"));

			conn1 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt1 = conn1.createStatement();
			conn2 = DriverManager
					.getConnection(getUrl(), "sysdba", "masterkey");
			stmt2 = conn2.createStatement();

			// initialization code from mpa008i
			stmt1.executeUpdate("CREATE TABLE TTT ("
					+ "ANUM NUMERIC(4) NOT NULL UNIQUE, AUTHOR CHAR(1));");
			stmt1.executeUpdate("INSERT INTO TTT VALUES (1,'A');");
			stmt1.executeUpdate("INSERT INTO TTT VALUES (2,'A');");
			stmt1.executeUpdate("INSERT INTO TTT VALUES (3,'A');");
			stmt1.executeUpdate("INSERT INTO TTT VALUES (4,'A');");
			stmt1.executeUpdate("INSERT INTO TTT VALUES (5,'A');");

			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);
			// from mpb008r
			try {
				stmt2.executeUpdate("   INSERT INTO TTT VALUES (3,'B');");
				fail();
			} catch (SQLException sqle) {
				// dont care
			}
			conn2.commit();

			// from mpa008r
			ResultSet rs1 = stmt1
					.executeQuery("SELECT COUNT(*) FROM TTT WHERE ANUM = 3;");
			rs1.next();
			assertEquals(1, rs1.getInt(1));
			stmt1.executeUpdate("DELETE FROM TTT WHERE ANUM = 3;");
			conn1.rollback(); // required or next insert hangs.
			// from mpb008r
			// without above delete, the next insert will fail
			try {
				stmt2.executeUpdate("INSERT INTO TTT VALUES (3,'B');");
				fail();
			} catch (SQLException sqle) {
				conn2.rollback();
			}
			// conn2.commit();

			// from mpa008r
			try {
				stmt1.executeUpdate("INSERT INTO TTT VALUES (3,'A');");
				fail();
			} catch (SQLException sqle) {
				// this will fail
				conn1.rollback();
			}
			stmt1.executeUpdate("DELETE FROM TTT WHERE ANUM = 3;");
			conn1.rollback(); // or next insert fails

			try {
				stmt2.executeUpdate("INSERT INTO TTT VALUES (3,'B');");
				fail();
			} catch (SQLException sqle) {
				// primary key violation
				conn2.rollback();
			}

			try {
				stmt1.executeUpdate("INSERT INTO TTT VALUES (3,'A');");
				fail();
			} catch (SQLException sqle) {
				// primary key violation again
				conn1.rollback();
			}

			conn1.commit();
			conn2.commit();

		} catch (ClassNotFoundException cnfe) {
			System.err.println("COULD NOT FIND JDBC CLASS");
			System.err.println(cnfe.getMessage());
		} finally {
			try {
				conn1.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
			try {
				conn2.close();
			} catch (SQLException fsqle) {
				System.out.println(fsqle.getMessage());
			}
		}
	}

}

