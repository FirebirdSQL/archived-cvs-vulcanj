-- S0328089
-- CREATING A FIREBIRD TABLE WITH ROW WIDTH >64K ABENDS WITH ASSERTION FAILURE

create database 'test.fdb'
   page_size=2048;

-- 64k is too big, will fail
create table foo (c1 varchar(20000), c2 varchar(20000), c3 varchar(20000), c4 varchar(5524) );

-- but 1 bytes less is ok
create table foo (c1 varchar(20000), c2 varchar(20000), c3 varchar(20000), c4 varchar(5523) );

show table foo;

drop database;
