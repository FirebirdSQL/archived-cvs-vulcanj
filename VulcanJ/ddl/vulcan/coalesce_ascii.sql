-- S0344083
-- XYTHOS: ISSUE #18: FETCH FAILS WHEN USING COALESCE AND THE DEFAULT CHARACTER SET IS ASCII 
create database 'test.fdb' default character set ascii;

Create table foo (col_a varchar(1));
Insert into foo values ('Y');

-- next line should work and not throw
-- "String data, right-truncated"

Select coalesce(col_a,'N') from foo;

drop database;
