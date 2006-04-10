-- Defect S0284783
-- VULCAN HANDLING OF SUBSTRING() 

SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

create table t4 (city char(20)); 
insert into t4 values ('raleigh'); 
select substring (city from 4 for -1) from t4; 

select substring (city from 0 for 2) from t4; 

select substring (city from 4 for 0) from t4; 

drop database;
