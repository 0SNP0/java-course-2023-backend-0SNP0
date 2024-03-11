--liquibase formatted sql

--changeset 0SNP0:1
create table if not exists chats (
    chat_id       bigint primary key,
    registered_at timestamp with time zone null
)
