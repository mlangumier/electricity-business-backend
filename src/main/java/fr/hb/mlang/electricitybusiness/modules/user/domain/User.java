package fr.hb.mlang.electricitybusiness.modules.user.domain;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.location.domain.Location;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.password.PasswordResetToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.refresh.RefreshToken;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity that contains basic user information, necessary for account creation.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "index_users_email", columnList = "email"),
    @Index(name = "index_users_phone", columnList = "phone_number")
})
public class User extends AuditedEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Email
  @Size(max = 255)
  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @NotBlank
  @Size(max = 60) // 60 is perfect for BCrypt; increase to 255 if we need to use something else
  @Column(name = "password_hash", nullable = false, length = 60)
  private String passwordHash;

  @Column(name = "is_email_verified", nullable = false)
  private boolean emailVerified;

  @Column(name = "last_login")
  private Instant lastLogin;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 32)
  private Role role = Role.USER;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile profile;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<Location> locations = new HashSet<>();

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  private Set<Booking> bookings = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private EmailVerificationToken emailVerificationToken;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private PasswordResetToken passwordResetToken;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<RefreshToken> refreshTokens = new HashSet<>();

  /**
   * Required by JPA
   */
  public User() {
  }

  /**
   * Entity constructor
   */
  public User(String email,String passwordHash) {
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean verified) {
    this.emailVerified = verified;
  }

  public Instant getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Instant lastLogin) {
    this.lastLogin = lastLogin;
  }


  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public UserProfile getProfile() {
    return profile;
  }

  public void setProfile(UserProfile profile) {
    if (this.profile == profile) return;
    if (this.profile != null) this.profile.setUser(null);
    this.profile = profile;
    if (profile != null) profile.setUser(this);
  }

  public Set<Location> getLocations() {
    return locations;
  }

  public Set<Booking> getBookings() {
    return bookings;
  }

  public EmailVerificationToken getEmailVerificationToken() {
    return emailVerificationToken;
  }

  public void setEmailVerificationToken(EmailVerificationToken emailVerificationToken) {
    if (this.emailVerificationToken == emailVerificationToken) return;
    if (this.emailVerificationToken != null) this.emailVerificationToken.setUser(null);
    this.emailVerificationToken = emailVerificationToken;
    if (emailVerificationToken != null) emailVerificationToken.setUser(this);
  }

  public PasswordResetToken getPasswordResetToken() {
    return passwordResetToken;
  }

  public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
    if (this.passwordResetToken ==  passwordResetToken) return;
    if (this.passwordResetToken != null) this.passwordResetToken.setUser(null);
    this.passwordResetToken = passwordResetToken;
    if (passwordResetToken != null) passwordResetToken.setUser(this);
  }

  public Set<RefreshToken> getRefreshTokens() {
    return refreshTokens;
  }

  //--- Helper methods

  public void addLocation(Location location) {
    if (location == null) {
      return;
    }
    this.locations.add(location);
    location.setUser(this);
  }

  public void removeLocation(Location location) {
    if (location == null) {
      return;
    }
    this.locations.remove(location);
    location.setUser(null);
  }

  public void addBooking(Booking booking) {
    if (booking == null) {
      return;
    }
    this.bookings.add(booking);
    booking.setCustomer(this);
  }

  public void removeBooking(Booking booking) {
    if (booking == null) {
      return;
    }
    this.bookings.remove(booking);
    booking.setCustomer(null);
  }

  public void addRefreshToken(RefreshToken refreshToken) {
    if (refreshToken == null) {
      return;
    }
    this.refreshTokens.add(refreshToken);
    refreshToken.setUser(this);
  }

  public void removeRefreshToken(RefreshToken refreshToken) {
    if (refreshToken == null) {
      return;
    }
    this.refreshTokens.remove(refreshToken);
    refreshToken.setUser(null);
  }

  //--- Authentication

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
  }

  @Override
  public String getPassword() {
    return this.passwordHash;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isEnabled() {
    return this.emailVerified;
  }

  //--- Overrides

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof User user)) {
      return false;
    }
    return Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", role=" + role +
        super.toString() +
        '}';
  }
}
