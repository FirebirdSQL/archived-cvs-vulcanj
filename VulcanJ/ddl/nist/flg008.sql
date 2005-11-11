SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT;

-- Query did not work in Firebird when I tried it, 2005/06/15

-- TEST:0454 SELECT nonGROUP column in GROUP BY!
-- FIPS Flagger Test. Support for this feature is not required.
-- If supported, this feature must be flagged as an extension to the
-- standard.

SELECT PTYPE, CITY, SUM (BUDGET), COUNT(*) FROM PROJ GROUP BY CITY ORDER BY CITY;

-- PASS:0454 If either 3, 4, or 6 rows are selected?
-- NOTE:0454 If 3 rows, then note whether sample CITY is given.
-- NOTE:0454 If 4 or 6 rows, then note whether SUM and COUNT
-- NOTE:0454 are for CITY or for PTYPE within CITY.

DROP DATABASE;
