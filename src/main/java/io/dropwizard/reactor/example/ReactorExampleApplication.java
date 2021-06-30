package io.dropwizard.reactor.example;

import brave.http.HttpTracing;
import com.smoketurner.dropwizard.zipkin.ZipkinBundle;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import com.smoketurner.dropwizard.zipkin.client.ZipkinClientBuilder;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
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

import javax.ws.rs.client.Client;
import java.util.Optional;

public class ReactorExampleApplication extends Application<ReactorExampleConfiguration> {

    private ZipkinBundle<ReactorExampleConfiguration> zipkinBundle;


    public static void main(final String[] args) throws Exception {
        new ReactorExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "ReactorExample";
    }

    @Override
    public void initialize(final Bootstrap<ReactorExampleConfiguration> bootstrap) {

        zipkinBundle =
                new ZipkinBundle<ReactorExampleConfiguration>(getName()) {
                    @Override
                    public ZipkinFactory getZipkinFactory(ReactorExampleConfiguration configuration) {
                        return configuration.getZipkin();
                    }
                };
        bootstrap.addBundle(zipkinBundle);
        Schedulers.onScheduleHook("tracing", r -> zipkinBundle.getHttpTracing().get()
                .tracing().currentTraceContext().wrap(r));
    }

    @Override
    public void run(final ReactorExampleConfiguration configuration,
                    final Environment environment) {

        //For tracing http clients
        final Optional<HttpTracing> tracing = zipkinBundle.getHttpTracing();

        final JerseyClientBuilder clientBuilder;
        if (tracing.isPresent()) {
            clientBuilder = new ZipkinClientBuilder(environment, tracing.get().clientOf(configuration.getZipkinClient().getServiceName()));
        } else {
            clientBuilder = new JerseyClientBuilder(environment);
        }

        final Client client = clientBuilder.using(configuration.getZipkinClient()).build(getName());

        Scheduler scheduler = Schedulers.newBoundedElastic(50, 1000, "sample");
        Scheduler candidateUsersScheduler = Schedulers.newBoundedElastic(10, 100, "candidate-users-service");
        Scheduler userMetadataScheduler = Schedulers.newBoundedElastic(10, 100, "user-metadata-service");
        Scheduler topicMetadataScheduler = Schedulers.newBoundedElastic(10, 100, "topic-metadata-service");
        Scheduler topicCountsScheduler = Schedulers.newBoundedElastic(10, 100, "topic-counts-service");

        CandidateUsersService candidateUserService = new CandidateUsersService(candidateUsersScheduler);
        UserMetadataService userMetaDataService = new UserMetadataService(userMetadataScheduler, topicMetadataScheduler);
        TopicCountsService topicCountService = new TopicCountsService(topicCountsScheduler);
        RecommendedUsersService recommendedUsersService = new RecommendedUsersService(userMetaDataService,
                topicCountService, candidateUserService);

        environment.jersey().register(new RecommendedUsersResource(recommendedUsersService));
        environment.jersey().register(new SampleResource(scheduler));
    }

}
