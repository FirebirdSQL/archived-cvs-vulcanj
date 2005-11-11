SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0145 Fully Qualified Column Spec.!
 SELECT STAFF.EMPNUM,EMPNAME,HOURS,USER FROM STAFF,WORKS WHERE STAFF.EMPNUM='E1' AND PNUM='P3';
-- Changed the expected value of user from SULLIVAN to SYSDBA - JiMcK
-- PASS:0145 If STAFF.EMPNAME = 'Alice', USER = 'SULLIVAN', HOURS =80?


DROP DATABASE;
