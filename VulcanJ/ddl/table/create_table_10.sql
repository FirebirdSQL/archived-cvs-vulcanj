-- S0311923
-- RECREATE TABLE WITH PRIMARY CAUSES EXCEPTION

-- S0340381
-- PROBLEMS WITH RECREATE TABLE WITH PRIMARY KEY

create database 'test.fdb' user 'sysdba' password 'masterkey'
   page_size=16834 default character set iso8859_1;

recreate table t (i integer not null primary key,
   c char(10) character set unicode_fss);

insert into t values (1, 'text'); 	
recreate table t (i integer not null primary key,
   c char(10) );

commit;

-- recreate table t (i integer not null primary key, c char(10) );

drop database;
