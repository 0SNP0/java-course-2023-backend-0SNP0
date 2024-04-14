package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackoverflowClient;
import edu.java.scrapper.client.UrlSupporter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig config;

    @Bean
    public BotClient botClient() {
        return new BotClient(config.botApi());
    }

    @Bean
    public GithubClient githubClient() {
        if (config.clients() == null || config.clients().github() == null) {
            return new GithubClient();
        }
        return new GithubClient(config.clients().github());
    }

    @Bean
    public StackoverflowClient stackoverflowClient() {
        if (config.clients() == null || config.clients().stackoverflow() == null) {
            return new StackoverflowClient();
        }
        return new StackoverflowClient(config.clients().stackoverflow());
    }

    @Bean
    public Map<String, UrlSupporter> clientsMap(List<UrlSupporter> clients) {
        return clients.stream().collect(Collectors.toMap(UrlSupporter::name, x -> x));
    }
}
