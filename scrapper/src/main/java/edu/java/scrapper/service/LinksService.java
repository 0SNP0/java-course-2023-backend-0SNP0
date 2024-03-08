package edu.java.scrapper.service;

import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.LinkResponse;
import edu.java.models.dto.ListLinksResponse;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.scrapper.exception.LinkAlreadyTrackingException;
import edu.java.scrapper.exception.LinkNotTrackingException;
import edu.java.scrapper.repository.LinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinksService {
    private final LinksRepository linksRepository;

    public ListLinksResponse getLinks(Long chatId) {
        var result = linksRepository.getLinks(chatId);
        return new ListLinksResponse(
            result.stream().map(url -> new LinkResponse(chatId, url)).toList(),
            result.size()
        );
    }

    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        if (!linksRepository.addLink(chatId, request.link())) {
            throw new LinkAlreadyTrackingException();
        }
        return new LinkResponse(chatId, request.link());
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        if (!linksRepository.removeLink(chatId, request.link())) {
            throw new LinkNotTrackingException();
        }
        return new LinkResponse(chatId, request.link());
    }
}
