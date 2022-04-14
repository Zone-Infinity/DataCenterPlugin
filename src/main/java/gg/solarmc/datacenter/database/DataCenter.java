package gg.solarmc.datacenter.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.solarmc.datacenter.database.data.Data;
import gg.solarmc.datacenter.database.data.DataKey;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataCenter {
    private final Logger logger;
    private final List<DataKey<?>> dataKeys = new ArrayList<>();

    private final HikariDataSource dataSource;

    public DataCenter(Logger logger, Path dataFolder) {
        this.logger = logger;

        // Create file for database
        Path dbFilePath = dataFolder.resolve("database.db");
        try {
            if (Files.notExists(dbFilePath)) {
                Files.createDirectories(dbFilePath.getParent());
                Files.createFile(dbFilePath);
                logger.info("Created database.db");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        // Load sqlite-jdbc
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbFilePath);
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    // TODO: When properties (columns) are changed in the key, alter the table to add the new columns
    public void registerKey(DataKey<?> key) {
        if (dataKeys.stream().noneMatch(it -> it.getName().equals(key.getName()))) {
            dataSource.setMaximumPoolSize(dataSource.getMaximumPoolSize() + 20);

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                String createQuery = key.getCreateQuery();
                statement.execute(createQuery);

                dataKeys.add(key);
                logger.info(key.getName() + " table initialized");
            } catch (SQLException e) {
                throw new RuntimeException("Exception while initializing the table", e);
            }
        } else {
            throw new IllegalArgumentException(key.getName() + " already exists.");
        }
    }

    public <T extends Data> T get(DataKey<T> key, String uuid) {
        return key.getData(this, uuid);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
