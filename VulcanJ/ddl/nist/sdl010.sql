SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

--  TEST:0146 GRANT SELECT, INSERT, DELETE!

 INSERT INTO STAFF4 SELECT * FROM STAFF;
 
 SELECT COUNT(*) FROM STAFF4 ;
--  PASS:0146 If 5 rows are inserted?

 SELECT EMPNUM,EMPNAME,USER FROM STAFF4 WHERE EMPNUM = 'E3';
--  Changed the expected value of user from SULLIVAN to SYSDBA - JiMcK
--  PASS:0146 If EMPNAME = 'Carmen' and USER = 'SULLIVAN'?

 INSERT INTO STAFF4 VALUES('E6','Ling',11,'Xi an');

 SELECT EMPNUM,EMPNAME FROM STAFF4 WHERE EMPNUM = 'E6';
--  PASS:0146 If EMPNAME = 'Ling'?

 DELETE FROM STAFF4;
 
 SELECT COUNT(*) FROM STAFF4 ;
-- PASS:0146 If count = 0?


DROP DATABASE;
