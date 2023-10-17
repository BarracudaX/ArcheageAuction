CREATE DATABASE Archeage;
CREATE DATABASE ArcheageData;
USE Archeage;
create table users (id bigint not null, email varchar(255), password varchar(255), role enum ('ADMIN','USER'), primary key (id)) engine=InnoDB;
create table users_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into users_id_sequence_generator values ( 1 );
