package fr.hb.mlang.electricitybusiness.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
    @NotBlank String baseUrl,
    @NotNull Jwt jwt
) {

  public record Jwt(
      @NotNull Duration accessExpiration,
      @NotNull Duration refreshExpiration,
      @NotNull Duration verificationExpiration,
      @NotNull Duration passwordExpiration
  ) {

  }
}
