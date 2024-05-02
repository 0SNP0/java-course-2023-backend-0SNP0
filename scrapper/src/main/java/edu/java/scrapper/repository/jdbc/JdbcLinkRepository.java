package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.repository.LinkRepository;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Link> findAll() {
        return jdbcTemplate.query(
            """
                select *
                  from links
                """,
            new BeanPropertyRowMapper<>(Link.class)
        );
    }

    @Override
    public Link add(Link link) {
        return jdbcTemplate.queryForObject(
            """
                insert into links (url, updated_at, client)
                values (?, ?, ?)
                returning *
                """,
            new BeanPropertyRowMapper<>(Link.class),
            link.getUrl().toString(),
            link.getUpdatedAt(),
            link.getClient()
        );
    }

    @Override
    public Link remove(Link link) {
        return jdbcTemplate.queryForObject(
            """
                delete from links
                where link_id = ?
                returning *
                """,
            new BeanPropertyRowMapper<>(Link.class),
            link.getLinkId()
        );
    }

    @Override
    public void update(Link link) {
        jdbcTemplate.update(
            """
                update links
                   set updated_at = ?
                 where link_id = ?
                """,
            link.getUpdatedAt(),
            link.getLinkId()
        );
    }

    @Override
    public void map(Long chatId, Long linkId) {
        jdbcTemplate.update(
            """
                insert into mapping (chat_id, link_id)
                values (?, ?)
                """,
            chatId,
            linkId
        );
    }

    @Override
    public void unmap(Long chatId, Long linkId) {
        jdbcTemplate.queryForObject(
            """
                delete from mapping
                 where chat_id = ? or link_id = ?
                returning *
                """,
            (rs, rowNum) -> null,
            chatId,
            linkId
        );
    }

    @Override
    public void removeIfUnused(Link link) {
        jdbcTemplate.update(
            """
                delete from links
                 where link_id = ? and link_id not in (
                    select link_id
                      from mapping
                )
                """,
            link.getLinkId()
        );
    }

    @Override
    public Link get(URI url) {
        return jdbcTemplate.queryForObject(
            """
                select *
                  from links
                 where url = ?
                """,
            new BeanPropertyRowMapper<>(Link.class),
            url.toString()
        );
    }

    @Override
    public Collection<Chat> chatsForLink(Long linkId) {
        return jdbcTemplate.query(
            """
                select *
                  from chats
                 where chat_id in (
                    select chat_id
                      from mapping
                     where link_id = ?
                )
                """,
            new BeanPropertyRowMapper<>(Chat.class),
            linkId
        );
    }

    @Override
    public Collection<Link> findAll(Long chatId) {
        return jdbcTemplate.query(
            """
                select *
                  from links
                 where link_id in (
                    select link_id
                      from mapping
                     where chat_id = ?
                )
                """,
            new BeanPropertyRowMapper<>(Link.class),
            chatId
        );
    }

    @Override
    public Collection<Link> findAll(Duration duration) {
        return jdbcTemplate.query(
            """
                select *
                  from links
                 where updated_at < ?
                """,
            new BeanPropertyRowMapper<>(Link.class),
            Timestamp.from(OffsetDateTime.now().minusSeconds(duration.getSeconds()).toInstant())
        );
    }
}
