package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Commands;
import edu.java.bot.service.responses.Responder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class BotListener implements UpdatesListener {

    private final TelegramBot bot;
    private final Responder responder;
    private final Commands commands;

    @EventListener(ApplicationReadyEvent.class)
    void start() {
        bot.execute(new SetMyCommands(commands.asArray()));
        bot.setUpdatesListener(this, e -> log.debug(e.response().toString()));
    }

    @Override
    public int process(@NotNull List<Update> list) {
        list.forEach(update -> {
            if (update.message() == null) {
                log.debug(update);
                return;
            }
            bot.execute(responder.process(update));
        });
        return CONFIRMED_UPDATES_ALL;
    }
}
