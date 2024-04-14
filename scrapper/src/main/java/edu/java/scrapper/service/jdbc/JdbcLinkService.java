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
import edu.java.scrapper.repository.ChatRepository;
import edu.java.scrapper.repository.LinkRepository;
import edu.java.scrapper.service.LinkService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final ChatRepository chatRepository;
    private final LinkRepository linksRepository;
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
        var result = linksRepository.findAll(chatId);
        return new ListLinksResponse(
            result.stream().map(link -> new LinkResponse(chatId, link.getUrl())).toList(),
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
            link = linksRepository.add(new Link()
                .setUrl(request.link())
                .setUpdatedAt(OffsetDateTime.now())
                .setClient(client.get().name())
            );
        } catch (DuplicateKeyException e) {
            link = linksRepository.get(request.link());
        }
        try {
            linksRepository.map(chatId, link.getLinkId());
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
            var link = linksRepository.get(request.link());
            linksRepository.unmap(chatId, link.getLinkId());
            linksRepository.removeIfUnused(link);
            return new LinkResponse(link.getLinkId(), link.getUrl());
        } catch (EmptyResultDataAccessException e) {
            throw new LinkNotTrackingException();
        }
    }

    @Override
    public void updateLink(Link link) {
        linksRepository.update(link);
    }

    @Override
    public Collection<Link> getLinks(Duration duration) {
        return linksRepository.findAll(duration);
    }

    @Override
    public Collection<Chat> getChats(Long linkId) {
        return linksRepository.chatsForLink(linkId);
    }
}
