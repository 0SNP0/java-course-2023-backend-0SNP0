package edu.java.scrapper.repository;

import java.net.URI;
import java.util.List;

public interface LinksRepository {

    boolean addLink(Long chatId, URI url);

    List<URI> getLinks(Long chatId);

    boolean removeLink(Long chatId, URI url);
}
