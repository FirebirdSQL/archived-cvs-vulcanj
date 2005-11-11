SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- this implementation of factorial uses recursion

set terminator ^;
CREATE PROCEDURE factorial(number INTEGER, mode INTEGER) RETURNS (result INTEGER)
   AS
     DECLARE VARIABLE temp INTEGER;
   BEGIN
     temp = number - 1;
     IF (NOT temp IS NULL) THEN BEGIN
       IF (temp > 0) THEN
         EXECUTE PROCEDURE factorial(:temp, 0) RETURNING_VALUES :temp;
       ELSE
         temp = 1;
       result = number * temp;
     END
     IF (mode = 1) THEN
       SUSPEND;
   END^
set terminator ;^

select * from factorial (5,1); 

execute procedure factorial (5,1);
   
drop database ;
