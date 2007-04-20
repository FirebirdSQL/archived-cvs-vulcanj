-- defect S0391814
-- INCORRECT ERROR MESSAGE FOR CERTAIN CONDITION FOR STORED PROCEDURES WITH PARAMETERS
set names ascii;

create database 'test.fdb';
set sqlstate on;

-- procedure won't be created because we're using p1 as 
-- a parameter and return parameter also
set term ^;
create procedure myproc (p1 char(10))
   returns ( p1 char(10))
   as begin
      p1='zzz';
   end
^

set term ;^


drop database;
