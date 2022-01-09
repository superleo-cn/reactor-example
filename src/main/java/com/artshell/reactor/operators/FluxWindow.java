package com.artshell.reactor.operators;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class FluxWindow {
    /**
     * @see Flux#window(Duration)
     * @see Flux#window(Duration, Scheduler)
     * @see Flux#window(Publisher)
     */
    private static void windowDuration() {
        Flux.concat(Flux.just("#").delayElements(Duration.ofMillis(20), Schedulers.newElastic("delay")), Flux.range(1, 10), Flux.range(11, 5).delayElements(Duration.ofMillis(15), Schedulers.newElastic("Elastic")))
                .window(Duration.ofMillis(10))
                .elapsed()
                .subscribe(flx -> {
                    System.out.println("cost time => " + flx.getT1());
                    flx.getT2().subscribe(System.out::println);
                });

        // obtain result:
        // cost time => 0
        // cost time => 18
        // cost time => 10
        // cost time => 11
        // cost time => 12
        // cost time => 12
        // cost time => 5
        // cost time => 10
        // cost time => 10
        // #, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        // cost time => 10
        // cost time => 10
        // 11
        // cost time => 10
        // 12
        // cost time => 10
        // cost time => 10
        // 13
        // cost time => 10
        // 14
        // cost time => 10
        // cost time => 10
        // 15
    }

    /**
     * @see Flux#window(Duration, Duration)
     * @see Flux#window(Duration, Duration, Scheduler)
     */
    private static void windowIgnore() {
        Flux.concat(Flux.just("#").delayElements(Duration.ofMillis(10)), Flux.just("&").delayElements(Duration.ofMillis(7)), Flux.just("%").delayElements(Duration.ofMillis(12)), Flux.just("@").delayElements(Duration.ofMillis(9)))
                .window(Duration.ofMillis(5), Duration.ofMillis(8))
                .subscribe(flx -> flx.subscribe(System.out::println));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // obtain result:
        // [DEBUG] (parallel-2) onNextDropped: #
        // #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-2) onNextDropped: #
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-1) onNextDropped: &
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // [DEBUG] (parallel-2) onNextDropped: %
        // %
    }

    /**
     * @see Flux#window(Duration, Duration)
     * @see Flux#window(Duration, Duration, Scheduler)
     */
    private static void windowOverlap() {
        Flux.concat(Flux.just("#").delayElements(Duration.ofMillis(10)), Flux.just("&").delayElements(Duration.ofMillis(7)), Flux.just("%").delayElements(Duration.ofMillis(12)), Flux.just("@").delayElements(Duration.ofMillis(9)))
                .window(Duration.ofMillis(10), Duration.ofMillis(5))
                .subscribe(flx -> flx.subscribe(System.out::println));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // obtain result:
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-3) onNextDropped: #
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // &
        // &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-4) onNextDropped: &
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-3) onNextDropped: %
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
        // [DEBUG] (parallel-4) onNextDropped: @
    }

    /**
     * @see Flux#window(Duration, Duration)
     * @see Flux#window(Duration, Duration, Scheduler)
     */
    private static void windowExact() {
        Flux.concat(Flux.just("#").delayElements(Duration.ofMillis(10)), Flux.just("&").delayElements(Duration.ofMillis(7)), Flux.just("%").delayElements(Duration.ofMillis(12)), Flux.just("@").delayElements(Duration.ofMillis(9)))
                .window(Duration.ofMillis(5), Duration.ofMillis(5))
                .subscribe(flx -> flx.subscribe(System.out::println));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // obtain result:
        // #, &, %, @
    }

    /**
     * @see Flux#window(int)
     */
    private static void windowMax() {
        Flux.range(1, 5)
                .window(3)
                .subscribe(flx -> flx.subscribe(System.out::println));

        // obtain result:
        // 1, 2, 3, 4, 5
    }


    /**
     * @see Flux#window(int, int)
     */
    private static void windowSkipIgnore() {
        Flux.range(1, 10)
                .window(2, 3)
                .subscribe(flx -> flx.subscribe(System.out::println));
        // obtain result:
        // 1, 2, 4, 5, 7, 8, 10
    }

    /**
     * @see Flux#window(int, int)
     */
    private static void windowSkipOverlap() {
        Flux.range(1, 10)
                .window(5, 4)
                .subscribe(flx -> flx.subscribe(System.out::println));
        // obtain result:
        // 1, 2, 3, 4, 5, 5, 6, 7, 8, 9, 9, 10
    }

    /**
     * @see Flux#window(int, int)
     */
    private static void windowSkipExact() {
        Flux.range(1, 10)
                .window(3, 3)
                .subscribe(flx -> flx.subscribe(System.out::println));
        // obtain result:
        // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    }

    public static void main(String[] args) {
//        windowDuration();
//        windowIgnore();
//        windowOverlap();
//        windowExact();
//        windowMax();
//        windowSkipIgnore();
//        windowSkipOverlap();
        windowSkipExact();
    }
}
