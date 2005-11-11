SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- tests from the yts8xx files. many are skipped because of 
-- information schema or timezone issues. 6/15/2005.

-- TEST:7532 <null predicate><interval value exp> as <row value cons>!
-- could not implement this test without interval processing on dates,
-- which FB does not support. 6/15/2005.

-- testYts_802 : skipped
-- TEST:7548 Support of FIPS SQL_FEATURES table in documentation schema!

-- testYts_803 : skipped
-- Test:7549 Support SQL-SIZING table in documentation schema!

-- testYts_805 : skipped
-- TEST:7562 Schema with crossed referential constraints between tables!

-- testYts_806 : skipped
-- TEST:7563 NATURAL FULL OUTER JOIN <table reference> - dynamic!

-- testYts_807 : skipped
-- Firebird SQL doesn't support AT LOCAL for timezone comparisons.
-- TEST:7565 LOCAL time zone in <datetime value expression>!

-- testYts_808 : skipped
-- Firebird SQL doesn't support TIME ZONE calculations.
-- TEST:7566 TIME ZONE in <datetime value expression>!

-- TEST:7567 FULL OUTER JOIN <tab ref> ON <search cond> dynamic!

 CREATE TABLE STAFFa ( HOURS   INTEGER, SALARY  DECIMAL(6), EMPNUM  CHAR(3), PNUM    DECIMAL(4), EMPNAME CHAR(20));
 CREATE TABLE CL_EMPLOYEE (EMPNUM  NUMERIC(5) NOT NULL PRIMARY KEY, DEPTNO  CHAR(3), LOC     CHAR(15), EMPNAME CHAR(20), SALARY  DECIMAL(6), GRADE   DECIMAL(4), HOURS   DECIMAL(5));
 INSERT INTO STAFFa VALUES (20,40000,'E1',11,'Alice');
 INSERT INTO STAFFa VALUES (15,20000,'E2',12,'Betty');
 INSERT INTO STAFFa VALUES (15,20000,'E2',13,'Betty');
 INSERT INTO STAFFa VALUES (10,15000,'E3',14,'Colin');
 INSERT INTO STAFFa VALUES (10,8000,'E3',15,'Colin');
 INSERT INTO STAFFa VALUES (10,8000,'E3',16,'Colin');
 INSERT INTO STAFFa VALUES (30,50000,'E5',17,'Edward');
 CREATE VIEW TA AS SELECT GRADE, DEPTNO, LOC, HOURS FROM CL_EMPLOYEE;
 CREATE VIEW TB AS SELECT EMPNAME, HOURS, EMPNUM, SALARY, PNUM FROM STAFFa;
 INSERT INTO CL_EMPLOYEE VALUES ( 1, 'abc', 'Susan', NULL, NULL, 1, 100);
 INSERT INTO CL_EMPLOYEE VALUES ( 2, 'abc', 'Matthew', NULL, NULL, 7, 100);
 INSERT INTO CL_EMPLOYEE VALUES ( 3, 'abc', 'Peter', NULL, NULL, 2, 100);
 INSERT INTO CL_EMPLOYEE VALUES ( 4, 'abc', 'Rosemary', NULL, NULL, 8, 100);
 INSERT INTO TB VALUES ('Praze-an-beeble    ',1,'aaa',100,3);
 INSERT INTO TB VALUES ('Chy-an-gwel        ',2,'abc',100,4);
 INSERT INTO TB VALUES ('Ponsonooth         ',3,'abc',100,5);
 INSERT INTO TB VALUES ('Tregwedyn          ',4,'abc',100,6);
 SELECT GRADE, COUNT (*) AS CC, EMPNUM FROM TA FULL OUTER JOIN TB ON GRADE > PNUM AND EMPNUM = DEPTNO WHERE GRADE IS NOT NULL GROUP BY GRADE, EMPNUM ORDER BY GRADE;

-- order by clause added to keep results uniform across FB variants.
-- below comment is now incorrect.
-- PASS:7567 If 4 rows returned in the following order?
-- PASS:7567 If 7 3 abc ?
-- PASS:7567 If 8 3 abc ?
-- PASS:7567 If 1 1 NULL ?
-- PASS:7567 If 2 1 NULL ?

ROLLBACK;

 DROP VIEW TA ;
 DROP VIEW TB ;
 DROP table staffa;

-- testYts_811 : skipped
-- Firebird doesn't support the GROUP BY in a view... Not much we can
-- do here when we can't create the specified view...
-- TEST:7568 WHERE <search condition> referencing column!

-- TEST:7569 <null predicate> with concatenation in <row value
-- constructor>!
 CREATE TABLE TX (TX1 INTEGER, TX2 CHARACTER(5), TX3 CHARACTER VARYING (10)); 
 INSERT INTO TX (TX1, TX3) VALUES (1, 'Susan');
 INSERT INTO TX (TX1, TX2) VALUES (2, 'lemon');
 INSERT INTO TX VALUES (3, 'apple', '');
 INSERT INTO TX VALUES (4, 'melon', 'Peter');
 INSERT INTO TX VALUES (5, 'peach', 'Matthew');
 SELECT COUNT (*) FROM TX  WHERE TX2 || TX3 IS NOT NULL; 
-- PASS:7569 If COUNT = 3?

 SELECT TX1 FROM TX WHERE TX3 || TX2 IS NULL; 
-- PASS:7569 If 2 rows returned in any order?
-- PASS:7569 If TX1 = 1?
-- PASS:7569 If TX1 = 2?

ROLLBACK;

DROP TABLE TX;

DROP DATABASE;
