package edu.java.scrapper.controller;

import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TgChatController {
    private static final String ID_PATH = "/{id}";

    private final ChatService service;

    @GetMapping(ID_PATH)
    public void isRegistered(@PathVariable Long id) {
        service.shouldBeRegistered(id);
    }

    @PostMapping(ID_PATH)
    public void registerChat(@PathVariable Long id) {
        service.register(id);
    }

    @DeleteMapping(ID_PATH)
    public void deleteChat(@PathVariable Long id) {
        service.delete(id);
    }
}
