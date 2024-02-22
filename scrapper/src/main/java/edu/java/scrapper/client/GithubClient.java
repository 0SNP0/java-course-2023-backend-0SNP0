package edu.java.scrapper.client;

import edu.java.scrapper.entities.GithubResponse;

public class GithubClient extends AbstractClient<GithubResponse> {
    public GithubClient(String apiUrl) {
        super(apiUrl, GithubResponse.class);
    }

    @Override
    public String uri(String path) {
        return "/repos/" + path;
    }
}
