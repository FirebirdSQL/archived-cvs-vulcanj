create database 'test.fdb';

create generator gen1;
set generator gen1 to 1000;
show generator gen1;

/*----------------------------*/
/* Computed by (a + gen_id()) */
/*----------------------------*/
create table t0 (a integer, genid_field computed by (a + gen_id(gen1, 1)));
show table t0;
insert into t0(a) values(10);
insert into t0(a) values(12);
select * from t0;

set generator gen1 to 1000;
show generator gen1;
select * from t0;

/*
**  Since computed fields are evaluated during run-time, the computed
**  field with gen_id() will be different every-time. So, the following
**  select will never have a match.
*/ 
set generator gen1 to 1000;
show generator gen1;
select * from t0 where genid_field = gen_id(gen1, 1);

drop database;
exit;

