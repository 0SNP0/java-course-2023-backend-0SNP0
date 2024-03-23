package edu.java.scrapper.repository.jdbc;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class JdbcRepository<T> implements Repository<T> {
    protected final JdbcTemplate jdbcTemplate;
    private final Class<T> entityClass;
    private final String tableName;

    @Override
    @Transactional
    public Collection<T> findAll() {
        return jdbcTemplate.query(
            """
                select *
                  from %s
                """.formatted(tableName),
            new BeanPropertyRowMapper<>(entityClass)
        );
    }
}
