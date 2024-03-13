package edu.java.scrapper.repository;

import edu.java.scrapper.entity.Chat;

public interface ChatRepository extends Repository<Chat> {

    Chat get(Chat chat);
}
