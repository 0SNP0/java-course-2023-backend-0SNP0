package edu.java.bot.controller;

import edu.java.bot.service.LinkUpdateService;
import edu.java.models.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/updates")
public class UpdatesController {
    private final LinkUpdateService service;

    @PostMapping
    public void post(@RequestBody LinkUpdateRequest request) {
        service.sendNotifications(request);
    }
}
