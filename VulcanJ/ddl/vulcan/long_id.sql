set names ascii;
-- s0363759 identifier is too long in fb2

create database 'test.fdb'; 

-- this create table statement fails, since column name > 31 characters
create table test1 (test567890test567890test567890test567890 integer);

create table fred (i integer);
insert into fred values (1);
select i as test567890test567890test567890test567890 from fred;

drop database;
