-- core-1040 from fb 2.0
-- Wrong single-segment ascending index on character field with NULL and
-- empty string values
create database 'test.fdb';
recreate table t (str varchar(10));
commit;

insert into t values ('');
insert into t values (null);
commit;

create index t_i on t (str);
commit;

--result is 1, as expected
select count(*) from t where str = '';

--result is 0, wrong
select count(*) from t where str is null;

-- fixed by vlad, no tracker id that i found
create table buggg (f1 int not null, f2 int not null);
commit;

insert into buggg values (1, 1);
commit;

alter table buggg add pk int not null primary key;
-- alter table buggg add constraint pk_buggg primary key (pk);
-- alter table buggg add constraint pk_buggg primary key (f1, f2, pk);

drop database;
quit;
