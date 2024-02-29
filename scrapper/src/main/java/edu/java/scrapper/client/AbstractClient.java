package edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class AbstractClient<T> implements Client<T> {
    protected final WebClient client;
    protected Class<T> responseEntity;

    protected AbstractClient(String apiUrl, Class<T> responseEntity) {
        this.client = WebClient.create(apiUrl);
        this.responseEntity = responseEntity;
    }

    protected abstract String uri(String path);

    @Override
    public Mono<T> fetch(String path) {
        return client.get().uri(uri(path)).retrieve().bodyToMono(responseEntity);
    }
}
