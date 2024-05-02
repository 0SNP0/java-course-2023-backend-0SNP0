package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    @Bean
    Scheduler scheduler,
    ApiLinks clients,
    @NotEmpty
    String botApi,
    @NotNull
    AccessType databaseAccessType,
    @NotNull
    Retries retry
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record ApiLinks(String github, String stackoverflow) {
    }

    public enum AccessType {
        JDBC,
        JPA
    }

    public record Retries(Retry links, Retry bot) {
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
}
