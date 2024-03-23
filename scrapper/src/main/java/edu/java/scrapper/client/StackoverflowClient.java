package edu.java.scrapper.client;

import edu.java.scrapper.entity.ClientResponse;
import edu.java.scrapper.entity.StackoverflowResponse;
import java.net.URI;
import java.util.regex.Pattern;

public class StackoverflowClient extends AbstractClient<StackoverflowResponse> {
    private static final String DEFAULT_URL = "https://api.stackexchange.com/2.3";

    public StackoverflowClient(String apiUrl) {
        super(
            apiUrl,
            StackoverflowResponse.class,
            "stackoverflow.com",
            Pattern.compile("^/questions/(?<id>\\\\d+)(/[\\w-]*)?(/)?$")
        );
    }

    public StackoverflowClient() {
        this(DEFAULT_URL);
    }

    @Override
    public String uri(String path) {
        return "/questions/" + path + "?site=stackoverflow";
    }

    @Override
    public ClientResponse fetch(URI url) {
        return fetch(pattern.matcher(url.getPath()).group("id")).block();
    }
}
