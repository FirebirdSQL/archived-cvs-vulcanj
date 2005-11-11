SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- TEST:0027 INSERT short string in long col -- space padding !
 CREATE TABLE TMP (T1 CHAR (10), T2 DECIMAL(2), T3 CHAR (10));
 INSERT INTO TMP (T1, T2, T3) VALUES ( 'xxxx',23,'xxxx');
 SELECT * FROM TMP WHERE T2 = 23 AND T3 = 'xxxx      ';
-- PASS:0027 If T1 = 'xxxx ' ?

 ROLLBACK;

-- TEST:0028 Insert String that fits Exactly in Column!
 INSERT INTO TMP (T1, T2, T3) VALUES ('xxxxxxxxxx', 23,'xxxxxxxxxx');
 SELECT * FROM TMP WHERE T2 = 23;
-- PASS:0028 If T1 = 'xxxxxxxxxx'?

 ROLLBACK;
 
-- TEST:0031 INSERT(column list) VALUES(NULL and literals)!
 INSERT INTO TMP (T2, T3, T1) VALUES (NULL,'zz','z');
 SELECT * FROM TMP WHERE T2 IS NULL;
-- PASS:0031 If T1 = 'z '?

DROP DATABASE;
