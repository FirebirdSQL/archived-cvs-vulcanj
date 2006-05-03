SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/sun-tab3.sql;
INPUT ddl/input/sun-tab3-refresh.sql;
commit;


-- TEST:0378 (ref. acr. sch.) delete P.K and corr. F.K e.!
 DELETE FROM STAFF_P WHERE EMPNUM='E1';
-- PASS:0378 If RI ERROR, children exist, 0 rows deleted?

 SELECT EMPNUM FROM STAFF_P WHERE EMPNUM = 'E1'; 
-- PASS:0378 If 1 row selected and EMPNUM = E1?

 ROLLBACK WORK;


-- TEST:0379 (ref. acr. sch.) update P.K and corr. F.K e.!
 UPDATE STAFF_P SET EMPNUM = 'E9' WHERE EMPNUM = 'E2';
-- PASS:0379 If RI ERROR, children exist, 0 rows updated?

 SELECT COUNT(*) FROM STAFF_P WHERE EMPNUM = 'E2';
-- PASS:0379 If count = 1?

 ROLLBACK WORK;

DROP DATABASE;
