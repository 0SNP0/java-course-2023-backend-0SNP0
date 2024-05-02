package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.service.ChatService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(Long chatId) {
        try {
            chatRepository.add(new Chat().setChatId(chatId).setRegisteredAt(OffsetDateTime.now()));
        } catch (DuplicateKeyException e) {
            throw new ChatAlreadyRegisteredException();
        }
    }

    @Override
    public void delete(Long chatId) {
        try {
            chatRepository.remove(new Chat().setChatId(chatId));
        } catch (EmptyResultDataAccessException e) {
            throw new ChatNotRegisteredException();
        }
    }

    @Override
    public void shouldBeRegistered(Long chatId) {
        try {
            chatRepository.get(new Chat().setChatId(chatId));
        } catch (EmptyResultDataAccessException e) {
            throw new ChatNotRegisteredException();
        }
    }
}
