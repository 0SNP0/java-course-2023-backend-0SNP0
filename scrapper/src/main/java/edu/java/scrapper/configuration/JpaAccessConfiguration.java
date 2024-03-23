package edu.java.scrapper.configuration;

import edu.java.scrapper.client.UrlSupporter;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaMappingRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.jpa.JpaChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@RequiredArgsConstructor
public class JpaAccessConfiguration {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaMappingRepository mappingRepository;
    private final List<UrlSupporter> urlSupporters;

    @Bean
    public ChatService chatService() {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public LinkService linkService() {
        return new JpaLinkService(chatRepository, linkRepository, mappingRepository, urlSupporters);
    }
}
