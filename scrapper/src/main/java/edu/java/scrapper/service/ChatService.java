package edu.java.scrapper.service;

import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;

public interface ChatService {
    void register(Long chatId) throws ChatAlreadyRegisteredException;

    void delete(Long chatId) throws ChatNotRegisteredException;

    void shouldBeRegistered(Long chatId) throws ChatNotRegisteredException;
}
