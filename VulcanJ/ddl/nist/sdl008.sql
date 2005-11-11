SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0144 Priv. violation: Column not in UPDATE column list!
 SELECT EMPNUM,EMPNAME,GRADE FROM STAFF3 WHERE EMPNUM = 'E3';
-- PASS:0144 If EMPNAME = 'Carmen' and GRADE = 13 ?

-- UPDATE STAFF3 SET EMPNUM = 'E8',GRADE = 30 WHERE EMPNUM = 'E3';
-- PASS:0144 If ERROR, syntax error/access violation, 0 rows updated?

 UPDATE STAFF3 SET EMPNUM='E8',EMPNAME='Yang' WHERE EMPNUM='E3';
-- PASS:0144 If 1 row is updated?

 SELECT EMPNUM,EMPNAME,GRADE FROM STAFF3 WHERE EMPNUM = 'E8';
-- PASS:0144 If EMPNAME = 'Yang' and GRADE = 13?


DROP DATABASE;
