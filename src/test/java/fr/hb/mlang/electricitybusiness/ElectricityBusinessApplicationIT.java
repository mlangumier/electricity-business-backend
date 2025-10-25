package fr.hb.mlang.electricitybusiness;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ElectricityBusinessApplicationIT extends DatabaseConfigIT {

  @Test
  void contextLoads() {
  }

}
