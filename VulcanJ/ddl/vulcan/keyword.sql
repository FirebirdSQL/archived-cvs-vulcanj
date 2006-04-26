-- S0290709
-- VULCAN CORE DUE TO USE OF RESERVED WORD IN FUNCTION 

set names ascii;

create database 'test.fdb';

CREATE table Foundation_xtension (objname  VARCHAR(20) CHARACTER SET ISO8859_1, "VALUE" VARCHAR(20) CHARACTER SET ISO8859_1, id VARCHAR(20) CHARACTER SET ISO8859_1, deleted int);

SELECT * FROM Foundation_xtension
   WHERE
   ( ( upper ( objname ) = upper ( 'ServerClassIdentifier')
   AND
   upper ( Value ) = upper ('INFORMIX' ) )
   AND
   ( ID IN ('A5UB37E1.A800001N' ,
            'A5UB37E1.A800001O' , 'A5UB37E1.A800001P' ) ) )
   AND
   "DELETED" =0;
   
drop database;
