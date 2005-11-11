SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0150 CREATE Table with Unique(...), INSERT Values!

 CREATE TABLE WORKS1(EMPNUM CHAR(3) NOT NULL, PNUM CHAR(3) NOT NULL, HOURS DECIMAL(5), UNIQUE(EMPNUM,PNUM));

 INSERT INTO WORKS1 VALUES('E1','P2',20);
 INSERT INTO WORKS1 VALUES('E1','P3',40);
 SELECT COUNT(*)FROM  WORKS1;
-- PASS:0150 If count = 2?

 INSERT INTO WORKS1 VALUES('E1','P2',80);
-- PASS:0150 If ERROR, unique constraint, 0 rows inserted?
-- NOTE:0150 Duplicates for (EMPNUM, PNUM) are not allows.

 SELECT COUNT(*)FROM   WORKS1;
-- PASS:0150 If count = 2?

DROP DATABASE;
