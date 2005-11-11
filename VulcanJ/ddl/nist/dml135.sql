SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- TEST:0692 Many TSQL features #3: enhanced proj/works!

 CREATE TABLE "Proj" ( PNUM     CHAR(3) NOT NULL PRIMARY KEY, PNAME    CHAR(20), PTYPE    CHAR(6)    DEFAULT 'Code', BUDGET   DECIMAL(9) DEFAULT 10000, CITY     CHAR(15)   DEFAULT 'Berlin');
-- PASS:0692 If table is created?

 CREATE VIEW "PTypes" ("TYPE", NUM) AS  SELECT PTYPE, COUNT(*) FROM "Proj"  GROUP BY PTYPE;
-- PASS:0692 If view is created?

 CREATE VIEW PTYPES AS SELECT * FROM "PTypes" WHERE NUM > 1;
-- PASS:0692 If view is created?

 CREATE TABLE "Works" ( EMPNUM   CHAR(3) NOT NULL, PNUM     CHAR(3) NOT NULL REFERENCES "Proj" ON DELETE CASCADE, HOURS    DECIMAL(5), PRIMARY KEY (EMPNUM,PNUM)); 
-- PASS:0692 If table is created?

 CREATE VIEW "PStaff" (PNUM, NUM) AS SELECT PNUM, COUNT(*) FROM "Works" WHERE HOURS >= 20 GROUP BY PNUM; 
-- PASS:0692 If view is created?

INPUT ddl/input/base-tab.sql;
COMMIT;

 INSERT INTO "Proj" SELECT * FROM Proj; 
 INSERT INTO "Proj" (PNUM, PNAME, BUDGET) VALUES ('P7', 'FROB', 10000);
 INSERT INTO "Proj" (PNUM, PNAME, BUDGET) VALUES ('P8', 'BORF', 15000); 
 INSERT INTO "Proj" (PNUM, PNAME, PTYPE) VALUES ('P9', 'FORB', 'Code'); 
 INSERT INTO "Proj" VALUES ('P10', 'ROBF', 'Docs', 1000, 'Sofia');
 INSERT INTO "Works" SELECT * FROM Works;

 SELECT * FROM PTYPES ORDER BY NUM;
-- PASS:0692 If 3 rows selected with ordered rows and column values ?
-- PASS:0692 Test 2 ?
-- PASS:0692 Design 3 ?
-- PASS:0692 Code 4 ?

 SELECT NUM, COUNT(*) FROM "PStaff" GROUP BY NUM ORDER BY NUM;
-- PASS:0692 If 3 rows selected with ordered rows and column values ?
-- PASS:0692 1 2 ?
-- PASS:0692 2 2 ?
-- PASS:0692 4 1 ?

 DELETE FROM "Proj" WHERE PTYPE = 'Design';
-- PASS:0692 If 3 rows from "Proj" and 5 rows from "Works" are deleted?

 SELECT * FROM PTYPES ORDER BY NUM;
-- PASS:0692 If 2 rows selected with ordered rows and column values ?
-- PASS:0692 Test 2 ?
-- PASS:0692 Code 4 ?

 SELECT NUM, COUNT(*) FROM "PStaff" GROUP BY NUM ORDER BY NUM; 
-- PASS:0692 If 2 rows selected with ordered rows and column values ?
-- PASS:0692 1 2 ?
-- PASS:0692 4 1 ?

DROP DATABASE;
