package fr.hb.mlang.electricitybusiness.security;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
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

  /**
   * Authenticates the user with their credentials (username & password).
   *
   * @return the authenticated user.
   */
  @Bean
  public AuthenticationManager getAuthenticationManager() throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Finds a {@link User} using their username (email) to authenticate them.
   *
   * @return the found user.
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepository
        .findByEmail(username)
        .map(SecurityUserDetails::from)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Couldn't find user with email: " + username));
  }

  /**
   * Creates a password encoder for the user's password.
   *
   * @return the password encoder.
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
