package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class StackoverflowClientTest {
    private final StackoverflowClient client;
    private static final String QUESTIONS = "/questions/";
    private static final String FILTER = "?site=stackoverflow";
    private static final String ID = "12345678";
    private static final OffsetDateTime DATE = OffsetDateTime.of(2024, 2, 23,
        0, 0, 0, 0, ZoneOffset.UTC
    );
    private static final String TITLE = "Title";

    public StackoverflowClientTest(@NotNull WireMockRuntimeInfo wmRuntimeInfo) {
        client = new StackoverflowClient(wmRuntimeInfo.getHttpBaseUrl());
    }

    @Test
    public void found() {
        stubFor(get(QUESTIONS + ID + FILTER).willReturn(ok()
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody("""
                {
                    "items": [
                        {
                            "last_activity_date": %d,
                            "title": "%s"
                        }
                    ]
                }
                """.formatted(DATE.toEpochSecond(), TITLE))
        ));
        var response = client.fetch(ID).block();

        assertThat(response).isNotNull();
        assertThat(response.items().getFirst().title()).isEqualTo(TITLE);
        assertThat(response.items().getFirst().lastActivityDate()).isEqualTo(DATE);
    }

    @Test void notFound() {
        stubFor(get(QUESTIONS + ID + FILTER).willReturn(ok()
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody("""
                {
                    "items": []
                }
                """)
        ));

        var response = client.fetch(ID).block();
        assertThat(response).isNotNull();
        assertThat(response.items()).isEmpty();
    }
}
