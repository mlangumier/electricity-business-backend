package fr.hb.mlang.electricitybusiness.security;

import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

  private final UserRepository userRepository;

  public ApplicationConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Provides the authentication manager with the correct objects to authenticate the user
   *
   * @param userDetailsService the service used to authenticate the user
   * @param passwordEncoder    the encoder used to encode the user's password
   * @return the authentication provider.
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  /**
   * Authenticates the user with their credentials (username & password).
   *
   * @return the authenticated user.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Finds a {@link UserDetails} using their username (email) to authenticate them.
   *
   * @return the found user.
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepository
        .findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Failed to find user with email: " + username));
  }

  /**
   * Creates a password encoder for the user's password.
   *
   * @return the BCrypt password encoder.
   */
  @Bean
  @Primary
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Creates a password encoder that we'll use to store an opaque Refresh token
   *
   * @return the argon2 password encoder.
   */
  @Bean
  public Argon2PasswordEncoder refreshTokenEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }
}
