package edu.java.scrapper.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "links")
public class Link {
    @Column(name = "link_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;
    @Column(name = "url", columnDefinition = "varchar(256)", nullable = false, unique = true)
    private URI url;
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
