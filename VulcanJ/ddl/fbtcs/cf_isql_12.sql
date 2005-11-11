create database 'test.fdb';

/*---------------------------------------------*/
/* Computed field using another computed field */
/*---------------------------------------------*/
create table t3 (a integer, af computed by (a*3), afaf computed by (af*2));
insert into t3(a) values(10);
select * from t3;

drop database;
exit;

