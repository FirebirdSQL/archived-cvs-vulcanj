/*
 * Author: bioliv Created on: Jul 20, 2005
 *  
 */
package org.firebirdsql.vulcan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.firebirdsql.nist.NistTestBase;

public class TestUTF16 extends NistTestBase {
	private Connection conn; // Connection object.

	private Statement stmt; // Statement object.

	private ResultSet rs; // Result set.

	private ResultSetMetaData rsmd; // Result set metadata.

	public static void main(String[] args) {
	}

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

	public TestUTF16(String name) {
		super(name);
	}

	/*
	 * execute block is only available through psql, so we can't test it as part
	 * of a .SQL script.
	 */
	public void test_utf16() throws SQLException {
		String query = "recreate table t (i integer not null primary key, c char(20));";
		stmt.executeUpdate(query);
		stmt
				.executeUpdate("execute block as begin insert into t(i) values (1); insert into t(i) values (2); end;");
		rs = stmt.executeQuery("select * from t");
		rs.next();
		assertEquals(rs.getInt(1), 1);
		rs.next();
		assertEquals(rs.getInt(1), 2);
		assertFalse(rs.next());

		/* now try same with invalid data */
		try {
			stmt
					.executeUpdate("execute block as begin insert into t (i)values (3); insert into t (i) values (3); end; ");
			fail();
		} catch (SQLException sqle) {
		}

		/* should be no rows with value of 3 */
		rs = stmt.executeQuery(" select * from t where i = 3");
		assertFalse(rs.next());

		/* now try with an output parameter */ 
		rs = stmt.executeQuery ("execute block returns (Y INTEGER) "
				+ " as begin "
				+ " select max(i) from t into :Y; " + " suspend; end; ");
		rs.next(); 
		assertEquals (rs.getInt(1), 2); 

		/* now try with local variable declaration */ 
		rs = stmt.executeQuery ("execute block returns (Y INTEGER) "
				+ " as declare j integer; begin "
				+ " select max(i) from t into :Y; "
		        + "j = 5; y = j; " 
				+ " suspend; end; ");
		rs.next(); 
		assertEquals (5, rs.getInt(1)); 

	}
}