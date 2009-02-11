SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET iso8859_1;

-- test COMMENT ON command
-- SAS DEFECT S0540661
-- NF: ADD SUPPORT FOR COMMENT ON SYNTAX

create table comment_tab (c1 char(20) ) ;
comment on table comment_tab is 'this is a comment';
-- have to commit, since this is a system relation
commit;

select rdb$relation_name, rdb$description from rdb$relations where rdb$relation_name = 'COMMENT_TAB';

drop database ;
