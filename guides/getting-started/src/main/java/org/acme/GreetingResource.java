package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/hello")
public class GreetingResource {
    
    private final GreetingService service;
    
    public GreetingResource(final GreetingService service) {
        this.service = Objects.requireNonNull(service, "The service must not be null.");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String greeting(final String name) {
        return service.greeting(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
    
}
