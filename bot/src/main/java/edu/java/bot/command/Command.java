package edu.java.bot.command;

import edu.java.bot.exception.UserIsNotRegisteredException;

public interface Command {
    String run(long chatId) throws UserIsNotRegisteredException;

    String handleNext(long chatId, String message);
}
