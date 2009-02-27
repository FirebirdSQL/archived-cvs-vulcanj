set names ascii;
-- s0363759 identifier is too long in fb2

create database 'test.fdb';

-- this create table statement fails, since table name > 31 characters
create table test567890test567890test567890test567890 (i integer);

-- this create table statement fails, since column name > 31 characters
create table test1 (test567890test567890test567890test567890 integer);

create table fred (i integer);
insert into fred values (1);

-- this select will fail, since column is unknown.
select test567890test567890test567890test567890 from fred;

-- but a long alias is ok? Seems fishy
select i as test567890test567890test567890test567890 from fred;

-- wbo: TODO this should say column name too long, but gives old message.
-- wbo: that's because the new column is a string, not a MetaName
-- "Name longer than database column size"
alter table fred alter column i to test567890test567890test567890test567890;

drop database;
