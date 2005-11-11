SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;

-- TEST:0180 NULLs sort together in ORDER BY!
 UPDATE STAFF SET GRADE = NULL WHERE EMPNUM = 'E1' OR EMPNUM = 'E3' OR EMPNUM = 'E5';
-- PASS:0180 If 3 rows are updated?
 SELECT EMPNUM,GRADE FROM STAFF ORDER BY GRADE,EMPNUM;
-- PASS:0180 If 5 rows are selected with NULLs together ?
-- PASS:0180 If first EMPNUM is either 'E1' or 'E2'?
-- PASS:0180 If last EMPNUM is either 'E4' or 'E5?

DROP DATABASE;
