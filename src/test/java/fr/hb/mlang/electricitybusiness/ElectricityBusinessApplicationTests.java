package fr.hb.mlang.electricitybusiness;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ElectricityBusinessApplicationTests extends DatabaseConfigTests {

  @Test
  void contextLoads() {
    System.out.println("Context loaded!");
  }

}
