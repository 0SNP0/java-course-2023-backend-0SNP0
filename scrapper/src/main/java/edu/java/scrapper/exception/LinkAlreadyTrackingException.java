package edu.java.scrapper.exception;

import edu.java.common.models.exception.ApiErrorException;
import org.springframework.http.HttpStatus;

public class LinkAlreadyTrackingException extends ApiErrorException {
    public LinkAlreadyTrackingException() {
        super(HttpStatus.CONFLICT, "Link is been already tracking for user");
    }
}
