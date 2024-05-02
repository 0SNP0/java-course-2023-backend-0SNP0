--liquibase formatted sql

--changeset 0SNP0:3
create table if not exists mapping (
    chat_id bigint not null,
    link_id bigint not null,
    primary key (chat_id, link_id),
    foreign key (chat_id) references chats(chat_id) on delete cascade,
    foreign key (link_id) references links(link_id) on delete cascade
)
