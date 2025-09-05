package fr.hb.mlang.electricitybusiness;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

@SpringBootApplication
public class ElectricityBusinessApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElectricityBusinessApplication.class, args);
  }

  //TODO:
  // - Add explanation (why & how with IntelliJ + possible with local env) & variable names in README file
  // - Continue config, set & test other env variables
  // - Create an AppProperties files to use instead of @Value everywhere
  // - Create a StartupCheck file that verifies the presence of env variables before start up
  // - Remove this
  @Bean
  CommandLineRunner checkEnv(@Value("${app.base.url}") String baseUrl) {
    return args -> {
      Assert.notNull(baseUrl, "'app_base_url' env variable not found");
      System.out.println("App base URL = " + baseUrl);
    };
  }
}
