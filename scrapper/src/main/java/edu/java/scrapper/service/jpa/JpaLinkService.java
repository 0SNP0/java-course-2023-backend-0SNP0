package edu.java.scrapper.service.jpa;

import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.LinkResponse;
import edu.java.models.dto.ListLinksResponse;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.scrapper.client.UrlSupporter;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.Mapping;
import edu.java.scrapper.entity.MappingId;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackingException;
import edu.java.scrapper.exception.LinkNotTrackingException;
import edu.java.scrapper.exception.UnsupportedLinkException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaMappingRepository;
import edu.java.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaMappingRepository mappingRepository;
    private final List<UrlSupporter> urlSupporters;

    private void checkRegistration(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotRegisteredException();
        }
    }

    @Override
    public ListLinksResponse getLinks(Long chatId) {
        checkRegistration(chatId);
        var result = mappingRepository.findAllLinksByChatId(chatId);
        return new ListLinksResponse(
            result.stream().map(link -> new LinkResponse(chatId, link.getUrl())).toList(),
            result.size()
        );
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        checkRegistration(chatId);
        if (urlSupporters.stream().noneMatch(x -> x.supports(request.link()))) {
            throw new UnsupportedLinkException();
        }
        Link link;
        try {
            link = linkRepository.save(new Link()
                .setUrl(request.link())
                .setUpdatedAt(OffsetDateTime.now())
            );
        } catch (DataIntegrityViolationException e) {
            link = linkRepository.findByUrl(request.link());
        }
        var mapping = new MappingId().setChatId(chatId).setLinkId(link.getLinkId());
        if (mappingRepository.existsById(mapping)) {
            throw new LinkAlreadyTrackingException();
        }
        mappingRepository.save(new Mapping().setId(mapping));
        return new LinkResponse(link.getLinkId(), link.getUrl());
    }

    @Override
    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        checkRegistration(chatId);
        var link = linkRepository.findByUrl(request.link());
        if (link == null) {
            throw new LinkNotTrackingException();
        }
        var mapping = new MappingId().setChatId(chatId).setLinkId(link.getLinkId());
        if (mappingRepository.existsById(mapping)) {
            throw new LinkNotTrackingException();
        }
        mappingRepository.deleteById(mapping);
        linkRepository.removeIfUnusedById(link.getLinkId());
        return new LinkResponse(link.getLinkId(), link.getUrl());
    }

    @Override
    public void updateLink(Link link) {
        linkRepository.save(link);
    }

    @Override
    public Collection<Link> getLinks(Duration duration) {
        return linkRepository.findAllNotNewer(
            OffsetDateTime.now().minusSeconds(duration.getSeconds())
        );
    }

    @Override
    public Collection<Chat> getChats(Long linkId) {
        return mappingRepository.findAllChatsByLinkId(linkId);
    }
}
