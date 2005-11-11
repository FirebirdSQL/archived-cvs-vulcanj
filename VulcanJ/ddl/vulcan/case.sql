SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

create table t1 (i integer);
insert into t1 values (1);
insert into t1 values (2);
insert into t1 values (3);

create table onerow (i integer);
insert into onerow values (1);

commit;

select i,
   case when i = 1
   then 'one'
   else (select rdb$relation_id
      from rdb$database)
   end
   from t1;
   
select i,
   case when i = (select i from onerow)
   then 'one'
   else 'not one'
   end
   from t1;


drop database ;
