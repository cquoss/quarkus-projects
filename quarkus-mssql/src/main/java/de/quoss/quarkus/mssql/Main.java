package de.quoss.quarkus.mssql;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String ... args) {
        LOGGER.info("Running main method");
        Quarkus.run(App.class, args);
    }

}
