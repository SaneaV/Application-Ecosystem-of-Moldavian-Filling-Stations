create table user_data (
                id bigint primary key,
                language varchar(2) not null,
                radius double precision not null,
                latitude double precision not null,
                longitude double precision not null);