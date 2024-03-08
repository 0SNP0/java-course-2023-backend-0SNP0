package edu.java.scrapper.service;

import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public void register(Long chatId) {
        if (!chatRepository.register(chatId)) {
            throw new ChatAlreadyRegisteredException();
        }
    }

    public void delete(Long chatId) {
        if (!chatRepository.delete(chatId)) {
            throw new ChatNotRegisteredException();
        }
    }

    public void isRegistered(Long chatId) {
        if (!chatRepository.isRegistered(chatId)) {
            throw new ChatNotRegisteredException();
        }
    }
}
