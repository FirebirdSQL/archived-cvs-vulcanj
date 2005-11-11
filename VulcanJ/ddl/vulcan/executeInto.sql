create database 'test.fdb' default character set iso8859_1;

--It's also possible to return values from the dynamic SQL.
--
--Syntax:
--
--    EXECUTE STATEMENT <value> INTO <var_list>;          (singleton form)
--      or
--    FOR EXECUTE STATEMENT <value> INTO <var_list> DO <stmt_list>;

recreate table onerow (i integer, vc varchar(100));
insert into onerow values (1, 'foo');
recreate table results (i integer);
commit;

SET TERM ^;
CREATE PROCEDURE test RETURNS(id INT) AS
BEGIN
  ID=5;
END ^
SET TERM ;^

SET TERM ^;
CREATE PROCEDURE test2 (id int) RETURNS(newid INT) AS
BEGIN
  newid=id;
END ^
SET TERM ;^

commit;

-- this is from firebird 1.5 release notes
set term ^;
CREATE PROCEDURE DynamicSampleThree (
   TextField VARCHAR(100),
   TableName VARCHAR(100))
RETURNS (Line VARCHAR(32000))
AS
DECLARE VARIABLE OneLine VARCHAR(100);
BEGIN
Line = '';
FOR EXECUTE STATEMENT 
   'SELECT ' || TextField || ' FROM ' || TableName INTO :OneLine
   DO
      IF (OneLine IS NOT NULL) THEN
         Line = Line || OneLine || ' ';
   SUSPEND;
END ^
set term ;^

execute procedure DynamicSampleThree ('VC', 'onerow');

CREATE EXCEPTION ExOverflow 'error';

SET TERM ^;
CREATE PROCEDURE DynamicSampleTwo (TableName VARCHAR(100))
AS
DECLARE VARIABLE Par INT;
BEGIN
   EXECUTE STATEMENT 'SELECT MAX(i) FROM ' || TableName INTO :Par;
   If (Par > 100) THEN
      EXCEPTION ExOverflow 'Overflow in ' || Tablename;
END ^
SET TERM ;^

execute procedure DynamicSampleTwo('onerow');
recreate table dst_test (i integer);
insert into dst_test values (101); 
execute procedure DynamicSampleTwo('dst_test');

SET TERM ^;
CREATE PROCEDURE DynamicSampleOne (Pname VARCHAR(100))
AS 
DECLARE VARIABLE Sql VARCHAR(1024);
DECLARE VARIABLE Par INT;
BEGIN
   SELECT MIN(i) FROM onerow INTO :Par;
   Sql = ' EXECUTE PROCEDURE ' || Pname || '(';
   Sql = Sql || CAST(Par AS VARCHAR(20)) || ')';
   EXECUTE STATEMENT Sql;
END ^
set term ;^

EXECUTE PROCEDURE DynamicSampleOne ('test2');

-- do the non-singleton form...
create table a (b numeric(18,3));
commit;
insert into a values(12345.678);
commit;
set term ^;
create procedure c 
  returns(d numeric(18,3)) 
as 
begin
  for execute statement 'select b from a' 
  into :d
  do suspend;
end
^
commit^
set term ;^

select * from c;

set term ^;
-- do the singleton form ...
EXECUTE BLOCK RETURNS (L INTEGER) AS BEGIN
   execute statement ('select cast (2+2 as integer) from onerow') into :L;
   insert into results values (:L);
   SUSPEND; 
END ^
set term ; ^

drop database ;
