package fr.hb.mlang.electricitybusiness.security;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Transient adapter that implements {@link UserDetails} and wraps both {@link User} and
 * {@link UserAuth}, exposing their fields while keeping them separate (entity vs authentication).
 */
public record SecurityUserDetailsService(
    User user,
    UserAuth auth
) implements UserDetails {

  // Convenience factory
  public static SecurityUserDetailsService from(User user) {
    return new SecurityUserDetailsService(user, user.getAuth());
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return auth.getPasswordHash();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }

  @Override
  public boolean isEnabled() {
    return auth.getEmailVerified();
  }
}
