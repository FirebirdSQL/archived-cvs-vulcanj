SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/sun-tab3.sql;
INPUT ddl/input/sun-tab3-refresh.sql;

-- TEST:0380 (ref. acr. sch.) insert F.K and no corr. P.K!
 INSERT INTO WORKS_P VALUES ('E9','P2',20); 
-- PASS:0380 If RI ERROR, parent missing, 0 rows inserted?
 
 SELECT COUNT(*) FROM WORKS_P WHERE EMPNUM = 'E9'; 
-- PASS:0380 If count = 0?

-- TEST:0381 (ref. acr. sch.) update F.K to no P.K corr.!
 UPDATE WORKS_P SET EMPNUM = 'E9' WHERE EMPNUM = 'E2'; 
-- PASS:0381 If RI ERROR, parent missing, 0 rows updated?

 SELECT COUNT(*) FROM WORKS_P WHERE EMPNUM = 'E2'; 
-- PASS:0381 If count = 2?

DROP DATABASE;
