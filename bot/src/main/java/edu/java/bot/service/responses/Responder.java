package edu.java.bot.service.responses;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Responder {
    SendMessage process(Update update);
}
