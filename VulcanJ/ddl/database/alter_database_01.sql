CREATE DATABASE 'test.fdb' ;

ALTER DATABASE ADD FILE 'TEST.G00' STARTING 10000;
COMMIT;

select rdb$file_sequence, rdb$file_start, rdb$file_length
      from rdb$files where rdb$file_name like '%TEST.G00';

DROP DATABASE;

