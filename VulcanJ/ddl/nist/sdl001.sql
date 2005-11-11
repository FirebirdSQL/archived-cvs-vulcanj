SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- References to schemas in the SDL series have been removed, since
-- Firebird doesn't support them. However, this kind of defeats the 
-- purpose of these tests. 6/16/2005.

 CREATE TABLE WHICH_SCHEMA1 (C1 CHAR (50));
 INSERT INTO WHICH_SCHEMA1 VALUES ('Use of SCHEMA1.STD is required to pass this test.');
 SELECT C1 FROM WHICH_SCHEMA1;
 
-- PASS:0137 If 1 row selected ?
-- PASS:0137 If C1 starts with 'Use of SCHEMA1.STD is required' ?
-- PASS:0137 If C1 ends with ' to pass this test.' ?


DROP DATABASE;
