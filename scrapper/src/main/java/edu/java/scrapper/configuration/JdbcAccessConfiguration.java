package edu.java.scrapper.configuration;

import edu.java.scrapper.client.UrlSupporter;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.repository.jdbc.LinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {
    private final JdbcTemplate jdbcTemplate;
    private final List<UrlSupporter> urlSupporters;

    @Bean
    public ChatRepository chatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public ChatService chatService() {
        return new JdbcChatService(chatRepository());
    }

    @Bean
    public LinkService linkService() {
        return new JdbcLinkService(chatRepository(), linkRepository(), urlSupporters);
    }
}
