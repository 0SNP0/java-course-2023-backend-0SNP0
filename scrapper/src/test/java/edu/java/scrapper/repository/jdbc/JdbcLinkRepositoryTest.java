package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;
import static org.assertj.core.api.Assertions.*;

public class JdbcLinkRepositoryTest extends JdbcRepositoryTest<JdbcLinkRepository, Link> {
    private static final List<Link> testEntities = LongStream.range(1, 3)
        .mapToObj(x -> new Link().setLinkId(x)
            .setUrl(URI.create("http://u.r.l/%d".formatted(x)))
            .setUpdatedAt(OffsetDateTime.now())
            .setClient("test_client")
        ).toList();

    protected JdbcLinkRepositoryTest() {
        super(JdbcLinkRepository::new, testEntities);
    }

    @TestTransactionalRollback
    void update() {
        var link = repository.add(testEntities.getFirst());
        var newDate = OffsetDateTime.now();
        repository.update(link.setUpdatedAt(newDate));
        assertThat(repository.get(link.getUrl()).getUpdatedAt())
            .isCloseTo(newDate, within(1, ChronoUnit.MICROS));
    }

    @TestTransactionalRollback
    void map() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        assertThat(repository.findAll(chat.getChatId())).doesNotContain(link);
        repository.map(chat.getChatId(), link.getLinkId());
        assertThat(repository.findAll(chat.getChatId())).contains(link);
    }

    @TestTransactionalRollback
    void mapExisted() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        repository.map(chat.getChatId(), link.getLinkId());
        assertThatExceptionOfType(DuplicateKeyException.class)
            .isThrownBy(() -> repository.map(chat.getChatId(), link.getLinkId()));
    }

    @TestTransactionalRollback
    void unmap() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        repository.map(chat.getChatId(), link.getLinkId());
        repository.unmap(chat.getChatId(), link.getLinkId());
        assertThat(repository.findAll(chat.getChatId())).doesNotContain(link);
    }

    @TestTransactionalRollback
    void unmapNotExisted() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
            .isThrownBy(() -> repository.unmap(chat.getChatId(), link.getLinkId()));
    }

    @TestTransactionalRollback
    void removeIfUnused() {
        var link = repository.add(testEntities.getFirst());
        repository.removeIfUnused(link);
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
            .isThrownBy(() -> repository.get(link.getUrl()));
    }

    @TestTransactionalRollback
    void removeOnlyUnused() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        repository.map(chat.getChatId(), link.getLinkId());
        repository.removeIfUnused(link);
        assertThat(repository.get(link.getUrl())).isEqualTo(link);
    }

    @TestTransactionalRollback
    void chatsForLink() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        chatRepository.add(
            new Chat().setChatId(2L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        repository.map(chat.getChatId(), link.getLinkId());
        assertThat(repository.chatsForLink(link.getLinkId())).containsOnly(chat);
    }

    @TestTransactionalRollback
    void findAllByChat() {
        var chatRepository = new JdbcChatRepository(jdbcTemplate);
        var chat = chatRepository.add(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        var link = repository.add(testEntities.getFirst());
        repository.add(testEntities.getLast());
        repository.map(chat.getChatId(), link.getLinkId());
        assertThat(repository.findAll(chat.getChatId())).containsOnly(link);
    }

    @TestTransactionalRollback
    void findAllByDuration() {
        var link = repository.add(testEntities.getFirst().setUpdatedAt(
            OffsetDateTime.now().minusSeconds(10)
        ));
        repository.add(testEntities.getLast().setUpdatedAt(OffsetDateTime.now()));
        assertThat(repository.findAll(Duration.of(10, ChronoUnit.SECONDS))).containsOnly(link);
    }
}
