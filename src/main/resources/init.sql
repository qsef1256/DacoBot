create table if not exists user
(
    discord_id      bigint(20)         not null primary key,
    register_time   timestamp,
    status          varchar(255)    default 'OK' not null
);

create table if not exists cash
(
    discord_id      bigint(20)         not null primary key,
    cash            int(255)        default 0 not null,
    constraint discord_id
        foreign key (discord_id) references discord_user (discord_id)
            on update cascade on delete cascade
);
