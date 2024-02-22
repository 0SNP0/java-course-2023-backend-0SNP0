package edu.java.scrapper.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubResponse(
    @JsonProperty("full_name") String fullName,
    @JsonProperty("updated_at") OffsetDateTime updatedAt
) {
}
