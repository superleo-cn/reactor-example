package io.dropwizard.reactor.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class CandidateUsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateUsersService.class);
    private final Scheduler scheduler;

    public CandidateUsersService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    // Fetches topUsers first and if we haven't fetched enough users
    // reach out to getInfluencers to fetch the remaining user ids
    public Flux<Long> getCandidateUsers(long userId, int limit) {
        //Eagerly subscribe and interleave
        Flux.merge(getTopUsers(userId), getInfluencers(userId));

        //Eagerly subscribe, but order will be maintained
        Flux.mergeSequential(getTopUsers(userId), getInfluencers(userId));

        //Subscribe sequentially
        return Flux.concat(getTopUsers(userId), getInfluencers(userId))
                .take(limit);
    }

    public Flux<Long> getTopUsers(long userId) {
        return Mono.fromCallable(() -> {
            LOGGER.info("Fetching list of top users");
            return LongStream.range(0,5).boxed().collect(Collectors.toList());
        }).subscribeOn(scheduler).flatMapIterable(x -> x);
    }

    public Flux<Long> getInfluencers(long userId) {
        return Mono.fromCallable(() -> {
            LOGGER.info("Fetching list of influencers");
            return LongStream.range(10,1000).boxed().collect(Collectors.toList());
        }).subscribeOn(scheduler).flatMapIterable(x -> x);
    }
}

