-- combined all of the AVG() aggregate function tests in 1 file.
-- and added comments!

set names ASCII;

CREATE DATABASE 'test.fdb' ;

-- test case #1;
CREATE TABLE test( id INTEGER NOT NULL CONSTRAINT unq UNIQUE,
                   text VARCHAR(32));
INSERT INTO test VALUES(5,null);

-- should return 5;
SELECT AVG(id) FROM test;

rollback;
drop table test;

-- test case #2;
CREATE TABLE test( id INTEGER NOT NULL);
INSERT INTO test VALUES(5);
INSERT INTO test VALUES(6);

-- should return 5;
SELECT AVG(id) FROM test;

rollback;
drop table test;


-- test case #3;
CREATE TABLE test( id INTEGER NOT NULL);
INSERT INTO test VALUES(5);
INSERT INTO test VALUES(5);
INSERT INTO test VALUES(6);

-- should return 5;
SELECT AVG(id) FROM test;

rollback;
drop table test;


-- test case #4;
CREATE TABLE test( id INTEGER NOT NULL);
INSERT INTO test VALUES(5);
INSERT INTO test VALUES(6);
INSERT INTO test VALUES(6);

-- should return 5;
SELECT AVG(id) FROM test;
rollback;
drop table test;


-- test case #5;
CREATE TABLE test( id INTEGER NOT NULL);
INSERT INTO test VALUES(2100000000);
INSERT INTO test VALUES(2100000000);
INSERT INTO test VALUES(2100000000);
INSERT INTO test VALUES(2100000000);

-- integer overflow condition;
SELECT AVG(2100000000*id) FROM test;
rollback;
drop table test;


-- test case #6;
CREATE TABLE test( id INTEGER);
INSERT INTO test VALUES(12);
INSERT INTO test VALUES(13);
INSERT INTO test VALUES(14);
INSERT INTO test VALUES(NULL);

-- should return 13;
SELECT AVG(id) FROM test;
rollback;
drop table test;


-- test case #7;
CREATE TABLE test( id INTEGER);
INSERT INTO test VALUES(NULL);

-- should return NULL;
SELECT AVG(id) FROM test;
rollback;
drop table test;

-- test case #8;
CREATE TABLE test( id DOUBLE PRECISION NOT NULL);
INSERT INTO test VALUES(5.123456789);

-- should return 5.123456789000000;
SELECT AVG(id) FROM test;
rollback;
drop table test;

DROP DATABASE;

