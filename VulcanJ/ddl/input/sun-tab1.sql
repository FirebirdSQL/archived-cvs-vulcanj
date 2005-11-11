 CREATE TABLE STAFF_M (EMPNUM CHAR(3) NOT NULL, EMPNAME CHAR(20), GRADE DECIMAL(4), CITY CHAR(15), PRI_WK CHAR(3), UNIQUE (EMPNUM));
 CREATE TABLE PROJ_M (PNUM CHAR(3) NOT NULL, PNAME CHAR(20), PTYPE CHAR(6), BUDGET DECIMAL(9), CITY CHAR(15), MGR CHAR(3), UNIQUE (PNUM), FOREIGN KEY (MGR) REFERENCES STAFF_M(EMPNUM));
 ALTER TABLE STAFF_M ADD FOREIGN KEY (PRI_WK) REFERENCES PROJ_M (PNUM);
 CREATE TABLE STAFF_C (EMPNUM   CHAR(3) NOT NULL, EMPNAME  CHAR(20), GRADE    DECIMAL(4), CITY     CHAR(15), MGR   CHAR(3), UNIQUE   (EMPNUM), FOREIGN KEY (MGR) REFERENCES STAFF_C(EMPNUM));
 UPDATE STAFF_M SET PRI_WK = NULL;
 UPDATE PROJ_M SET MGR = NULL;
 UPDATE STAFF_C SET MGR = NULL;
 INSERT INTO STAFF_M VALUES('E1','Alice',12,'Deale',NULL);
 INSERT INTO STAFF_M VALUES('E2','Betty',10,'Vienna',NULL);
 INSERT INTO STAFF_M VALUES('E3','Carmen',13,'Vienna',NULL);
 INSERT INTO STAFF_M VALUES('E5','Don',12,'Deale',NULL);
 INSERT INTO STAFF_M VALUES('E4','Don',12,'Deale',NULL);
 INSERT INTO PROJ_M VALUES('P1','MXSS','Design',10000,'Deale',NULL);
 INSERT INTO PROJ_M VALUES('P2','CALM','Code',30000,'Vienna',NULL);
 INSERT INTO PROJ_M VALUES('P4','SDP','Design',20000,'Deale',NULL);
 INSERT INTO PROJ_M VALUES('P3','SDP','Test',30000,'Tample',NULL);
 INSERT INTO PROJ_M VALUES('P5','IRM','Test',10000,'Vienna',NULL);
 INSERT INTO PROJ_M VALUES('P6','PAYR','Design',50000,'Deale',NULL);
 UPDATE STAFF_M SET PRI_WK = 'P1' WHERE EMPNUM = 'E1';
 UPDATE STAFF_M SET PRI_WK = 'P1' WHERE EMPNUM = 'E2';
 UPDATE STAFF_M SET PRI_WK = 'P1' WHERE EMPNUM = 'E3';
 UPDATE STAFF_M SET PRI_WK = 'P2' WHERE EMPNUM = 'E4';
 UPDATE STAFF_M SET PRI_WK = 'P4' WHERE EMPNUM = 'E5';
 UPDATE PROJ_M SET MGR = 'E2' WHERE PNUM = 'P1';
 UPDATE PROJ_M SET MGR = 'E2' WHERE PNUM = 'P2';
 UPDATE PROJ_M SET MGR = 'E3' WHERE PNUM = 'P3';
 UPDATE PROJ_M SET MGR = 'E4' WHERE PNUM = 'P4';
 UPDATE PROJ_M SET MGR = 'E4' WHERE PNUM = 'P5';