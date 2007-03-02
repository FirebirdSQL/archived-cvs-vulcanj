-- defect s0369191
-- FIREBIRD RETURNS WARNING SQLSTATE FOR STRING TRUCATION ON INSERT (WRONG SQLSTATE)
set names ascii;
set sqlstate on;
create database 'test.fdb' default character set iso8859_1;
set sqlstate on;

create table test (c char(3) );

-- should produce sqlstate 22001
insert into test values ('xxxx');

create table testsrc (c char(5) );
create table testdest (c char(4) );
insert into testsrc values ('xxxxx');

-- again, sqlstate 22001
insert into testdest select * from testsrc;

create table test_utf8 (c char(5) character set utf8);
create table test_ascii (c char(5) character set ascii);
insert into test_utf8 values (_utf8'testñ');
insert into test_ascii select * from test_utf8;



SET TERM ^;
CREATE PROCEDURE trunc_proc (c_src char(10) ) 
AS
  declare c_dest char(5);
BEGIN
  c_dest = c_src;
END ^
SET TERM ;^
SHOW PROCEDURE trunc_proc;

execute procedure trunc_proc ('xxxxxxxxxxx');
execute procedure trunc_proc ('xxxxxxxxxx');

drop database;

quit;
