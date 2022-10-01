create table if not exists categories (
    id  serial not null,
    name varchar(255) not null unique,
    primary key (id)
                        );
create table if not exists users (
    id  serial not null,
    email varchar(55) not null,
    name varchar(55),
    is_subscribe boolean not null,
    primary key (id)
);
create table if not exists compilations (
    id  serial not null,
    pinned boolean not null,
    title varchar(255) not null,
    primary key (id)
                          );
create table if not exists locations (
    id  serial not null,
    lat float4 not null,
    lon float4 not null,
    primary key (id),
    constraint uq_lat_lon unique (lat, lon)
);
create table if not exists events (
    id  serial not null,
    annotation varchar(2000) not null,
    confirmed_requests int4 not null,
    created_on timestamp,
    description varchar(7000) not null,
    event_date timestamp,
    paid boolean not null,
    participant_limit int4 not null,
    published_on timestamp,
    request_moderation boolean not null,
    state varchar(255) not null,
    title varchar(120) not null,
    views int4 not null,
    category_id int4 not null,
    initiator_id int4 not null,
    location_id int4 not null,
    primary key (id),
    constraint fk_events_on_categories foreign key (category_id) references categories,
    constraint fk_events_on_users foreign key (initiator_id) references users,
    constraint fk_events_on_locations foreign key (location_id) references locations
                    );
create table if not exists requests (
    id  serial not null,
    created timestamp not null,
    status varchar(255) not null,
    event_id int4 not null,
    requester_id int4 not null,
    primary key (id),
    constraint uq_event_requester unique (event_id, requester_id),
    constraint fk_requests_on_events foreign key (event_id) references events,
    constraint fk_requests_on_users foreign key (requester_id) references users
                      );
create table if not exists compilation_event (
    compilation_id int4 not null,
    event_id int4 not null,
    primary key (compilation_id, event_id),
    constraint fk_compilation_event_on_event foreign key (event_id) references events,
    constraint fk_compilation_event_on_compilation foreign key (compilation_id) references compilations
);
create table if not exists user_friends (
    users_id int4 not null,
    subscription_id int4 not null,
    primary key (users_id, subscription_id),
    constraint uq_subscription unique (subscription_id),
    constraint fk_user_friends_on_users foreign key (subscription_id) references users,
    constraint fk_user_friend_on_friend foreign key (users_id) references users
);