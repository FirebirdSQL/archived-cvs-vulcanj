-- s0406511
-- union against firebird with utf8 or utf16 encoded database returns duplicate columns

set names ascii;
create database 'test.fdb' default character set iso8859_1;
CREATE TABLE t1(c1 CHAR(10));
INSERT INTO t1 VALUES('one');
INSERT INTO t1 VALUES('one');
INSERT INTO t1 VALUES('two');

CREATE TABLE t2(c1 CHAR(5));
INSERT INTO t2 VALUES('one');
INSERT INTO t2 VALUES('one');
INSERT INTO t2 VALUES('two');

set sqlda_display on;

SELECT * FROM t1 UNION SELECT * FROM t2;
-- should get 2 rows

set sqlda_display off;
commit;
drop table t1;
drop table t2;


CREATE TABLE t1(c1 CHAR(10) character set utf16);
INSERT INTO t1 VALUES('one');
INSERT INTO t1 VALUES('one');
INSERT INTO t1 VALUES('two');

CREATE TABLE t2(c1 CHAR(5) character set utf16);
INSERT INTO t2 VALUES('one');
INSERT INTO t2 VALUES('one');
INSERT INTO t2 VALUES('two');
commit;

set sqlda_display on;

SELECT * FROM t1 UNION SELECT * FROM t2;
-- should get 2 rows

set sqlda_display off;

commit;
drop table t1;
drop table t2;


-- S0520312
-- NLS:GOT THE REPEATED RECORDS WHEN UNION TWO TABLES WITH FIREBIRD DRIVER 

create table TEST1 (Z char(10) character set utf16, ZV varchar(10) character set utf16);
insert into TEST1 values ('ABC', 'ABC');
insert into TEST1 values ('ABC', 'ABC');
insert into TEST1 values ('DEF', 'DEF');

create table TEST2 (Z char(10) character set utf16);
insert into TEST2 values ('ABC');
insert into TEST2 values ('ABC');
insert into TEST2 values ('DEF');
commit;

set sqlda_display on;

select * from test1;

SELECT Z FROM TEST2 UNION SELECT ZV as Z  FROM TEST1;
-- should get 2 rows

drop database;

quit;
