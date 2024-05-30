package com.summitsync.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

@TestConfiguration(proxyBeanMethods = false)
public class TestSummitSyncApplication {

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
	}

	public static void main(String[] args) {
		SpringApplication.from(SummitSyncApplication::main).with(TestSummitSyncApplication.class).run(args);
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
