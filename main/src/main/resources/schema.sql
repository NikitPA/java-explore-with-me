create table categories (
    id  serial not null,
    name varchar(255),
    primary key (id)
                        );
create table compilation_event (
    compilation_id int4 not null,
    event_id int4 not null
                               );
create table compilations (
    id  serial not null,
    pinned boolean not null,
    title varchar(255),
    primary key (id)
                          );
create table events (
    id  serial not null,
    annotation varchar(2000),
    confirmed_requests int4 not null,
    created_on timestamp,
    description varchar(7000),
    event_date timestamp,
    paid boolean not null,
    participant_limit int4 not null,
    published_on timestamp,
    request_moderation boolean not null,
    state varchar(255), title varchar(120),
    views int4 not null,
    category_id int4,
    initiator_id int4,
    location_id int4,
    primary key (id)
                    );
create table locations (
    id  serial not null,
    lat float4 not null,
    lon float4 not null,
    primary key (id)
                       );
create table requests (
    id  serial not null,
    created timestamp,
    status varchar(255),
    event_id int4,
    requester_id int4,
    primary key (id)
                      );
create table users (
    id  serial not null,
    email varchar(255),
    name varchar(255),
    primary key (id)
                   );
alter table categories add constraint UK_t8o6pivur7nn124jehx7cygw5 unique (name);
alter table locations add constraint UK5kd1jdvx2b90kyrifb1714edc unique (lat, lon);
alter table requests add constraint UKarb4jcc7yar9a3mxk937loo67 unique (event_id, requester_id);
alter table compilation_event add constraint FKiriu17nlpdxqwhchs08741syt foreign key (event_id) references events;
alter table compilation_event add constraint FKowxfw4s9mhp8kcp2g3349foxy foreign key (compilation_id) references compilations;
alter table events add constraint FKo6mla8j1p5bokt4dxrlmgwc28 foreign key (category_id) references categories;
alter table events add constraint FKgsyp7tc40dhju9fq5i767kyun foreign key (initiator_id) references users;
alter table events add constraint FK7a9tiyl3gaugxrtjc2m97awui foreign key (location_id) references locations;
alter table requests add constraint FKm7vtr0204t3xcymbx4sa9t1ot foreign key (event_id) references events;
alter table requests add constraint FKeoax2t4j9i61p9lmon3009tr4 foreign key (requester_id) references users;
