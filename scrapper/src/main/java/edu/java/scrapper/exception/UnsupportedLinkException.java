package edu.java.scrapper.exception;

import edu.java.models.exception.ApiErrorException;
import org.springframework.http.HttpStatus;

public class UnsupportedLinkException extends ApiErrorException {
    public UnsupportedLinkException() {
        super(HttpStatus.NOT_ACCEPTABLE, "This link isn't supported");
    }
}
