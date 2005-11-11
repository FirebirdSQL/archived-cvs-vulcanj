SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

--  TEST:0155 CREATE Table with Unique (...), INSERT via SELECT!
 SELECT COUNT(*) FROM WORKS;
--  PASS:0155 If count = 12?
 INSERT INTO WORKS SELECT 'E3',PNUM,100 FROM PROJ;
-- PASS:0155 If ERROR, unique constraint, 0 rows inserted?
 SELECT COUNT(*) FROM WORKS;
--  PASS:0155 If count = 12?

DROP DATABASE;
