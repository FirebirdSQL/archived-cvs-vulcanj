SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0151 CREATE VIEW!
 CREATE VIEW STAFFV1 AS SELECT * FROM STAFF WHERE  GRADE >= 12;

 SELECT COUNT(*)FROM STAFFV1;
-- PASS:0151 If count = 4?

DROP DATABASE;
