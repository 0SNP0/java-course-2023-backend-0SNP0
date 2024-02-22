package edu.java.scrapper.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowResponse(@JsonProperty("items") List<Question> items) {

    public record Question(
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
    ) {
    }
}
