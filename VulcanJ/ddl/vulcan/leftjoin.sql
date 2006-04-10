-- S0287708
-- Defect title: FIREBIRD DRIVER RETURNS INCORRECT ROWS FOR LEFT JOIN

set names ascii;
create database 'test.fdb';

create table table1(c_key int not null primary key, val char(20));
insert into table1 values(0, 'not defined');
insert into table1 values(1, 'value 1');
insert into table1 values(2, 'value 2');

create table table2(pk int not null primary key, info char(10), C_KEY int not null);
insert into table2 values(3, 'xxx', 0);
insert into table2 values(1, 'yyy', 1);
insert into table2 values(2, 'zzz', 2);

-- we expect 3 rows in any order;
-- yyy Value 1
-- zzz Value 2
-- xxx NULL
SELECT A.INFO,B.VAL FROM TABLE2 A
LEFT JOIN TABLE1 B ON (B.C_KEY=A.C_KEY) AND (B.C_KEY>0);

drop database;
