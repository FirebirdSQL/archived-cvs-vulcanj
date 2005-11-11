SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

 CREATE VIEW STAFFV2 AS SELECT * FROM STAFF WHERE GRADE >= 12 WITH CHECK OPTION;
 INSERT INTO STAFFV2 VALUES('E6','Ling',15,'Xian');
-- PASS:0152 If 1 row is inserted?
 
 SELECT COUNT(*) FROM STAFFV2;
-- PASS:0152 If count = 5?

 INSERT INTO STAFFV2 VALUES('E7','Gallagher',10,'Rockville');
-- PASS:0152 If ERROR, view check constraint, 0 rows inserted?

 SELECT COUNT(*) FROM STAFFV2;
-- PASS:0152 If count = 5?

 SELECT COUNT(*) FROM STAFF;
-- PASS:0152 If count = 6?

DROP DATABASE;
