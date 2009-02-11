--
-- defectid=S0284768
-- VULCAN ERROR MESSAGE ON CREATE TABLE WHEN PRIMARY KEY IS NULLABLE
--
create database 'test.fdb'; 

create table t2 (c char(20) primary key); 
-- create table fails in vulcan, because c is NULLABLE
-- create table succeeds in fb2.5, and sets C to NOT NULL

show table t2;

drop database;
