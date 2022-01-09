package com.artshell.reactor.operators;

import reactor.core.publisher.Flux;

import java.util.function.Predicate;

public class FluxWindowWhile {

    /**
     * @see Flux#windowWhile(Predicate)
     * @see Flux#windowWhile(Predicate, int)
     */
    public static void main(String[] args) {
        Flux.concat(Flux.just("A", "BB", "CC", "D", "R"), Flux.just("!", "@@", "#", "$", "%%","^", "&", "**"))
                .windowWhile(s -> s.length() > 1)
                .flatMap(flx -> flx)
                .subscribe(System.out::println);

        // obtain result:
        // BB, CC, @@, %%, **
    }
}
