CREATE DATABASE 'test.fdb' ;
CREATE TABLE tb(id INT);

CREATE VIEW test (id,num,text) AS SELECT id,5 FROM tb;
SHOW VIEW test;

DROP DATABASE;

