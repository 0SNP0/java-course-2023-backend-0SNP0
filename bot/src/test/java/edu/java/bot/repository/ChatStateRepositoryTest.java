package edu.java.bot.repository;

import edu.java.bot.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatStateRepositoryTest {
    private final long chatId = 1L;
    ChatStateRepository repository;

    @BeforeEach
    public void init() {
        repository = new ChatStateMemoryRepository();
    }

    @Test
    public void defaultState() {
        assertThat(repository.getCurrentCommand(chatId))
            .isEqualTo(ChatStateRepository.NoCommand.defaultState);
    }

    @Test
    public void newState() {
        var command = mock(Command.class);
        repository.setState(chatId, command);
        assertThat(repository.getCurrentCommand(chatId)).isEqualTo(command);
    }

    @Test
    public void clearState() {
        repository.setState(chatId, mock(Command.class));
        repository.clearState(chatId);
        assertThat(repository.getCurrentCommand(chatId))
            .isEqualTo(ChatStateRepository.NoCommand.defaultState);
    }
}
