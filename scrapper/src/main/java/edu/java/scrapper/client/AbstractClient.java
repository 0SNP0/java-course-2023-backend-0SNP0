package edu.java.scrapper.client;

import edu.java.scrapper.entity.ClientResponse;
import java.net.URI;
import java.util.regex.Pattern;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class AbstractClient<T extends ClientResponse> implements Client<T> {
    protected final String hostUrl;
    protected final WebClient client;
    protected Class<T> responseEntity;
    protected final Pattern pattern;

    protected AbstractClient(String apiUrl, Class<T> responseEntity, Pattern pattern) {
        this.hostUrl = apiUrl;
        this.client = WebClient.create(apiUrl);
        this.responseEntity = responseEntity;
        this.pattern = pattern;
    }

    protected abstract String uri(String path);

    @Override
    public Mono<T> fetch(String path) {
        return client.get().uri(uri(path)).retrieve().bodyToMono(responseEntity);
    }

    @Override
    public boolean supports(URI url) {
        return hostUrl.equals(url.getHost().toLowerCase())
            && pattern.matcher(url.getPath()).matches();
    }
}
