SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT WORK;


  CREATE VIEW TEMP_SS(EMPNUM,GRADE,CITY)
     AS SELECT EMPNUM,GRADE,CITY
        FROM   STAFF
        WHERE  GRADE > 12
     WITH CHECK OPTION;

COMMIT;

-- TEST:0443 VIEW with check option rejects unknown!

   INSERT INTO TEMP_SS VALUES('E7',NULL,'Frankfurt'); 
-- PASS:0443 If ERROR, view check constraint, 0 rows inserted? 

   INSERT INTO TEMP_SS VALUES('E8',NULL,'Atlanta'); 
-- PASS:0443 If ERROR, view check constraint, 0 rows inserted?
 
   INSERT INTO TEMP_SS(EMPNUM) VALUES('E9'); 
-- PASS:0443 If ERROR, view check constraint, 0 rows inserted?
 
   UPDATE WORKS 
       SET HOURS = NULL 
       WHERE PNUM = 'P2';
 
   INSERT INTO TEMP_SS 
       SELECT PNUM,HOURS,'Nowhere'  
          FROM WORKS 
          WHERE EMPNUM = 'E1'; 
-- PASS:0443 If ERROR, view check constraint, 0 rows inserted?
 
   UPDATE TEMP_SS 
       SET GRADE = NULL 
       WHERE EMPNUM = 'E3'; 
-- PASS:0443 If ERROR, view check constraint, 0 rows updated? 

   UPDATE TEMP_SS 
       SET GRADE = NULL 
       WHERE EMPNUM = 'E5';
-- PASS:0443 If ERROR, view check constraint, 0 rows updated?
  
   SELECT COUNT(*) 
       FROM STAFF 
       WHERE GRADE IS NULL; 
-- PASS:0443 If count = 0?
 
   SELECT COUNT(*) 
       FROM TEMP_SS; 
-- PASS:0443 If count = 2?
 
   SELECT COUNT(*) 
       FROM STAFF; 
-- PASS:0443 If count = 5?
 
   ROLLBACK WORK; 
DROP DATABASE;
