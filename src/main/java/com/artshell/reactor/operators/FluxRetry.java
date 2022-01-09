package com.artshell.reactor.operators;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalTime;

public class FluxRetry {
    /**
     * 重试次数
     *
     * @see Flux#retry()
     * @see Flux#retry(long)
     */
    private static void retry() {
        Flux.concat(Flux.just("$"), Flux.error(new IllegalStateException()))
                .retry(1)
                .subscribe(System.out::println);
        // obtain result:
        // $
        // $
        // Exception in thread "main" reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalStateException
    }

    /**
     * 重试策略
     *
     * @see Flux#retryWhen()
     */
    private static void retryPredicate() {
        Flux.concat(Flux.just("#"), Flux.error(new IllegalStateException()))
                .retryWhen(Retry
                        .backoff(3, Duration.ofMillis(100)).jitter(0d)
                        .doAfterRetry(rs -> System.out.println("retried at " + LocalTime.now() + ", attempt " + rs.totalRetries()))
                        .onRetryExhaustedThrow((spec, rs) -> rs.failure()))
                .subscribe(System.out::println);
        // obtain result:
        // #, #, #, # ...

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        retry();
        retryPredicate();
    }
}
