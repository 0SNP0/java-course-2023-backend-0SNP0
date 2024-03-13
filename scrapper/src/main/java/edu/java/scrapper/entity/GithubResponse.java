package edu.java.scrapper.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubResponse(
    @JsonProperty("full_name") String fullName,
    @JsonProperty("updated_at") OffsetDateTime updatedAt
) implements ClientResponse {
    @Override
    public String description() {
        return "GitHub: %s".formatted(fullName);
    }
}
