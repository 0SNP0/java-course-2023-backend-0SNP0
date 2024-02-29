package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    final ApplicationConfig config;

    public ClientConfiguration(ApplicationConfig config) {
        this.config = config;
    }

    @Bean
    public GithubClient githubClient() {
        if (config.clients() == null || config.clients().github() == null) {
            return new GithubClient();
        }
        return new GithubClient(config.clients().github());
    }

    @Bean StackoverflowClient stackoverflowClient() {
        if (config.clients() == null || config.clients().stackoverflow() == null) {
            return new StackoverflowClient();
        }
        return new StackoverflowClient(config.clients().stackoverflow());
    }
}
