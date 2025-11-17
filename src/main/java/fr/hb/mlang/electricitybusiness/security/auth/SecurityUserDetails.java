package fr.hb.mlang.electricitybusiness.security.auth;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//TODO: auth -> replace this with User
/**
 * Transient adapter that implements {@link UserDetails} and wraps both {@link User} and
 * {@link UserAuth}, exposing their fields while keeping them separate (entity vs authentication).
 */
public record SecurityUserDetails(
    User user,
    UserAuth auth
) implements UserDetails {

  // Convenience factory
  public static SecurityUserDetails from(User user) {
    return new SecurityUserDetails(user, user.getAuth());
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
