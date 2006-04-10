-- S0287716
-- FIREBIRD STRANGE ALTER TABLE BEHAVIOR 
SET NAMES ASCII;

CREATE DATABASE 'test.fdb' ;

RECREATE TABLE foo ( i INTEGER );
INSERT INTO foo VALUES (2000000000);

-- Now try to alter the column to be a varchar
-- Should err, VARCHAR(5) can't hold '2000000000'
ALTER TABLE foo ALTER i TYPE VARCHAR(5);

-- Should err, VARCHAR(5) can't hold '2000000000'
ALTER TABLE foo ALTER i TYPE VARCHAR(9);

-- show have column I as integer
show table foo;

drop database;
