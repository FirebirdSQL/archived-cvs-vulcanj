SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- TEST:0885 FIPS sizing, VARCHAR (1000)!

 CREATE TABLE CONTACTS ( NAME CHAR (20), DESCRIPTION CHAR (500) CHARACTER SET UNICODE_FSS, KEYWORDS CHAR (500) CHARACTER SET UNICODE_FSS); 
-- PASS:0885 If table created successfully?

   COMMIT WORK;

   INSERT INTO CONTACTS VALUES ('Harry',
    _UNICODE_FSS'Harry works in the Redundancy Automation Division of the Materials Blasting Laboratory in the National Cattle Acceleration Project of lower Michigan.  His job is to document the trajectory of cattle and correlate the loft and acceleration versus the quality of materials used in the trebuchet.  He served ten years as the vice-president in charge of marketing in the now defunct milk trust of the Pennsylvania Coalition of All Things Bovine.  Prior to that he established himself as a world-class gra',
    _UNICODE_FSS'aardvark albatross nutmeg redundancy automation materials blasting cattle acceleration trebuchet catapult loft coffee java sendmail SMTP FTP HTTP censorship expletive senility extortion distortion conformity conformance nachos chicks goslings ducklings honk quack melatonin tie noose circulation column default ionic doric chlorine guanine Guam invasions rubicon helmet plastics recycle HDPE nylon ceramics plumbing parachute zeppelin carbon hydrogen vinegar sludge asphalt adhesives tensile magnetic');
-- PASS:0887 If 1 row inserted successfully?

   SELECT COUNT(*) 
     FROM CONTACTS
     WHERE DESCRIPTION =_UNICODE_FSS'Harry works in the Redundancy Automation Division of the Materials Blasting Laboratory in the National Cattle Acceleration Project of lower Michigan.  His job is to document the trajectory of cattle and correlate the loft and acceleration versus the quality of materials used in the trebuchet.  He served ten years as the vice-president in charge of marketing in the now defunct milk trust of the Pennsylvania Coalition of All Things Bovine.  Prior to that he established himself as a world-class gra'
  AND KEYWORDS =
_UNICODE_FSS'aardvark albatross nutmeg redundancy automation materials blasting cattle acceleration trebuchet catapult loft coffee java sendmail SMTP FTP HTTP censorship expletive senility extortion distortion conformity conformance nachos chicks goslings ducklings honk quack melatonin tie noose circulation column default ionic doric chlorine guanine Guam invasions rubicon helmet plastics recycle HDPE nylon ceramics plumbing parachute zeppelin carbon hydrogen vinegar sludge asphalt adhesives tensile magnetic';
-- PASS:0887 If COUNT = 1?

   SELECT COUNT(*) 
     FROM CONTACTS
     WHERE DESCRIPTION LIKE _UNICODE_FSS'%gra'
     AND KEYWORDS LIKE _UNICODE_FSS'%magnetic';
-- PASS:0887 If COUNT = 1?

   COMMIT WORK;

  DROP TABLE CONTACTS;
   
 DROP DATABASE;
