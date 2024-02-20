package edu.java.bot.repositories;

import edu.java.bot.exceptions.UserIsNotRegisteredException;
import java.util.List;

public interface LinksRepository {
    boolean register(long chatId);

    boolean isRegistered(long chatId);

    boolean addLink(long chatId, String link);

    List<String> getLinks(long chatId) throws UserIsNotRegisteredException;

    boolean removeLink(long chatId, String link);
}
