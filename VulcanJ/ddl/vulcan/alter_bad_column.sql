-- defect s0389131
-- sigaccess on alter table with bogus column name

set names ascii;

create database 'test.fdb';
create table testme (i integer) ;

-- should get error message "local column BOGUS not found"
alter table testme alter BOGUS to NEWNAME ;

drop database;
