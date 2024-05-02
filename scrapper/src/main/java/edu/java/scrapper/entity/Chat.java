package edu.java.scrapper.entity;

import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Chat {
    private Long chatId;
    private OffsetDateTime registeredAt;
}
