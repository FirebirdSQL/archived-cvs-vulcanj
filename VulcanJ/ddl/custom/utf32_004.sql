SET NAMES ASCII;
CREATE DATABASE 'test.fdb' ;

--set sqlda_display on;

create table test_ascii (i integer, c char(10) character set ascii);
insert into test_ascii values (1, 'ascii');

create table test_none (i integer, c char(10) character set none);
insert into test_none values (1, 'none');

create table test_none_8 (i integer, c char(8) character set none);
insert into test_none_8 values (1, 'none');

create table test_utf32 (i integer, c char(10) character set utf32);

-- b engine11:\jrd_intl_ss\pad_spaces 1703
-- try with ascii first
insert into test_utf32 select * from test_ascii;

-- next operation will fail.
-- copying data from CS_NONE to UTF32 attempts a binary copy, which
-- won't work because the character set sizes are different, and
-- blank padding will get messed up
insert into test_utf32 select * from test_none;

-- this insert will work ok, but when we try to read it, we find
-- we've inserted invalid UTF32 data into table - at least data
-- that can't be converted back into ASCII
insert into test_utf32 select * from test_none_8;

select * from test_utf32;

-- now try with char varying. This will put invalid utf32 data into table!
commit;
recreate table test_utf32 (i integer, c varchar(10) character set utf32);
insert into test_utf32 select * from test_ascii;
insert into test_utf32 select * from test_none;
insert into test_utf32 select * from test_none_8;
select * from test_utf32;

DROP DATABASE;

