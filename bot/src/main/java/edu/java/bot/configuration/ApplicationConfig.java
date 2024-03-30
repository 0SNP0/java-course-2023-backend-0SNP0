package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotEmpty
    String scrapperApi,
    @NotNull
    Retry retry
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
}
