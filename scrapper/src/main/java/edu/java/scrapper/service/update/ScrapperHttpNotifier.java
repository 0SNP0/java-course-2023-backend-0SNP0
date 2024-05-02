package edu.java.scrapper.service.update;

import edu.java.common.models.dto.LinkUpdateRequest;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapperHttpNotifier implements LinkUpdateService {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        botClient.notify(request).block();
    }
}
