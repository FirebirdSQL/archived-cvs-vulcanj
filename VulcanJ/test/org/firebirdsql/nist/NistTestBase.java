/* $Id$ */
/* $Id: NistTestBase
 * .java,v 1.21 2005/01/27 19:12:20 bioliv Exp $ */
package org.firebirdsql.nist;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import junit.framework.TestCase;

import org.firebirdsql.gds.GDSType;
import org.firebirdsql.management.FBManager;

public class NistTestBase extends TestCase {

	protected static final String DRIVER = System.getProperty(
			"test.driver.name", "org.firebirdsql.jdbc.FBDriver");
	protected static final String DB_DIALECT = System.getProperty(
			"test.db.dialect", "FirebirdSQL");
	protected static final String DB_HOST = System.getProperty("test.db.host",
			"localhost");
	protected static final String DB_PORT = System.getProperty("test.db.port",
			"3050");
	protected static final String DB_PATH = System.getProperty("test.db.path",
			"output/db");
	protected static final String DB_GDS_TYPE = System.getProperty(
			"test.db.gds_type", "EMBEDDED");
	protected static final String DB_NAME = System.getProperty("test.db.name",
			"sqltest.fdb");
	protected static final String DB_USER = System.getProperty("test.db.user",
			"sysdba");
	protected static final String DB_PASSWORD = System.getProperty(
			"test.db.password", "masterkey");

	private FBManager fbManager = null;

	public NistTestBase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		System.out.println(getName() + ", " + getUrl());
		// if a URL is supplied, we won't build the database.
		String url = System.getProperty("test.db.url");

		if ((url == null) || "none".equals(url)) {
			String dbPath = DB_PATH + "/" + DB_NAME;
			File f = new File(dbPath);
			String absDbPath = f.getAbsolutePath();

//			String host = System.getProperty("test.db.host", "localhost");
//			String port = System.getProperty("test.db.port", "3050");
//			String user = System.getProperty("test.db.user", "sysdba");
//			String password = System.getProperty("test.db.password",
//					"masterkey");

			fbManager = new FBManager(GDSType.getType(DB_GDS_TYPE));
			fbManager.setServer(DB_HOST);
			fbManager.setPort(Integer.parseInt(DB_PORT));
			fbManager.start();
			fbManager.createDatabase(absDbPath, DB_USER, DB_PASSWORD);

		} else if (url.startsWith("jdbc:sastkts")) {
			// we can do specific things for SAS Table Server here
		}

	}

	protected void tearDown() throws Exception {
		String url = System.getProperty("test.db.url");
		if ((url == null) || "none".equals(url)) {
			String dbPath = DB_PATH + "/" + DB_NAME;
			File f = new File(dbPath);
			String absDbPath = f.getAbsolutePath();

//			String host = System.getProperty("test.db.host", "localhost");
//			String port = System.getProperty("test.db.port", "3050");
//			String user = System.getProperty("test.db.user", "sysdba");
//			String password = System.getProperty("test.db.password",
//					"masterkey");
//
			fbManager.dropDatabase(absDbPath, DB_USER, DB_PASSWORD);
			fbManager.stop();
			fbManager = null;
		} else if (url.startsWith("jdbc:sastkts")) {
			// we can do specific things for SAS Table Server here
		}
	}

	protected Connection getConnectionViaDriverManager(String userid,
			String password) throws SQLException {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ex) {
			throw new SQLException("No suitable driver.");
		}

		return DriverManager.getConnection(getUrl(), userid, password);
	}

	protected Connection getConnectionViaDriverManager() throws SQLException {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ex) {
			throw new SQLException("No suitable driver.");
		}

		return DriverManager.getConnection(getUrl(), DB_USER, DB_PASSWORD);
	}

	public static String getUrl() {
		String testUrl;
		testUrl = System.getProperty("test.db.url");
		if ((testUrl == null) || "none".equals(testUrl)) {
			// not supplied, build our own.
			String dbPath = DB_PATH + "/" + DB_NAME;
			File f = new File(dbPath);
			String absDbPath = f.getAbsolutePath();
			String host = System.getProperty("test.db.host", "localhost");
			String port = System.getProperty("test.db.port", "3050");

			if ("EMBEDDED".equals(DB_GDS_TYPE))
				testUrl = "jdbc:firebirdsql:embedded:" + dbPath;
			else if ("PURE_JAVA".equals(DB_GDS_TYPE))
				testUrl = "jdbc:firebirdsql://" + host + "/" + absDbPath;
			else {
				throw new RuntimeException("Unrecoginzed value \""
						+ DB_GDS_TYPE + "\" for 'test.gds_type' property.");
			}
		}
		return testUrl;
	}

	protected Properties getDefaultPropertiesForConnection() {
		final Properties returnValue = new Properties();

		returnValue.setProperty("user", "user");
		returnValue.setProperty("password", "password");

		return returnValue;
	}

	class OnlySPD implements FilenameFilter {
		public boolean accept(File dir, String s) {
			if (s.endsWith(".spdd9"))
				return true;
			return false;
		}
	}
}

