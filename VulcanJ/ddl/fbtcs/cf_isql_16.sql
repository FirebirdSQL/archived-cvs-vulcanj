create database 'test.fdb';

/*-------------------------------------------------------------*/
/* Create a table with computed field and improper attributes. */
/*-------------------------------------------------------------*/
create table t0 (a integer, af computed by (a*3) default 10);
create table t1 (a integer, af computed by (a*3) not null);
create table t2 (a char(5), af computed by (a||a) collate DOS850);
create table t3 (a integer, af computed by (a*3) check (a > 3));

create table t4 (a integer);
create table t4r (a integer, af computed by (a*3) references t4(a));

create table t5 (a integer, af computed by (a*3) unique);
create table t6 (a integer, af computed by (a*3) primary key);

show tables;
drop database;
exit;
