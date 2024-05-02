package edu.java.scrapper.client;

import edu.java.common.models.dto.LinkUpdateRequest;
import edu.java.common.models.exception.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private static final String UPDATES_API = "/updates";
    private final WebClient client;
    @Autowired
    @Qualifier("bot")
    private RetryTemplate retryTemplate;

    public BotClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Mono<ResponseEntity<Void>> notify(LinkUpdateRequest request) {
        return retryTemplate.execute(ctx -> client.post()
            .uri(UPDATES_API)
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, ApiErrorException::of)
            .toBodilessEntity());
    }
}
