SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

INPUT ddl/input/base-tab.sql;
COMMIT;

-- TEST:0880 Long constraint names, cursor names!

 CREATE TABLE T0880 ( C1 INT not null, C2 INT not null, CONSTRAINT "It was the best of times; it wa"PRIMARY KEY (C1, C2));
 INSERT INTO T0880 VALUES (0, 1);
 INSERT INTO T0880 VALUES (1, 2);
 INSERT INTO T0880 VALUES (1, 2);
	-- PASS:0880 If ERROR - integrity constraint violation?

 SELECT C1 FROM T0880 ORDER BY C1;
-- PASS:0880 If 2 rows are returned in the following order?
--                c1
--                ==
-- PASS:0880 If 0 ?
-- PASS:0880 If 1 ?

 ALTER TABLE T0880 DROP CONSTRAINT "It was the best of times; it wa" ;
 INSERT INTO T0880 VALUES (0, 1);
 SELECT COUNT (*) FROM T0880;
-- PASS:0880 If COUNT = 3?


 CREATE DOMAIN "Little boxes on the hillside, L"CHAR (4) ;
 CREATE TABLE T0881 ( C1 "Little boxes on the hillside, L" );
 INSERT INTO T0881 VALUES ('ABCD');

 SELECT COUNT(*) FROM T0881 WHERE C1 = 'ABCD';
-- PASS:0881 If COUNT = 1?

COMMIT;

 DROP TABLE T0881;

 DROP DOMAIN "Little boxes on the hillside, L";

DROP DATABASE;
