package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import org.springframework.dao.EmptyResultDataAccessException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.LongStream;
import static org.assertj.core.api.Assertions.*;

public class JdbcChatRepositoryTest extends JdbcRepositoryTest<JdbcChatRepository, Chat> {
    private static final List<Chat> testEntities = LongStream.range(1, 3)
        .mapToObj(x -> new Chat().setChatId(x).setRegisteredAt(OffsetDateTime.now())).toList();

    public JdbcChatRepositoryTest() {
        super(JdbcChatRepository::new, testEntities);
    }

    @TestTransactionalRollback
    void get() {
        repository.add(testEntities.getFirst());
        assertThat(repository.get(testEntities.getFirst())).isNotNull();
    }

    @TestTransactionalRollback
    void getNotExisted() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
            .isThrownBy(() -> repository.get(testEntities.getFirst()));
    }
}
