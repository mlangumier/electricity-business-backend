package fr.hb.mlang.electricitybusiness.modules.user.domain;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.location.Location;
import fr.hb.mlang.electricitybusiness.modules.token.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.token.password.PasswordResetToken;
import fr.hb.mlang.electricitybusiness.modules.token.refresh.RefreshToken;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends AuditedEntity {
  //TODO: Split into:
  // - `User`: domain, only user info no credentials (remove password)
  // - `UserAuth` (new table): authentication only (user credentials), same `id`(PK) as User
  // - `SecurityUser`: implements UserDetails, adapts User-UserAuth
  // -> Separation of concerns, easy to scale

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone_number", unique = true)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false)
  private UserAuth auth;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile profile;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<Location> locations = new HashSet<>();

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  private Set<Booking> bookings = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private EmailVerificationToken emailVerificationToken;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private PasswordResetToken passwordResetToken;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<RefreshToken> refreshTokens = new HashSet<>();

  public User() {
    // Required by JPA
  }

  public User(UUID id, String email, String phoneNumber, Role role) {
    this.id = id;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.role = role;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public UserAuth getAuth() {
    return auth;
  }

  public void setAuth(UserAuth auth) {
    this.auth = auth;
  }

  public UserProfile getProfile() {
    return profile;
  }

  public void setProfile(UserProfile profile) {
    this.profile = profile;
  }

  public Set<Location> getLocations() {
    return locations;
  }

  public void setLocations(Set<Location> locations) {
    this.locations = locations;
  }

  public Set<Booking> getBookings() {
    return bookings;
  }

  public void setBookings(Set<Booking> bookings) {
    this.bookings = bookings;
  }

  public EmailVerificationToken getEmailVerificationToken() {
    return emailVerificationToken;
  }

  public void setEmailVerificationToken(EmailVerificationToken emailVerificationToken) {
    this.emailVerificationToken = emailVerificationToken;
  }

  public PasswordResetToken getPasswordResetToken() {
    return passwordResetToken;
  }

  public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
    this.passwordResetToken = passwordResetToken;
  }

  public Set<RefreshToken> getRefreshTokens() {
    return refreshTokens;
  }

  public void setRefreshTokens(Set<RefreshToken> refreshTokens) {
    this.refreshTokens = refreshTokens;
  }

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
        ", phoneNumber='" + phoneNumber + '\'' +
        ", role=" + role +
        '}';
  }
}
