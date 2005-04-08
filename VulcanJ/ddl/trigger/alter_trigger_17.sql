CREATE DATABASE 'test.fdb' ;
CREATE TABLE test( id INTEGER NOT NULL CONSTRAINT unq UNIQUE,
                   text VARCHAR(32));
SET TERM ^;
CREATE TRIGGER tg FOR test BEFORE DELETE POSITION 1
AS
BEGIN
  old.text='tg';
END ^

ALTER TRIGGER tg AS
BEGIN
  new.text='altered trigger';
END ^

SET TERM ;^

DROP DATABASE;

