SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- TEST:0854 Informational: mixing SDL and DML!

 CREATE TABLE TRANSIENT (WINDOW_ID INT);
 INSERT INTO TRANSIENT VALUES (1);
 CREATE VIEW CTRANS (WIN_COUNT) AS SELECT COUNT(*) FROM TRANSIENT;
 INSERT INTO TRANSIENT VALUES (2);

 SELECT * FROM CTRANS;
-- PASS:0854 If WIN_COUNT = 2?


 SELECT * FROM TRANSIENT ORDER BY WINDOW_ID;
-- PASS:0854 If 2 rows are selected with the following order?
-- PASS:0854 If 1 ?
-- PASS:0854 If 2 ?

DROP DATABASE;
