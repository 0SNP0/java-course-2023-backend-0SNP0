package edu.java.scrapper.exception;

import edu.java.common.models.exception.ApiErrorException;
import org.springframework.http.HttpStatus;

public class ChatNotRegisteredException extends ApiErrorException {
    public ChatNotRegisteredException() {
        super(HttpStatus.NOT_FOUND, "Chat doesn't exists");
    }
}
