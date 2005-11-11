SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0203 CREATE VIEW On a VIEW !

CREATE VIEW STAFFV2 AS 
   SELECT * FROM STAFF WHERE  GRADE >= 12 WITH CHECK OPTION;
   
CREATE VIEW STAFFV2_VIEW AS 
   SELECT * FROM   STAFFV2 WHERE  CITY = 'Vienna';
   
 SELECT COUNT(*) FROM STAFFV2_VIEW;
-- PASS:0203 If count = 1?

 SELECT EMPNUM, GRADE FROM STAFFV2_VIEW WHERE EMPNUM = 'E3'; 
-- PASS:0203 If EMPNUM = 'E3' and GRADE = 13 ?

 INSERT INTO STAFFV2_VIEW VALUES('E7','Gallagher',17,'Vienna'); 
-- PASS:0203 If 1 row is inserted?

 SELECT COUNT(*) FROM STAFFV2_VIEW; 
-- PASS:0203 If count = 2 ?

DROP DATABASE;
