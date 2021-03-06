--
-- defect S0315972, 
-- ACCESS VIOLATION WHEN INSERTING LONG STRING INTO CHAR(127) utf16 COLUMN
--
set names ascii;
create database 'test.fdb' default character set iso8859_1;

create table t32 (c char (127) character set utf16 );
insert into t32 (c) values (
'this string has 32 characters..' ||
'this string has 32 characters..' ||
'this string has 32 characters..' ||
'this string has 32 characters..' ||
'this string has 32 characters..' ||
'this string has 32 characters..' ||
'this string has 32 characters..' ||
'this string has 32 characters..' 
); 
-- insert should be rejected, too big!


-- test of comparison operator with pad character
CREATE TABLE STAFF4 (EMPNUM char(3), CITY char(15) CHARACTER SET utf16);
INSERT INTO STAFF4 (EMPNUM) VALUES ('E1');
INSERT INTO STAFF4 (EMPNUM, CITY) VALUES ('E2', ' ');
SELECT EMPNUM FROM STAFF4 WHERE CITY = ' ';
-- should return one row, with EMPNUM=E2


-- I don't think that _utf16 is doing what you want it to. I think it says
-- that the following data is utf16 instead of connection char set. I don't
-- think it actually converts the data to utf16's. Very confusing conventions.

create table test_table ( i integer, emp char(20) character set utf16);
insert into test_table values (1, user);
insert into test_table values (2, 'huffman');
insert into test_table values (3, 'smith'); 
select * from test_table ;
-- should return 3 rows.
-- I=1, EMP=S
-- I=2, EMP=huffman
-- I=3, EMP=s

ROLLBACK WORK;
DROP TABLE STAFF4;

SET NAMES ASCII;
CREATE TABLE STAFF4 (
   EMPNUM CHAR(3) CHARACTER SET utf16 NOT NULL,
   EMPNAME CHAR(20) CHARACTER SET utf16 DEFAULT NULL,
   GRADE DECIMAL(4) DEFAULT 0,
   CITY CHAR (15) CHARACTER SET utf16 DEFAULT '               ');
   
INSERT INTO STAFF4 (EMPNUM,GRADE) VALUES ('E1',40);
INSERT INTO STAFF4 (EMPNUM,EMPNAME) VALUES ('E2','HUFFMAN');
SELECT EMPNAME FROM STAFF4 WHERE GRADE=0 ;
-- should return 1 row, EMPNAME=H

drop database ;
