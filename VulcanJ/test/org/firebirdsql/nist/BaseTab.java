/* $Id$ */
/*
 * Author: bioliv
 * Created on: Aug 23, 2004
 *
 * Contains the following NIST SQL schema job streams:
 * 
 *    sql/baseTab.sql
 *    sql/flattab.sql
 *    schema/cts5sch1.sql
 *    schema/cts5sch2.sql
 *    schema/cts5sch3.sql
 *    schema/cts5sch4.sql
 *    schema/cts5sch5.sql
 *    schema/schema1.sql
 *    schema/schema2.sql
 *    schema/schema3.sql
 *    schema/schema4.sql
 *    schema/schema5.sql
 *    schema/schema6.sql
 *    schema/schema7.sql
 *    schema/schema8.sql
 *    schema/schema9.sql
 *    schema/schema10.sql
 *    schema/xdrop1.sql
 *    schema/xdrop2.sql
 *    schema/xschema1.sql
 *    schema/xschema2.sql
 * 
 */
package org.firebirdsql.nist;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bioliv, Aug 23, 2004
 *  
 */
public class BaseTab {
	
	public static void setupStaff(Statement stmt) throws SQLException {
		try {stmt.executeUpdate("drop table staff"); } catch (SQLException sqle) {}
		// TODO: we should have a DB_DIALECT final variable
		if (System.getProperty(
				"test.db.dialect", "firebirdsql").equalsIgnoreCase("tssql")) {
			stmt.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
					+ "GRADE double precision, " + "CITY CHAR(15));");
		} else{			stmt.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
					+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
		}
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
	}
	

	public static void setupBaseTab(Statement stmt) throws SQLException {

		try { stmt.execute("drop table staff"); }
		catch (SQLException sqle) {}
		try { stmt.execute("drop table proj"); }
		catch (SQLException sqle) {}
		try { stmt.execute("drop table works"); }
		catch (SQLException sqle) {}
		try { stmt.execute("drop table vtable"); }
		catch (SQLException sqle) {}
		try { stmt.execute("drop table staff3"); }
		catch (SQLException sqle) {}
		try { stmt.execute("drop table upuniq"); }
		catch (SQLException sqle) {}

		if (System.getProperty(
				"test.db.dialect", "firebirdsql").equalsIgnoreCase("tssql")) {
			stmt.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
					+ "GRADE double precision, " + "CITY CHAR(15));");
			stmt.executeUpdate("CREATE TABLE PROJ "
					+ "(PNUM CHAR(3) NOT NULL UNIQUE, " + "PNAME CHAR(20), "
					+ "PTYPE CHAR(6), " + "BUDGET double precision, "
					+ "CITY CHAR(15));");
			stmt.executeUpdate("CREATE TABLE WORKS " + "(EMPNUM CHAR(3) NOT NULL, "
					+ "PNUM CHAR(3) NOT NULL, " + "HOURS double precision, "
					+ "UNIQUE(EMPNUM,PNUM));");
			stmt.executeUpdate("CREATE TABLE VTABLE " + "(COL1   INTEGER, "
					+ "COL2   INTEGER, " + "COL3   INTEGER, " + "COL4   INTEGER, "
					+ "COL5   double precision); ");
			stmt.executeUpdate("CREATE TABLE UPUNIQ ("
					+ "NUMKEY  double precision NOT NULL UNIQUE, " + "COL2 CHAR(2));");
		} 
		else {
			stmt.executeUpdate("CREATE TABLE STAFF "
					+ "(EMPNUM CHAR(3) NOT NULL UNIQUE," + "EMPNAME CHAR(20), "
					+ "GRADE DECIMAL(4), " + "CITY CHAR(15));");
			stmt.executeUpdate("CREATE TABLE PROJ "
					+ "(PNUM CHAR(3) NOT NULL UNIQUE, " + "PNAME CHAR(20), "
					+ "PTYPE CHAR(6), " + "BUDGET   DECIMAL(9), "
					+ "CITY CHAR(15));");
			stmt.executeUpdate("CREATE TABLE WORKS " + "(EMPNUM CHAR(3) NOT NULL, "
					+ "PNUM CHAR(3) NOT NULL, " + "HOURS DECIMAL(5), "
					+ "UNIQUE(EMPNUM,PNUM));");
			stmt.executeUpdate("CREATE TABLE VTABLE " + "(COL1   INTEGER, "
					+ "COL2   INTEGER, " + "COL3   INTEGER, " + "COL4   INTEGER, "
					+ "COL5   DECIMAL(7,2)); ");
			stmt.executeUpdate("CREATE TABLE UPUNIQ ("
					+ "NUMKEY  DECIMAL(3) NOT NULL UNIQUE, " + "COL2 CHAR(2));");
		}
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


		stmt.executeUpdate("INSERT INTO VTABLE VALUES(10,+20,30,40,10.50);");
		stmt.executeUpdate("INSERT INTO VTABLE VALUES(0,1,2,3,4.25);");
		stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(100,200,300,400,500.01);");
		stmt.executeUpdate("INSERT INTO VTABLE "
				+ "VALUES(1000,-2000,3000,NULL,4000.00);");

		stmt.executeUpdate("CREATE TABLE STAFF3 "
				+ "(EMPNUM CHAR(3) NOT NULL, " + "EMPNAME  CHAR(20), "
				+ "GRADE DECIMAL(4), " + "CITY CHAR(15), "
				+ "UNIQUE (EMPNUM));");

		stmt.executeUpdate("INSERT INTO STAFF3 " + "SELECT * FROM STAFF;");

		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(1,'A');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(2,'B');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(3,'C');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(4,'D');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(6,'F');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(8,'H');");

	}


	public static void setupBaseTab2(Statement stmt) throws SQLException {

		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID HU ******
		// ***************************************************************
		// MODULE BASETAB

		// SQL Test Suite, V6.0, Interactive SQL, basetab.sql
		// 59-byte ID
		// TEd Version #

		// AUTHORIZATION HU

		stmt.executeQuery("SELECT USER FROM ECCO;");
		// RERUN if USER value does not match preceding AUTHORIZATION comment

		// date_time print
		
		//   This routine initializes the contents of tables:
		//        STAFF, PROJ, WORKS, STAFF3, VTABLE, and UPUNIQ
		//   This routine may be run at any time to re-initialize tables.
		stmt.executeUpdate("DELETE FROM ECCO;");
		stmt.executeUpdate("INSERT INTO ECCO VALUES ('NL');");
		stmt.executeUpdate("DELETE FROM STAFF;");
		stmt.executeUpdate("DELETE FROM PROJ;");
		stmt.executeUpdate("DELETE FROM WORKS;");

		stmt.executeUpdate("INSERT INTO STAFF VALUES ('E1','Alice',12,'Deale');");
		stmt.executeUpdate("INSERT INTO STAFF VALUES ('E2','Betty',10,'Vienna');");
		stmt.executeUpdate("INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');");
		stmt.executeUpdate("INSERT INTO STAFF VALUES ('E4','Don',12,'Deale');");
		stmt.executeUpdate("INSERT INTO STAFF VALUES ('E5','Ed',13,'Akron');");

		stmt.executeUpdate("INSERT INTO PROJ VALUES  ('P1','MXSS','Design',10000,'Deale');");
		stmt.executeUpdate("INSERT INTO PROJ VALUES  ('P2','CALM','Code',30000,'Vienna');");
		stmt.executeUpdate("INSERT INTO PROJ VALUES  ('P3','SDP','Test',30000,'Tampa');");
		stmt.executeUpdate("INSERT INTO PROJ VALUES  ('P4','SDP','Design',20000,'Deale');");
		stmt.executeUpdate("INSERT INTO PROJ VALUES  ('P5','IRM','Test',10000,'Vienna');");
		stmt.executeUpdate("INSERT INTO PROJ VALUES  ('P6','PAYR','Design',50000,'Deale');");

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

		stmt.executeQuery("SELECT COUNT(*) FROM PROJ;");
		// PASS:Setup if count = 6?

		stmt.executeQuery("SELECT COUNT(*) FROM STAFF;");
		// PASS:Setup if count = 5?

		stmt.executeQuery("SELECT COUNT(*) FROM WORKS;");
		// PASS:Setup if count = 12?


		stmt.executeUpdate("DELETE FROM STAFF3;");
		stmt.executeUpdate("DELETE FROM VTABLE;");
		stmt.executeUpdate("DELETE FROM UPUNIQ;");

		stmt.executeUpdate("INSERT INTO STAFF3 " +
				"SELECT * " +
				"FROM   STAFF;");

		stmt.executeUpdate("INSERT INTO VTABLE VALUES(10,+20,30,40,10.50);");
		stmt.executeUpdate("INSERT INTO VTABLE VALUES(0,1,2,3,4.25);");
		stmt.executeUpdate("INSERT INTO VTABLE VALUES(100,200,300,400,500.01);");
		stmt.executeUpdate("INSERT INTO VTABLE VALUES(1000,-2000,3000,NULL,4000.00);");

		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(1,'A');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(2,'B');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(3,'C');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(4,'D');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(6,'F');");
		stmt.executeUpdate("INSERT INTO UPUNIQ VALUES(8,'H');");

		stmt.executeQuery("SELECT COUNT(*) FROM STAFF3;");
		// PASS:Setup if count = 5?

		stmt.executeQuery("SELECT COUNT(*) FROM VTABLE;");
		// PASS:Setup if count = 4?

		stmt.executeQuery("SELECT COUNT(*) FROM UPUNIQ;");
		// PASS:Setup if count = 6?
		// *************************************************////END-OF-MODULE	
	}
	

	public static void setupFlatTab(Statement stmt) throws SQLException {
		
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID FLATER **
		// ***************************************************************
		// MODULE  FLATTAB  

		// SQL Test Suite, V6.0, Interactive SQL, flattab.sql
		// 59-byte ID
		// TEd Version #

		// AUTHORIZATION FLATER

		stmt.executeQuery("SELECT USER FROM ECCO;");
		// RERUN if USER value does not match preceding AUTHORIZATION comment

		// date_time print

		// This routine initializes the contents of tables:
		//      BASE_VS1, USIG and U_SIG
		// This routine may be run at any time to re-initialize tables.

		stmt.executeUpdate("DELETE FROM BASE_VS1;");
		stmt.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,1);");
		stmt.executeUpdate("INSERT INTO BASE_VS1 VALUES (1,0);");
		stmt.executeUpdate("INSERT INTO BASE_VS1 VALUES (0,0);");
		stmt.executeUpdate("INSERT INTO BASE_VS1 VALUES (1,1);");

		stmt.executeQuery("SELECT COUNT(*) FROM BASE_VS1;");
		// PASS:Setup If count = 4?

		stmt.executeUpdate("DELETE FROM USIG;");
		stmt.executeUpdate("INSERT INTO USIG VALUES (0,2);");
		stmt.executeUpdate("INSERT INTO USIG VALUES (1,3);");

		stmt.executeUpdate("DELETE FROM U_SIG;");
		stmt.executeUpdate("INSERT INTO U_SIG VALUES (4,6);");
		stmt.executeUpdate("INSERT INTO U_SIG VALUES (5,7);");

		stmt.executeQuery("SELECT COUNT(*) FROM USIG;");
		// PASS:Setup If count = 2?

		stmt.executeQuery("SELECT COUNT(*) FROM U_SIG;");
		// PASS:Setup If count = 2?

	}
	
	
	public static void setupCts5sch1(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, cts5sch1.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID CTS2 ******
		// ***************************************************************

		// This file defines the base tables needed for some INFORMATION_SCHEMA tests.

		// The following command is supported only at Intermediate Level
		//   CREATE SCHEMA CTS2;

		// The following command should be used if ENTRY level rather than 
		// intermediate is supported
		// CREATE SCHEMA AUTHORIZATION CTS2;

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE PROJ_MAN " +
					"(P_REF   CHAR(3) NOT NULL UNIQUE, " +
					"BUDGET  DECIMAL(20), " +
					"SCOPE   CHAR(20), " +
					"MGR     CHAR(15) UNIQUE);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Precision must be from 1 to 18");
			}	
		}		
		
		// DOMAIN STATEMENTS

		stmt.executeUpdate("CREATE DOMAIN numdom AS INTEGER;");

		// GRANT PERMISSIONS

		errorCode = 0;
		try {
			stmt.executeUpdate("GRANT REFERENCES ON PROJ_MAN TO CTS1;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return table/view PROJ_MAN does not exist)");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("GRANT USAGE ON numdom TO CTS1;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		

		// ************* End of Schema *************		
	}
	
	
	public static void setupCts5sch2(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, cts5sch2.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID CTS1 ******
		// ***************************************************************

		//  The following command is supported only at INTERMEDIATE level
		//  CREATE SCHEMA CTS1;

		//  The following command should be used if ENTRY level rather than
		//  intermediate is supported.
		//  CREATE SCHEMA AUTHORIZATION CTS1;

		// ************* create character set statements *****

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE CHARACTER SET CS GET SQL_TEXT;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		
		
		// ************* create table statements *************


		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table ECCO already exists");
			}	
		}
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE FIPS1 " +
				"(FIPS_TEST CHAR(20));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table FIPS1 already exists");
			}	
		}
		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF " +
					"(EMPNUM   CHAR(3) NOT NULL UNIQUE, " +
					"EMPNAME  CHAR(20), " +
					"GRADE    DECIMAL(4), " +
					"CITY     CHAR(15));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE WORKS " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"PNUM     CHAR(3) NOT NULL, " +
					"HOURS    DECIMAL(5), " +
					"UNIQUE(EMPNUM,PNUM));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table WORKS already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF1 " +
					"(EMPNUM    CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE DECIMAL(4), " +
					"CITY   CHAR(15));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF1 already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF4 " +
					"(EMPNUM    CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE DECIMAL(4), " +
					"CITY   CHAR(15));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF4 already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE VTABLE " +
					"(COL1   INTEGER, " +
					"COL2   INTEGER, " +
					"COL3   INTEGER, " +
					"COL4   INTEGER, " +
					"COL5   DECIMAL(7,2));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table VTABLE already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF3 " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE    DECIMAL(4), " +
					"CITY     CHAR(15), " +
					"UNIQUE (EMPNUM));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF3 already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE PROJ3 " +
					"(PNUM     CHAR(3) NOT NULL, " +
					"PNAME    CHAR(20), " +
					"PTYPE    CHAR(6), " +
					"BUDGET   DECIMAL(9), " +
					"CITY     CHAR(15), " +
					"UNIQUE (PNUM));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table PROJ3 already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF7 (EMPNUM    CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE DECIMAL(4), " +
					"CITY   CHAR(15), " +
					"PRIMARY KEY (EMPNUM), " +
					"CHECK (GRADE BETWEEN 1 AND 20));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF7 already exists");
			}	
		}	
		
		stmt.executeUpdate("CREATE TABLE WORKS3a " +
				"(EMPNUM   CHAR(3) NOT NULL, " +
				"PNUM     CHAR(3) NOT NULL, " +
				"HOURS    DECIMAL(5), " +
				"FOREIGN KEY (PNUM) REFERENCES PROJ3(PNUM));");


		stmt.executeUpdate("CREATE TABLE STAFFa " +
				"( HOURS   INTEGER, " +
				"SALARY  DECIMAL(6), " +
				"EMPNUM  CHAR(3), " +
				"PNUM    DECIMAL(4), " +
				"EMPNAME CHAR(20));");

		stmt.executeUpdate("CREATE TABLE STAFFb " +
				"( SALARY   DECIMAL(6), " +
				"EMPNAME  CHAR(20), " +
				"HOURS    INTEGER, " +
				"PNUM     CHAR(3), " +
				"CITY     CHAR(15), " +
				"SEX      CHAR);");

		stmt.executeUpdate("CREATE TABLE STAFFc " +
				"(  EMPNUM   CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE    DECIMAL(4), " +
				"CITY     CHAR(15), " +
				"MGR      CHAR(3), " +
				"UNIQUE   (EMPNUM));");

		stmt.executeUpdate("CREATE TABLE STAFFd " +
				"(  EMPNUM   CHAR(3) NOT NULL, " +
				"GRADE    DECIMAL(4), " +
				"MGR      CHAR(3));");

		stmt.executeUpdate("CREATE TABLE STAFF_CTS " +
				"(  PNUM   CHAR(3), " +
				"CITY   CHAR(15), " +
				"GRADE  DECIMAL(4), " +
				"EMPNAME CHAR(20));");

		// TODO: Vulcan returns a different error message! 
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFFz " +
					"( EMPNUM CHAR(3) REFERENCES STAFF3(EMPNUM), " +
					"SALARY DECIMAL(6) CHECK (SALARY > 0));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return could not find UNIQUE INDEX with specified columns");
			}	
		}	
		
		stmt.executeUpdate("CREATE TABLE PROJ_DURATION " +
				"( MONTHS  INTEGER, " +
				"TIME_LEFT   INTEGER, " +
				"EMP_HOURS      INTEGER, " +
				"CHECK (MONTHS > 0));");

		stmt.executeUpdate("CREATE TABLE STAFF_CTS2 " +
				"(EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE EMPLOYEES2 " +
				"(  name     CHAR(10), " +
				"empno    INTEGER);");

		stmt.executeUpdate("CREATE TABLE A " +
				"(   p   INTEGER, " +
				"q   INTEGER );");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TT " +
					"(TTA   INTEGER, " +
					"TTB   INTEGER, " +
					"TTC   INTEGER);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table TT already exists");
			}	
		}	
		
		stmt.executeUpdate("CREATE TABLE TU " +
				"(TUD   CHAR(2), " +
				"TUE   INTEGER);");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TT2 " +
					"(TTA INTEGER, " +
					"TTB INTERVAL YEAR TO MONTH, " +
					"TTC DECIMAL(6,0));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		
		
		stmt.executeUpdate("CREATE TABLE TV " +
				"(A   INTEGER, " +
				"B   CHAR);");

		stmt.executeUpdate("CREATE TABLE TW " +
				"(D   CHAR, " +
				"E   INTEGER);");

		stmt.executeUpdate("CREATE TABLE TX " +
				"(TX1     INTEGER, " +
				"TX2     CHARACTER(5), " +
				"TX3     CHARACTER VARYING (10));");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE COMP_BUDG " +
					"(P_REF   CHAR(3) NOT NULL, " +
					"BUDGET  DECIMAL(20), " +
					"HOURS   INTEGER, " +
					"SALARY  DECIMAL(6), " +
					"FOREIGN KEY (P_REF) REFERENCES PROJ_MAN(P_REF));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Precision must be from 1 to 18");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE PROJ_STATUS " +
					"( MGR    CHAR(15)  REFERENCES PROJ_MAN(MGR), " +
					"P_REF  CHAR(3), " +
					"ONTIME CHAR, " +
					"BUDGET DECIMAL(20), " +
					"COST   DECIMAL(20));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Precision must be from 1 to 18");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE DATA_TYPE " +
					"(  NUM   NUMERIC, " +
					"DEC   DECIMAL, " +
					"ING   INTEGER, " +
					"SMA   SMALLINT, " +
					"FLO   FLOAT, " +
					"REA   REAL, " +
					"DOU   DOUBLE PRECISION);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		
		   
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TTIME_BASE " +
					"(PK           INTEGER, " +
					"TT           TIME, " +
					"TS           TIMESTAMP, " +
					"TT2          TIME WITH TIME ZONE, " +
					"TS2          TIMESTAMP WITH TIME ZONE, " +
					"PRIMARY KEY (PK));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		
		
		stmt.executeUpdate("CREATE TABLE CL_DATA_TYPE " +
				"(CL_CHAR CHAR(10), " +
				"CL_NUM NUMERIC, " +
				"CL_DEC DECIMAL, " +
				"CL_REAL REAL);");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE CL_EMPLOYEE " +
					"(EMPNUM  NUMERIC(5) PRIMARY KEY, " +
					"DEPTNO  CHAR(3), " +
					"LOC     CHAR(15), " +
					"EMPNAME CHAR(20), " +
					"SALARY  DECIMAL(6), " +
					"GRADE   DECIMAL(4), " +
					"HOURS   DECIMAL(5));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Column: EMPNUM not defined as NOT NULL - " +
						"cannot be used in PRIMARY KEY/UNIQUE constraint definition");
			}	
		}		
		
		stmt.executeUpdate("CREATE TABLE TEST6740A " +
				"(TNUM NUMERIC(4), " +
				"TCHARA CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6740B " +
				"(TNUM NUMERIC(4), " +
				"TCHARB CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6740C " +
				"(TNUMERIC NUMERIC(4), " +
				"TCHAR CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6840A " +
				"(NUM_A NUMERIC(4), " +
				"CH_A CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6840B " +
				"(NUM_B NUMERIC(4), " +
				"CH_B CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TEST6840C " +
				"(NUM_C1 NUMERIC(4), " +
				"CH_C1 CHAR(10), " +
				"NUM_C2 NUMERIC(4), " +
				"CH_C2 CHAR(10));");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TEST12849B " +
					"(col_num3 NUMERIC(3), " +
					"PRIMARY KEY (col_num3));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Column: COL_NUM3 not defined as NOT NULL - " +
						"cannot be used in PRIMARY KEY/UNIQUE constraint definition");
			}	
		}		
		
		// TODO: Vulcan returns a different error message!
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TEST12849A " +
					"(col_num1   NUMERIC(5) PRIMARY KEY, " +
					"col_str1   VARCHAR(15) NOT NULL, " +
					"col_str2   VARCHAR(10), " +
					"col_num2   NUMERIC(5) CONSTRAINT constr_1 REFERENCES TEST12849A, " +
					"col_str3   VARCHAR(25), " +
					"col_num3   NUMERIC(7,2), " +
					"col_num4   NUMERIC(3) NOT NULL " +
					"CONSTRAINT constr_3 REFERENCES TEST12849B " +
					"ON DELETE CASCADE, " +
					"CONSTRAINT constr_2 UNIQUE (col_str1, col_str2));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return 'REFERENCES table' without '(column)' requires PRIMARY KEY on referenced table");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE T4 " +
					"(STR110 CHAR(110) NOT NULL, " +
					"NUM6   NUMERIC(6) NOT NULL, " +
					"COL3   CHAR(10), " +
					"COL4 CHAR(20), " +
					"UNIQUE(STR110,NUM6));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table T4 already exists");
			}	
		}	
		
		stmt.executeUpdate("CREATE TABLE EMPTY740 " +
				"(COL_1   CHAR(10), " +
				"COL_2   VARCHAR(5), " +
				"COL_3   NUMERIC(5), " +
				"COL_4   DECIMAL(6), " +
				"COL_5   TIME);");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TABX760 " +
					"( DEPTNO  NUMERIC(5) UNIQUE NOT NULL, " +
					"EMPNAME CHAR(20)   UNIQUE NOT NULL, " +
					"SALARY  DECIMAL(7));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return unsupported DYN verb");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TABCS " +
					"( COLUN   NUMERIC(5) UNIQUE, " +
					"COLSTR1 CHAR(10)    CHARACTER SET CS, " +
					"COLSTR2 VARCHAR(10) CHARACTER SET CS);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Data type unknown");
			}	
		}

		stmt.executeUpdate("CREATE TABLE CL_STANDARD " +
				"(COL_NUM1 NUMERIC(4), " +
				"COL_CH1  CHAR(10), " +
				"COL_NUM2 NUMERIC(4), " +
				"COL_CH2  CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TABLE728a " +
				"(C1 CHAR(10), " +
				"C2 CHAR(10));");

		stmt.executeUpdate("CREATE TABLE TABLE728b " +
				"(COL_1 CHAR(10), " +
				"COL_2 CHAR(10));");


		//
		// TODO: Vulcan returns error: CHARACTER SET ISO8859_1 is not defined
		//        Firebird runs to successful completion.
		//
		stmt.executeUpdate("CREATE TABLE TAB734 " +
				"( CSTR1 NCHAR(10), " +
				"CSTR2 NCHAR VARYING(12));");


		// LATIN1 is not required by SQL-92 DWF 1996-02-21
		//  CREATE TABLE TABLATIN1
		//  ( COL1 CHARACTER(10) CHARACTER SET LATIN1,
		//    COL2 CHAR(12)      CHARACTER SET LATIN1,
		//    COL3 VARCHAR(15)   CHARACTER SET LATIN1,
		//    COL4 NUMERIC(5));

		stmt.executeUpdate("CREATE TABLE ET " +
				"(col1    CHAR(3), " +
				"col2    CHAR(20), " +
				"col3    DECIMAL(4), " +
				"col4    CHAR(15), " +
				"col5    INTEGER, " +
				"col6    INTEGER);");

		stmt.executeUpdate("CREATE TABLE TTSTORE " +
				"(numx    INTEGER, " +
				"colthu  INTEGER, " +
				"coltmu  INTEGER, " +
				"TT      TIME);");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TTSTORE2 " +
					"(num     INTEGER, " +
					"colthu  INTEGER, " +
					"coltmu  INTEGER, " +
					"TT2     TIME WITH TIME ZONE);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE CONCATBUF (ZZ CHAR(240));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table CONCATBUF already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW TESTREPORT AS " +
					"SELECT TESTNO, RESULT, TESTTYPE " +
					"FROM TESTREPORT;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Column unknown TESTNO");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW TTIME (PK, TT, TS) AS " +
					"SELECT PK, TT, TS " +
					"FROM TTIME_BASE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Table unknown TTIME_BASE");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW TTIME2 (PK, TT2, TS2) AS " +
					"SELECT PK, TT2, TS2 " +
					"FROM TTIME_BASE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Table unknown TTIME_BASE");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW TTIME3 (PK, TT, TT2, TS2) AS " +
					"SELECT PK, TT, TT2, TS2 " +
					"FROM TTIME_BASE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Table unknown TTIME_BASE");
			}	
		}	
		
		// ************* create domain statements ***********

		stmt.executeUpdate("CREATE DOMAIN esal AS INTEGER " +
				"CHECK (VALUE > 500);");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE DOMAIN atom CHARACTER " +
					"CHECK ('a' <= VALUE) " +
					"CHECK ('m' >= VALUE);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return duplicate specification of DOMAIN CHECK CONSTRAINT" +
						" - not supported");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE DOMAIN smint INTEGER " +
					"CHECK (1<= VALUE) " +
					"CHECK (100 >= VALUE);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return duplicate specification of DOMAIN CHECK CONSTRAINT" +
						" - not supported");
			}	
		}	
		
		// ************* grant statements follow *************

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON CONCATBUF TO PUBLIC;");

		stmt.executeUpdate("GRANT SELECT ON ECCO TO PUBLIC;");

		stmt.executeUpdate("GRANT INSERT ON TESTREPORT TO PUBLIC WITH GRANT OPTION;");

		errorCode = 0;
		try {
			stmt.executeUpdate("GRANT SELECT ON DATA_TYPE TO CTS4;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return table/view DATA_TYPE does not exist");
			}	
		}	
		
		// ************* End of Schema *************	
	}
	

	public static void setupCts5sch3(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, cts5sch3.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID CTS1 ******
		// ***************************************************************

		// This file defines the base tables needed for some INFORMATION_SCHEMA tests.

		// The creation of this schema is supported only at INTERMEDIATE level
		// CREATE SCHEMA CTS1b;

		// Because this is executed under CTS1, all the identifiers must be fully
		// qualified to prevent them from ending up in schema 
	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF7 (EMPNUM    CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE DECIMAL(4), " +
					"CITY   CHAR(15), " +
					"PRIMARY KEY (EMPNUM), " +
					"CHECK (GRADE BETWEEN 1 AND 20));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF7 already exists");
			}	
		}	
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE PROJ_DURATION " +
					"( MONTHS  INTEGER, " +
					"TIME_LEFT   INTEGER, " +
					"EMP_HOURS   INTEGER, " +
					"CHECK (MONTHS > 0));"); 
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table PROJ_DURATION already exists");
			}	
		}	

		// ************* create domain statements ***********    
		 	
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE DOMAIN esal AS INTEGER " +
			"CHECK  (VALUE>500);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return attempt to store duplicate value " +
						"(visible to active transactions) in unique index 'RDB$INDEX_2'");
			}	
		}	

		stmt.executeUpdate("CREATE DOMAIN domchar AS CHARACTER;");

		stmt.executeUpdate("CREATE DOMAIN domsmall AS SMALLINT " +
				"CHECK (VALUE IN (1, 3 ,5, 7));");

		// ************* End of Schema *************	
	}

	
	public static void setupCts5sch4(Statement stmt) throws SQLException {

		// SQL Test Suite, V6.0, Schema Definition, schema4.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID CTS4 ******
		// ***************************************************************

		// This file defines a schema refered to in some xcts tests.

		//  The following command is supported only at INTERMEDIATE level
		//  CREATE SCHEMA CTS4;

		//  The following command should be used if ENTRY level rather than
		//  intermediate is supported.
		//  CREATE SCHEMA AUTHORIZATION CTS4;

		// ************* End of Schema *************	
	}
	
	
	public static void setupCts5sch5(Statement stmt) throws SQLException {

		// SQL Test Suite, V6.0, Schema Definition, schema5.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID CTS3 ******
		// ***************************************************************

		// This file defines a schema refered to in some xcts tests.

		//  The following command is supported only at INTERMEDIATE level
		//  CREATE SCHEMA CTS3;

		//  The following command should be used if ENTRY level rather than
		//  intermediate is supported.
		//  CREATE SCHEMA AUTHORIZATION CTS3;

		// ************* End of Schema ************	
	}
		
	
	public static void setupReport(Statement stmt) throws SQLException {
		
		// Schema for reporting structure of SQL Test Suite, Version 5.0
		// Followed by sample data and queries

		// =========================================================
		// Static tables to define the test suite structure
		// =========================================================

		// This table is an enumeration of all features 
		// and collections of features (profiles) to be tested.
		// This is a reference table of codes (FEATURE1) and a
		// lookup table of names.
		// A profile has the value P in column1 of FEATURE1.
		// A logical profile has the value L in column1 of FEATURE1.
		// A lowest-level "leaf" feature is numeric in column1 of FEATURE1.
		// [A logical profile is a convenient grouping of features or
		// profiles, for purposes of recursion, but not reporting.].

		stmt.executeUpdate("CREATE TABLE REPORTFEATURE " +
				"(FEATURE1      CHAR(4) NOT NULL PRIMARY KEY, " +
				"FEATURENAME   CHAR(30) NOT NULL);");
		
		// This table describes the reporting structure for SQL testing;
		// i.e., the network of relationships between REPORTFEATURE rows.
		// Each row is a directed arc in the network.

		// Profiles and logical profiles are nodes in the hierarchy.

		stmt.executeUpdate("CREATE TABLE IMPLICATION " +
				"(PARENT_F  CHAR(4) NOT NULL REFERENCES REPORTFEATURE, " +
				"CHILD_F   CHAR(4) NOT NULL REFERENCES REPORTFEATURE);");

		// List of programs, authorization identifiers, and
		// special notes on how to run each the program.
		// P_NOTE indicates whether a test is a concurrency test,
		// requires a subroutine, etc.

		stmt.executeUpdate("CREATE TABLE TESTPROG " +
				"(PROG      CHAR(6) NOT NULL PRIMARY KEY, " +
				"AUTHID    CHAR(18) NOT NULL, " +
				"P_NOTE    CHAR(10));");

		// List of test cases, descriptions, containing program, and
		// special notes on operational problems, such as
		// may not compile, may cause segmentation error, requires
		// visual inspection (ergo no pass/fail in TESTREPORT), etc.

		stmt.executeUpdate("CREATE TABLE TESTCASE " +
				"(TESTNO    CHAR(4) NOT NULL PRIMARY KEY, " +
				"DESCR     CHAR(50) NOT NULL, " +
				"PROG      CHAR(6) NOT NULL REFERENCES TESTPROG, " +
				"T_NOTE    CHAR(10), " +
				"ISQL_CT   DECIMAL(2) NOT NULL);");

		// Each test is for one or more features.
		// This table describes the test cases in the programs.

		stmt.executeUpdate("CREATE TABLE TESTFEATURE " +
				"(TESTNO    CHAR(4) NOT NULL REFERENCES TESTCASE, " +
				"FEATURE1  CHAR(4) NOT NULL REFERENCES REPORTFEATURE, " +
				"PRIMARY KEY (TESTNO, FEATURE1));");

		// On-line cross reference to SQL-92 (population deferred)
		// Sequence number is decimal to facilitate adding references
		// between existing references without renumbering.
		// [same idea as Dewey Decimal system used in libraries]

		stmt.executeUpdate("CREATE TABLE TESTREFERENCE " +
				"(TESTNO    CHAR(4) NOT NULL REFERENCES TESTCASE, " +
				"SEQ       DECIMAL (6,4) NOT NULL, " +
				"TESTREF   CHAR(50) NOT NULL, " +
				"PRIMARY KEY (TESTNO, SEQ));");



		// =========================================================
		// Tables to specify vendor claims follow:
		// =========================================================

		// Features supported for this testing:

		stmt.executeUpdate("CREATE TABLE FEATURE_CLAIMED " +
				"(FEATURE1  CHAR(4) NOT NULL UNIQUE REFERENCES REPORTFEATURE);");

		// Bindings supported for this testing:

		stmt.executeUpdate("CREATE TABLE BINDING_CLAIMED " +
				"(BINDING1  CHAR(3) NOT NULL UNIQUE, " +
				"CHECK (BINDING1 IN " +
				"('PCO','PFO','PC','PPA','PAD','PMU','PPL', " +
				"'MCO','MFO','MC','MPA','MAD','MMU','MPL','SQL')));");

		// =========================================================
		// Tables to generate vendor-specific requirements follow:
		// =========================================================

		// Features required, to be derived recursively,
		// including claim to be tested -- C1,
		// lowest reporting profile -- P1, and
		// lowest recursive link -- L1.
		// F1 is the feature to be tested.

		stmt.executeUpdate("CREATE TABLE F_REQ " +
				"(C1           CHAR(4) NOT NULL, " +
				"P1           CHAR(4) NOT NULL, " +
				"F1           CHAR(4) NOT NULL, " +
				"LVL          INTEGER);");

		// Working version of F_REQ, 
		// needed because an INSERT cannot be self-referencing.

		stmt.executeUpdate("CREATE TABLE F_TEMP " +
				"(C1           CHAR(4) NOT NULL, " +
				"P1           CHAR(4) NOT NULL, " +
				"F1           CHAR(4), " +
				"LVL          INTEGER);");

		stmt.executeUpdate("CREATE TABLE R_STRUCTURE " +
				"(C1        CHAR(4) NOT NULL, " +
				"P1        CHAR(4) NOT NULL, " +
				"TESTNO    CHAR(4) NOT NULL, " +
				"LVL       INTEGER);");

		// Tests selected for this validation,
		// corresponding to the features selected and
		// corresponding to the bindings selected.
		// Result will be derived later from TESTREPORT.

		stmt.executeUpdate("CREATE TABLE T_REQ " +
				"(TESTNO    CHAR(4) NOT NULL, " +
				"PROG      CHAR(6) NOT NULL, " +
				"BINDING1  CHAR(3) NOT NULL, " +
				"REQOPTNA  CHAR(3) NOT NULL, " +
				"RESULT    CHAR(4));");

		// TODO: Auto-commit is on by default.
		// COMMIT WORK;
		//	
	}
	
	
	public static void setupSchema1(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		int       rowCount;  // Row count.

		// SQL Test Suite, V6.0, Schema Definition, schema1.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID HU ******
		// ***************************************************************

		// This file defines the base tables used in most of the tests.

		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the 
		// remaining tests.

		// CREATE SCHEMA AUTHORIZATION HU;

		stmt.executeUpdate("CREATE TABLE BASE_TESTREPORT " +
				"(TESTNO   CHAR(4), " +
				"RESULT   CHAR(4), " +
				"TESTTYPE  CHAR(3));");

		stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2));");

		stmt.executeUpdate("CREATE TABLE STAFF " +
				"(EMPNUM   CHAR(3) NOT NULL UNIQUE, " +
				"EMPNAME  CHAR(20), " +
				"GRADE    DECIMAL(4), " +
				"CITY     CHAR(15));");


		stmt.executeUpdate("CREATE TABLE PROJ " +
				"(PNUM     CHAR(3) NOT NULL UNIQUE, " +
				"PNAME    CHAR(20), " +
				"PTYPE    CHAR(6), " +
				"BUDGET   DECIMAL(9), " +
				"CITY     CHAR(15));");


		stmt.executeUpdate("CREATE TABLE WORKS " +
				"(EMPNUM   CHAR(3) NOT NULL, " +
				"PNUM     CHAR(3) NOT NULL, " +
				"HOURS    DECIMAL(5), UNIQUE(EMPNUM,PNUM));");



		stmt.executeUpdate("CREATE TABLE FIPS1 " +
				"(FIPS_TEST CHAR(20));");




		stmt.executeUpdate("CREATE TABLE STAFF1 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE PROJ1 (PNUM    CHAR(3) NOT NULL UNIQUE, " +
				"PNAME  CHAR(20), " +
				"PTYPE  CHAR(6), " +
				"BUDGET DECIMAL(9), " +
				"CITY   CHAR(15));");


		stmt.executeUpdate("CREATE TABLE WORKS1(EMPNUM    CHAR(3) NOT NULL, " +
				"PNUM    CHAR(3) NOT NULL, " +
				"HOURS   DECIMAL(5), " +
				"UNIQUE(EMPNUM, PNUM));");


		stmt.executeUpdate("CREATE TABLE STAFF3 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE STAFF4 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15));");


		stmt.executeUpdate("CREATE TABLE LONGINT (LONG_INT DECIMAL(15));");


		stmt.executeUpdate("CREATE TABLE TEMP_S " +
				"(EMPNUM  CHAR(3), " +
				"GRADE DECIMAL(4), " +
				"CITY CHAR(15));");


		stmt.executeUpdate("CREATE TABLE TMP (T1 CHAR (10), T2 DECIMAL(2), T3 CHAR (10));");


		
		stmt.executeUpdate("CREATE TABLE AA (CHARTEST     CHAR(20));");

		stmt.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");

		stmt.executeUpdate("CREATE TABLE CC (CHARTEST     CHARACTER(20));");

		stmt.executeUpdate("CREATE TABLE DD (CHARTEST     CHARACTER);");

		stmt.executeUpdate("CREATE TABLE EE (INTTEST     INTEGER);");

		stmt.executeUpdate("CREATE TABLE FF (INTTEST     INT);");

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

		stmt.executeUpdate("CREATE TABLE P1 (NUMTEST  NUMERIC(1));");

		stmt.executeUpdate("CREATE TABLE P7 (NUMTEST  NUMERIC(7));");

		stmt.executeUpdate("CREATE TABLE P12 (NUMTEST  NUMERIC(12));");

		stmt.executeUpdate("CREATE TABLE P15 (NUMTEST  NUMERIC(15));");



		stmt.executeUpdate("CREATE TABLE VTABLE " +
				"(COL1   INTEGER, " +
				"COL2   INTEGER, " +
				"COL3   INTEGER, " +
				"COL4   INTEGER, " +
				"COL5   DECIMAL(7,2));");



		stmt.executeUpdate("CREATE TABLE UPUNIQ (NUMKEY  DECIMAL(3) NOT NULL UNIQUE, " +
				"COL2    CHAR(2));");




		stmt.executeUpdate("CREATE TABLE TEXT80  (TEXXT CHAR(80));");
		stmt.executeUpdate("CREATE TABLE TEXT132  (TEXXT CHAR(132));");
		stmt.executeUpdate("CREATE TABLE TEXT240  (TEXXT CHAR(240));");
		stmt.executeUpdate("CREATE TABLE TEXT256  (TEXXT CHAR(256));");
		stmt.executeUpdate("CREATE TABLE TEXT512  (TEXXT CHAR(512));");
		stmt.executeUpdate("CREATE TABLE TEXT1024  (TEXXT CHAR(1024));");

		// The following tables are used to test the limitations (12-14-88)


		stmt.executeUpdate("CREATE TABLE T240(STR240 CHAR(240));");

		stmt.executeUpdate("CREATE TABLE DEC15(COL1 DECIMAL(15,7));");

		stmt.executeUpdate("CREATE TABLE FLO15(COL1 FLOAT(15));");

		stmt.executeUpdate("CREATE TABLE INT10(COL1 INTEGER, COL2 SMALLINT);");

		stmt.executeUpdate("CREATE TABLE T100(C1 CHAR(2),C2 CHAR(2),C3 CHAR(2),C4 CHAR(2), " +
				"C5 CHAR(2),C6 CHAR(2),C7 CHAR(2),C8 CHAR(2), " +
				"C9 CHAR(2),C10 CHAR(2),C11 CHAR(2),C12 CHAR(2), " +
				"C13 CHAR(2),C14 CHAR(2),C15 CHAR(2),C16 CHAR(2), " +
				"C17 CHAR(2),C18 CHAR(2),C19 CHAR(2),C20 CHAR(2), " +
				"C21 CHAR(2),C22 CHAR(2),C23 CHAR(2),C24 CHAR(2), " +
				"C25 CHAR(2),C26 CHAR(2),C27 CHAR(2),C28 CHAR(2), " +
				"C29 CHAR(2),C30 CHAR(2),C31 CHAR(2),C32 CHAR(2), " +
				"C33 CHAR(2),C34 CHAR(2),C35 CHAR(2),C36 CHAR(2), " +
				"C37 CHAR(2),C38 CHAR(2),C39 CHAR(2),C40 CHAR(2), " +
				"C41 CHAR(2),C42 CHAR(2),C43 CHAR(2),C44 CHAR(2), " +
				"C45 CHAR(2),C46 CHAR(2),C47 CHAR(2),C48 CHAR(2), " +
				"C49 CHAR(2),C50 CHAR(2),C51 CHAR(2),C52 CHAR(2), " +
				"C53 CHAR(2),C54 CHAR(2),C55 CHAR(2),C56 CHAR(2), " +
				"C57 CHAR(2),C58 CHAR(2),C59 CHAR(2),C60 CHAR(2), " +
				"C61 CHAR(2),C62 CHAR(2),C63 CHAR(2),C64 CHAR(2), " +
				"C65 CHAR(2),C66 CHAR(2),C67 CHAR(2),C68 CHAR(2), " +
				"C69 CHAR(2),C70 CHAR(2),C71 CHAR(2),C72 CHAR(2), " +
				"C73 CHAR(2),C74 CHAR(2),C75 CHAR(2),C76 CHAR(2), " +
				"C77 CHAR(2),C78 CHAR(2),C79 CHAR(2),C80 CHAR(2), " +
				"C81 CHAR(2),C82 CHAR(2),C83 CHAR(2),C84 CHAR(2), " +
				"C85 CHAR(2),C86 CHAR(2),C87 CHAR(2),C88 CHAR(2), " +
				"C89 CHAR(2),C90 CHAR(2),C91 CHAR(2),C92 CHAR(2), " +
				"C93 CHAR(2),C94 CHAR(2),C95 CHAR(2),C96 CHAR(2), " +
				"C97 CHAR(2),C98 CHAR(2),C99 CHAR(2),C100 CHAR(2));");

		stmt.executeUpdate("CREATE TABLE T2000(STR110 CHAR(110),STR120 CHAR(120), " +
				"STR130 CHAR(130),STR140 CHAR(140), " +
				"STR150 CHAR(150),STR160 CHAR(160), " +
				"STR170 CHAR(170),STR180 CHAR(180), " +
				"STR190 CHAR(190),STR200 CHAR(200), " +
				"STR210 CHAR(210),STR216 CHAR(216));");

		stmt.executeUpdate("CREATE TABLE T8(COL1 CHAR(2) NOT NULL,COL2 CHAR(4) NOT NULL, " +
				"COL3 CHAR(6) NOT NULL,COL4 CHAR(8) NOT NULL, " +
				"COL5 CHAR(10) NOT NULL,COL6 CHAR(12) NOT NULL, " +
				"COL7 CHAR(14),COL8 CHAR(16), " +
				"UNIQUE(COL1,COL2,COL3,COL4,COL5,COL6));");

		stmt.executeUpdate("CREATE TABLE T118(STR118 CHAR(118) NOT NULL UNIQUE);");

		stmt.executeUpdate("CREATE TABLE T4(STR110 CHAR(110) NOT NULL, " +
				"NUM6   NUMERIC(6) NOT NULL, " +
				"COL3   CHAR(10),COL4 CHAR(20), " +
				"UNIQUE(STR110,NUM6));");

		stmt.executeUpdate("CREATE TABLE T12(COL1 CHAR(1), COL2 CHAR(2), " +
				"COL3 CHAR(4), COL4 CHAR(6), " +
				"COL5 CHAR(8), COL6 CHAR(10), " +
				"COL7 CHAR(20), COL8 CHAR(30), " +
				"COL9 CHAR(40), COL10 CHAR(50), " +
				"COL11 INTEGER, COL12 INTEGER);");


		   
		stmt.executeUpdate("CREATE TABLE NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");


		stmt.executeUpdate("CREATE TABLE SV (NUMTEST NUMERIC(8,3));");

		stmt.executeUpdate("CREATE TABLE JJ_20 (FLOATTEST  FLOAT(20));");

		stmt.executeUpdate("CREATE TABLE PP_15 (NUMTEST  DECIMAL(15,15));");
		 
		stmt.executeUpdate("CREATE TABLE PP_7  (NUMTEST  DECIMAL(15,7));");

		stmt.executeUpdate("CREATE TABLE P15_15 (NUMTEST  NUMERIC(15,15));");

		stmt.executeUpdate("CREATE TABLE P15_7 (NUMTEST  NUMERIC(15,7));");

		stmt.executeUpdate("CREATE TABLE TEMP_OBSERV " +
				"(YEAR_OBSERV  NUMERIC(4), " +
				"CITY         CHAR(10), " +
				"MAX_TEMP     NUMERIC(5,2), " +
				"MIN_TEMP     NUMERIC(5,2));");

		stmt.executeUpdate("CREATE TABLE TOKENS " +
				"(PROG_NO INT, TOKEN_NO INT);");

		stmt.executeUpdate("CREATE TABLE WHICH_SCHEMA1 (C1 CHAR (1));");

		// ************* create view statements follow *************

		stmt.executeUpdate("CREATE VIEW TESTREPORT AS " +
				"SELECT TESTNO, RESULT, TESTTYPE " +
				"FROM BASE_TESTREPORT;");

		stmt.executeUpdate("CREATE VIEW CELSIUS_OBSERV (CITY, YEAR_OBSERV, MIN_C, MAX_C) " +
				"AS SELECT CITY, YEAR_OBSERV, (MIN_TEMP - 32) * 5 / 9, " +
				"(MAX_TEMP - 32) * 5 / 9 " +
				"FROM TEMP_OBSERV;");

		stmt.executeUpdate("CREATE VIEW MULTI_YEAR_OBSERV (CITY, HIGH, LOW) " +
				"AS SELECT CITY, AVG(MAX_TEMP), AVG(MIN_TEMP) " +
				"FROM TEMP_OBSERV " +
				"GROUP BY CITY;");

		stmt.executeUpdate("CREATE VIEW EXTREME_TEMPS (YEAR_OBSERV, HIGH, LOW) " +
				"AS SELECT YEAR_OBSERV, MAX(MAX_TEMP), MIN(MIN_TEMP) " +
				"FROM TEMP_OBSERV " +
				"GROUP BY YEAR_OBSERV;");

		stmt.executeUpdate("CREATE VIEW SET_TEST (EMP1, EMP_AVG, EMP_MAX) AS " +
				"SELECT STAFF.EMPNUM, AVG(HOURS), MAX(HOURS) " +
				"FROM STAFF, WORKS " +
				"GROUP BY STAFF.EMPNUM;");
		          
		stmt.executeUpdate("CREATE VIEW DUP_COL (EMP1, PNO, HOURS, HOURS_2) AS " +
				"SELECT EMPNUM, PNUM, HOURS, HOURS * 2 " +
				"FROM WORKS;");


		stmt.executeUpdate("CREATE VIEW STAFFV1 " +
				"AS SELECT * FROM STAFF " +
				"WHERE  GRADE >= 12;");

		stmt.executeUpdate("CREATE VIEW STAFFV2 " +
				"AS SELECT * FROM STAFF " +
				"WHERE  GRADE >= 12 " +
				"WITH CHECK OPTION;");


		  
		stmt.executeUpdate("CREATE VIEW STAFFV2_VIEW " +
				"AS SELECT * " +
				"FROM   STAFFV2 " +
				"WHERE  CITY = 'Vienna';");


		stmt.executeUpdate("CREATE VIEW DOMAIN_VIEW " +
				"AS SELECT * " +
				"FROM   WORKS " +
				"WHERE  EMPNUM = 'E1' AND HOURS = 80 " +
				"OR EMPNUM = 'E2' AND HOURS = 40 " +
				"OR EMPNUM = 'E4' AND HOURS = 20 " +
				"WITH CHECK OPTION;");

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("CREATE VIEW STAFF2 " +
						"AS SELECT *  " +
						"FROM   STAFF " +
						"WITH CHECK OPTION;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return No WHERE clause for VIEW WITH CHECK OPTION");
			}	
		}



		stmt.executeUpdate("CREATE VIEW STAFF_WORKS_DESIGN (NAME,COST,PROJECT) " +
				"AS SELECT EMPNAME,HOURS*2*GRADE,PNAME " +
				"FROM   PROJ,STAFF,WORKS " +
				"WHERE  STAFF.EMPNUM=WORKS.EMPNUM " +
				"AND WORKS.PNUM=PROJ.PNUM " +
				"AND PTYPE='Design';");


		stmt.executeUpdate("CREATE VIEW SUBSP (EMPNUM,PNUM,HOURS) " +
				"AS SELECT EMPNUM,PNUM,HOURS " +
				"FROM   WORKS " +
				"WHERE  EMPNUM='E3' " +
				"WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE VIEW TEMP_SS(EMPNUM,GRADE,CITY) " +
				"AS SELECT EMPNUM,GRADE,CITY " +
				"FROM   STAFF " + 
				"WHERE  GRADE > 12 " +
				"WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE VIEW V_WORKS1 " +
				"AS SELECT * FROM WORKS " +
				"WHERE HOURS > 15 " +
				"WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE VIEW V_WORKS2 " +
				"AS SELECT * FROM V_WORKS1 " +
				"WHERE EMPNUM = 'E1' " +
				"OR EMPNUM = 'E6';");

		stmt.executeUpdate("CREATE VIEW V_WORKS3 " +
				"AS SELECT * FROM V_WORKS2 " +
				"WHERE PNUM = 'P2' " +
				"OR PNUM = 'P7' " +
				"WITH CHECK OPTION;"); 

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW1 " +
				"AS SELECT ALL CITY " +
				"FROM PROJ;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW2 " +
				"AS SELECT HOURS, EMPNUM, PNUM " +
				"FROM WORKS " +
				"WHERE HOURS IN (10, 20, 40);");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW3 " +
				"AS SELECT * " +
				"FROM WORKS " +
				"WHERE PNUM BETWEEN 'P2' AND 'P4' " +
				"AND EMPNUM NOT BETWEEN 'E2' AND 'E3';");
		 
		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW4 " +
				"AS SELECT PNUM, EMPNUM " +
				"FROM WORKS " +
				"WHERE PNUM LIKE '_2%';");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW5 " +
				"AS SELECT * " +
				"FROM STAFF " +
				"WHERE EMPNAME IS NOT NULL AND CITY IS NULL;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW6 " +
				"AS SELECT EMPNAME, CITY, GRADE " +
				"FROM STAFF " +
				"WHERE EMPNAME >= 'Betty' AND EMPNUM < 'E35' " +
				"OR CITY <= 'Deale' AND GRADE > 12 " +
				"OR GRADE = 13 AND CITY <> 'Akron';");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW7 " +
				"AS SELECT EMPNAME, CITY, GRADE " +
				"FROM STAFFV2 " +
				"WHERE EMPNAME >= 'Betty' AND EMPNUM < 'E35' " +
				"OR CITY <= 'Deale' AND GRADE > 12 " +
				"OR GRADE = 13 AND CITY <> 'Akron';");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW8 " +
				"AS SELECT MYTABLE.EMPNUM, MYTABLE.EMPNAME " +
				"FROM STAFF MYTABLE " +
				"WHERE MYTABLE.GRADE = 12;");

		stmt.executeUpdate("CREATE VIEW UPDATE_VIEW9 " +
				"AS SELECT EMPNAME, CITY, GRADE " +
				"FROM STAFF " +
				"WHERE NOT EMPNAME >= 'Betty' AND EMPNUM <= 'E35' " +
				"OR NOT (CITY <= 'Deale') AND GRADE > 9 " +
				"AND NOT (GRADE = 13 AND CITY <> 'Akron') " +
				"OR NOT CITY IN ('Vienna','New York','Deale');");

		stmt.executeUpdate("CREATE VIEW VSTAFF3 AS SELECT * FROM STAFF3;");

		// ************* grant statements follow *************
		stmt.executeUpdate("GRANT SELECT ON ECCO TO PUBLIC;");

		stmt.executeUpdate("GRANT INSERT ON TESTREPORT TO PUBLIC WITH GRANT OPTION;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON TESTREPORT " +
				"TO SUN, CTS1, XOPEN1 WITH GRANT OPTION;");

		stmt.executeUpdate("GRANT SELECT ON STAFF TO SULLIVAN1 WITH GRANT OPTION;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON STAFF " +
				"TO PUBLIC;");

		stmt.executeUpdate("GRANT SELECT ON WORKS " +
				"TO PUBLIC;");

		stmt.executeUpdate("GRANT SELECT ON PROJ " +
				"TO PUBLIC;");

		stmt.executeUpdate("GRANT SELECT,UPDATE ON WORKS " +
				"TO CUGINI " +
				"WITH GRANT OPTION;");

		errorCode = 0;
		try {
			rowCount = stmt.executeUpdate("GRANT SELECT,UPDATE ON STAFF2 " +
						"TO SULLIVAN;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return table/view STAFF2 does not exist");
			}	
		}

		stmt.executeUpdate("GRANT SELECT,UPDATE(EMPNUM,EMPNAME) ON STAFF3 " +
				"TO SULLIVAN, CUGINI, FLATER, SULLIVAN1 " +
				"WITH GRANT OPTION;");

		stmt.executeUpdate("GRANT SELECT,INSERT, DELETE ON STAFF4 " +
				"TO SULLIVAN, CUGINI, FLATER, SULLIVAN1;");


		stmt.executeUpdate("GRANT INSERT ON UPUNIQ TO SULLIVAN;");

		stmt.executeUpdate("GRANT SELECT,UPDATE(EMPNUM,EMPNAME) ON VSTAFF3 " +
				"TO FLATER WITH GRANT OPTION;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON HH TO SCHANZLE;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON FF TO SCHANZLE;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON P1 TO SCHANZLE;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON P15 TO FLATER;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON T100 TO FLATER;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON TOKENS TO PUBLIC;");

		stmt.executeUpdate("GRANT UPDATE (COL1) ON VTABLE TO FLATER;");

		// ************* End of Schema *************
	}

	public static void setupSchema2(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, schema2.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID CUGINI
		// ***************************************************************

		// This is non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the
		// remaining tests.

		// CREATE SCHEMA AUTHORIZATION CUGINI;

		stmt.executeUpdate("GRANT SELECT,UPDATE ON WORKS " +
			"TO SULLIVAN;");
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE VTABLE " +
					"(COL1  INTEGER, " +
					"COL2  INTEGER, " +
					"COL3  INTEGER, " +
					"COL4  INTEGER, " +
					"COL5  DECIMAL(7,2));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table VTABLE already exists");
			}	
		}
			
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON VTABLE TO HU;");

		// Test GRANT without grant permission below -- expect error message!
		// "WITH GRANT OPTION" purposefully omitted from HU's GRANT on STAFF4. 
		// Do not change file SCHEMA1 to allow "WITH GRANT OPTION" on STAFF4.

		stmt.executeUpdate("GRANT SELECT, INSERT, DELETE ON STAFF4 " +
				"TO SCHANZLE;");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE AA (CHARTEST CHAR(20));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table AA already exists");
			}	
		}	
		
		stmt.executeUpdate("CREATE VIEW VAA AS SELECT * FROM AA;");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE BB (CHARTEST     CHAR);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table BB already exists");
			}	
		}		
		
		stmt.executeUpdate("CREATE VIEW VBB AS SELECT * FROM BB;");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE CC (CHARTEST     CHARACTER(20));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table CC already exists");
			}	
		}		
		
		stmt.executeUpdate("CREATE VIEW VCC AS SELECT * FROM CC;");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE DD (CHARTEST     CHARACTER);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table DD already exists");
			}	
		}		
		
		stmt.executeUpdate("CREATE VIEW VDD AS SELECT * FROM DD;");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE EE (INTTEST     INTEGER);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table EE already exists");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE FF (INTTEST     INT);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table FF already exists");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE GG (C1 INT);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table GG already exists");
			}	
		}		
	
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE HH (SMALLTEST  SMALLINT);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table HH already exists");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE II (C1 INT);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table II already exists");
			}	
		}		
		
		stmt.executeUpdate("CREATE VIEW VII AS SELECT * FROM II;");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE JJ (C1 INT);");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table JJ already exists");
			}	
		}		
		
		stmt.executeUpdate("CREATE TABLE SRCH1 (C1 INT);");
		stmt.executeUpdate("CREATE TABLE BADG1 (C1 INT);");

		stmt.executeUpdate("CREATE TABLE BADG2 (C1 INT);");
		stmt.executeUpdate("CREATE VIEW VBADG2 AS SELECT * FROM BADG2;");

		stmt.executeUpdate("CREATE VIEW VVTABLE AS SELECT * FROM VTABLE;");

		stmt.executeUpdate("GRANT SELECT ON AA TO SCHANZLE;");
		stmt.executeUpdate("GRANT SELECT ON VAA TO SCHANZLE;");
		stmt.executeUpdate("GRANT INSERT ON BB TO SCHANZLE;");
		stmt.executeUpdate("GRANT INSERT ON VBB TO SCHANZLE;");
		stmt.executeUpdate("GRANT UPDATE ON CC TO SCHANZLE;");
		stmt.executeUpdate("GRANT UPDATE ON VCC TO SCHANZLE;");
		stmt.executeUpdate("GRANT DELETE ON DD TO SCHANZLE;");
		stmt.executeUpdate("GRANT DELETE ON VDD TO SCHANZLE;");
		stmt.executeUpdate("GRANT SELECT ON EE TO PUBLIC;");
		stmt.executeUpdate("GRANT INSERT ON FF TO PUBLIC;");
		stmt.executeUpdate("GRANT UPDATE ON GG TO PUBLIC;");
		stmt.executeUpdate("GRANT DELETE ON HH TO PUBLIC;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON II TO SCHANZLE;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON VII TO SCHANZLE;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON JJ TO PUBLIC;");
		stmt.executeUpdate("GRANT INSERT, UPDATE, DELETE ON SRCH1 TO SCHANZLE;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON BADG1 TO FLATER;");
		stmt.executeUpdate("GRANT SELECT ON VBADG2 TO FLATER WITH GRANT OPTION;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON VVTABLE TO HU;");

		// ************* End of Schema *************		
	}	

	
	public static void setupSchema3(Statement stmt) throws SQLException {
		
		// SQL Test Suite, V6.0, Schema Definition, schema3.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID MCGINN **
		// ***************************************************************

		// CREATE SCHEMA AUTHORIZATION MCGINN

		// This file contains extensions to the SQL standard.
		// Failure to create this schema is not a deficiency.

		// This file tests extensions to SQL to allow names longer than 18.
		// If a create table or create view statement causes a syntax error,
		// delete that create table or create view statement.

		stmt.executeUpdate("CREATE TABLE TABLEFGHIJKLMNOPQ19 " +
				"(COL2  INTEGER);");

		stmt.executeUpdate("CREATE TABLE SHORTTABLE " +
				"(COLUMN123456789IS19  INTEGER);");

		stmt.executeUpdate("CREATE TABLE BASETABLE1 (COL1  INTEGER);");

		stmt.executeUpdate("CREATE VIEW VIEWABCDEFGHIKLMN19 (COL3) " +
				"AS SELECT COL1 FROM BASETABLE1;");


		// ************* End of Schema *************	
	}
	

	public static void setupSchema4(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, schema4.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ******************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID SULLIVAN1
		// ******************************************************************

		// This non-standard standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the
		// remaining tests.

		// CREATE SCHEMA AUTHORIZATION SULLIVAN1;

		stmt.executeUpdate("CREATE TABLE AUTH_TABLE (FIRST1 INTEGER, SECOND2 CHAR);");

		stmt.executeUpdate("CREATE VIEW MUL_SCH " +
				"AS SELECT EMPNUM, SECOND2 FROM STAFF, AUTH_TABLE " +
				"WHERE GRADE = FIRST1;");
		    
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON AUTH_TABLE TO HU;");
		stmt.executeUpdate("GRANT SELECT ON MUL_SCH TO HU;");

		// The following tables are used to run concurrency program pairs
		// e.g MPA001 and MPB001 use the tables with prefix MP1_ 

		stmt.executeUpdate("CREATE TABLE MP1_MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE MP1_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP1_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");

		stmt.executeUpdate("CREATE TABLE MP2_MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE MP2_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP2_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");

		stmt.executeUpdate("CREATE TABLE MP3_MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE MP3_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP3_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");

		stmt.executeUpdate("CREATE TABLE MP4_MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE MP4_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP4_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");

		stmt.executeUpdate("CREATE TABLE MP5_AA (ANUM NUMERIC(4));");
		stmt.executeUpdate("CREATE TABLE MP5_AA_INDEX (ANUM NUMERIC(4) NOT NULL UNIQUE);");
		stmt.executeUpdate("CREATE TABLE MP5_TT (TESTTYPE CHAR(3), KOUNT DECIMAL(4));");

		// The following tables are used to run interactive concurrency program pairs.

		stmt.executeUpdate("CREATE TABLE TTT (ANUM NUMERIC(4) NOT NULL UNIQUE, AUTHOR CHAR(1));");
		stmt.executeUpdate("CREATE TABLE TT (DOLLARS NUMERIC(4), ANUM NUMERIC(4));");
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE AA (ANUM NUMERIC(4));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table AA already exists");
			}	
		}		
			
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE BB (BNUM NUMERIC(4));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table BB already exists");
			}	
		}		

		// Test GRANT UPDATE for additional columns beyond those WITH GRANT OPTION.
		// expect error message!
		// In SCHEMA1 for USER HU is the following grant:
		// GRANT SELECT,UPDATE(EMPNUM,EMPNAME) ON STAFF3
		// TO ..., SULLIVAN1 WITH GRANT OPTION
		// If the following self-grant by SULLIVAN1 will not compile/execute, delete it.
		stmt.executeUpdate("GRANT SELECT,UPDATE ON STAFF3 TO SULLIVAN1 " +
				"WITH GRANT OPTION;");


		stmt.executeUpdate("CREATE TABLE MP6_MM2 (NUMTEST NUMERIC(10));");
		stmt.executeUpdate("CREATE TABLE MP6_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP6_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");
		stmt.executeUpdate("CREATE TABLE MP6_AA (ANUM NUMERIC(4));");
		stmt.executeUpdate("CREATE TABLE MP6_BB (BNUM NUMERIC(4));");


		stmt.executeUpdate("CREATE TABLE MP7_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP7_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");
		stmt.executeUpdate("CREATE TABLE MP7_AA (ANUM NUMERIC(4));");
		stmt.executeUpdate("CREATE TABLE MP7_BB (BNUM NUMERIC(4));");


		stmt.executeUpdate("CREATE TABLE MP8_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP8_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");
		stmt.executeUpdate("CREATE TABLE MP8_AA (ANUM NUMERIC(4) NOT NULL, " +
				"AUTHOR CHAR(1), UNIQUE (ANUM));");
		stmt.executeUpdate("CREATE TABLE MP8_BB (NUMTEST NUMERIC(9));");

		stmt.executeUpdate("CREATE TABLE MP9_NN (NUMTEST NUMERIC(9));");
		stmt.executeUpdate("CREATE TABLE MP9_NEXTKEY (KEYNUM INTEGER, AUTHOR CHAR(1), " +
				"DOLLARS INTEGER);");
		stmt.executeUpdate("CREATE TABLE MP9_AA (ANUM NUMERIC(4));");
		stmt.executeUpdate("CREATE TABLE MP9_BB (BNUM NUMERIC(4));");

		stmt.executeUpdate("CREATE TABLE USG102 (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE USG103 (C1 INT, C_1 INT);");

		// ************* End of Schema *************	
	}
	
	
	public static void setupSchema5(Statement stmt) throws SQLException {
		int       errorCode; // Error code.

		// SQL Test Suite, V6.0, Schema Definition, schema5.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// *******************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID FLATER ******
		// *******************************************************************

		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the 
		// remaining tests.

		// CREATE SCHEMA AUTHORIZATION FLATER;


		// VIEW FR1 tests forward references in schema definitions.  This view
		// was checked by test 0523 in SDL032; that test was removed prior to
		// the release of V4.  I personally believe that two-pass SDL processing
		// is the Right Thing and ought to be required, but I speak only for
		// myself.
		// CREATE VIEW FR1 AS SELECT * FROM DV1

		stmt.executeUpdate("CREATE TABLE CONCATBUF (ZZ CHAR(240));");
		stmt.executeUpdate("CREATE TABLE USIG (C1 INT, C_1 INT);");
		stmt.executeUpdate("CREATE TABLE U_SIG (C1 INT, C_1 INT);");

		stmt.executeUpdate("CREATE VIEW DV1 AS " +
				"SELECT DISTINCT HOURS FROM WORKS;");

		// This small one-column table is used to generate an
		// indicator overflow data exception for SQLSTATE testing.
		// If the table cannot be created, the test is assumed passed.
		// Save the error message and then use TEd to delete the CREATE TABLE
		// as well as the GRANT ALL PRIVILEGES ON TINY TO SCHANZLE below.
		// Use the following TEd change: del *schema5.[sop]* /TINY/
		// Test number 0491 in program DML082 may also need to be deleted.		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TINY (C1 CHAR(33000));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return short integer expected");
			}	
		}		

		// For generation of "with check option violation" SQLSTATE.
		stmt.executeUpdate("CREATE TABLE BASE_WCOV (C1 INT);");
		stmt.executeUpdate("CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE " +
				"C1 > 0 WITH CHECK OPTION;");

		stmt.executeUpdate("CREATE TABLE BASE_VS1 (C1 INT, C2 INT);");
		stmt.executeUpdate("CREATE VIEW VS1 AS SELECT * FROM BASE_VS1 WHERE C1 = 0;");
		stmt.executeUpdate("CREATE VIEW VS2 AS " +
				"SELECT A.C1 FROM BASE_VS1 A WHERE EXISTS " +
				"(SELECT B.C2 FROM BASE_VS1 B WHERE B.C2 = A.C1);");
		stmt.executeUpdate("CREATE VIEW VS3 AS " +
				"SELECT A.C2 FROM BASE_VS1 A WHERE A.C2 IN " +
				"(SELECT B.C1 FROM BASE_VS1 B WHERE B.C1 < A.C2);");
		stmt.executeUpdate("CREATE VIEW VS4 AS " +
				"SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < ALL " +
				"(SELECT B.C2 FROM BASE_VS1 B);");
		stmt.executeUpdate("CREATE VIEW VS5 AS " +
				"SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < SOME " +
				"(SELECT B.C2 FROM BASE_VS1 B);");
		stmt.executeUpdate("CREATE VIEW VS6 AS " +
				"SELECT A.C1 FROM BASE_VS1 A WHERE A.C1 < ANY " +
				"(SELECT B.C2 FROM BASE_VS1 B);");


		errorCode = 0;
		try {
			stmt.executeUpdate("GRANT ALL PRIVILEGES ON TINY TO SCHANZLE;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return table/view TINY does not exist)");
			}	
		}		
		
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON BASE_WCOV TO SCHANZLE;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON WCOV TO SCHANZLE;");
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON VS1 TO SCHANZLE;");


		// Test granting of privileges that we don't have to start with.
		// We have GRANT OPTION, but we should not be able to grant unrestricted
		// update on STAFF3 since our own update is restricted to two columns.
		// Do not change SCHEMA1 to grant unrestricted update.
		// * expect error message *
		stmt.executeUpdate("GRANT SELECT, UPDATE ON STAFF3 TO SCHANZLE;");

		// Same thing for views.
		// * expect error message *
		stmt.executeUpdate("GRANT SELECT, UPDATE ON VSTAFF3 TO SCHANZLE;");

		// See whether GRANT ALL PRIVILEGES gives you GRANT OPTION.
		// It should not.  GRANT OPTION is not technically a privilege.
		// * expect error message *
		stmt.executeUpdate("GRANT SELECT ON BADG1 TO SCHANZLE;");

		// See whether GRANT OPTION on a view gives you GRANT OPTION
		// on the base table.
		// * expect error message *
		stmt.executeUpdate("GRANT SELECT ON BADG2 TO SCHANZLE;");

		// Delimited identifiers.
		// TODO: Unable to get double quotes to work!
		//       stmt.executeUpdate("CREATE VIEW \"SELECT\" as (\"select\") AS SELECT C1 FROM BASE_VS1;");
		//       stmt.executeUpdate("GRANT ALL PRIVILEGES ON \"SELECT\" TO SCHANZLE;");



		// Please be aware of the following errata; they are not being
		// tested here.

		// Check for erratum which allowed duplicate
		// <unique constraint definition>s
		// Reference ISO/IEC JTC1/SC21 N6789 section 11.7 SR7
		// and Annex E #4
		//
		// The following should be flagged or rejected:
		// CREATE TABLE T0512 (C1 INT, C2 INT, C3 INT,
		// UNIQUE (C1,C2), UNIQUE (C3), UNIQUE (C2,C1))

		// Check for erratum which allowed recursive view definitions.
		// Reference ISO/IEC JTC1/SC21 N6789 section 11.19 <view definition> SR4
		// and Annex E #6
		// 
		// The following should be flagged or rejected:
		// CREATE VIEW T0513 (C1, C2) AS
		// SELECT T0513.C2, BASE_VS1.C1 FROM T0513, BASE_VS1

		// ************* End of Schema *************
	}
	
	
	public static void setupSchema6(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, schema6.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// *******************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID FLATER ******
		// *******************************************************************

		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the 
		// remaining tests.

		// This SDL file requires Feature 17 of Transitional SQL (multiple schemas
		// per user).

		// Explicit AUTHORIZATION

		//   CREATE SCHEMA SHIRLEY_HURWITZ
		//     AUTHORIZATION FLATER;

		// Forward reference not applicable to .smi version.

		stmt.executeUpdate("CREATE TABLE MEETINGS (TIMESLOT INT, AGENDA CHAR(4));");
		stmt.executeUpdate("CREATE VIEW NOT_AVAILABLE AS " +
				"SELECT TIMESLOT FROM MEETINGS;");

		// Implicit AUTHORIZATION

		//   CREATE SCHEMA LEN_GALLAGHER;
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TRAVEL ( " +
					"TRAVEL_ORDER CHAR(40) NOT NULL UNIQUE, " +
					"DESTINATION CHAR(40), " +
					"REASON_CODE NUMERIC(9)); " +
					"GRANT SELECT ON TRAVEL TO PUBLIC;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Token unknown)");
			}	
		}		
		
		// ************* End of Schema *************	
	}

	
	public static void setupSchema7(Statement stmt) throws SQLException {
		
		// SQL Test Suite, V6.0, Schema Definition, schema7.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// **************************************************************
		// ***** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID *********
		// ***** CANWEPARSELENGTH18                             *********
		// **************************************************************

		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the
		// remaining tests.

		// used in program SDL026

		//CREATE SCHEMA AUTHORIZATION CANWEPARSELENGTH18;
		stmt.executeUpdate("CREATE TABLE CHARACTER18TABLE18 (CHARS18NAME18CHARS CHAR(4));");
		stmt.executeUpdate("CREATE VIEW  CHARACTERS18VIEW18 (LONGNAME18LONGNAME) " +
				"AS SELECT CHARS18NAME18CHARS " +
				"FROM CHARACTER18TABLE18 " +
				"WHERE CHARS18NAME18CHARS <> 'long';");

		// ************* End of Schema *************	
	}
	
	
	public static void setupSchema8(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, schema8.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID SUN *****
		// ***************************************************************

		// This file defines the base tables used in most of the CDR tests.

		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the
		// remaining tests.


		//  CREATE SCHEMA
		//      AUTHORIZATION SUN;


		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(2));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table ECCO already exists");
			}	
		}		

		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE    DECIMAL(4), " +
					"CITY     CHAR(15));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF already exists");
			}	
		}		


		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE PROJ " +
					"(PNUM     CHAR(3) NOT NULL, " +
					"PNAME    CHAR(20), " +
					"PTYPE    CHAR(6), " +
					"BUDGET   DECIMAL(9), " +
					"CITY     CHAR(15));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table PROJ already exists");
			}	
		}		


		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE WORKS " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"PNUM     CHAR(3) NOT NULL, " +
					"HOURS    DECIMAL(5));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table WORKS already exists");
			}	
		}		

		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF3 " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20), " +
					"GRADE    DECIMAL(4), " +
					"CITY     CHAR(15), " +
					"UNIQUE (EMPNUM));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF3 already exists");
			}	
		}		


		stmt.executeUpdate("CREATE TABLE PROJ3 " +
				"(PNUM     CHAR(3) NOT NULL, " +
				"PNAME    CHAR(20), " +
				"PTYPE    CHAR(6), " +
				"BUDGET   DECIMAL(9), " +
				"CITY     CHAR(15), " +
				"UNIQUE (PNUM));");


		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE WORKS3 " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"PNUM     CHAR(3) NOT NULL, " +
					"HOURS    DECIMAL(5), " +
					"FOREIGN KEY (EMPNUM) REFERENCES STAFF3(EMPNUM), " +
					"FOREIGN KEY (PNUM) REFERENCES PROJ3(PNUM));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return could not find UNIQUE INDEX with specified columns");
			}	
		}		
		

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF4 (EMPNUM    CHAR(3) NOT NULL, " +
					"EMPNAME  CHAR(20) DEFAULT NULL, " +
					"GRADE DECIMAL(4) DEFAULT 0, " +
					"CITY   CHAR(15) DEFAULT '               ');");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF4 already exists");
			}	
		}		
		

		stmt.executeUpdate("CREATE TABLE STAFF14 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20) DEFAULT USER, " +
		        // EMPNAME CHAR precision may be changed to implementation-defined
		        //              precision for value of USER
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15));");


		stmt.executeUpdate("CREATE TABLE STAFF5 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"PRIMARY KEY (EMPNUM), " +
				"CHECK (GRADE > 0 AND GRADE < 20));");


		stmt.executeUpdate("CREATE TABLE STAFF6 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4) CHECK (GRADE > 0 AND GRADE < 20), " +
				"CITY   CHAR(15));");

		stmt.executeUpdate("CREATE TABLE STAFF7 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"PRIMARY KEY (EMPNUM), " +
				"CHECK (GRADE BETWEEN 1 AND 20));");


		stmt.executeUpdate("CREATE TABLE STAFF8 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"PRIMARY KEY (EMPNUM), " +
				"CHECK (EMPNAME IS NOT NULL));");


		stmt.executeUpdate("CREATE TABLE STAFF9 (EMPNUM    CHAR(3) NOT NULL PRIMARY KEY, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"CHECK (EMPNAME NOT LIKE 'T%'));");


		stmt.executeUpdate("CREATE TABLE STAFF10 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"PRIMARY KEY (EMPNUM), " +
				"CHECK (GRADE NOT IN (5,22)));");


		stmt.executeUpdate("CREATE TABLE STAFF11 (EMPNUM    CHAR(3) NOT NULL PRIMARY KEY, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"CHECK (GRADE NOT IN (5,22)  " +
				"AND EMPNAME NOT LIKE 'T%'));");


		stmt.executeUpdate("CREATE TABLE STAFF12 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"PRIMARY KEY (EMPNUM), " +
				"CHECK (NOT GRADE IN (5,22) AND NOT EMPNAME LIKE 'T%'));");


		stmt.executeUpdate("CREATE TABLE STAFF13 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15), " +
				"PRIMARY KEY (EMPNUM), " +
				"CHECK (NOT EMPNAME IS NULL));");

		stmt.executeUpdate("CREATE TABLE STAFF15 (EMPNUM    CHAR(3), " +
				"EMPNAME  CHAR(20) NOT NULL, " +
				"GRADE DECIMAL(4), " +
				"CITY   CHAR(15));");
		   
		stmt.executeUpdate("CREATE TABLE STAFF16 (EMPNUM    CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20) DEFAULT NULL, " +
				"GRADE DECIMAL(4) NOT NULL CHECK (GRADE IN (100,150,200)), " +
				"CITY   CHAR(15), PRIMARY KEY (GRADE,EMPNUM));");
		                
		stmt.executeUpdate("CREATE TABLE SIZ1_P " +
				"(S1   CHAR(3) NOT NULL, " +
				"S2   CHAR(3) NOT NULL, " +
				"S3   DECIMAL(4) NOT NULL, " +
				"S4   CHAR(3) NOT NULL, " +
				"S5   DECIMAL(4) NOT NULL, " +
				"S6   CHAR(3) NOT NULL, " +
				"R1   CHAR(3), " +
				"R2   CHAR(3), " +
				"R3   DECIMAL(4), " +
				"UNIQUE   (S1,S2,S3,S4,S5,S6));");
		        
		        
		stmt.executeUpdate("CREATE TABLE SIZ1_F " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(3), " +
				"F3   DECIMAL(4), " +
				"F4   CHAR(3), " +
				"F5   DECIMAL(4), " +
				"F6   CHAR(3), " +
				"R1   CHAR(3), " +
				"R2   DECIMAL(5), " +
				"R3   DECIMAL(4), " +
				"FOREIGN KEY   (F1,F2,F3,F4,F5,F6) " +
				"REFERENCES SIZ1_P(S1,S2,S3,S4,S5,S6));");
		        
		        
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_P " +
				"(P1   CHAR(3) NOT NULL, " +
				"P2   CHAR(3) NOT NULL, " +
				"P3   DECIMAL(4) NOT NULL, " +
				"P4   CHAR(3) NOT NULL, " +
				"P5   DECIMAL(4) NOT NULL, " +
				"P6   CHAR(3) NOT NULL, " +
				"P7   CHAR(3) NOT NULL, " +
				"P8   DECIMAL(4) NOT NULL, " +
				"P9   DECIMAL(4) NOT NULL, " +
				"P10   DECIMAL(4) NOT NULL, " +
				"P11   CHAR(4), " +
				"UNIQUE (P1), " +
				"UNIQUE (P2), " +
				"UNIQUE (P3), " +
				"UNIQUE (P4), " +
				"UNIQUE (P5), " +
				"UNIQUE (P6), " +
				"UNIQUE (P7), " +
				"UNIQUE (P8), " +
				"UNIQUE (P9), " +
				"UNIQUE (P10));");
		        
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F1 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1)  " +
				"REFERENCES SIZ2_P(P1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F2 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1)  " +
				"REFERENCES SIZ2_P(P2));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F3 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P3));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F4 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P4));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F5 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P5));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F6 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P6));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F7 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P7));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F8 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P8));");
	        
		stmt.executeUpdate("CREATE TABLE SIZ2_F9 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P9));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ2_F10 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"FOREIGN KEY (F1) " +
				"REFERENCES SIZ2_P(P10));");
		        
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P1 " +
				"(F1   CHAR(3) NOT NULL, F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P2 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P3 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P4 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P5 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P6 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P7 " +
				"(F1   CHAR(3) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P8 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P9 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_P10 " +
				"(F1   DECIMAL(4) NOT NULL, " +
				"F2   CHAR(8), " +
				"UNIQUE (F1));");
		        
		stmt.executeUpdate("CREATE TABLE SIZ3_F " +
				"(P1   CHAR(3) NOT NULL, " +
				"P2   CHAR(3), " +
				"P3   DECIMAL(4), " +
				"P4   CHAR(3), " +
				"P5   DECIMAL(4), " +
				"P6   CHAR(3), " +
				"P7   CHAR(3), " +
				"P8   DECIMAL(4), " +
				"P9   DECIMAL(4), " +
				"P10   DECIMAL(4), " +
				"P11   CHAR(4), " +
				"FOREIGN KEY (P1) " +
				"REFERENCES SIZ3_P1(F1), " +
				"FOREIGN KEY (P2) " +
				"REFERENCES SIZ3_P2(F1), " +
				"FOREIGN KEY (P3) " +
				"REFERENCES SIZ3_P3(F1), " +
				"FOREIGN KEY (P4) " +
				"REFERENCES SIZ3_P4(F1), " +
				"FOREIGN KEY (P5) " +
				"REFERENCES SIZ3_P5(F1), " +
				"FOREIGN KEY (P6) " +
				"REFERENCES SIZ3_P6(F1), " +
				"FOREIGN KEY (P7) " +
				"REFERENCES SIZ3_P7(F1), " +
				"FOREIGN KEY (P8) " +
				"REFERENCES SIZ3_P8(F1), " +
				"FOREIGN KEY (P9) " +
				"REFERENCES SIZ3_P9(F1), " +
				"FOREIGN KEY (P10) " +
				"REFERENCES SIZ3_P10(F1));");

		stmt.executeUpdate("CREATE TABLE DEPT " +
				"(DNO DECIMAL(4) NOT NULL, " +
				"DNAME CHAR(20) NOT NULL, " +
				"DEAN CHAR(30), " +
				"PRIMARY KEY (DNO), " +
				"UNIQUE (DNAME));");
		        
		stmt.executeUpdate("CREATE TABLE EMP " +
				"(ENO DECIMAL(4) NOT NULL, " +
				"ENAME CHAR(20) NOT NULL, " +
				"EDESC CHAR(30), " +
				"DNO DECIMAL(4) NOT NULL, " +
				"DNAME CHAR(20), " +
				"BTH_DATE  DECIMAL(6) NOT NULL, " +
				"PRIMARY KEY (ENO), " +
				"UNIQUE (ENAME,BTH_DATE), " +
				"FOREIGN KEY (DNO) REFERENCES " +
				"DEPT(DNO), " +
				"FOREIGN KEY (DNAME) REFERENCES " +
				"DEPT(DNAME));");
		        
		        
		stmt.executeUpdate("CREATE TABLE EXPERIENCE " +
				"(EXP_NAME CHAR(20), " +
				"BTH_DATE DECIMAL(6), " +
				"WK_DATE  DECIMAL(6), " +
				"DESCR CHAR(40), " +
				"FOREIGN KEY (EXP_NAME,BTH_DATE) REFERENCES " +
				"EMP(ENAME,BTH_DATE));");
		        
		// The following tables, STAFF_M and PROJ_M reference each other.
		// Table STAFF_M has a "forward reference" to PROJ_M.
		                                 
		stmt.executeUpdate("CREATE TABLE STAFF_M " +
				"(EMPNUM   CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE    DECIMAL(4), " +
				"CITY     CHAR(15), " +
				"PRI_WK   CHAR(3), " +
				"UNIQUE   (EMPNUM));");

		        
		stmt.executeUpdate("CREATE TABLE PROJ_M " +
				"(PNUM     CHAR(3) NOT NULL, " +
				"PNAME    CHAR(20), " +
				"PTYPE    CHAR(6), " +
				"BUDGET   DECIMAL(9), " +
				"CITY     CHAR(15), " +
				"MGR   CHAR(3), " +
				"UNIQUE (PNUM), " +
				"FOREIGN KEY (MGR) " +
				"REFERENCES STAFF_M(EMPNUM));");

		stmt.executeUpdate("ALTER TABLE STAFF_M ADD FOREIGN KEY (PRI_WK) " +
				"REFERENCES PROJ_M (PNUM);");
		        
		// The following table is self-referencing.

		stmt.executeUpdate("CREATE TABLE STAFF_C " +
				"(EMPNUM   CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE    DECIMAL(4), " +
				"CITY     CHAR(15), " +
				"MGR   CHAR(3), " +
				"UNIQUE   (EMPNUM), " +
				"FOREIGN KEY (MGR) " +
				"REFERENCES STAFF_C(EMPNUM));");
		        
		        

		stmt.executeUpdate("CREATE TABLE STAFF_P " +
				"(EMPNUM   CHAR(3) NOT NULL, " +
				"EMPNAME  CHAR(20), " +
				"GRADE    DECIMAL(4), " +
				"CITY     CHAR(15), " +
				"UNIQUE  (EMPNUM));");
		        
		        
		stmt.executeUpdate("CREATE TABLE PROJ_P " +
				"(PNUM     CHAR(3) NOT NULL, " +
				"PNAME    CHAR(20), " +
				"PTYPE    CHAR(6), " +
				"BUDGET   DECIMAL(9), " +
				"CITY     CHAR(15), " +
				"UNIQUE   (PNUM));");
		        
		        
		stmt.executeUpdate("CREATE TABLE MID1 (P_KEY DECIMAL(4) NOT NULL UNIQUE, " +
				"F_KEY DECIMAL(4) REFERENCES MID1(P_KEY));");

		stmt.executeUpdate("CREATE TABLE ACR_SCH_P(P1 DECIMAL(4) NOT NULL UNIQUE, " +
				"P2 CHAR(4));");

		stmt.executeUpdate("CREATE TABLE CHAR_DEFAULT " +
				"(SEX_CODE  CHAR(1)  DEFAULT 'F', " +
				"NICKNAME  CHAR(20) DEFAULT 'No nickname given', " +
				"INSURANCE1 CHAR(5)  DEFAULT 'basic');");

		stmt.executeUpdate("CREATE TABLE EXACT_DEF " +
				"(BODY_TEMP NUMERIC(4,1) DEFAULT 98.6, " +
				"MAX_NUM   NUMERIC(5)   DEFAULT -55555, " +
				"MIN_NUM   DEC(6,6)     DEFAULT .000001);");

		stmt.executeUpdate("CREATE TABLE APPROX_DEF " +
				"(X_COUNT   REAL DEFAULT 1.78E12, " +
				"Y_COUNT   REAL DEFAULT -9.99E10, " +
				"Z_COUNT   REAL DEFAULT 3.45E-11, " +
				"ZZ_COUNT  REAL DEFAULT -7.6777E-7);");

		stmt.executeUpdate("CREATE TABLE SIZE_TAB " +
				"(COL1 CHAR(75)  DEFAULT " +
				"'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz0123456789012', " +
				"COL2 INTEGER   DEFAULT -999888777, " +
				"COL3 DEC(15,6) DEFAULT 987654321.123456, " +
				"COL4 REAL      DEFAULT -1.048576E22);");   


		stmt.executeUpdate("CREATE TABLE COMMODITY " +
				"(C_NUM INTEGER NOT NULL, " +
				"C_NAME CHAR(7) NOT NULL UNIQUE, " +
				"PRIMARY KEY (C_NUM));");

		stmt.executeUpdate("CREATE TABLE CURRENCY_TABLE " +
				"(CURRENCY CHAR(10) NOT NULL, " +
				"DOLLAR_EQUIV NUMERIC(5, 2), " +
				"PRIMARY KEY (CURRENCY));");
		                 
		stmt.executeUpdate("CREATE TABLE MEASURE_TABLE " +
				"(MEASURE CHAR(8) NOT NULL, " +
				"POUND_EQUIV NUMERIC(8,2), " +
				"PRIMARY KEY (MEASURE));");

		stmt.executeUpdate("CREATE TABLE C_TRANSACTION " +
				"(COMMOD_NO INTEGER, " +
				"TOT_PRICE     DECIMAL(12,2), " +
				"CURRENCY  CHAR(10), " +
				"UNITS     INTEGER, " +
				"MEASURE   CHAR(8), " +
				"T_DATE    INTEGER, " +
				"FOREIGN KEY (COMMOD_NO) " +
				"REFERENCES COMMODITY, " +
				"FOREIGN KEY (CURRENCY) " +
				"REFERENCES CURRENCY_TABLE, " +
				"FOREIGN KEY (MEASURE) " +
				"REFERENCES MEASURE_TABLE);");

		stmt.executeUpdate("CREATE TABLE T6118REF ( " +
				"COL1 CHAR(20) NOT NULL, COL2 CHAR(20) NOT NULL, " +
				"COL3 CHAR(20) NOT NULL, COL4 CHAR(20) NOT NULL, " +
				"COL5 CHAR(23) NOT NULL, COL6 NUMERIC (4) NOT NULL, " +
				"STR118 CHAR(118) NOT NULL UNIQUE, " +
				"UNIQUE (COL1, COL2, COL4, COL3, COL5, COL6));");


		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE T118(STR118 CHAR(118) NOT NULL UNIQUE, " +
				"FOREIGN KEY (STR118) REFERENCES T6118REF (STR118));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table T118 already exists");
			}	
		}		
		
		
		stmt.executeUpdate("CREATE TABLE T6 (COL1 CHAR(20), COL2 CHAR(20), " +
				"COL3 CHAR(20), COL4 CHAR(20), " +
				"COL5 CHAR(23), COL6 NUMERIC (4), " +
				"FOREIGN KEY (COL1, COL2, COL4, COL3, COL5, COL6) " +
				"REFERENCES T6118REF (COL1, COL2, COL4, COL3, COL5, COL6));");

		// ********************** create view statements *****************

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW TESTREPORT AS " +
					"SELECT TESTNO, RESULT, TESTTYPE " +
					"FROM TESTREPORT;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Column unknown TESTNO");
			}	
		}		
		
		stmt.executeUpdate("CREATE VIEW DOLLARS_PER_POUND (COMMODITY, UNIT_PRICE, FROM_DATE, TO_DATE) " +
				"AS SELECT COMMODITY.C_NAME, " +
				"SUM(TOT_PRICE * DOLLAR_EQUIV) / SUM(UNITS * POUND_EQUIV), " +
				"MIN(T_DATE), MAX(T_DATE) " +
				"FROM C_TRANSACTION, COMMODITY, CURRENCY_TABLE, MEASURE_TABLE " +
				"WHERE C_TRANSACTION.COMMOD_NO = COMMODITY.C_NUM " +
				"AND C_TRANSACTION.CURRENCY = CURRENCY_TABLE.CURRENCY " +
				"AND C_TRANSACTION.MEASURE  = MEASURE_TABLE.MEASURE " +
				"GROUP BY COMMODITY.C_NAME " +
				"HAVING SUM(TOT_PRICE * DOLLAR_EQUIV) > 10000;");

		// View COST_PER_UNIT for OPTIONAL test 0403
		// Remove view from schema if it causes errors.

		stmt.executeUpdate("CREATE VIEW COST_PER_UNIT " +
				"(COMMODITY, UNIT_PRICE, CURRENCY, MEASURE) " +
				"AS SELECT COMMODITY, UNIT_PRICE * POUND_EQUIV / DOLLAR_EQUIV, " +
				"CURRENCY, MEASURE " +
				"FROM DOLLARS_PER_POUND, CURRENCY_TABLE, MEASURE_TABLE;");

		stmt.executeUpdate("CREATE VIEW STAFF6_WITH_GRADES AS " +
				"SELECT EMPNUM,EMPNAME,GRADE,CITY " +
				"FROM STAFF6 " +
				"WHERE GRADE > 0 AND GRADE < 20 " +
				"WITH CHECK OPTION;");

		// ************** grant statements follow *************
		stmt.executeUpdate("GRANT SELECT ON ECCO TO PUBLIC;");
		        
		stmt.executeUpdate("GRANT INSERT ON TESTREPORT TO PUBLIC;");

		stmt.executeUpdate("GRANT REFERENCES ON ACR_SCH_P TO SULLIVAN " +
				"WITH GRANT OPTION;");
		        
		stmt.executeUpdate("GRANT ALL PRIVILEGES ON PROJ_P TO SULLIVAN;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON T6118REF TO FLATER;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON T118 TO FLATER;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON T6 TO FLATER;");

		// Test GRANT without grant permission below.
		// "WITH GRANT OPTION" purposefully omitted from SUN's GRANT. 
		// Do not insert text "WITH GRANT OPTION"

		stmt.executeUpdate("GRANT REFERENCES ON STAFF_P TO SULLIVAN;");

		stmt.executeUpdate("GRANT REFERENCES (C_NUM) ON COMMODITY TO SCHANZLE;");

		// ************* End of Schema *************	
	}

	
	public static void setupSchema9(Statement stmt) throws SQLException {
		int       errorCode; // Error code.
		
		// SQL Test Suite, V6.0, Schema Definition, schema9.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID SULLIVAN
		// ***************************************************************

		// This file defines base tables used in the CDR tests.

		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the
		// remaining tests.

		//  CREATE SCHEMA
		//  AUTHORIZATION SULLIVAN;

		stmt.executeUpdate("CREATE TABLE WORKS_P " +
				"(EMPNUM   CHAR(3) REFERENCES STAFF_P(EMPNUM), " +
				"PNUM     CHAR(3), " +
				"HOURS    DECIMAL(5), " +
				"FOREIGN KEY (PNUM) " +
				"REFERENCES PROJ_P(PNUM));");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TTT(P1 DECIMAL(4) NOT NULL UNIQUE, " +
				"P2 CHAR(4));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table TTT already exists");
			}	
		}		
		
		// ************* grant statements follow *************

		stmt.executeUpdate("GRANT SELECT ON TTT TO SCHANZLE;");

		stmt.executeUpdate("GRANT REFERENCES ON ACR_SCH_P TO SCHANZLE;");


		//  Test GRANT without grant permission below -- expect error message!
		//  "WITH GRANT OPTION" purposefully omitted from SUN's GRANT.
		//  Do not change file SCHEMA8 to allow "WITH GRANT OPTION" on STAFF_P.

		stmt.executeUpdate("GRANT REFERENCES ON STAFF_P TO SCHANZLE;");

		// ************* End of Schema *************		
	}

	
	public static void setupSchema10(Statement stmt) throws SQLException {
		int       errorCode; // Error code.

		// SQL Test Suite, V6.0, Schema Definition, schem10.smi
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER AUTHORIZATION ID SCHANZLE 
		// ***************************************************************
				
		// This file defines base tables used in the CDR tests.
				
		// This non-standard schema definition is provided so that
		// implementations which require semicolons to terminate statements,
		// but which are otherwise conforming, can still execute the
		// remaining tests.
				
		// CREATE SCHEMA 
		// AUTHORIZATION SCHANZLE;

		stmt.executeUpdate("CREATE TABLE ACR_SCH_F(F1 DECIMAL(4), F2 CHAR(4), " +
				"FOREIGN KEY (F1) REFERENCES ACR_SCH_P(P1));");

		// Test GRANT REFERENCES without grant permission below -- expect error message!
		// "WITH GRANT OPTION" purposefully omitted from SUN's GRANT on STAFF_P
		// Do not change file SCHEMA8 to allow "WITH GRANT OPTION"

		stmt.executeUpdate("CREATE TABLE TAB5(F15 CHAR(3), F5 CHAR(4), " +
				"FOREIGN KEY (F15) REFERENCES STAFF_P(EMPNUM));");
	     
		// Test REFERENCES without reference permission below -- expect error message!
		// GRANT SELECT by SULLIVAN does not imply GRANT REFERENCES.
		// Do not change file SCHEMA9 to allow "GRANT REFERENCES" on TTT.

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE TAB6(F16 DECIMAL(4), F6 CHAR(4), " +
				"FOREIGN KEY (F16) REFERENCES TTT(P1));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return could not find UNIQUE INDEX with specified columns");
			}	
		}		
		
		// Constraints:  column vs. column

		stmt.executeUpdate("CREATE TABLE RET_CATALOG ( " +
				"VENDOR_ID INT, " +
				"PRODUCT_ID INT, " +
				"WHOLESALE NUMERIC (10,2), " +
				"RETAIL NUMERIC (10,2), " +
				"MARKUP NUMERIC (10,2), " +
				"EXPORT_CODE CHAR(2), " +
				"EXPORT_LICNSE_DATE CHAR(20), " +
				"CHECK (EXPORT_LICNSE_DATE IS NULL OR ( " +
				"EXPORT_CODE = 'F1' OR " +
				"EXPORT_CODE = 'F2' OR " +
				"EXPORT_CODE = 'F3'                  )), " +
				"CHECK (EXPORT_CODE <> 'F2' OR WHOLESALE > 10000.00), " +
				"CHECK (RETAIL >= WHOLESALE), " +
				"CHECK (RETAIL = WHOLESALE + MARKUP));");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW SALE_ITEMS AS " +
					"SELECT * FROM RET_CATALOG " +
					"WHERE MARKUP < WHOLESALE / 10.0 " +
					"WITH CHECK OPTION;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Inappropriate self-reference of column");
			}	
		}		
		
		stmt.executeUpdate("CREATE TABLE RASTER " +
				"(C1 INT NOT NULL, " +
				"FOREIGN KEY (C1) " +
				"REFERENCES COMMODITY (C_NUM));");

		// Test REFERENCES without reference permission below -- expect error message!
		// GRANT REFERENCES (C_NUM) on COMMODITY by SUN does not imply
		// GRANT REFERENCES (C_NAME).
		// Do not change file SCHEMA8 to allow "GRANT REFERENCES" on column C_NAME.

		stmt.executeUpdate("CREATE TABLE REFRESH " +
				"(C1 CHAR (7), " +
				"FOREIGN KEY (C1) " +
				"REFERENCES COMMODITY (C_NAME));");

		stmt.executeUpdate("CREATE TABLE CPBASE " +
				"(KC INT NOT NULL, " +
				"JUNK1 CHAR (10), " +
				"PRIMARY KEY (KC));");
		
		stmt.executeUpdate("CREATE TABLE CPREF " +
				"(KCR INT, " +
				"JUNK2 CHAR (10), " +
				"FOREIGN KEY (KCR) REFERENCES CPBASE);");

		stmt.executeUpdate("CREATE TABLE FOUR_TYPES " +
				"(T_INT     INTEGER, " +
				"T_CHAR    CHAR(10), " +
				"T_DECIMAL DECIMAL(10,2), " +
				"T_REAL    REAL);");

		// ************* End of Schema *************
	}

	
	public static void setupDataload(Statement stmt) throws SQLException {
		
		// This file loads the reporting structure into tables
		// TESTFEATURE, TESTCASE, TESTPROG, IMPLICATION, REPORTFEATURE 
		// created by file report.sql
		 
		 
		// You may insert additional COMMIT WORK statements, as required
		// by your implementation, because there is no sizing specification
		// for number of INSERT statements within one transaction.


		 
		stmt.executeUpdate("DELETE FROM F_TEMP;");
		stmt.executeUpdate("INSERT INTO F_TEMP SELECT FEATURE1, 'AAAA', 'AAAA', 0 FROM FEATURE_CLAIMED;");
		stmt.executeUpdate("DELETE FROM FEATURE_CLAIMED;");
		// TODO: COMMIT WORK: 
		stmt.executeUpdate("DELETE FROM TESTFEATURE;");
		stmt.executeUpdate("DELETE FROM TESTCASE;");
		stmt.executeUpdate("DELETE FROM TESTPROG;");
		stmt.executeUpdate("DELETE FROM IMPLICATION;");
		stmt.executeUpdate("DELETE FROM REPORTFEATURE;");
		// TODO: COMMIT WORK; 
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0001', 'Dynamic SQL');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0002', 'Basic information schema');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0003', 'Basic schema manipulation');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0004', 'Joined table');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0005', 'DATETIME data types');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0006', 'VARCHAR data type');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0007', 'TRIM function');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0008', 'UNION in views');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0009', 'Implicit numeric casting');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0010', 'Implicit character casting');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0011', 'Transaction isolation');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0012', 'Get diagnostics');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0013', 'Grouped operations');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0014', 'Qualified * in select list');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0015', 'Lowercase identifiers');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0016', 'PRIMARY KEY enhancement');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0017', 'Multiple schemas per user');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0018', 'Multiple module support');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0019', 'Referential delete actions');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0020', 'CAST functions');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0021', 'INSERT expressions');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0022', 'Explicit defaults');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0023', 'Privilege tables');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0024', 'Keyword relaxations');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0025', 'Domain definition');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0026', 'CASE expression');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0027', 'Compound character literals');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0028', 'LIKE enhancements');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0029', 'UNIQUE predicate');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0030', 'Table operations');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0031', 'Schema definition statement');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0032', 'User authorization');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0033', 'Constraint tables');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0034', 'Usage tables');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0035', 'Intermediate information schem');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0036', 'Subprogram support');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0037', 'Intermediate SQL Flagging');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0038', 'Schema manipulation');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0039', 'Long identifiers');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0040', 'Full outer join');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0041', 'Time zone specification');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0042', 'National character');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0043', 'Scrolled cursors');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0044', 'Intermediate set function');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0045', 'Character set definition');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0046', 'Named character sets');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0047', 'Scalar subquery values');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0048', 'Expanded null predicate');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0049', 'Constraint management');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0050', 'Documentation schema');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('00L2', 'SQL-89 Level 2');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0CFL', 'OPTIONAL Catalog-Lookup Flag');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0EFL', 'FIPS 127-2 Entry Syntax Flag');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0ENT', 'SQL-92 New Entry SQL Feature');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0FIP', 'FIPS/ISO SQL only - not X/Open');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0FSZ', 'FIPS sizing - not X/Open');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0IEF', 'SQL-89 Integrity Enhancement');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0INF', 'Informational Test');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0SZE', 'Sizing Test - Entry');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0SZT', 'Sizing Test - Transitional');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0SZI', 'Sizing Test - Intermediate');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0XC2', 'XPG4 SQL, App.C, C.2 > Entry');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0XC3', 'XPG4 SQL, App.C, C.3 extension');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('0XSZ', 'XPG4 SQL ONLY Sizing Test');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P110', 'FIPS 127-2 Entry Syntax Flags');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P120', 'ISO/IEC 9075:1992 Entry SQL');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P125', 'FIPS 127-2 Entry SQL');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P135', 'FIPS 127-2 Transitional SQL');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P140', 'ISO/IEC 9075:1992 Intermediate');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P145', 'FIPS 127-2 Intermediate SQL');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P210', 'XPG4 SQL Profile without IEF');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P230', 'XPG4 SQL Profile with IEF');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P325', 'Sizing Tests - Entry');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P335', 'Sizing Tests - Transitional');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P345', 'Sizing Tests - Intermediate');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P405', 'Entry Catalog-Lookup Flagging');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P410', 'Miscellaneous Informational');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P415', 'Optional/Informational Tests');");
		stmt.executeUpdate("INSERT INTO REPORTFEATURE VALUES ('P998', 'Individual Features');");
		// TODO: COMMIT WORK;
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P110', '0EFL');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P120', '00L2');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P120', '0ENT');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P120', '0FIP');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P120', '0IEF');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P125', 'P110');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P125', 'P120');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0001');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0002');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0003');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0004');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0005');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0006');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0007');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0008');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0009');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0010');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0011');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0012');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0013');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0014');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0015');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0016');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0017');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0018');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0019');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0020');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0021');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0022');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0023');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', '0024');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P135', 'P125');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0025');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0026');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0027');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0028');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0029');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0030');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0031');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0032');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0033');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0034');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0035');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0036');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0037');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0038');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0039');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0040');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0041');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0042');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0043');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0044');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0045');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0047');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0048');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', '0049');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P140', 'P135');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P145', '0046');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P145', '0050');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P145', 'P140');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0001');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0002');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0003');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0006');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0009');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0010');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0011');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0012');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0013');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0014');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0015');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '00L2');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0ENT');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0SZE');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0SZT');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0XC2');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0XC3');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P210', '0XSZ');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P230', '0IEF');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P230', 'P210');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P325', '0FSZ');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P325', '0SZE');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P335', '0SZT');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P335', 'P325');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P345', '0SZI');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P345', 'P335');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P405', '0CFL');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P410', '0INF');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P415', 'P405');");
		stmt.executeUpdate("INSERT INTO IMPLICATION VALUES ('P415', 'P410');");
		// TODO: COMMIT WORK;
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada002', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada003', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada004', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada005', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada006', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada007', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada008', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada009', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada010', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada011', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada012', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ada013', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc002', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc003', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc004', 'HU                ', 'subroutine');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc005', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc006', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc007', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc008', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc009', 'HU                ', 'subroutine');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc010', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ccc011', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr001', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr002', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr003', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr004', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr005', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr006', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr007', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr008', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr009', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr010', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr011', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr012', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr013', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr014', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr015', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr016', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr017', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr018', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr019', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr020', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr021', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr022', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr023', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr024', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr025', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr026', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr027', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr028', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr029', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr030', 'SUN               ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cdr031', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob002', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob004', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob005', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob006', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob007', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob008', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob009', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('cob010', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml002', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml003', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml004', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml005', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml006', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml007', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml008', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml009', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml010', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml011', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml012', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml013', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml014', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml015', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml016', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml017', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml018', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml019', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml020', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml021', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml022', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml023', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml024', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml025', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml026', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml027', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml028', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml029', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml033', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml034', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml035', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml036', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml037', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml038', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml039', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml040', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml041', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml042', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml043', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml044', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml045', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml046', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml047', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml048', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml049', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml050', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml051', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml052', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml053', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml054', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml055', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml056', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml057', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml058', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml059', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml060', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml061', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml062', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml063', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml064', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml065', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml066', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml067', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml068', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml069', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml070', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml071', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml072', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml073', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml074', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml075', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml076', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml077', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml078', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml079', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml080', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml081', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml082', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml083', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml084', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml085', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml086', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml087', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml088', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml090', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml091', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml092', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml093', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml094', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml095', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml096', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml097', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml098', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml099', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml100', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml101', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml104', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml105', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml106', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml107', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml108', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml109', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml110', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml111', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml112', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml113', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml114', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml115', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml116', 'FLATER            ', 'subroutine');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml117', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml118', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml119', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml120', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml121', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml122', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml123', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml124', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml125', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml126', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml127', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml128', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml129', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml130', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml131', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml132', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml133', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml134', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml135', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml136', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml137', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml138', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml139', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml140', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml141', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml142', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml143', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml144', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml145', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml146', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml147', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml148', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml149', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml150', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml151', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml152', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml153', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml154', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml155', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml156', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml157', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml158', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml159', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml160', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml161', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml162', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml163', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml164', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml165', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml166', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml167', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml168', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml169', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml170', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml171', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml172', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml173', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml174', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml175', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml176', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml177', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml178', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml179', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml180', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml181', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml182', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml183', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml184', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml185', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('dml186', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg005', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg006', 'MCGINN            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg008', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg009', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg010', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg011', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg012', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('flg013', 'FLATER            ', 'readsource');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('for001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('for002', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('for003', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi001', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi002', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi003', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi004', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi005', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi006', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi007', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('isi008', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist001', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist002', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist003', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist004', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist005', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist006', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist007', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('ist008', 'FLATER            ', 'c-pli-mump');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa001', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa002', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa003', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa004', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa005', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa006', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa007', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa008', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa009', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa010', 'FLATER; HU        ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa011', 'FLATER; HU        ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa012', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpa013', 'SULLIVAN1         ', 'MPB-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('mpquic', 'SULLIVAN1         ', 'int-concur');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('pas001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('pas002', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('pas003', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('pas004', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl001', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl002', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl003', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl004', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl005', 'CUGINI            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl006', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl007', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl008', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl009', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl010', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl011', 'SULLIVAN1         ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl012', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl013', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl014', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl015', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl016', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl017', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl018', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl019', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl020', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl021', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl022', 'SULLIVAN          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl023', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl024', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl025', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl026', 'CANWEPARSELENGTH18', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl027', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl028', 'HU                ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl029', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl030', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl031', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl032', 'FLATER            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl033', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl034', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl035', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl036', 'SCHANZLE          ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('sdl037', 'SULLIVAN1         ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop700', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop701', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop702', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop703', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop706', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop707', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop708', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop709', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop710', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop712', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop719', 'XOPEN2            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop720', 'XOPEN3            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop721', 'XOPEN2            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop722', 'XOPEN3            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop723', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xop725', 'XOPEN1            ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts700', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts701', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts702', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts703', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts713', 'T7013PC           ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts714', 'CTS2              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts715', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts716', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts717', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts718', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts719', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts720', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts721', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts722', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts723', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts724', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts725', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts726', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts727', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts728', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts729', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts730', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts731', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts732', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts733', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts734', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts735', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts736', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts737', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts738', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts739', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts740', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts741', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts742', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts744', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts745', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts746', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts747', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts748', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts749', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts750', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts751', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts752', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts753', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts754', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts755', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts756', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts758', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts759', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts760', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts761', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts762', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts763', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts764', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts765', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts766', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts767', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts768', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts769', 'CTS3              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts770', 'CTS3              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts771', 'CTS3              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts798', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('xts799', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts750', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts751', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts752', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts753', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts754', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts755', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts756', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts757', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts759', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts760', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts761', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts762', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts763', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts764', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts765', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts766', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts767', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts768', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts769', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts770', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts771', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts772', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts773', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts774', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts775', 'CTS1              ', 'subroutine');");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts776', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts777', 'CTS2              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts778', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts779', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts780', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts781', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts782', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts783', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts784', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts788', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts789', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts790', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts791', 'CTS2              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts792', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts793', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts794', 'CTS2              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts795', 'CTS4              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts796', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts797', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts798', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts799', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts800', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts802', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts803', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts805', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts806', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts807', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts808', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts809', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts810', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts811', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts812', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts813', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTPROG VALUES ('yts814', 'CTS1              ', NULL);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0001', 'CURSOR with ORDER BY DESC                         ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0002', 'CURSOR with ORDER BY integer ASC                  ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0003', 'CURSOR with ORDER BY DESC integer, named column   ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0004', 'CURSOR with UNION: ORDER by integer DESC          ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0005', 'CURSOR with UNION ALL                             ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0006', 'Error for second consecutive OPEN without CLOSE   ', 'dml002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0007', 'Error for second consecutive CLOSE                ', 'dml003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0008', 'SQLCODE = 100: FETCH on empty table               ', 'dml004', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0009', 'FETCH NULL value, get indicator = -1              ', 'dml004', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0010', 'FETCH truncated CHAR column with indicator        ', 'dml004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0011', 'FIPS sizing - DECIMAL(15)                         ', 'dml005', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0012', 'SQLCODE < 0: DELETE CURRENT at end-of-data        ', 'dml006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0013', 'DELETE CURRENT row twice                          ', 'dml006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0014', 'UPDATE CURRENT                                    ', 'dml007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0015', 'UPDATE CURRENT - view with check and unique       ', 'dml007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0016', 'SQLCODE < 0: 2 rows selected by single-row SELECT ', 'dml008', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0017', 'SELECT DISTINCT                                   ', 'dml008', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0018', 'SQLCODE = 100: SELECT with no data                ', 'dml008', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0019', 'SQLCODE = 0: SELECT with data                     ', 'dml008', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0020', 'SELECT NULL value, get indicator = -1             ', 'dml008', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0021', 'SELECT CHAR(M) col. into short var., get indic = M', 'dml008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0022', 'INSERT (column list) VALUES (literals and NULL)   ', 'dml009', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0023', 'SQLCODE < 0 if left-truncate DEC (>= col.def.)    ', 'dml009', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0024', 'SQLCODE = 100: INSERT query spec. is empty        ', 'dml009', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0025', 'SQLCODE = 0:  INSERT query spec. is not empty     ', 'dml009', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0026', 'INSERT into view, check option + unique violations', 'dml009', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0027', 'INSERT short string into long column-space padding', 'dml010', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0028', 'INSERT string that exactly fits in column         ', 'dml010', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0031', 'INSERT (column list) VALUES (variables and NULL)  ', 'dml010', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0033', 'UPDATE view without WHERE clause - all rows       ', 'dml011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0034', 'UPDATE table with SET column in WHERE clause      ', 'dml011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0035', 'UPDATE with correlated subquery in WHERE clause   ', 'dml011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0036', 'UPDATE view globally with check option violation  ', 'dml011', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0037', 'DELETE without WHERE clause - all rows            ', 'dml012', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0038', 'DELETE with correlated subquery in WHERE clause   ', 'dml012', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0039', 'COUNT DISTINCT function                           ', 'dml013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0040', 'SUM function with WHERE clause                    ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0041', 'MAX function in subquery                          ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0042', 'MIN function in subquery                          ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0043', 'AVG function                                      ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0044', 'AVG function: empty result NULL value             ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0045', 'BETWEEN predicate                                 ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0046', 'NOT BETWEEN predicate                             ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0047', 'IN predicate with subquery                        ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0048', 'NOT IN predicate with subquery                    ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0049', 'IN predicate with value list                      ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0050', 'LIKE predicate with % (percent)                   ', 'dml014', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0051', 'LIKE predicate with _ (underscore)                ', 'dml014', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0052', 'LIKE predicate with ESCAPE character              ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0053', 'NOT LIKE predicate                                ', 'dml014', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0054', 'IS NULL predicate                                 ', 'dml014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0055', 'NOT NULL predicate                                ', 'dml014', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0056', 'NOT EXISTS predicate                              ', 'dml014', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0057', 'ALL quantifier                                    ', 'dml014', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0058', 'SOME quantifier                                   ', 'dml014', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0059', 'ANY quantifier                                    ', 'dml014', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0060', 'COMMIT WORK closes cursors                        ', 'dml015', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0061', 'COMMIT WORK keeps changes to database             ', 'dml015', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0062', 'ROLLBACK WORK cancels changes to database         ', 'dml015', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0063', 'ROLLBACK WORK closes cursors                      ', 'dml015', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0064', 'SELECT USER                                       ', 'dml016', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0065', 'SELECT CHAR literal, term with numeric literal    ', 'dml016', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0066', 'SELECT numeric literal                            ', 'dml016', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0067', 'WHENEVER NOT FOUND(SQLCODE=100), GOTO and CONTINUE', 'dml017', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0068', 'WHENEVER SQLERROR (SQLCODE< 0), GOTO and CONTINUE ', 'dml017', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0069', 'HAVING COUNT with WHERE, GROUP BY                 ', 'dml018', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0070', 'HAVING COUNT with GROUP BY                        ', 'dml018', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0071', 'HAVING MIN, MAX with GROUP BY 3 columns           ', 'dml018', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0072', 'Nested HAVING IN, with no outer reference         ', 'dml018', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0073', 'HAVING MIN with no GROUP BY                       ', 'dml018', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0074', 'GROUP BY column: SELECT column, SUM               ', 'dml019', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0075', 'GROUP BY clause                                   ', 'dml019', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0076', 'GROUP BY 2 columns                                ', 'dml019', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0077', 'GROUP BY all columns, with SELECT *               ', 'dml019', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0078', 'GROUP BY 3 columns, SELECT 2 of them              ', 'dml019', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0079', 'GROUP BY NULL value                               ', 'dml019', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0080', 'Simple 2-table join                               ', 'dml020', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0081', 'Simple 2-table join with 1-table predicate        ', 'dml020', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0082', 'Join 3 tables                                     ', 'dml020', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0083', 'Join a table with itself                          ', 'dml020', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0084', 'Data type CHAR(20)                                ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0085', 'Data type CHARACTER(20)                           ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0086', 'Data type INTEGER                                 ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0087', 'Data type INT                                     ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0088', 'Data type REAL                                    ', 'dml034', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0089', 'Data type SMALLINT                                ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0090', 'Data type DOUBLE PRECISION                        ', 'dml034', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0091', 'Data type FLOAT                                   ', 'dml034', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0092', 'Data type FLOAT(32)                               ', 'dml034', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0093', 'Data type NUMERIC(13,6)                           ', 'dml034', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0094', 'Data type DECIMAL(13,6)                           ', 'dml034', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0095', 'Data type DEC(13,6)                               ', 'dml034', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0096', 'Subquery with MAX in < comparison predicate       ', 'dml022', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0097', 'Subquery with AVG - 1 in <= comparison predicate  ', 'dml022', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0098', 'IN predicate with simple subquery                 ', 'dml022', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0099', 'Nested IN predicate - 2 levels                    ', 'dml022', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0100', 'Nested IN predicate - 6 levels                    ', 'dml022', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0101', 'Quantified predicate <= ALL with AVG and GROUP BY ', 'dml022', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0102', 'Nested NOT EXISTS with corr. subqueries, DISTINCT ', 'dml022', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0103', 'Subquery with = comparison predicate              ', 'dml023', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0104', 'SQLCODE < 0: subquery with more than 1 value      ', 'dml023', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0105', 'Subquery in comparison predicate is empty         ', 'dml023', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0106', 'Comparison predicate <> (not equal)               ', 'dml023', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0107', 'Short string logically blank-padded in = pred.    ', 'dml023', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0108', 'Search condition true OR NOT (true)               ', 'dml024', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0109', 'Search condition true AND NOT (true)              ', 'dml024', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0110', 'Search condition unknown OR NOT (unknown)         ', 'dml024', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0111', 'Search condition unknown AND NOT (unknown)        ', 'dml024', NULL, 2);");		
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0112', 'Search condition unknown AND true                 ', 'dml024', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0113', 'Search condition unknown OR true                  ', 'dml024', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0114', 'Set functions without GROUP BY returns 1 row      ', 'dml025', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0115', 'GROUP BY 0 groups returns 0 rows: SEL col.,AVG... ', 'dml025', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0116', 'GROUP BY 0 groups returns 0 rows: SELECT set fnc. ', 'dml025', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0117', 'Set functions with GROUP BY several groups        ', 'dml025', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0118', 'Monadic arithmetic operator + (plus)              ', 'dml026', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0119', 'Monadic arithmetic operator - (minus)             ', 'dml026', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0120', 'Value expression with NULL primary yields NULL    ', 'dml026', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0121', 'Dyadic arithmetic operators +, -, *, /            ', 'dml026', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0122', 'SQLCODE < 0: divisor shall not be zero            ', 'dml026', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0123', 'Evaluation order of expression                    ', 'dml026', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0124', 'UPDATE UNIQUE column (key=key+1) interim conflict ', 'dml027', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0125', 'UPDATE UNIQUE column (key+1): no interim conflict ', 'dml027', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0126', 'CLOSE, OPEN, FETCH returns first row              ', 'dml028', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0127', 'OPEN 2 cursors at same time                       ', 'dml028', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0128', 'OPEN 3 cursors at same time                       ', 'dml028', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0129', 'Double quote mark ('') in character string literal ', 'dml029', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0130', 'Approximate numeric literal <mantissa>E<exponent> ', 'dml029', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0131', 'Approximate numeric literal with neg. exponent    ', 'dml029', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0135', 'Upper and lower case letters are distinct         ', 'dml033', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0137', 'CREATE SCHEMA                                     ', 'sdl001', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0138', 'GRANT ALL PRIVILEGES TO PUBLIC (SELECT, INSERT)   ', 'sdl002', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0139', 'GRANT ALL PRIVILEGES TO PUBLIC (SELECT, UPDATE)   ', 'sdl003', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0140', 'Priv.violation: GRANT SELECT TO PUBLIC, no INSERT ', 'sdl004', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0141', 'GRANT SELECT and UPDATE to individual             ', 'sdl005', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0142', 'GRANT SELECT and UPDATE WITH GRANT OPTION         ', 'sdl006', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0143', 'GRANT SELECT and UPDATE on VIEW                   ', 'sdl007', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0144', 'Priv.violation: colunm not in UPDATE column list  ', 'sdl008', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0145', 'Fully qualified column specification              ', 'sdl009', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0146', 'GRANT SELECT, DELETE, INSERT                      ', 'sdl010', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0147', 'CREATE SCHEMA                                     ', 'sdl011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0148', 'CREATE TABLE with NOT NULL                        ', 'sdl012', 'synvio_yes', 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0149', 'CREATE TABLE with NOT NULL UNIQUE                 ', 'sdl013', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0150', 'CREATE TABLE with UNIQUE(...): INSERT VALUES      ', 'sdl014', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0151', 'CREATE VIEW                                       ', 'sdl015', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0152', 'CREATE VIEW with CHECK OPTION                     ', 'sdl016', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0153', 'CREATE VIEW joining 3 tables                      ', 'sdl017', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0154', 'Schema def.- same table name for different schemas', 'sdl018', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0155', 'CREATE TABLE with UNIQUE(...): INSERT via SELECT  ', 'sdl019', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0156', 'Tables are multi-sets, dup. INSERTed via subquery ', 'sdl020', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0157', 'CURSOR with ORDER BY approximate numeric          ', 'dml035', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0158', 'CURSOR with UNION and NOT EXISTS subquery         ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0159', 'CURSOR with 2 UNIONs, ORDER BY 2 integers         ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0160', 'CURSOR with UNION, UNION ALL and ORDER BY         ', 'dml001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0161', 'SQLCODE < 0: FETCH NULL value without indicator   ', 'dml004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0162', 'FETCH NULL value with INDICATOR syntax            ', 'dml004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0163', 'SQLCODE < 0: DELETE CURRENT without FETCH         ', 'dml006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0164', 'Default of SELECT is ALL, not DISTINCT            ', 'dml008', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0165', 'Truncate CHAR column SELECTed into shorter var.   ', 'dml008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0166', 'INSERT NULL value with indicator = -1             ', 'dml036', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0167', 'SUM ALL function                                  ', 'dml013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0168', 'SUM function                                      ', 'dml013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0169', 'COUNT (*) function                                ', 'dml013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0170', 'SUM DISTINCT function with WHERE clause           ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0171', 'SUM (column) + literal                            ', 'dml013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0172', 'SELECT USER into short variable                   ', 'dml016', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0173', 'Data type CHAR                                    ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0174', 'Data type CHARACTER                               ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0175', 'Data type NUMERIC                                 ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0176', 'Data type NUMERIC(9): SELECT *                    ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0177', 'Data type NUMERIC(9): SELECT column               ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0178', 'Data type DECIMAL                                 ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0179', 'Data type DECIMAL(8)                              ', 'dml021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0180', 'NULLs sort together in ORDER BY                   ', 'dml023', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0181', 'NULLs are equal for DISTINCT                      ', 'dml023', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0182', 'Approx. num. literal with neg. mantissa and exp.  ', 'dml029', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0183', 'C language embedded host identifiers              ', 'ccc001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0184', 'C language NULL terminator                        ', 'ccc002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0185', 'COBOL - embedded host identifiers                 ', 'cob001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0186', 'COBOL - CHAR(80)                                  ', 'cob002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0187', 'COBOL - CHAR(132)                                 ', 'cob002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0188', 'COBOL - CHAR(240)                                 ', 'cob002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0189', 'OPTIONAL - CHAR(256)                              ', 'dml078', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0190', 'OPTIONAL - CHAR(512)                              ', 'dml078', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0191', 'OPTIONAL - CHAR(1024)                             ', 'dml078', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0192', 'C language continuation of SQL char. literal      ', 'ccc003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0193', 'C language comments within embedded SQL statement ', 'ccc003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0194', 'C language EXTERN storage class                   ', 'ccc004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0195', 'C language STATIC storage class                   ', 'ccc005', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0196', 'COBOL PIC S9(12) precision test                   ', 'cob004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0197', 'COBOL PIC S9(18) precision test                   ', 'cob004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0198', 'C language AUTO storage class                     ', 'ccc006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0199', 'Priv.violation: GRANT SELECT to PUBLIC, no DELETE ', 'sdl021', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0200', 'Priv.violation: GRANT INSERT to indiv., no SELECT ', 'sdl022', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0201', 'Priv.violation: GRANT without GRANT OPTION        ', 'sdl023', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0202', 'Host variable names same as column names          ', 'dml037', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0203', 'CREATE VIEW on VIEW                               ', 'sdl024', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0204', 'Updatable VIEW with AND, OR in CHECK clause       ', 'sdl025', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0205', 'Cartesian product is produced without WHERE       ', 'dml038', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0206', 'COBOL - continuation of SQL char. literal         ', 'cob005', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0207', 'COBOL - comments within embedded SQL statement    ', 'cob006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0208', 'LIKE predicate with underscore is case sensitive  ', 'dml039', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0209', 'Join 2 tables from different schemas              ', 'dml040', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0210', 'COBOL - PIC S9(1) syntax                          ', 'cob007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0211', 'COBOL - PIC S9(7) syntax                          ', 'cob007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0212', 'Enforcement of CHECK clauses in nested views      ', 'dml041', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0213', 'FIPS sizing - 100 columns in a row                ', 'dml042', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0214', 'FIPS sizing - 2000 byte-row                       ', 'dml043', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0215', 'FIPS sizing - 6 columns in a UNIQUE specification ', 'dml044', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0216', 'FIPS sizing - 120 bytes in a UNIQUE specification ', 'dml044', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0217', 'FORTRAN - continuation of SQL character literal   ', 'for001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0218', 'FIPS sizing - 6 columns in GROUP BY               ', 'dml045', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0219', 'FIPS sizing - 120 bytes in GROUP BY               ', 'dml045', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0220', 'FIPS sizing - 6 columns in ORDER BY               ', 'dml046', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0221', 'FIPS sizing - 120 bytes in ORDER BY               ', 'dml046', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0222', 'FIPS sizing - CHARACTER(240)                      ', 'dml047', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0223', 'FORTRAN - comments within embedded SQL statements ', 'for001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0224', 'FIPS sizing - 10 cursors open at once             ', 'dml048', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0225', 'FIPS sizing - 10 tables in FROM clause            ', 'dml049', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0226', 'FIPS sizing - 10 tables in nested SQL statements  ', 'dml050', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0227', 'BETWEEN predicate with character string values    ', 'dml051', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0228', 'NOT BETWEEN predicate with character string value ', 'dml051', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0229', 'LIKE predicate is case sensitive                  ', 'dml052', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0230', 'Transactions serializable: assign sequential key  ', 'mpa001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0231', 'Transactions serializable: SELECT/UPDATE(replace) ', 'mpa002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0232', 'Transactions serializable: UPDATE with arithmetic ', 'mpa003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0233', 'Tables are multi-sets: duplicate via INSERT VALUE ', 'dml053', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0234', 'SQL comments (double hyphen) in SQL statements    ', 'dml037', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0235', 'COBOL - exact numeric types S9(i)V9(k)            ', 'cob008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0236', 'COBOL: SQLCODE < 0: exception losing signif. digit', 'cob008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0237', 'Identifier length 18                              ', 'sdl026', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0238', 'Pascal - comments within embedded SQL statements  ', 'pas002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0239', 'Pascal - embedded host identifiers                ', 'pas001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0240', 'Updatable CURSOR with ALL, IN, BETWEEN            ', 'dml054', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0241', 'Updatable CURSOR with LIKE, NULL, >, =, <         ', 'dml054', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0242', 'Updatable CURSOR with view, correlation name, NOT ', 'dml054', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0243', 'FIPS sizing - precision of SMALLINT >= 4          ', 'dml055', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0244', 'FIPS sizing - precision of INTEGER >= 9           ', 'dml055', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0245', 'FIPS sizing - precision of DECIMAL >= 15          ', 'dml055', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0246', 'FIPS sizing - 100 values in INSERT                ', 'dml056', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0247', 'FIPS sizing - 20 values in update SET clause      ', 'dml056', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0248', 'FIPS sizing - binary precision of FLOAT >= 20     ', 'dml057', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0249', 'FIPS sizing - binary precision of REAL >= 20      ', 'dml057', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0250', 'FIPS sizing - binary precision of DOUBLE >= 30    ', 'dml057', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0251', 'COMMIT keeps changes of current transaction       ', 'dml058', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0252', 'ROLLBACK cancels changes of current transaction   ', 'dml058', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0253', 'TEST0124 workaround (key = key+1)                 ', 'dml058', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0254', 'Column name in SET clause                         ', 'dml058', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0255', 'Key word USER for INSERT, UPDATE                  ', 'dml058', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0256', 'Key word USER in WHERE clause                     ', 'dml058', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0257', 'SELECT MAX, MIN (COL1 + or - COL2)                ', 'dml059', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0258', 'SELECT SUM (:var * COL1 * COL2)                   ', 'dml059', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0259', 'SOME, ANY in GROUP BY, HAVING clause              ', 'dml059', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0260', 'EXISTS in GROUP BY, HAVING                        ', 'dml059', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0261', 'WHERE (:var * (COL1 - COL2)) BETWEEN              ', 'dml060', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0262', 'WHERE clause with computation, ANY/ALL subqueries ', 'dml060', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0263', 'Computed column in ORDER BY                       ', 'dml060', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0264', 'WHERE, HAVING without GROUP BY                    ', 'dml059', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0265', 'UPDATE : positioned - view with check option      ', 'dml060', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0266', 'UPDATE : positioned - UNIQUE violation under view ', 'dml060', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0267', 'UPDATE compound key, interim uniqueness conflicts ', 'dml060', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0268', 'Transactions serializable: deadlock management    ', 'mpa004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0269', 'BETWEEN value expressions in wrong order          ', 'dml061', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0270', 'BETWEEN approximate and exact numeric values      ', 'dml061', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0271', 'COUNT(*) with Cartesian product subset            ', 'dml061', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0272', 'Statement rollback for integrity violation        ', 'dml061', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0273', 'SUM, MAX, MIN = NULL (not 0) for empty arguments  ', 'dml061', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0274', 'COMMIT and ROLLBACK across schemas                ', 'dml062', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0275', 'COMMIT and ROLLBACK of multiple cursors           ', 'dml062', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0276', 'View across schemas                               ', 'dml062', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0277', 'Computation with NULL value specification         ', 'dml061', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0278', 'IN value list with USER, literal,variable spec.   ', 'dml061', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0279', 'IN is a 3-valued predicate, EXISTS is 2-valued    ', 'dml062', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0280', 'SQL key words used as embedded host identifiers   ', 'dml063', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0281', 'Updatable VIEW with ALL, IN, BETWEEN              ', 'dml064', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0282', 'Updatable VIEW with LIKE, NULL, >, =, <           ', 'dml064', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0283', 'Updatable VIEW with view, correlation name, NOT   ', 'dml064', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0284', 'INSERT,SELECT character strings with blanks       ', 'dml065', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0285', 'INSERT,SELECT integers with various formats       ', 'dml065', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0286', 'Compatibility of structures and host variables    ', 'dml066', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0287', 'Compatibility of arrays and host structures       ', 'dml066', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0288', 'Embedded - multiple identifiers in 1 declaration  ', 'dml067', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0289', 'Embedded - multiple declare sections              ', 'dml067', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0290', 'C language common placement of SQL statements     ', 'ccc007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0291', 'C language SQL statements in functions            ', 'ccc008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0292', 'COBOL - VALUE IS initialization                   ', 'cob009', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0293', 'COBOL - placement of SQL statements               ', 'cob009', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0294', 'Pascal - placement of SQL statements              ', 'pas003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0295', 'FORTRAN - placement of SQL statements             ', 'for002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0296', 'FIPS Flagger - vendor provided character function ', 'flg005', 'visual chk', 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0297', 'FIPS Flagger - vendor provided integer function   ', 'flg005', 'visual chk', 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0298', 'Pascal - SQL statements in functions              ', 'pas004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0299', 'FIPS Flagger - identifier length > 18             ', 'flg006', 'visual chk', 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0300', 'DEFAULT value literal + number in a table         ', 'cdr001', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0301', 'DEFAULT value USER in a table                     ', 'cdr001', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0302', 'CHECK <comp. predicate> in <tab. cons.>, insert   ', 'cdr002', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0303', 'CHECK <comp. predicate> in <col. cons.>, insert   ', 'cdr002', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0304', 'CHECK <between predicate> in <tab. cons.>, insert ', 'cdr002', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0305', 'CHECK <null predicate> in <tab. cons.>, insert    ', 'cdr002', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0306', 'CHECK X IS NOT NULL, NOT X IS NULL are equivalent ', 'cdr003', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0307', 'CHECK <like predicate> in <tab. cons>, insert     ', 'cdr003', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0308', 'CHECK <in predicate> in <tab. cons.>, insert      ', 'cdr003', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0309', 'CHECK combination predicates in <tab. cons.>      ', 'cdr004', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0310', 'CHECK X NOT IN, NOT X IN equivalent, insert       ', 'cdr004', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0311', 'CHECK NOT NULL in col.cons., insert, null explicit', 'cdr004', 'synvio_yes', 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0312', 'CHECK NOT NULL in col.cons., insert, null implicit', 'cdr004', 'synvio_yes', 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0313', 'CHECK <comp. predicate> in <tab. cons.>, update   ', 'cdr005', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0314', 'CHECK <comp. predicate> in <col. cons.>, update   ', 'cdr005', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0315', 'CHECK <between predicate> in <tab. cons.>, update ', 'cdr005', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0316', 'CHECK <null predicate> in <tab. cons.>, update    ', 'cdr006', 'synvio_yes', 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0317', 'CHECK X IS NOT NULL, NOT X IS NULL same, by update', 'cdr006', 'synvio_yes', 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0318', 'CHECK <like predicate> in <tab. cons.>, update    ', 'cdr006', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0319', 'CHECK <in predicate> in <tab. cons.>, update      ', 'cdr007', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0320', 'CHECK combination pred. in <tab. cons.>, update   ', 'cdr007', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0321', 'CHECK if X NOT LIKE/IN, NOT X LIKE/IN same, update', 'cdr007', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0322', 'CHECK <null predicate> in <col. cons>, update     ', 'cdr007', 'synvio_yes', 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0323', '(2 pr.,1 son),both P.K e, F.K e,insert another F.K', 'cdr008', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0324', '(2 pr.,1 son),1 P.K exist,another not. insert F.K ', 'cdr008', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0325', '(2 pr.,1 son),both P.K e, F.K e, delete 1 P.K     ', 'cdr008', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0326', '(2 pr.,1 son),P.K e, no F.K, modify P.K           ', 'cdr008', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0327', '(2 pr.,1 son),check PRIMARY KEY unique via insert ', 'cdr009', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0328', '(2 pr.,1 son),F.K exist,modify P.K                ', 'cdr009', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0329', '(2 pr.,1 son),check PRIMARY KEY unique via modify ', 'cdr009', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0330', '(2 pr.,1 son),modify F.K to no P.K corr.          ', 'cdr009', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0331', '(2 pr.,1 son),modify F.K to P.K corr. value       ', 'cdr009', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0332', '(self ref.), P.K exist, insert a F.K              ', 'cdr010', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0333', '(self ref.), delete P.K but F.K exist.            ', 'cdr010', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0334', '(self ref.), update P.K, no corr. F.K             ', 'cdr010', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0335', '(self ref.), insert a F.K but no corr. P.K        ', 'cdr011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0336', '(self ref.), update P.K, but corr. F.K e.         ', 'cdr011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0337', '(self ref.), update P.K, check P.K unique via var.', 'cdr011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0338', '(self ref.), update F.K and no corr. P.K          ', 'cdr011', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0339', '(self ref.), update F.K and corr. P.K exist       ', 'cdr011', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0340', '(ref. each other), insert F.K and corr. P.K e     ', 'cdr012', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0341', '(ref. each other), delete P.K but corr. F.K e     ', 'cdr012', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0342', '(ref. each other), update P.K and no corr. F.K    ', 'cdr012', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0343', '(ref. each other), update P.K and corr. F.K e     ', 'cdr013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0344', '(ref. each other), update F.K to no corr. P.K     ', 'cdr013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0345', '(ref. each other), update F.K to corr. P.K e      ', 'cdr013', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0346', '(ref. each other), insert F.K and no corr. P.K    ', 'cdr013', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0347', 'FIPS sz. (comb.keys=6), P.K unique,insert         ', 'cdr014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0348', 'FIPS sz. (comb.keys=6), insert F.K + no corr. P.K ', 'cdr014', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0349', 'FIPS sz. (comb.keys=6), delete P.K + corr. F.K e  ', 'cdr015', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0350', 'FIPS sz. (comb.keys=6), update P.K + no corr. F.K ', 'cdr015', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0351', 'FIPS sz. (comb.keys=6), update P.K + corr. P.K e  ', 'cdr015', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0352', 'FIPS sz. (comb.keys=6), P.K unique, update        ', 'cdr016', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0353', 'FIPS sz. (comb.keys=6), update F.K to no corr. P.K', 'cdr016', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0354', 'FIPS sz. (comb.keys=6), update F.K to corr. P.K e ', 'cdr016', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0355', 'FIPS sz. (1 pr.,6 son), insert F.K + no corr. P.K ', 'cdr017', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0356', 'FIPS sz. (1 pr.,6 son), delete P.K + corr. F.K e  ', 'cdr017', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0357', 'FIPS sz. (1 pr.,6 son), update P.K but corr. F.K e', 'cdr017', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0358', 'FIPS sz. (1 pr.,6 son), check key unique, update  ', 'cdr017', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0359', 'FIPS sz. (1 pr.,6 son), update F.K to no corr. P.K', 'cdr017', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0360', 'FIPS sz. (6 pr.,1 son), insert F.K, without P.K   ', 'cdr018', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0361', 'FIPS sz. (6 pr.,1 son), delete P.K, but corr.F.K e', 'cdr018', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0362', 'FIPS sz. (6 pr.,1 son), update P.K, but corr.F.K e', 'cdr018', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0363', 'FIPS sz. (6 pr.,1 son), check key unique ,update  ', 'cdr018', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0364', 'FIPS sz. (6 pr.,1 son), update F.K to no corr. P.K', 'cdr018', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0365', '(3-level schema), check insert F.K                ', 'cdr019', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0366', '(3-level schema), check delete P.K                ', 'cdr019', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0367', '(3-level schema), update mid. tab. check P.K + F.K', 'cdr019', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0368', '(3-level schema), check update P.K                ', 'cdr019', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0369', 'update P. K, set F1=F1+1, interim violation       ', 'cdr020', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0370', 'update F. K, set F1=F1+1, interim violation       ', 'cdr020', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0371', 'update self-ref table, interim violation          ', 'cdr020', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0372', 'delete self-ref table, interim violation          ', 'cdr020', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0373', 'insert with embeded var.+ indic. var. CHECK clause', 'cdr003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0374', 'computation in update, CHECK clause               ', 'cdr003', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0375', 'ref. integrity with computation                   ', 'cdr017', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0376', 'ref. integrity with join                          ', 'cdr017', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0377', 'DEFAULT value with explicit NULL                  ', 'cdr001', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0378', '(ref. acr. sch.) delete P.K and corr. F.K e.      ', 'cdr021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0379', '(ref. acr. sch.) update P.K and corr. F.K e.      ', 'cdr021', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0380', '(ref. acr. sch.) insert F.K and no corr. P.K      ', 'cdr022', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0381', '(ref. acr. sch.) update F.K to no P.K corr.       ', 'cdr022', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0382', '(ref. acr. sch.) with GRANT OPTION, insert        ', 'cdr023', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0383', 'Priv.violation: GRANT without GRANT OPTION        ', 'cdr023', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0384', 'Priv.violation: SELECT, but not REFERENCES        ', 'cdr023', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0385', 'character default column values                   ', 'cdr024', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0386', 'exact numeric default column values               ', 'cdr024', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0387', 'approximate numeric default column values         ', 'cdr024', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0388', 'FIPS sz. default column values                    ', 'cdr024', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0389', '95-char ASCII collating sequence (PL vs. SQL)     ', 'dml068', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0390', 'Short char column value blank-padded in larger var', 'dml072', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0391', 'Correlation names used in self-join of view       ', 'sdl027', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0392', 'FORTRAN - placement of additional SQL statements  ', 'for003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0393', 'SUM, MAX on Cartesian product                     ', 'dml073', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0394', 'AVG, MIN on joined table with WHERE               ', 'dml073', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0395', 'SUM, MIN on joined table with GROUP BY            ', 'dml073', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0396', 'SUM, MIN on joined table with WHERE,GROUP,HAVING  ', 'dml073', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0397', 'Grouped view                                      ', 'sdl028', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0398', 'Embedded C initial value                          ', 'ccc011', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0399', 'C language storage class and class modifier comb. ', 'ccc009', 'visual chk', 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0401', 'View with computed columns (degrees F to C)       ', 'sdl027', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0402', 'Computed GROUP BY view over referencing tables    ', 'cdr025', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0403', 'View on computed GROUP BY view with joins         ', 'cdr025', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0404', '2 FETCHes (different target types) on same cursor ', 'dml069', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0405', '2 cursors open from different schemas (coded join)', 'dml069', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0406', 'Subquery from different schema                    ', 'dml069', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0407', 'SELECT INTO :XX ... WHERE :XX =                   ', 'dml069', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0408', 'UPDATE references column value BEFORE update      ', 'dml069', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0409', 'Effective outer join--join with two cursors       ', 'dml070', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0410', 'NULL value in OPEN CURSOR                         ', 'dml076', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0411', 'Effective set difference                          ', 'dml070', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0412', 'Effective set intersection                        ', 'dml070', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0413', 'Computed SELECT on computed VIEW                  ', 'cdr025', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0414', 'WHENEVER NOT FOUND, multiple settings             ', 'dml071', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0415', 'WHENEVER SQLERROR, multiple settings              ', 'dml071', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0416', 'WHENEVER NOTFOUND overlaps WHENEVER SQLERROR      ', 'dml071', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0417', 'Cartesian product GROUP BY 2 columns with NULLs   ', 'dml073', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0418', 'AVG,SUM,COUNT on Cartesian product with NULLs     ', 'dml073', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0419', 'SUM, MAX, MIN on joined table view                ', 'dml073', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0420', 'View with multiple SELECT of same column          ', 'sdl028', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0421', 'Module language constants and expressions         ', 'dml074', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0422', 'Module language order of SQLCODE (not first)      ', 'dml074', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0423', 'Module language multiple SQLCODE parameters       ', 'dml074', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0424', 'Ada embedded host identifiers                     ', 'ada001', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0425', 'Ada comments                                      ', 'ada002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0426', 'Ada initial value                                 ', 'ada002', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0427', 'Ada common placement of SQL statements            ', 'ada003', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0428', 'Ada placement of SQL statements                   ', 'ada004', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0430', 'Ada unqualified type spec - without SQL_STANDARD  ', 'ada005', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0431', 'Redundant rows in IN subquery                     ', 'dml075', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0432', 'Unknowns in subquery of ALL, SOME, ANY            ', 'dml075', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0433', 'Empty subquery of ALL, SOME, ANY                  ', 'dml075', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0434', 'GROUP BY with HAVING EXISTS-correlated set fnc    ', 'dml075', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0435', 'Host variables in UPDATE WHERE CURRENT            ', 'dml076', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0436', 'NULL values for various SQL data types            ', 'dml076', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0437', 'NULL values for various host variable types       ', 'dml076', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0438', '(partial-NULL F.K) F.K INSERT supported           ', 'cdr026', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0439', '(partial-NULL F.K) F.K UPDATE supported           ', 'cdr026', NULL, 13);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0440', '(partial-NULL F.K) no restrict P.K update/delete  ', 'cdr026', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0441', 'NULL value for various predicates                 ', 'dml076', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0442', 'DISTINCT with GROUP BY, HAVING                    ', 'dml075', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0443', 'VIEW with check option rejects unknown (NULL)     ', 'dml077', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0444', 'Updatable cursor, modify value selected on        ', 'dml077', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0445', 'Values not assigned to targets for SQLCODE=100    ', 'dml077', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0446', 'Table CHECK constraint allows unknown (NULL)      ', 'cdr027', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0447', 'NULLs with check constraint and check option      ', 'cdr027', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0448', 'PRIMARY KEY implies UNIQUE                        ', 'cdr027', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0449', 'Constraint definition is case sensitive           ', 'cdr027', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0450', 'Referential integrity is case sensitive           ', 'cdr027', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0451', 'UNIQUEness is case sensitive                      ', 'dml079', NULL, 18);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0452', 'Order of precedence, left-to-right in UNION [ALL] ', 'dml079', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0453', 'NULL with empty subquery of ALL, SOME, ANY        ', 'dml079', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0454', 'SELECT nonGROUP column in GROUP BY                ', 'flg008', 'visual chk', 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0455', 'Relaxed union compatability rules for columns     ', 'flg009', 'visual chk', 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0456', 'Module language Ada subtype enforcement,name assoc', 'ada006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0457', 'Transactions serializable: phantom read           ', 'mpa005', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0458', 'Priv.violation: GRANT only SELECT to individual   ', 'sdl029', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0459', 'Priv.violation: GRANT only INSERT to individual   ', 'sdl029', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0460', 'Priv.violation: GRANT only UPDATE to individual   ', 'sdl029', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0461', 'Priv.violation: GRANT only DELETE to individual   ', 'sdl029', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0462', 'SQLCODE 100: DELETE with no data                  ', 'dml080', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0463', 'SQLCODE 100: UPDATE with no data                  ', 'dml080', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0464', 'Priv.violation: GRANT only SELECT to PUBLIC       ', 'sdl030', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0465', 'Priv.violation: GRANT only INSERT to PUBLIC       ', 'sdl030', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0466', 'Priv.violation: GRANT only UPDATE to PUBLIC       ', 'sdl030', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0467', 'Priv.violation: GRANT only DELETE to PUBLIC       ', 'sdl030', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0468', 'Priv.violation: individual without any privileges ', 'sdl031', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0469', 'GRANT ALL PRIVILEGES to individual                ', 'sdl031', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0470', 'GRANT ALL PRIVILEGES to public                    ', 'sdl031', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0471', 'Priv.violation: GRANT privilege not grantable     ', 'sdl031', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0472', 'Priv.violation: individual SELECT, column UPDATE  ', 'sdl032', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0473', 'Priv.violation: GRANT all on view but not table   ', 'sdl033', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0474', 'Priv.violation: need SELECT for searched UPDATE   ', 'sdl034', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0475', 'Priv.violation: GRANT ALL w/o GRANT OPTION        ', 'sdl034', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0476', 'Priv.violation: GRANT OPTION view but not table   ', 'sdl034', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0477', 'Priv.violation: GRANT only SELECT on view         ', 'sdl035', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0478', 'Priv.violation: GRANT only INSERT on view         ', 'sdl035', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0479', 'Priv.violation: GRANT only UPDATE on view         ', 'sdl035', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0480', 'Priv.violation: GRANT only DELETE on view         ', 'sdl035', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0481', 'Priv.violation: no privileges on view             ', 'sdl036', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0482', 'GRANT ALL PRIVILEGES on view                      ', 'sdl036', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0483', 'Priv.violation: GRANT UPDATE not grantable on view', 'sdl036', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0484', 'Priv.violation: SELECT and column UPDATE on view  ', 'sdl032', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0485', 'Priv.violation: SELECT and column UPDATE cursor   ', 'sdl032', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0486', 'Priv.violation: illegal REFERENCES                ', 'cdr028', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0487', 'SQLSTATE 00000: successful completion             ', 'dml081', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0488', 'SQLSTATE 21000: cardinality violation             ', 'dml081', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0489', 'SQLSTATE 02000: no data                           ', 'dml081', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0490', 'SQLSTATE 22012: data exception/division by zero   ', 'dml081', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0491', 'SQLSTATE 22022: data exception/indicator overflow ', 'dml082', 'synvio_yes', 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0492', 'SQLSTATE 22019: data exception/invalid escape char', 'dml082', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0493', 'SQLSTATE 22025: data exception/invalid escape seq.', 'dml082', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0494', 'SQLSTATE 22003: data exception/numeric val.range 1', 'dml082', NULL, 18);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0495', 'Priv.violation: illegal GRANT to self             ', 'sdl037', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0496', 'SQLSTATE 22002: data exception/null but no indic  ', 'dml083', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0497', 'SQLSTATE 22003: data exception/numeric val.range 2', 'dml091', NULL, 22);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0498', 'SQLSTATE 22001: data exception/string right trunc.', 'dml083', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0499', 'SQLSTATE 22024: data exception/unterminat.C string', 'ccc010', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0500', 'SQLSTATE 01003: warning/null elim. in set function', 'dml083', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0501', 'SQLSTATE 01004: warning/string right truncation   ', 'dml083', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0502', 'SQLSTATE 24000: invalid cursor state              ', 'dml081', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0503', 'SQLSTATE 42000: syntax error or access rule vio.1 ', 'dml084', 'synvio_yes', 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0504', 'SQLSTATE 42000: syntax error or access rule vio.2 ', 'dml084', 'synvio_yes', 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0505', 'SQLSTATE 44000: with check option violation       ', 'dml082', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0506', 'SQLSTATE 40001: trans.rollback/serialization fail.', 'mpa006', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0507', 'Transactions serializable:  dueling cursors       ', 'mpa007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0508', 'Delimited identifiers                             ', 'dml085', NULL, 26);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0509', 'Renaming columns with AS for ORDER BY             ', 'dml085', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0510', '<parameter name> = <column name> (OK with SQL-92) ', 'dml085', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0511', 'CHECK clauses in nested views(clarified in SQL-92)', 'dml086', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0512', '<value expression> for IN predicate               ', 'dml090', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0513', 'NUMERIC(4) implies CHECK BETWEEN -9999 AND 9999   ', 'dml090', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0514', 'SQLSTATE 23000: integrity constraint violation    ', 'dml141', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0515', 'FIPS sizing:  NUMERIC (15) decimal precision      ', 'dml132', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0516', 'SQLSTATE 23000: integrity constraint violation    ', 'cdr030', NULL, 24);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0517', 'Transactions serializable:  Twins Problem         ', 'mpa008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0518', 'CREATE VIEW with DISTINCT                         ', 'dml087', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0519', 'CREATE VIEW with subqueries                       ', 'dml087', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0520', 'Underscores are legal and significant             ', 'dml087', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0521', 'New format in MODULE-<parameter declaration list> ', 'dml088', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0522', 'No implied natural join on FROM T1, T2            ', 'cdr029', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0523', '<value expression> for BETWEEN predicate          ', 'dml090', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0524', 'FIPS sizing:  100 Items in a SELECT list          ', 'dml132', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0525', 'FIPS sizing:  15 Table references in SQL statement', 'dml132', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0526', 'FIPS sizing:  Length FOREIGN KEY column list = 120', 'cdr031', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0527', 'Priv. violation:  HU                              ', 'dml142', 'synvio_yes', 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0528', 'Tables are multi-sets:  cursor operations         ', 'dml142', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0529', 'Priv. violation:  SELECT in <insert statement>    ', 'dml143', 'synvio_yes', 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0530', 'Interactive SQL serializability:  dirty read      ', 'mpquic', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0531', 'Interactive serializability:  non-repeatable read ', 'mpquic', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0532', 'package SQLSTATE_CODES                            ', 'ada007', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0533', 'Misc. in package SQL_STANDARD                     ', 'ada008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0534', 'ADA Tasks                                         ', 'ada009', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0535', 'COBOL - BINARY PICTURE for INTEGER, SMALLINT type ', 'cob010', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0536', 'FIPS sizing - COBOL BINARY decimal precision >= 9 ', 'cob010', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0537', 'Table check constraint:  column vs. column        ', 'cdr029', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0538', 'With check option:  column vs. column             ', 'cdr029', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0539', 'Interactive SQL serializability:  phantom read    ', 'mpquic', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0554', 'More column renaming - single row select with join', 'dml085', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0556', 'Static insert, dynamic fetch, static commit       ', 'dml123', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0557', 'Static insert, dynamic commit, static rollback    ', 'dml123', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0558', 'Dynamic insert, static delete, dynamic count      ', 'dml123', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0559', 'Static insert, dynamic rollback, static fetch     ', 'dml123', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0560', 'Table privileges vs. column privileges            ', 'mpa010', 'synvio_yes', 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0561', 'Double SET TRANSACTION                            ', 'dml149', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0562', 'Interactive serializability:  ISOLATION MODE      ', 'mpquic', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0564', 'Outer ref. directly contained in HAVING clause    ', 'dml090', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0565', 'VARCHAR for Transitional SQL                      ', 'dml092', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0566', 'VARCHAR for TSQL:  dynamic version                ', 'dml093', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0567', 'CHAR type in Dynamic SQL                          ', 'dml094', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0568', 'INFORMATION_SCHEMA.TABLES definition              ', 'isi001', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0569', 'INFORMATION_SCHEMA.VIEWS definition               ', 'isi002', NULL, 28);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0570', 'INFORMATION_SCHEMA.COLUMNS definition             ', 'isi003', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0571', 'INFORMATION_SCHEMA.SCHEMATA definition            ', 'isi004', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0572', 'INFORMATION_SCHEMA.TABLE_PRIVILEGES definition    ', 'isi005', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0573', 'INFORMATION_SCHEMA.COLUMN_PRIVILEGES definition   ', 'isi006', NULL, 15);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0574', 'Orphaned IS data structures, Intermediate SQL     ', 'isi007', NULL, 38);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0575', 'VARCHAR in INFORMATION_SCHEMA                     ', 'isi008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0576', 'NUMERIC type in Dynamic SQL                       ', 'dml095', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0577', 'DECIMAL type in Dynamic SQL                       ', 'dml096', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0578', 'INTEGER and SMALLINT types in Dynamic SQL         ', 'dml097', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0579', 'FIPS sizing, Dynamic SQL exact numerics           ', 'dml098', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0580', 'FIPS sizing, Dynamic SQL approximate numerics     ', 'dml098', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0581', 'Implicit numeric casting (feature 9) dynamic      ', 'dml099', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0582', 'Implicit numeric casting (feature 9) static       ', 'dml099', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0583', 'FIPS sizing, Dynamic SQL character strings        ', 'dml099', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0584', 'FIPS sizing, VARCHAR (254) strings (static)       ', 'dml092', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0585', 'FIPS sizing, VARCHAR (254) strings (dynamic)      ', 'dml093', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0586', 'Sizing of FLOAT in a descriptor (dynamic)         ', 'dml098', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0587', 'SET TR READ ONLY / READ WRITE (static)            ', 'dml100', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0588', 'SET TR READ ONLY / READ WRITE (dynamic)           ', 'dml101', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0589', 'SET TR ISOLATION LEVEL (static)                   ', 'mpa013', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0590', 'SET TR ISOLATION LEVEL (dynamic)                  ', 'mpa012', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0591', 'NATURAL JOIN (feature 4) (static)                 ', 'dml104', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0592', 'INNER JOIN (feature 4) (static)                   ', 'dml104', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0593', 'LEFT OUTER JOIN (feature 4) (static)              ', 'dml104', NULL, 27);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0594', 'RIGHT OUTER JOIN (feature 4) (static)             ', 'dml104', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0595', 'NATURAL JOIN (feature 4) (dynamic)                ', 'dml105', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0596', 'INNER JOIN (feature 4) (dynamic)                  ', 'dml105', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0597', 'LEFT OUTER JOIN (feature 4) (dynamic)             ', 'dml105', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0598', 'RIGHT OUTER JOIN (feature 4) (dynamic)            ', 'dml105', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0599', 'UNION in views (feature 8) (static)               ', 'dml106', NULL, 27);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0600', 'UNION in views (feature 8) (dynamic)              ', 'dml107', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0601', 'DATETIME data types (feature 5) (static)          ', 'dml106', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0602', 'DATETIME data types (feature 5) (dynamic)         ', 'dml107', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0603', 'INFO_SCHEM.TABLES definition                      ', 'ist001', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0604', 'INFO_SCHEM.VIEWS definition                       ', 'ist002', NULL, 25);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0605', 'INFO_SCHEM.COLUMNS definition                     ', 'ist003', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0606', 'INFO_SCHEM.SCHEMATA definition                    ', 'ist004', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0607', 'INFO_SCHEM.TABLE_PRIVILEGES definition            ', 'ist005', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0608', 'INFO_SCHEM.COLUMN_PRIVILEGES definition           ', 'ist006', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0609', 'Orphaned IS data structures, Transitional SQL     ', 'ist007', NULL, 25);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0610', 'VARCHAR in INFO_SCHEM                             ', 'ist008', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0611', 'FIPS sizing, DATETIME data types (static)         ', 'dml106', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0612', 'FIPS sizing, DATETIME data types (dynamic)        ', 'dml107', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0613', '<datetime value function> (static)                ', 'dml106', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0614', '<datetime value function> (dynamic)               ', 'dml107', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0615', 'DATETIME-related SQLSTATE codes (static)          ', 'dml106', 'synvio_yes', 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0616', 'DATETIME-related SQLSTATE codes (dynamic)         ', 'dml107', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0617', 'DATETIME with predicates, set fns (static)        ', 'dml108', NULL, 24);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0618', 'DATETIME with predicates, set fns (dynamic)       ', 'dml109', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0619', 'DATETIME cursor operations (static)               ', 'dml110', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0620', 'DATETIME cursor operations (dynamic)              ', 'dml111', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0621', 'DATETIME NULLs (static)                           ', 'dml112', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0622', 'DATETIME NULLs (dynamic)                          ', 'dml113', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0623', 'OUTER JOINs with NULLs and empty tables (static)  ', 'dml112', NULL, 27);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0624', 'OUTER JOINs with NULLs and empty tables (dynamic) ', 'dml113', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0625', 'ADD COLUMN and DROP COLUMN (static)               ', 'dml112', 'synvio_yes', 37);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0626', 'ADD COLUMN and DROP COLUMN (dynamic)              ', 'dml113', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0627', '<grant statement> (static)                        ', 'mpa010', 'synvio_yes', 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0628', '<revoke statement> (static)                       ', 'mpa010', 'synvio_yes', 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0629', '<grant statement> (dynamic)                       ', 'mpa011', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0630', '<revoke statement> (dynamic)                      ', 'mpa011', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0631', 'Datetimes in a <default clause> (static)          ', 'dml112', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0632', 'Datetimes in a <default clause> (dynamic)         ', 'dml113', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0633', 'TRIM function (static)                            ', 'dml112', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0634', 'TRIM function (dynamic)                           ', 'dml113', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0635', 'Feature 13, grouped operations (static)           ', 'dml114', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0636', 'Feature 13, grouped operations (dynamic)          ', 'dml115', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0637', 'Feature 14, Qualified * in select list (static)   ', 'dml114', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0638', 'Feature 14, Qualified * in select list (dynamic)  ', 'dml115', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0639', 'Feature 15, Lowercase Identifiers (static)        ', 'dml114', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0640', 'Feature 15, Lowercase Identifiers (dynamic)       ', 'dml115', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0641', 'Feature 16, PRIMARY KEY enhancement (static)      ', 'dml114', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0642', 'Feature 16, PRIMARY KEY enhancement (dynamic)     ', 'dml115', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0643', 'Feature 17, Multiple schemas per user             ', 'dml133', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0644', 'Feature 18, Multiple module support               ', 'dml116', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0645', 'Feature 19, Referential delete actions (static)   ', 'dml117', NULL, 44);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0646', 'Feature 19, Referential delete actions (dynamic)  ', 'dml118', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0647', 'Feature 20, CAST functions (static)               ', 'dml119', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0648', 'Feature 20, CAST functions (dynamic)              ', 'dml120', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0649', 'Feature 22, Explicit defaults (static)            ', 'dml121', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0650', 'Feature 22, Explicit defaults (dynamic)           ', 'dml122', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0651', 'Feature 24, Keyword relaxations (static)          ', 'dml121', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0652', 'Feature 24, Keyword relaxations (dynamic)         ', 'dml122', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0653', 'Descriptors:  DESCRIBE OUTPUT                     ', 'dml124', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0654', 'Descriptors:  INTO SQL DESCRIPTOR                 ', 'dml124', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0655', 'Descriptors:  USING SQL DESCRIPTOR                ', 'dml124', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0656', 'Descriptors:  datetimes                           ', 'dml124', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0657', 'Descriptors:  VARCHAR                             ', 'dml125', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0658', 'Descriptors:  SQLSTATE codes                      ', 'dml125', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0659', 'Descriptors:  TSQL orphaned features              ', 'dml125', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0660', 'Dynamic SQL SQLSTATEs                             ', 'dml126', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0661', 'Errata:  datetime casting (static)                ', 'dml121', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0662', 'Errata:  datetime casting (dynamic)               ', 'dml122', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0663', 'Errata:  datetime SQLSTATEs (static)              ', 'dml121', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0664', 'Errata:  datetime SQLSTATEs (dynamic)             ', 'dml122', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0665', 'Diagnostics:  statement information               ', 'dml127', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0666', 'Diagnostics:  condition information               ', 'dml127', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0667', 'Diagnostics:  access violations                   ', 'dml152', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0668', 'Diagnostics:  COMMAND_FUNCTION (static)           ', 'dml152', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0669', 'Diagnostics:  COMMAND_FUNCTION F# 3, 11           ', 'dml152', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0670', 'Diagnostics:  COMMAND_FUNCTION (dynamic)          ', 'dml126', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0671', 'Diagnostics:  DYNAMIC_FUNCTION                    ', 'dml126', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0672', 'Diagnostics:  Multiple conditions                 ', 'dml152', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0673', 'Diagnostics SQLSTATE:  inv. cond. number          ', 'dml152', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0674', 'Diagnostics:  TSQL orphaned features              ', 'dml128', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0675', 'Diagnostics:  MORE                                ', 'dml128', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0676', 'Diagnostics:  VARCHAR                             ', 'dml129', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0677', 'VARCHAR with <like predicate>                     ', 'dml129', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0678', 'Data type semantics with NULL / NOT NULL          ', 'dml130', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0679', 'INFO_SCHEM:  Table data types                     ', 'dml130', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0680', 'INFO_SCHEM:  View data types                      ', 'dml130', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0681', 'INFO_SCHEM:  Varchar data types                   ', 'dml129', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0682', 'INFO_SCHEM:  Datetime data types                  ', 'dml130', NULL, 18);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0683', 'INFO_SCHEM:  Changes are visible                  ', 'dml131', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0684', 'INFO_SCHEM:  Visibility to other users            ', 'dml131', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0685', 'INFO_SCHEM:  Privileges and privilege views       ', 'dml131', NULL, 22);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0686', 'INFO_SCHEM:  Primary key enh. is not null         ', 'dml131', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0687', 'INFO_SCHEM:  Multiple schemas per user            ', 'dml131', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0688', 'INFO_SCHEM:  Dynamic changes are visible          ', 'dml134', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0689', 'Many Trans SQL features #1:  inventory system     ', 'dml134', NULL, 37);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0690', 'Many Trans SQL features #2:  talk show schedule   ', 'dml134', NULL, 40);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0691', 'INFO_SCHEM:  SQLSTATEs for length overruns        ', 'dml134', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0692', 'Many TSQL features #3:  enhanced proj/works       ', 'dml135', NULL, 28);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0693', 'Many TSQL features #4:  enhanced INFO_SCHEM       ', 'dml135', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0694', 'Interval Arithmetic and Casts                     ', 'dml135', NULL, 32);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0695', '<updatability clause> in <declare cursor>         ', 'dml135', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0696', 'Many TSQL features #5:  Video Game Scores         ', 'dml136', NULL, 23);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0697', 'Erratum:  drop behavior, constraints (static)     ', 'dml137', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0698', 'Erratum:  drop behavior, constraints (dynamic)    ', 'dml138', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0699', '<drop behavior> on a REVOKE (static)              ', 'dml139', 'synvio_yes', 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0700', 'X/O,DEFAULTS and LIMITS for DATA TYPES            ', 'xop700', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0701', 'X/O,WHENEVER SQLWARNING and scoping of C labels   ', 'xop701', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0702', 'X/O,ALTER TABLE ADD                               ', 'xop702', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0703', 'X/O,CREATE INDEX on existent/non-existent tables  ', 'xop703', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0706', 'X/O,CREATE INDEX on at least 6 columns            ', 'xop706', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0707', 'X/O,Limit on depth of nested sub-queries          ', 'xop707', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0708', 'X/O,Limit on the total length of an Index Key     ', 'xop708', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0709', 'X/O,SQL Escape Clause Processing                  ', 'xop709', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0710', 'X/O,Acceptance of correctly placed SQLCA          ', 'xop710', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0712', 'X/O,MAPPING OF DATATYPES ONTO SQL DECIMAL         ', 'xop712', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0719', 'X/O,GRANT ALL with optional PRIVILEGES omitted    ', 'xop719', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0720', 'X/O,GRANT ALL with optional PRIVILEGES omitted    ', 'xop720', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0721', 'X/O,REVOKE ALL with optional PRIVILEGES omitted   ', 'xop721', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0722', 'X/O,REVOKE ALL with optional PRIVILEGES omitted   ', 'xop722', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0723', 'X/O,DROP TABLE with outstanding grants and views  ', 'xop723', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0724', 'X/O,MAPPING ONTO SQL SMALLINT, DECIMAL AND INTEGER', 'xop712', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0725', 'X/O,INCLUDE SQLCA IN LINKAGE SECTION              ', 'xop725', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0829', '<drop behavior> on a REVOKE (dynamic)             ', 'dml140', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0830', 'FIPS Flagger - WHENEVER SQLWARNING                ', 'flg010', 'visual chk', 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0831', 'FIPS Flagger - ADD (column, ...)                  ', 'flg011', 'visual chk', 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0832', 'FIPS Flagger - CREATE INDEX                       ', 'flg012', 'visual chk', 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0833', 'FIPS Flagger - INCLUDE SQLCA                      ', 'flg013', 'visual chk', 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0834', '<length expression> (static)                      ', 'dml144', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0835', '<character substring function> (static)           ', 'dml144', NULL, 22);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0836', '<length expression> (dynamic)                     ', 'dml145', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0837', '<character substring function> (dynamic)          ', 'dml145', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0838', '<character substring function> varchar            ', 'dml146', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0839', 'Composed <length expression> and SUBSTRING        ', 'dml144', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0840', 'Roll back schema manipulation                     ', 'dml147', 'synvio_yes', 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0841', 'Multiple-join and default order of joins          ', 'dml147', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0842', 'Multi-column joins                                ', 'dml147', NULL, 36);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0843', 'Ordering of column names in joins                 ', 'dml148', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0844', 'Outer join predicates                             ', 'dml148', NULL, 45);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0845', 'Parameters and indicators in dynamic SQL statement', 'dml150', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0846', 'Feature 20, CAST functions (static) nits          ', 'dml149', NULL, 34);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0847', 'Dynamic SQL:  serializability                     ', 'mpa009', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0848', 'Query spec with subquery is now updatable         ', 'dml153', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0849', 'Descriptors:  datetime length in positions        ', 'dml154', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0850', 'Comparing fixed vs. variable length char strings  ', 'dml155', NULL, 15);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0851', 'Errata:  SQL_STANDARD changed to Interfaces.SQL   ', 'ada010', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0852', 'Transitive grant in COLUMN_PRIV, TABLE_PRIV       ', 'dml154', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0853', 'Exceptions not affecting position of cursor       ', 'dml151', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0854', 'Informational:  mixing SDL and DML                ', 'dml154', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0855', 'Dynamic SQL syntax errors                         ', 'dml156', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0856', 'Transitional Schema Definition                    ', 'dml157', NULL, 98);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0857', '<join condition> set function, outer reference    ', 'dml158', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0858', '? (dyn parm spec) in <having clause>              ', 'dml159', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0859', '<joined table> contained in <select list>         ', 'dml160', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0860', 'Domains over various data types                   ', 'dml160', NULL, 33);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0861', 'CURRENT_USER, SESSION_USER, SYSTEM_USER           ', 'dml161', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0862', 'CURRENT_USER etc. with set session authid         ', 'dml161', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0863', '<joined table> directly contained in cursor, view ', 'dml162', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0864', 'Intermediate Dynamic SQL syntax errors            ', 'dml163', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0865', 'Result data types for case expressions            ', 'dml163', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0866', 'Case expressions in other than SELECT             ', 'dml163', NULL, 15);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0867', 'LIKE enhancements:  keyword search                ', 'dml164', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0868', 'More <unique predicate>                           ', 'dml164', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0869', 'More table operations                             ', 'dml164', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0870', 'Non-identical descriptors in UNION                ', 'dml165', NULL, 13);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0871', 'Errata:  Interfaces.SQL.Numerics--TC3 clause 23.3 ', 'ada011', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0872', 'Errata:  Interfaces.SQL.Varying--TC3 clause 23.3  ', 'ada012', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0873', 'Dynamic schema creation                           ', 'dml166', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0874', 'INFORMATION_SCHEMA catalog columns                ', 'dml167', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0875', 'INFORMATION_SCHEMA column coverage                ', 'dml167', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0876', 'SQL_IDENTIFIER and CHARACTER_DATA domains         ', 'dml168', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0877', 'Intermediate DB, Flag at Entry level              ', 'dml169', 'visual chk', 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0878', 'Keyword COLUMN in ALTER TABLE is optional         ', 'dml168', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0879', '<drop table constraint definition>                ', 'dml168', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0880', 'Long constraint names, cursor names               ', 'dml170', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0881', 'Long character set names, domain names            ', 'dml170', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0882', 'More full outer join                              ', 'dml171', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0883', 'Errata:  Interfaces.SQL.Varying.NCHAR--TC3, 23.3  ', 'ada013', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0884', 'ASCII_FULL and SQL_TEXT in column definition      ', 'dml172', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0885', 'FIPS sizing, CHAR (1000)                          ', 'dml173', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0886', 'FIPS sizing, VARCHAR (1000)                       ', 'dml174', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0887', 'FIPS sizing, NCHAR (500)                          ', 'dml175', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0888', 'FIPS sizing, NCHAR VARYING (500)                  ', 'dml176', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0889', 'FIPS sizing, INTEGER binary prec >= 31            ', 'dml177', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0890', 'FIPS sizing, SMALLINT binary prec >= 15           ', 'dml177', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0891', 'FIPS sizing, 250 columns, 4000 char data statement', 'dml178', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0892', 'FIPS sizing, rowlen >= 8000, statement var >= 4000', 'dml179', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0893', 'FIPS sizing, descriptor occurrences >= 100        ', 'dml180', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0894', 'FIPS sizing, length of column lists >= 750        ', 'dml181', NULL, 13);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0895', 'FIPS sizing, columns in list >= 15                ', 'dml182', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0896', 'FIPS sizing, 50 WHEN clauses in a CASE expression ', 'dml183', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0897', 'Constraint usage redux                            ', 'dml184', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0898', 'COLUMN_DEFAULT interpretation                     ', 'dml185', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('0899', 'FIPS sizing, INTERVAL decimal leading field prec  ', 'dml186', NULL, 18);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7001', 'NULLIF producing NULL                             ', 'xts700', NULL, 13);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7002', 'NULLIF producing non-NULL                         ', 'xts798', NULL, 27);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7003', 'COALESCE with three <value expression>s           ', 'xts799', NULL, 15);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7004', 'Compound char. literal in <comparison predicate>  ', 'xts701', NULL, 13);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7005', 'Compound character literal as inserted value      ', 'xts701', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7006', 'Compound character literal in a <select list>     ', 'xts701', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7007', 'LIKE with unrestricted <match value>              ', 'xts702', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7008', 'LIKE with general char. value for pattern + escape', 'xts702', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7009', 'LIKE with zero-length escape                      ', 'xts702', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7010', 'UNIQUE predicate, single table, all values distinc', 'xts703', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7011', 'UNIQUE PREDICATE, table subquery with non-null dup', 'xts703', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7012', 'UNIQUE predicate, duplicates containing null      ', 'xts703', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7013', 'Schema definition in an SQL statement-single table', 'xts713', NULL, 26);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7014', 'Schema definition named schema, implicit auth-id. ', 'xts714', NULL, 23);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7015', 'Schema definition - explicit name and auth-id.    ', 'xts715', NULL, 30);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7016', 'SET SESSION AUTHORIZATION to current auth-id.     ', 'xts716', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7017', 'SET SESSION AUTH. to current auth-id in. transactn', 'xts717', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7018', 'SET SESSION AUTHORIZATION to different value      ', 'xts718', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7019', 'Access to KEY_COLUMN_USAGE view                   ', 'xts719', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7020', 'Access to VIEW_TABLE_USAGE view                   ', 'xts720', NULL, 15);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7021', 'Access to VIEW_COLUMN_USAGE view                  ', 'xts721', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7022', 'Access to CONSTRAINT_TABLE_USAGE view             ', 'xts722', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7023', 'Access to CONSTRAINT_COLUMN_USAGE view            ', 'xts723', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7024', 'Access to COLUMN_DOMAIN_USAGE view                ', 'xts724', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7025', 'Flagging - Full SQL INSENSITIVE cursor            ', 'xts725', 'visual chk', 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7026', 'Flagging Full SQL - cursor FOR UPDATE and ORDER BY', 'xts726', 'visual chk', 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7027', 'Flagging - Full SQL - <explicit table> in <qry exp', 'xts727', 'visual chk', 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7028', 'Flagging,Full SQL,<null predicate> with two-col ro', 'xts728', 'visual chk', 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7029', 'Column name with 19 and 128 characters - regular. ', 'xts729', NULL, 41);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7030', 'Table name with 19 characters - delimited.        ', 'xts730', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7031', 'View name with 128 characters - delimited.        ', 'xts731', NULL, 26);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7032', 'NATURAL FULL OUTER JOIN <table ref> -- static.    ', 'xts732', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7033', 'FULL OUTER JOIN <table ref> ON <search condition> ', 'xts733', NULL, 40);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7034', 'National Character data type in comparison predica', 'xts734', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7035', 'INSERT National character literal in NCHAR column ', 'xts735', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7036', 'Update NCHAR VARYING column with value from NCHAR ', 'xts736', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7037', 'Scrolled cursor with ORDER BY DESC, FETCH NEXT    ', 'xts737', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7038', 'Scrolled cursor with ORDER BY DESC, FETCH PRIOR   ', 'xts738', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7039', 'Scrolled cursor with ORDER BY int, name ASC, FETCH', 'xts739', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7040', 'COUNT(ALL <column name>) with Nulls in column     ', 'xts740', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7041', 'COUNT(ALL NULLIF...) with generated Nulls         ', 'xts741', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7042', 'COUNT ALL <literal>                               ', 'xts742', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7044', 'Presence of SQL_CHARACTER in CHARACTER_SETS view  ', 'xts744', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7045', 'Presence of ASCII_GRAPHIC in CHARACTER_SETS view  ', 'xts745', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7046', 'Presence of LATIN1 in CHARACTER_SETS view         ', 'xts746', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7047', 'Presence of ASCII_FULL in CHARACTER_SETS view     ', 'xts747', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7048', 'Named constraint in column definition in schema de', 'xts748', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7049', 'Named table constraint in table definition.       ', 'xts749', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7050', 'Named domain constraint.                          ', 'xts750', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7051', 'Name of violated column constraint returned in dia', 'xts751', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7052', 'ALTER TABLE ADD TABLE CONSTRAINT                  ', 'xts752', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7053', 'ALTER TABLE ADD COLUMN WITH <data type>           ', 'xts753', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7054', 'ALTER TABLE ADD COLUMN WITH domain and constraint ', 'xts754', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7055', 'ALTER TABLE DROP COLUMN RESTRICT                  ', 'xts755', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7056', 'ALTER TABLE DROP COLUMN CASCADE                   ', 'xts756', NULL, 13);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7058', 'Scrolled cursor FETCH ABSOLUTE non-literal, after ', 'xts758', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7059', 'Scrolled cursor on grouped view,FETCH RELATIVE,FIR', 'xts759', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7060', 'MAX of column derived from <set function specifica', 'xts760', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7061', 'Defined character set in <comparison predicate>   ', 'xts761', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7062', 'Defined character set in <like predicate>         ', 'xts762', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7063', 'Access to CHARACTER_SETS view                     ', 'xts763', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7064', 'REVOKE USAGE on character set RESTRICT            ', 'xts764', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7065', 'REVOKE USAGE on character set CASCADE             ', 'xts765', NULL, 68);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7066', 'Drop character set no granted privileges          ', 'xts766', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7067', 'DROP character set, outstanding granted privileges', 'xts767', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7068', 'Presence of SQL_TEXT in CHARACTER_SETS view       ', 'xts768', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7069', '<Character set specification> of LATIN1 in <litera', 'xts769', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7070', '<Character set specification> of SQL_CHARACTER in ', 'xts770', NULL, 9);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7071', 'CHARACTER SET ASCII_GRAPHIC in <data type>        ', 'xts771', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7500', 'CREATE DOMAIN -SQL Procedure statement,no options ', 'yts750', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7501', 'CREATE DOMAIN as SQL proc statement with default  ', 'yts751', NULL, 18);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7502', 'CREATE DOMAIN-SQL proc statement with constraint  ', 'yts752', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7503', 'DROP DOMAIN RESTRICT                              ', 'yts753', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7504', 'DROP DOMAIN CASCADE - domain definition in use    ', 'yts754', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7505', 'DROP DOMAIN CASCADE-domain w. default + constraint', 'yts755', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7506', 'Domain Constraint Containing VALUE                ', 'yts756', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7507', 'INSERT value in column defined on domain          ', 'yts757', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7508', 'Put value in col defined on dom breaking constrain', 'yts757', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7509', 'GRANT USAGE on a domain                           ', 'yts759', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7510', 'DROP SCHEMA - empty schema with restrict          ', 'yts776', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7511', 'DROP SCHEMA - non-empty schema                    ', 'yts777', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7512', 'Scr.cursor,no ORDER,FETCH all,FIRST,LAST,NEXT     ', 'yts783', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7513', 'Scr.cursor with joined table, FETCH ABS literal   ', 'yts784', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7517', '<query expression> with EXCEPT                    ', 'yts762', NULL, 32);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7518', '<query expression> with INTERSECT CORRESPONDING   ', 'yts763', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7519', '<query expression> with UNION ALL CORRESPONDING BY', 'yts764', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7520', 'ALTER TABLE SET COLUMN DEFAULT                    ', 'yts778', NULL, 28);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7521', 'ALTER TABLE DROP COLUMN DEFAULT                   ', 'yts779', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7522', 'CREATE CHARACTER SET, implicit default collation  ', 'yts788', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7523', 'CREATE CHAR SET in schema def,COLLATION FROM DEFLT', 'yts789', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7524', 'GRANT USAGE on character set,WITH GRANT OPTION    ', 'yts790', NULL, 1);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7525', 'GRANT USAGE on character set,WITH GRANT OPTION    ', 'yts791', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7526', 'GRANT USAGE on character set,WITH GRANT OPTION    ', 'yts792', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7527', 'GRANT USAGE on character set,no WGO               ', 'yts793', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7528', 'GRANT USAGE on character set, no WGO              ', 'yts794', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7529', 'GRANT USAGE on character set, no WGO              ', 'yts795', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7530', '<scalar subquery> as first operand in <comp pred> ', 'yts796', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7531', '<subqry> as<row val constructor>in<null predicate>', 'yts799', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7532', '<nul pred><interval value exp> as <row value cons>', 'yts800', NULL, 2);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7534', 'CASE expression with one simple WHEN              ', 'yts760', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7535', 'CASE expression with searched WHEN                ', 'yts761', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7536', 'Set local time zone - valid value                 ', 'yts781', NULL, 27);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7537', 'Explicit table constrnts in TABLE_CONSTRAINTS view', 'yts765', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7538', 'Column constraints in TABLE_CONSTRAINTS view      ', 'yts765', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7539', 'Unique identification in TABLE_CONSTRAINTS view   ', 'yts765', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7540', 'Explicit table constrnts - REFERENTIAL_CONSTRAINTS', 'yts766', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7541', 'Column constraints in REFERENTIAL_CONSTRAINTS view', 'yts766', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7542', 'Unique id in REFERENTIAL_CONSTRAINTS view         ', 'yts766', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7543', 'Values in columns of REFERENTIAL_CONSTRAINTS view ', 'yts766', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7544', 'Explicit table constr. in CHECK_CONSTRAINTS view  ', 'yts767', NULL, 20);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7545', 'Column constraints in CHECK_CONSTRAINTS view      ', 'yts767', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7546', 'Domain constraints in CHECK_CONSTRAINTS view      ', 'yts767', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7547', 'Unique identification in CHECK_CONSTRAINTS view   ', 'yts767', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7548', 'Support of SQL_FEATURES tab. in documentatn schema', 'yts802', NULL, 8);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7549', 'Support SQL_SIZING table in documentation schema  ', 'yts803', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7550', 'Access to SCHEMATA view                           ', 'yts768', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7551', 'Access to DOMAINS view                            ', 'yts769', NULL, 11);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7552', 'Access to DOMAIN_CONSTRAINTS view                 ', 'yts770', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7553', 'Access to CHARACTER_SETS view                     ', 'yts771', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7554', 'Access to ASSERTIONS view                         ', 'yts772', NULL, 6);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7555', 'Access to SQL_LANGUAGES view                      ', 'yts773', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7556', 'Access to INFORMATION_SCHEMA_CATALOG_NAME base tab', 'yts774', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7557', 'SQL host prog. with duplicate local variable names', 'yts775', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7558', '<scalar subquery> in SET of searched update       ', 'yts797', NULL, 7);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7559', '<scalar subqry> in <sel.list> of single-row sel.  ', 'yts798', NULL, 3);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7560', '<time zone interval> in literal                   ', 'yts780', NULL, 17);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7561', 'Set local time zone - invalid value, exception    ', 'yts782', NULL, 12);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7562', 'Schema with crossed referential const. bet. tables', 'yts805', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7563', 'NATURAL FULL OUTER JOIN <table ref> - dynamic     ', 'yts806', NULL, 14);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7564', 'TIMEZONE_HOUR + TIMEZONE_MINUTE in <extract expr.>', 'yts807', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7565', 'LOCAL time zone in <datetime value expression>    ', 'yts808', NULL, 10);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7566', 'TIME ZONE in <datetime value expression>          ', 'yts809', NULL, 5);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7567', 'FULL OUTER JOIN<table ref>ON<search condition> dyn', 'yts810', NULL, 19);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7568', 'WHERE <search cond.> referencing column           ', 'yts811', NULL, 16);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7569', '<null predicate>,concat. in<row value constructor>', 'yts812', NULL, 4);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7570', '<null predct>, <numrc val expr> in <row val cons> ', 'yts813', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTCASE VALUES ('7571', '<module character set specification>              ', 'yts814', NULL, 0);");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0001', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0002', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0003', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0004', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0005', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0006', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0007', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0008', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0009', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0010', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0011', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0012', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0013', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0014', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0015', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0016', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0017', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0018', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0019', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0020', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0021', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0022', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0023', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0024', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0025', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0026', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0027', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0028', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0031', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0033', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0034', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0035', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0036', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0037', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0038', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0039', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0040', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0041', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0042', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0043', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0044', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0045', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0046', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0047', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0048', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0049', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0050', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0051', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0052', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0053', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0054', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0055', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0056', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0057', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0058', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0059', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0060', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0061', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0062', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0063', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0064', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0065', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0066', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0067', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0068', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0069', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0070', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0071', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0072', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0073', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0074', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0075', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0076', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0077', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0078', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0079', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0080', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0081', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0082', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0083', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0084', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0085', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0086', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0087', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0088', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0089', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0090', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0091', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0092', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0093', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0094', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0095', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0096', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0097', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0098', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0099', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0100', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0101', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0102', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0103', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0104', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0105', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0106', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0107', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0108', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0109', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0110', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0111', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0112', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0113', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0114', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0115', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0116', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0117', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0118', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0119', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0120', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0121', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0122', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0123', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0124', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0125', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0126', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0127', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0128', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0129', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0130', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0131', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0135', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0137', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0137', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0138', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0139', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0140', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0141', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0142', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0143', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0144', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0145', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0146', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0147', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0148', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0149', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0150', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0151', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0152', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0153', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0154', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0155', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0156', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0157', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0158', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0159', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0160', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0161', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0162', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0163', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0164', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0165', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0166', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0167', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0168', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0169', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0170', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0171', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0172', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0173', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0174', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0175', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0176', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0177', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0178', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0179', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0180', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0181', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0182', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0183', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0184', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0185', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0186', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0187', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0188', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0189', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0190', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0191', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0192', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0193', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0194', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0195', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0196', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0197', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0198', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0199', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0200', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0201', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0202', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0203', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0204', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0205', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0206', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0207', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0208', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0209', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0210', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0211', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0212', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0213', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0214', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0215', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0215', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0216', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0216', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0217', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0218', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0218', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0219', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0219', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0220', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0220', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0221', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0221', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0222', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0223', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0224', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0225', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0226', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0227', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0228', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0229', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0230', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0231', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0232', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0233', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0234', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0235', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0236', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0237', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0238', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0239', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0240', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0241', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0242', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0243', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0244', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0245', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0246', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0246', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0247', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0248', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0249', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0250', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0251', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0252', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0253', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0254', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0255', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0256', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0257', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0258', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0259', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0260', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0261', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0262', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0263', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0264', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0265', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0266', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0267', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0268', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0269', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0270', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0271', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0272', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0273', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0274', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0275', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0276', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0277', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0278', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0279', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0280', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0281', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0282', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0283', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0284', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0285', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0286', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0287', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0288', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0289', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0290', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0291', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0292', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0293', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0294', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0295', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0296', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0297', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0298', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0299', '0CFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0300', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0301', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0302', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0303', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0304', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0305', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0306', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0307', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0308', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0309', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0310', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0311', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0312', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0313', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0314', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0315', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0316', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0317', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0318', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0319', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0320', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0321', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0322', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0323', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0324', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0325', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0326', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0327', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0328', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0329', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0330', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0331', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0332', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0333', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0334', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0335', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0336', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0337', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0338', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0339', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0340', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0341', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0342', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0343', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0344', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0345', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0346', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0347', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0347', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0348', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0348', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0349', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0349', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0350', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0350', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0351', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0351', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0352', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0352', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0353', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0353', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0354', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0354', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0355', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0355', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0356', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0356', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0357', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0357', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0358', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0358', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0359', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0359', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0360', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0360', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0361', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0361', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0362', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0362', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0363', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0363', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0364', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0364', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0365', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0366', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0367', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0368', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0369', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0370', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0371', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0372', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0373', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0374', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0375', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0376', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0377', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0378', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0379', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0380', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0381', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0382', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0383', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0384', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0385', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0386', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0387', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0388', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0388', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0389', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0390', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0391', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0392', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0393', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0394', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0395', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0396', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0397', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0398', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0399', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0401', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0402', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0403', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0404', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0405', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0406', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0407', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0408', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0409', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0410', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0411', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0412', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0413', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0414', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0415', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0416', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0417', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0418', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0419', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0420', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0421', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0422', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0423', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0424', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0425', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0426', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0427', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0428', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0430', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0431', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0432', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0433', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0434', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0435', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0436', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0437', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0438', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0439', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0440', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0441', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0442', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0443', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0444', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0445', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0446', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0447', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0448', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0449', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0450', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0451', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0452', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0453', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0454', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0455', '0CFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0456', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0457', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0458', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0459', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0460', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0461', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0462', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0463', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0464', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0465', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0466', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0467', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0468', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0469', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0470', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0471', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0472', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0473', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0474', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0475', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0476', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0477', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0478', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0479', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0480', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0481', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0482', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0483', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0484', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0485', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0486', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0487', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0488', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0489', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0490', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0491', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0492', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0493', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0494', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0495', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0496', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0497', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0498', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0499', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0500', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0501', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0502', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0503', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0504', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0505', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0506', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0507', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0508', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0509', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0510', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0511', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0512', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0513', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0514', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0515', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0516', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0516', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0517', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0518', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0519', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0520', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0521', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0522', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0523', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0524', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0524', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0525', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0526', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0526', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0527', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0528', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0529', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0530', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0531', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0532', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0533', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0534', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0535', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0536', '0SZE');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0537', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0538', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0539', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0554', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0556', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0556', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0557', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0557', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0558', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0558', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0559', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0559', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0560', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0561', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0561', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0562', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0562', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0564', '00L2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0565', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0565', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0565', '0010');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0566', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0566', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0566', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0566', '0010');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0567', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0567', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0567', '0010');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0568', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0568', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0569', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0569', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0570', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0570', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0570', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0571', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0571', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0571', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0572', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0572', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0572', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0573', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0573', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0573', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0574', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0574', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0574', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0574', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0574', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0575', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0575', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0575', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0576', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0576', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0576', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0577', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0577', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0577', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0578', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0578', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0578', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0579', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0580', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0581', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0581', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0581', '0009');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0582', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0582', '0009');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0583', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0584', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0585', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0586', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0587', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0587', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0587', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0588', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0588', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0588', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0589', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0589', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0589', '0019');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0589', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0590', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0590', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0590', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0590', '0019');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0590', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0591', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0591', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0592', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0592', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0593', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0593', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0594', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0594', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0595', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0595', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0595', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0596', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0596', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0596', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0597', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0597', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0597', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0598', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0598', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0598', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0599', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0599', '0008');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0600', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0600', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0600', '0008');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0601', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0601', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0602', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0602', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0602', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0603', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0603', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0603', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0604', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0604', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0605', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0605', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0605', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0605', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0606', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0606', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0607', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0607', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0608', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0608', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0609', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0609', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0609', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0610', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0610', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0611', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0611', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0612', '0FSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0612', '0SZT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0613', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0613', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0613', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0614', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0614', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0614', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0614', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0615', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0615', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0615', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0616', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0616', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0616', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0616', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0617', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0617', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0617', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0618', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0618', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0618', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0618', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0619', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0619', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0619', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0620', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0620', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0620', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0620', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0621', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0621', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0621', '0008');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0621', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0622', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0622', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0622', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0622', '0008');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0622', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0623', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0623', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0624', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0624', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0624', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0625', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0625', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0625', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0626', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0626', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0626', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0626', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0627', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0628', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0629', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0629', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0630', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0630', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0631', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0631', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0631', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0631', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0631', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0632', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0632', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0632', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0632', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0632', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0632', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0633', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0633', '0007');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0634', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0634', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0634', '0007');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0635', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0635', '0013');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0636', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0636', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0636', '0013');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0637', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0637', '0014');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0638', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0638', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0638', '0014');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0639', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0639', '0015');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0640', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0640', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0640', '0015');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0641', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0641', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0641', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0642', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0642', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0642', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0642', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0643', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0644', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0644', '0018');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0645', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0645', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0645', '0019');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0645', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0646', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0646', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0646', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0646', '0019');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0646', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0647', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0647', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0647', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0648', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0648', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0648', '0009');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0648', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0648', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0649', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0649', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0649', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0650', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0650', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0650', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0650', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0651', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0651', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0652', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0652', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0652', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0653', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0653', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0653', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0654', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0654', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0654', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0655', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0655', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0655', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0656', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0656', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0656', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0657', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0657', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0657', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0657', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0658', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0659', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0659', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0660', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0661', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0661', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0661', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0662', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0662', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0662', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0662', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0663', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0663', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0664', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0664', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0664', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0665', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0665', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0666', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0666', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0667', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0667', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0668', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0668', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0669', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0669', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0669', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0669', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0670', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0670', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0670', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0671', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0671', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0671', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0672', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0672', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0672', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0672', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0673', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0673', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0674', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0674', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0675', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0675', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0675', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0675', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0676', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0676', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0676', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0677', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0677', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0678', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0678', '0010');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0679', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0679', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0679', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0679', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0680', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0680', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0680', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0681', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0681', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0681', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0681', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0682', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0682', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0682', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0682', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0683', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0683', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0683', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0684', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0685', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0685', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0685', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0686', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0686', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0686', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0686', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0687', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0687', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0688', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0688', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0688', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0688', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0007');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0008');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0689', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0690', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0691', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0691', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0691', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0691', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0013');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0015');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0016');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0019');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0692', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0015');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0693', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0694', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0694', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0694', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0695', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0014');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0696', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0697', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0697', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0697', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0698', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0698', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0698', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0698', '0IEF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0699', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0699', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0700', '0XSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0701', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0702', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0703', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0706', '0XSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0707', '0XSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0708', '0XSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0709', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0710', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0712', '0XSZ');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0719', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0720', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0721', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0722', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0723', '0XC2');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0724', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0725', '0XC3');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0829', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0829', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0829', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0830', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0831', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0832', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0833', '0EFL');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0834', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0834', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0834', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0835', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0835', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0835', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0836', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0836', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0836', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0836', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0837', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0837', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0837', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0837', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0838', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0838', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0838', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0839', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0839', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0839', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0840', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0841', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0842', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0842', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0843', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0844', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0844', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0845', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0845', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0845', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0846', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0846', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0846', '0021');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0847', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0848', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0848', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0848', '0FIP');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0849', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0849', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0849', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0850', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0850', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0851', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0852', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0852', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0852', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0853', '0ENT');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0854', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0855', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0855', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0855', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0855', '0007');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0856', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0856', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0856', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0856', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0857', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0858', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0859', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0859', '0047');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0008');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0860', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0861', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0862', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0863', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0863', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0864', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0864', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0864', '0027');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0865', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0866', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0866', '0048');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0867', '0027');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0867', '0028');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0868', '0029');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0869', '0030');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0870', '0030');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0871', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0872', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0873', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0874', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0875', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0876', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0876', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0877', '0037');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0878', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0879', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0879', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0880', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0880', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0881', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0881', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0881', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0882', '0040');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0883', '0INF');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0884', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0885', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0886', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0887', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0888', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0889', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0890', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0891', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0892', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0893', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0894', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0895', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0896', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0897', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0897', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0897', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0898', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0898', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0898', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0898', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('0899', '0SZI');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7001', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7002', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7003', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7004', '0027');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7005', '0027');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7006', '0027');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7007', '0028');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7008', '0028');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7009', '0007');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7009', '0028');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7010', '0029');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7011', '0029');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7012', '0029');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7013', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7014', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7014', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7015', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7015', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7015', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7016', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7016', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7017', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7018', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7019', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7019', '0003');");	
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7019', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7020', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7020', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7020', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7020', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7021', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7021', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7021', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7022', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7022', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7022', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7022', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7023', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7023', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7023', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7023', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7024', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7024', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7024', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7024', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7024', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7025', '0037');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7026', '0037');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7027', '0037');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7028', '0037');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7029', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7029', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7029', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7029', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7030', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7030', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7030', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7031', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7031', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7031', '0039');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7032', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7032', '0011');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7032', '0040');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7033', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7033', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7033', '0040');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7034', '0042');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7035', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7035', '0042');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7036', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7036', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7036', '0022');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7036', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7036', '0042');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7037', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7037', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7038', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7038', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7039', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7039', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7040', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7040', '0044');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7041', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7041', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7041', '0044');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7042', '0044');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7044', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7044', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7045', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7045', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7046', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7046', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7047', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7047', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7048', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7048', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7048', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7048', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7048', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7048', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7049', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7049', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7049', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7049', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7050', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7050', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7050', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7050', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7050', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7051', '0012');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7051', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7052', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7052', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7052', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7052', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7052', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7053', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7053', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7054', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7054', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7054', '0034');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7054', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7054', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7055', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7055', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7056', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7056', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7056', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7056', '0049');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7058', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7058', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7059', '0024');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7059', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7060', '0044');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7061', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7062', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7063', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7063', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7064', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7064', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7064', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7064', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7064', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7065', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7065', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7065', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7065', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7066', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7066', '0023');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7066', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7067', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7067', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7067', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7067', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7068', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7068', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7069', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7069', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7070', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7070', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7071', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7071', '0006');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7071', '0046');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7500', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7500', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7500', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7501', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7501', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7501', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7502', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7502', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7503', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7503', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7504', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7504', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7505', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7505', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7506', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7506', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7507', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7507', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7508', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7508', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7509', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7509', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7509', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7510', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7510', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7510', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7510', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7510', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7511', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7511', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7511', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7511', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7512', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7513', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7513', '0043');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7517', '0030');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7518', '0030');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7519', '0030');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7520', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7520', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7520', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7521', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7521', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7521', '0038');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7522', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7522', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7523', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7523', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7523', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7524', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7524', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7524', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7524', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7524', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7525', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7525', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7525', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7525', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7525', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7526', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7526', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7526', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7526', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7526', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7527', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7527', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7527', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7527', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7528', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7528', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7528', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7528', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7528', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7529', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7529', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7529', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7529', '0032');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7529', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7530', '0047');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7531', '0047');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7531', '0048');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7532', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7532', '0048');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7534', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7535', '0026');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7536', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7536', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7537', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7537', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7538', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7538', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7539', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7539', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7540', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7540', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7541', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7541', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7542', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7542', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7543', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7543', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7544', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7544', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7544', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7544', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7545', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7545', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7545', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7545', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7546', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7546', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7546', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7546', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7546', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7547', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7547', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7547', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7547', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7548', '0050');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7549', '0050');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7550', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7550', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7551', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7551', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7551', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7552', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7552', '0025');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7552', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7553', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7554', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7555', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7556', '0035');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7557', '0036');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7558', '0047');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7559', '0047');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7560', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7560', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7561', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7561', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7561', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7562', '0017');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7562', '0031');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7562', '0033');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7563', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7563', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7563', '0040');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7564', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7564', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7564', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7565', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7565', '0020');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7565', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7566', '0005');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7566', '0041');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7567', '0001');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7567', '0004');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7567', '0040');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7568', '0044');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7569', '0048');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7570', '0048');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7571', '0002');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7571', '0003');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7571', '0045');");
		stmt.executeUpdate("INSERT INTO TESTFEATURE VALUES ('7571', '0046');");
		stmt.executeUpdate("INSERT INTO FEATURE_CLAIMED SELECT C1 FROM F_TEMP;");
		stmt.executeUpdate("DELETE FROM F_TEMP;");
	}
	
	public static void setupXdrop1(Statement stmt) throws SQLException {
		int       errorCode; // Error code.

		stmt.executeUpdate("DROP VIEW TESTREPORT  /* CASCADE */ ;");
		stmt.executeUpdate("DROP TABLE ECCO  /* CASCADE */ ;");
	
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE AAA  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view AAA does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE BBB  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view BBB does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE CCC  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view CCC does not exist");
			}	
		}		
			
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE CHAR_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view CHAR_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE INT_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view INT_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE SMALL_TEST  /* CASCADE */ ;"); 
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view SMALL_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE REAL_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view REAL_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE REAL3_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view REAL3_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE DOUB_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view DOUB_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE DOUB3_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view DOUB3_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE FLOAT_TEST  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view FLOAT_TEST does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE INDEXLIMIT  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view INDEXLIMIT does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE WIDETABLE  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view WIDETABLE does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE WIDETAB  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view WIDETAB does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE TEST_TRUNC  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view TEST_TRUNC does not exist");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE WARNING  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view WARNING does not exist");
			}	
		}		
		
		stmt.executeUpdate("DROP TABLE TV  /* CASCADE */ ;");
		stmt.executeUpdate("DROP TABLE TU  /* CASCADE */ ;");

		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE STAFF  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return cannot delete COLUMN EMPNUM " +
						"there are 4 dependencies");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE PROJ  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return cannot delete COLUMN PNUM " +
						"there are 1 dependencies");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE WORKS  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return cannot delete COLUMN EMPNUM " +
						"there are 4 dependencies");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("DROP TABLE INTS  /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view INTS does not exist");
			}	
		}		
	}
	
	public static void setupXdrop2(Statement stmt) throws SQLException {
		int       errorCode; /* Error code.                           */

		errorCode = 0;
		try {
			stmt.executeUpdate("DROP VIEW XOPEN2V /* CASCADE */ ;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return table/view INTS does not exist");
			}	
		}		
	}
	
	
	public static void setupXschema1(Statement stmt) throws SQLException {
		int       errorCode; // Error code.

		// X/OPEN Extensions SQL Test Suite, V6.0, Schema Definition, xschema1.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID XOPEN1 ******
		// ***************************************************************

		// This file defines the base tables used in most of the tests. It
		// is used for X/Open Testing only.

		// Users may delete the CREATE SCHEMA statement or add further
		// statements, as necessary, to permit creation of the schema
		// without prejudice to the implementation's X/Open conformance status
		// Implementations which support CREATE UNIQUE INDEX instead of the
		// UNIQUE syntax in this schema definition should use xschema1.nc.

		// Implementations which support CREATE UNIQUE INDEX (instead of UNIQUE
		// syntax found in this schema definition) should use file xschema1.nc.

		//  CREATE SCHEMA AUTHORIZATION XOPEN1;

		stmt.executeUpdate("CREATE TABLE ECCO (C1 CHAR(6));");

		stmt.executeUpdate("CREATE TABLE AAA (A1 CHAR(2), A2 CHAR(2), A3 CHAR(2));");
		stmt.executeUpdate("CREATE TABLE BBB (B1 CHAR(2), B2 CHAR(2), B3 CHAR(2) NOT NULL UNIQUE);");
		stmt.executeUpdate("CREATE TABLE CCC (C1 CHAR(2), C2 CHAR(2), C3 CHAR(2));");

		stmt.executeUpdate("CREATE TABLE CHAR_TEST (COL1 CHAR(254));");
		stmt.executeUpdate("CREATE TABLE INT_TEST (COL1 INTEGER);");
		stmt.executeUpdate("CREATE TABLE SMALL_TEST (COL1 SMALLINT);");
		stmt.executeUpdate("CREATE TABLE REAL_TEST (REF CHAR(1),COL1 REAL);");
		stmt.executeUpdate("CREATE TABLE REAL3_TEST (COL1 REAL,COL2 REAL,COL3 REAL);");
		stmt.executeUpdate("CREATE TABLE DOUB_TEST (REF CHAR(1),COL1 DOUBLE PRECISION);");
		stmt.executeUpdate("CREATE TABLE DOUB3_TEST (COL1 DOUBLE PRECISION,COL2 DOUBLE " +
			"PRECISION,COL3 DOUBLE PRECISION);");
	
		// Users may provide an explicit precision for FLOAT_TEST.COL1

		stmt.executeUpdate("CREATE TABLE FLOAT_TEST (REF CHAR(1),COL1 FLOAT);");

		stmt.executeUpdate("CREATE TABLE INDEXLIMIT(COL1 CHAR(2), COL2 CHAR(2), " +
				"COL3 CHAR(2), COL4 CHAR(2), COL5 CHAR(2), " +
				"COL6 CHAR(2), COL7 CHAR(2));");

		stmt.executeUpdate("CREATE TABLE WIDETABLE (WIDE CHAR(118));");
		stmt.executeUpdate("CREATE TABLE WIDETAB (WIDE1 CHAR(38), WIDE2 CHAR(38), WIDE3 CHAR(38));");

		stmt.executeUpdate("CREATE TABLE TEST_TRUNC (TEST_STRING CHAR (6));");

		stmt.executeUpdate("CREATE TABLE WARNING(TESTCHAR CHAR(6), TESTINT INTEGER);");

		stmt.executeUpdate("CREATE TABLE TV (dec3 DECIMAL(3), dec1514 DECIMAL(15,14), " +
				"dec150 DECIMAL(15,0), dec1515 DECIMAL(15,15));");

		stmt.executeUpdate("CREATE TABLE TU (smint SMALLINT, dec1514 DECIMAL(15,14), " +
				"integr INTEGER, dec1515 DECIMAL(15,15));");

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE STAFF " +
					"(EMPNUM   CHAR(3) NOT NULL UNIQUE, " +
					"EMPNAME  CHAR(20), " +
					"GRADE    DECIMAL(4), " +
					"CITY     CHAR(15));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table STAFF already exists");
			}	
		}		
			
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE PROJ " +
					"(PNUM     CHAR(3) NOT NULL UNIQUE, " +
					"PNAME    CHAR(20), " +
					"PTYPE    CHAR(6), " +
					"BUDGET   DECIMAL(9), " +
					"CITY     CHAR(15));");

		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table PROJ already exists");
			}	
		}		
		
		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE TABLE WORKS " +
					"(EMPNUM   CHAR(3) NOT NULL, " +
					"PNUM     CHAR(3) NOT NULL, " +
					"HOURS    DECIMAL(5), " +
					"UNIQUE(EMPNUM,PNUM));");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return Table WORKS already exists");
			}	
		}
		
		stmt.executeUpdate("CREATE TABLE INTS " +
				"(INT1      SMALLINT NOT NULL, " +
				"INT2      SMALLINT NOT NULL);");

		// ************* create view statements follow *************

		errorCode = 0;
		try {
			stmt.executeUpdate("CREATE VIEW TESTREPORT AS " +
					"SELECT TESTNO, RESULT, TESTTYPE " +
					"FROM TESTREPORT;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544569)
			{
				System.out.println("Should return Column unknown TESTNO");
			}	
		}	


		// ************* grant statements follow *************
		stmt.executeUpdate("GRANT SELECT ON ECCO TO PUBLIC;");

		errorCode = 0;
		try {
			stmt.executeUpdate("GRANT INSERT ON TESTREPORT TO PUBLIC;");
		} catch (SQLException excep) {
			errorCode = excep.getErrorCode();
		} finally {
			if (errorCode != 335544351)
			{
				System.out.println("Should return table/view TESTREPORT does not exist");
			}	
		}	

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON STAFF " +
				"TO PUBLIC;");

		stmt.executeUpdate("GRANT SELECT ON WORKS " +
				"TO PUBLIC;");

		stmt.executeUpdate("GRANT SELECT ON PROJ " +
				"TO PUBLIC;");

		stmt.executeUpdate("GRANT ALL PRIVILEGES ON AAA TO XOPEN2;");
		stmt.executeUpdate("GRANT SELECT, UPDATE ON AAA TO XOPEN2 WITH GRANT OPTION;");
		stmt.executeUpdate("GRANT INSERT, DELETE ON BBB TO XOPEN2 WITH GRANT OPTION;");
		stmt.executeUpdate("GRANT SELECT ON BBB TO XOPEN2 WITH GRANT OPTION;");
		stmt.executeUpdate("GRANT SELECT, INSERT ON CCC TO XOPEN2;");


		// ************* End of Schema *************	
	}

	public static void setupXschema2(Statement stmt) throws SQLException {

		// X/OPEN Extensions SQL Test Suite, V6.0, Schema Definition, xschema2.sql
		// 59-byte ID
		// TEd Version #
		// date_time print
		// ***************************************************************
		// ****** THIS FILE SHOULD BE RUN UNDER SCHEMA ID XOPEN2 ******
		// ***************************************************************


		// This schema definition must be run after xschema1.smi, this 
		// schema file is for X/Open testing only.

		// Users may delete the CREATE SCHEMA statement or add further
		// statements, as necessary, to permit creation of the schema
		// without prejudice to the implementation's X/Open conformance status

		//   CREATE SCHEMA AUTHORIZATION XOPEN2;

		// ************* create view statements follow *************

		stmt.executeUpdate("CREATE VIEW XOPEN2V AS SELECT B1, B2, B3 FROM BBB;");

		// ************* grant statements *************

		stmt.executeUpdate("GRANT SELECT ON XOPEN2V TO XOPEN1;");

		// ************* End of Schema *************	
	}	
}