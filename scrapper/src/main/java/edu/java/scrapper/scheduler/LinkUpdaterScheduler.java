package edu.java.scrapper.scheduler;

import edu.java.models.dto.LinkUpdateRequest;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.UrlSupporter;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.service.LinkService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true", matchIfMissing = true)
@EnableScheduling
@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final ApplicationConfig config;
    private final BotClient botClient;
    private final LinkService linkService;
    private final Map<String, UrlSupporter> clientsMap;

    @Scheduled(fixedDelayString = "#{@scheduler.interval().toMillis()}")
    public void update() {
        log.info("Updating links...");
        linkService.getLinks(config.scheduler().forceCheckDelay()).forEach(link -> {
            var client = clientsMap.get(link.getClient());
            if (client == null) {
                log.error("Unsupported link in DB: %s".formatted(link.getUrl()));
                return;
            }
            var clientResponse = client.fetch(link.getUrl());

            if (clientResponse.updatedAt().isAfter(link.getUpdatedAt())) {
                linkService.updateLink(link.setUpdatedAt(clientResponse.updatedAt()));

                var chats = linkService.getChats(link.getLinkId());
                botClient.notify(new LinkUpdateRequest(
                    link.getLinkId(),
                    link.getUrl(),
                    clientResponse.description(),
                    chats.stream().map(Chat::getChatId).toList()
                )).block();
            }
        });
    }
}
