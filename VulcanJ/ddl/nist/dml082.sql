SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT WORK;

-- TEST:0492 SQLSTATE 22019: data exception/invalid escape char!
 UPDATE STAFF SET CITY = 'Percent%Xunder_' WHERE EMPNUM = 'E1';

 SELECT COUNT(*) FROM STAFF WHERE CITY LIKE '%XX%X_%' ESCAPE 'XX';
-- PASS:0492 If ERROR, data exception/invalid escape char?
-- PASS:0492 0 rows selected OR SQLSTATE = 22019 OR SQLCODE < 0?

 SELECT COUNT(*) FROM STAFF WHERE CITY LIKE '%XX%X_%' ESCAPE 'X';
-- PASS:0492 If count = 1?

 SELECT COUNT(*) FROM STAFF WHERE CITY LIKE '%XX_%' ESCAPE 'XX';
-- PASS:0492 If ERROR, data exception/invalid escape char?
-- PASS:0492 0 rows selected OR SQLSTATE = 22019 OR SQLCODE < 0?

 SELECT COUNT(*) FROM STAFF WHERE CITY LIKE '%XX_%' ESCAPE 'X';
-- PASS:0492 If count = 1?

ROLLBACK WORK;

-- TEST:0493 SQLSTATE 22025: data exception/invalid escape seq.!
 CREATE TABLE CPBASE (KC INT NOT NULL, JUNK1 CHAR (10), PRIMARY KEY (KC));
 CREATE TABLE BASE_WCOV (C1 INT);
 CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE C1 > 0 WITH CHECK OPTION;
 DELETE FROM CPBASE;
 INSERT INTO CPBASE VALUES(82,'Per%X&und_');
 SELECT COUNT(*) FROM CPBASE WHERE JUNK1 LIKE 'P%X%%X' ESCAPE 'X';
-- PASS:0493 If ERROR, data exception/invalid escape seq.?
-- PASS:0493 0 rows selected OR SQLSTATE = 22025 OR SQLCODE < 0?

 SELECT COUNT(*) FROM CPBASE WHERE JUNK1 LIKE 'P%X%%' ESCAPE 'X';
-- PASS:0493 If count = 1?

 INSERT INTO STAFF SELECT 'E12','ff',KC,'gg' FROM CPBASE WHERE JUNK1 LIKE '%X%%Xd_' ESCAPE 'X';
-- PASS:0493 If ERROR, data exception/invalid escape seq.?
-- PASS:0493 0 rows inserted OR SQLSTATE = 22025 OR SQLCODE < 0?

 INSERT INTO STAFF SELECT 'E13','ff',KC,'gg' FROM CPBASE WHERE JUNK1 LIKE '%X%%X_' ESCAPE 'X';
-- PASS:0493 If 1 row is inserted?

 UPDATE CPBASE SET KC = -1 WHERE JUNK1 LIKE '%?X%' ESCAPE '?';
-- PASS:0493 If ERROR, data exception/invalid escape seq.?
-- PASS:0493 0 rows updated OR SQLSTATE = 22025 OR SQLCODE < 0?

 UPDATE CPBASE SET KC = -1 WHERE JUNK1 LIKE '%?%X%' ESCAPE '?';
-- PASS:0493 If 1 row is updated?

 DELETE FROM CPBASE WHERE JUNK1 LIKE '_e%&u%' ESCAPE '&';
-- PASS:0493 If ERROR, data exception/invalid escape seq.?
-- PASS:0493 0 rows deleted OR SQLSTATE = 22025 OR SQLCODE < 0?

 DELETE FROM CPBASE WHERE JUNK1 LIKE '_e%&&u%' ESCAPE '&';
-- PASS:0493 If 1 row is deleted?

ROLLBACK;

DROP VIEW WCOV;
DROP TABLE BASE_WCOV;
DROP TABLE CPBASE;

-- TEST:0494 SQLSTATE 22003: data exception/numeric value out of range!

 CREATE TABLE HH (SMALLTEST  SMALLINT);
 CREATE TABLE CPBASE (KC INT NOT NULL, JUNK1 CHAR (10), PRIMARY KEY (KC));
 CREATE TABLE BASE_WCOV (C1 INT);
 CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE C1 > 0 WITH CHECK OPTION;
 DELETE FROM HH;
 INSERT INTO HH VALUES (10);
-- PASS:0494 If 1 row is inserted?
-- PASS:0494 OR ERROR, data exception/numeric value out of range?
-- PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

 INSERT INTO HH VALUES (100);
-- PASS:0494 If 1 row is inserted?
-- PASS:0494 OR ERROR, data exception/numeric value out of range?
-- PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

 INSERT INTO HH VALUES (1000);
-- PASS:0494 If 1 row is inserted?
-- PASS:0494 OR ERROR, data exception/numeric value out of range?
-- PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

 INSERT INTO HH VALUES (10000);
-- PASS:0494 If 1 row is inserted?
-- PASS:0494 OR ERROR, data exception/numeric value out of range?
-- PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

 INSERT INTO HH VALUES (100000);
-- PASS:0494 If 1 row is inserted?
-- PASS:0494 OR ERROR, data exception/numeric value out of range?
-- PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?

 INSERT INTO HH VALUES (1000000);
-- PASS:0494 If 1 row is inserted?
-- PASS:0494 OR ERROR, data exception/numeric value out of range?
-- PASS:0494 OR 0 rows inserted OR SQLSTATE = 22003 OR SQLCODE < 0?


ROLLBACK;

DROP VIEW WCOV;
DROP TABLE BASE_WCOV;
DROP TABLE CPBASE;
DROP TABLE HH;

-- TEST:0505 SQLSTATE 44000: with check option violation!

 CREATE TABLE HH (SMALLTEST  SMALLINT);
 CREATE TABLE CPBASE (KC INT NOT NULL, JUNK1 CHAR (10), PRIMARY KEY (KC));
 CREATE TABLE BASE_WCOV (C1 INT);
 CREATE VIEW WCOV AS SELECT * FROM BASE_WCOV WHERE C1 > 0 WITH CHECK OPTION;
 INSERT INTO WCOV VALUES (0);
-- PASS:0505 If ERROR, with check option violation?
-- PASS:0505 0 rows inserted OR SQLSTATE = 44000 OR SQLCODE < 0?

 INSERT INTO WCOV VALUES (75);
-- PASS:0505 If 1 row is inserted?

 UPDATE WCOV SET C1 = -C1 WHERE C1 = 75;
-- PASS:0505 If ERROR, with check option violation?
-- PASS:0505 0 rows updated OR SQLSTATE = 44000 OR SQLCODE < 0?

DROP DATABASE;
