package io.dropwizard.reactor.example;

import io.dropwizard.Application;
import io.dropwizard.reactor.example.resources.RecommendedUsersResource;
import io.dropwizard.reactor.example.resources.SampleResource;
import io.dropwizard.reactor.example.service.CandidateUsersService;
import io.dropwizard.reactor.example.service.RecommendedUsersService;
import io.dropwizard.reactor.example.service.TopicCountsService;
import io.dropwizard.reactor.example.service.UserMetadataService;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactorExampleApplication extends Application<ReactorExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ReactorExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "ReactorExample";
    }

    @Override
    public void initialize(final Bootstrap<ReactorExampleConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ReactorExampleConfiguration configuration,
                    final Environment environment) {
        Scheduler scheduler = Schedulers.newBoundedElastic(50, 1000, "sample");
        Scheduler candidateUsersScheduler = Schedulers.newBoundedElastic(10, 10, "candidate-users-service");
        Scheduler userMetadataScheduler = Schedulers.newBoundedElastic(10, 10, "user-metadata-service");
        Scheduler topicMetadataScheduler = Schedulers.newBoundedElastic(10, 10, "topic-metadata-service");
        Scheduler topicCountsScheduler = Schedulers.newBoundedElastic(10, 10, "topic-counts-service");

        CandidateUsersService candidateUserService = new CandidateUsersService(candidateUsersScheduler);
        UserMetadataService userMetaDataService = new UserMetadataService(userMetadataScheduler, topicMetadataScheduler);
        TopicCountsService topicCountService = new TopicCountsService(topicCountsScheduler);
        RecommendedUsersService recommendedUsersService = new RecommendedUsersService(userMetaDataService,
                topicCountService, candidateUserService);

        environment.jersey().register(new RecommendedUsersResource(recommendedUsersService));
        environment.jersey().register(new SampleResource(scheduler));
    }

}
