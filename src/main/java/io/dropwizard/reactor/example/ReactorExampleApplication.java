package io.dropwizard.reactor.example;

import io.dropwizard.Application;
import io.dropwizard.reactor.example.resources.SampleResource;
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
        environment.jersey().register(new SampleResource(scheduler));
    }

}
