SET NAMES ASCII;
CREATE DATABASE 'test.fdb' ;
CREATE TABLE t( a INTEGER);
CREATE INDEX i ON t(a);

ALTER INDEX i INACTIVE;
ALTER INDEX i ACTIVE;
COMMIT;
SELECT RDB$INDEX_NAME, RDB$INDEX_INACTIVE FROM RDB$INDICES WHERE RDB$INDEX_NAME='I';

DROP DATABASE;

