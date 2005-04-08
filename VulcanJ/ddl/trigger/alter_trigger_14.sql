CREATE DATABASE 'test.fdb' ;
CREATE TABLE test( id INTEGER NOT NULL CONSTRAINT unq UNIQUE,
                   text VARCHAR(32));
SET TERM ^;
CREATE TRIGGER tg FOR test BEFORE INSERT POSITION 1
AS
BEGIN
  new.text=new.text||'tg1 ';
END ^

ALTER TRIGGER tg AS
BEGIN
  new.text='altered trigger';
END ^

SET TERM ;^
SHOW TRIGGER tg;

DROP DATABASE;

