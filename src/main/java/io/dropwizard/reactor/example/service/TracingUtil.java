package io.dropwizard.reactor.example.service;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracing;
import brave.propagation.TraceContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class TracingUtil {
    public static <T> Mono<T> trace(Mono<T> delegate, String operationName) {
        return Mono.fromSupplier(() -> getSpan(operationName))
                .flatMap(trace -> delegate.doFinally(x -> trace.ifPresent(s -> finish(s))));
    }

    public static <T> Flux<T> trace(Flux<T> delegate, String operationName) {
        return Mono.fromSupplier(() -> getSpan(operationName))
                .flatMapMany(trace -> delegate.doFinally(x -> trace.ifPresent(s -> finish(s))));
    }

    private static Optional<ScopedSpan> getSpan(String operationName) {
        if(Tracing.current() == null || Tracing.current().currentTraceContext().get() == null) {
            return Optional.empty();
        }
        ScopedSpan scopedSpan = Tracing.current().tracer().startScopedSpan(operationName);
        scopedSpan.tag("start_thread_name", Thread.currentThread().getName());
        return Optional.of(scopedSpan);
    }

    private static void finish(ScopedSpan scopedSpan) {
        scopedSpan.tag("finish_thread_name", Thread.currentThread().getName());
        scopedSpan.finish();
    }
}
