-- S0281266
-- Defect title: VULCAN: SQL LIKE CLAUSE ON UNICODE VARCHAR COLUMN HANGS 

set names iso8859_1;
create database 'test.fdb';

CREATE TABLE PARENT (
    PARENT_ID    INTEGER NOT NULL,
    PARENT_NAME  VARCHAR(200) CHARACTER SET UNICODE_FSS,
    PARENT_DATE  TIMESTAMP);

-- Try to get back all of the rows starting with 'NIÑ':
select * from parent where parent_name like _UNICODE_FSS 'NIÑ%';

drop database;
