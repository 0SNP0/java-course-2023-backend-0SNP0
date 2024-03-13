package edu.java.scrapper.client;

import edu.java.scrapper.entity.ClientResponse;
import java.net.URI;

public interface UrlSupporter {
    boolean supports(URI url);

    ClientResponse fetch(URI url);
}
