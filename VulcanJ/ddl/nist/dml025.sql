SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0114 Set functions without GROUP BY returns 1 row!
 SELECT SUM(HOURS),AVG(HOURS),MIN(HOURS),MAX(HOURS) FROM    WORKS WHERE   EMPNUM='E1';
-- PASS:0114 If SUM(HOURS) = 184 and AVG(HOURS) is 30 to 31?
-- PASS:0114 If MIN(HOURS) = 12 and MAX(HOURS) = 80 ?

-- TEST:0115 GROUP BY col, set function: 0 groups returns empty table!
 SELECT PNUM,AVG(HOURS),MIN(HOURS),MAX(HOURS) FROM    WORKS WHERE   EMPNUM='E8' GROUP BY PNUM;
-- PASS:0115 If 0 rows are selected ?

-- TEST:0116 GROUP BY set functions: zero groups returns empty table!
 SELECT SUM(HOURS),AVG(HOURS),MIN(HOURS),MAX(HOURS) FROM    WORKS WHERE   EMPNUM='E8' GROUP BY PNUM;
-- PASS:0116 If 0 rows are selected?

-- TEST:0117 GROUP BY column, set functions with several groups!
 SELECT PNUM,AVG(HOURS),MIN(HOURS),MAX(HOURS) FROM    WORKS GROUP BY PNUM ORDER BY PNUM;
-- PASS:0117 If 6 rows are selected and first PNUM = 'P1'?
-- PASS:0117 and first MAX(HOURS) = 40?

DROP DATABASE;
