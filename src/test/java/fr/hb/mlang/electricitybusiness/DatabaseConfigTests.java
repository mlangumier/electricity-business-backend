package fr.hb.mlang.electricitybusiness;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DatabaseConfigTests {
  static final MySQLContainer<?> mysql =
      new MySQLContainer<>(DockerImageName.parse("mysql:8.4"))
          .withDatabaseName("eb")
          .withUsername("test")
          .withPassword("test");

  @BeforeAll
  static void startContainer() {
    mysql.start();
  }

  @DynamicPropertySource
  static void registerProps(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", mysql::getJdbcUrl);
    r.add("spring.datasource.username", mysql::getUsername);
    r.add("spring.datasource.password", mysql::getPassword);
    // Temporary, replace with flyway migration when implemented:
    r.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    r.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
  }

}
