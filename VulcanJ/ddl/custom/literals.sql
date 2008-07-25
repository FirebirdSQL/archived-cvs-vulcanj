-- DEFCT s0401304
-- FIREBIRD SHOULD DEFAULT TEXT LITERALS TO UTF8

set names UNICODE_FSS;

create database 'test.fdb' default character set iso8859_1;

set sqlda_display on;

-- sqlsubtype should now be 3, unicode_fss
select 'xyz' from rdb$database;

drop database;
quit;
