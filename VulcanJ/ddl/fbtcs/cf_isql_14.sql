create database 'test.fdb';

/*---------------------------------------------*/
/* Create a table with computed field.         */
/*---------------------------------------------*/
create table t0 (a integer, af computed by (a*3));
insert into t0(a) values(10);

/*---------------------------------------------------------------*/
/* Insert a value into computed-field column, which should fail. */
/*---------------------------------------------------------------*/
insert into t0(af) values(11);
select * from t0;

/*---------------------------------------------------------------*/
/* Update the computed-field column directly, which should fail. */
/*---------------------------------------------------------------*/
update t0 set af = 99 where a = 10;
select * from t0;

/*-----------------------------------------------------------------------------*/
/* Create a table with only a computed-field, which has constant value. Trying */
/* to insert a value in it should fail.                                        */
/*-----------------------------------------------------------------------------*/
create table t5 (af computed by (1+2));
insert into t5 values(10);

drop database;
exit;
