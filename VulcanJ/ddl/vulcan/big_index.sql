SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;
set list on;

/* varchar(255) as primary key didn't work in FB 1.5, but should now. */
/* index was too big... */

REcreate table t (v varchar(255) not null primary key); 
show table t; 

REcreate table t (i integer, v varchar(255)); 
create index index1 on t(v); 
show table t; 

/* insert some data */
set terminator ^;
create procedure populate_t AS
declare variable i integer =1; BEGIN
while (i <= 255) DO BEGIN
   insert into t values (1, 'text string '||:i );
   i=i+1;
END
END ^
set terminator ;^
execute procedure populate_t; 
select * from t; 

drop database ;
