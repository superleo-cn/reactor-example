package io.dropwizard.reactor.example.service;

import io.dropwizard.reactor.example.api.UserTopic;
import io.dropwizard.reactor.example.api.UserTopicStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class TopicCountsService {
    private final Scheduler scheduler;
    private final Random random;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicCountsService.class);

    public TopicCountsService(Scheduler scheduler) {
        this.scheduler = scheduler;
        this.random = new Random();
    }
    private static final long MAX_RETRIES = 100;
    private static final long BACKOFF_TIME = 100;

    private static boolean isRetryableException(Throwable e) {
        return true;
    }

    public Flux<UserTopicStats> getUserAccurateTopicCount(List<UserTopic> userTopicList) {
        return Mono.fromCallable(() -> {
            LOGGER.info("Fetching accurate user topic count");

            if(random.nextBoolean()) {
                throw new RuntimeException("Failed to fetch accurate counts");
            }

            return userTopicList.stream().map(ug -> new UserTopicStats(ug.getUser(), ug.getTopTopic(),
                    ug.getUser().getId() * ug.getTopTopic().getId())).collect(toList());
        }).subscribeOn(scheduler).flatMapIterable(x -> x);
    }

    public Flux<UserTopicStats> getUserApproxTopicCount(List<UserTopic> userTopicList) {
        return Mono.fromCallable(() -> {
            LOGGER.info("Fetching approx user topic count");
            return userTopicList.stream().map(ug -> new UserTopicStats(ug.getUser(), ug.getTopTopic(),
                    ug.getUser().getId() * ug.getTopTopic().getId())).collect(toList());
        }).subscribeOn(scheduler).flatMapIterable(x -> x);
    }

    private static final RetryBackoffSpec retrySpec = Retry.backoff(MAX_RETRIES, Duration.ofMillis(BACKOFF_TIME))
            .filter(e -> isRetryableException(e));

    public Flux<UserTopicStats> getUserTopicCount(List<UserTopic> userTopicList) {
        return getUserAccurateTopicCount(userTopicList)
                .doOnError(e -> LOGGER.error("Failed to fetch accurate topic counts", e))
                .retryWhen(retrySpec) //Exponential backoff
                .onErrorResume(e -> getUserApproxTopicCount(userTopicList)); //Fallback logic on error
    }
}
