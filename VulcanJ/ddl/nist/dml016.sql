SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;

 COMMIT;

-- TEST:0064 SELECT USER!
 SELECT USER,PNAME FROM PROJ;
-- PASS:0064 If 6 rows are selected and each USER = 'SYSDBA' ?

-- TEST:0065 SELECT CHAR literal and term with numeric literal!
 SELECT 'USER',PNAME FROM PROJ;
-- PASS:0065 If 6 rows are selected and first column is value 'USER'?

 SELECT PNUM,'BUDGET IN GRAMS IS ',BUDGET * 5 FROM PROJ WHERE PNUM = 'P1';
-- PASS:0065 If values are 'P1', 'BUDGET IN GRAMS IS ', 50000?

-- TEST:0066 SELECT numeric literal!
 SELECT EMPNUM,10 FROM STAFF WHERE GRADE = 10;
-- PASS:0066 If 1 row with values 'E2' and 10?

 SELECT EMPNUM, 10 FROM STAFF;
-- PASS:0066 If 5 rows are selected with second value always = 10?
-- PASS:0066 and EMPNUMs are 'E1', 'E2', 'E3', 'E4', 'E5'?

DROP DATABASE;
