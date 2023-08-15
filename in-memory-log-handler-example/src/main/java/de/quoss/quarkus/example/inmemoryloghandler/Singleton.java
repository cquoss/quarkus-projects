package de.quoss.quarkus.example.inmemoryloghandler;

import io.quarkus.runtime.Startup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
public class Singleton {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Singleton.class);
    
    public Singleton() {
        LOGGER.info("I'm getting created!");
    }
    
}
