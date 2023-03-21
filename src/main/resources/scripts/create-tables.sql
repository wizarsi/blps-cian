create table coordinates
(
    id        bigint primary key not null,
    latitude  double precision   not null,
    longitude double precision   not null
);

create table locations
(
    id             bigint primary key not null,
    address        text               not null,
    coordinates_id bigint             not null references coordinates (id)
);

create table ads
(
    id              bigint primary key not null,
    ad_type         varchar(20)        not null,
    property_type   varchar(20)        not null,
    location_id     bigint             not null references locations (id),
    amount_of_rooms int                not null,
    area            double precision   not null,
    floor           int                not null,
    price           int                not null
);

create table metro_stations
(
    id             bigint primary key not null,
    name           text               not null,
    coordinates_id bigint             not null references coordinates (id)
);

create table roles
(
    id   bigint primary key not null,
    name varchar(20)        not null
);

create table users
(
    id       bigint primary key not null,
    name     text               not null,
    email    text               not null,
    password text,
    role_id  bigint               not null references roles (id)
);