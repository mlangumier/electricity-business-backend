package fr.hb.mlang.electricitybusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // Scan config files: allow initial set up check
public class ElectricityBusinessApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElectricityBusinessApplication.class, args);
  }
}
