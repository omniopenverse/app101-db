package com.example.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseRunner {

    public static void main(String[] args) {
        String jdbcUrl = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String changelogFile = System.getenv("DB_CHANGELOG_FILE_PATH");
        String driver = System.getenv().getOrDefault("DB_DRIVER", "org.postgresql.Driver");

        try {
            // Load JDBC driver explicitly
            Class.forName(driver);

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
                liquibase.update("");

                System.out.println("✅ Database schema updated successfully!");
            }
        } catch (LiquibaseException e) {
            System.err.println("❌ Liquibase error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ General error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
