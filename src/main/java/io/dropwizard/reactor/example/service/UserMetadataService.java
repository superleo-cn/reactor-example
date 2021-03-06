package io.dropwizard.reactor.example.service;

import io.dropwizard.reactor.example.api.Topic;
import io.dropwizard.reactor.example.api.User;
import io.dropwizard.reactor.example.api.UserTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class UserMetadataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMetadataService.class);
    private final Scheduler userScheduler;
    private final Scheduler topicScheduler;

    public UserMetadataService(Scheduler userScheduler, Scheduler topicScheduler) {
        this.userScheduler = userScheduler;
        this.topicScheduler = topicScheduler;
    }

    private Mono<User> getUser(long id) {
        return TracingUtil.trace(Mono.fromCallable(() -> {
            LOGGER.info("Fetching user info");
            Thread.sleep(100);
            return new User(id, "name" + id);
        }).subscribeOn(userScheduler), "user-metadata");
    }

    private Mono<Topic> getUserTopTopic(long id) {
        return TracingUtil.trace(Mono.fromCallable(() -> {
            LOGGER.info("Fetching user's top topic info");
            Thread.sleep(100);
            return new Topic(id, "topic" + id);
        }).subscribeOn(topicScheduler), "user-top-topic");
    }

    // Fetch user metadata and user's top topic in parallel
    public Mono<UserTopic> getUserMetaData(long id) {
        // Fetch user and user's top topic in parallel
        // and combine them into a UserTopic class
        Mono<UserTopic> userTopicMono = getUser(id)
                .zipWith(getUserTopTopic(id),
                        (user, topic) -> new UserTopic(user, topic));
        return userTopicMono;
    }
}
