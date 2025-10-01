package fr.hb.mlang.electricitybusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan // Scan config files: allow initial set up check
@EnableJpaAuditing
public class ElectricityBusinessApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElectricityBusinessApplication.class, args);
  }
}
