package edu.java.scrapper.service;

import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.LinkResponse;
import edu.java.models.dto.ListLinksResponse;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import java.time.Duration;
import java.util.Collection;

public interface LinkService {

    ListLinksResponse getLinks(Long chatId);

    LinkResponse addLink(Long chatId, AddLinkRequest request);

    LinkResponse removeLink(Long chatId, RemoveLinkRequest request);

    void updateLink(Link link);

    Collection<Link> getLinks(Duration duration);

    Collection<Chat> getChats(Long linkId);
}
