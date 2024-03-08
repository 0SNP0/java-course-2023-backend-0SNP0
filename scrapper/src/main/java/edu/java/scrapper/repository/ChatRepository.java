package edu.java.scrapper.repository;

public interface ChatRepository {
    boolean register(Long chatId);

    boolean isRegistered(Long chatId);

    boolean delete(Long chatId);
}
