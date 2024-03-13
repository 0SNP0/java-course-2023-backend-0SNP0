package edu.java.scrapper.controller;

import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.LinkResponse;
import edu.java.models.dto.ListLinksResponse;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinksController {
    private static final String CHAT_ID_HEADER = "Tg-Chat-Id";
    private final LinkService service;

    @GetMapping
    public ListLinksResponse get(@RequestHeader(CHAT_ID_HEADER) Long chatId) {
        return service.getLinks(chatId);
    }

    @PostMapping
    public LinkResponse add(
        @RequestHeader(CHAT_ID_HEADER) Long chatId,
        @RequestBody AddLinkRequest request
    ) {
        return service.addLink(chatId, request);
    }

    @DeleteMapping
    public LinkResponse remove(
        @RequestHeader(CHAT_ID_HEADER) Long chatId,
        @RequestBody RemoveLinkRequest request
    ) {
        return service.removeLink(chatId, request);
    }
}
