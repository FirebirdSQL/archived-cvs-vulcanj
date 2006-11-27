-- defect S0315902
-- TABLE CORRUPTED WHEN RECREATE FAILS

create database 'test.fdb';

create table imaloserbaby (clientid integer, description varchar(20) );
insert into imaloserbaby values (1, 'test');
commit;
alter table imaloserbaby alter clientid to ci;

-- show table should show column CI.
show table imaloserbaby;

-- now for the real test. column header should say CI, not CLIENTID.
select * from imaloserbaby;

drop database;
