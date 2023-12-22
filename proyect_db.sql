drop database if exists project_db;
drop sequence if exists et_users_seq;
drop sequence if exists et_sector_seq;
drop sequence if exists et_checkpoint_seq;
drop user if exists user_watcher;


create user user_watcher with password 'password';
create database project_db with template=template0 owner=user_watcher;
\connect project_db;
alter default privileges grant all on tables to user_watcher;
alter default privileges grant  all on sequences to user_watcher;

create table et_users(
    user_id integer primary key not null ,
    first_name varchar(20) not null ,
    last_name varchar(20) not null ,
    email varchar(20) not null ,
    password text not null
);

create table et_sectors(
    sector_id integer primary key  not null,
    center_gps point not null,
    report_date bigint not null
);

create table et_sectors_of_users(
    user_id integer primary key not null references et_users(user_id),
    sector_id integer null references  et_sectors(sector_id)
);

CREATE TYPE user_state AS ENUM ('untracked', 'ok', 'alert','danger');

create table et_checkpoints(
    checkpoint_id integer primary key not null,
    user_id integer not null references et_users(user_id),
    sector_id integer null references et_sectors(sector_id) ,
    point_gps point not null,
    state user_state null, --not null ,
    report_date bigint not null

);

alter table et_checkpoints  add constraint users_constrain_fk
foreign key (user_id) references et_users(user_id);


alter table et_checkpoints add constraint sector_constrain_fk
foreign key (sector_id) references et_sectors(sector_id);

alter table et_sectors_of_users add constraint sector_of_users_constrain_user_fk
    foreign key (user_id) references et_users(user_id);

alter table et_sectors_of_users add constraint sector_of_users_constrain_sector_fk
    foreign key (sector_id) references et_sectors(sector_id);

create sequence et_users_seq minvalue 0 increment 1 start  0 ;
create sequence et_sector_seq minvalue 0 increment 1 start 0;
create sequence et_checkpoint_seq minvalue 0 increment 1 start 1000;

CREATE VIEW latest_et_sectors AS
SELECT *
FROM (
         SELECT *,
                ROW_NUMBER() OVER (PARTITION BY sector_id ORDER BY report_date DESC) AS row_num
         FROM et_sectors
     ) AS sector_with_row_number
WHERE row_num = 1;


CREATE VIEW latest_et_checkpoints AS
SELECT *
FROM (
         SELECT *,
                ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY report_date DESC) AS row_num
         FROM et_checkpoints
     ) AS checkpoint_with_row_number
WHERE row_num = 1;
