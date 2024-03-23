package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.Mapping;
import edu.java.scrapper.entity.MappingId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaMappingRepository extends JpaRepository<Mapping, MappingId> {
    @Query("""
        select l
          from Link l
         where l.linkId in (
            select m.id.linkId
              from Mapping m
             where m.id.chatId = ?1
        )
        """)
    List<Link> findAllLinksByChatId(Long chatId);

    @Query("""
        select c
          from Chat c
         where c.chatId in (
            select m.id.chatId
              from Mapping m
             where m.id.linkId = ?1
        )
        """)
    List<Chat> findAllChatsByLinkId(Long linkId);
}
