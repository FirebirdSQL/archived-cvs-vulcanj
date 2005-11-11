/* $Id$ */
/*
 * $Id$
 */
package org.firebirdsql.nist;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;
import org.firebirdsql.gds.impl.GDSType;
import org.firebirdsql.management.FBManager;

public class NistTestBase extends TestCase {

	protected static final String DRIVER = System.getProperty(
			"test.driver.name", "org.firebirdsql.jdbc.FBDriver");
	protected static final String DB_HOST = System.getProperty("test.db.host",
			"localhost");
	protected static final int DB_PORT = Integer.parseInt(System.getProperty(
			"test.db.port", "3050"));
	protected static final String DB_DIR = System.getProperty("test.db.dir",
			"output/db");
	protected static final String DB_NAME = System.getProperty("test.db.name",
			"sqltest.fdb");
	protected static final String DB_USER = System.getProperty("test.db.user",
			"sysdba");
	protected static final String DB_PASSWORD = System.getProperty(
			"test.db.password", "masterkey");
	protected static final String DB_URL = System.getProperty("test.db.url");
	protected static final String DB_DIALECT = System.getProperty(
			"test.db.dialect", "firebirdsql");
	private FBManager fbManager = null;

	public NistTestBase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		System.out.println(getName() + ", " + getUrl());
		if ((DB_URL == null) || "none".equals(DB_URL)) {
			fbManager = new FBManager(getGdsType());
			fbManager.setServer(DB_HOST);
			fbManager.setPort(DB_PORT);
			fbManager.setType(getGdsType().toString());
			fbManager.start();
			fbManager.createDatabase(DB_DIR + "/" + DB_NAME, DB_USER,
					DB_PASSWORD);
		} else if (DB_URL.startsWith("jdbc:sastkts")) {
			// we could do specific things for SAS Table Server here
		}
	}

	protected void tearDown() throws Exception {
		String url = System.getProperty("test.db.url");
		if ((url == null) || "none".equals(url)) {
			fbManager.dropDatabase(getDatabasePath(), DB_USER, DB_PASSWORD);
			fbManager.stop();
			fbManager = null;
		} else if (url.startsWith("jdbc:sastkts")) {
			// we could do specific things for SAS Table Server here
		}
	}
	private static final Map gdsTypeToUrlPrefixMap = new HashMap();
	static {
		gdsTypeToUrlPrefixMap.put(GDSType.getType("PURE_JAVA"),
				"jdbc:firebirdsql:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("EMBEDDED"),
				"jdbc:firebirdsql:embedded:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("NATIVE"),
				"jdbc:firebirdsql:native:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("ORACLE_MODE"),
				"jdbc:firebirdsql:oracle:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("LOCAL"),
				"jdbc:firebirdsql:local:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("NIO"),
				"jdbc:firebirdsql:nio:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("VULCAN_EMBEDDED"),
				"jdbc:vulcan:embedded:");
		gdsTypeToUrlPrefixMap.put(GDSType.getType("VULCAN"), "jdbc:vulcan:");

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
		if ((DB_URL == null) || "none".equals(DB_URL)) {
			// firebird 
			testUrl = gdsTypeToUrlPrefixMap.get(getGdsType())
			+ getdbpath(DB_NAME);
		} else {
			testUrl = DB_URL;
		}
		System.out.println(testUrl);
		return testUrl;
	}

	protected static GDSType getGdsType() {
		final GDSType gdsType = GDSType.getType(System.getProperty(
				"test.gds_type", "PURE_JAVA"));
		if (gdsType == null)
			throw new RuntimeException(
					"Unrecoginzed value for 'test.gds_type' property.");

		return gdsType;
	}
	protected static String getdbpath(String name) {
		if (getGdsType().toString().indexOf("EMBEDDED") > 0)
			return DB_DIR + "/" + name;
		else if ("LOCAL".equalsIgnoreCase(System.getProperty("test.gds_type")))
			return DB_DIR + "/" + name;
		else
			return DB_HOST + "/" + DB_PORT + ":" + DB_DIR + "/" + name;
	}

	protected static String getDatabasePath() {
		return DB_DIR + "/" + DB_NAME;
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

