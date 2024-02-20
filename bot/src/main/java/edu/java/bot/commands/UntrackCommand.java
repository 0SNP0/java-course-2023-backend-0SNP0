package edu.java.bot.commands;

import org.springframework.stereotype.Component;

@Component
public class UntrackCommand extends CommandWithArgs {

    protected UntrackCommand() {
        super(
            "/untrack",
            "Stop to track link",
            "Enter the link. /list to get a list of them."
        );
    }

    @Override public String handleNext(long chatId, String message) {
        states.clearState(chatId);
        return linksRepository.removeLink(chatId, message) ? "Done" : "Link hasn't added";
    }
}
