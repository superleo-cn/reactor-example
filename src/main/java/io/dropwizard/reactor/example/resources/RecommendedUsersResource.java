package io.dropwizard.reactor.example.resources;

import com.codahale.metrics.Timer;
import io.dropwizard.reactor.example.service.RecommendedUsersService;
import org.glassfish.jersey.server.ManagedAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/recommended_users")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendedUsersResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendedUsersResource.class);
    private final RecommendedUsersService recommendedUsersService;
    private Timer timer;
    public RecommendedUsersResource(RecommendedUsersService recommendedUsersService) {
        this.recommendedUsersService = recommendedUsersService;
    }

    @GET
    @Path("/{id}")
    public void asyncGet(@Suspended final AsyncResponse asyncResponse,
                         @PathParam("id") long id,
                         @QueryParam("limit") int limit) {
        recommendedUsersService.getUsers(id, limit)  //Returns Mono<List<UserTopicStats>>
                .subscribe(userTopicStatsList -> asyncResponse.resume(Response.ok(userTopicStatsList).build()), //consumer
                        e -> asyncResponse.resume(Response.serverError().build())); //error consumer
    }
}
