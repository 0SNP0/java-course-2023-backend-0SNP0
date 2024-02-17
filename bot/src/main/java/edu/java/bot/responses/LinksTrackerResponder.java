package edu.java.bot.responses;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.AbstractCommand;
import edu.java.bot.commands.Commands;
import edu.java.bot.repositories.ChatStateRepository;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LinksTrackerResponder implements Responder {
    final Map<String, AbstractCommand> commands;
    final ChatStateRepository chatStates;

    public LinksTrackerResponder(@NotNull Commands commands, ChatStateRepository chatStates) {
        this.commands = commands.getList().stream().collect(Collectors.toMap(BotCommand::command, x -> x));
        this.chatStates = chatStates;
    }

    public String getAnswer(long chatId, String text) {
        try {
            if (text.charAt(0) == '/') {
                var split = text.split(" ", 2);
                var command = commands.get(split[0]);
                if (command == null) {
                    return "Unknown command. Type /help for commands list.";
                }
                if (split.length == 2 && !command.isAwaitingArgs()) {
                    return "This command doesn't need arguments.";
                }
                return split.length == 1 ? command.run(chatId) : command.handleNext(chatId, split[1]);
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
        return new SendMessage(chatId, getAnswer(chatId, text));
    }
}