-- S0394715
-- INCORRECT ERROR WHEN TRYING TO ADD FOREIGN KEY
set names ascii;
create database 'test.fdb'; 

create table "TKTS1" ("COL1" int, "COL2" int);

-- should fail with descriptive error
create table "TKTS2" ("COL1" int, "COL2" int, foreign key("COL2")
     references "TKTS1" ("COL1"));


drop database;
