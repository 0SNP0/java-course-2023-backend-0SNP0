package edu.java.scrapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "chats")
public class Chat {
    @Column(name = "chat_id")
    @Id
    private Long chatId;
    @Column(name = "registered_at")
    private OffsetDateTime registeredAt;
}
