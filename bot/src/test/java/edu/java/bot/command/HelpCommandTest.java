package edu.java.bot.command;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HelpCommandTest extends CommandTest {
    List<AbstractCommand> commands;
    private final int commandCount = 5;

    @Override
    public void init() {
        super.init();
        commands = IntStream.range(0, commandCount).mapToObj(x -> new CommandWithoutArgs(
            "/command" + x,
            "description" + x,
            null
        ) {
        }).collect(Collectors.toList());
        command = new HelpCommand(commands);
        command.states = chatStateRepository;
    }

    @SneakyThrows @Test
    public void helpMessage() {
        assertThat(command.run(chatId)).isEqualTo(
            IntStream.range(0, commandCount)
                .mapToObj(x -> String.format("/command%d - description%d", x, x))
                .collect(Collectors.joining("\n"))
        );
    }

    @AfterEach
    public void checkStatesCleared() {
        verify(chatStateRepository, times(1)).clearState(chatId);
    }
}
