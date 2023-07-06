package de.quoss.quarkus.mssql;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class App implements QuarkusApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private DataSource dataSource;

    public App(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int run(String ... args) throws Exception {
        final String methodName = "run(String[])";
        try (final Connection connection = dataSource.getConnection()) {
            LOGGER.info("{} [connection={}]", methodName, connection);
        }
        Quarkus.waitForExit();
        return 0;
    }

}
