/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 26, 2004
 *
 */
package org.firebirdsql.nist;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bioliv, Aug 26, 2004
 *
 */
public class SulTab {
	
	public static void setupSulTab1 (Statement stmt) throws SQLException {
		//  CREATE SCHEMA
		//  AUTHORIZATION SULLIVAN;

		  stmt.executeUpdate("CREATE TABLE WORKS_P "+
		   "(EMPNUM   CHAR(3) REFERENCES STAFF_P(EMPNUM), "+
		    "PNUM     CHAR(3), "+ 
		    "HOURS    DECIMAL(5), "+ 
		    "FOREIGN KEY (PNUM) "+
		    "REFERENCES PROJ_P(PNUM)); ");

		  stmt.executeUpdate("CREATE TABLE TTT(P1 DECIMAL(4) NOT NULL UNIQUE, "+
		                 "P2 CHAR(4));");

		// ************* grant statements follow *************

		   // GRANT SELECT ON TTT TO SCHANZLE;

		   // GRANT REFERENCES ON ACR_SCH_P TO SCHANZLE;


		//  Test GRANT without grant permission below // expect error message!
		//  "WITH GRANT OPTION" purposefully omitted from SUN's GRANT.
		//  Do not change file SCHEMA8 to allow "WITH GRANT OPTION" on STAFF_P.

		   // GRANT REFERENCES ON STAFF_P TO SCHANZLE;

	}

	public static void refreshSulTab1 (Statement stmt) throws SQLException {
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E1','P1',40);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E1','P2',20);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E1','P3',80);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E1','P4',20);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E1','P5',12);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E1','P6',12);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E2','P1',40);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E2','P2',80);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E3','P2',20);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E4','P2',20);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E4','P4',40);");
		  stmt.executeUpdate("INSERT INTO WORKS_P VALUES  ('E4','P5',80);");


	}
	
}
