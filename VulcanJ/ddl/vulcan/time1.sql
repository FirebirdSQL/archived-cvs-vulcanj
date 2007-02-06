-- S0360390
-- PARAMETER DESCRIBED AS DECIMAL, NOT NUMERIC, FOR TIME ELAPSED CALCULATION
set names ascii;
create database 'test.fdb';

set sqlda_display on;
select current_timestamp - current_timestamp from rdb$database;

drop database;
