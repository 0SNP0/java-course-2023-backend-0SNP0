package edu.java.bot.handler;

import edu.java.common.models.dto.ApiErrorResponse;
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
            "Некорректные параметры запроса",
            "400",
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        ));
    }
}
