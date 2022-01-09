package com.artshell.reactor.operators;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class FluxThenEmpty {
    /**
     * @see Flux#thenEmpty(Publisher)
     */
    public static void main(String[] args) {
        Flux.just("#")
                .thenEmpty(Flux.empty())
                .subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("complete"));
        // obtain result:
        // complete
    }
}
