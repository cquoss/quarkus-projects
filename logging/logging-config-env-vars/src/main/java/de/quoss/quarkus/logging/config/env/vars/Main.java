package de.quoss.quarkus.logging.config.env.vars;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String ... args) {
        LOGGER.info("Running main method ...");
        LOGGER.debug("This is a debug output.");
        Quarkus.run(args);
    }
    
}
