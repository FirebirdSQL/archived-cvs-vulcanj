-- S0371095
-- XYTHOS: (24) DSQL ERROR: INTERNAL: ASSERTION FAILURE IN BANDWIDTHQUOTATEST SUITE

create database 'test.fdb';

create table xy_test (bytes_this_period bigint, period_start timestamp);
commit;

update xy_test
set
   bytes_this_period =
      case
         when period_start < ? then bytes_this_period + ?
         else ?
      end;

drop database ;
