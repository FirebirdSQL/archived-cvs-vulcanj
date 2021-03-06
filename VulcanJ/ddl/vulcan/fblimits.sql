--
-- Defect S0328089
-- CREATING A FIREBIRD TABLE WITH ROW WIDTH >64K ABENDS WITH ASSERTION FAILURE
--
-- demonstrate the following:
-- row size limit of table is 65535 bytes
-- columns per index is 16
-- columns per primary key is 16
--
-- 2005/11/28
--
set names ascii;
create database 'test.fdb'; 

-- should get "new record size of 65536 bytes is too big"
recreate table bigrow (c1 char(30000),
   c2 char(30000), c3 char(5532) );

-- should pass...
recreate table bigrow (c1 char(30000),
   c2 char(30000), c3 char(5531) );

-- insert will succeed
insert into bigrow values ('hi', 'there', 'mom' );

-- select is now too big, and will fail
-- with "Implementation limit exceeded"
select * from bigrow; 

rollback;

-- this recreate will pass
recreate table bigrow (c1 char(30000), 
   c2 char(30000), c3 char(5514) );

-- this insert will pass
insert into bigrow values ('hi', 'there', 'mom' );

-- this select is just big enough to pass.
select * from bigrow; 

rollback;

-----
--
-- Columns per index test.
--
-----

recreate table lotsa_columns (i1 integer,
   i2 integer,
   i3 integer,
   i4 integer,
   i5 integer,
   i6 integer,
   i7 integer,
   i8 integer,
   i9 integer,
   i10 integer,
   i11 integer,
   i12 integer,
   i13 integer,
   i14 integer,
   i15 integer,
   i16 integer,
   i17 integer);

-- 16 columns per index ok
create index lc_index on lotsa_columns (i1,i2,i3,i4,i5,i6,i7,i8,i9,
   i10,i11,i12,i13,i14,i15,i16); 

-- insert should be ok
insert into lotsa_columns values (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17);

drop index lc_index;

rollback;

-----
--
-- Columns per primary key test.
--
-----

recreate table lotsa_columns (i1 integer not null,
   i2 integer not null,
   i3 integer not null,
   i4 integer not null,
   i5 integer not null,
   i6 integer not null,
   i7 integer not null,
   i8 integer not null,
   i9 integer not null,
   i10 integer not null,
   i11 integer not null,
   i12 integer not null,
   i13 integer not null,
   i14 integer not null,
   i15 integer not null,
   i16 integer not null,
   i17 integer not null);

-- 16 columns per pk ok
alter table lotsa_columns
   add constraint pk_lotsa_columns
   primary key (i1,i2,i3,i4,i5,i6,i7,i8,i9,
   i10,i11,i12,i13,i14,i15,i16); 

-- insert should be ok
insert into lotsa_columns values (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17);

alter table lotsa_columns drop constraint pk_lotsa_columns;

commit;

-- 17 columns per pk no good...
alter table lotsa_columns
   add constraint pk_lotsa_columns
   primary key (i1,i2,i3,i4,i5,i6,i7,i8,i9,
   i10,i11,i12,i13,i14,i15,i16,i17); 

-- ...and the drop should fail.
alter table lotsa_columns drop constraint pk_lotsa_columns;

-- here is another way to do it, all in 1 statement. This will fail (17 columns for PK).
recreate table lots_columns (i1 integer not null, i2 integer not null, i3 integer not null, i4 integer not null, i5 integer not null, i6 integer not null, i7 integer not null , i8 integer not null , i9 integer not null , i10 integer not null , i11 integer not null , i12 integer not null , i13 integer not null , i14 integer not null , i15 integer not null , i16 integer not null , i17 integer not null,
   primary key (i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17) );


drop database;
