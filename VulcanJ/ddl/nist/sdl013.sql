SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0149 CREATE Table with NOT NULL Unique!
 INSERT INTO PROJ1(PNUM,PNAME,BUDGET) VALUES('P10','IRM',10000);
-- PASS:0149 If 1 row is inserted ?

 SELECT COUNT(*) FROM PROJ1;
-- PASS:0149 If count = 1 ?

 INSERT INTO PROJ1(PNUM,PNAME,PTYPE) VALUES('P10','SDP','Test'); 
-- PASS:0149 If ERROR, unique constraint, 0 rows inserted?

 SELECT COUNT(*) FROM PROJ1;
-- PASS:0149 If count = 1?

DROP DATABASE;
