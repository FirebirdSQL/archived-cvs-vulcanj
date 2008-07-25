--
-- S0346938
-- TABLE COUPLING FAILURE WITH DEFAULT CHARACTER SET UTF16 

-- create database command would hang
create database 'test.sdb' default character set utf16;
show database;

show system;

select * from sas$metadata;

drop database ;
