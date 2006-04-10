-- S0318311
-- FBTCS: DSQL_DOMAIN_04

create database 'test.fdb' ;
create table river_states (
   STATE varchar(4),
   RIVER varchar(30)
   );
insert into river_states values ('NC', 'Neuse');
commit;
create domain dom04e as varchar(25)
  check (value in (select river from river_states));
commit;

create table river2 (i integer, r1 dom04e);
insert into river2 (i, r1) values (1, 'Neuse');
commit;

insert into river2 (i, r1) values (2, 'Mississippi');

select * from river2;

drop database ;
