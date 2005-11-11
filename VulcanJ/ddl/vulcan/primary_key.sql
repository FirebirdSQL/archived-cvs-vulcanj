--
-- defectid=S0284768
-- VULCAN ERROR MESSAGE ON CREATE TABLE WHEN PRIMARY KEY IS NULLABLE
--
create database 'test.fdb'; 

create table t2 (c char(20) primary key); 
-- create table should fail, because c is NULLABLE

drop database;
