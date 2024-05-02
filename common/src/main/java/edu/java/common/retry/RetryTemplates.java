package edu.java.common.retry;

import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@UtilityClass
public class RetryTemplates {
    public static RetryTemplate constant(
        Set<Integer> retryStatuses,
        Integer maxAttempts,
        Long period
    ) {
        var template = new RetryTemplate();
        var backoffPolicy = new FixedBackOffPolicy();
        backoffPolicy.setBackOffPeriod(period);
        template.setBackOffPolicy(backoffPolicy);
        template.setRetryPolicy(new HttpStatusRetryPolicy(
            retryStatuses, maxAttempts
        ));
        return template;
    }

    public static RetryTemplate linear(
        Set<Integer> retryStatuses,
        Integer maxAttempts,
        Long initialInterval,
        Long maxInterval
    ) {
        var template = new RetryTemplate();
        var backoffPolicy = new LinearBackoffPolicy();
        backoffPolicy.setInitialInterval(initialInterval);
        backoffPolicy.setMaxInterval(maxInterval);
        template.setBackOffPolicy(backoffPolicy);
        template.setRetryPolicy(new HttpStatusRetryPolicy(
            retryStatuses, maxAttempts
        ));
        return template;
    }

    public static RetryTemplate exponential(
        Set<Integer> retryStatuses,
        Integer maxAttempts,
        Long initialInterval,
        Double multiplier,
        Long maxInterval
    ) {
        var template = new RetryTemplate();
        var backoffPolicy = new ExponentialBackOffPolicy();
        backoffPolicy.setInitialInterval(initialInterval);
        backoffPolicy.setMultiplier(multiplier);
        backoffPolicy.setMaxInterval(maxInterval);
        template.setBackOffPolicy(backoffPolicy);
        template.setRetryPolicy(new HttpStatusRetryPolicy(
            retryStatuses, maxAttempts
        ));
        return template;
    }
}
