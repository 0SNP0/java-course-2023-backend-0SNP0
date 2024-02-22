package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class GithubClientTest {
    private final GithubClient client;
    private static final String REPOS = "/repos/";
    private static final String NAME = "a/b";
    private static final OffsetDateTime DATE = OffsetDateTime.of(2024, 2, 23,
        0, 0, 0, 0, ZoneOffset.UTC
    );

    public GithubClientTest(@NotNull WireMockRuntimeInfo wmRuntimeInfo) {
        client = new GithubClient(wmRuntimeInfo.getHttpBaseUrl());
    }

    @Test
    public void found() {
        stubFor(get(REPOS + NAME).willReturn(ok()
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody("""
                {
                   "full_name": "%s",
                   "updated_at": "%s"
                }
                """.formatted(NAME, DATE.toString()))
        ));
        var response = client.fetch(NAME).block();

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo(NAME);
        assertThat(response.updatedAt()).isEqualTo(DATE);
    }

    @Test void notFound() {
        stubFor(get(REPOS + NAME).willReturn(badRequest()
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody("""
                {
                   "message": "Not Found",
                   "documentation_url": "https://docs.github.com/rest/repos/repos#get-a-repository"
                }
                """)
        ));

        assertThatExceptionOfType(WebClientResponseException.class)
            .isThrownBy(() -> client.fetch(NAME).block());
    }
}
