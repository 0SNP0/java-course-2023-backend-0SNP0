package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Link findByUrl(URI url);

    @Query("""
        select count(l) > 0
          from Link l
         where l.linkId = ?1 and l.linkId not in (
            select m.id.linkId
              from Mapping m
        )
        """)
    boolean isUnusedWithId(Long linkId);

    @Query("""
        select l
          from Link l
         where l.updatedAt < ?1
        """)
    List<Link> findAllNotNewer(OffsetDateTime dateTime);
}
