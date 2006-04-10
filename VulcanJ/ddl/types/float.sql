-- S0275993 
-- FIREBIRD FLOAT SUPPORT DOES NOT CONFORM TO INTERBASE SPEC 

set names ascii;
create database 'test.fdb'; 

create table float_test (i integer, f float);

insert into float_test values (1, 3.0);
insert into float_test values (1, 3.402823466e+38); 
select * from float_test;

drop database;
