-- s0383802
-- FIREBIRD NEEDS BETTER MESSAGES FOR UNSUPPORTED FUNCTIONS
SET NAMES ASCII; 
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

create table func_test (d double precision);
insert into func_test values (1);
insert into func_test values (2);
insert into func_test values (3);

select avg(d) from func_test;

-- this will fail

select std(d) from func_test;

drop database;
