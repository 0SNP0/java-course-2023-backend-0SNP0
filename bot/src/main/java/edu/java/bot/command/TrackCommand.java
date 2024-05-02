package edu.java.bot.command;

import edu.java.bot.exception.UnsupportedLinkException;
import edu.java.bot.util.Validation;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand extends CommandWithArgs {

    protected TrackCommand() {
        super("/track", "Start to track link", "Enter your link");
    }

    @Override public String handleNext(long chatId, String message) {
        states.clearState(chatId);
        if (Validation.isLink(message)) {
            try {
                return linksRepository.addLink(chatId, message) ? "Done" : "Link has already added";
            } catch (UnsupportedLinkException e) {
                return "Unsupported link";
            }
        } else {
            return "Incorrect URL";
        }
    }
}
