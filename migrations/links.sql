--liquibase formatted sql

--changeset 0SNP0:2
create table if not exists links (
    link_id    bigserial primary key,
    url        varchar(256) unique not null,
    updated_at timestamp with time zone null
)
