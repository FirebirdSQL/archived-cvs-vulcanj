-- S0319537
-- SQLCODE -607, UNSUPPORTED DYN VERB WHEN CREATING A TABLE AND SPECIFY TWO "NOT NULL"'S BACK TO BACK

create database 'test.fdb'
   page_size=16834 default character set iso8859_1;

create table foo ( bar int not null not null );

show table foo;

drop database;
