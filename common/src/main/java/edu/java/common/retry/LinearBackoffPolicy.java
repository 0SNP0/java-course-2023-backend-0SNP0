package edu.java.common.retry;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;

@Setter
public class LinearBackoffPolicy implements BackOffPolicy {
    private Long initialInterval;
    private Long maxInterval;

    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackoffContext();
    }

    @Override
    @SneakyThrows
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        Thread.sleep(Math.min(
            maxInterval,
            initialInterval * ++((LinearBackoffContext) backOffContext).attempts
        ));
    }

    static class LinearBackoffContext implements BackOffContext {
        private Integer attempts = 0;
    }
}
