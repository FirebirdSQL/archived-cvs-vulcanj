SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- these are tests from the yts75X series that have to do with domains.
-- cascade and restrict weren't implemented, not supported by FB.
-- 2005/06/21.

-- TEST:7500 CREATE DOMAIN - SQL Procedure statement, no options!

 CREATE DOMAIN INTDOMAIN INTEGER;
 select rdb$field_name, RDB$DEFAULT_SOURCE,  RDB$FIELD_LENGTH,  RDB$FIELD_SCALE,  RDB$FIELD_TYPE,  RDB$Character_Length,  rdb$character_set_id from RDB$fields where rdb$field_name = 'INTDOMAIN';

 DROP DOMAIN intdomain ;
-- PASS:7500 If domain dropped successfully?


-- TEST:7501 CREATE DOMAIN as SQL proc statement with default!
 CREATE DOMAIN CHARDOMAIN AS CHAR(10) DEFAULT 'MANCHESTER';
 select count(*) from RDB$fields where rdb$field_name = 'CHARDOMAIN' ;
 select rdb$field_name, RDB$DEFAULT_SOURCE,  RDB$FIELD_LENGTH,  RDB$FIELD_SCALE,  RDB$FIELD_TYPE,  RDB$Character_Length  from RDB$fields where rdb$field_name = 'CHARDOMAIN' ;

 DROP DOMAIN CHARDOMAIN;
 -- PASS:7501 If domain dropped successfully?

-- TEST:7502 CREATE DOMAIN - SQL proc statement with default!

 CREATE DOMAIN sintdom AS SMALLINT CHECK (VALUE > 5 and VALUE < 24000);
-- PASS:7502 If domain created successfully?

 CREATE TABLE shorttab (keycol integer, domcol sintdom);
-- PASS:7502 If table created successfully?

 INSERT INTO shorttab VALUES (1,6);
-- PASS:7502 If 1 row inserted successfully?

 INSERT INTO shorttab VALUES (2,3);
	-- PASS:7502 If ERROR - integrity constraint violation?
	
 INSERT INTO shorttab VALUES (3, 123456789);
-- PASS:7502 If ERROR -integrity const. violation or ?
-- PASS:7502 numeric value out of range ?

 INSERT INTO shorttab VALUES (4,100);
-- PASS:7502 If 1 row inserted successfully?

 SELECT COUNT(*) FROM shorttab;
-- PASS:7502 If COUNT = 2?

 SELECT domcol FROM shorttab WHERE keycol = 1;
-- PASS:7502 If domcol = 6?

 SELECT domcol FROM shorttab WHERE keycol = 1;
-- PASS:7502 If domcol = 6?

 SELECT domcol FROM shorttab WHERE keycol = 4;
-- PASS:7502 If domcol = 100?

ROLLBACK;

 DROP TABLE shorttab;
-- PASS:7502 If table dropped successfully?
 DROP DOMAIN sintdom;
-- PASS:7502 If domain dropped successfully?


-- TEST:7506 Domain Constraint Containing VALUE!
 CREATE DOMAIN d AS INTEGER CHECK (VALUE IN (3,5,7,9,11));
-- PASS:7506 If domain created successfully?
 CREATE DOMAIN e AS CHAR CHECK (VALUE LIKE 'a');
-- PASS:7506 If domain created successfully?

 CREATE DOMAIN f AS INTEGER CHECK (VALUE * VALUE > 1 + VALUE);
-- PASS:7506 If domain created successfully?

 CREATE TABLE def_chk ( d_chk    d, e_chk    e, f_chk    f);
-- PASS:7506 If table created successfully?

 INSERT INTO def_chk VALUES (3,'a',3);
-- PASS:7506 If 1 row inserted successfully?

 INSERT INTO def_chk VALUES (2,'a',3);
	-- PASS:7506 If ERROR - integrity constraint violation?
	
 INSERT INTO def_chk VALUES (3,'z',3);
	-- PASS:7506 If ERROR - integrity constraint violation?
	
 INSERT INTO def_chk VALUES (3,'a',1);
	-- PASS:7506 If ERROR - integrity constraint violation?

ROLLBACK;

 DROP TABLE DEF_CHK ;
-- PASS:7506 If table dropped successfully?
 DROP DOMAIN d ;
-- PASS:7506 If domain dropped successfully?
 DROP DOMAIN e ;
-- PASS:7506 If domain dropped successfully?
 DROP DOMAIN f ;
-- PASS:7506 If domain dropped successfully?


-- TEST:7507 INSERT value in column defined on domain!
 CREATE DOMAIN atom CHARACTER  CHECK ('a' <= VALUE  and 'm' >= VALUE);
 CREATE DOMAIN smint INTEGER CHECK (1<= VALUE and 100 >= VALUE);
 CREATE TABLE dom_chk (col1 atom, col2 smint);
 INSERT INTO dom_chk VALUES ('c',38);
-- PASS:7507 If 1 row inserted successfully?

 SELECT col1, col2 FROM dom_chk;
-- PASS:7507 If col1 = "c" and col2 = 38?

 INSERT INTO dom_chk VALUES ('a',1);
-- PASS:7507 If 1 row inserted successfully?

 INSERT INTO dom_chk VALUES ('m', 100);
-- PASS:7507 If 1 row inserted successfully?

 INSERT INTO dom_chk VALUES ('z', 101);
	-- PASS:7507 If ERROR - integrity constraint violation?

 SELECT COUNT (*) FROM dom_chk  WHERE col1 = 'z';
-- PASS:7507 If COUNT = 0?

 UPDATE dom_chk SET col1 = 'q' WHERE col2 = 38;
-- PASS:7507 If ERROR - integrity constraint violation?

 SELECT COUNT (*) FROM dom_chk WHERE col1 = 'q';
-- PASS:7507 If COUNT = 0?

ROLLBACK; 

 DROP TABLE dom_chk;
 DROP domain atom;
 DROP domain smint;
 

-- TEST:7508 Put value in column defined on domain breading constraint!
 
 CREATE DOMAIN atom CHARACTER  CHECK ('a' <= VALUE  and 'm' >= VALUE);
 CREATE DOMAIN smint INTEGER CHECK (1<= VALUE and 100 >= VALUE);
 CREATE TABLE dom_chk (col1 atom, col2 atom);
 INSERT INTO dom_chk VALUES ('<', 100);
	-- PASS:7508 If ERROR - integrity constraint violation?

 INSERT INTO dom_chk VALUES ('a', 101);
-- PASS:7508 If ERROR - integrity constraint violation?

ROLLBACK;

 DROP TABLE dom_chk;
 DROP domain atom;
 DROP domain smint;



DROP DATABASE;
