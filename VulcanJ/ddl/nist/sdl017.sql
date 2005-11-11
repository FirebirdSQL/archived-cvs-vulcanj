SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0153 CREATE VIEW Joining 3 tables!
 CREATE VIEW STAFF_WORKS_DESIGN (NAME,COST,PROJECT) AS SELECT EMPNAME,HOURS*2*GRADE,PNAME FROM   PROJ,STAFF,WORKS WHERE  STAFF.EMPNUM=WORKS.EMPNUM AND WORKS.PNUM=PROJ.PNUM AND PTYPE='Design';

 SELECT COUNT(*),SUM(COST) FROM STAFF_WORKS_DESIGN; 
 --PASS:0153 If count = 5 and SUM(COST) = 3488?

DROP DATABASE;
