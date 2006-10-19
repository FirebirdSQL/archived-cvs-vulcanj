set names ascii;
-- s0366698 primary key columns can be dropped with native firebird

--  The rules for dropping a regular column:
--
--    1. the column is not referenced in any views.
--    2. the column is not part of any user defined indexes.
--    3. the column is not used in any SQL statements inside of store
--         procedures or triggers
--    4. the column is not part of any check-constraints
--
--  The rules for dropping a column that was created as primary key:
--
--    1. the column is not defined as any foreign keys
--    2. the column is not defined as part of compound primary keys
--
--  The rules for dropping a column that was created as foreign key:
--
--    1. the column is not defined as a compound foreign key. A
--         compound foreign key is a foreign key consisted of more
--         than one columns.

create database 'test.fdb';

create table pk2 (i1 integer not null, i2 integer);
alter table pk2 add constraint pk2_pk primary key (i1);

-- next line should work, as pk column ins't part of a foreign key or
-- isn't part of a compound primary key
alter table pk2 drop i1;
show table pk2;

create table pk1 (i1 integer not null primary key, i2 integer);

-- next line should work, as pk column ins't part of a foreign key or
-- isn't part of a compound primary key
alter table pk1 drop i1;

show table pk1;


--
-- now try with user-defined index
--

create table ud_table (i1 integer not null, i2 integer not null);
create index ud_idx on ud_table(i1);

-- next line should fail, as i1 is part of a user-defined index.
alter table ud_table drop i1;

--
-- now try with view
--

create table base_tbl (i1 integer not null, i2 integer not null);
create view base_view as select i1 from base_tbl;

-- next line shoudl fail, as i1 is part of view
alter table base_tbl drop i1;


--
-- now try w foreign key
--
create table tbl1 (i1 integer not null primary key, i2 integer not null);
create table tbl2 (i1 integer not null primary key, i2 integer not null);
alter table tbl2 add constraint fk_tbl2 foreign key (i1) references tbl1(i1);

-- next line should fail, as it is part of fk
alter table tbl1 drop i1;

--
-- now try w compound primary key
--
create table ck1 (i1 integer not null, i2 integer not null);
alter table ck1 add constraint ck1_ck1 primary key (i1, i2); 
alter table ck1 drop i1;

drop database;
