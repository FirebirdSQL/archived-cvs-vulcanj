SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- TEST:0832 FIPS Flagger - CREATE INDEX!
-- FIPS Flagger Test.  Support for this feature is not required.
-- If supported, this feature must be flagged as an extension to the standard.

 CREATE TABLE USIG (C1 INT, C_1 INT);

 CREATE INDEX II1 ON USIG(C1);
-- PASS:0832 If index created?
-- NOTE:0832 Shows support for CREATE INDEX extension

 CREATE UNIQUE INDEX II2 ON USIG(C_1);
-- PASS:0832 If index created?
-- NOTE:0832 Shows support for CREATE INDEX extension

 SHOW TABLE USIG;
 
 SHOW INDEX;
 
DROP DATABASE;
