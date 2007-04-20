-- S0363296 ALTER TABLE ERROR MESSAGE / SQLSTATE ISSUES
create database 'test.fdb'; 

create table t (i integer);

set SQLSTATE ON;

-- column XYZ not present. should be 42S22, column not found
alter table t drop xyz;

-- again column XYZ not present, but this is a modify operation
-- again 42S22
alter table t alter xyz type integer;

drop database;
