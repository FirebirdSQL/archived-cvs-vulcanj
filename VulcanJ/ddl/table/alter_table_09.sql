CREATE DATABASE 'test.fdb' ;
CREATE TABLE test( id INTEGER NOT NULL,
                   text VARCHAR(32));
INSERT INTO test(id,text) VALUES(0,'text 1');
COMMIT;

ALTER TABLE test DROP text;
SELECT * FROM test;

DROP DATABASE;

