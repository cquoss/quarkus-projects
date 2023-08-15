package de.quoss.quarkus.hello.world;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class App implements QuarkusApplication {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
    @Override
    public int run(String...args) {
        LOGGER.info("Hello, World!");
        return 0;
    }
    
}
