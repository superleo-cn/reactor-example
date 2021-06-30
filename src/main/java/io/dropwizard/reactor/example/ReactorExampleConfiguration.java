package io.dropwizard.reactor.example;

import com.smoketurner.dropwizard.zipkin.ConsoleZipkinFactory;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import com.smoketurner.dropwizard.zipkin.client.ZipkinClientConfiguration;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ReactorExampleConfiguration extends Configuration {
    @Valid
    @NotNull
    public final ZipkinFactory zipkin = new ConsoleZipkinFactory();

    @Valid @NotNull
    private final ZipkinClientConfiguration zipkinClient = new ZipkinClientConfiguration();

    @JsonProperty
    public ZipkinFactory getZipkin() {
        return zipkin;
    }

    @JsonProperty
    public ZipkinClientConfiguration getZipkinClient() {
        return zipkinClient;
    }
}
