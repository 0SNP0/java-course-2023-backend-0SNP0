package edu.java.bot.repository;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.UserIsNotRegisteredException;
import edu.java.models.dto.AddLinkRequest;
import edu.java.models.dto.RemoveLinkRequest;
import edu.java.models.exception.ApiErrorException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScrapperLinksRepository implements LinksRepository {
    private final ScrapperClient client;

    @Override
    public boolean register(long chatId) {
        return Boolean.TRUE.equals(
            client.registerChat(chatId)
                .map(x -> true)
                .onErrorReturn(false)
                .block()
        );
    }

    @Override
    public boolean isRegistered(long chatId) {
        return Boolean.TRUE.equals(
            client.checkChat(chatId)
                .map(x -> true)
                .onErrorReturn(false)
                .block()
        );
    }

    @Override
    public boolean addLink(long chatId, String link) {
        return Boolean.TRUE.equals(
            client.addLink(chatId, new AddLinkRequest(URI.create(link)))
                .map(x -> true)
                .onErrorReturn(false)
                .block()
        );
    }

    @Override
    public List<String> getLinks(long chatId) throws UserIsNotRegisteredException {
        try {
            return Objects.requireNonNull(
                    Objects.requireNonNull(client.getLinks(chatId).block())
                        .getBody()
                ).links().stream()
                .map(linkResponse -> linkResponse.url().toString()).toList();
        } catch (ApiErrorException e) {
            throw new UserIsNotRegisteredException();
        }
    }

    @Override
    public boolean removeLink(long chatId, String link) {
        return Boolean.TRUE.equals(
            client.removeLink(chatId, new RemoveLinkRequest(URI.create(link)))
                .map(x -> true)
                .onErrorReturn(false)
                .block()
        );
    }
}
