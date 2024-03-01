package edu.java.scrapper.repository;

import edu.java.scrapper.exception.ChatNotRegisteredException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryRepository implements LinksRepository, ChatRepository {
    private final ConcurrentMap<Long, Set<URI>> map = new ConcurrentHashMap<>();

    @Override
    public boolean register(Long chatId) {
        if (map.containsKey(chatId)) {
            return false;
        }
        map.put(chatId, new HashSet<>());
        return true;
    }

    @Override
    public boolean isRegistered(Long chatId) {
        return map.containsKey(chatId);
    }

    @Override
    public boolean delete(Long chatId) {
        return map.remove(chatId) != null;
    }

    @Override
    public boolean addLink(Long chatId, URI url) {
        try {
            return map.get(chatId).add(url);
        } catch (NullPointerException e) {
            throw new ChatNotRegisteredException();
        }
    }

    @Override
    public List<URI> getLinks(Long chatId) {
        var set = map.get(chatId);
        if (set == null) {
            throw new ChatNotRegisteredException();
        }
        return set.stream().toList();
    }

    @Override
    public boolean removeLink(Long chatId, URI url) {
        try {
            return map.get(chatId).remove(url);
        } catch (NullPointerException e) {
            throw new ChatNotRegisteredException();
        }
    }
}
