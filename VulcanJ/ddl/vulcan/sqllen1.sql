set names none;
create database 'test.fdb';
set sqlda_display on;
REcreate table david (i integer, c char(10) character set iso8859_1);
insert into david values (1, 'test');
commit;
select _UNICODE_FSS'abc' || c from david;

drop database;
