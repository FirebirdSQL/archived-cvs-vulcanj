-- defect s0442109
-- error message cleanup
create database 'test.fdb';

create table foo (c char(32768));

create table foo (c varchar(32768));

create table foo (c nchar(32768));

create table foo (c national char varying (32768));

create table foo4567890123456789012345678901234567 (c22 char(32));

drop database;
