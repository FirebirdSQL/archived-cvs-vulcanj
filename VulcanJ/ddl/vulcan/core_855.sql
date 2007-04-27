-- S0372403
-- INTEGRATE CORE-855 FROM SF, BUGCHECK IN EVL.CPP 
set names ascii;
create database 'test.fdb';
select * from (
  select rdb$relation_id
  from rdb$database
)
where sum(rdb$relation_id) = 0;

drop database;
