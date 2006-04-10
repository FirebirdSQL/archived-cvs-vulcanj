-- test valid page sizes, including default


-- default is 4k
CREATE DATABASE 'test.fdb' ;
SHOW DATABASE;

DROP DATABASE;


-- test various page sizes;

-- 1K page size;
CREATE DATABASE 'test.fdb' PAGE_SIZE 1024;
SHOW DATABASE;

DROP DATABASE;

-- 2k page size;
CREATE DATABASE 'test.fdb' PAGE_SIZE 2048;
SHOW DATABASE;

DROP DATABASE;

-- 4k page size;
CREATE DATABASE 'test.fdb' PAGE_SIZE 4096;
SHOW DATABASE;

DROP DATABASE;

-- 8k page size;
CREATE DATABASE 'test.fdb' PAGE_SIZE 8192;
SHOW DATABASE;

DROP DATABASE;

-- 16k page size;
CREATE DATABASE 'test.fdb' PAGE_SIZE 16384;
SHOW DATABASE;

DROP DATABASE;

