package com.summitsync.api.integrationtest.testcontainers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
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
}
