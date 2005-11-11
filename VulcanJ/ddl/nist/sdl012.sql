SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0148 CREATE Table with NOT NULL!

 INSERT INTO STAFF1(EMPNAME,GRADE,CITY) VALUES('Carmen',40,'Boston'); 
-- PASS:0148 If ERROR, NOT NULL constraint, 0 rows inserted?
-- NOTE:0148 Not Null Column EMPNUM is missing.

 SELECT COUNT(*) FROM STAFF1;
-- PASS:0148 If count = 0?


DROP DATABASE;
