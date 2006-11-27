-- S0367441
-- DROP TABLE PROBLEMS

create database 'test.fdb';

   CREATE TABLE UNDROPPABLE (
  C1 INT NOT NULL PRIMARY KEY );


   CREATE TABLE DROPPABLE (
  C1 INT NOT NULL PRIMARY KEY REFERENCES UNDROPPABLE,
  C2 INT, CHECK (C1 < C2));

-- we should be able to drop table DROPPABLE.
drop table droppable;

drop database;

