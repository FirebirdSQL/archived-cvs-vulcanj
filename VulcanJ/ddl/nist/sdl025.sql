SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0204 Updatable VIEW with compound conditions in CHECK!
 CREATE VIEW DOMAIN_VIEW AS SELECT * FROM WORKS WHERE  EMPNUM = 'E1' AND HOURS = 80 OR EMPNUM = 'E2' AND HOURS = 40 OR EMPNUM = 'E4' AND HOURS = 20 WITH CHECK OPTION;

COMMIT;

 SELECT EMPNUM, HOURS FROM DOMAIN_VIEW WHERE PNUM = 'P3';
-- PASS:0204 If EMPNUM = 'E1' and HOURS = 80?

 INSERT INTO DOMAIN_VIEW VALUES('E1', 'P7', 80);
-- PASS:0204 If 1 row is inserted?

 SELECT COUNT(*) FROM DOMAIN_VIEW;
-- PASS:0204 If count = 4?

 INSERT INTO DOMAIN_VIEW VALUES('E2', 'P4', 80);
-- PASS:0204 If 0 rows are inserted - Violation of check option?

 INSERT INTO DOMAIN_VIEW VALUES('E5', 'P5', 20);
-- PASS:0204 If 0 rows are inserted - Violation of check option?

 SELECT COUNT(*) FROM DOMAIN_VIEW;
-- PASS:0204 If count = 4

 SELECT COUNT(*) FROM WORKS;
-- PASS:0204 If count = 13

 COMMIT;

 DROP view DOMAIN_VIEW;

DROP DATABASE;
