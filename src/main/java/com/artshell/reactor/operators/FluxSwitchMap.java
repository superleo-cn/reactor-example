package com.artshell.reactor.operators;

import reactor.core.publisher.Flux;

import java.util.function.Function;

public class FluxSwitchMap {
    /**
     * @see Flux#switchMap(Function)
     * @see Flux#switchMap(Function, int)
     */
    public static void main(String[] args) {
        Flux.range(4, 3)
                .switchMap(i -> Flux.just(i + "$"))
                .subscribe(System.out::println);
        // obtain result:
        // 4$, 5$, 6$
    }
}
