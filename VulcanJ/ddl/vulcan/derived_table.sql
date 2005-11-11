SET NAMES ASCII;
CREATE DATABASE 'test.fdb' DEFAULT CHARACTER SET ISO8859_1;

-- you can now use SEQUENCE as a synonym for GENERATOR

recreate table clients (job char(20) ) ;
insert into clients values ('a1');
insert into clients values ('b1');
commit;

SELECT * FROM (SELECT * FROM clients WHERE job LIKE 'a%') AS cl;

SELECT job FROM (SELECT * FROM clients WHERE job LIKE 'a%') AS cl;

SELECT cl.job FROM (SELECT * FROM clients WHERE job LIKE 'a%') AS cl;

-- here is another test

create table employees ( name varchar(20), dept_id integer, emp_id integer); 
create table departments (department varchar(20), mgr_id integer, dept_id integer);

insert into employees values ('bob', 1, 1);
insert into employees values ('sarah', 1, 2);
insert into employees values ('sally', 2, 3);
insert into employees values ('suzie', 2, 4);
insert into employees values ('michael', 2, 5);

insert into departments values ('development', 1, 1);
insert into departments values ('marketing', 2, 2);

commit;

-- couldn't get this to work, probably because the group by has e1.emp_id
-- this was the sample from :
-- http://firebird.sourceforge.net/index.php?op=devel&sub=engine&id=roadmap

--Select
--  e.name,
--  d.department
--from employees e
--  join
--    (select d1.department,
--            d1.mgr_id
--     from departments d1
--       join employees e1
--       on (d1.dept_id = e1.dept_id)
--     group by
--       d1.department,
--       e1.emp_id
--       having count (*) > 25) d
--  on (d.mgr_id = e.emp_id);

-- query below groups by d1.mgr_id, not e1.emp_id and it works
Select
  e.name,
  d.department
from employees e
  join
    (select d1.department,
            d1.mgr_id
     from departments d1
       join employees e1
       on (d1.dept_id = e1.dept_id)
     group by
       d1.department,
       d1.mgr_id
       having count (*) > 2) d
  on (d.mgr_id = e.emp_id);

drop database ;
