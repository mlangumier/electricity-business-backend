package fr.hb.mlang.electricitybusiness.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
public abstract class DatabaseConfigTests {

  //TODO: Add persistence option to keep the container alive during all integration tests (need shorter lifetime)
  //TODO: Create one set of data for all tests, with fixed entities for expected fetch requests

  static final MySQLContainer<?> mysql =
      new MySQLContainer<>(DockerImageName.parse("mysql:8.4"));

  @BeforeAll
  static void startContainer() {
    mysql.start();
  }

  @AfterAll
  static void stopContainer() {
    mysql.stop();
  }

  @DynamicPropertySource
  static void registerProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
    registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    // Temporary (replace with flyway migration when implemented)
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
  }
}
