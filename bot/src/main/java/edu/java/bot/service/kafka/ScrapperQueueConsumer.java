package edu.java.bot.service.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.KafkaConfiguration;
import edu.java.bot.service.LinkUpdateService;
import edu.java.common.models.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ScrapperQueueConsumer {
    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate;
    private final LinkUpdateService linkUpdateService;

    @KafkaListener(topics = "${app.kafka.updates-topic.name}",
                   groupId = "updates.listeners",
                   containerFactory = "listenerContainerFactory")
    public void listen(@Payload LinkUpdateRequest request) {
        try {
            linkUpdateService.sendNotifications(request);
        } catch (Exception e) {
            log.error(e);
            kafkaTemplate.send(
                config.kafka().updatesTopic().name() + KafkaConfiguration.DLQ_SUFFIX,
                0L,
                request
            );
        }
    }
}
