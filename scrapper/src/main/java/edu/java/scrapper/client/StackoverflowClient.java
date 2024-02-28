package edu.java.scrapper.client;

import edu.java.scrapper.entities.StackoverflowResponse;

public class StackoverflowClient extends AbstractClient<StackoverflowResponse> {
    private static final String DEFAULT_URL = "https://api.stackexchange.com/2.3";

    public StackoverflowClient(String apiUrl) {
        super(apiUrl, StackoverflowResponse.class);
    }

    public StackoverflowClient() {
        this(DEFAULT_URL);
    }

    @Override
    public String uri(String path) {
        return "/questions/" + path + "?site=stackoverflow";
    }
}
