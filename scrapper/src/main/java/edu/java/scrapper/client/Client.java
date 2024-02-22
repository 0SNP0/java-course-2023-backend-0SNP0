package edu.java.scrapper.client;

import reactor.core.publisher.Mono;

public interface Client<T> {
    Mono<T> fetch(String path);
}
