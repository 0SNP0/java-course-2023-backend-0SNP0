package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.repository.ChatRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class JdbcChatRepository extends JdbcRepository<Chat> implements ChatRepository {
    private static final String TABLE_NAME = "chats";

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Chat.class, TABLE_NAME);
    }

    @Override
    @Transactional
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
    @Transactional
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
