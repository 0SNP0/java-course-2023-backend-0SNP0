package edu.java.bot.repository;

import edu.java.bot.exception.UserIsNotRegisteredException;
import java.util.List;

public interface LinksRepository {
    boolean register(long chatId);

    boolean isRegistered(long chatId);

    boolean addLink(long chatId, String link);

    List<String> getLinks(long chatId) throws UserIsNotRegisteredException;

    boolean removeLink(long chatId, String link);
}
