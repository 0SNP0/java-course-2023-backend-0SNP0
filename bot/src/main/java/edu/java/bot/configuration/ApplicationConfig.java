package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotEmpty
    String scrapperApi,
    @NotNull
    Retry retry,
    @NotNull
    KafkaConfig kafka
) {
    public record Retry(
        Set<Integer> httpStatuses,
        Integer maxAttempts,
        RetryType type,
        RetryConfig config
    ) {
        public enum RetryType {
            CONSTANT, LINEAR, EXPONENTIAL
        }

        public record RetryConfig(
            Long initialIntervalMillis,
            Long maxIntervalMillis,
            Double multiplier
        ) {
        }
    }

    public record KafkaConfig(
        String server,
        Topic updatesTopic
    ) {
        public record Topic(
            String name
        ) {
        }
    }
}
