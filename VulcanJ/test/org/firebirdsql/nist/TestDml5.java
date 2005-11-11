package org.firebirdsql.nist;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TestDml5 extends NistTestBase {
	private Connection conn; // Connection object.
	private Statement stmt; // Statement object.
	private ResultSet rs; // Result set.

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

	public TestDml5(String arg0) {
		super(arg0);
	}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_027
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml027.
//
// ORIGIN: NIST file sql/dml027.sql
//	
public void testDml_027() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);
	conn.setAutoCommit(false);

	// TEST:0124 UPDATE UNIQUE column (key = key + 1) interim conflict!

	// setup
	errorCode = 0;
	try {
		rowCount = stmt.executeUpdate("UPDATE UPUNIQ "
				+ "SET NUMKEY = NUMKEY + 1;");
	} catch (SQLException excep) {
		errorCode = excep.getErrorCode();
	} finally {
		if (errorCode != 335544665) {
			fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
					+ "INTEG_17 on table STAFF");
		}
	}
	// PASS:0124 If 0 rows updated?

	rs = stmt.executeQuery("SELECT COUNT(*),SUM(NUMKEY) " + "FROM UPUNIQ;");
	//
	// Expected output:
	//
	//        COUNT SUM
	// ============ =====================
	//
	//            6 24
	//
	rs.next();
	assertEquals(6, rs.getInt(1));
	assertEquals(24, rs.getInt(2));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0124 If count = 6 and SUM(NUMKEY) = 24?

	// restore
	conn.rollback();

	// END TEST >>> 0124 <<< END TEST
	// ********************************************************

	// TEST:0125 UPDATE UNIQUE column (key = key + 1) no interim conflit!

	// setup
	rowCount = stmt.executeUpdate("UPDATE UPUNIQ "
			+ "SET NUMKEY = NUMKEY + 1 " + "WHERE NUMKEY >= 4;");
	assertEquals(3, rowCount);
	// PASS:0125 If 3 rows are updated?

	rs = stmt.executeQuery("SELECT COUNT(*),SUM(NUMKEY) " + "FROM UPUNIQ;");
	//
	// Expected output:
	//
	//        COUNT SUM
	// ============ =====================
	//
	//            6 27
	//
	rs.next();
	assertEquals(6, rs.getInt(1));
	assertEquals(27, rs.getInt(2));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0125 If count = 6 and SUM(NUMKEY) = 27?

	// restore
	conn.rollback();

	// END TEST >>> 0125 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_029
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml029.
//
// ORIGIN: NIST file sql/dml029.sql
//	
public void testDml_029() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);
	stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");

	conn.setAutoCommit(false);

	// TEST:0129 Double quote work in character string literal!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO STAFF "
			+ "VALUES('E8','Yang Ling',15,'Xi''an');");
	assertEquals(1, rowCount);
	// PASS:0129 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT GRADE,CITY " + "FROM STAFF "
			+ "WHERE EMPNUM = 'E8';");
	//
	// Expected output:
	//
	//        GRADE CITY
	// ============ ===============
	//
	//           15 Xi'an
	//
	rs.next();
	assertEquals(15, rs.getInt(1));
	assertEquals("Xi'an          ", rs.getString(2));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0129 If GRADE = 15 and CITY = 'Xi'an'?

	// restore
	conn.rollback();

	// END TEST >>> 0129 <<< END TEST
	// ************************************************************

	// TEST:0130 Approximate numeric literal <mantissa>E<exponent>!
	rowCount = stmt.executeUpdate("INSERT INTO JJ " + "VALUES(123.456E3);");
	assertEquals(1, rowCount);
	// PASS:0130 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
			+ "WHERE FLOATTEST > 123455 AND FLOATTEST < 123457;");
	//
	// Expected output:
	//
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0130 If count = 1 ?

	// restore
	conn.rollback();

	// END TEST >>> 0130 <<< END TEST
	// ***************************************************************

	// TEST:0131 Approximate numeric literal with negative exponent!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO JJ " + "VALUES(123456E-3);");
	assertEquals(1, rowCount);
	// PASS:0131 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
			+ "WHERE FLOATTEST > 122 AND FLOATTEST < 124;");
	//
	// Expected output:
	//
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0131 If count = 1 ?

	// restore
	conn.rollback();

	// END TEST >>> 0131 <<< END TEST
	// ********************************************************

	// TEST:0182 Approx numeric literal with negative mantissa & exponent!

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO JJ " + "VALUES(-123456E-3);");
	assertEquals(1, rowCount);
	// PASS:0182 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM JJ "
			+ "WHERE FLOATTEST > -124 AND FLOATTEST < -122;");
	//
	// Expected output:
	//
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0182 If count = 1 ?

	// restore
	conn.rollback();

	// END TEST >>> 0182 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_033
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml033.
//
// ORIGIN: NIST file sql/dml033.sql
//	
public void testDml_033() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);
	conn.setAutoCommit(false);

	// TEST:0135 Upper and loer case letters are distinct!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO WORKS "
			+ "VALUES('UPP','low',100);");
	assertEquals(1, rowCount);
	// PASS:0135 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT EMPNUM,PNUM " + "FROM WORKS "
			+ "WHERE EMPNUM='UPP' AND PNUM='low';");
	//
	// Expected output:
	//
	// EMPNUM PNUM
	// ====== ======
	//
	// UPP low
	//
	rs.next();
	assertEquals("UPP", rs.getString(1));
	assertEquals("low", rs.getString(2));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0135 If EMPNUM = 'UPP' and PNUM = 'low'?

	rs = stmt.executeQuery("SELECT EMPNUM,PNUM " + "FROM WORKS "
			+ "WHERE EMPNUM='upp' OR PNUM='LOW';");
	rowCount = 0;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(0, rowCount);
	// PASS:0135 If 0 rows are selected - out of data?

	// restore
	conn.rollback();
	// END TEST >>> 0135 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_034
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml034.
//
// ORIGIN: NIST file sql/dml034.sql
//
// TODO: Are the getfloat() delta's acceptable?
//
public void testDml_034() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	stmt.executeUpdate("CREATE TABLE GG (REALTEST     REAL);");
	stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
	stmt.executeUpdate("CREATE TABLE II (DOUBLETEST  DOUBLE PRECISION);");
	stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");
	stmt.executeUpdate("CREATE TABLE KK (FLOATTEST  FLOAT(32));");
	stmt.executeUpdate("CREATE TABLE LL (NUMTEST  NUMERIC(13,6));");
	stmt.executeUpdate("CREATE TABLE MM (NUMTEST  NUMERIC);");
	stmt.executeUpdate("CREATE TABLE MM2 (NUMTEST NUMERIC(10));");
	stmt.executeUpdate("CREATE TABLE NN (NUMTEST  NUMERIC(9));");
	stmt.executeUpdate("CREATE TABLE OO (NUMTEST  NUMERIC(9));");
	stmt.executeUpdate("CREATE TABLE PP (NUMTEST  DECIMAL(13,6));");
	stmt.executeUpdate("CREATE TABLE QQ (NUMTEST  DECIMAL);");
	stmt.executeUpdate("CREATE TABLE RR (NUMTEST  DECIMAL(8));");
	stmt.executeUpdate("CREATE TABLE SS (NUMTEST  DEC(13,6));");

	conn.setAutoCommit(false);

	// TEST:0088 Data type REAL!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO GG "
			+ "VALUES(123.4567E-2);");
	assertEquals(1, rowCount);
	// PASS:0088 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
	//
	// Expected output:
	//
	//       REALTEST
	// ==============
	//
	//      1.2345670
	//
	rs.next();
	assertEquals(1.2345670, rs.getFloat(1), 0.00000005);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0088 If REALTEST = 1.234567 ?
	// PASS:0088 OR is between 1.234562 and 1.234572 ?

	rs = stmt.executeQuery("SELECT * FROM GG "
			+ "WHERE REALTEST > 1.234561 and REALTEST < 1.234573;");
	//
	// Expected output:
	//
	//       REALTEST
	// ==============
	//
	//      1.2345670
	//
	rs.next();
	assertEquals(1.2345670, rs.getFloat(1), 0.00000005);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0088 If 1 row selected?

	// restore
	conn.rollback();

	// END TEST >>> 0088 <<< END TEST
	// ****************************************************************

	// TEST:0090 Data type DOUBLE PRECISION!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO II "
			+ "VALUES(0.123456123456E6);");
	assertEquals(1, rowCount);
	// PASS:0090 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
	//
	// Expected output:
	//
	//              DOUBLETEST
	// =======================
	//
	//       123456.1234560000
	//
	rs.next();
	assertEquals(123456.1234560000, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0090 If DOUBLETEST = 123456.123456 ?
	// PASS:0090 OR is between 123456.123451 and 123456.123461 ?

	rs = stmt
			.executeQuery("SELECT * FROM II "
					+ "WHERE DOUBLETEST > 123456.123450 and DOUBLETEST < 123456.123462;");
	//
	// Expected output:
	//
	//              DOUBLETEST
	// =======================
	//
	//       123456.1234560000
	//
	rs.next();
	assertEquals(123456.1234560000, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0090 If 1 row selected?

	// restore
	conn.rollback();

	// END TEST >>> 0090 <<< END TEST
	// ***********************************************************

	// TEST:0091 Data type FLOAT!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(12.345678);");
	assertEquals(1, rowCount);
	// PASS:0091 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
	//
	// Expected output:
	//
	//      FLOATTEST
	// ==============
	//
	//      12.345678
	//
	rs.next();
	assertEquals(12.345678, rs.getFloat(1), 0.0000005);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0091 If FLOATTEST = 12.345678 ?
	// PASS:0091 OR is between 12.345673 and 12.345683 ?

	rs = stmt.executeQuery("SELECT * FROM JJ "
			+ "WHERE FLOATTEST > 12.345672 and FLOATTEST < 12.345684;");
	//
	// Expected output:
	//
	//      FLOATTEST
	// ==============
	//
	//      12.345678
	//
	rs.next();
	assertEquals(12.345678, rs.getFloat(1), 0.0000005);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0091 If 1 row selected?

	// restore
	conn.rollback();

	// END TEST >>> 0091 <<< END TEST
	// **********************************************************

	// TEST:0092 Data type FLOAT(32)!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO KK "
			+ "VALUES(123.456123456E+3);");
	assertEquals(1, rowCount);
	// PASS:0092 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT FLOATTEST FROM KK;");
	//
	// Expected output:
	//
	//               FLOATTEST
	// =======================
	//
	//       123456.1234560000
	//
	rs.next();
	assertEquals(123456.12345600, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0092 If FLOATTEST = 123456.123456 ?
	// PASS:0092 OR is between 123456.1233 and 123456.1236 ?

	rs = stmt
			.executeQuery("SELECT * FROM KK "
					+ "WHERE FLOATTEST > 123456.123450 and FLOATTEST < 123456.123462;");
	//
	// Expected output:
	//
	//               FLOATTEST
	// =======================
	//
	//       123456.1234560000
	//
	rs.next();
	assertEquals(123456.12345600, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0092 If 1 row selected?

	// restore
	conn.rollback();

	// END TEST >>> 0092 <<< END TEST
	// *************************************************************

	// TEST:0093 Data type NUMERIC(13,6)!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO LL VALUES(123456.123456);");
	assertEquals(1, rowCount);
	// PASS:0093 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT * FROM LL;");
	//
	// Expected output:
	//
	//               NUMTEST
	// =====================
	//
	//         123456.123456
	//
	rs.next();
	assertEquals(123456.123456, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0093 If NUMTEST = 123456.123456 ?
	// PASS:0093 OR is between 123456.123451 and 123456.123461 ?

	rs = stmt.executeQuery("SELECT * FROM LL "
			+ "WHERE NUMTEST > 123456.123450 and NUMTEST < 123456.123462;");
	//
	// Expected output:
	//
	//               NUMTEST
	// =====================
	//
	//         123456.123456
	//
	rs.next();
	assertEquals(123456.123456, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0093 If 1 row selected?

	// restore
	conn.rollback();

	// END TEST >>> 0093 <<< END TEST
	// *************************************************************

	// TEST:0094 Data type DECIMAL(13,6)!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO PP "
			+ "VALUES(123456.123456);");
	assertEquals(1, rowCount);
	// PASS:0094 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT * FROM PP;");
	//
	// Expected output:
	//
	//               NUMTEST
	// =====================
	//
	//         123456.123456
	//
	rs.next();
	assertEquals(123456.123456, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0094 If NUMTEST = 123456.123456 ?
	// PASS:0094 OR is between 123456.123451 and 123456.123461 ?

	// restore
	conn.rollback();

	// END TEST >>> 0094 <<< END TEST
	// **************************************************************

	// TEST:0095 Data type DEC(13,6)!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO SS "
			+ "VALUES(123456.123456);");
	assertEquals(1, rowCount);
	// PASS:0095 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT * FROM SS;");
	//
	// Expected output:
	//
	//               NUMTEST
	// =====================
	//
	//         123456.123456
	//
	rs.next();
	assertEquals(123456.123456, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0095 If NUMTEST = 123456.123456 ?
	// PASS:0095 OR is between 123456.123451 and 123456.123461 ?

	// restore
	conn.rollback();

	// END TEST >>> 0095 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_035
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml035.
//
// ORIGIN: NIST file sql/dml035.sql
//
// TODO: Are the getfloat() delta's acceptable?
//
public void testDml_035() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");
	stmt.executeUpdate("CREATE TABLE KK (FLOATTEST  FLOAT(32));");
	stmt.executeUpdate("CREATE TABLE LL (NUMTEST  NUMERIC(13,6));");
	stmt.executeUpdate("CREATE TABLE MM (NUMTEST  NUMERIC);");
	stmt.executeUpdate("CREATE TABLE MM2 (NUMTEST NUMERIC(10));");
	stmt.executeUpdate("CREATE TABLE NN (NUMTEST  NUMERIC(9));");
	stmt.executeUpdate("CREATE TABLE OO (NUMTEST  NUMERIC(9));");
	stmt.executeUpdate("CREATE TABLE PP (NUMTEST  DECIMAL(13,6));");
	stmt.executeUpdate("CREATE TABLE QQ (NUMTEST  DECIMAL);");
	stmt.executeUpdate("CREATE TABLE RR (NUMTEST  DECIMAL(8));");
	stmt.executeUpdate("CREATE TABLE SS (NUMTEST  DEC(13,6));");

	conn.setAutoCommit(false);

	// TEST:0157 ORDER BY approximate numeric!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(66.2);");
	assertEquals(1, rowCount);
	// PASS:0157 If 1 row is inserted?
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(-44.5);");
	assertEquals(1, rowCount);
	// PASS:0157 If 1 row is inserted?
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(0.2222);");
	assertEquals(1, rowCount);
	// PASS:0157 If 1 row is inserted?
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(66.3);");
	assertEquals(1, rowCount);
	// PASS:0157 If 1 row is inserted?
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(-87);");
	assertEquals(1, rowCount);
	// PASS:0157 If 1 row is inserted?
	rowCount = stmt.executeUpdate("INSERT INTO JJ VALUES(-66.25);");
	assertEquals(1, rowCount);
	// PASS:0157 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ "
			+ "ORDER BY FLOATTEST DESC;");
	//
	// Expected output:
	//
	// FLOATTEST
	// ==============
	//
	//      66.300003
	//      66.199997
	//     0.22220001
	//     -44.500000
	//     -66.250000
	//     -87.000000
	//
	rs.next();
	assertEquals(66.300003, rs.getFloat(1), 0.0000001);

	rs.next();
	assertEquals(66.199997, rs.getFloat(1), 0.0000001);

	rs.next();
	assertEquals(0.22220001, rs.getFloat(1), 0.00000001);

	rs.next();
	assertEquals(-44.500000, rs.getFloat(1), 0.0);

	rs.next();
	assertEquals(-66.250000, rs.getFloat(1), 0.0);

	rs.next();
	assertEquals(-87.000000, rs.getFloat(1), 0.0);

	rowCount = 6;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(6, rowCount);
	// PASS:0157 If 6 rows are selected ?
	// PASS:0157 If last FLOATTEST = -87 OR is between -87.5 and -86.5 ?

	// restore
	conn.rollback();

	// END TEST >>> 0157 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_037
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml037.
//
// ORIGIN: NIST file sql/dml037.sql
//
public void testDml_037() throws SQLException {
	int errorCode; // Error code.
	int tmpint[];
	int rowCount; // Row count.

	stmt.executeUpdate("CREATE TABLE TEXT240  (TEXXT CHAR(240));");
	conn.setAutoCommit(false);

	// NO_TEST:0202 Host variable names same as column name!

	// Testing host identifier

	// ***********************************************************

	// TEST:0234 SQL-style comments with SQL statements!
	// OPTIONAL TEST

	rowCount = stmt.executeUpdate("DELETE  -- we empty the table \n"
			+ "FROM TEXT240;");

	rowCount = stmt
			.executeUpdate("INSERT INTO TEXT240   -- This is the test for the rules \n"
					+ "VALUES         -- for the placement \n"
					+ "('SQL-STYLE COMMENTS') -- of \n"
					+ "-- SQL-style comments \n" + ";");
	assertEquals(1, rowCount);
	// PASS:0234 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT * " + "FROM TEXT240;");
	//
	// Expected output:
	//
	// TEXXT
	// ===============================================================================
	//
	// SQL-STYLE COMMENTS
	//
	rs.next();
	assertEquals(
			"SQL-STYLE COMMENTS                                                                                                                                                                                                                              ",
			rs.getString(1));
	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0234 If TEXXT = 'SQL-STYLE COMMENTS'?

	// restore
	conn.rollback();

	// END TEST >>> 0234 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_038
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml038.
//
// ORIGIN: NIST file sql/dml038.sql
//
public void testDml_038() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);

	// TEST:0205 Cartesian product is produced without WHERE clause!

	rs = stmt.executeQuery("SELECT GRADE, HOURS, BUDGET "
			+ "FROM STAFF, WORKS, PROJ;");
	rs.next();
	assertEquals(12, rs.getInt(1));
	assertEquals(40, rs.getInt(2));
	assertEquals(10000, rs.getInt(3));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(360, rowCount);
	// PASS:0205 If 360 rows are selected ?

	// END TEST >>> 0205 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_039
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml039.
//
// ORIGIN: NIST file sql/dml039.sql
//
public void testDml_039() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);

	// TEST:0208 Upper and lower case in LIKE predicate!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO STAFF "
			+ "VALUES('E7', 'yanping',26,'China');");
	assertEquals(1, rowCount);
	// PASS:0208 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO STAFF "
			+ "VALUES('E8','YANPING',30,'NIST');");
	assertEquals(1, rowCount);
	// PASS:0208 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT CITY " + "FROM STAFF "
			+ "WHERE EMPNAME LIKE 'yan____%';");
	//
	// Expected output:
	//
	// CITY
	// ===============
	//
	// China
	//
	rs.next();
	assertEquals("China          ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0208 If CITY = 'China'?

	rs = stmt.executeQuery("SELECT CITY " + "FROM STAFF "
			+ "WHERE EMPNAME LIKE 'YAN____%';");
	//
	// Expected output:
	//
	// CITY
	// ===============
	//
	// NIST
	//
	rs.next();
	assertEquals("NIST           ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0208 If CITY = 'NIST'?

	// END TEST >>> 0208 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_040
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml040.
//
// ORIGIN: NIST file sql/dml040.sql
//
public void testDml_040() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	// TEST:0209 Join 2 tables from different schemas!

	/**
	 * TODO: This test hangs Firebird/Vulcan. // setup rowCount =
	 * stmt.executeUpdate("INSERT INTO VTABLE " + "SELECT * " + "FROM
	 * VTABLE;");
	 * 
	 * rs = stmt.executeQuery("SELECT COL1, EMPNUM, GRADE " + "FROM VTABLE,
	 * STAFF " + "WHERE COL1 < 200 AND GRADE > 12;"); // PASS:0209 If 6 rows
	 * are selected and SUM(COL1) = 220?
	 */

	// END TEST >>> 0209 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/*
 * 
 * testDml_041_EnforcementOfCheckClauseInNestedViews()
 * 
 * TEST:0212 Enforcement of CHECK clause in nested views!
 *  
 */
public void testDml_041_EnforcementOfCheckClauseInNestedViews()
		throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);
	stmt.executeUpdate("CREATE VIEW V_WORKS1 " + "AS SELECT * FROM WORKS "
			+ "WHERE HOURS > 15 " + "WITH CHECK OPTION;");

	stmt.executeUpdate("CREATE VIEW V_WORKS2 "
			+ "AS SELECT * FROM V_WORKS1 " + "WHERE EMPNUM = 'E1' "
			+ "OR EMPNUM = 'E6';");

	stmt.executeUpdate("CREATE VIEW V_WORKS3 "
			+ "AS SELECT * FROM V_WORKS2 " + "WHERE PNUM = 'P2' "
			+ "OR PNUM = 'P7' " + "WITH CHECK OPTION;");

	errorCode = 0;
	try {
		rowCount = stmt.executeUpdate("INSERT INTO V_WORKS2 "
				+ "VALUES('E9','P7',13);");
	} catch (SQLException excep) {
		errorCode = excep.getErrorCode();
	} finally {
		if (errorCode != 335544558) {
			fail("Should return Operation violates CHECK constraint on view or table V_WORKS1");
		}
	}
	// PASS:0212 If ERROR, view check constraint, 0 rows inserted?

	stmt.executeUpdate("INSERT INTO V_WORKS2 " + "VALUES('E7','P4',95);");
	// PASS:0212 If 1 row is inserted?

	stmt.executeUpdate("INSERT INTO V_WORKS3 " + "VALUES('E8','P2',85);");
	// PASS:0212 If either 1 row is inserted OR ?
	// PASS:0212 If ERROR, view check constraint, 0 rows inserted?
	// NOTE:0212 Vendor interpretation follows
	// NOTE:0212 Insertion of row means: outer check option does not imply
	// NOTE:0212 inner check options

	// NOTE:0212 Failure to insert means: outer check option implies
	// NOTE:0212 inner check options

	stmt.executeUpdate("INSERT INTO V_WORKS3 " + "VALUES('E1','P7',90);");
	// PASS:0212 If 1 row is inserted?

	errorCode = 0;
	try {
		rowCount = stmt.executeUpdate("INSERT INTO V_WORKS3 "
				+ "VALUES('E9','P2',10);");
	} catch (SQLException excep) {
		errorCode = excep.getErrorCode();
	} finally {
		if (errorCode != 335544558) {
			fail("Should return Operation violates CHECK constraint on view or table V_WORKS1");
		}
	}
	// PASS:0212 If ERROR, view check constraint, 0 rows inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
			+ "WHERE EMPNUM = 'E9';");
	rs.next();
	assertEquals(0, rs.getInt(1));

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM WORKS "
			+ "WHERE HOURS > 85;");
	rs.next();
	assertEquals(2, rs.getInt(1));
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_042
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml042.
//
// ORIGIN: NIST file sql/dml042.sql
//
public void testDml_042() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

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

	// TEST:0213 FIPS sizing -- 100 columns in a row!
	// FIPS sizing TEST

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO T100(C1,C21,C41,C61,C81,C100) "
					+ "VALUES(' 1','21','41','61','81','00');");
	assertEquals(1, rowCount);
	// PASS:0213 If 1 row is inserted?

	rs = stmt
			.executeQuery("SELECT C1,C21,C41,C61,C81,C100 " + "FROM T100;");
	//
	// Expected output:
	//
	// C1 C21 C41 C61 C81 C100
	// ====== ====== ====== ====== ====== ======
	//
	// 1 21 41 61 81 00
	//
	rs.next();
	assertEquals(" 1", rs.getString(1));
	assertEquals("21", rs.getString(2));
	assertEquals("41", rs.getString(3));
	assertEquals("61", rs.getString(4));
	assertEquals("81", rs.getString(5));
	assertEquals("00", rs.getString(6));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0213 If C1 = ' 1' and C100 = '00' ?

	// restore

	// END TEST >>> 0213 <<< END TEST

	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_043
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml043.
//
// ORIGIN: NIST file sql/dml043.sql
//
public void testDml_043() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	stmt
			.executeUpdate("CREATE TABLE T2000(STR110 CHAR(110),STR120 CHAR(120), "
					+ "STR130 CHAR(130),STR140 CHAR(140), "
					+ "STR150 CHAR(150),STR160 CHAR(160), "
					+ "STR170 CHAR(170),STR180 CHAR(180), "
					+ "STR190 CHAR(190),STR200 CHAR(200), "
					+ "STR210 CHAR(210),STR216 CHAR(216));");
	// TEST:0214 FIPS sizing -- 2000-byte row!
	// FIPS sizing TEST

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO T2000(STR110,STR200,STR216) "
					+ "VALUES ('STR11111111111111111111111111111111111111111111111', "
					+ "'STR22222222222222222222222222222222222222222222222', "
					+ "'STR66666666666666666666666666666666666666666666666');");
	assertEquals(1, rowCount);
	// PASS:0214 If 1 row is inserted?

	rowCount = stmt.executeUpdate("UPDATE T2000 " + "SET STR140 = "
			+ "'STR44444444444444444444444444444444444444444444444';");
	assertEquals(1, rowCount);
	// PASS:0214 If 1 row is updated?

	rowCount = stmt.executeUpdate("UPDATE T2000 " + "SET STR180 = "
			+ "'STR88888888888888888888888888888888888888888888888';");
	assertEquals(1, rowCount);
	// PASS:0214 If 1 row is updated?

	rs = stmt.executeQuery("SELECT STR110,STR180,STR216 " + "FROM T2000;");
	//
	// Expected output:
	//
	// STR110 STR180 STR216
	// ===============================================================================
	// ===============================================================================
	// ===============================================================================
	//
	// STR11111111111111111111111111111111111111111111111
	// STR88888888888888888888888888888888888888888888888
	// STR66666666666666666666666666666666666666666666666
	//
	//
	rs.next();
	assertEquals(
			"STR11111111111111111111111111111111111111111111111                                                            ",
			rs.getString(1));
	assertEquals(
			"STR88888888888888888888888888888888888888888888888                                                                                                                                  ",
			rs.getString(2));
	assertEquals(
			"STR66666666666666666666666666666666666666666666666                                                                                                                                                                      ",
			rs.getString(3));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0214 If STR180 = ?
	// PASS:0214 'STR88888888888888888888888888888888888888888888888'?
	// PASS:0214 If STR216 = ?
	// PASS:0214 'STR66666666666666666666666666666666666666666666666'?

	// END TEST >>> 0214 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_044
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml044.
//
// ORIGIN: NIST file sql/dml044.sql
//
public void testDml_044() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	// TEST:0215 FIPS sizing -- 6 columns in a UNIQUE constraint!
	// FIPS sizing TEST

	stmt
			.executeUpdate("CREATE TABLE T8(COL1 CHAR(2) NOT NULL,COL2 CHAR(4) NOT NULL, "
					+ "COL3 CHAR(6) NOT NULL,COL4 CHAR(8) NOT NULL, "
					+ "COL5 CHAR(10) NOT NULL,COL6 CHAR(12) NOT NULL, "
					+ "COL7 CHAR(14),COL8 CHAR(16), "
					+ "UNIQUE(COL1,COL2,COL3,COL4,COL5,COL6));");

	rowCount = stmt.executeUpdate("INSERT INTO T8 "
			+ "VALUES('th','seco','third3','fourth_4','fifth_colu', "
			+ "'sixth_column','seventh_column','last_column_of_t');");
	assertEquals(1, rowCount);
	// PASS:0215 If 1 row is inserted?

	errorCode = 0;
	try {
		rowCount = stmt.executeUpdate("INSERT INTO T8 "
				+ "VALUES('th','seco','third3','fourth_4','fifth_colu', "
				+ "'sixth_column','column_seventh','column_eighth_la');");
	} catch (SQLException excep) {
		errorCode = excep.getErrorCode();
	} finally {
		if (errorCode != 335544665) {
			fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
					+ "INTEG_24 on table T8");
		}
	}
	// PASS:0215 If ERROR, unique constraint, 0 rows inserted?

	rs = stmt
			.executeQuery("SELECT COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8 "
					+ "FROM T8;");
	//
	// Expected output:
	//
	// COL1 COL2 COL3 COL4 COL5 COL6 COL7 COL8
	// ====== ====== ====== ======== ========== ============ ==============
	// ================
	//
	// th seco third3 fourth_4 fifth_colu sixth_column seventh_column
	// last_column_of_t
	//
	rs.next();
	assertEquals("th", rs.getString(1));
	assertEquals("seco", rs.getString(2));
	assertEquals("third3", rs.getString(3));
	assertEquals("fourth_4", rs.getString(4));
	assertEquals("fifth_colu", rs.getString(5));
	assertEquals("sixth_column", rs.getString(6));
	assertEquals("seventh_column", rs.getString(7));
	assertEquals("last_column_of_t", rs.getString(8));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0215 If COL1 = 'th'?

	// END TEST >>> 0215 <<< END TEST
	// **************************************************************

	// TEST:0216 FIPS sizing -- 120 bytes in a UNIQUE constraint!
	// FIPS sizing TEST

	stmt.executeUpdate("CREATE TABLE T4(STR110 CHAR(110) NOT NULL, "
			+ "NUM6   NUMERIC(6) NOT NULL, "
			+ "COL3   CHAR(10),COL4 CHAR(20), " + "UNIQUE(STR110,NUM6));");

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO T4 VALUES "
					+ "('This test is trying to test the limit on the total length of an index', "
					+ "-123456, 'which is','not less than 120');");
	assertEquals(1, rowCount);
	// PASS:0216 If 1 row is inserted?

	errorCode = 0;
	try {
		rowCount = stmt
				.executeUpdate("INSERT INTO T4 VALUES "
						+ "('This test is trying to test the limit on the total length of an index', "
						+ "-123456,'which is','not less than 120');");
	} catch (SQLException excep) {
		errorCode = excep.getErrorCode();
	} finally {
		if (errorCode != 335544665) {
			fail("Should return violation of PRIMARY or UNIQUE KEY constraint "
					+ "INTEG_29 on table T4");
		}
	}
	// PASS:0216 If ERROR, unique constraint, 0 rows inserted?

	rs = stmt.executeQuery("SELECT STR110 FROM T4;");
	//
	// Expected output:
	//
	// STR110
	// ===============================================================================
	//
	// This test is trying to test the limit on the total length of an index
	//
	rs.next();
	assertEquals(
			"This test is trying to test the limit on the total length of an index                                         ",
			rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0216 If STR110 starts with 'This test is trying to test the '?
	// PASS:0216 and ends with 'limit on the total length of an index'?

	// END TEST >>> 0216 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_045
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml045.
//
// ORIGIN: NIST file sql/dml045.sql
//
public void testDml_045() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	stmt.executeUpdate("CREATE TABLE T12(COL1 CHAR(1), COL2 CHAR(2), "
			+ "COL3 CHAR(4), COL4 CHAR(6), "
			+ "COL5 CHAR(8), COL6 CHAR(10), "
			+ "COL7 CHAR(20), COL8 CHAR(30), "
			+ "COL9 CHAR(40), COL10 CHAR(50), "
			+ "COL11 INTEGER, COL12 INTEGER);");

	conn.setAutoCommit(false);

	// TEST:0218 FIPS sizing -- 6 columns in GROUP BY!
	// FIPS sizing TEST

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888888','0101010101', "
			+ "'2020...20','3030...30','4040...40','5050...50',44,48);");
	assertEquals(1, rowCount);
	// PASS:0218 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888888','1010101010', "
			+ "'2020...20','3030...30','4040...40','5050...50',11,12);");
	assertEquals(1, rowCount);
	// PASS:0218 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888888','1010101010', "
			+ "'2020...20','3030...30','4040...40','5050...50',22,24);");
	assertEquals(1, rowCount);
	// PASS:0218 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888888','0101010101', "
			+ "'2020...20','3030...30','4040...40','5050...50',33,36);");
	assertEquals(1, rowCount);
	// PASS:0218 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            4
	//
	rs.next();
	assertEquals(4, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0218 If count = 4?

	rs = stmt.executeQuery("SELECT COL6,SUM(COL11),MAX(COL12) "
			+ "FROM T12 " + "GROUP BY COL1,COL5,COL3,COL6,COL2,COL4 "
			+ "ORDER BY COL6 DESC;");
	//
	// Expected output:
	//
	// COL6 SUM MAX
	// ========== ===================== ============
	//
	// 1010101010 33 24
	// 0101010101 77 48
	//
	rs.next();
	assertEquals("1010101010", rs.getString(1));
	assertEquals(33, rs.getInt(2));
	assertEquals(24, rs.getInt(3));

	rs.next();
	assertEquals("0101010101", rs.getString(1));
	assertEquals(77, rs.getInt(2));
	assertEquals(48, rs.getInt(3));

	rowCount = 2;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(2, rowCount);
	// PASS:0218 If 2 rows are selected and second COL6 = 0101010101 and ?
	// PASS:0218 second SUM(COL11) = 77 and second MAX(COL12) = 48?

	// restore
	conn.rollback();

	// END TEST >>> 0218 <<< END TEST
	// ****************************************************************

	// TEST:0219 FIPS sizing -- 120 bytes in GROUP BY!
	// FIPS sizing TEST

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4444','666666','88888888','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',111,112);");
	assertEquals(1, rowCount);
	// PASS:0219 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4444','666666','88888889','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',333,336);");
	assertEquals(1, rowCount);
	// PASS:0219 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4444','666666','88888889','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',444,448);");
	assertEquals(1, rowCount);
	// PASS:0219 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4444','666666','88888888','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',222,224);");
	// PASS:0219 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            4
	//
	rs.next();
	assertEquals(4, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0219 If count = 4?

	rs = stmt.executeQuery("SELECT COL5,SUM(COL11),MAX(COL12) "
			+ "FROM T12 " + "GROUP BY COL9,COL5,COL7,COL4,COL3,COL8 "
			+ "ORDER BY COL5 DESC;");
	//
	// Expected output:
	//
	// COL5 SUM MAX
	// ======== ===================== ============
	//
	// 88888889 777 448
	// 88888888 333 224
	//
	rs.next();
	assertEquals("88888889", rs.getString(1));
	assertEquals(777, rs.getInt(2));
	assertEquals(448, rs.getInt(3));

	rs.next();
	assertEquals("88888888", rs.getString(1));
	assertEquals(333, rs.getInt(2));
	assertEquals(224, rs.getInt(3));

	rowCount = 2;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(2, rowCount);
	// PASS:0219 If 2 rows are selected ?
	// PASS:0219 If row #1 COL5=88888889, SUM(COL11)=777, MAX(COL12)=448?
	// PASS:0219 If row #2 COL5=88888888, SUM(COL11)=333, MAX(COL12)=224?

	// restore
	conn.rollback();

	// END TEST >>> 0219 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_046
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml046.
//
// ORIGIN: NIST file sql/dml046.sql
//
public void testDml_046() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	stmt.executeUpdate("CREATE TABLE T12(COL1 CHAR(1), COL2 CHAR(2), "
			+ "COL3 CHAR(4), COL4 CHAR(6), "
			+ "COL5 CHAR(8), COL6 CHAR(10), "
			+ "COL7 CHAR(20), COL8 CHAR(30), "
			+ "COL9 CHAR(40), COL10 CHAR(50), "
			+ "COL11 INTEGER, COL12 INTEGER);");

	conn.setAutoCommit(false);

	// TEST:0220 FIPS sizing -- 6 column in ORDER BY!
	// FIPS sizing TEST

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888884','1010101010', "
			+ "'2020...20','3030...30','4040...40','5050...50',11,12);");
	assertEquals(1, rowCount);
	// PASS:0220 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888883','1010101010', "
			+ "'2020...20','3030...30','4040...40','5050...50',22,24);");
	assertEquals(1, rowCount);
	// PASS:0220 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888882','0101010101', "
			+ "'2020...20','3030...30','4040...40','5050...50',33,36);");
	assertEquals(1, rowCount);
	// PASS:0220 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO T12 "
			+ "VALUES('1','22','4444','666666','88888881','0101010101', "
			+ "'2020...20','3030...30','4040...40','5050...50',44,48);");
	assertEquals(1, rowCount);
	// PASS:0220 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            4
	//
	rs.next();
	assertEquals(4, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0220 If count = 4?

	rs = stmt.executeQuery("SELECT COL5,COL6,COL11,COL3,COL4,COL7,COL8 "
			+ "FROM T12 " + "ORDER BY COL7,COL8,COL3,COL4,COL6,COL5 DESC;");
	//
	// Expected output:
	//
	// COL5 COL6 COL11 COL3 COL4 COL7 COL8
	// ======== ========== ============ ====== ====== ====================
	// ==============================
	//
	// 88888882 0101010101 33 4444 666666 2020...20 3030...30
	// 88888881 0101010101 44 4444 666666 2020...20 3030...30
	// 88888884 1010101010 11 4444 666666 2020...20 3030...30
	// 88888883 1010101010 22 4444 666666 2020...20 3030...30
	//
	rs.next();
	assertEquals("88888882", rs.getString(1));
	assertEquals("0101010101", rs.getString(2));
	assertEquals(33, rs.getInt(3));
	assertEquals("4444", rs.getString(4));
	assertEquals("666666", rs.getString(5));
	assertEquals("2020...20           ", rs.getString(6));
	assertEquals("3030...30                     ", rs.getString(7));

	rs.next();
	assertEquals("88888881", rs.getString(1));
	assertEquals("0101010101", rs.getString(2));
	assertEquals(44, rs.getInt(3));
	assertEquals("4444", rs.getString(4));
	assertEquals("666666", rs.getString(5));
	assertEquals("2020...20           ", rs.getString(6));
	assertEquals("3030...30                     ", rs.getString(7));

	rs.next();
	assertEquals("88888884", rs.getString(1));
	assertEquals("1010101010", rs.getString(2));
	assertEquals(11, rs.getInt(3));
	assertEquals("4444", rs.getString(4));
	assertEquals("666666", rs.getString(5));
	assertEquals("2020...20           ", rs.getString(6));
	assertEquals("3030...30                     ", rs.getString(7));

	rs.next();
	assertEquals("88888883", rs.getString(1));
	assertEquals("1010101010", rs.getString(2));
	assertEquals(22, rs.getInt(3));
	assertEquals("4444", rs.getString(4));
	assertEquals("666666", rs.getString(5));
	assertEquals("2020...20           ", rs.getString(6));
	assertEquals("3030...30                     ", rs.getString(7));

	rowCount = 4;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(4, rowCount);
	// PASS:0220 If 4 rows are selected and first row?
	// PASS:0220 COL5 = 88888882, COL6 = 0101010101 and COL11 = 33?

	// restore
	conn.rollback();

	// END TEST >>> 0220 <<< END TEST
	// **************************************************************

	// TEST:0221 FIPS sizing -- 120 bytes in ORDER BY!
	// FIPS sizing TEST

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4442','666666','88888888','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',111,112);");
	assertEquals(1, rowCount);
	// PASS:0221 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4443','666666','88888888','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',222,224);");
	assertEquals(1, rowCount);
	// PASS:0221 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4441','666666','88888888','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',333,336);");
	assertEquals(1, rowCount);
	// PASS:0221 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("INSERT INTO T12 "
					+ "VALUES('1','22','4444','666666','88888888','1010101010', "
					+ "'20202020202020202020','303030303030303030303030303030', "
					+ "'4040404040404040404040404040404040404040', '5050...50',444,448);");
	assertEquals(1, rowCount);
	// PASS:0221 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM  T12;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            4
	//
	rs.next();
	assertEquals(4, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0221 If count = 4?

	rs = stmt.executeQuery("SELECT COL3,COL11,COL9,COL8,COL7,COL5,COL4 "
			+ "FROM T12 " + "ORDER BY COL9,COL8,COL7,COL5,COL4,COL3;");
	//
	// Expected output:
	//
	// COL3 COL11 COL9 COL8 COL7 COL5 COL4
	// ====== ============ ========================================
	// ============================== ==================== ======== ======
	//
	// 4441 333 4040404040404040404040404040404040404040
	// 303030303030303030303030303030 20202020202020202020 88888888 666666
	// 4442 111 4040404040404040404040404040404040404040
	// 303030303030303030303030303030 20202020202020202020 88888888 666666
	// 4443 222 4040404040404040404040404040404040404040
	// 303030303030303030303030303030 20202020202020202020 88888888 666666
	// 4444 444 4040404040404040404040404040404040404040
	// 303030303030303030303030303030 20202020202020202020 88888888 666666
	//
	rs.next();
	assertEquals("4441", rs.getString(1));
	assertEquals(333, rs.getInt(2));
	assertEquals("4040404040404040404040404040404040404040", rs
			.getString(3));
	assertEquals("303030303030303030303030303030", rs.getString(4));
	assertEquals("20202020202020202020", rs.getString(5));
	assertEquals("88888888", rs.getString(6));
	assertEquals("666666", rs.getString(7));

	rs.next();
	assertEquals("4442", rs.getString(1));
	assertEquals(111, rs.getInt(2));
	assertEquals("4040404040404040404040404040404040404040", rs
			.getString(3));
	assertEquals("303030303030303030303030303030", rs.getString(4));
	assertEquals("20202020202020202020", rs.getString(5));
	assertEquals("88888888", rs.getString(6));
	assertEquals("666666", rs.getString(7));

	rs.next();
	assertEquals("4443", rs.getString(1));
	assertEquals(222, rs.getInt(2));
	assertEquals("4040404040404040404040404040404040404040", rs
			.getString(3));
	assertEquals("303030303030303030303030303030", rs.getString(4));
	assertEquals("20202020202020202020", rs.getString(5));
	assertEquals("88888888", rs.getString(6));
	assertEquals("666666", rs.getString(7));

	rs.next();
	assertEquals("4444", rs.getString(1));
	assertEquals(444, rs.getInt(2));
	assertEquals("4040404040404040404040404040404040404040", rs
			.getString(3));
	assertEquals("303030303030303030303030303030", rs.getString(4));
	assertEquals("20202020202020202020", rs.getString(5));
	assertEquals("88888888", rs.getString(6));
	assertEquals("666666", rs.getString(7));
	// PASS:0221 If 4 rows are selected ?
	// PASS:0221 If first row COL3 = 4441 and COL11 = 333?

	// restore
	conn.rollback();

	// END TEST >>> 0221 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_047
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml047.
//
// ORIGIN: NIST file sql/dml047.sql
//
public void testDml_047() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	// TEST:0222 FIPS sizing -- Length(240) of a character string!
	// FIPS sizing TEST
	// NOTE:0222 Literal length is only 78

	stmt.executeUpdate("CREATE TABLE T240(STR240 CHAR(240));");

	rowCount = stmt
			.executeUpdate("INSERT INTO T240 "
					+ "VALUES('Now is the time for all good men and women to come to the aid of their country');");
	assertEquals(1, rowCount);
	// PASS:0222 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT * FROM T240;");
	rs.next();
	assertEquals(
			"Now is the time for all good men and women to come to the aid of their country                                                                                                                                                                  ",
			rs.getString(1));
	assertFalse(rs.next());
	// PASS:0222 If STR240 starts with 'Now is the time for all good men'?
	// PASS:0222 and ends 'and women to come to the aid of their country'?

	// END TEST >>> 0222 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_049
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml049.
//
// ORIGIN: NIST file sql/dml049.sql
//
public void testDml_049() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);

	stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
			+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
	stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
			+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
			+ "CITY   CHAR(15));");

	stmt
			.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
					+ "PNAME  CHAR(20), "
					+ "PTYPE  CHAR(6), "
					+ "BUDGET DECIMAL(9), " + "CITY   CHAR(15));");

	stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, "
			+ "PNUM    CHAR(3) NOT NULL, " + "HOURS   DECIMAL(5), "
			+ "UNIQUE(EMPNUM, PNUM));");

	stmt.executeUpdate("CREATE TABLE STAFF4 (EMPNUM    CHAR(3) NOT NULL, "
			+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
			+ "CITY   CHAR(15));");

	conn.setAutoCommit(false);

	// TEST:0225 FIPS sizing -- ten tables in FROM clause!
	// FIPS sizing TEST

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
			+ "SELECT EMPNUM,GRADE,CITY " + "FROM STAFF "
			+ "WHERE GRADE > 11;");
	assertEquals(4, rowCount);
	// PASS:0225 If 4 rows are inserted ?

	rowCount = stmt.executeUpdate("INSERT INTO STAFF1 " + "SELECT * "
			+ "FROM STAFF;");
	assertEquals(5, rowCount);
	// PASS:0225 If 5 rows are inserted?

	rowCount = stmt.executeUpdate("INSERT INTO WORKS1 " + "SELECT * "
			+ "FROM WORKS;");
	assertEquals(12, rowCount);
	// PASS:0225 If 12 rows are inserted?

	rowCount = stmt.executeUpdate("INSERT INTO STAFF4 " + "SELECT * "
			+ "FROM STAFF;");
	assertEquals(5, rowCount);
	// PASS:0225 If 5 rows are inserted?

	rowCount = stmt.executeUpdate("INSERT INTO PROJ1 " + "SELECT * "
			+ "FROM PROJ;");
	assertEquals(6, rowCount);
	// PASS:0225 If 6 rows are inserted?

	rs = stmt.executeQuery("SELECT STAFF.EMPNUM,PROJ.PNUM,WORKS.HOURS, "
			+ "STAFF3.GRADE,STAFF4.CITY,WORKS1.HOURS, "
			+ "TEMP_S.GRADE,PROJ1.PNUM,STAFF1.GRADE, " + "UPUNIQ.COL2 "
			+ "FROM   STAFF,PROJ,WORKS,STAFF3,STAFF4,WORKS1, "
			+ "TEMP_S,PROJ1,STAFF1,UPUNIQ "
			+ "WHERE  STAFF.EMPNUM = WORKS.EMPNUM    AND "
			+ "PROJ.PNUM = WORKS.PNUM         AND "
			+ "STAFF3.EMPNUM = WORKS.EMPNUM   AND "
			+ "STAFF4.EMPNUM = WORKS.EMPNUM   AND "
			+ "WORKS1.EMPNUM = WORKS.EMPNUM   AND "
			+ "WORKS1.PNUM = WORKS.PNUM       AND "
			+ "TEMP_S.EMPNUM = WORKS.EMPNUM   AND "
			+ "PROJ1.PNUM = WORKS.PNUM        AND "
			+ "STAFF1.EMPNUM = WORKS.EMPNUM   AND " + "UPUNIQ.COL2 = 'A' "
			+ "ORDER BY 1, 2;");
	//
	// Expected output:
	//
	// EMPNUM PNUM HOURS GRADE CITY HOURS GRADE PNUM GRADE COL2
	// ====== ====== ============ ============ =============== ============
	// ============ ====== ============ ======
	//
	// E1 P1 40 12 Deale 40 12 P1 12 A
	// E1 P2 20 12 Deale 20 12 P2 12 A
	// E1 P3 80 12 Deale 80 12 P3 12 A
	// E1 P4 20 12 Deale 20 12 P4 12 A
	// E1 P5 12 12 Deale 12 12 P5 12 A
	// E1 P6 12 12 Deale 12 12 P6 12 A
	// E3 P2 20 13 Vienna 20 13 P2 13 A
	// E4 P2 20 12 Deale 20 12 P2 12 A
	// E4 P4 40 12 Deale 40 12 P4 12 A
	// E4 P5 80 12 Deale 80 12 P5 12 A
	//
	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("P1 ", rs.getString(2));
	assertEquals(40, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(40, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P1 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("P2 ", rs.getString(2));
	assertEquals(20, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(20, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P2 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("P3 ", rs.getString(2));
	assertEquals(80, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(80, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P3 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("P4 ", rs.getString(2));
	assertEquals(20, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(20, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P4 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("P5 ", rs.getString(2));
	assertEquals(12, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(12, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P5 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("P6 ", rs.getString(2));
	assertEquals(12, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(12, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P6 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E3 ", rs.getString(1));
	assertEquals("P2 ", rs.getString(2));
	assertEquals(20, rs.getInt(3));
	assertEquals(13, rs.getInt(4));
	assertEquals("Vienna         ", rs.getString(5));
	assertEquals(20, rs.getInt(6));
	assertEquals(13, rs.getInt(7));
	assertEquals("P2 ", rs.getString(8));
	assertEquals(13, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E4 ", rs.getString(1));
	assertEquals("P2 ", rs.getString(2));
	assertEquals(20, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(20, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P2 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E4 ", rs.getString(1));
	assertEquals("P4 ", rs.getString(2));
	assertEquals(40, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(40, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P4 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rs.next();
	assertEquals("E4 ", rs.getString(1));
	assertEquals("P5 ", rs.getString(2));
	assertEquals(80, rs.getInt(3));
	assertEquals(12, rs.getInt(4));
	assertEquals("Deale          ", rs.getString(5));
	assertEquals(80, rs.getInt(6));
	assertEquals(12, rs.getInt(7));
	assertEquals("P5 ", rs.getString(8));
	assertEquals(12, rs.getInt(9));
	assertEquals("A ", rs.getString(10));

	rowCount = 10;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(10, rowCount);
	// PASS:0225 If 10 rows are selected ?
	// PASS:0225 If first STAFF.EMPNUM='E1',PROJ.PNUM='P1',WORKS.HOURS=40?
	// PASS:0225 If last STAFF.EMPNUM='E4',PROJ.PNUM='P5',WORKS.HOURS=80?

	// restore
	conn.rollback();

	// END TEST >>> 0225 <<< END TEST

	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_050
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml050.
//
// ORIGIN: NIST file sql/dml050.sql
//
public void testDml_050() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);

	// TEST:0226 FIPS sizing - 10 tables in SQL statement!
	// FIPS sizing TEST

	rs = stmt.executeQuery("SELECT EMPNUM, EMPNAME " + "FROM STAFF "
			+ "WHERE EMPNUM IN " + "(SELECT EMPNUM  FROM WORKS "
			+ "WHERE PNUM IN " + "(SELECT PNUM  FROM PROJ "
			+ "WHERE PTYPE IN " + "(SELECT PTYPE  FROM PROJ "
			+ "WHERE PNUM IN " + "(SELECT PNUM  FROM WORKS "
			+ "WHERE EMPNUM IN " + "(SELECT EMPNUM  FROM WORKS "
			+ "WHERE PNUM IN " + "(SELECT PNUM   FROM PROJ "
			+ "WHERE PTYPE IN " + "(SELECT PTYPE  FROM PROJ "
			+ "WHERE CITY IN " + "(SELECT CITY  FROM STAFF "
			+ "WHERE EMPNUM IN " + "(SELECT EMPNUM  FROM WORKS "
			+ "WHERE HOURS = 20 " + "AND PNUM = 'P2' )))))))));");
	//
	// Expected output:
	//
	// EMPNUM EMPNAME
	// ====== ====================
	//
	// E1 Alice
	// E2 Betty
	// E3 Carmen
	// E4 Don
	//
	rs.next();
	assertEquals("E1 ", rs.getString(1));
	assertEquals("Alice               ", rs.getString(2));

	rs.next();
	assertEquals("E2 ", rs.getString(1));
	assertEquals("Betty               ", rs.getString(2));

	rs.next();
	assertEquals("E3 ", rs.getString(1));
	assertEquals("Carmen              ", rs.getString(2));

	rs.next();
	assertEquals("E4 ", rs.getString(1));
	assertEquals("Don                 ", rs.getString(2));

	rowCount = 4;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(4, rowCount);
	// PASS:0226 If 4 rows selected excluding EMPNUM='E5', EMPNAME='Ed'?

	// END TEST >>> 0226 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_051
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml051.
//
// ORIGIN: NIST file sql/dml051.sql
//
public void testDml_051() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */
	BaseTab.setupBaseTab(stmt);
	conn.setAutoCommit(false);

	// TEST:0227 BETWEEN predicate with character string values!
	rs = stmt.executeQuery("SELECT PNUM " + "FROM   PROJ "
			+ "WHERE  PNAME BETWEEN 'A' AND 'F';");
	//
	// Expected output:
	//
	// PNUM
	// ======
	//
	// P2
	//
	rs.next();
	assertEquals("P2 ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0227 If PNUM = 'P2'?

	rs = stmt.executeQuery("SELECT PNUM " + "FROM   PROJ "
			+ "WHERE PNAME >= 'A' AND PNAME <= 'F';");
	//
	// Expected output:
	//
	// PNUM
	// ======
	//
	// P2
	//
	rs.next();
	assertEquals("P2 ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0227 If PNUM = 'P2'?

	// END TEST >>> 0227 <<< END TEST
	// ***********************************************************

	// TEST:0228 NOT BETWEEN predicate with character string values!
	rs = stmt.executeQuery("SELECT CITY " + "FROM   STAFF "
			+ "WHERE  EMPNAME NOT BETWEEN 'A' AND 'E';");
	//
	// Expected output:
	//
	// CITY
	// ===============
	// 
	// Akron
	//
	rs.next();
	assertEquals("Akron          ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0228 If CITY = 'Akron'?

	rs = stmt.executeQuery("SELECT CITY " + "FROM   STAFF "
			+ "WHERE  NOT( EMPNAME BETWEEN 'A' AND 'E' );");
	//
	// Expected output:
	//
	// CITY
	// ===============
	// 
	// Akron
	//
	rs.next();
	assertEquals("Akron          ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0228 If CITY = 'Akron'?

	// END TEST >>> 0228 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_052
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml052.
//
// ORIGIN: NIST file sql/dml052.sql
//
public void testDml_052() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);
	conn.setAutoCommit(false);

	// TEST:0229 Case-sensitive LIKE predicate!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO STAFF "
			+ "VALUES('E6','ALICE',11,'Gaithersburg');");
	assertEquals(1, rowCount);
	// PASS:0229 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT EMPNAME " + "FROM   STAFF "
			+ "WHERE  EMPNAME LIKE 'Ali%';");
	//
	// Expected output:
	//
	// EMPNAME
	// ====================
	//
	// Alice
	//
	rs.next();
	assertEquals("Alice               ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0229 If 1 row is returned and EMPNAME = 'Alice' (not 'ALICE')?

	rs = stmt.executeQuery("SELECT EMPNAME " + "FROM   STAFF "
			+ "WHERE  EMPNAME LIKE 'ALI%';");
	//
	// Expected output:
	//
	// EMPNAME
	// ====================
	//
	// Alice
	//
	rs.next();
	assertEquals("ALICE               ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0229 If 1 row is returned and EMPNAME = 'ALICE' (not 'Alice')?

	// restore
	conn.rollback();

	// END TEST >>> 0229 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_053
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml053.
//
// ORIGIN: NIST file sql/dml053.sql
//
public void testDml_053() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	BaseTab.setupBaseTab(stmt);
	stmt.executeUpdate("CREATE TABLE TEMP_S " + "(EMPNUM  CHAR(3), "
			+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
	conn.setAutoCommit(false);

	// TEST:0233 Table as multiset of rows - INSERT duplicate VALUES()!

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
			+ "VALUES('E1',11,'Deale');");
	assertEquals(1, rowCount);
	// PASS:0233 If 1 row is inserted?

	rowCount = stmt.executeUpdate("INSERT INTO TEMP_S "
			+ "VALUES('E1',11,'Deale');");
	assertEquals(1, rowCount);
	// PASS:0233 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM TEMP_S "
			+ "WHERE EMPNUM='E1' AND GRADE=11 AND CITY='Deale';");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            2
	//
	rs.next();
	assertEquals(2, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0233 If count = 2?

	// restore
	conn.rollback();

	// END TEST >>> 0233 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/***************************************************************************
 * --------------------------------------------------------------- METHOD:
 * testDml_055
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml055.
//
// ORIGIN: NIST file sql/dml055.sql
//
public void testDml_055() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	// TEST:0243 FIPS sizing - precision of SMALLINT >= 4!
	stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");

	// setup
	assertEquals(1, stmt.executeUpdate("INSERT INTO HH " + "VALUES(9999);"));
	// PASS:0243 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM HH "
			+ "WHERE SMALLTEST = 9999;");
	rs.next();
	assertEquals(1, rs.getInt(1));

	assertEquals(1, stmt
			.executeUpdate("INSERT INTO HH " + "VALUES(-9999);"));
	// PASS:0243 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT SMALLTEST " + "FROM HH "
			+ "WHERE SMALLTEST = -9999;");
	rs.next();
	assertEquals(-9999, rs.getInt(1));
	// PASS:0243 If SMALLTEST = -9999?
	assertFalse(rs.next());

	// END TEST >>> 0243 <<< END TEST
}

public void testDml_055_FipsSizingIntegerPrecision() throws SQLException {
	stmt.executeUpdate("CREATE TABLE EE (INTTEST     INTEGER);");
	// TEST:0244 FIPS sizing - precision of INTEGER >= 9!
	// FIPS sizing TEST

	assertEquals(1, stmt.executeUpdate("INSERT INTO EE "
			+ "VALUES(999999999);"));
	// PASS:0244 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT INTTEST " + "FROM EE "
			+ "WHERE INTTEST = 999999999;");
	rs.next();
	assertEquals(999999999, rs.getInt(1));
	// PASS:0244 If INTTEST = 999999999?
	assertFalse(rs.next());

	assertEquals(1, stmt.executeUpdate("INSERT INTO EE "
			+ "VALUES(-999999999);"));
	// PASS:0244 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM EE "
			+ "WHERE INTTEST = -999999999;");
	rs.next();
	assertEquals(1, rs.getInt(1));
	// PASS:0244 If count = 1?

	// END TEST >>> 0244 <<< END TEST

}

public void testDml_055_FipsSizingDecimalPrecision() throws SQLException {

	// TEST:0245 FIPS sizing - precision of DECIMAL >= 15!
	// FIPS sizing TEST
	stmt.executeUpdate("CREATE TABLE PP_15 (NUMTEST  DECIMAL(15,15));");

	// setup
	assertEquals(1, stmt.executeUpdate("INSERT INTO PP_15 "
			+ "VALUES(.123456789012345);"));
	// PASS:0245 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT NUMTEST " + "FROM PP_15;");
	rs.next();
	assertEquals(0.123456789012345, rs.getDouble(1), 0.0);
	// PASS:0245 If NUMTEST = 0.123456789012345?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM PP_15 "
			+ "WHERE NUMTEST = 0.123456789012345;");
	rs.next();
	assertEquals(1, rs.getInt(1));
	// PASS:0245 If count = 1?

	assertEquals(1, stmt.executeUpdate("DELETE FROM PP_15;"));
	// PASS:0245 If 1 row is deleted?

	// setup
	assertEquals(1, stmt.executeUpdate("INSERT INTO PP_15 "
			+ "VALUES(-.912345678901234);"));
	// PASS:0245 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM PP_15 "
			+ "WHERE NUMTEST = -0.912345678901234;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	// END TEST >>> 0245 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/**
 * ---------------------------------------------------------------
 * testDml_056
 * ----------------------------------------------------------------
 */
//
// Statements used to exercise test sql/dml056.
//
// ORIGIN: NIST file sql/dml056.sql
//
public void testDml_056() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	// TEST:0246 FIPS sizing - 100 values in INSERT!
	// FIPS sizing TEST

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

	rowCount = stmt.executeUpdate("INSERT INTO T100 "
			+ "VALUES('ZA','ZB','CA','ZC','ZD','AA','ZE','ZF','BA','ZG', "
			+ "'YA','YB','CB','YC','YD','AB','YE','YF','BB','YG', "
			+ "'XA','XB','CC','XC','XD','AC','XE','XF','BC','XG', "
			+ "'UA','UB','CD','UC','UD','AD','UE','UF','BD','UG', "
			+ "'VA','VB','CE','VC','VD','AE','VE','VF','BE','VG', "
			+ "'WA','WB','CF','WC','WD','AF','WE','WF','BF','WG', "
			+ "'LA','LB','CG','LC','LD','AG','LE','LF','BG','LG', "
			+ "'MA','MB','CH','MC','MD','AH','ME','MF','BH','MG', "
			+ "'NA','NB','CI','NC','ND','AI','NE','NF','BI','NG', "
			+ "'OA','OB','CJ','OC','OD','AJ','OE','OF','BJ','OG');");
	assertEquals(1, rowCount);
	// PASS:0246 If 1 row is inserted?

	rs = stmt
			.executeQuery("SELECT C6, C16, C26, C36, C46, C56, C66, C76, C86, C96, C100 "
					+ "FROM T100 " + "WHERE C1 = 'ZA' AND C2 = 'ZB';");
	//
	// Expected output:
	//
	// C6 C16 C26 C36 C46 C56 C66 C76 C86 C96 C100
	// ====== ====== ====== ====== ====== ====== ====== ====== ====== ======
	// ======
	//
	// AA AB AC AD AE AF AG AH AI AJ OG
	//
	rs.next();
	assertEquals("AA", rs.getString(1));
	assertEquals("AB", rs.getString(2));
	assertEquals("AC", rs.getString(3));
	assertEquals("AD", rs.getString(4));
	assertEquals("AE", rs.getString(5));
	assertEquals("AF", rs.getString(6));
	assertEquals("AG", rs.getString(7));
	assertEquals("AH", rs.getString(8));
	assertEquals("AI", rs.getString(9));
	assertEquals("AJ", rs.getString(10));
	assertEquals("OG", rs.getString(11));

	assertFalse(rs.next());
	// PASS:0246 If C6 = 'AA', C16 = 'AB', C26 = 'AC', C36 = 'AD' ?
	// PASS:0246 If C46 = 'AE', C56 = 'AF', C66 = 'AG', C76 = 'AH' ?
	// PASS:0246 If C86 = 'AI', C96 = 'AJ', C100 = 'OG' ?

	// END TEST >>> 0246 <<< END TEST

	// *********************************************************************

	// TEST:0247 FIPS sizing - 20 values in update SET clause!
	// FIPS sizing TEST

	rowCount = stmt.executeUpdate("DELETE FROM T100;");
	rowCount = stmt.executeUpdate("INSERT INTO T100 "
			+ "VALUES('ZA','ZB','CA','ZC','ZD','AA','ZE','ZF','BA','ZG', "
			+ "'YA','YB','CB','YC','YD','AB','YE','YF','BB','YG', "
			+ "'XA','XB','CC','XC','XD','AC','XE','XF','BC','XG', "
			+ "'UA','UB','CD','UC','UD','AD','UE','UF','BD','UG', "
			+ "'VA','VB','CE','VC','VD','AE','VE','VF','BE','VG', "
			+ "'WA','WB','CF','WC','WD','AF','WE','WF','BF','WG', "
			+ "'LA','LB','CG','LC','LD','AG','LE','LF','BG','LG', "
			+ "'MA','MB','CH','MC','MD','AH','AE','AF','BH','BG', "
			+ "'NA','NB','CI','NC','ND','AI','NE','NF','BI','NG', "
			+ "'OA','OB','CJ','OC','OD','AJ','OE','OF','BJ','OG');");
	assertEquals(1, rowCount);
	// PASS:0247 If 1 row is inserted?

	rowCount = stmt
			.executeUpdate("UPDATE T100 "
					+ "SET C5 = 'BA', C10 = 'ZP', C15 = 'BB', C20 = 'YP', C25 = 'BC', "
					+ "C30 = 'XP', C35 = 'BD', C40 = 'UP', C45 = 'BE', C50 = 'VP', "
					+ "C55 = 'BF', C60 = 'WP', C65 = 'BG', C70 = 'LP', C75 = 'BH', "
					+ "C80 = 'MP', C85 = 'BI', C90 = 'NP', C95 = 'BJ', C100 = 'OP';");
	assertEquals(1, rowCount);
	// PASS:0247 If 1 row is updated ?

	rs = stmt
			.executeQuery("SELECT C5, C20, C35, C40, C55, C60, C75, C80, C90, C95, C100 "
					+ "FROM T100 " + "WHERE C1 = 'ZA' AND C2 = 'ZB';");
	//
	// Expected output:
	//
	// C5 C20 C35 C40 C55 C60 C75 C80 C90 C95 C100
	// ====== ====== ====== ====== ====== ====== ====== ====== ====== ======
	// ======
	//
	// BA YP BD UP BF WP BH MP NP BJ OP
	//
	rs.next();
	assertEquals("BA", rs.getString(1));
	assertEquals("YP", rs.getString(2));
	assertEquals("BD", rs.getString(3));
	assertEquals("UP", rs.getString(4));
	assertEquals("BF", rs.getString(5));
	assertEquals("WP", rs.getString(6));
	assertEquals("BH", rs.getString(7));
	assertEquals("MP", rs.getString(8));
	assertEquals("NP", rs.getString(9));
	assertEquals("BJ", rs.getString(10));
	assertEquals("OP", rs.getString(11));
	assertFalse(rs.next());
	// PASS:0247 If C5 = 'BA', C35 = 'BD', C55 = 'BF', C75 = 'BH' ?
	// PASS:0247 If C90 = 'NP', C100 = 'OP'?

	// END TEST >>> 0247 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/*
 * 
 * testDml_157_FipsSizingOfFloatRealAndDouble()
 * 
 * TEST:0248 FIPS sizing - binary precision of FLOAT >= 20!
 *  
 */
public void testDml_057_FipsSizingOfFloatRealAndDouble()
		throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */

	stmt.executeUpdate("CREATE TABLE GG (REALTEST     REAL);");
	stmt.executeUpdate("CREATE TABLE II (DOUBLETEST  DOUBLE PRECISION);");
	stmt.executeUpdate("CREATE TABLE JJ (FLOATTEST  FLOAT);");

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO JJ " + "VALUES(0.1048575);");
	assertEquals(1, rowCount);
	// PASS:0248 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
	rs.next();
	assertEquals(0.10485750, rs.getFloat(1), 0.00000001);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0248 If FLOATTEST = 0.1048575 ?
	// PASS:0248 OR is between 0.1048574 and 0.1048576 ?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM JJ "
			+ "WHERE FLOATTEST > 0.1048574 AND FLOATTEST < 0.1048576;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0248 If count = 1?

	rowCount = stmt.executeUpdate("DELETE FROM JJ;");
	// Making sure the table is empty

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO JJ " + "VALUES(-0.1048575);");
	assertEquals(1, rowCount);
	// PASS:0248 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT FLOATTEST FROM JJ;");
	//
	// Expected output:
	//
	//      FLOATTEST
	// ==============
	//
	//    -0.10485750
	//
	rs.next();
	assertEquals(-0.10485750, rs.getFloat(1), 0.00000001);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0248 If FLOATTEST = -0.1048575 ?
	// PASS:0248 OR is between -0.1048576 and -0.1048574 ?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM JJ "
			+ "WHERE FLOATTEST > -0.1048576 AND FLOATTEST < -0.1048574;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0248 If count = 1?

	// restore

	// END TEST >>> 0248 <<< END TEST

	// *****************************************************************

	// TEST:0249 FIPS sizing - binary precision of REAL >= 20!
	// FIPS sizing TEST

	rowCount = stmt.executeUpdate("INSERT INTO GG " + "VALUES(0.1048575);");
	assertEquals(1, rowCount);
	// PASS:0249 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
	//
	// Expected output:
	//
	//       REALTEST
	// ==============
	//
	//     0.10485750
	//
	rs.next();
	assertEquals(0.10485750, rs.getFloat(1), 0.00000001);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0249 If REALTEST = 0.1048575 ?
	// PASS:0249 OR is between 0.1048574 and 0.1048576 ?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM GG "
			+ "WHERE REALTEST > 0.1048574 AND REALTEST < 0.1048576;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0249 If count = 1?

	rowCount = stmt.executeUpdate("DELETE FROM GG;");
	// Making sure the table is empty

	// setup
	rowCount = stmt
			.executeUpdate("INSERT INTO GG " + "VALUES(-0.1048575);");
	assertEquals(1, rowCount);
	// PASS:0249 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT REALTEST FROM GG;");
	//
	// Expected output:
	//
	//       REALTEST
	// ==============
	//
	//    -0.10485750
	//
	rs.next();
	assertEquals(-0.10485750, rs.getFloat(1), 0.00000001);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0249 If REALTEST = -0.1048575 ?
	// PASS:0249 OR is between -0.1048576 and -0.1048574 ?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM GG "
			+ "WHERE REALTEST > -0.1048576 AND REALTEST < -0.1048574;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0249 If count = 1?

	// END TEST >>> 0249 <<< END TEST

	// ***************************************************************

	// TEST:0250 FIPS sizing - bin. precision of DOUBLE >= 30!
	// FIPS sizing TEST
	rowCount = stmt.executeUpdate("INSERT INTO II "
			+ "VALUES(0.1073741823);");
	assertEquals(1, rowCount);
	// PASS:0250 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
	//
	// Expected output:
	//
	//              DOUBLETEST
	// =======================
	//
	//      0.1073741823000000
	//
	rs.next();
	assertEquals(0.1073741823000000, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0250 If DOUBLETEST = 0.1073741823 ?
	// PASS:0250 OR is between 0.1073741822 and 0.1073741824 ?

	rs = stmt
			.executeQuery("SELECT COUNT(*) FROM II "
					+ "WHERE DOUBLETEST > 0.1073741822 AND DOUBLETEST < 0.1073741824;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0250 If count = 1?

	rowCount = stmt.executeUpdate("DELETE FROM II;");
	// Making sure the table is empty

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO II "
			+ "VALUES(-0.1073741823);");
	assertEquals(1, rowCount);
	// PASS:0250 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT DOUBLETEST FROM II;");
	//
	// Expected output:
	//
	//              DOUBLETEST
	// =======================
	//
	//     -0.1073741823000000
	//
	rs.next();
	assertEquals(-0.1073741823000000, rs.getDouble(1), 0.0);

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0250 If DOUBLETEST = -0.1073741823 ?
	// PASS:0250 OR is between -0.1073741824 and -0.1073741822 ?

	rs = stmt
			.executeQuery("SELECT COUNT(*) FROM II "
					+ "WHERE DOUBLETEST > -0.1073741824 AND DOUBLETEST < -0.1073741822;");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            1
	//
	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0250 If count = 1?

	// END TEST >>> 0250 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/*
 * 
 * testDml_58
 * 
 * TEST:0251 COMMIT keeps changes of current transaction!
 *  
 */
public void testDml_058() throws SQLException {
	int errorCode; /* Error code. */
	int rowCount; /* Row count. */
	BaseTab.setupBaseTab(stmt);
	stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, "
			+ "EMPNAME  CHAR(20), " + "GRADE DECIMAL(4), "
			+ "CITY   CHAR(15));");
	stmt
			.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, "
					+ "PNAME  CHAR(20), "
					+ "PTYPE  CHAR(6), "
					+ "BUDGET DECIMAL(9), " + "CITY   CHAR(15));");
	stmt.executeUpdate("CREATE TABLE T4(STR110 CHAR(110) NOT NULL, "
			+ "NUM6   NUMERIC(6) NOT NULL, "
			+ "COL3   CHAR(10),COL4 CHAR(20), " + "UNIQUE(STR110,NUM6));");

	conn.setAutoCommit(false);

	rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
	// Making sure the table is empty

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
			+ "SELECT * FROM STAFF;");
	assertEquals(5, rowCount);
	// PASS:0251 If 5 rows are inserted?

	rs = stmt.executeQuery("SELECT COUNT(*) FROM STAFF1;");
	rs.next();
	assertEquals(5, rs.getInt(1));
	// PASS:0251 If count = 5?

	rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
			+ "VALUES('E9','Tom',50,'London');");
	assertEquals(1, rowCount);
	// PASS:0251 If 1 row is inserted?

	rowCount = stmt.executeUpdate("UPDATE STAFF1 " + "SET GRADE = 40 "
			+ "WHERE EMPNUM = 'E2';");
	assertEquals(1, rowCount);
	// PASS:0251 If 1 row is updated?

	conn.commit();

	rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
	assertEquals(6, rowCount);
	// PASS:0251 If 6 rows are deleted?

	// verify
	conn.rollback();

	// verify previous commit
	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM STAFF1 "
			+ "WHERE GRADE > 12;");
	rs.next();
	assertEquals(4, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0251 If count = 4?

	// restore
	rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
	conn.commit();

	// END TEST >>> 0251 <<< END TEST

	// ***************************************************************

	// TEST:0252 ROLLBACK cancels changes of current transaction!

	rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
	// Making sure the table is empty

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO STAFF1 " + "SELECT * "
			+ "FROM STAFF;");
	assertEquals(5, rowCount);
	// PASS:0252 If 5 rows are inserted?

	conn.commit();

	rowCount = stmt.executeUpdate("INSERT INTO STAFF1 "
			+ "VALUES('E10','Tom',50,'London');");
	assertEquals(1, rowCount);
	// PASS:0252 If 1 row is inserted?

	rowCount = stmt.executeUpdate("UPDATE STAFF1 " + "SET GRADE = 40 "
			+ "WHERE EMPNUM = 'E1';");
	assertEquals(1, rowCount);
	// PASS:0252 If 1 row is updated?

	rowCount = stmt.executeUpdate("DELETE FROM STAFF1 "
			+ "WHERE EMPNUM = 'E2';");
	assertEquals(1, rowCount);
	// PASS:0252 If 1 row is deleted?

	conn.rollback();

	// verify
	rs = stmt.executeQuery("SELECT SUM(GRADE) FROM STAFF1;");
	//
	// Expected output:
	//
	//                   SUM
	// =====================
	//
	//                    60
	//
	rs.next();
	assertEquals(60, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0252 If SUM(GRADE) = 60?

	// restore
	rowCount = stmt.executeUpdate("DELETE FROM STAFF1;");
	conn.commit();

	// END TEST >>> 0252 <<< END TEST

	// ****************************************************************

	// TEST:0253 TEST0124 workaround (key = key+1)!

	rs = stmt.executeQuery("SELECT NUMKEY " + "FROM UPUNIQ "
			+ "ORDER BY NUMKEY DESC;");
	//
	// Expected output:
	//
	//        NUMKEY
	// ============
	//
	//            8
	//            6
	//            4
	//            3
	//            2
	//            1
	//
	rs.next();
	assertEquals(8, rs.getInt(1));

	rs.next();
	assertEquals(6, rs.getInt(1));

	rs.next();
	assertEquals(4, rs.getInt(1));

	rs.next();
	assertEquals(3, rs.getInt(1));

	rs.next();
	assertEquals(2, rs.getInt(1));

	rs.next();
	assertEquals(1, rs.getInt(1));

	rowCount = 6;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(6, rowCount);
	// PASS:0253 If 6 rows are selected and first NUMKEY = 8 ?

	rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 8 + 1 "
			+ "WHERE NUMKEY = 8;");
	assertEquals(1, rowCount);
	// PASS:0253 If 1 row is updated?

	rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 6 + 1 "
			+ "WHERE NUMKEY = 6;");
	assertEquals(1, rowCount);
	// PASS:0253 If 1 row is updated?

	rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 4 + 1 "
			+ "WHERE NUMKEY = 4;");
	assertEquals(1, rowCount);
	// PASS:0253 If 1 row is updated?

	rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 3 + 1 "
			+ "WHERE NUMKEY = 3;");
	assertEquals(1, rowCount);
	// PASS:0253 If 1 row is updated?

	rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 2 + 1 "
			+ "WHERE NUMKEY = 2;");
	assertEquals(1, rowCount);
	// PASS:0253 If 1 row is updated?

	rowCount = stmt.executeUpdate("UPDATE UPUNIQ " + "SET NUMKEY = 1 + 1 "
			+ "WHERE NUMKEY = 1;");
	assertEquals(1, rowCount);
	// PASS:0253 If 1 row is updated?

	rs = stmt.executeQuery("SELECT MAX(NUMKEY), MIN(NUMKEY) "
			+ "FROM UPUNIQ;");
	//
	// Expected output:
	//
	//          MAX MIN
	// ============ ============
	//
	//            9 2
	//
	rs.next();
	assertEquals(9, rs.getInt(1));
	assertEquals(2, rs.getInt(2));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0253 If MAX(NUMKEY) = 9 AND MIN(NUMKEY) = 2?

	// restore
	conn.rollback();

	// END TEST >>> 0253 <<< END TEST

	// **************************************************************

	// TEST:0254 Column name in SET clause!

	//		stmt.executeUpdate("CREATE TABLE PROJ1 (PNUM CHAR(3) NOT NULL UNIQUE,
	// " +
	//				"PNAME CHAR(20), " +
	//				"PTYPE CHAR(6), " +
	//				"BUDGET DECIMAL(9), " +
	//				"CITY CHAR(15));");
	rowCount = stmt.executeUpdate("INSERT INTO PROJ1 " + "SELECT * "
			+ "FROM PROJ;");
	assertEquals(6, rowCount);
	// PASS:0254 If 6 rows are inserted?

	rowCount = stmt.executeUpdate("UPDATE PROJ1 " + "SET CITY = PTYPE;");
	assertEquals(6, rowCount);
	// PASS:0254 If 6 rows are updated?

	rs = stmt.executeQuery("SELECT CITY " + "FROM PROJ1 "
			+ "WHERE PNUM = 'P1';");
	//
	// Expected output:
	//
	// CITY
	// ===============
	//
	// Design
	//
	rs.next();
	assertEquals("Design         ", rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0254 If CITY = 'Design'?

	// restore
	conn.rollback();

	// END TEST >>> 0254 <<< END TEST

	// **************************************************************

	// TEST:0255 Key word USER for INSERT, UPDATE!
	rowCount = stmt.executeUpdate("DELETE FROM T4;");
	// Making sure the table is empty

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO T4 "
			+ "VALUES(USER,100,'good','luck');");
	assertEquals(1, rowCount);
	// PASS:0255 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT STR110 " + "FROM T4 "
			+ "WHERE NUM6 = 100;");
	//
	// Expected output:
	//
	// STR110
	// ===============================================================================
	//
	// SYSDBA
	//
	rs.next();
	assertEquals(
			"SYSDBA                                                                                                        ",
			rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0255 If STR110 = 'SYSDBA'?

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO T4 "
			+ "VALUES('Hello',101,'good','luck');");
	assertEquals(1, rowCount);
	// PASS:0255 If 1 row is inserted?

	rowCount = stmt.executeUpdate("UPDATE T4 " + "SET STR110 = USER "
			+ "WHERE NUM6 = 101;");
	assertEquals(1, rowCount);
	// PASS:0255 If 1 row is updated?

	rs = stmt.executeQuery("SELECT STR110 " + "FROM T4 "
			+ "WHERE NUM6 = 101;");
	//
	// Expected output:
	//
	// STR110
	// ===============================================================================
	//
	// SYSDBA
	//
	rs.next();
	assertEquals(
			"SYSDBA                                                                                                        ",
			rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0255 If STR110 = 'SYSDBA'?

	// restore
	conn.rollback();

	// END TEST >>> 0255 <<< END TEST

	// ***************************************************************

	// TEST:0256 Key word USER in WHERE clause!

	rowCount = stmt.executeUpdate("DELETE FROM T4;");
	// Making sure the table is empty

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO T4 "
			+ "VALUES('SYSDBA',100,'good','luck');");
	assertEquals(1, rowCount);
	// PASS:0256 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT STR110 " + "FROM T4 "
			+ "WHERE STR110 = USER;");
	//
	// Expected output:
	//
	// STR110
	// ===============================================================================
	//
	// SYSDBA
	//
	rs.next();
	assertEquals(
			"SYSDBA                                                                                                        ",
			rs.getString(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0256 If STR110 = 'SYSDBA'?

	// setup
	rowCount = stmt.executeUpdate("INSERT INTO T4 "
			+ "VALUES('Hello',101,'good','luck');");
	assertEquals(1, rowCount);
	// PASS:0256 If 1 row is inserted?

	rowCount = stmt.executeUpdate("DELETE FROM T4 "
			+ "WHERE STR110 = USER;");
	assertEquals(1, rowCount);
	// PASS:0256 If 1 row is deleted?

	rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM T4 "
			+ "WHERE STR110 LIKE '%SYSDBA%';");
	//
	// Expected output:
	//
	//        COUNT
	// ============
	//
	//            0
	//
	rs.next();
	assertEquals(0, rs.getInt(1));

	rowCount = 1;
	while (rs.next()) {
		rowCount++;
	}
	assertEquals(1, rowCount);
	// PASS:0256 If count = 0?

	// restore
	conn.rollback();

	// END TEST >>> 0256 <<< END TEST
	// *************************************************////END-OF-MODULE
}

/*
 * 
 * TestDml_059_SelectMaxMin()
 * 
 * TEST:0257 SELECT MAX, MIN (COL1 + or - COL2)!
 *  
 */
public void testDml_059_SelectMaxMin() throws SQLException {

	BaseTab.setupBaseTab(stmt);
	// TEST:0257 SELECT MAX, MIN (COL1 + or - COL2)!

	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES(10,11,12,13,15);"));
	// PASS:0257 If 1 row is inserted?

	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES(100,111,1112,113,115);"));
	// PASS:0257 If 1 row is inserted?

	rs = stmt
			.executeQuery("SELECT COL1, MAX(COL2 + COL3), MIN(COL3 - COL2) "
					+ "FROM VTABLE " + "GROUP BY COL1 " + "ORDER BY COL1;");
	rs.next();
	assertEquals(0, rs.getInt(1));
	assertEquals(3, rs.getInt(2));
	assertEquals(1, rs.getInt(3));

	rs.next();
	assertEquals(10, rs.getInt(1));
	assertEquals(50, rs.getInt(2));
	assertEquals(1, rs.getInt(3));

	rs.next();
	assertEquals(100, rs.getInt(1));
	assertEquals(1223, rs.getInt(2));
	assertEquals(100, rs.getInt(3));

	rs.next();
	assertEquals(1000, rs.getInt(1));
	assertEquals(1000, rs.getInt(2));
	assertEquals(5000, rs.getInt(3));

	assertFalse(rs.next());
	// PASS:0257 If 4 rows are selected in order with values:?
	// PASS:0257 ( 0, 3, 1) ?
	// PASS:0257 ( 10, 50, 1)?
	// PASS:0257 ( 100, 1223, 100)?
	// PASS:0257 ( 1000, 1000, 5000)?

	// END TEST >>> 0257 <<< END TEST
}

/*
 * 
 * testDml_059_SelectSumInHavingSum()
 * 
 * TEST:0258 SELECT SUM(2*COL1*COL2) in HAVING SUM(COL2*COL3)!
 *  
 */
public void testDml_059_SelectSumInHavingSum() throws SQLException {

	BaseTab.setupBaseTab(stmt);

	// TEST:0258 SELECT SUM(2*COL1*COL2) in HAVING SUM(COL2*COL3)!
	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES (10,11,12,13,15);"));
	// PASS:0258 if 1 row is inserted?

	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES (100,111,1112,113,115);"));
	// PASS:0258 if 1 row is inserted ?

	rs = stmt.executeQuery("SELECT COL1,SUM(2 * COL2 * COL3) "
			+ "FROM VTABLE " + "GROUP BY COL1 "
			+ "HAVING SUM(COL2 * COL3) > 2000 "
			+ "OR SUM(COL2 * COL3) < -2000 " + "ORDER BY COL1;");
	rs.next();
	assertEquals(100, rs.getInt(1));
	assertEquals(366864, rs.getInt(2));

	rs.next();
	assertEquals(1000, rs.getInt(1));
	assertEquals(-12000000, rs.getInt(2));

	assertFalse(rs.next());
	// PASS:0258 If 2 rows are selected?
	// PASS:0258 If first row has values (100, 366864) ?
	// PASS:0258 If second row has values (1000, -12000000) ?

	// END TEST >>> 0258 <<< END TEST
}

/*
 * 
 * testDml_059_SomeAnyInHavingClause()
 * 
 * TEST:0259 SOME, ANY in HAVING clause!
 *  
 */
public void testDml_059_SomeAnyInHavingClause() throws SQLException {

	BaseTab.setupBaseTab(stmt);

	// TEST:0259 SOME, ANY in HAVING clause!
	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES(10,11,12,13,15);"));
	// PASS:0259 If 1 row is inserted?

	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES(100,111,1112,113,115);"));
	// PASS:0259 If 1 row is inserted?

	if (!DB_DIALECT.equalsIgnoreCase("firebirdsql")) {
		rs = stmt.executeQuery("SELECT COL1, MAX(COL2) " + "FROM VTABLE "
				+ "GROUP BY COL1 "
				+ "HAVING MAX(COL2) > ANY (SELECT GRADE FROM STAFF) "
				+ "AND MAX(COL2) < SOME (SELECT HOURS FROM WORKS) "
				+ "ORDER BY COL1;");
		rs.next();
		assertEquals(10, rs.getInt(1));
		assertEquals(20, rs.getInt(2));
		assertFalse(rs.next());
		// PASS:0259 If 1 row is selected and COL1 = 10 and MAX(COL2) = 20?
	}
	// END TEST >>> 0259 <<< END TEST

}
/*
 * 
 * testDml_059_ExistsInHavingClause()
 * 
 * TEST:0260 EXISTS in HAVING clause!
 *  
 */
public void testDml_059_ExistsInHavingClause() throws SQLException {

	BaseTab.setupBaseTab(stmt);
	// TEST:0260 EXISTS in HAVING clause!

	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES(10,11,12,13,15);"));
	// PASS:0260 If 1 row is inserted?

	assertEquals(1, stmt.executeUpdate("INSERT INTO VTABLE "
			+ "VALUES(100,111,1112,113,115);"));
	// PASS:0260 If 1 row is inserted?

	rs = stmt.executeQuery("SELECT COL1, MAX(COL2) " + "FROM VTABLE "
			+ "GROUP BY COL1 " + "HAVING EXISTS (SELECT * " + "FROM STAFF "
			+ "WHERE EMPNUM = 'E1') " + "AND MAX(COL2) BETWEEN 10 AND 90 "
			+ "ORDER BY COL1;");
	rs.next();
	assertEquals(10, rs.getInt(1));
	assertEquals(20, rs.getInt(2));

	assertFalse(rs.next());
	// PASS:0260 If 1 row is selected and COL1 = 10 and MAX(COL2) = 20?

	// END TEST >>> 0260 <<< END TEST
}

/*
 * 
 * testDml_059_WhereHavingWithoutGroupBy
 * 
 * TEST:0264 WHERE, HAVING without GROUP BY!
 *  
 */
public void testDml_059_WhereHavingWithoutGroupBy() throws SQLException {

	BaseTab.setupBaseTab(stmt);

	// TEST:0264 WHERE, HAVING without GROUP BY!

	rs = stmt.executeQuery("SELECT SUM(COL1) " + "FROM VTABLE "
			+ "WHERE 10 + COL1 > COL2 " + "HAVING MAX(COL1) > 100;");
	rs.next();
	assertEquals(1000, rs.getInt(1));
	// PASS:0264 If SUM(COL1) = 1000?

	rs = stmt.executeQuery("SELECT SUM(COL1) " + "FROM VTABLE "
			+ "WHERE 1000 + COL1 >= COL2 " + "HAVING MAX(COL1) > 100;");
	rs.next();
	assertEquals(1110, rs.getInt(1));
	// PASS:0264 If SUM(COL1) = 1110?

	// END TEST >>> 0264 <<< END TEST

	// *************************************************////END-OF-MODULE
}
}
