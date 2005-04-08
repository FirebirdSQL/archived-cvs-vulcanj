/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 23, 2004
 *
 */
package org.firebirdsql.nist;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bioliv, Aug 23, 2004
 *  
 */
public class SunTab {

	public static void setupSunTab1(Statement stmt) throws SQLException {

		// The following tables, STAFF_M and PROJ_M reference each other.
		// Table STAFF_M has a "forward reference" to PROJ_M.
		stmt.executeUpdate("CREATE TABLE STAFF_M "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "EMPNAME CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15), " + "PRI_WK CHAR(3), "
				+ "UNIQUE (EMPNUM));");

		stmt.executeUpdate("CREATE TABLE PROJ_M " + "(PNUM CHAR(3) NOT NULL, "
				+ "PNAME CHAR(20), " + "PTYPE CHAR(6), "
				+ "BUDGET DECIMAL(9), " + "CITY CHAR(15), " + "MGR CHAR(3), "
				+ "UNIQUE (PNUM), " + "FOREIGN KEY (MGR) "
				+ "REFERENCES STAFF_M(EMPNUM));");

		stmt.executeUpdate("ALTER TABLE STAFF_M ADD FOREIGN KEY (PRI_WK) "
				+ "REFERENCES PROJ_M (PNUM);");

		// The following table is self-referencing.

		stmt.executeUpdate("CREATE TABLE STAFF_C "
				+ "(EMPNUM   CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE    DECIMAL(4), " + "CITY     CHAR(15), "
				+ "MGR   CHAR(3), " + "UNIQUE   (EMPNUM), "
				+ "FOREIGN KEY (MGR) " + "REFERENCES STAFF_C(EMPNUM));");

		//   This routine initializes the contents of tables:
		//        STAFF_M, PROJ_M, STAFF_C,
		//   This routine may be run at any time to re-initialize tables.

		// SET NULL foreign key values for tables which
		// reference each other or are self-referencing:

		stmt.executeUpdate("UPDATE STAFF_M SET PRI_WK = NULL;");

		stmt.executeUpdate("UPDATE PROJ_M SET MGR = NULL;");

		stmt.executeUpdate("UPDATE STAFF_C SET MGR = NULL;");

		stmt.executeUpdate("INSERT INTO STAFF_M "
				+ "VALUES('E1','Alice',12,'Deale',NULL);");
		stmt.executeUpdate("INSERT INTO STAFF_M "
				+ "VALUES('E2','Betty',10,'Vienna',NULL);");
		stmt.executeUpdate("INSERT INTO STAFF_M "
				+ "VALUES('E3','Carmen',13,'Vienna',NULL);");
		stmt.executeUpdate("INSERT INTO STAFF_M "
				+ "VALUES('E5','Don',12,'Deale',NULL);");
		stmt.executeUpdate("INSERT INTO STAFF_M "
				+ "VALUES('E4','Don',12,'Deale',NULL);");

		stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES('P1','MXSS','Design',10000,'Deale',NULL);");
		stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES('P2','CALM','Code',30000,'Vienna',NULL);");
		stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES('P4','SDP','Design',20000,'Deale',NULL);");
		stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES('P3','SDP','Test',30000,'Tample',NULL);");
		stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES('P5','IRM','Test',10000,'Vienna',NULL);");
		stmt.executeUpdate("INSERT INTO PROJ_M "
				+ "VALUES('P6','PAYR','Design',50000,'Deale',NULL);");

		stmt.executeUpdate("UPDATE STAFF_M " + "SET PRI_WK = 'P1' "
				+ "WHERE EMPNUM = 'E1';");
		stmt.executeUpdate("UPDATE STAFF_M " + "SET PRI_WK = 'P1' "
				+ "WHERE EMPNUM = 'E2';");
		stmt.executeUpdate("UPDATE STAFF_M " + "SET PRI_WK = 'P1' "
				+ "WHERE EMPNUM = 'E3';");
		stmt.executeUpdate("UPDATE STAFF_M " + "SET PRI_WK = 'P2' "
				+ "WHERE EMPNUM = 'E4';");
		stmt.executeUpdate("UPDATE STAFF_M " + "SET PRI_WK = 'P4' "
				+ "WHERE EMPNUM = 'E5';");
		stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E2' "
				+ "WHERE PNUM = 'P1';");
		stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E2' "
				+ "WHERE PNUM = 'P2';");
		stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E3' "
				+ "WHERE PNUM = 'P3';");
		stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E4' "
				+ "WHERE PNUM = 'P4';");
		stmt.executeUpdate("UPDATE PROJ_M " + "SET MGR = 'E4' "
				+ "WHERE PNUM = 'P5';");

		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E1','Alice',12,'Deale',NULL);");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E2','Betty',10,'Vienna','E1');");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E3','Carmen',13,'Vienna','E2');");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E4','Don',12,'Deale','E2');");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E5','Don',12,'Deale','E1');");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E6','Tom',14,'Gettysburg','E5');");
		stmt.executeUpdate("INSERT INTO STAFF_C "
				+ "VALUES('E7','Kingdom',18,'Gettysburg','E7');");
	}

	//   This routine creates the tables:
	//        SIZ1_P, SIZ1_F
	//        SIZ2_P, SIZ2_F1 through SIZ2_F10
	//        SIZ3_P1 through SIZ3_P10, SIZ3_F
	public static void setupSunTab2(Statement stmt) throws SQLException {

		stmt.executeUpdate("CREATE TABLE SIZ1_P " + "(S1   CHAR(3) NOT NULL, "
				+ "S2   CHAR(3) NOT NULL, " + "S3   DECIMAL(4) NOT NULL, "
				+ "S4   CHAR(3) NOT NULL, " + "S5   DECIMAL(4) NOT NULL, "
				+ "S6   CHAR(3) NOT NULL, " + "R1   CHAR(3), "
				+ "R2   CHAR(3), " + "R3   DECIMAL(4), "
				+ "UNIQUE   (S1,S2,S3,S4,S5,S6)); ");

		stmt.executeUpdate("CREATE TABLE SIZ1_F " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(3), " + "F3   DECIMAL(4), " + "F4   CHAR(3), "
				+ "F5   DECIMAL(4), " + "F6   CHAR(3), " + "R1   CHAR(3), "
				+ "R2   DECIMAL(5), " + "R3   DECIMAL(4), "
				+ "FOREIGN KEY   (F1,F2,F3,F4,F5,F6) "
				+ "REFERENCES SIZ1_P(S1,S2,S3,S4,S5,S6));");

		stmt.executeUpdate("CREATE TABLE SIZ2_P " + "(P1   CHAR(3) NOT NULL, "
				+ "P2   CHAR(3) NOT NULL, " + "P3   DECIMAL(4) NOT NULL, "
				+ "P4   CHAR(3) NOT NULL, " + "P5   DECIMAL(4) NOT NULL, "
				+ "P6   CHAR(3) NOT NULL, " + "P7   CHAR(3) NOT NULL, "
				+ "P8   DECIMAL(4) NOT NULL, " + "P9   DECIMAL(4) NOT NULL, "
				+ "P10   DECIMAL(4) NOT NULL, " + "P11   CHAR(4), "
				+ "UNIQUE (P1), " + "UNIQUE (P2), " + "UNIQUE (P3), "
				+ "UNIQUE (P4), " + "UNIQUE (P5), " + "UNIQUE (P6), "
				+ "UNIQUE (P7), " + "UNIQUE (P8), " + "UNIQUE (P9), "
				+ "UNIQUE (P10)); ");

		stmt.executeUpdate("CREATE TABLE SIZ2_F1 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "FOREIGN KEY (F1)  "
				+ "REFERENCES SIZ2_P(P1));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F2 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "FOREIGN KEY (F1)  "
				+ "REFERENCES SIZ2_P(P2));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F3 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "FOREIGN KEY (F1)  " + "REFERENCES SIZ2_P(P3));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F4 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "FOREIGN KEY (F1)  "
				+ "REFERENCES SIZ2_P(P4));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F5 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "FOREIGN KEY (F1)  " + "REFERENCES SIZ2_P(P5));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F6 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "FOREIGN KEY (F1)  "
				+ "REFERENCES SIZ2_P(P6));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F7 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "FOREIGN KEY (F1)  "
				+ "REFERENCES SIZ2_P(P7));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F8 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "FOREIGN KEY (F1)  " + "REFERENCES SIZ2_P(P8));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F9 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "FOREIGN KEY (F1)  " + "REFERENCES SIZ2_P(P9));");

		stmt.executeUpdate("CREATE TABLE SIZ2_F10 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "FOREIGN KEY (F1)  " + "REFERENCES SIZ2_P(P10));");

		stmt.executeUpdate("CREATE TABLE SIZ3_P1 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "UNIQUE (F1)); ");

		stmt.executeUpdate("CREATE TABLE SIZ3_P2 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "UNIQUE (F1));");

		stmt.executeUpdate("CREATE TABLE SIZ3_P3 "
				+ "(F1 DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "UNIQUE (F1));");

		stmt.executeUpdate("CREATE TABLE SIZ3_P4 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "UNIQUE (F1));");

		stmt.executeUpdate("CREATE TABLE SIZ3_P5 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "UNIQUE (F1)); ");

		stmt.executeUpdate("CREATE TABLE SIZ3_P6 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "UNIQUE (F1)); ");

		stmt.executeUpdate("CREATE TABLE SIZ3_P7 " + "(F1   CHAR(3) NOT NULL, "
				+ "F2   CHAR(8), " + "UNIQUE (F1));");

		stmt.executeUpdate("CREATE TABLE SIZ3_P8 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "UNIQUE (F1));");
		stmt.executeUpdate("CREATE TABLE SIZ3_P9 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "UNIQUE (F1));");

		stmt.executeUpdate("CREATE TABLE SIZ3_P10 "
				+ "(F1   DECIMAL(4) NOT NULL, " + "F2   CHAR(8), "
				+ "UNIQUE (F1));");

		stmt.executeUpdate("CREATE TABLE SIZ3_F " + "(P1   CHAR(3) NOT NULL, "
				+ "P2   CHAR(3), " + "P3   DECIMAL(4), " + "P4   CHAR(3), "
				+ "P5   DECIMAL(4), " + "P6   CHAR(3), " + "P7   CHAR(3), "
				+ "P8   DECIMAL(4), " + "P9   DECIMAL(4), "
				+ "P10   DECIMAL(4), " + "P11   CHAR(4), "
				+ "FOREIGN KEY (P1) " + "REFERENCES SIZ3_P1(F1), "
				+ "FOREIGN KEY (P2) " + "REFERENCES SIZ3_P2(F1), "
				+ "FOREIGN KEY (P3) " + "REFERENCES SIZ3_P3(F1), "
				+ "FOREIGN KEY (P4) " + "REFERENCES SIZ3_P4(F1), "
				+ "FOREIGN KEY (P5) " + "REFERENCES SIZ3_P5(F1), "
				+ "FOREIGN KEY (P6) " + "REFERENCES SIZ3_P6(F1), "
				+ "FOREIGN KEY (P7) " + "REFERENCES SIZ3_P7(F1), "
				+ "FOREIGN KEY (P8) " + "REFERENCES SIZ3_P8(F1), "
				+ "FOREIGN KEY (P9) " + "REFERENCES SIZ3_P9(F1), "
				+ "FOREIGN KEY (P10) " + "REFERENCES SIZ3_P10(F1));");
	}

	//   This routine initializes the contents of tables:
	//        SIZ1_P, SIZ1_F
	//        SIZ2_P, SIZ2_F1 through SIZ2_F10
	//        SIZ3_P1 through SIZ3_P10, SIZ3_F
	//   This routine may be run at any time to re-initialize tables.

	public static void refreshSunTab2(Statement stmt) throws SQLException {
		stmt.executeUpdate("DELETE FROM SIZ1_F;");
		stmt.executeUpdate("DELETE FROM SIZ1_P;");
		stmt.executeUpdate("DELETE FROM SIZ2_F1;");
		stmt.executeUpdate("DELETE FROM SIZ2_F2;");
		stmt.executeUpdate("DELETE FROM SIZ2_F3;");
		stmt.executeUpdate("DELETE FROM SIZ2_F4;");
		stmt.executeUpdate("DELETE FROM SIZ2_F5;");
		stmt.executeUpdate("DELETE FROM SIZ2_F6;");
		stmt.executeUpdate("DELETE FROM SIZ2_F7;");
		stmt.executeUpdate("DELETE FROM SIZ2_F8;");
		stmt.executeUpdate("DELETE FROM SIZ2_F9;");
		stmt.executeUpdate("DELETE FROM SIZ2_F10;");
		stmt.executeUpdate("DELETE FROM SIZ2_P;");
		stmt.executeUpdate("DELETE FROM SIZ3_F;");
		stmt.executeUpdate("DELETE FROM SIZ3_P1;");
		stmt.executeUpdate("DELETE FROM SIZ3_P2;");
		stmt.executeUpdate("DELETE FROM SIZ3_P3;");
		stmt.executeUpdate("DELETE FROM SIZ3_P4;");
		stmt.executeUpdate("DELETE FROM SIZ3_P5;");
		stmt.executeUpdate("DELETE FROM SIZ3_P6;");
		stmt.executeUpdate("DELETE FROM SIZ3_P7;");
		stmt.executeUpdate("DELETE FROM SIZ3_P8;");
		stmt.executeUpdate("DELETE FROM SIZ3_P9;");
		stmt.executeUpdate("DELETE FROM SIZ3_P10;");

		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E1','TTT',1,'SSS',10,'RRR','HHH','YYY',20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E1','TTS',1,'SSS',10,'RRR','HHH','YYY',21);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E2','TTT',1,'SSS',10,'RRR','HHH','YYY',22);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E3','TTT',1,'SSS',11,'RRR','HHH','YYY',23);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E4','TTT',2,'SSS',10,'RRR','HHH','YYY',24);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E1','TTS',3,'SSS',10,'RRR','HHH','YYY',25);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E2','TTT',1,'SSS',10,'TRR','HHH','YYY',26);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_P VALUES('E3','TTT',4,'SSS',11,'RRR','HHH','YYY',27);");

		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E1','TTT',1,'SSS',10,'RRR','YYY',90,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E1','TTS',1,'SSS',10,'RRR','YYY',91,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E2','TTT',1,'SSS',10,'RRR','YYY',92,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E3','TTT',1,'SSS',11,'RRR','YYY',93,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E4','TTT',2,'SSS',10,'RRR','YYY',94,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E1','TTS',3,'SSS',10,'RRR','YYY',95,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E2','TTT',1,'SSS',10,'TRR','YYY',96,20);");
		stmt
				.executeUpdate("INSERT INTO SIZ1_F VALUES('E2','TTT',1,'SSS',10,'TRR','YYY',97,20);");

		stmt.executeUpdate("INSERT INTO SIZ2_P VALUES "
				+ "('  A','  B',1,'  C',2,'  D','  E',3,4,5,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ2_P VALUES "
				+ "('  B','  C',2,'  D',3,'  E','  F',4,5,6,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ2_P VALUES "
				+ "('  C','  D',3,'  E',4,'  F','  G',5,6,7,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ2_P VALUES "
				+ "('  D','  E',4,'  F',5,'  G','  H',6,7,8,'TTT');");

		stmt.executeUpdate("INSERT INTO SIZ2_F1 VALUES ('  A','AAA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F1 VALUES ('  C','AAB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F1 VALUES ('  C','AAC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F1 VALUES ('  D','AAD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F2 VALUES ('  E','BBA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F2 VALUES ('  E','BBB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F2 VALUES ('  C','BBC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F2 VALUES ('  D','BBD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F3 VALUES (1,'CCA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F3 VALUES (1,'CCB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F3 VALUES (2,'CCC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F3 VALUES (3,'CCD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F4 VALUES ('  E','DDA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F4 VALUES ('  F','DDB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F4 VALUES ('  C','DDC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F4 VALUES ('  D','DDD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F5 VALUES (4,'EEA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F5 VALUES (4,'EEB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F5 VALUES (2,'EEC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F5 VALUES (3,'EED');");

		stmt.executeUpdate("INSERT INTO SIZ2_F6 VALUES ('  E','FFA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F6 VALUES ('  F','FFB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F6 VALUES ('  G','FFC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F6 VALUES ('  D','FFD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F7 VALUES ('  H','GGA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F7 VALUES ('  F','GGB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F7 VALUES ('  G','GGC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F7 VALUES ('  H','GGD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F8 VALUES (4,'HHA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F8 VALUES (4,'HHB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F8 VALUES (5,'HHC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F8 VALUES (3,'HHD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F9 VALUES (4,'JJA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F9 VALUES (4,'JJB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F9 VALUES (6,'JJC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F9 VALUES (7,'JJD');");

		stmt.executeUpdate("INSERT INTO SIZ2_F10 VALUES (5,'KKA');");
		stmt.executeUpdate("INSERT INTO SIZ2_F10 VALUES (5,'KKB');");
		stmt.executeUpdate("INSERT INTO SIZ2_F10 VALUES (7,'KKC');");
		stmt.executeUpdate("INSERT INTO SIZ2_F10 VALUES (8,'KKD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P1 VALUES ('  A','AAA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P1 VALUES ('  B','AAB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P1 VALUES ('  C','AAC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P1 VALUES ('  D','AAD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P2 VALUES ('  B','BBA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P2 VALUES ('  C','BBB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P2 VALUES ('  D','BBC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P2 VALUES ('  E','BBD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P3 VALUES (1,'CCA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P3 VALUES (2,'CCB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P3 VALUES (3,'CCC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P3 VALUES (4,'CCD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P4 VALUES ('  E','DDA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P4 VALUES ('  F','DDB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P4 VALUES ('  C','DDC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P4 VALUES ('  D','DDD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P5 VALUES (4,'EEA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P5 VALUES (5,'EEB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P5 VALUES (2,'EEC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P5 VALUES (3,'EED');");

		stmt.executeUpdate("INSERT INTO SIZ3_P6 VALUES ('  E','FFA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P6 VALUES ('  F','FFB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P6 VALUES ('  G','FFC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P6 VALUES ('  D','FFD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P7 VALUES ('  H','GGA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P7 VALUES ('  F','GGB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P7 VALUES ('  G','GGC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P7 VALUES ('  E','GGD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P8 VALUES (4,'HHA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P8 VALUES (6,'HHB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P8 VALUES (5,'HHC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P8 VALUES (3,'HHD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P9 VALUES (4,'JJA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P9 VALUES (5,'JJB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P9 VALUES (6,'JJC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P9 VALUES (7,'JJD');");

		stmt.executeUpdate("INSERT INTO SIZ3_P10 VALUES (5,'KKA');");
		stmt.executeUpdate("INSERT INTO SIZ3_P10 VALUES (6,'KKB');");
		stmt.executeUpdate("INSERT INTO SIZ3_P10 VALUES (7,'KKC');");
		stmt.executeUpdate("INSERT INTO SIZ3_P10 VALUES (8,'KKD');");

		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  A','  B',1,'  C',2,'  D','  E',3,4,5,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  B','  C',2,'  D',3,'  E','  F',4,5,6,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  C','  D',3,'  E',4,'  F','  G',5,6,7,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  D','  E',4,'  F',5,'  G','  H',6,7,8,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  B','  B',1,'  C',2,'  D','  E',3,4,5,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  C','  C',2,'  D',3,'  E','  F',4,5,6,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  C','  D',3,'  E',4,'  F','  G',5,6,7,'TTT');");
		stmt.executeUpdate("INSERT INTO SIZ3_F VALUES "
				+ "('  D','  E',4,'  F',5,'  G','  H',6,7,8,'TTT');");

		//  SELECT COUNT(*) FROM SIZ1_P;
		//// PASS:Setup if count = 8?
		//
		//  SELECT COUNT(*) FROM SIZ1_F;
		//// PASS:Setup if count = 8?
		//
		//  SELECT COUNT(*) FROM SIZ2_P;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F1;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F2;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F3;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F4;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F5;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F6;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F7;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F8;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F9;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ2_F10;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P1;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P2;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P3;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P4;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P5;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P6;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P7;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P8;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P9;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_P10;
		//// PASS:Setup if count = 4?
		//
		//  SELECT COUNT(*) FROM SIZ3_F;
		//// PASS:Setup if count = 8?

	}

	public static void setupSunTab3(Statement stmt) throws SQLException {

		stmt.executeUpdate("CREATE TABLE DEPT " + "(DNO DECIMAL(4) NOT NULL, "
				+ "DNAME CHAR(20) NOT NULL, " + "DEAN CHAR(30), "
				+ "PRIMARY KEY (DNO), " + "UNIQUE (DNAME));");

		stmt.executeUpdate("CREATE TABLE EMP " + "(ENO DECIMAL(4) NOT NULL, "
				+ "ENAME CHAR(20) NOT NULL, " + "EDESC CHAR(30), "
				+ "DNO DECIMAL(4) NOT NULL, " + "DNAME CHAR(20), "
				+ "BTH_DATE  DECIMAL(6) NOT NULL, " + "PRIMARY KEY (ENO), "
				+ "UNIQUE (ENAME,BTH_DATE), " + "FOREIGN KEY (DNO) REFERENCES "
				+ "DEPT(DNO), " + "FOREIGN KEY (DNAME) REFERENCES "
				+ "DEPT(DNAME));");

		stmt.executeUpdate("CREATE TABLE EXPERIENCE " + "(EXP_NAME CHAR(20), "
				+ "BTH_DATE DECIMAL(6), " + "WK_DATE  DECIMAL(6), "
				+ "DESCR CHAR(40), "
				+ "FOREIGN KEY (EXP_NAME,BTH_DATE) REFERENCES "
				+ "EMP(ENAME,BTH_DATE)); ");

		stmt.executeUpdate("CREATE TABLE STAFF_P "
				+ "(EMPNUM   CHAR(3) NOT NULL," + "EMPNAME  CHAR(20), "
				+ "GRADE    DECIMAL(4), " + "CITY     CHAR(15), "
				+ "UNIQUE  (EMPNUM));");

		stmt.executeUpdate("CREATE TABLE PROJ_P "
				+ "(PNUM     CHAR(3) NOT NULL, " + "PNAME    CHAR(20), "
				+ "PTYPE    CHAR(6), " + "BUDGET   DECIMAL(9), "
				+ "CITY     CHAR(15), " + "UNIQUE   (PNUM)); ");

		stmt
				.executeUpdate("CREATE TABLE MID1 (P_KEY DECIMAL(4) NOT NULL UNIQUE, "
						+ "F_KEY DECIMAL(4) REFERENCES MID1(P_KEY)); ");

		stmt
				.executeUpdate("CREATE TABLE ACR_SCH_P(P1 DECIMAL(4) NOT NULL UNIQUE, "
						+ "P2 CHAR(4));");

		// Test GRANT REFERENCES without grant permission below -- expect error
		// message!
		// "WITH GRANT OPTION" purposefully omitted from SUN's GRANT on STAFF_P
		// Do not change file SCHEMA8 to allow "WITH GRANT OPTION"

		stmt.executeUpdate("CREATE TABLE TAB5(F15 CHAR(3), " + "F5 CHAR(4), "
				+ "FOREIGN KEY (F15) " + "REFERENCES STAFF_P(EMPNUM));");

	}
	public static void refreshSunTab3(Statement stmt) throws SQLException {
		//		   stmt.executeUpdate("DELETE FROM ECCO;");
		//		   stmt.executeUpdate("INSERT INTO ECCO VALUES ('NL');");
		stmt.executeUpdate("DELETE FROM EXPERIENCE;");
		stmt.executeUpdate("DELETE FROM EMP;");
		stmt.executeUpdate("DELETE FROM DEPT;");
		stmt.executeUpdate("DELETE FROM STAFF_P;");
		stmt.executeUpdate("DELETE FROM PROJ_P;");
		stmt.executeUpdate("DELETE FROM ACR_SCH_P;");

		stmt
				.executeUpdate("INSERT INTO DEPT VALUES (12,'Computer','Charles');");
		stmt.executeUpdate("INSERT INTO DEPT VALUES (13,'Physics','Richard');");
		stmt
				.executeUpdate("INSERT INTO DEPT VALUES (14,'Education','Jeffersion');");
		stmt.executeUpdate("INSERT INTO DEPT VALUES (15,'English','Liss');");

		stmt
				.executeUpdate("INSERT INTO EMP VALUES "
						+ "(21,'Tom','Languages & Operating System',12,'Computer',040523);");
		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(22,'David','Database', 12,'Computer',101024);");
		stmt
				.executeUpdate("INSERT INTO EMP VALUES "
						+ "(23,'Lilian','Software Enginerring', 12,'Computer',112156);");
		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(24,'Mary','Liquid Mechanics', 13,'Physics',121245);");
		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(25,'John','Fraction', 13,'Physics',030542);");
		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(26,'Joseph','Child Education',14, 'Education',020556);");
		stmt.executeUpdate("INSERT INTO EMP VALUES "
				+ "(27,'Peter','Literature', 15,'English',020434);");

		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Tom',040523,000046,'Teacher');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Tom',040523,000066,'Officer');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Tom',040523,000076,'Retire');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('David',101024,000048,'Farmer');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('David',101024,000066,'Porter');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Lilian',112156,000072,'Baby siter');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Lilian',112156,000082,'Nurse');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Mary',121245,000065,'Fashion Model');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('John',030542,000064,'Actor');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Joseph',020556,000072,'Sportsman');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Joseph',020556,000072,'Teacher');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Peter',020434,000071,'Photographer');");
		stmt.executeUpdate("INSERT INTO EXPERIENCE VALUES "
				+ "('Peter',020434,000081,'Movie Producer');");

		stmt.executeUpdate("INSERT INTO STAFF_P "
				+ "VALUES ('E1','Alice',12,'Deale');");
		stmt.executeUpdate("INSERT INTO STAFF_P "
				+ "VALUES ('E2','Betty',10,'Vienna');");
		stmt.executeUpdate("INSERT INTO STAFF_P "
				+ "VALUES ('E3','Carmen',13,'Vienna');");
		stmt.executeUpdate("INSERT INTO STAFF_P "
				+ "VALUES ('E4','Don',12,'Deale');");
		stmt.executeUpdate("INSERT INTO STAFF_P "
				+ "VALUES ('E5','Ed',13,'Akron');");

		stmt
				.executeUpdate("INSERT INTO PROJ_P VALUES ('P1','MXSS','Design',10000,'Deale');");
		stmt
				.executeUpdate("INSERT INTO PROJ_P VALUES ('P2','CALM','Code',30000,'Vienna');");
		stmt
				.executeUpdate("INSERT INTO PROJ_P VALUES ('P3','SDP','Test',30000,'Tampa');");
		stmt
				.executeUpdate("INSERT INTO PROJ_P VALUES ('P4','SDP','Design',20000,'Deale');");
		stmt
				.executeUpdate("INSERT INTO PROJ_P VALUES ('P5','IRM','Test',10000,'Vienna');");
		stmt
				.executeUpdate("INSERT INTO PROJ_P VALUES ('P6','PAYR','Design',50000,'Deale');");

		stmt.executeUpdate("INSERT INTO ACR_SCH_P VALUES(1,'AAA');");
	}

}