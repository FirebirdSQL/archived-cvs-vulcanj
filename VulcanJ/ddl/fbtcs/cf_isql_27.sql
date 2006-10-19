-- S0318609
-- Defect title: FBTCS: CF_ISQL_27, EXECUTE STORED PROCEDURE WITH GENERATOR 

CREATE DATABASE 'test.fdb';
CREATE GENERATOR GEN_TEST1;
SET GENERATOR GEN_TEST1 TO 4294967298 ;
CREATE GENERATOR GEN_TEST2;
SET GENERATOR GEN_TEST2 TO 0;
SET TERM ^ ;
create procedure rpl$generator_values
returns (
   gen_name varchar(31),
   gen_value integer
)
as
begin
  for 
    select rdb$generator_name
    from rdb$generators 
-- for fb2 system tables have 0's, not nulls. Use this WHERE clause:
--    where coalesce(rdb$system_flag, 0) = 0
    where rdb$system_flag is null 
    into :gen_name do
  begin
    execute statement 'select gen_id(' || gen_name || ',0) from rdb$database' into :gen_value;
    suspend;
  end
END^
SET TERM ; ^
commit;
show procedures;

connect 'test.fdb';
execute procedure rpl$generator_values;
drop database;
