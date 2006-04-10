-- S0287714
-- FIREBIRD INVALID RESULTS DUE TO INDEX 

create database 'test.fdb';

CREATE TABLE foo (
    a INTEGER,
    b CHAR(10),
    c CHAR(10),
    d INTEGER,
    e INTEGER);

INSERT INTO foo VALUES (1, '1', 'a', 1,    1);
INSERT INTO foo VALUES (2, '2', 'b', 2,    3);
INSERT INTO foo VALUES (3, '3', 'c', 2,    2);
INSERT INTO foo VALUES (4, '4', 'd', 3,    2);
INSERT INTO foo VALUES (6, '5', 'a', NULL, 1);
INSERT INTO foo VALUES (8, '6', 'c', 3,    7);
INSERT INTO foo VALUES (7, '7', 'b', 1,    4);

commit;

-- we expect
--   1, 1, a, 1, 1
--   7, 7, b, 1, 4
SELECT * FROM foo x WHERE not x.d = any
   (SELECT y.d FROM foo y WHERE y.c = 'c');


CREATE INDEX fooidx ON foo (d);

-- we STILL expect
--   1, 1, a, 1, 1
--   7, 7, b, 1, 4
SELECT * FROM foo x WHERE not x.d = any
   (SELECT y.d FROM foo y WHERE y.c = 'c');

drop database;
