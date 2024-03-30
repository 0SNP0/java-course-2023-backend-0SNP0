package edu.java.bot.configuration;

import edu.java.common.retry.RetryTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig config;

    @Bean
    public RetryTemplate retryTemplate() {
        var retry = config.retry();
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
}
