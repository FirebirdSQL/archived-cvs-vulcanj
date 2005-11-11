SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0397 Grouped view!

 CREATE VIEW SET_TEST (EMP1, EMP_AVG, EMP_MAX) AS SELECT STAFF.EMPNUM, AVG(HOURS), MAX(HOURS) FROM STAFF, WORKS GROUP BY STAFF.EMPNUM; 
 CREATE VIEW DUP_COL (EMP1, PNO, HOURS, HOURS_2) AS SELECT EMPNUM, PNUM, HOURS, HOURS * 2 FROM WORKS;
 SELECT EMP1, EMP_AVG, EMP_MAX  FROM SET_TEST ORDER BY EMP1; 
-- PASS:0397 If for the first row EMP1 = 'E1',?
-- PASS:0397 EMP_AVG is between 38 and 39, and EMP_MAX = 80?

 SELECT EMP1, HOURS, HOURS_2 FROM DUP_COL WHERE EMP1 = 'E3';
-- PASS:0420 If EMP1 = 'E3', HOURS = 20 and HOURS_2 = 40?

DROP DATABASE;
