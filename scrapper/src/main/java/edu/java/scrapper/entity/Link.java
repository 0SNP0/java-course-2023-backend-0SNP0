package edu.java.scrapper.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Link {
    private Long linkId;
    private URI url;
    private OffsetDateTime updatedAt;
    private String client;
}
