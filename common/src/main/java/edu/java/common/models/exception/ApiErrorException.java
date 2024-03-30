package edu.java.common.models.exception;

import edu.java.common.models.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Getter
@RequiredArgsConstructor
public class ApiErrorException extends RuntimeException {
    private final ApiErrorResponse response;
    private final HttpStatus statusCode;
    private final String reason;

    public ApiErrorException(ApiErrorResponse response) {
        this(response, HttpStatus.valueOf(Integer.parseInt(response.code())), response.description());
    }

    public ApiErrorException(HttpStatus statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.response = new ApiErrorResponse(
            reason,
            String.valueOf(statusCode.value()),
            this.getClass().getSimpleName(),
            this.getMessage(),
            Arrays.stream(this.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    public static Mono<? extends Throwable> of(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class).map(ApiErrorException::new);
    }

    @Override
    public String getMessage() {
        return reason + (this.reason != null ? " \"" + this.reason + "\"" : "");
    }
}
