package edu.java.bot.repository;

import edu.java.bot.exception.UserIsNotRegisteredException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//Remains for tests
public class LinksMemoryRepository implements LinksRepository {
    private final ConcurrentMap<Long, Set<String>> map = new ConcurrentHashMap<>();

    @Override
    public boolean register(long chatId) {
        if (map.containsKey(chatId)) {
            return false;
        }
        map.put(chatId, new HashSet<>());
        return true;
    }

    @Override
    public boolean isRegistered(long chatId) {
        return map.containsKey(chatId);
    }

    @Override
    public boolean addLink(long chatId, String link) {
        return map.get(chatId).add(link);
    }

    @Override
    public List<String> getLinks(long chatId) throws UserIsNotRegisteredException {
        var set = map.get(chatId);
        if (set == null) {
            throw new UserIsNotRegisteredException();
        }
        return set.stream().toList();
    }

    @Override
    public boolean removeLink(long chatId, String link) {
        return map.get(chatId).remove(link);
    }
}
