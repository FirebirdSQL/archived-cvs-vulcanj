SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0455 Relaxed union compatability rules for columns!
-- NOTE: OPTIONAL FIPS Flagger test
-- FIPS Flagger Test. Support for this feature is not required.
-- If supported, this feature must be flagged as an extension to the
-- standard.

 SELECT EMPNUM, CITY FROM STAFF UNION SELECT PTYPE, CITY FROM PROJ;
-- PASS:0455 If 9 rows are selected?
-- NOTE:0455 Shows support for UNION of CHAR columns
-- NOTE:0455 with different lengths.


 SELECT EMPNUM, CITY FROM STAFF UNION SELECT 'e1', CITY FROM PROJ;
-- PASS:0455 If 8 rows selected?
-- NOTE:0455 Shows support for UNION of Char column
-- NOTE:0455 with shorter CHAR literal.
				

 SELECT EMPNUM, GRADE FROM STAFF UNION SELECT EMPNUM, HOURS FROM WORKS;
				
-- PASS:0455 If 14 rows selected?
-- NOTE:0455 Shows support for UNION of DECIMAL columns
-- NOTE:0455 with different precision.
				
DROP DATABASE;
