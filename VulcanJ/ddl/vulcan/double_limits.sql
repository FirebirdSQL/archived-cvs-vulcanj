-- defect S0410647
-- SAS CRASH WHEN INSERTING EXTREMELY LARGE VALUE VIA PROC TSSQL
set names ascii;
create database 'test.fdb';
create table test (col1 double precision);

-- works
insert into test values (1.79769313486231570E+308);

-- too big, should err
insert into "TEST" ( "COL1" ) values ( 1.79769313486232e+308 ) ;
commit;

select * from test;
commit;

-- this did crash
select  -2.488355210669293e+39 from rdb$database;

-- exponent should now print as e+39 instead of e+039 on windows
select cast ('-2.488355210669293e+39' as double precision) from rdb$database;

drop database;
quit;
