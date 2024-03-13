package edu.java.scrapper.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowResponse(
    @JsonProperty("items") List<Question> items
) implements ClientResponse {

    @Override
    public String description() {
        return "StackOverflow: %s".formatted(items.getFirst().title());
    }

    @Override
    public OffsetDateTime updatedAt() {
        return items.getFirst().lastActivityDate();
    }

    public record Question(
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
    ) {
    }
}
