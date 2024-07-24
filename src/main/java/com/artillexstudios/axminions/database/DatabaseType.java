package com.artillexstudios.axminions.database;

import com.artillexstudios.axminions.config.Config;
import com.artillexstudios.axminions.utils.FileUtils;
import com.zaxxer.hikari.HikariConfig;
import org.jooq.SQLDialect;

public enum DatabaseType {
    H2(SQLDialect.H2) {
        @Override
        public HikariConfig getConfig() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
            hikariConfig.setPoolName("axminions-database-pool");
            hikariConfig.setMaximumPoolSize(Config.DATABASE_MAXIMUM_POOL_SIZE);
            hikariConfig.addDataSourceProperty("url", "jdbc:h2:./" + FileUtils.PLUGIN_DIRECTORY.toFile() + "/data");
            return hikariConfig;
        }
    },
    SQLITE(SQLDialect.SQLITE) {
        @Override
        public HikariConfig getConfig() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
            hikariConfig.setPoolName("axminions-database-pool");
            hikariConfig.setMaximumPoolSize(Config.DATABASE_MAXIMUM_POOL_SIZE);
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + FileUtils.PLUGIN_DIRECTORY.toFile() + "/data");
            return hikariConfig;
        }
    },
    MySQL(SQLDialect.MYSQL) {
        @Override
        public HikariConfig getConfig() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName("axminions-database-pool");
            hikariConfig.setMaximumPoolSize(Config.DATABASE_MAXIMUM_POOL_SIZE);
            hikariConfig.setMinimumIdle(Config.DATABASE_MINIMUM_IDLE);
            hikariConfig.setMaxLifetime(Config.DATABASE_MAXIMUM_LIFETIME);
            hikariConfig.setKeepaliveTime(Config.DATABASE_KEEPALIVE_TIME);
            hikariConfig.setConnectionTimeout(Config.DATABASE_CONNECTION_TIMEOUT);

            hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
            hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.DATABASE_ADDRESS + ":" + Config.DATABASE_PORT + "/" + Config.DATABASE_DATABASE);
            hikariConfig.addDataSourceProperty("user", Config.DATABASE_USERNAME);
            hikariConfig.addDataSourceProperty("password", Config.DATABASE_PASSWORD);
            return hikariConfig;
        }
    };

    public static final DatabaseType[] entries = DatabaseType.values();
    private final SQLDialect dialect;

    DatabaseType(SQLDialect dialect) {
        this.dialect = dialect;
    }

    public static DatabaseType parse(String name) {
        for (DatabaseType entry : entries) {
            if (entry.name().equalsIgnoreCase(name)) {
                return entry;
            }
        }

        return DatabaseType.H2;
    }

    public SQLDialect getType() {
        return this.dialect;
    }

    public abstract HikariConfig getConfig();
}