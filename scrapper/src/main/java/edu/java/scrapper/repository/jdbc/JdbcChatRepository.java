package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.repository.ChatRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Chat> findAll() {
        return jdbcTemplate.query(
            """
                select *
                  from chats
                """,
            new BeanPropertyRowMapper<>(Chat.class)
        );
    }

    @Override
    public Chat add(Chat chat) {
        return jdbcTemplate.queryForObject(
            """
            insert into chats (chat_id, registered_at)
            values (?, ?)
            returning *
            """,
            new BeanPropertyRowMapper<>(Chat.class),
            chat.getChatId(),
            chat.getRegisteredAt()
        );
    }

    @Override
    public Chat remove(Chat chat) {
        return jdbcTemplate.queryForObject(
            """
            delete from chats
            where chat_id = ?
            returning *
            """,
            new BeanPropertyRowMapper<>(Chat.class),
            chat.getChatId()
        );
    }

    @Override
    public Chat get(Chat chat) {
        return jdbcTemplate.queryForObject(
            """
            select *
              from chats
             where chat_id = ?
            """,
            new BeanPropertyRowMapper<>(Chat.class),
            chat.getChatId()
        );
    }
}
