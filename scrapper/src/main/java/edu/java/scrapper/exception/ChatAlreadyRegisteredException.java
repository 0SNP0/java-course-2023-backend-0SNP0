package edu.java.scrapper.exception;

import edu.java.models.exception.ApiErrorException;
import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends ApiErrorException {
    public ChatAlreadyRegisteredException() {
        super(HttpStatus.CONFLICT, "Chat is already registered");
    }
}
