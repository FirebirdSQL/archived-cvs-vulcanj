--
-- Defect S0320338
-- EXTRA COLON CAUSES ISQL TO CRASH 
-- 
create database 'test.fdb'; 

create table USER_DATA (
    BASE_DATA_ID numeric(18,0) not null,
    OWNER_IDENTITY_ID numeric(18,0),
    primary key (BASE_DATA_ID)
);

create table IDENTITY (
    IDENTITY_ID numeric(18,0) not null,
    OBJECT_VERSION numeric(18,0) not null,
    IS_ACTIVE char(1),
    IS_GROUP char(1),
    OMRID varchar(80) not null unique,
    NAME varchar(80) not null,
    IDENTITY_PROXY_ID numeric(18,0) unique,
    primary key (IDENTITY_ID)
);

SELECT ident.IDENTITY_PROXY_ID
FROM USER_DATA ud
JOIN IDENTITY ident on ud.OWNER_IDENTITY_ID = ident.IDENTITY_ID
WHERE ud.BASE_DATA_ID = :OBJECT_ID;
-- this query should fail, but not crash

drop database;
