create database 'test.fdb';

/*---------------------------------------------*/
/* Create a table with computed field.         */
/*---------------------------------------------*/
create table t0 (a integer, af computed by (a*3));
insert into t0(a) values(10);

/*---------------------------------------------*/
/* Create a table with nested computed field.  */
/*---------------------------------------------*/
create table t1 (a integer, af computed by (a*4), afaf computed by (af*5));
insert into t1(a) values(11);

/*---------------------------------------------------------------------*/
/* Now alter table and drop the field which is used in computed field. */
/* It shouldn't allow you to drop the field.                           */
/*---------------------------------------------------------------------*/
alter table t0 drop a;
show table t0;
select * from t0;

/*---------------------------------------------------------------------*/
/* Now alter table and drop the computed field which is used in other  */
/* computed field.                                                     */ 
/* It shouldn't allow you to drop the field.                           */
/*---------------------------------------------------------------------*/
alter table t1 drop af;
show table t1;
select * from t1;

drop database;
