create database 'test.fdb';
set names ascii;

recreate table elmer("VALUE" int);                                              

-- next line should fail, value needs to be quoted.
delete from elmer where value = ?;                                              

-- this line should pass.
delete from elmer where "VALUE" = 3;

-- this line should fail
delete from elmer where "VALUE" = ?;

recreate table fudd ("FROM" char(20) ); 
insert into fudd values ('text');
commit;

select "FROM" from fudd;

drop database;
quit;
