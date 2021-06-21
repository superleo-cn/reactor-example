package io.dropwizard.reactor.example.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;

@Path("/sample")
public class SampleResource {
    private final Scheduler scheduler;

    public SampleResource(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @GET
    @Path("/blocking")
    public Response getBlocking() {
        return Mono.delay(Duration.ofMillis(50))
                .map(x -> Response.ok().build())
                .subscribeOn(scheduler)
                .block();
    }

    @GET
    @Path("/nonblocking")
    public void getNonBlocking(@Suspended AsyncResponse asyncResponse) {
        Mono.delay(Duration.ofMillis(50))
                .map(x -> Response.ok().build())
                .subscribeOn(scheduler)
                .subscribe(r -> asyncResponse.resume(r),
                        t -> asyncResponse.resume(Response.serverError().build()));
    }
}
