
set echo;

CREATE DATABASE 'test.fdb';

create table t(a int);
create view v as select a from t;

show tables;
show views;
show table v;
show table t;
show view v;
show view t;
drop database;
