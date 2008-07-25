--
-- Error conditions for table coupling

create database 'test.sdb;sasopts=new_dbpassword="vulcanrules"' default character set iso8859_1;
show database;
-- it is normal for the show database command to have the "sasopts=..." string;
-- appended.;

show system;
show table sas$metadata;

-- next test is invalid parse condition - list of options w/o parens
connect 'test.sdb;sasopts=dbpassword="vulcanrules",newpassword="fred"' user 'sysdba' password 'masterkey';

-- should be able to connect - password wasn't changed
connect 'test.sdb;sasopts=dbpassword="vulcanrules"' user 'sysdba' password 'masterkey';

-- another invalid parse condition - using semicolon as separator
connect 'test.sdb;sasopts=(dbpassword="vulcanrules";newpassword="fred")' user 'sysdba' password 'masterkey';

-- should be able to connect - password wasn't changed
connect 'test.sdb;sasopts=dbpassword="vulcanrules"' user 'sysdba' password 'masterkey';

-- unknown option test case
connect 'test.sdb;sasopts=(dbpassword="vulcanrules",ABC="fred")' user 'sysdba' password 'masterkey';

-- goofy test case option outside of list
connect 'test.sdb;sasopts=(dbpassword="vulcanrules",newpassword="fred");abc=bad' user 'sysdba' password 'masterkey';

-- now connect and drop. If drop fails, something went wrong
connect 'test.sdb;sasopts=dbpassword="vulcanrules"' user 'sysdba' password 'masterkey';
drop database;


-- now try some bad create calls
-- invalid parse - list of options w/o parens
create database 'test.sdb;sasopts=dbpassword="vulcanrules",newpassword="fred"' default character set iso8859_1;

