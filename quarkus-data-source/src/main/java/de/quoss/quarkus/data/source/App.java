package de.quoss.quarkus.data.source;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;

@QuarkusMain
public class App implements QuarkusApplication {

    private final DataSource ds;
    
    public App (final DataSource ds) {
        this.ds = ds;
    }

    public static void main(String... args) {
        Quarkus.run(App.class, args);
    }
    
    @Override
    public int run(String... args) {
        Log.info("Start");
        final String arg = Optional.ofNullable(args.length == 0 ? "" : args[0]).orElse("");
        switch (arg) {
            case "agroal-result-set-leaked", "" -> agroalResultSetLeaked();
            default -> throw new IllegalStateException("Unknown function: " + args[0]);
        }
        Log.info("End");
        return 0;
    }
    
    private void agroalResultSetLeaked() {
        Log.info("Create customer table: " + executeSql("CREATE TABLE CUSTOMER ("
                + "    ID   INT,"
                + "    NAME VARCHAR(50)"
                + ")"));
        Log.info("Insert customer record: " + executeSql("INSERT INTO CUSTOMER "
                + "( ID, NAME ) VALUES ( 0, 'Clemens Quoß' )"));
        Log.info("Select customer records: " + executeSql("SELECT * FROM CUSTOMER "));
        Log.info("Select customer records (nested-arm): " + executeSqlNestedArm("SELECT * FROM CUSTOMER "));
        Log.info("Drop customer table: " + executeSql("DROP TABLE CUSTOMER"));
    }
    
    private List<Map<String, Object>> executeSql(final String sql) {
        try (final Connection c = ds.getConnection();
                final Statement s = c.createStatement()) {
            if (s.execute(sql)) {
                final ResultSet rs = s.getResultSet();
                return processResultSet(rs);
            } else {
                return List.of(Map.of("UPDATE_COUNT", s.getUpdateCount()));
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Map<String, Object>> executeSqlNestedArm(final String sql) {
        try (final Connection c = ds.getConnection();
                final Statement s = c.createStatement()) {
            if (s.execute(sql)) {
                try (final ResultSet rs = s.getResultSet()) {
                    return processResultSet(rs);
                }
            } else {
                return List.of(Map.of("UPDATE_COUNT", s.getUpdateCount()));
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<Map<String, Object>> processResultSet(final ResultSet rs) throws SQLException {
        final List<Map<String, Object>> result = new ArrayList<>();
        final ResultSetMetaData md = rs.getMetaData();
        while (rs.next()) {
            final Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            result.add(Collections.unmodifiableMap(row));
        }
        return Collections.unmodifiableList(result);
    }

}

