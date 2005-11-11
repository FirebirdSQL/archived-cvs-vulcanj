SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;

 INSERT INTO WORKS(PNUM,EMPNUM,HOURS) VALUES ('P22','E22',NULL);
 SELECT EMPNUM,PNUM FROM WORKS WHERE HOURS IS NULL;
-- PASS:0022 If EMPNUM = 'E22'?

-- TEST:0023 DEC precision >= col.def.: ERROR if left-truncate!
 CREATE TABLE TEMP_S (EMPNUM  CHAR(3), GRADE DECIMAL(4), CITY CHAR(15));
 INSERT INTO TEMP_S(EMPNUM,GRADE,CITY) VALUES('E23',2323.4,'China');
-- PASS:0023 If 1 row inserted or ?
-- PASS:0023 insert fails due to precision of 23234?
 SELECT COUNT(*) FROM TEMP_S;
 INSERT INTO TEMP_S VALUES('E23',23234,'China');
 SELECT COUNT(*) FROM TEMP_S;
 ROLLBACK;
 
 INSERT INTO TEMP_S SELECT EMPNUM,GRADE,CITY FROM STAFF WHERE GRADE > 13;

-- TEST:0024 INSERT:<query spec.> is empty: SQLCODE = 100!
 DELETE FROM TEMP_S;
 INSERT INTO TEMP_S(EMPNUM,GRADE,CITY) SELECT EMPNUM,GRADE,CITY FROM STAFF WHERE GRADE > 12;
 SELECT COUNT(*) FROM TEMP_S;
 
-- TEST:0026 INSERT into view with check option and unique violation!
 SELECT COUNT(*) FROM STAFF;
 INSERT INTO TEMP_SS SELECT EMPNUM,GRADE,CITY FROM STAFF3 WHERE GRADE = 10;
	-- PASS:0026 If ERROR, view check constraint, 0 rows inserted OR ?
	-- PASS:0026 If ERROR, unique constraint, 0 rows inserted?

 SELECT COUNT(*) FROM STAFF;

DROP DATABASE;