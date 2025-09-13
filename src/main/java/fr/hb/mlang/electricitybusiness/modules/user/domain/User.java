package fr.hb.mlang.electricitybusiness.modules.user.domain;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.location.Location;
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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Entity that contains basic user information, necessary for account creation.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "index_users_email", columnList = "email"),
    @Index(name = "index_users_phone", columnList = "phone_number")
})
public class User extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Email
  @Size(max = 255)
  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Size(max = 15)
  @Pattern(regexp = "^\\+?[0-9 .()-]{7,15}$")
  @Column(name = "phone_number", unique = true, length = 15)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 32)
  private Role role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserAuth auth;

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
