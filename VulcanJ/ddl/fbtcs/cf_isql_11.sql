create database 'test.fdb';

/*-----------------------------------------*/
/* Computed field using non-existing field */
/*-----------------------------------------*/
create table t0 (a integer, af computed by (b));

/*--------------------------------------------*/
/* Computed field using not yet defined field */
/*--------------------------------------------*/
create table t1 (af computed by (a), a integer);

show tables;
drop database;
exit;

