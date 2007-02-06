-- DEFECT S0346697
-- XYTHOS: (19) NEED BETTER ERROR MESSAGE WHEN CREATE INDEX FAILS
set names ascii;

create database 'test.fdb' page_size=1024;

create table fred (v varchar(1024) );

-- create index command will fail, page size is too small.
create index i1 on fred(v);

drop database;
