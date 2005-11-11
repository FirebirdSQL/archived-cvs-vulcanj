SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- Notes: TEST:0835 <character substring function> (static)!
 CREATE TABLE MOREGRUB (C1 VARCHAR (10), ID INT); 
 CREATE VIEW X4 (S1, S2, ID) AS SELECT SUBSTRING (C1 FROM 6), SUBSTRING (C1 FROM 2 FOR 4), ID FROM MOREGRUB;
-- PASS:0835 If 1 row selected and value is 'on '?

 COMMIT;

 INPUT ddl/input/base-tab.sql;
 COMMIT;

 SELECT SUBSTRING (CITY FROM 4 FOR 10) FROM STAFF WHERE EMPNAME = 'Ed';
-- PASS:0835 If 1 row selected and value is 'on '?

 SELECT SUBSTRING (CITY FROM 4 FOR -1) FROM STAFF WHERE EMPNAME = 'Ed';
-- PASS:0835 If ERROR, substring error, 0 rows selected?

 INSERT INTO MOREGRUB VALUES ('Pretzels', 1);
 INSERT INTO MOREGRUB VALUES (NULL, 2);
 INSERT INTO MOREGRUB VALUES ('Chips', 3);
 SELECT S1 FROM X4 WHERE ID = 1;
-- PASS:0835 If 1 row selected and S1 = 'els'?

 SELECT S1 FROM X4 WHERE ID = 3;
-- PASS:0835 If 1 row selected and S1 = ''?

 SELECT S2 FROM X4 WHERE ID = 1; 
-- PASS:0835 If 1 row selected and S2 = 'retz'?

 SELECT S2 FROM X4 WHERE ID = 3;
-- PASS:0835 If 1 row selected and S2 = 'hips'?

 SELECT S1 FROM X4 WHERE ID = 2;
-- PASS:0835 If 1 row selected and S1 is NULL?

DROP DATABASE;
