package edu.java.bot.commands;

import edu.java.bot.utils.Validation;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand extends CommandWithArgs {

    protected TrackCommand() {
        super("/track", "Start to track link", "Enter your link");
    }

    @Override public String handleNext(long chatId, String message) {
        states.clearState(chatId);
        if (Validation.isLink(message)) {
            return linksRepository.addLink(chatId, message) ? "Done" : "Link has already added";
        } else {
            return "Incorrect URL";
        }
    }
}
