package fr.hb.mlang.electricitybusiness.security;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.security.auth.SecurityUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

  //@Bean
  //public AuthenticationProvider authenticationProvider() {
  //  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
  //  authProvider.setUserDetailsService(this.userDetailsService());
  //  authProvider.setPasswordEncoder(this.bCryptPasswordEncoder());
  //  return authProvider;
  //}

  /**
   * Authenticates the user with their credentials (username & password).
   *
   * @return the authenticated user.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
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
        .map(SecurityUserDetails::from) //WARNING: could this be the issue?
        .orElseThrow(() -> new UsernameNotFoundException(
            "Couldn't find user with email: " + username));
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
  public Argon2PasswordEncoder argon2() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }
}
