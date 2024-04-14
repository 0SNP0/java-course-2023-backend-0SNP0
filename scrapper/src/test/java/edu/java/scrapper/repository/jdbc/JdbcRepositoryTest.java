package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.repository.IntegrationTest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.function.Function;
import edu.java.scrapper.repository.JdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
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

    @JdbcTest
    void add() {
        assertThat(repository.findAll()).isEmpty();
        repository.add(testEntities.getFirst());
        assertThat(repository.findAll()).isNotEmpty();
    }

    @JdbcTest
    void addExisted() {
        repository.add(testEntities.getFirst());
        assertThatExceptionOfType(DuplicateKeyException.class)
            .isThrownBy(() -> repository.add(testEntities.getFirst()));
    }

    @JdbcTest
    void remove() {
        add();
        repository.remove(testEntities.getFirst());
        assertThat(repository.findAll()).isEmpty();
    }

    @JdbcTest
    void removeNotExisted() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
            .isThrownBy(() -> repository.remove(testEntities.getFirst()));
    }

    @JdbcTest
    void FindAllTest() {
        testEntities.forEach(repository::add);
        assertThat(repository.findAll()).size().isEqualTo(testEntities.size());
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Transactional
    @Rollback
    protected @interface JdbcTest {
    }
}
