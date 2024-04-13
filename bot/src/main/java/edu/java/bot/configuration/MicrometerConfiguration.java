package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MicrometerConfiguration {
    private final ApplicationConfig config;
    private final MeterRegistry registry;

    @Bean
    public Counter processedMessagesCounter() {
        return Counter.builder(config.micrometer().processedMessagesCounter().name())
            .description(config.micrometer().processedMessagesCounter().description())
            .register(registry);
    }
}
