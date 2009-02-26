-- defect S0302147
-- will need to rebench when this defect is fixed.
create database 'test.fdb' default character set iso8859_1; 

CREATE TABLE FSK_GROUP (
       group_id             INTEGER NOT NULL PRIMARY KEY,
       group_name           VARCHAR(35) NOT NULL,
       group_desc           VARCHAR(35) NOT NULL,
       logical_delete_ind   CHAR(1) CHECK (logical_delete_ind = 'Y' or logical_delete_ind = 'N') NOT NULL
);

show table fsk_group; 

insert into fsk_group values (1, 'gname1', 'gdesc2', 'y'); 
insert into fsk_group values (2, 'gname2', 'gdesc3', 'Y'); 
insert into fsk_group values (3, 'gname3', 'gdesc3', 'B'); 

select group_id, logical_delete_ind from fsk_group; 


drop database; 
