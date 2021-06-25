package io.dropwizard.reactor.example.service;

import io.dropwizard.reactor.example.api.UserTopicStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class RecommendedUsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendedUsersService.class);

    private final UserMetadataService userMetadataService;
    private final TopicCountsService topicCountsService;
    private final CandidateUsersService candidateUsersService;

    public RecommendedUsersService(UserMetadataService userMetadataService,
                                  TopicCountsService topicCountsService,
                                  CandidateUsersService candidateUsersService) {
        this.userMetadataService = userMetadataService;
        this.topicCountsService = topicCountsService;
        this.candidateUsersService = candidateUsersService;
    }

    public Mono<List<UserTopicStats>> getUsers(long userId, int limit) {
        //Caching Flux so items can be replayed in future subscriptions. This is to avoid re-fetching data
        Flux<UserTopicStats> userTopicStatsFlux = getUsersFlux(userId, limit).cache();
        //Cache each user
        userTopicStatsFlux.subscribe(u -> cacheRecommendedUser(u));
        //Convert to Mono
        return userTopicStatsFlux.collectList();
    }

    private Flux<UserTopicStats> getUsersFlux(long userId, int limit) {
        return candidateUsersService.getCandidateUsers(userId, limit)
                //For each user create Mono<UserTopic> and flatten them to a single Flux
                .flatMap(x -> userMetadataService.getUserMetaData(x))
                //Buffering items, 100 at a time into a list as getUserTopicCount supports batch API
                .buffer(100)
                .flatMap(topicCountsService::getUserTopicCount);
    }


    private void cacheRecommendedUser(UserTopicStats u) {
        Mono.fromCallable(() -> {
            LOGGER.info("Caching user {}", u);
            return u;
        }).subscribeOn(Schedulers.parallel()).subscribe();
    }
}
