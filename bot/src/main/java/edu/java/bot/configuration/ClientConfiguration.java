package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    final ApplicationConfig config;

    @Bean ScrapperClient scrapperClient() {
        return new ScrapperClient(config.scrapperApi());
    }
}