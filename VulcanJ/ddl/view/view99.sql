set names ascii;
create database 'test.fdb' default character set iso8859_1; 

-- CORE-831 backport for SAS defects

-- s0513575, CAN'T USE FUNCTIONS ON VIEW COLUMNS
CREATE TABLE T1( X INTEGER ) ;
CREATE TABLE T2 (X INTEGER, Y INTEGER);
CREATE VIEW V2 AS SELECT AVG(X) AS AA FROM T1 ;

DROP VIEW V2;

-- s0513476, create view issue
CREATE VIEW V1 AS SELECT X, Y FROM T2;
CREATE VIEW V2 AS SELECT T1_T.X as xx FROM (SELECT * FROM T1) T1_T, V1 WHERE T1_T.X=V1.X;
SHOW VIEW V2;

-- s0513595, can't create literal columns from a view
CREATE VIEW V3 AS SELECT X, 'HELLO WORLD' AS GREETING FROM T1;
SHOW VIEW V3;

drop database;

