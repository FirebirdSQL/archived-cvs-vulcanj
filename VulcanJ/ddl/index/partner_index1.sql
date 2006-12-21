-- s0389551: firebird should not support altering parent key under
-- foreign key constraint
-- Backported from FB2 release.

set names ascii;

create database 'test.fdb';
create table parent (pk integer not null primary key, c char(20) );
create table child (pk integer not null primary key, d char(20) );
alter table child add  foreign key (pk) references parent;
insert into parent values (1, 'test1');
insert into parent values (2, 'test2');
insert into child  values (1, 'test1');
commit;
-- we should not let this work!
alter table parent  alter pk type varchar(50);

drop database;
