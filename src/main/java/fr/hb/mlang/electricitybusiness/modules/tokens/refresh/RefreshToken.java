package fr.hb.mlang.electricitybusiness.modules.tokens.refresh;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "refresh_token", indexes = {
    @Index(name = "index_rt_expires_at", columnList = "expires_at")
})
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Size(max = 64)
  @Column(name = "token_hash", nullable = false, updatable = false, unique = true, length = 64)
  private String tokenHash;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @NotNull
  @Future
  @Column(name = "expires_at", nullable = false, updatable = false)
  private Instant expiresAt;

  @Size(max = 255)
  @Column(name = "device_info", length = 255)
  private String deviceInfo;

  @Column(name = "is_revoked", nullable = false)
  private boolean revoked = false;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /**
   * Required by JPA
   */
  public RefreshToken() {
  }

  /**
   * Minimal constructor
   */
  public RefreshToken(String tokenHash, Instant expiresAt, User user) {
    this.tokenHash = tokenHash;
    this.expiresAt = expiresAt;
    this.user = user;
  }

  /**
   * Full constructor
   */
  public RefreshToken(
      UUID id,
      String tokenHash,
      Instant createdAt,
      Instant expiresAt,
      String deviceInfo,
      boolean revoked,
      User user
  ) {
    this.id = id;
    this.tokenHash = tokenHash;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.deviceInfo = deviceInfo;
    this.revoked = revoked;
    this.user = user;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTokenHash() {
    return tokenHash;
  }

  public void setTokenHash(String tokenHash) {
    this.tokenHash = tokenHash;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Instant expiresAt) {
    this.expiresAt = expiresAt;
  }

  public String getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(String deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  public boolean getRevoked() {
    return revoked;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof RefreshToken that)) {
      return false;
    }
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "RefreshToken{" +
        "id=" + id +
        ", tokenHash='" + tokenHash + '\'' +
        ", createdAt=" + createdAt +
        ", expiresAt=" + expiresAt +
        (deviceInfo != null ? ", deviceInfo=" + deviceInfo + '\'' : "") +
        ", revoked=" + revoked +
        '}';
  }
}

