package edu.java.scrapper.service.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationTest {
    private static final Long chatId = 1L;
    @Autowired
    private JpaChatRepository chatRepository;
    private ChatService chatService;

    @BeforeEach
    void init() {
        chatService = new JpaChatService(chatRepository);
    }

    @TestTransactionalRollback
    void register() {
        assertThat(chatRepository.existsById(chatId)).isFalse();
        chatService.register(chatId);
        assertThat(chatRepository.existsById(chatId)).isTrue();
    }

    @TestTransactionalRollback
    void alreadyRegistered() {
        chatService.register(chatId);
        assertThatExceptionOfType(ChatAlreadyRegisteredException.class)
            .isThrownBy(() -> chatService.register(chatId));
    }

    @TestTransactionalRollback
    void delete() {
        chatService.register(chatId);
        chatService.delete(chatId);
        assertThat(chatRepository.existsById(chatId)).isFalse();
    }

    @TestTransactionalRollback
    void deleteNotRegistered() {
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> chatService.delete(chatId));
    }

    @TestTransactionalRollback
    void isRegistered() {
        chatService.register(chatId);
        assertThatNoException().isThrownBy(() -> chatService.isRegistered(chatId));
    }

    @TestTransactionalRollback
    void isNotRegistered() {
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> chatService.isRegistered(chatId));
    }
}
