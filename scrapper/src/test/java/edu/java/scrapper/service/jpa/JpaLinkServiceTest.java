package edu.java.scrapper.service.jpa;

import edu.java.common.models.dto.AddLinkRequest;
import edu.java.common.models.dto.LinkResponse;
import edu.java.common.models.dto.RemoveLinkRequest;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.client.UrlSupporter;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.Mapping;
import edu.java.scrapper.entity.MappingId;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackingException;
import edu.java.scrapper.exception.LinkNotTrackingException;
import edu.java.scrapper.exception.UnsupportedLinkException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaMappingRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
public class JpaLinkServiceTest extends IntegrationTest {
    private static final List<Link> linksEntities = LongStream.range(1, 3)
        .mapToObj(x -> new Link().setLinkId(x)
            .setUrl(URI.create("https://github.com/user/repo%d".formatted(x)))
            .setUpdatedAt(OffsetDateTime.now())
            .setClient("github")
        ).toList();

    @Autowired
    private JpaChatRepository chatRepository;
    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaMappingRepository mappingRepository;
    @Autowired
    private List<UrlSupporter> urlSupporters;
    private LinkService linkService;

    @BeforeEach
    void init() {
        linkService = new JpaLinkService(chatRepository, linkRepository, mappingRepository, urlSupporters);
    }

    @TestTransactionalRollback
    void getLinksByChatId() {
        linkRepository.saveAll(linksEntities);
        var link = linkRepository.findAll().getFirst();
        var chatId = chatRepository.save(new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now()))
            .getChatId();
        mappingRepository.save(new Mapping().setId(new MappingId()
            .setChatId(chatId).setLinkId(link.getLinkId())
        ));
        assertThat(linkService.getLinks(chatId).links())
            .containsOnly(new LinkResponse(link.getLinkId(), link.getUrl()));
    }

    @TestTransactionalRollback
    void addLink() {
        var chatId = chatRepository.save(new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now()))
            .getChatId();
        var link = linksEntities.getFirst();
        linkService.addLink(chatId, new AddLinkRequest(link.getUrl()));
        assertThat(linkRepository.findByUrl(link.getUrl()).getUrl()).isEqualTo(link.getUrl());
    }

    @TestTransactionalRollback
    void addUnsupportedLink() {
        var chatId = chatRepository.save(new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now()))
            .getChatId();
        var addLinkRequest = new AddLinkRequest(URI.create("http://u.r.l/a/b"));
        assertThatExceptionOfType(UnsupportedLinkException.class)
            .isThrownBy(() -> linkService.addLink(chatId, addLinkRequest));
    }

    @TestTransactionalRollback
    void addLinkAlreadyTracked() {
        var chatId = chatRepository.save(new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now()))
            .getChatId();
        var addLinkRequest = new AddLinkRequest(linksEntities.getFirst().getUrl());
        linkService.addLink(chatId, addLinkRequest);
        assertThatExceptionOfType(LinkAlreadyTrackingException.class)
            .isThrownBy(() -> linkService.addLink(chatId, addLinkRequest));
    }

    @TestTransactionalRollback
    void removeLink() {
        var chatId = chatRepository.save(new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now()))
            .getChatId();
        var url = linksEntities.getFirst().getUrl();
        linkService.addLink(chatId, new AddLinkRequest(url));
        linkService.removeLink(chatId, new RemoveLinkRequest(url));
        assertThat(mappingRepository.findAllLinksByChatId(chatId)).isEmpty();
    }

    @TestTransactionalRollback
    void removeNotTracked() {
        var chatId = chatRepository.save(new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now()))
            .getChatId();
        var url = linksEntities.getFirst().getUrl();
        assertThatExceptionOfType(LinkNotTrackingException.class)
            .isThrownBy(() -> linkService.removeLink(chatId, new RemoveLinkRequest(url)));
    }

    @TestTransactionalRollback
    void updateLink() {
        var link = linkRepository.save(linksEntities.getFirst());
        var newDate = OffsetDateTime.now();
        link.setUpdatedAt(newDate);
        linkService.updateLink(link);
        assertThat(linkRepository.findByUrl(link.getUrl()).getUpdatedAt())
            .isCloseTo(newDate, within(1, ChronoUnit.MICROS));
    }

    @TestTransactionalRollback
    void getByDuration() {
        var link = linkRepository.save(linksEntities.getFirst().setUpdatedAt(
            OffsetDateTime.now().minusSeconds(10)
        ));
        linkRepository.save(linksEntities.getLast().setUpdatedAt(OffsetDateTime.now()));
        assertThat(linkService.getLinks(Duration.of(10, ChronoUnit.SECONDS)))
            .containsOnly(link);
    }

    @TestTransactionalRollback
    void getChatsByLink() {
        var chat = chatRepository.save(
            new Chat().setChatId(1L).setRegisteredAt(OffsetDateTime.now())
        );
        chatRepository.save(
            new Chat().setChatId(2L).setRegisteredAt(OffsetDateTime.now())
        );
        var url = linksEntities.getFirst().getUrl();
        linkService.addLink(chat.getChatId(), new AddLinkRequest(url));
        var link = linkRepository.findByUrl(url);
        assertThat(linkService.getChats(link.getLinkId())).containsOnly(chat);
    }

    @TestTransactionalRollback
    void unregistered() {
        var chatId = 1L;
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> linkService.getLinks(chatId));
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> linkService
                .addLink(chatId, new AddLinkRequest(linksEntities.getFirst().getUrl())));
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> linkService
                .removeLink(chatId, new RemoveLinkRequest(linksEntities.getFirst().getUrl())));

    }
}
