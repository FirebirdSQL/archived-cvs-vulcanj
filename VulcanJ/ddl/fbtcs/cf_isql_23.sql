
SET SQL DIALECT 3;
SET NAMES ISO8859_1;

CREATE DATABASE 'test.fdb'

PAGE_SIZE 8192
DEFAULT CHARACTER SET ISO8859_1;

SET TERM ^ ;

/******************************************************************************/
/* Stored Procedures */
/******************************************************************************/

CREATE PROCEDURE GROUP_COPY (
SOURCE INTEGER,
DESTINATION INTEGER)
AS
BEGIN
EXIT;
END^


CREATE PROCEDURE INSERT_VALUES (
CONT INTEGER,
D_GROUP INTEGER)
AS
BEGIN
EXIT;
END^

SET TERM ; ^

/******************************************************************************/
/* Tables */
/******************************************************************************/

CREATE TABLE GROUPS (
GR_ID INTEGER NOT NULL,
GR_NAME VARCHAR(40) CHARACTER SET ISO8859_1 NOT NULL
COLLATE DE_DE
);

CREATE TABLE TEST (
ID INTEGER NOT NULL,
T_GROUP INTEGER NOT NULL
);

/******************************************************************************/
/* Primary Keys */
/******************************************************************************/
ALTER TABLE GROUPS ADD CONSTRAINT PK_GROUPS PRIMARY
KEY (GR_ID);
ALTER TABLE TEST ADD CONSTRAINT PK_TEST PRIMARY KEY
(ID, T_GROUP);

/******************************************************************************/
/* Foreign Keys */
/******************************************************************************/
ALTER TABLE TEST ADD CONSTRAINT FK_TEST FOREIGN KEY
(T_GROUP)
REFERENCES GROUPS (GR_ID);

/******************************************************************************/
/* Stored Procedures */
/******************************************************************************/

SET TERM ^ ;

ALTER PROCEDURE GROUP_COPY (
SOURCE INTEGER,
DESTINATION INTEGER)
AS
begin
insert into TEST( ID, T_GROUP )
select A.ID, :Destination from TEST A
where A.T_GROUP = :Source
and not exists (select * from TEST B
where B.ID = A.ID
and :Destination = B.T_GROUP );
end
^

ALTER PROCEDURE INSERT_VALUES (
CONT INTEGER,
D_GROUP INTEGER)
AS
DECLARE VARIABLE ANZ INTEGER;
begin
ANZ = 0;

while ( ANZ < CONT ) do
begin
if ( not exists ( select ID from TEST where ID = :ANZ
and T_GROUP =

:D_GROUP )) then

insert into TEST ( ID , T_GROUP ) values ( :ANZ ,
:D_GROUP );
ANZ = ANZ +1;
end
end
^

SET TERM ; ^

commit;

/* Default values for the tables. */
insert into GROUPS VALUES ( 1 , 'Group1' );
insert into GROUPS VALUES ( 2 , 'Group2' );
commit;
execute procedure INSERT_VALUES( 3000 , 1);
commit;

delete from TEST where T_GROUP = 2;
execute procedure GROUP_COPY( 1 , 2 );
commit;
select count(*) from test;
select * from groups;

drop database;
