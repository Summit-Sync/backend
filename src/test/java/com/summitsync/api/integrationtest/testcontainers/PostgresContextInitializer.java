package com.summitsync.api.integrationtest.testcontainers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?>postgres=new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
            .withDatabaseName("test_summitsync")
            .withUsername("test_db_user")
            .withPassword("test_db-password");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgres.start();

        TestPropertyValues.of(
                        "spring.datasource.url=" + postgres.getJdbcUrl(),
                        "spring.datasource.username=" + postgres.getUsername(),
                        "spring.datasource.password=" + postgres.getPassword())
                .applyTo(applicationContext.getEnvironment());
    }

    public static void cleanAllTables(JdbcTemplate jdbcTemplate) {
        var query = """
CREATE OR REPLACE FUNCTION truncate_tables(username IN VARCHAR) RETURNS void AS $$
DECLARE
statements CURSOR FOR
SELECT tablename FROM pg_tables
WHERE tableowner = username AND schemaname = 'public';
BEGIN
FOR stmt IN statements LOOP
EXECUTE 'TRUNCATE TABLE ' || quote_ident(stmt.tablename) || ' CASCADE;';
END LOOP;
END;
$$ LANGUAGE plpgsql;

BEGIN;
SELECT truncate_tables('test_db_user');
COMMIT;""";

        try {
            jdbcTemplate.execute(query);
            System.out.println("All tables truncated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error occurred while truncating tables: " + e.getMessage());
        }
    }
}
