package edu.java.scrapper.client;

import edu.java.scrapper.entity.ClientResponse;
import edu.java.scrapper.entity.GithubResponse;
import java.net.URI;
import java.util.regex.Pattern;

public class GithubClient extends AbstractClient<GithubResponse> {
    private static final String DEFAULT_URL = "https://api.github.com";

    public GithubClient(String apiUrl) {
        super(
            apiUrl,
            GithubResponse.class,
            Pattern.compile("^/[0-9a-z-A-Z]+/[\\w-.]+/?$")
        );
    }

    public GithubClient() {
        this(DEFAULT_URL);
    }

    @Override
    public String uri(String path) {
        return "/repos/" + path;
    }

    @Override
    public ClientResponse fetch(URI url) {
        return fetch(url.getPath()).block();
    }
}
