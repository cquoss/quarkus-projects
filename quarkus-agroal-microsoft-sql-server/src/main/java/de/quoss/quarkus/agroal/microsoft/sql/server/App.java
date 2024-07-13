package de.quoss.quarkus.agroal.microsoft.sql.server;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

import javax.sql.DataSource;

@QuarkusMain
public class App implements QuarkusApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	@Inject
	DataSource dataSource;
	
	@Override
	public int run(final String... args) {
		final String methodName = "run(String[])";
		LOGGER.info("{} [data-source={}]", methodName, dataSource);
		try (final Connection connection = dataSource.getConnection()) {
			LOGGER.info("{} [connection={},connection.schema={}]", methodName, connection, connection.getSchema());
		} catch (final SQLException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return 0;
	}
	
}