SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0156 Tables(Multi-sets), duplicate rows allowed!
 CREATE TABLE TEMP_S (EMPNUM  CHAR(3), GRADE DECIMAL(4), CITY CHAR(15));

 INSERT INTO TEMP_S SELECT EMPNUM,GRADE,CITY FROM STAFF;
-- PASS:0156 If 5 rows are inserted?

 SELECT COUNT(*) FROM TEMP_S;
-- PASS:0156 If count = 5?

 INSERT INTO TEMP_S SELECT EMPNUM,GRADE,CITY FROM STAFF;
-- PASS:0156 If 5 rows are inserted?

 SELECT COUNT(*) FROM TEMP_S;
-- PASS:0156 If count = 10?

 SELECT COUNT(DISTINCT EMPNUM) FROM TEMP_S;
-- PASS:0156 If count = 5?

DROP DATABASE;
