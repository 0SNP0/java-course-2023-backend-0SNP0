package edu.java.scrapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Accessors(chain = true)
@Data
@Embeddable
@NoArgsConstructor
public class MappingId implements Serializable {
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "link_id", nullable = false)
    private Long linkId;
}
