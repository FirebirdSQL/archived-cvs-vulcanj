CREATE DATABASE 'test.fdb' ;
CREATE TABLE test( id INTEGER NOT NULL,
                   text VARCHAR(32));

ALTER TABLE test DROP text;
SHOW TABLE test;

DROP DATABASE;

