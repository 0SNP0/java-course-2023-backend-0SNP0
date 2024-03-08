package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.models.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class LinkUpdateService {
    private final TelegramBot bot;

    public void sendNotifications(LinkUpdateRequest request) {
        for (var chatId : request.tgChatIds()) {
            log.info("Sending notification to chat %d".formatted(chatId));
            bot.execute(new SendMessage(
                chatId,
                "The link has been updated: %s\n%s".formatted(request.url(), request.description())
            ));
        }
    }
}
