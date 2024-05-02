package edu.java.scrapper.configuration;

import edu.java.common.models.dto.LinkUpdateRequest;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.service.LinkUpdateService;
import edu.java.scrapper.service.update.ScrapperHttpNotifier;
import edu.java.scrapper.service.update.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class LinkUpdateServiceConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-kafka", havingValue = "true")
    public LinkUpdateService scrapperQueueProducer(
        ApplicationConfig config,
        KafkaTemplate<Long, LinkUpdateRequest> template
    ) {
        return new ScrapperQueueProducer(config, template);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-kafka", havingValue = "false", matchIfMissing = true)
    public LinkUpdateService scrapperHttpNotifier(BotClient botClient) {
        return new ScrapperHttpNotifier(botClient);
    }
}
