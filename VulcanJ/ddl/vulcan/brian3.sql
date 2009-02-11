-- defect S0550609
-- VIEW WITH CASE LOSES SQLSUBTYPE OF UNICODE_FSS 

create database 'test.tdb' default character set unicode_fss; 
set sqlda_display on;

drop view all_privs;

REcreate table privs (
   is_negative char(1),
   privilege_type varchar(5)
   );
   
insert into privs values ('N', 'FOO');
commit;

-- works, sqlsubtype is 3
select cast (privilege_type as varchar(5) character set unicode_fss) from privs;

-- view appears to be created ok...
create view all_privs1 as select cast (privilege_type as varchar(5) character set unicode_fss) PV from privs;

show view all_privs1;

-- but we lose the sqlsubtype in query using view - should be 3 for unicode_fss
select * from all_privs1;


-- this one uses the database default
create view all_privs2 as select cast (privilege_type as varchar(5)) PV from privs;
select * from all_privs2;

drop database;
