package fr.hb.mlang.electricitybusiness.modules.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that contains the authentication user data. Shares their <code>Primary Key</code> with the
 * {@link  User} entity, since they are the same semantic entity.
 */
@Entity
@Table(name = "user_auth")
public class UserAuth {

  @Id
  private UUID id;

  @MapsId // Defines a shared PK for both entities
  @OneToOne(optional = false)
  @JoinColumn(name = "id")
  private User user;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "is_email_verified", nullable = false)
  private boolean emailVerified;

  @Column(name = "last_login", nullable = false)
  private Instant lastLogin;

  /**
   * Required by JPA
   */
  public UserAuth() {
  }

  /**
   * Creates credentials for the given {@link User}. The Primary Key will be inherited from the user
   * (see {@link MapsId}).
   */
  public UserAuth(User user, String passwordHash) {
    this.user = user;
    this.passwordHash = passwordHash;
    this.emailVerified = false;
    this.lastLogin = null;
  }

  public UserAuth(User user, String passwordHash, boolean emailVerified, Instant lastLogin) {
    this.user = user;
    this.passwordHash = passwordHash;
    this.emailVerified = emailVerified;
    this.lastLogin = lastLogin;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof UserAuth userAuth)) {
      return false;
    }
    return Objects.equals(getId(), userAuth.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "UserAuth{" +
        "id=" + id +
        ", passwordHash='" + passwordHash + '\'' +
        ", verified=" + emailVerified +
        ", lastLogin=" + lastLogin +
        '}';
  }
}
