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
select 'xx' || c1 || 'xx' from t1;
select 'xx' || c1 || 'xx' from t2;

SELECT * FROM t1 UNION SELECT * FROM t2;

drop database;

create database 'test.fdb' default character set utf16;
RECREATE TABLE t1(c1 CHAR(10) character set utf16);
INSERT INTO t1 VALUES('one');
INSERT INTO t1 VALUES('one');
INSERT INTO t1 VALUES('two');

RECREATE TABLE t2(c1 CHAR(5) character set utf16);
INSERT INTO t2 VALUES('one');
INSERT INTO t2 VALUES('one');
INSERT INTO t2 VALUES('two');
commit;

select 'xx' || c1 || 'xx' from t1;
select 'xx' || c1 || 'xx' from t2;

SELECT * FROM t1 UNION SELECT * FROM t2;

drop database;

quit;
