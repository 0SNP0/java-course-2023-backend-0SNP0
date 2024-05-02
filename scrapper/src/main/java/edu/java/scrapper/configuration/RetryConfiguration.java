package edu.java.scrapper.configuration;

import edu.java.common.retry.RetryTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import static edu.java.scrapper.configuration.RetryConfiguration.RetryCategory.BOT;
import static edu.java.scrapper.configuration.RetryConfiguration.RetryCategory.LINKS;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig config;

    @Bean
    @Qualifier("links")
    public RetryTemplate linksRetryTemplate() {
        return retryTemplate(LINKS);
    }

    @Bean
    @Qualifier("bot")
    public RetryTemplate botRetryTemplate() {
        return retryTemplate(BOT);
    }

    public RetryTemplate retryTemplate(RetryCategory category) {
        var retry = switch (category) {
            case LINKS -> config.retry().links();
            case BOT -> config.retry().bot();
        };
        return switch (retry.type()) {
            case CONSTANT -> RetryTemplates.constant(
                retry.httpStatuses(),
                retry.maxAttempts(),
                retry.config().initialIntervalMillis()
            );
            case LINEAR -> RetryTemplates.linear(
                retry.httpStatuses(),
                retry.maxAttempts(),
                retry.config().initialIntervalMillis(),
                retry.config().maxIntervalMillis()
            );
            case EXPONENTIAL -> RetryTemplates.exponential(
                retry.httpStatuses(),
                retry.maxAttempts(),
                retry.config().initialIntervalMillis(),
                retry.config().multiplier(),
                retry.config().maxIntervalMillis()
            );
        };
    }

    public enum RetryCategory {
        LINKS, BOT
    }
}
