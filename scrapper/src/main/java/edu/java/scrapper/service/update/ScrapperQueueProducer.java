package edu.java.scrapper.service.update;

import edu.java.common.models.dto.LinkUpdateRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements LinkUpdateService {
    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate;

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        kafkaTemplate.send(config.kafka().updatesTopic().name(), request.id(), request);
    }
}
