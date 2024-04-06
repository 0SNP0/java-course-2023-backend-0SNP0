package edu.java.bot.configuration;

import edu.java.common.models.dto.LinkUpdateRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {
    public static final String DLQ_SUFFIX = "_dlq";

    private final ApplicationConfig config;

    @Bean
    public NewTopic updatesTopic() {
        return TopicBuilder
            .name(config.kafka().updatesTopic().name())
            .partitions(1).replicas(1)
            .build();
    }

    @Bean
    public NewTopic updatesDlqTopic() {
        return TopicBuilder
            .name(config.kafka().updatesTopic().name() + DLQ_SUFFIX)
            .partitions(1).replicas(1)
            .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest> listenerContainerFactory(
        ConsumerFactory<Long, LinkUpdateRequest> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ProducerFactory<Long, LinkUpdateRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka().server(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
            JsonSerializer.ADD_TYPE_INFO_HEADERS, false
        ));
    }

    @Bean
    public KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate(
        ProducerFactory<Long, LinkUpdateRequest> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
