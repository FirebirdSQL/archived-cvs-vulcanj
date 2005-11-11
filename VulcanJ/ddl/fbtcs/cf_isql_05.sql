create database 'test.fdb';

/*
** Syntax test cases - Valid Date operations
*/

/*-----------------*/
/* Computed by (d) */
/*-----------------*/
create table t0 (d date, dc computed by (d));
show table t0;
insert into t0(d) values('today');
insert into t0(d) values('tomorrow');
insert into t0(d) values('yesterday');
insert into t0(d) values('now');
select 'Passed 1 - Insert' from t0 where dc = d having count(*) = 4;

/*----------------------------------------------------------------*/
/* Note: Using cast() to convert 'today' first to a date literal. */
/*       And, then doing date arithmetic. The proper behaviour    */ 
/*       should just allow straight date addition as              */ 
/*       set d = 'today' + 5. There is already a bug entered about*/
/*       this (Bug No. xxxx). Change this test case once this     */
/*       bug is fixed.                                            */ 
/*----------------------------------------------------------------*/
update t0 set d = cast('today'as date) + 5  where d = 'today';
select 'Passed 1 - Update' from t0 where dc = d having count(*) = 4;

/*---------------------*/
/* Computed by (d + 1) */
/*---------------------*/
create table t5 (d date, dc computed by (d + 1));
show table t5;
insert into t5(d) values('today');
insert into t5(d) values('tomorrow');
insert into t5(d) values('yesterday');
insert into t5(d) values('now');
select 'Passed 2 - Insert' from t5 where dc = d + 1 having count(*) = 4;

update t5 set d = cast('today' as date) + 5 where d = 'today';
select 'Passed 2 - Update' from t5 where dc = d + 1 having count(*) = 4;

/*---------------------*/
/* Computed by (d - 1) */
/*---------------------*/
create table t10 (d date, dc computed by (d - 1));
show table t10;
insert into t10(d) values('today');
insert into t10(d) values('tomorrow');
insert into t10(d) values('yesterday');
insert into t10(d) values('now');
select 'Passed 3 - Insert' from t10 where dc = d - 1 having count(*) = 4;

update t10 set d = cast('today' as date) - 5 where d = 'today';
select 'Passed 3 - Update' from t10 where dc = d - 1 having count(*) = 4;

/*-----------------------*/
/* Computed by ('today') */
/*-----------------------*/
create table t15 (d date, dc computed by ('today'));
show table t15;
insert into t15(d) values('today');
insert into t15(d) values('tomorrow');
insert into t15(d) values('yesterday');
insert into t15(d) values('now');
select 'Passed 4 - Insert' from t15 where dc = 'today' having count(*) = 4;

update t15 set d = cast('today' as date) + 5 where d = 'today';
select 'Passed 4 - Update' from t15 where dc = 'today' having count(*) = 4;

/*---------------------------*/
/* Computed by ('today' + 1) */
/*---------------------------*/
create table t20 (d date, dc computed by (cast('today' as date) + 1 ));
show table t20;
insert into t20(d) values('today');
insert into t20(d) values('tomorrow');
insert into t20(d) values('yesterday');
insert into t20(d) values('now');
select 'Passed 5 - Insert' from t20 where dc = cast ('today' as date) + 1 having count(*) = 4;

update t20 set d = cast('today' as date) + 5 where d = 'today';
select 'Passed 5 - Update' from t20 where dc = cast ('today' as date) + 1 having count(*) = 4;

/*-----------------------*/
/* Computed by (d1 - d2) */
/*-----------------------*/
create table t25 (d_start date, d_end date, date_diff computed by (d_end - d_start));
show table t25;
insert into t25(d_start, d_end) values('yesterday', 'today');
insert into t25(d_start, d_end) values('today', 'tomorrow');
insert into t25(d_start, d_end) values('yesterday', 'tomorrow');
select 'Passed 6 - Insert' from t25 where date_diff = d_end - d_start having count(*) = 3;

update t25 set d_end = cast('today' as date) + 5 where d_start = 'today';
select 'Passed 6 - Update' from t25 where date_diff = d_end - d_start having count(*) = 3;

drop database;
exit;

