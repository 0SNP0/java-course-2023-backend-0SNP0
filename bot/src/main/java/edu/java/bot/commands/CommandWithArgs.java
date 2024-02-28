package edu.java.bot.commands;

import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.repositories.LinksRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommandWithArgs extends AbstractCommand {
    @Autowired protected transient LinksRepository linksRepository;

    protected CommandWithArgs(String command, String description, String answer) {
        super(command, description, answer, true);
    }

    @Override public String run(long chatId) throws UserIsNotRegisteredException {
        if (!linksRepository.isRegistered(chatId)) {
            throw new UserIsNotRegisteredException();
        }
        states.setState(chatId, this);
        return answer;
    }
}
