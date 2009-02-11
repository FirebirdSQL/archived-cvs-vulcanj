SET NAMES ASCII; 
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- code coverage check
create table dptest (d double precision);
insert into dptest values ('1.3e+07');
insert into dptest values ('1. 3e + 0 7');

-- should be 2 values
select * from dptest;

-- consider cases where spaces can be in the numeric constant
recreate table testy (i integer, bi bigint) ;
insert into testy (bi) values ('0xDEADBEEF');

-- preceding spaces will be ignored
insert into testy (bi) values ('   0xDEADBEEF');

-- max length is 16 hex digits
insert into testy (bi) values ('0xDEADBEEFDEADBEEF');

-- invalid case, too long
insert into testy (bi) values ('0xDEADBEEFDEADBEEFB');

-- should get 3 rows
select * from testy;

drop database;
