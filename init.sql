CREATE DATABASE Archeage_en CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;;
CREATE DATABASE Archeage_ru CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;;
CREATE DATABASE Archeage;

CREATE DATABASE Archeage_price_lucius;
USE Archeage_price_lucius;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_corvus;
USE Archeage_price_corvus;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_fanem;
USE Archeage_price_fanem;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_shaeda;
USE Archeage_price_shaeda;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_ifnir;
USE Archeage_price_ifnir;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_xanatos;
USE Archeage_price_xanatos;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_taron;
USE Archeage_price_taron;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_raven;
USE Archeage_price_raven;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


CREATE DATABASE Archeage_price_nagashar;
USE Archeage_price_nagashar;
create table item_prices (copper integer not null, gold integer not null, silver integer not null, id bigint not null, timestamp datetime(6), item_name varchar(255), primary key (id)) engine=InnoDB;
create table item_prices_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into item_prices_id_sequence_generator values ( 1 );
create table pack_prices (id bigint not null, destination_location varchar(255), primary key (id)) engine=InnoDB;
alter table item_prices add constraint ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT unique (item_name, timestamp);
alter table pack_prices add constraint FK4rwpmf6cfyicyh56ah3ykoio5 foreign key (id) references item_prices (id);


USE Archeage;
create table users (id bigint not null, email varchar(255), password varchar(255), role enum ('ADMIN','USER'), primary key (id)) engine=InnoDB;
create table users_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into users_id_sequence_generator values ( 1 );
USE Archeage_en;
create table crafting_material (quantity integer not null, item_id bigint not null, recipe_id bigint not null, primary key (item_id, recipe_id)) engine=InnoDB;
create table items (id bigint not null, description TEXT, name varchar(255), primary key (id)) engine=InnoDB;
create table items_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into items_id_sequence_generator values ( 1 );
create table locations (has_factory bit not null, id bigint not null, continent enum ('EAST','NORTH','WEST'), name varchar(255), primary key (id)) engine=InnoDB;
create table locations_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into locations_id_sequence_generator values ( 1 );
create table packs (creation_location_id bigint not null, id bigint not null, primary key (id)) engine=InnoDB;
create table recipes (produced_quantity integer not null, craftable_id bigint, id bigint not null, primary key (id)) engine=InnoDB;
create table recipes_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into recipes_id_sequence_generator values ( 1 );
create table servers (name varchar(255) not null, primary key (name)) engine=InnoDB;
alter table items add constraint ITEMS_UNIQUE_NAME_CONSTRAINT unique (name);
alter table crafting_material add constraint FKl24g8bw11xtarq11hvxm8sxi8 foreign key (item_id) references items (id);
alter table crafting_material add constraint FKe5wa9q3i5l5o7g9jnmauoqhx0 foreign key (recipe_id) references recipes (id);
alter table packs add constraint FK1xi3vivn5vbkpsptcme6e4ovy foreign key (creation_location_id) references locations (id);
alter table packs add constraint FKjml7yq3pqca98ugehgx0ul48k foreign key (id) references items (id);
alter table recipes add constraint FK3j7mnbptd1qayblp29pnqgy5l foreign key (craftable_id) references items (id);

USE Archeage_ru;
create table crafting_material (quantity integer not null, item_id bigint not null, recipe_id bigint not null, primary key (item_id, recipe_id)) engine=InnoDB;
create table items (id bigint not null, description TEXT, name varchar(255), primary key (id)) engine=InnoDB;
create table items_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into items_id_sequence_generator values ( 1 );
create table locations (has_factory bit not null, id bigint not null, continent enum ('EAST','NORTH','WEST'), name varchar(255), primary key (id)) engine=InnoDB;
create table locations_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into locations_id_sequence_generator values ( 1 );
create table packs (creation_location_id bigint not null, id bigint not null, primary key (id)) engine=InnoDB;
create table recipes (produced_quantity integer not null, craftable_id bigint, id bigint not null, primary key (id)) engine=InnoDB;
create table recipes_id_sequence_generator (next_val bigint) engine=InnoDB;
insert into recipes_id_sequence_generator values ( 1 );
create table servers (name varchar(255) not null, primary key (name)) engine=InnoDB;
alter table items add constraint ITEMS_UNIQUE_NAME_CONSTRAINT unique (name);
alter table crafting_material add constraint FKl24g8bw11xtarq11hvxm8sxi8 foreign key (item_id) references items (id);
alter table crafting_material add constraint FKe5wa9q3i5l5o7g9jnmauoqhx0 foreign key (recipe_id) references recipes (id);
alter table packs add constraint FK1xi3vivn5vbkpsptcme6e4ovy foreign key (creation_location_id) references locations (id);
alter table packs add constraint FKjml7yq3pqca98ugehgx0ul48k foreign key (id) references items (id);
alter table recipes add constraint FK3j7mnbptd1qayblp29pnqgy5l foreign key (craftable_id) references items (id);
