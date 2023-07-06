package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
