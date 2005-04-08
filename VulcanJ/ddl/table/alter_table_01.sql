CREATE DATABASE 'test.fdb' ;
CREATE TABLE test( id INTEGER);

/*
Tested command:
*/
ALTER TABLE test ADD text varchar(32);
SHOW TABLE test;

DROP DATABASE;

