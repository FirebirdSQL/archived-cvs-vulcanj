
CREATE DATABASE 'test.fdb';

set echo;

-- I am a comment

/* BEGIN */
-- I am a comment
select * from rdb$database;
/* END */

/* BEGIN */
-- comment with unclosed 'quoted string
select * from rdb$database;
/* END */

/* BEGIN */
-- comment with unclosed "quoted string
select * from rdb$database;
/* END */

/* BEGIN */
-- I am a comment;
select * from rdb$database;
/* END */

/* BEGIN with unclosed "quoted */
-- I am a comment;
select * from rdb$database;
/* END */

select * /*
comment
*/
from rdb$database;

select * 
/* comment */
from rdb$database;

select * 
-- comment
from rdb$database;

/*
Comment
*/ select * from rdb$database;

drop database;
