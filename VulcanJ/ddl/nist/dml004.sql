SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;

 COMMIT;

-- TEST:0008 SQLCODE 100:SELECT on empty table !
 SELECT EMPNUM,HOURS FROM WORKS WHERE PNUM = 'P8' ORDER BY EMPNUM DESC;
-- PASS:0008 If 0 rows selected, SQLCODE = 100, end of data?

 INSERT INTO WORKS VALUES('E9','P9',NULL);
-- PASS:0009 If 1 row is inserted?

 SELECT EMPNUM FROM WORKS WHERE HOURS IS NULL;
-- PASS:0009 If EMPNUM = 'E9'?

 SELECT EMPNUM, HOURS FROM WORKS WHERE PNUM = 'P9' ORDER BY EMPNUM DESC;
-- PASS:0009 If EMPNUM = 'E9' and HOURS is NULL?

DROP DATABASE;