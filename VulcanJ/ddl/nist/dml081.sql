SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT WORK;

-- NOTE Direct support for SQLCODE or SQLSTATE is not required
-- NOTE in Interactive Direct SQL, as defined in FIPS 127-2.
-- NOTE ********************* instead ***************************
-- NOTE If a statement raises an exception condition,
-- NOTE then the system shall display a message indicating that
-- NOTE the statement failed, giving a textual description
-- NOTE of the failure.
-- NOTE If a statement raises a completion condition that is a
-- NOTE "warning" or "no data", then the system shall display
-- NOTE a message indicating that the statement completed,
-- NOTE giving a textual description of the "warning" or "no data."

-- TEST:0487 SQLSTATE 00000: successful completion!

 SELECT COUNT(*) FROM WORKS;
-- PASS:0487 If count = 12?
-- PASS:0487 OR SQLSTATE = 00000: successful completion?

-- TEST:0488 SQLSTATE 21000: cardinality violation!
 SELECT COUNT(*) FROM WORKS WHERE PNUM = (SELECT PNUM FROM WORKS WHERE HOURS = 80);
--  PASS:0488 If ERROR, cardinality violation, 0 rows selected?
--  PASS:0488 OR SQLSTATE = 21000 OR SQLCODE < 0?

 SELECT GRADE FROM STAFF WHERE EMPNUM = 'xx';
-- PASS:0489 If 0 rows selected?
-- PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

 DELETE FROM STAFF WHERE GRADE = 11;
-- PASS:0489 If 0 rows deleted?
-- PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

 INSERT INTO STAFF (EMPNUM,GRADE) SELECT EMPNUM, 9 FROM WORKS WHERE PNUM = 'X9';
-- PASS:0489 If 0 rows inserted?
-- PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

 UPDATE STAFF SET CITY = 'Ho' WHERE GRADE = 15;
-- PASS:0489 If 0 rows updated?
-- PASS:0489 OR SQLSTATE = 02000: no data OR SQLCODE = 100?

ROLLBACK WORK;

-- TEST:0490 SQLSTATE 22012: data exception/division by zero!
 INSERT INTO STAFF VALUES ('E6','Fidel',0,'Havana');

 SELECT COUNT(*) FROM STAFF WHERE EMPNAME = 'Fidel' AND 16/GRADE > 2;
--  PASS:0490 If ERROR, data exception/division by zero, 0 rows
--  selected?
--  PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?

 SELECT 16/GRADE FROM STAFF WHERE EMPNAME = 'Fidel';
--  PASS:0490 If ERROR, data exception/division by zero, 0 rows
--  selected?
--  PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?

 SELECT COUNT(*) FROM STAFF GROUP BY CITY HAVING SUM(GRADE/0) > 44;
--  PASS:0490 If ERROR, data exception/division by zero, 0 rows
--  selected?
--  PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?

 SELECT COUNT(*) FROM STAFF WHERE GRADE = (SELECT 16/GRADE FROM STAFF WHERE EMPNUM = 'E6');
--  PASS:0490 If ERROR, data exception/division by zero, 0 rows
--  selected?
--  PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?

 UPDATE STAFF SET GRADE = GRADE/0 WHERE GRADE = 12;
--  PASS:0490 If ERROR, data exception/division by zero, 0 rows
--  updated?
--  PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?

 INSERT INTO STAFF SELECT 'X','Y',HOURS/0,'z' FROM WORKS WHERE PNUM = 'P6';
--  PASS:0490 If ERROR, data exception/division by zero, 0 rows
--  inserted?
--  PASS:0490 OR SQLSTATE = 22012 OR SQLCODE < 0?

DROP DATABASE;
