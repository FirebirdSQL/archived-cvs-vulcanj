-- S0329941
-- EXECUTE STATEMENT EATS PARSING ERROR
SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

set terminator ^;
create procedure testa AS
DECLARE VARIABLE Sql VARCHAR(30000); BEGIN
Sql = 'CREATE TABLE test1 ( i integer ) ;';
EXECUTE STATEMENT Sql;
end; ^
set terminator ;^


set terminator ^;
create procedure create_test_table AS
DECLARE VARIABLE Sql VARCHAR(30000);
declare variable i integer =1; BEGIN

Sql = 'CREATE TABLE test2 ( ';
while (i <= 5) DO BEGIN
   Sql = Sql || 'COL' || i || ' integer';
   i=i+1;
   if (i < 5) THEN
      Sql = Sql || ', ';
END
Sql = Sql || ' ); ';
EXECUTE STATEMENT Sql;
END; ^
set terminator ;^
commit;

execute procedure testa;

execute procedure create_test_table;
show tables;

drop database;

