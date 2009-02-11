SET NAMES ASCII; 
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

create table HEXTEST_HW (x varchar(20) character set octets) ;
create table HEXTEST1 (c1 char (10) character set octets, c2 varchar(10) character set octets); 
create table HEXTEST2 (b bigint, i integer ) ; 

commit;

-- hello world test
insert into HEXTEST_HW (x) values (x'48454c4c4f20574f524c44');
-- this is binary
select * from HEXTEST_HW;
-- now with cast as ascii
select cast (x as char(20) character set ascii) from hextest_hw ;

rollback;

-- test for null values
insert into HEXTEST1 values (null, null); 
select * from HEXTEST1;
rollback;

-- empty strings, not nulls
insert into HEXTEST1 values (x'', x''); 
select * from HEXTEST1;
rollback;

-- try some valid literals
insert into HEXTEST1 (c1, c2) values (x'F3', x'F3');
select c1 from HEXTEST1;
rollback;

insert into HEXTEST2 values (0xaF3, 0xaF3);
select * from HEXTEST2;
rollback;

-- another valid case
insert into HEXTEST1 (c1, c2) values (x'03', x'03');
select c1 from HEXTEST1;
rollback;

-- another valid case, x'7af3'
insert into HEXTEST1 (c1, c2) values (x'7af3', x'7af3');
select c1 from HEXTEST1;
rollback;

-- another valid case
insert into HEXTEST1 values (x'aA7bB3c6C2DdE4e10fF8', x'aA7bB3c6C2DdE4e10fF8');
select c1 from HEXTEST1;
rollback;

-- invalid test case, truncation
insert into HEXTEST1 values (x'0aA7bB3c6C2DdE4e10fF80', x'0aA7bB3c6C2DdE4e10fF80');
select * from HEXTEST1;
rollback;

--
-- now some int/bigint tests
--

-- valid test case
insert into HEXTEST2 (b, i) values (0x7af3, 0x7af3);
select * from HEXTEST2;
rollback;

-- valid test case
insert into HEXTEST2 (b, i) values (0x3, 0x3);
select * from HEXTEST2;
rollback;

-- max bigint
insert into HEXTEST2 (b) values (9223372036854775807);
insert into HEXTEST2 (b) values (0x7FFFFFFFFFFFFFFF);

-- values should match
select b from HEXTEST2;
rollback;

-- min bigint
insert into HEXTEST2 (b) values (-9223372036854775808);
insert into HEXTEST2 (b) values (0x8000000000000000);

-- values should match
select b from HEXTEST2;
rollback;


-- some error cases
-- value > max BIGINT
insert into HEXTEST2 (b) values (9223372036854775808);
-- value < min BIGINT
insert into HEXTEST2 (b) values (-9223372036854775809);
select * from hextest2;
rollback;

-- max int
-- max signed integer
insert into HEXTEST2 (b,i) values (2147483647, 2147483647);
-- max unsigned integer
insert into HEXTEST2 (b,i) values (4294967295, 4294967295);
insert into HEXTEST2 (b,i) values (0xFFFFFFFF, 0xFFFFFFFF);
select * from hextest2;
rollback;

-- min int
insert into HEXTEST2 (b,i) values (0x80000000, 0x80000000);
select * from hextest2;
rollback;


--
-- test cases suggested by adriano
--

recreate table testy (i integer, bi bigint) ; 
insert into testy (i) values ('01') ;

-- insert should pass, just like previous insert
insert into testy (i) values ('0x0101') ;

-- insert should pass
insert into testy (i) values ('0xDEADBEEF') ;

-- insert should pass
insert into testy (i) values ('0xdeadbeef') ;

-- this insert should fail, invalid hex constant
insert into testy (i) values ('0xABCQQ');

-- select should return 4 rows
select * from testy;

-- now some BIGINT tests

insert into testy (bi) values ('0x8000000000000000');
insert into testy (bi) values ('0x7FFFFFFFFFFFFFFF');

-- select should return 2 rows
select * from testy where bi is not null;

drop database;
