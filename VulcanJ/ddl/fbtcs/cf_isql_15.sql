create database 'test.fdb';

/*-----------------------------------------------------------------------------*/
/* Create a table with computed field which is defined using non-existing UDF. */
/*-----------------------------------------------------------------------------*/
create table t0 (a integer, af computed by (non_exist_udf(a)));

drop database;
exit;
