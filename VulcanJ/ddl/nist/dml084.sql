SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT WORK;

-- note table upupup does not exist.

-- TEST:0503 SQLSTATE 42000: syntax error or access rule vio.1!

 SELECT COL2 FROM UPUNIQ WHERE NUMKEY = 1;
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows selected OR SQLSTATE = 42000 OR SQLCODE < 0?

 UPDATE UPUNIQ SET COL2 = 'xx';
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows updated OR SQLSTATE = 42000 OR SQLCODE < 0?

 DELETE FROM UPUNIQ;
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows deleted OR SQLSTATE = 42000 OR SQLCODE < 0?

 INSERT INTO UPUNIQ VALUES (9,'M');
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows inserted OR SQLSTATE = 42000 OR SQLCODE < 0?

 SELECT COUNT(*) FROM STAFF WHERE GRADE < (SELECT MAX(HOURS) FROM WORKS) OR    GRADE > (SELECT MAX(NUMKEY) FROM UPUNIQ) OR    GRADE + 100 > (SELECT MIN(HOURS) FROM WORKS);
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows selected OR SQLSTATE = 42000 OR SQLCODE < 0?

 INSERT INTO UPUNIQ VALUES (13,44);
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows inserted OR SQLSTATE = 42000 OR SQLCODE < 0?

 INSERT INTO UPUNIQ VALUES (555666777);
-- PASS:0503 If ERROR, syntax error or access rule violation?
-- PASS:0503 0 rows inserted OR SQLSTATE = 42000 OR SQLCODE < 0?

ROLLBACK WORK;

 SELECT COL2 FROM UPUNIQ WHERE NUMKEY = 1;
 SELECT COL2 FROM UPUPUP WHERE NUMKEY = 1;
--  PASS:0504 If the SQLSTATE value is the same as?
--  PASS:0504 the SQLSTATE value of the previous SELECT?

 UPDATE UPUNIQ SET COL2 = 'xx';
 UPDATE UPUPUP SET COL2 = 'xx';
--  PASS:0504 If the SQLSTATE value is the same as?
--  PASS:0504 the SQLSTATE value of the previous UPDATE?

 DELETE FROM UPUNIQ;
 DELETE FROM UPUPUP;
--  PASS:0504 If the SQLSTATE value is the same as?
--  PASS:0504 the SQLSTATE value of the previous DELETE?

 INSERT INTO UPUNIQ VALUES (9,'M');
 INSERT INTO UPUPUP VALUES (9,'M');
--  PASS:0504 If the SQLSTATE value is the same as?
--  PASS:0504 the SQLSTATE value of the previous INSERT?

 SELECT COUNT(*) FROM STAFF WHERE GRADE < (SELECT MAX(HOURS) FROM WORKS) OR    GRADE > (SELECT MAX(NUMKEY) FROM UPUNIQ) OR    GRADE + 100 > (SELECT MIN(HOURS) FROM WORKS);
-- value should be 5

 SELECT COUNT(*) FROM STAFF WHERE GRADE < (SELECT MAX(HOURS) FROM WORKS) OR    GRADE > (SELECT MAX(NUMKEY) FROM UPUPUP) OR    GRADE + 100 > (SELECT MIN(HOURS) FROM WORKS);
--  PASS:0504 If the SQLSTATE value is the same as?
--  PASS:0504 the SQLSTATE value of the previous SELECT?

DROP DATABASE;
