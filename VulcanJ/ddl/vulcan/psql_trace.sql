-- S0330798
-- ERROR IN STORED PROC DOES NOT RETURN CORRECT ERROR MESSAGE
-- Actually implement PSQL Stack trace in Vulcan
set names ascii;
create database 'test.fdb';

CREATE TABLE ERR (ID INT NOT NULL PRIMARY KEY, NAME VARCHAR(16));               

CREATE EXCEPTION EX '!';                                                        

set term ^;
CREATE PROCEDURE ERR_1 AS                                                       
BEGIN                                                                           
  EXCEPTION EX 'ID = 3';                                                        
END
^

CREATE TRIGGER ERR_BI FOR ERR BEFORE INSERT AS
BEGIN
  IF (NEW.ID = 2)                                                   
  THEN EXCEPTION EX 'ID = 2';
  IF (NEW.ID = 3)
  THEN EXECUTE PROCEDURE ERR_1;
  IF (NEW.ID = 4)
  THEN NEW.ID = 1 / 0;
END
^

CREATE  PROCEDURE ERR_2 AS
BEGIN                                                                           
  INSERT INTO ERR VALUES (3, '333');                                            
END
^
set term ;^

-- 1. User exception from trigger: 
INSERT INTO ERR VALUES (2, '2');

-- 2. User exception from procedure called by trigger:
INSERT INTO ERR VALUES (3, '3');

-- 3. Division by zero occurred in trigger:  
INSERT INTO ERR VALUES (4, '4');

-- 4. User exception from procedure:
EXECUTE PROCEDURE ERR_1; 

-- 5. User exception from procedure with more deep call stack: 
EXECUTE PROCEDURE ERR_2;
drop database;
