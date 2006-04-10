-- S0302037
-- VULCAN: CREATE DOMAIN WITH MISSPELLED VALUE KEYWORD CAUSES CRASH

SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- domain is misspelled. Should produce an error saying the column
-- is misspelled.
create domain bb integer  check (vlaue > 0 and value <= 1) ;
-- please don't crash!

show domain bb;

drop database ;
