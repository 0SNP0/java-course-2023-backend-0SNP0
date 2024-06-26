package edu.java.bot.command;

import edu.java.bot.exception.UserIsNotRegisteredException;
import edu.java.bot.repository.LinksRepository;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends CommandWithoutArgs {
    final transient LinksRepository linksRepository;

    protected ListCommand(LinksRepository linksRepository) {
        super(
            "/list",
            "List of tracked links",
            "List of tracked links: \n"
        );
        this.linksRepository = linksRepository;
    }

    @Override public String run(long chatId) throws UserIsNotRegisteredException {
        var result = linksRepository.getLinks(chatId);
        return result.isEmpty() ? "No tracked links" : answer + String.join("\n", result);
    }
}
