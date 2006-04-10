create database 'test.fdb'; 

create table t (i integer not null primary key);

set term ^;
execute block as
begin
  insert into t(i) values (1);
  insert into t(i) values (2);
end;
^

set term ;^

-- trivial case should return 2 rows;
select * from t;
rollback;


set term ^;
execute block returns (a int)
  as begin
  insert into t(i) values (1);
  insert into t(i) values (2);
  end;
^

set term ;^

-- added RETURNS clause, but should still return 2 rows;
select * from t;
rollback;

set term ^;
-- should trigger pk violation
execute block returns (a int)
  as begin
  insert into t(i) values (3);
  insert into t(i) values (3);
  end;
^

set term ;^

-- should return no rows - previous EB insert was bad because of PK violation;
select * from t;
rollback;


set term ^;
-- a little more complex. the "suspend" lets us return a result set;
-- variables get returned as the type they are declared;
execute block returns (a int, b int, c int)
  as begin
  insert into t(i) values (1);
  insert into t(i) values (2);
  select max(i), cast (max(i) as double precision), max(i)+1 from t into :A, :B, :C;
  suspend;
  end;
^

set term ;^

-- should return 2 rows;
select * from t;
rollback;

drop database;
