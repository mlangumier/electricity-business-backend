package fr.hb.mlang.electricitybusiness.security;

import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfig {

  private final AuthenticationConfiguration authConfig;
  private final UserRepository userRepository;

  public ApplicationConfig(AuthenticationConfiguration authConfig, UserRepository userRepository) {
    this.authConfig = authConfig;
    this.userRepository = userRepository;
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepository
        .findByEmail(username)
        .map(SecurityUserDetailsService::from)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Couldn't find user with email: " + username));
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
