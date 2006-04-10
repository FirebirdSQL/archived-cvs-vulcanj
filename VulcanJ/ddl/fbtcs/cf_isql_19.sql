CREATE DATABASE 'test.fdb';

create table ONEROW (i integer);
insert into ONEROW values (1);
commit;

-- I am a comment

/* BEGIN */
-- I am a comment
select * from onerow;
/* END */

/* BEGIN */
-- comment with unclosed 'quoted string
select * from onerow;
/* END */

/* BEGIN */
-- comment with unclosed "quoted string
select * from onerow;
/* END */

/* BEGIN */
-- I am a comment;
select * from onerow;
/* END */

/* BEGIN with unclosed "quoted */
-- I am a comment;
select * from onerow;
/* END */

select * /*
comment
*/
from onerow;

select * 
/* comment */
from onerow;

select * 
-- comment
from onerow;

/*
Comment
*/ select * from onerow;

drop database;
