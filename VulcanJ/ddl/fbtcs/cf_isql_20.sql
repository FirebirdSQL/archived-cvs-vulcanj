CREATE DATABASE 'test.fdb';

create table t(a int);
create table u(a int);
set term ^;
create trigger t for u after delete as begin
old.a=old.a; end^
commit ^

set term ;^

show table t;
show table u;

drop database;
