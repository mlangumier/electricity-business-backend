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

@Entity
@Table(name = "user_auth")
public class UserAuth {

  @Id
  private UUID id;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "is_verified", nullable = false)
  private Boolean verified;

  @Column(name = "last_login", nullable = false)
  private Instant lastLogin;

  @MapsId
  @OneToOne
  @JoinColumn(name = "id", nullable = false)
  private User user;

  public UserAuth() {
    // Required by JPA
  }

  public UserAuth(UUID id, String passwordHash, Instant lastLogin, User user) {
    this.id = id;
    this.passwordHash = passwordHash;
    this.verified = Boolean.FALSE;
    this.lastLogin = lastLogin;
    this.user = user;
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

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
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
        ", verified=" + verified +
        ", lastLogin=" + lastLogin +
        '}';
  }
}
