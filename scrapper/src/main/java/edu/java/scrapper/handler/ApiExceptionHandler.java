package edu.java.scrapper.handler;

import edu.java.models.dto.ApiErrorResponse;
import edu.java.models.exception.ApiErrorException;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({HttpMessageNotReadableException.class, NullPointerException.class})
    public ResponseEntity<ApiErrorResponse> badRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
            "Bad request",
            "400",
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        ));
    }

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiErrorResponse> response(ApiErrorException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponse());
    }
}
