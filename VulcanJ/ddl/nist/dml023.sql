SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;

-- TEST:0103 Subquery with comparison predicate!
 SELECT PNUM FROM PROJ WHERE PROJ.CITY = (SELECT STAFF.CITY FROM STAFF WHERE EMPNUM = 'E1');
-- PASS:0103 If 3 rows are selected with PNUMs:'P1','P4','P6?

-- TEST:0104 SQLCODE < 0, subquery with more than 1 value!
 SELECT PNUM FROM PROJ WHERE PROJ.CITY = (SELECT STAFF.CITY FROM STAFF WHERE EMPNUM > 'E1');
-- PASS:0104 If ERROR, SELECT returns more than 1 row in subquery?
-- PASS:0104 If 0 rows are selected?

-- TEST:0105 Subquery in comparison predicate is empty!
 SELECT COUNT(*) FROM STAFF WHERE STAFF.CITY = (SELECT PROJ.CITY FROM PROJ WHERE PNUM > 'P7');
-- PASS:0105 If count = 0?
 SELECT COUNT(*) FROM STAFF WHERE NOT (STAFF.CITY = (SELECT PROJ.CITY FROM PROJ WHERE PNUM > 'P7'));
 -- PASS:0105 If count = 0?

-- TEST:0106 Comparison predicate <> !
 SELECT PNUM FROM PROJ WHERE CITY <> 'Deale';
-- PASS:0106 If 3 rows are selected with PNUMs:'P2','P3','P5'?

-- TEST:0107 Comp predicate with short string logically blank padded!
 SELECT COUNT(*) FROM WORKS WHERE EMPNUM = 'E1';
-- PASS:0107 If count = 6 ?
 SELECT COUNT(*) FROM WORKS WHERE EMPNUM = 'E1' AND EMPNUM = 'E1 ';
-- PASS:0107 If count = 6?

DROP DATABASE;
