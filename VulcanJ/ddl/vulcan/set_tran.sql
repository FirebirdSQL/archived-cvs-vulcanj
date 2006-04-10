-- 
-- Defect S0321244
-- SET TRAN CAUSES ERROR IN ISQL 
--

set names ascii;
create database 'test.fdb';

-- do some work
recreate table foo (i integer not null);
insert into foo values (1);

set transaction wait isolation level read committed; 

show table foo;
-- pass
select * from foo;
-- no rows;

insert into foo values (1);
commit;
select * from foo;
-- 1 row

create table lock (c char(20)) ; 
set transaction wait isolation level read committed reserving lock for shared write; 
insert into lock values ('test');
commit;

set transaction w1;
-- named transaction fails in ISQL
commit;

set transaction wait isolation level read committed; 
commit;

select * from foo;
-- 1 row;

drop database;
