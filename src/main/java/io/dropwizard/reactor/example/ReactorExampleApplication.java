package io.dropwizard.reactor.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: implement application
    }

}
