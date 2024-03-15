package edu.java.scrapper.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationsTest extends IntegrationTest {
    Connection connection;

    @BeforeEach
    void init() throws SQLException {
        connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @ParameterizedTest
    @ValueSource(strings = {"chats", "links", "mapping"})
    void tableExists(String tableName) throws SQLException {
        assertThat(
            connection.getMetaData().getTables(null, null, tableName, null).next()
        ).isTrue();
    }
}
