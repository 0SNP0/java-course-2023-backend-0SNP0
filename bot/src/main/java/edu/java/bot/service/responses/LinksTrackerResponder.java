package edu.java.bot.service.responses;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.AbstractCommand;
import edu.java.bot.command.Commands;
import edu.java.bot.exception.UserIsNotRegisteredException;
import edu.java.bot.repository.ChatStateRepository;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class LinksTrackerResponder implements Responder {
    final Map<String, AbstractCommand> commands;
    final ChatStateRepository chatStates;

    public LinksTrackerResponder(@NotNull Commands commands, ChatStateRepository chatStates) {
        this.commands = commands.getList().stream().collect(Collectors.toMap(BotCommand::command, x -> x));
        this.chatStates = chatStates;
    }

    public String getAnswer(long chatId, @NotNull String text) {
        try {
            if (text.charAt(0) == '/') {
                var split = text.split(" ", 2);
                var command = commands.get(split[0]);
                if (command == null) {
                    return "Unknown command. Type /help for commands list.";
                }
                return split.length == 1 ? command.run(chatId)
                    : command.isAwaitingArgs() ? command.handleNext(chatId, split[1])
                    : "This command doesn't need arguments.";
            } else {
                return chatStates.getCurrentCommand(chatId).handleNext(chatId, text);
            }
        } catch (UserIsNotRegisteredException e) {
            chatStates.clearState(chatId);
            return "You should to register at first.";
        }
    }

    @Override
    public SendMessage process(Update update) {
        var chatId = update.message().chat().id();
        var text = update.message().text();
        return new SendMessage(
            chatId,
            text == null ? "Only text messages is allowed" : getAnswer(chatId, text)
        );
    }
}
