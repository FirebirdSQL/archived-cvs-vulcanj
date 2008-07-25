--
-- tests for table coupling feature
--
-- usage notes:
--  1) to add a coupling password, create a new database with the database file
--     name set to 'file.fdb;sasopts=newdbpassword="newpassword"' 
--  2) for multiple options, you can use parentheses and property/value pairs
--     example: 'test.sdb;sasopts=(property="value",property2="value2")'
--  3) if no coupling password is initially given, table coupling is not present

create database 'test.sdb;sasopts=new_dbpassword="vulcanrules"' default character set iso8859_1;
show database;
-- it is normal for the show database command to have the "sasopts=..." string;
-- appended.;

show system;
show table sas$metadata;

connect 'test.sdb;sasopts=dbpassword="vulcanrules"' user 'sysdba' password 'masterkey';
-- should connect;
rollback work;
drop database;
-- now try with options specified in parentheses;
create database 'test.sdb;sasopts=(new_dbpassword="vulcanrules")' default character set iso8859_1; 
show database;
select * from rdb$database;
-- should have 1 row;

select * from sas$metadata;
-- should have 1 row;
connect 'test.sdb;sasopts=dbpassword="vulcanrules"' user 'sysdba' password 'masterkey';
-- should connect;

connect 'test.sdb';
-- should fail, no coupling password supplied;

connect 'test.sdb;sasopts=dbpassword="imaloser"';
-- should fail;

connect 'test.sdb;sasopts=(dbpassword="imaloser",new_dbpassword="bigloser")';
-- should fail;

connect 'test.sdb;sasopts=dbpassword="vulcanrules"';
-- should pass;

connect 'test.sdb;sasopts=(dbpassword="vulcanrules",new_dbpassword="fred")'; 
-- connect and change password, should work.;

connect 'test.sdb;sasopts=dbpassword="vulcanrules"';
-- should now fail, that password has changed;

connect 'test.sdb;sasopts=dbpassword="fred"';
-- should pass with new password;

drop database ;




---;
--- now try with SCKEY alias;
---;
create database 'test.sdb;sasopts=new_sckey="vulcanrules"' default character set iso8859_1;
show database;
-- it is normal for the show database command to have the "sasopts=..." string;
-- appended.;

show system;
show table sas$metadata;

connect 'test.sdb;sasopts=sckey="vulcanrules"' user 'sysdba' password 'masterkey';
-- should connect;
rollback work;
drop database;
-- now try with options specified in parentheses;
create database 'test.sdb;sasopts=(new_sckey="vulcanrules")'; 
show database;
select * from rdb$database;
-- should have 1 row;

select * from sas$metadata;
-- should have 1 row;
connect 'test.sdb;sasopts=sckey="vulcanrules"' user 'sysdba' password 'masterkey';
-- should connect;

connect 'test.sdb';
-- should fail, no coupling password supplied;

connect 'test.sdb;sasopts=sckey="imaloser"';
-- should fail;

connect 'test.sdb;sasopts=(sckey="imaloser",new_sckey="bigloser")';
-- should fail;

connect 'test.sdb;sasopts=sckey="vulcanrules"';
-- should pass;

connect 'test.sdb;sasopts=(sckey="vulcanrules",new_sckey="fred")'; 
-- connect and change password, should work.;

connect 'test.sdb;sasopts=sckey="vulcanrules"';
-- should now fail, that password has changed;

connect 'test.sdb;sasopts=sckey="fred"';
-- should pass with new password;

drop database ;




---;
--- now try with PW alias;
---;
create database 'test.sdb;sasopts=NEWPW="vulcanrules"' default character set iso8859_1;
show database;
-- it is normal for the show database command to have the "sasopts=..." string;
-- appended.;

show system;
show table sas$metadata;

connect 'test.sdb;sasopts=PW="vulcanrules"' user 'sysdba' password 'masterkey';
-- should connect;
rollback work;
drop database;
-- now try with options specified in parentheses;
create database 'test.sdb;sasopts=(NEWPW="vulcanrules")'; 
show database;
select * from rdb$database;
-- should have 1 row;

select * from sas$metadata;
-- should have 1 row;
connect 'test.sdb;sasopts=PW="vulcanrules"' user 'sysdba' password 'masterkey';
-- should connect;

connect 'test.sdb';
-- should fail, no coupling password supplied;

connect 'test.sdb;sasopts=PW="imaloser"';
-- should fail;

connect 'test.sdb;sasopts=(PW="imaloser",NEWPW="bigloser")';
-- should fail;

connect 'test.sdb;sasopts=PW="vulcanrules"';
-- should pass;

connect 'test.sdb;sasopts=(PW="vulcanrules",NEWPW="fred")'; 
-- connect and change password, should work.;

connect 'test.sdb;sasopts=PW="vulcanrules"';
-- should now fail, that password has changed;

connect 'test.sdb;sasopts=PW="fred"';
-- should pass with new password;

drop database ;
