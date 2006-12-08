SET NAMES ASCII; 
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

create table HEXTEST_HW (x varchar(20) character set octets) ;
create table HEXTEST1 (c1 char (10) character set octets, c2 varchar(10) character set octets); 
create table HEXTEST2 (b bigint, i integer ) ; 

commit;

-- hello world test
insert into HEXTEST_HW (x) values (x'48454c4c4f20574f524c44');
select * from HEXTEST_HW;
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
insert into HEXTEST1 (c1, c2) values (x'aF3', x'aF3');
select c1 from HEXTEST1;
rollback;

insert into HEXTEST2 values (0xaF3, 0xaF3);
select * from HEXTEST2;
rollback;

-- another valid case, x'3'
insert into HEXTEST1 (c1, c2) values (x'3', x'3');
select c1 from HEXTEST1;
rollback;

-- another valid case, x'7af3'
insert into HEXTEST1 (c1, c2) values (x'7af3', x'7af3');
select c1 from HEXTEST1;
rollback;

-- another valid case, x'aA7bB3c6C2DdE4e10fF8'
insert into HEXTEST1 values (x'aA7bB3c6C2DdE4e10fF8', x'aA7bB3c6C2DdE4e10fF8');
select c1 from HEXTEST1;
rollback;

-- invalid test case, truncation
insert into HEXTEST1 values (x'0aA7bB3c6C2DdE4e10fF8', x'0aA7bB3c6C2DdE4e10fF8');
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

recreate table testy (i integer) ; 
insert into testy values ('01') ;

-- insert should pass, just like previous insert
insert into testy values ('0x0101') ;

-- select should return 2 rows
select * from testy;


drop database;
