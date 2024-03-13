package edu.java.scrapper.entity;

import java.time.OffsetDateTime;

public interface ClientResponse {
    String description();

    OffsetDateTime updatedAt();
}
