-- S0302911
-- FREBIRD DOESN'T SUPPORT "LONG" FLOATING POINT CONSTANTS 

set names ascii;
create database 'test.fdb'; 

create table dp_test (x double precision);
insert into dp_test values(11000000000000000.1);
insert into dp_test values(110000000000000000.);
insert into dp_test values(1100000000000000000);
insert into dp_test values(11000000000000000000);
insert into dp_test values(110000000000000000.00);
insert into dp_test values(110000000000.00000000);
select * from dp_test;

drop database;
