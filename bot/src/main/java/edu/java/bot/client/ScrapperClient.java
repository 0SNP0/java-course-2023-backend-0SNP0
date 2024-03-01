package edu.java.bot.client;

import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.LinkResponse;
import edu.java.models.dto.ListLinksResponse;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.models.exception.ApiErrorException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private static final String CHAT_API_URI = "/tg-chat/{id}";
    private static final String LINK_API_URI = "/links";
    private static final String CHAT_ID_HEADER = "Tg-Chat-Id";

    private final WebClient client;

    public ScrapperClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Mono<ResponseEntity<Void>> registerChat(Long tgChatId) {
        return client.post()
            .uri(CHAT_API_URI, tgChatId)
            .retrieve()
            .onStatus(HttpStatus.CONFLICT::equals, ApiErrorException::of)
            .toBodilessEntity();
    }

    public Mono<ResponseEntity<Void>> checkChat(Long tgChatId) {
        return client.get()
            .uri(CHAT_API_URI, tgChatId)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, ApiErrorException::of)
            .toBodilessEntity();
    }

    public Mono<ResponseEntity<Void>> deleteChat(Long tgChatId) {
        return client.delete()
            .uri(CHAT_API_URI, tgChatId)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, ApiErrorException::of)
            .toBodilessEntity();
    }

    public Mono<ResponseEntity<ListLinksResponse>> getLinks(Long tgChatId) {
        return client.get()
            .uri(LINK_API_URI)
            .header(CHAT_ID_HEADER, tgChatId.toString())
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, ApiErrorException::of)
            .toEntity(ListLinksResponse.class);
    }

    public Mono<ResponseEntity<LinkResponse>> addLink(Long tgChatId, AddLinkRequest request) {
        return client.post()
            .uri(LINK_API_URI)
            .header(CHAT_ID_HEADER, tgChatId.toString())
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatus.CONFLICT::equals, ApiErrorException::of)
            .toEntity(LinkResponse.class);
    }

    public Mono<ResponseEntity<LinkResponse>> removeLink(Long tgChatId, RemoveLinkRequest request) {
        return client.method(HttpMethod.DELETE)
            .uri(LINK_API_URI)
            .header(CHAT_ID_HEADER, tgChatId.toString())
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, ApiErrorException::of)
            .toEntity(LinkResponse.class);
    }
}
