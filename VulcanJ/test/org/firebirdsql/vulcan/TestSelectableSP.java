package org.firebirdsql.vulcan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.firebirdsql.nist.NistTestBase;

public class TestSelectableSP extends NistTestBase {
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
		try {
			stmt.close();
			conn.close();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			System.out.println("HI");
		}
		super.tearDown();
	}

	public TestSelectableSP(String name) {
		super(name);
	}

	/*
	 * test for mimicing a stored function using a selectable stored proc.
	 * 
	 * the selectable stored procedure should return only 1 row, or else you get
	 * the dreaded "multiple rows in singleton select" error.
	 *  
	 */
	public void test_selectable_sp() throws SQLException {
		String q = "create procedure factorial ( max_rows integer, mode integer ) "
				+ "returns (row_num integer, result integer ) as declare variable temp integer; "
				+ "declare variable counter integer; BEGIN "
				+ "counter=0; temp=1; while (counter <= max_rows) do begin "
				+ "row_num = counter; if (row_num=0) then temp=1; else temp=temp * row_num;  result=temp; counter = counter + 1; if (mode=1) then suspend; end "
				+ "if (mode=2) then suspend; end ";
		try {
			stmt.executeUpdate(q);
		} catch (SQLException sqle) {
			System.out.println("info: could not create stored procedure.");
		}

		try {
			stmt.executeUpdate("recreate table onerow (i integer); ");
			stmt.executeUpdate("insert into onerow values (1); ");
		} catch (SQLException sqle) {
			System.out.println("info: could not create table onerow");
		}
		PreparedStatement ps = null;
		try {
			ps = conn
					.prepareStatement("select (select FRED from factorial(5,?)) as F2 from onerow ;");
			// ps.setString(1, "i");
			ps.setInt(1, 2);
			ResultSet rs = ps.executeQuery();
			rs.next();
			assertEquals(120, rs.getInt(1));
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			ps.close();
		}

		try {
			stmt.executeUpdate("drop procedure factorial;");
		} catch (SQLException sqle) {
		}

	}
}