package edu.java.scrapper.client;

import edu.java.scrapper.entities.GithubResponse;

public class GithubClient extends AbstractClient<GithubResponse> {
    private static final String DEFAULT_URL = "https://api.github.com";

    public GithubClient(String apiUrl) {
        super(apiUrl, GithubResponse.class);
    }

    public GithubClient() {
        this(DEFAULT_URL);
    }

    @Override
    public String uri(String path) {
        return "/repos/" + path;
    }
}
