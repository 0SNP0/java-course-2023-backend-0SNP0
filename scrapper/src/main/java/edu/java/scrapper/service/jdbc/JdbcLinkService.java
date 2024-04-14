package edu.java.scrapper.service.jdbc;

import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.LinkResponse;
import edu.java.models.dto.ListLinksResponse;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.scrapper.client.UrlSupporter;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackingException;
import edu.java.scrapper.exception.LinkNotTrackingException;
import edu.java.scrapper.exception.UnsupportedLinkException;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.repository.jdbc.LinkRepository;
import edu.java.scrapper.service.LinkService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final List<UrlSupporter> urlSupporters;

    private void checkRegistration(Long chatId) {
        try {
            chatRepository.get(new Chat().setChatId(chatId));
        } catch (EmptyResultDataAccessException e) {
            throw new ChatNotRegisteredException();
        }
    }

    @Override
    @Transactional
    public ListLinksResponse getLinks(Long chatId) {
        checkRegistration(chatId);
        var result = linkRepository.findAll(chatId);
        return new ListLinksResponse(
            result.stream().map(link -> new LinkResponse(link.getLinkId(), link.getUrl())).toList(),
            result.size()
        );
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        checkRegistration(chatId);
        var client = urlSupporters.stream().filter(x -> x.supports(request.link())).findFirst();
        if (client.isEmpty()) {
            throw new UnsupportedLinkException();
        }
        Link link;
        try {
            link = linkRepository.add(new Link()
                .setUrl(request.link())
                .setUpdatedAt(OffsetDateTime.now())
                .setClient(client.get().name())
            );
        } catch (DuplicateKeyException e) {
            link = linkRepository.get(request.link());
        }
        try {
            linkRepository.map(chatId, link.getLinkId());
        } catch (DuplicateKeyException e) {
            throw new LinkAlreadyTrackingException();
        }

        return new LinkResponse(link.getLinkId(), link.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        checkRegistration(chatId);
        try {
            var link = linkRepository.get(request.link());
            linkRepository.unmap(chatId, link.getLinkId());
            linkRepository.removeIfUnused(link);
            return new LinkResponse(link.getLinkId(), link.getUrl());
        } catch (EmptyResultDataAccessException e) {
            throw new LinkNotTrackingException();
        }
    }

    @Override
    public void updateLink(Link link) {
        linkRepository.update(link);
    }

    @Override
    public Collection<Link> getLinks(Duration duration) {
        return linkRepository.findAll(duration);
    }

    @Override
    public Collection<Chat> getChats(Long linkId) {
        return linkRepository.chatsForLink(linkId);
    }
}
