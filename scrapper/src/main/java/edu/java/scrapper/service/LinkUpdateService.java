package edu.java.scrapper.service;

import edu.java.common.models.dto.LinkUpdateRequest;

public interface LinkUpdateService {
    void sendUpdate(LinkUpdateRequest request);
}
