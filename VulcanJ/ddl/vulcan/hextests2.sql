-- defect s0395933
-- SET SQLSUBTYPE TO CS_BINARY FOR STRING HEX LITERALS
SET NAMES ASCII; 
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

set sqlda_display on;

-- SQLSUBTYPE should be 1, cs_binary for hex string literals
select x'deadbeef' from RDB$database;

drop database;
quit;
