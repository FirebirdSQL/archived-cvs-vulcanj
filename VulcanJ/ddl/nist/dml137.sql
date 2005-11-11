SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- Notes: This test was implemented, but in the firebird case there's not
-- much to do since we only support a simple "DROP TABLE" without restrict
-- option. Many TSQL features #3: enhanced proj/works! Note that the 2nd and
-- third part of this test were not implemented, as they use information
-- schema and interval arithmetic.

-- TEST:0697 Erratum: drop behavior, constraints (static)!

 CREATE TABLE UNDROPPABLE ( C1 INT NOT NULL PRIMARY KEY ); 
 CREATE TABLE DROPPABLE ( C1 INT NOT NULL PRIMARY KEY REFERENCES UNDROPPABLE, C2 INT, CHECK (C1 < C2));
 DROP TABLE UNDROPPABLE ; 


DROP DATABASE;
