package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public abstract class JdbcRepositoryTest<R extends JdbcRepository<T>, T> extends IntegrationTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    protected R repository;
    private final Function<JdbcTemplate, R> repoConstructor;
    protected final List<T> testEntities;

    protected JdbcRepositoryTest(Function<JdbcTemplate, R> repoConstructor, List<T> testEntities) {
        this.repoConstructor = repoConstructor;
        this.testEntities = testEntities;
    }

    @BeforeEach
    public void init() {
        repository = repoConstructor.apply(jdbcTemplate);
    }

    @TestTransactionalRollback
    void add() {
        assertThat(repository.findAll()).isEmpty();
        repository.add(testEntities.getFirst());
        assertThat(repository.findAll()).isNotEmpty();
    }

    @TestTransactionalRollback
    void addExisted() {
        repository.add(testEntities.getFirst());
        assertThatExceptionOfType(DuplicateKeyException.class)
            .isThrownBy(() -> repository.add(testEntities.getFirst()));
    }

    @TestTransactionalRollback
    void remove() {
        var entity = repository.add(testEntities.getFirst());
        repository.remove(entity);
        assertThat(repository.findAll()).isEmpty();
    }

    @TestTransactionalRollback
    void removeNotExisted() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
            .isThrownBy(() -> repository.remove(testEntities.getFirst()));
    }

    @TestTransactionalRollback
    void FindAllTest() {
        testEntities.forEach(repository::add);
        assertThat(repository.findAll()).size().isEqualTo(testEntities.size());
    }
}
