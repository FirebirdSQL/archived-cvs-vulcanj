create database 'test.fdb' user 'sysdba' password 'masterkey' 
   page_size=16834 default character set iso8859_1; 

-- bigint and numeric are both large integers, it's just that their
-- subtype is different. There was a problem where altering the 
-- numeric column to bigint left the column metadata at NUMERIC(0,0)
-- which was obviously incorrect. This has now been fixed.

create table tt (n numeric(11,0) );
alter table tt alter n type integer;
-- Statement failed, SQLCODE = -607
-- unsuccessful metadata update
-- -Cannot change datatype for N.  Conversion from base type BIGINT to INTEGER is not supported.

alter table tt alter n type bigint;
show table tt;

-- n should now be BIGINT, not NUMERIC

drop database;