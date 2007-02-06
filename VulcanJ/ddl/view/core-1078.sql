-- DEFECT S0398273
-- SF: CORE-1078, VIEW WITH EQUALLY NAMED SOURCE FIELDS NOT FEASABLE
set names ascii;

create database 'test.fdb' default character set iso8859_1;

create table t1(id integer, field1 integer);
create table t2(id integer, field1 integer);

create view view1 as 
   select t1.field1 as t1f1,
	   t2.field1 as t2f1 
	from t1,t2
      where t1.id=t2.id;

-- using SQL-92 syntax always works
create view view2 as
   select t1.field1 as t1f1, t2.field1 as t2f1
   from t1
   inner join t2 on t1.field1 = t2.field1;

show views ;

show view view1;
show view view2;

drop database;
