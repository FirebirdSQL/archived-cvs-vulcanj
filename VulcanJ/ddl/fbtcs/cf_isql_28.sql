CREATE DATABASE 'test.fdb';

CREATE TABLE TEST (ID INTEGER NOT NULL);

COMMIT WORK;
SET AUTODDL OFF;
SET TERM ^ ;

CREATE TRIGGER TEST_BU FOR TEST
ACTIVE BEFORE UPDATE POSITION 0
AS
BEGIN
delete from TEST where id=old.id;
END ^

COMMIT WORK ^
SET TERM ; ^


/* Step 2: DML */

insert into TEST values (1);
insert into TEST values (2);
insert into TEST values (3);
insert into TEST values (4);
insert into TEST values (5);
insert into TEST values (6);
commit;

/* fine so far */
/* fail with commit or rollback */

update TEST set id=-1 where id=1;
commit;

delete from TEST;


insert into TEST values (1);
insert into TEST values (2);
insert into TEST values (3);
insert into TEST values (4);
insert into TEST values (5);
insert into TEST values (6);
commit;

update TEST set id=-1 where id=1;
rollback;
/* error */

select * from test;

drop database;
