set names ascii;
set sqlstate on;
create database 'test.fdb' default character set iso8859_1;
create table test (i integer);

-- sqlstate table already exists, 42S01
create table test (i integer);


-- DEFECT S0326512: violate an integrity constraint, should be 23000
create table foo (i integer not null primary key);
insert into foo values (1);
insert into foo values (1);
rollback;

-- german characters into ascii table
create table ascii_t (c char(30) character set ascii);
insert into ascii_t values (_UNICODE_FSS'Ich weiß es nicht');
rollback;

-- string is too big, 01004
create table big_str (c char(2) );
insert into big_str values ('test');
rollback;

-- object is in use, 42000
create table in_use (i integer);
insert into in_use values (1);
commit;
select * from in_use;
drop table in_use;

-- dependencies, 42000
create table t (i integer);
create view v as select * from t;
drop table t;


-- integrity constraint violation, 23000
create table t1 (i integer not null unique);
insert into t1 values (1);
insert into t1 values (1);


drop database;
quit;
