package edu.java.scrapper.service.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;
    @Override
    public void register(Long chatId) throws ChatAlreadyRegisteredException {
        if (chatRepository.existsById(chatId)) {
            throw new ChatAlreadyRegisteredException();
        }
        chatRepository.save(new Chat().setChatId(chatId).setRegisteredAt(OffsetDateTime.now()));
    }

    @Override
    public void delete(Long chatId) throws ChatNotRegisteredException {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotRegisteredException();
        }
        chatRepository.deleteById(chatId);
    }

    @Override
    public void isRegistered(Long chatId) throws ChatNotRegisteredException {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotRegisteredException();
        }
    }
}
