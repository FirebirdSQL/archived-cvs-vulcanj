-- defect S0387755
-- STORED PROCEDURES FAIL WHEN CALLED WITHOUT INPUT PARAMETERS
set names ascii;

create database 'test.fdb';

set term ^;
create procedure simple 
returns (result integer) as begin
result = 19; 
suspend; 
end;
^

set term ;^

select * from simple;

rollback;

drop database;
