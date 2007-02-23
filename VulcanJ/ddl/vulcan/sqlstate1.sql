-- new test cases for sqlstate
-- tests are not exhaustive, but hopefully provide good coverage
set names ascii;
set sqlstate on;
create database 'test.fdb';

create table test (i integer);
insert into test values (1);
insert into test values (2);
commit;

-- cardinality, 21000
select * from test where i = (select * from test) ;


-- domain not found, 42000 - syntax error or access violation
drop domain NO_SUCH_DOMAIN;

-- generator not found, 42000
drop generator NO_SUCH_GEN;


-- duplicate index, 42S11
create table idx_test (i integer);
create index i1 on idx_test (i);
create index i1 on idx_test (i);

-- column already exists, 42S21
create table test_add_column (i integer);
alter table test_add_column add i integer;

-- column not found, 42S22
select boogie from rdb$database;
create index i_nf on test(NO_COL) ;
alter table test drop NO_COL;



-- table not found 42S02 - verify SQLCODE, too, please.
drop table NOSUCHTABLE;

-- view not found 42S02 - verify SQLCODE, too, please.
drop view NOSUCHVIEW;


drop database;
quit;
