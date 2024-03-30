package edu.java.scrapper.service.jpa;

import edu.java.common.models.dto.AddLinkRequest;
import edu.java.common.models.dto.LinkResponse;
import edu.java.common.models.dto.ListLinksResponse;
import edu.java.common.models.dto.RemoveLinkRequest;
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
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
            result.stream().map(link -> new LinkResponse(link.getLinkId(), link.getUrl())).toList(),
            result.size()
        );
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        checkRegistration(chatId);
        if (urlSupporters.stream().noneMatch(x -> x.supports(request.link()))) {
            throw new UnsupportedLinkException();
        }
        var link = linkRepository.findByUrl(request.link());
        if (link == null) {
            link = linkRepository.save(new Link()
                .setUrl(request.link())
                .setUpdatedAt(OffsetDateTime.now())
            );
        }
        var mapping = new MappingId().setChatId(chatId).setLinkId(link.getLinkId());
        if (mappingRepository.existsById(mapping)) {
            throw new LinkAlreadyTrackingException();
        }
        mappingRepository.save(new Mapping().setId(mapping));
        return new LinkResponse(link.getLinkId(), link.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        checkRegistration(chatId);
        var link = linkRepository.findByUrl(request.link());
        if (link == null) {
            throw new LinkNotTrackingException();
        }
        var mapping = new MappingId().setChatId(chatId).setLinkId(link.getLinkId());
        if (!mappingRepository.existsById(mapping)) {
            throw new LinkNotTrackingException();
        }
        mappingRepository.deleteById(mapping);
        if (linkRepository.isUnusedWithId(link.getLinkId())) {
            linkRepository.delete(link);
        }
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
