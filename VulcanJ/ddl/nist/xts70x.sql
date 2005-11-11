  SET NAMES ASCII; 
  CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- tests from the xts_70x series. 
-- 6/21/2006.

	-- TEST:7001 NULLIF producing NULL!
 CREATE TABLE STAFFb (SALARY DECIMAL(6), EMPNAME CHAR(20), HOURS INTEGER, PNUM CHAR(3), CITY CHAR(15), SEX CHAR);
 INSERT INTO STAFFb VALUES(10000,'Kilroy',10000,'P4','Athens','M');
 INSERT INTO STAFFb VALUES(15000,'Nickos',20000,'P6','Nickos','M');
 INSERT INTO STAFFb VALUES(NULL,'Nickos',NULL,'P5','Rhodes','M');
 INSERT INTO STAFFb VALUES(10010,'George',NULL,'P7','Georgia','M');
 INSERT INTO STAFFb VALUES(10005,NULL,30000,'P8',NULL,'M');
 INSERT INTO STAFFb VALUES(10001,'Gregory',12000,'P9',NULL,'M');
 SELECT SALARY, EMPNAME, HOURS, CITY FROM STAFFb WHERE NULLIF(SALARY,HOURS) IS NULL ORDER BY EMPNAME; 
	-- PASS:7001 If 2 rows are selected with the following order?
	--             SALARY EMPNAME HOURS CITY
	--             ====== ======= ===== ====
	-- PASS:7001 10000 Kilroy 10000 Athens?
	-- PASS:7001 NULL Nickos NULL Rhodes?
 SELECT SALARY,PNUM,HOURS,NULLIF(EMPNAME,CITY) FROM STAFFb WHERE EMPNAME = CITY OR EMPNAME IS NULL ORDER BY PNUM;
	-- PASS:7001 If 2 rows are selected with the following order?
	--             SALARY PNUM HOURS NULLIF(EMPNAME,CITY)
	--             ====== ==== ===== ====================
	-- PASS:7001 15000 P6 20000 NULL?
	-- PASS:7001 10005 P8 30000 NULL?
 SELECT SUM(NULLIF(NULLIF(SALARY,10000),20000)) as MySum FROM STAFFb; 
	-- todo: per comments, the result should have been 195016, but I get
	-- 45016, and that seems correct. Why the discrepancy?

	-- PASS:7001 If SUM = 195016?


ROLLBACK;
 CREATE TABLE ECCO (C1 CHAR(2));
 CREATE TABLE T4 (STR110 CHAR(110) NOT NULL, NUM6 NUMERIC(6) NOT NULL, COL3 CHAR(10), COL4 CHAR(20), UNIQUE(STR110,NUM6));
 INSERT INTO ECCO VALUES ('NL');
COMMIT;
 
   INSERT INTO T4
      VALUES
      ('This is the first compound character literal.',1,NULL,NULL);
-- PASS:7004 If 1 row inserted successfully?

   INSERT INTO T4
         VALUES('Second character literal.',2,NULL,NULL);
-- PASS:7004 If 1 row inserted successfully?

   INSERT INTO T4
         VALUES('Third character literal.',3,NULL,NULL);
-- PASS:7004 If 1 row inserted successfully?

   SELECT NUM6
         FROM   T4
         WHERE  STR110 = 'This is the compound ' ||
         'character literal.';
-- PASS:7004 If 0 rows selected - no data condition?

   SELECT COUNT(*)
         FROM   T4
         WHERE  STR110 <> 'This is the first compound ' ||
         'character literal.';
-- PASS:7004 If COUNT = 2?

   SELECT NUM6
         FROM   T4
         WHERE  NUM6 = 2 AND 
         STR110 <= 'Second character '--Comments here
         ||'literal.';
-- PASS:7004 If NUM6 = 2?

   SELECT NUM6
         FROM   T4
         WHERE  STR110 = 'Third character literal.'--Comments here
         || 'second fragment'
         || 'third fragment.';
-- PASS:7004 If 0 rows selected - no data condition?

   SELECT NUM6
         FROM   T4
         WHERE  STR110 = 'First fragment'
         || 'another fragment'--Comments    
         || 'Second character literal.'--Comments here
         || 'fourth fragment.';
-- PASS:7004 If 0 rows selected - no data condition?

   SELECT NUM6
         FROM   T4
         WHERE  STR110 <= 'Second '    
         || 'chara'--Comments    
         || 'cter liter'--Comments here
         || 'al.'
         || '     ';
-- PASS:7004 If NUM6 = 2?

   SELECT COUNT(*)
         FROM   T4
         WHERE  STR110 < 'An indifferent'--Comments
         || ' charac'    
         || 'ter literal.';
-- PASS:7004 If sum of this COUNT and the next COUNT = 3?

   SELECT COUNT(*)
         FROM   T4
         WHERE  STR110 >= 'An indifferent'--Comments
         || ' charac'     
         || 'ter literal.';
-- PASS:7004 If sum of this COUNT and the previous COUNT = 3?

   SELECT NUM6
         FROM   T4
         WHERE  STR110 = 'Second '    
         || 'chara'--Comments    
         || 'cter liter'--Comments here
         || 'al.'
         || '     '--Comments
         ||'      ';
-- PASS:7004 If NUM6 = 2?

   SELECT NUM6
         FROM   T4
         WHERE  NUM6 = 2 AND STR110 < 'Second '    
         ||'chara'--Comments    
         ||'cter literal.';
-- PASS:7004 If 0 rows selected - no data condition?

   ROLLBACK WORK;

-- END TEST >>> 7004 <<< END TEST
-- *********************************************

-- TEST:7005 Compound character literal as inserted value!

   INSERT INTO T4
     VALUES
('This is the first fragment of a compound character literal,'--Comments
       || ' and this is the second part.',11,NULL,'Compound '  
          
      --Comments   
                 
       || 'literal.');
-- PASS:7005 If 1 row inserted successfully?

   INSERT INTO T4
         VALUES('This is a comp'
         || 'ound character literal,'       
         || ' in the second table row.',12,NULL,NULL);
-- PASS:7005 If 1 row inserted successfully?

   INSERT INTO T4
         VALUES('This is '
         || 'a comp'      
         || 'ound '
         || 'char'
         || 'acter lit'-- Comments
         || 'eral, ' 
     -- Comments        
           
         || 'in the th'
         || 'ird '
         || 'table '
         || 'row.',13,NULL,NULL);
-- PASS:7005 If 1 row inserted successfully?

   SELECT STR110, COL4 FROM T4 WHERE NUM6 = 11;
-- PASS:7005 If STR110 = 'This is the first fragment of a compound
--                character literal, and this is the second part.'?
-- PASS:7005 If COL4 = 'Compound literal.'?

   SELECT STR110 FROM T4 WHERE  NUM6 = 12;
-- PASS:7005 If STR110 = 'This is a compound character literal, in
--                        the second table row.'?

   SELECT STR110
         FROM   T4
         WHERE  NUM6 = 13;
-- PASS:7005 If STR110 = 'This is a compound character literal, in 
--                        the third table row.'?

   ROLLBACK WORK;

-- END TEST >>> 7005 <<< END TEST
-- *********************************************

-- TEST:7006 Compound character literal in a <select list>!

   SELECT 'First fragment of a compound character literal, '
 --Comment1   
     
     
 --Comment2
     ||'and second part.',
'This is the first fragment of a compound character literal,'--...
     ||' this is the second,'
     

     ||' and this is the third part.'
              FROM   ECCO;
-- PASS:7006 If 1st value = 'First fragment of a compound character 
--                           literal, and second part.'?
-- PASS:7006 If 2nd value = 'This is the first fragment of a compound 
--                         character literal, this is the second, and
--                         this is the third part.'?

   ROLLBACK WORK;




-- Notes: Test of LIKE operator, with ESCAPE clause, including zero-length
-- ESCAPE clause. xts_702
-- 
-- TODO: Firebird wouldn't process the ESCPAE clause as written, this has
-- been commented out for now. Test is only partially implemented.
   
   
 CREATE TABLE STAFF (EMPNUM CHAR(3) NOT NULL UNIQUE,EMPNAME CHAR(20), GRADE DECIMAL(4), CITY CHAR(15));
 INSERT INTO STAFF VALUES ('E1','Alice',12,'Deale');
 INSERT INTO STAFF VALUES ('E2','Betty',10,'Vienna');
 INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');
 INSERT INTO STAFF VALUES ('E4','Don',12,'Deale');
 INSERT INTO STAFF VALUES ('E5','Ed',13,'Akron');
 SELECT COUNT(*) FROM STAFF WHERE  'Alice' LIKE 'Alice';
-- PASS:7007 If COUNT = 5?

 SELECT COUNT(*) FROM STAFF WHERE  'Equal_literal' NOT LIKE 'Eq_alS_literal%' ESCAPE 'S';
-- PASS:7007 If COUNT = 0?

 SELECT COUNT(*) FROM STAFF  WHERE  USER LIKE 'SYSDBA%';
-- PASS:7007 If COUNT = 5?


-- TEST:7008 LIKE with general char. value for pattern & escape!
 SELECT COUNT(*) FROM STAFF WHERE  EMPNAME LIKE EMPNAME;
-- PASS:7008 If COUNT = 5?

 INSERT INTO STAFF VALUES('E6','Theodora_FL',14,'T%S_FL%%%%%%%%%');
-- PASS:7008 If 1 row is inserted successfully?

 SELECT COUNT(*) FROM STAFF WHERE EMPNAME LIKE CITY ESCAPE 'S';
-- PASS:7008 If COUNT = 1?

 DELETE FROM STAFF;
 INSERT INTO STAFF VALUES('S','Dana%ELFT',14,'D%S%%%%%%%%%%%%');
-- PASS:7008 If 1 row inserted successfully?

-- TEST:7009 LIKE with zero-length escape!
 DELETE FROM STAFF;
 INSERT INTO STAFF VALUES('   ','Dana%ELFT',14,'D%0%%%%%%%%%%%%');
-- PASS:7009 If 1 row inserted successfully?
   
   
-- following test was form xts_703
-- TEST:7010 UNIQUE predicate, single table, all values distinct!
   
 drop table staff;
 CREATE TABLE STAFF (EMPNUM CHAR(3) NOT NULL UNIQUE,EMPNAME CHAR(20), GRADE DECIMAL(4), CITY CHAR(15));
 INSERT INTO STAFF VALUES ('E1','Alice',12,'Deale');
 INSERT INTO STAFF VALUES ('E2','Betty',10,'Vienna');
 INSERT INTO STAFF VALUES ('E3','Carmen',13,'Vienna');
 INSERT INTO STAFF VALUES ('E4','Don',12,'Deale');
 INSERT INTO STAFF VALUES ('E5','Ed',13,'Akron');

 DROP TABLE STAFFc;
 CREATE TABLE STAFFc (EMPNUM CHAR(3) NOT NULL, EMPNAME CHAR(20), GRADE DECIMAL(4), CITY CHAR(15), MGR CHAR(3), UNIQUE (EMPNUM));
 SELECT COUNT(*) FROM STAFF WHERE SINGULAR(SELECT * FROM STAFF);
-- PASS:7010 If COUNT = 0?

-- TEST:7011 UNIQUE PREDICATE, table subquery with non-null duplicates!
 SELECT COUNT(*) FROM   STAFF WHERE SINGULAR(SELECT GRADE FROM STAFF);
-- PASS:7011 If COUNT = 0?

 SELECT EMPNUM, GRADE FROM STAFF X WHERE (SINGULAR(SELECT GRADE FROM STAFF Y WHERE X.GRADE = Y.GRADE));
-- PASS:7011 If EMPNUM = E2?
-- PASS:7011 If GRADE = 10?

 SELECT COUNT (*) FROM STAFF X WHERE NOT SINGULAR (SELECT GRADE FROM STAFF Y WHERE X.GRADE = Y.GRADE);
-- PASS:7011 If COUNT = 4?

   DELETE FROM STAFFc;
   INSERT INTO STAFFc VALUES ('E1','Alice',12,'Deale',NULL);
   INSERT INTO STAFFc VALUES ('E2','Betty',10,'Vienna','E1');
   INSERT INTO STAFFc VALUES ('E3','Carmen',13,'Vienna','E2');
   INSERT INTO STAFFc VALUES ('E4','Don',12,'Deale','E2');
   INSERT INTO STAFFc VALUES ('E5','Don',12,'Deale','E1');
   INSERT INTO STAFFc VALUES ('E6','Tom',14,'Gettysburg','E5');
   INSERT INTO STAFFc VALUES ('E7','Kingdom',18,'Gettysburg','E7');

 INSERT INTO STAFFC (EMPNUM,EMPNAME,GRADE,CITY) VALUES('E9','Terry',13,NULL);
-- PASS:7012 If 1 row inserted successfully?

 INSERT INTO STAFFC (EMPNUM,EMPNAME,GRADE,CITY) VALUES('E8','Nick',13,NULL);
-- PASS:7012 If 1 row inserted successfully?

-- TODO: Couldn't recode this to work against Firebird...
-- SELECT COUNT(*) FROM STAFFC AS X 
--   WHERE UNIQUE(SELECT CITY, MGR FROM STAFFC AS Y 
--     WHERE X.GRADE = Y.GRADE);
-- PASS:7012 If COUNT = 9?

-- TODO: Couldn't recode this to work against Firebird...
-- SELECT COUNT(*) FROM STAFFC AS X
-- WHERE NOT UNIQUE(SELECT GRADE, CITY FROM STAFFC AS Y 
-- WHERE X.GRADE = Y.GRADE);
-- PASS:7012 If COUNT = 3?

ROLLBACK; 

DROP DATABASE;
