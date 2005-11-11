SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- this implementation of factorial is not recursive. 

 set term ^;
 create procedure factorial (
 max_rows integer,
 mode integer )
 returns (row_num integer, result integer ) as
 declare variable temp integer;
 declare variable counter integer;
 begin
 counter=0;
 temp=1;
 while (counter <= max_rows) do begin
 row_num = counter;
 if (row_num=0) then
 temp=1;
 else
 temp=temp * row_num;
 result=temp;
 counter = counter + 1;
 if (mode=1) then suspend;
 end
 if (mode=2) then suspend;
 end
 ^
 set term ; ^

select * from factorial (5,2); 

create table onerow (i integer);
insert into onerow values (5);

-- note the derived table query
select (select ROW_NUM from factorial(i,2)) as RN from onerow ;
select (select RESULT from factorial(i,2)) as RS from onerow ;

-- note the derived table query
select (select ROW_NUM from factorial(i,1)) as RN from onerow ;
select (select RESULT from factorial(i,1)) as RS from onerow ;

drop database ;
