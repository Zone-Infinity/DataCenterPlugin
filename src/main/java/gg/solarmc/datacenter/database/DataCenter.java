package gg.solarmc.datacenter.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.solarmc.datacenter.config.DatabaseConfig;
import gg.solarmc.datacenter.database.data.Data;
import gg.solarmc.datacenter.database.data.DataKey;

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

    public DataCenter(DatabaseConfig databaseConfig, Logger logger) {
        this.logger = logger;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        hikariConfig.setJdbcUrl(String.format("jdbc:mariadb://%s:%s/%s",
                databaseConfig.host(),
                databaseConfig.port(),
                databaseConfig.database()));
        hikariConfig.setUsername(databaseConfig.user());
        hikariConfig.setPassword(databaseConfig.password());
        hikariConfig.setMaximumPoolSize(8);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");

        dataSource = new HikariDataSource(hikariConfig);
    }

    // TODO: Store the keys and their columns
    // TODO: When properties (columns) are changed in the key, alter the table to add the new columns
    public void registerKey(DataKey<?> key) {
        if (dataKeys.stream().noneMatch(it -> it.getName().equals(key.getName()))) {
            String createQuery = key.getCreateQuery();
            if (!createQuery.contains("IF NOT EXISTS")) {
                throw new IllegalArgumentException("Table create query should have `IF NOT EXISTS`");
            }

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
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
