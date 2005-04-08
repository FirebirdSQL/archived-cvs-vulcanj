CREATE DATABASE 'test.fdb' user 'sysdba' password 'masterkey';
create table foo (col char(10));
commit work;
insert into foo values (1);
insert into foo values (null);
select * from foo order by col NULLS FIRST;

DROP DATABASE;

