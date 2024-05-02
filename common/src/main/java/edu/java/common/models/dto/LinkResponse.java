package edu.java.common.models.dto;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url) {
}
