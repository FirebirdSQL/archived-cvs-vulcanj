SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- you can now use SEQUENCE as a synonym for GENERATOR

create sequence party_key;
create sequence account_key;
create sequence id ; 
show generators; 

create table test_table (id integer not null primary key, c char(20) ); 


set terminator ^;

create trigger test_table_pk for test_table
 active before insert position 0
 as 
 begin
    if (new.id is null) then
       new.id=gen_id(id,1);
 end^


create procedure populate_test_table AS
declare variable i integer =1; BEGIN
while (i <= 255) DO BEGIN
   insert into test_table (c) values ('text string '||:i) ;
   i=i+1;
END
END; ^
set terminator ;^
execute procedure populate_test_table; 

select id, c from test_table rows 10; 

drop database ;
