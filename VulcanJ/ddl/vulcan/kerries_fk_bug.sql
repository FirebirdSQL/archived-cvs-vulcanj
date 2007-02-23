-- S0385573
-- DI STUDIO ISSUES WITH FOREIGN KEYS 
set names ascii;
set sqlstate on;

create database 'test.fdb';

create table product_list (
   product_id bigint not null primary key,
   product_name varchar (45),
   supplier_id bigint,
   product_level bigint,
   product_ref_id bigint) ;
   

create table kbdiscounttgt (
   product_id double precision not null,
   start_date date not null,
   end_date date not null,
   unit_sales_price double precision not null,
   discount double precision not null);

alter table KBDISCOUNTTGT
   add foreign key (PRODUCT_ID) references PRODUCT_LIST;

drop database;

quit;
