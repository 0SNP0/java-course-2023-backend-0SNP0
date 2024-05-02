package edu.java.scrapper.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "mapping")
public class Mapping {
    @EmbeddedId
    private MappingId id;
}
