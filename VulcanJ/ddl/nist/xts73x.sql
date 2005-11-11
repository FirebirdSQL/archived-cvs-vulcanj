  SET NAMES ASCII; 
  CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- tests from the xts_73x series. 
-- 6/21/2006.


INPUT ddl/input/base-tab.sql;
COMMIT;


-- TEST:7030 Table name with 19 characters - delimited!

 CREATE TABLE "LONGIDENTIFIERSAAAA" (TNUM double precision);
-- PASS:7030 If table created successfully?

 CREATE TABLE "longidentifiersaaab" (TNUM double precision);
-- PASS:7030 If table created successfully?

 CREATE TABLE "0""LONGIDENTIFIERS_1" (TNUM double precision);
-- PASS:7030 If table created successfully?

 CREATE TABLE "0""LONGIDENTIFIERS_2" (TNUM double precision);
-- PASS:7030 If table created successfully?

 CREATE TABLE "lngIDENTIFIER% .,()" (TNUM double precision);
-- PASS:7030 If table created successfully?

 select rdb$relation_name from rdb$relations where rdb$relation_name not like 'RDB$%';
-- PASS:7030 If 5 rows are selected in following order?
--                     table_name
--                     ==========
--  PASS:7030 If 0"LONGIDENTIFIERS_1?
--  PASS:7030 If 0"LONGIDENTIFIERS_2?
--  PASS:7030 If LONGIDENTIFIERSAAAA?
--  PASS:7030 If lngIDENTIFIER% .,()?
--  PASS:7030 If longidentifiersaaab?

 DROP TABLE "LONGIDENTIFIERSAAAA" ;
-- PASS:7030 If table dropped successfully?

 DROP TABLE "longidentifiersaaab";
-- PASS:7030 If table dropped successfully?

 DROP TABLE "0""LONGIDENTIFIERS_1"; 
-- PASS:7030 If table dropped successfully?

 DROP TABLE "0""LONGIDENTIFIERS_2";
-- PASS:7030 If table dropped successfully?

 DROP TABLE "lngIDENTIFIER% .,()";
-- PASS:7030 If table dropped successfully?


-- TEST:7031 View name with 69 and 128 characters - deliminited!
-- NOTE:  If long lines are not supported by the ISQL interface, an
--        implementation defined line continuation format may be used
-- This test could be modified when support for longer column names is
-- added. For now it serves to test Firebird's 32-character column name
-- limit.
 CREATE VIEW A234597890123459789012345678901 AS SELECT * FROM STAFF;
-- PASS:7031 If view created successfully?

 CREATE VIEW "a234597890123459789012345678901" AS SELECT * FROM WORKS;
-- PASS:7031 If view created successfully?

 CREATE VIEW "longIDENTIFIERSJJJJJJJJWW% .,()" AS SELECT * FROM VTABLE;
-- PASS:7031 If view created successfully?

 CREATE VIEW LONGIDENTIFIERSAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEEFFFFFFFFFFGGGGGGGGGGHHHHHHHHHHIIIIIIIIIIJJJJJJJJJJKKKKKKKKKKAAAAAAAA as select * from vtable;
-- PASS:7031 If view created successfully?
-- this fails for firebird. 6/21/2005.

 SELECT  COUNT(*) FROM rdb$view_relations  WHERE RDB$RELATION_NAME = 'STAFF' AND rdb$VIEW_name = 'A234597890123459789012345678901';
-- PASS:7031 If COUNT = 0?

 SELECT  COUNT(*) FROM rdb$view_relations  WHERE RDB$RELATION_NAME = 'WORKS' AND rdb$VIEW_name = 'a234597890123459789012345678901';
 SELECT  COUNT(*) FROM rdb$view_relations  WHERE RDB$RELATION_NAME = 'VTABLE' AND rdb$VIEW_name = 'longIDENTIFIERSJJJJJJJJWW% .,()';
 DROP VIEW A234597890123459789012345678901;
 DROP VIEW "a234597890123459789012345678901";
 DROP VIEW "longIDENTIFIERSJJJJJJJJWW% .,()";

-- testXts_732 : skipped!
-- no NATURAL FULL OUTER JOIN operator. 6/21/2005.
-- TEST:7032 NATURAL FULL OUTER JOIN <table ref> -- static!


-- TEST:7034 National Character data type in comparison predicate!
 CREATE TABLE TAB734 ( CSTR1 NCHAR(10), CSTR2 NCHAR VARYING(12));
 INSERT INTO TAB734 VALUES ('   !','*  *');
 INSERT INTO TAB734 VALUES (' * ','+ +');
 INSERT INTO TAB734 VALUES ('+ +','+ +');
 INSERT INTO TAB734 VALUES (NULL,' + ');
 SELECT COUNT(*) FROM TAB734 WHERE CSTR1 = CSTR2;
-- PASS:7034 If COUNT = 1?

 SELECT COUNT(*) FROM TAB734 WHERE CSTR1 <>  _UNICODE_FSS '   !';
-- PASS:7034 If COUNT = 2?

 SELECT COUNT(CSTR2) FROM TAB734 WHERE CSTR2 =  _UNICODE_FSS '*  *';
-- PASS:7034 If COUNT = 1?

 SELECT COUNT(*)FROM TAB734 WHERE NOT CSTR1 <>  _UNICODE_FSS '   !';
-- PASS:7034 If COUNT = 1?

 SELECT COUNT(*) FROM TAB734 WHERE NOT  _UNICODE_FSS '*  *' = CSTR2;
-- PASS:7034 If COUNT = 3?



-- TEST:7035 INSERT National character literal in NCHAR column!

   CREATE TABLE TAB735
         (C1 NUMERIC(5) UNIQUE,
          C2 NCHAR(12));
-- PASS:7035 If table created successfully?

   COMMIT WORK;

   INSERT INTO TAB735 VALUES(1,NULL);
-- PASS:7035 If 1 row inserted successfully?

   INSERT INTO TAB735 VALUES(2, _UNICODE_FSS'!');
-- PASS:7035 If 1 row inserted successfully?

   INSERT INTO TAB735 VALUES(3, _UNICODE_FSS'  !');
-- PASS:7035 If 1 row inserted successfully?

   COMMIT WORK;

   SELECT C1,C2
         FROM TAB735
         ORDER BY C1;
-- PASS:7035 If 3 rows are selected with the following order?
--                 c1      c2
--                ----    ----
-- PASS:7035 If    1      NULL?
-- PASS:7035 If    2       !  ?
-- PASS:7035 If    3       !  ?

   ROLLBACK WORK;

   DROP TABLE TAB735;
-- PASS:7035 If table dropped successfully?

   COMMIT WORK;
   
   
   
   
   
-- TEST:7036 Update NCHAR Varying column with value from NCHAR domain!

   CREATE DOMAIN DOM1 AS NATIONAL CHARACTER VARYING(10)
         DEFAULT _UNICODE_FSS 'KILLER';
-- PASS:7036 If domain created successfully?

   COMMIT WORK;

   CREATE DOMAIN DOM2 AS NATIONAL CHAR VARYING(12)
         DEFAULT _UNICODE_FSS 'HELLAS';
-- PASS:7036 If domain created successfully?

   COMMIT WORK;

   CREATE DOMAIN DOM3 AS NCHAR VARYING(16)
         CHECK (VALUE IN (_UNICODE_FSS'NEW YORK', _UNICODE_FSS'ATHENS',
                          _UNICODE_FSS'ZANTE'));
-- PASS:7036 If domain created successfully?

   COMMIT WORK;

   CREATE TABLE TAB736
         (COLK DECIMAL(4) NOT NULL PRIMARY KEY,
          COL1 DOM1,
          COL2 DOM2,
          COL3 DOM3);
-- PASS:7036 If table created successfully?

   COMMIT WORK;

   INSERT INTO TAB736 (colk, col3) VALUES(1,_UNICODE_FSS'ATHENS');
-- PASS:7036 If 1 row inserted successfully?

   INSERT INTO TAB736 (colk, col3) VALUES(2,_UNICODE_FSS'ZANTE');
-- PASS:7036 If 1 row inserted successfully?

   COMMIT WORK;

   UPDATE TAB736 
         SET COL1 = 'KILLER',
         COL2 = 'HELLAS',
         COL3 = _UNICODE_FSS'NEW YORK'
         WHERE COLK = 2;
-- PASS:7036 If 1 row updated successfully?

   SELECT COLK,COL1,COL2,COL3
         FROM TAB736 
         ORDER BY COLK DESC;
-- PASS:7036 If 2 rows selected in the following order?
--                 colk     col1     col2     col3 
--                 ====     ====     ====     ====
-- PASS:7036 If     2       KILLER   HELLAS   NEW YORK?
-- PASS:7036 If     1       KILLER   HELLAS   ATHENS  ?

   ROLLBACK WORK;

   DROP TABLE TAB736 ;
-- PASS:7036 If table dropped successfully?

   COMMIT WORK;

   DROP DOMAIN DOM1 ;
-- PASS:7036 If domain dropped successfully?

   COMMIT WORK;

   DROP DOMAIN DOM2 ;
-- PASS:7036 If domain dropped successfully?

   COMMIT WORK;

   DROP DOMAIN DOM3 ;
-- PASS:7036 If domain dropped successfully?


DROP DATABASE;
