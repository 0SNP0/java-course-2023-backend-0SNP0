--liquibase formatted sql

--changeset 0SNP0:3
create table if not exists mapping (
    chat_id bigint not null,
    url     varchar(256) not null,
    primary key (chat_id, url),
    foreign key (chat_id) references chats(chat_id) on delete cascade,
    foreign key (url)     references links(url) on delete cascade
)
