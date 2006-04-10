-- s0284777
-- VULCAN ERROR MESSAGE ON DROP TABLE CASCADE

CREATE DATABASE 'test.fdb' ;

create table t3 (i integer);
drop table t3 cascade;

SHOW TABLE t3;

DROP DATABASE;
