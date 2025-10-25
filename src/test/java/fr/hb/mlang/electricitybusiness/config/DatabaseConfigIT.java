package fr.hb.mlang.electricitybusiness.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * Spring Boot test container config that is created with the application context and automatically
 * injects the JDBC props.
 * @implNote Serves as a base environment for Integration Testing.
 */
@ActiveProfiles("test")
public abstract class DatabaseConfigIT {

  @Container
  @ServiceConnection
  static final MySQLContainer<?> mysql =
      new MySQLContainer<>("mysql:8.4");
}

