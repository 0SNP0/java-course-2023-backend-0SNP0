package edu.java.scrapper.client;

import edu.java.scrapper.entity.ClientResponse;
import reactor.core.publisher.Mono;

public interface Client<T extends ClientResponse> extends UrlSupporter {
    Mono<T> fetch(String path);
}
