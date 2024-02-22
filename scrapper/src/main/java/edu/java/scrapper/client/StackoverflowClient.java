package edu.java.scrapper.client;

import edu.java.scrapper.entities.StackoverflowResponse;

public class StackoverflowClient extends AbstractClient<StackoverflowResponse> {
    public StackoverflowClient(String apiUrl) {
        super(apiUrl, StackoverflowResponse.class);
    }

    @Override
    public String uri(String path) {
        return "/questions/" + path + "?site=stackoverflow";
    }
}
