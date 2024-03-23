package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;

public interface LinkRepository extends Repository<Link> {

    void update(Link link);

    void map(Long chatId, Long linkId);

    void unmap(Long chatId, Long linkId);

    void removeIfUnused(Link link);

    Link get(URI url);

    Collection<Chat> chatsForLink(Long linkId);

    Collection<Link> findAll(Long chatId);

    Collection<Link> findAll(Duration duration);
}
