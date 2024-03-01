package edu.java.scrapper.exception;

import edu.java.models.exception.ApiErrorException;
import org.springframework.http.HttpStatus;

public class LinkNotTrackingException extends ApiErrorException {
    public LinkNotTrackingException() {
        super(HttpStatus.NOT_FOUND, "Link isn't found");
    }
}
