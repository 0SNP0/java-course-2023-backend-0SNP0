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
        var link = config.clients().github();
        return link == null ? new GithubClient() : new GithubClient(link);
    }

    @Bean StackoverflowClient stackoverflowClient() {
        var link = config.clients().stackoverflow();
        return link == null ? new StackoverflowClient() : new StackoverflowClient(link);
    }
}
