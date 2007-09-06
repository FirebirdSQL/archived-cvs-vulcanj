
-- s0452168
-- backport of fix for core-1389
create database 'test.fdb';
create table tab (col integer not null);
create index index1 on tab(col);

insert into tab values (1);
insert into tab values (3);
insert into tab values (2);

commit;

SET PLAN ON;

SELECT FIRST 1 COL FROM TAB ORDER BY 1 ASC;
SELECT MIN(COL) FROM TAB;

DROP DATABASE;
QUIT;
