package fr.hb.mlang.electricitybusiness.modules.tokens.password;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name = "password_reset_token", indexes = {
    @Index(name = "index_prt_expires_at", columnList = "expires_at")
})
public class PasswordResetToken {

  @Id
  private UUID id;

  @MapsId
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotBlank
  @Size(max = 64)
  @Column(name = "token_hash", nullable = false, updatable = false, length = 64)
  private String tokenHash;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @NotNull
  @Future
  @Column(name = "expires_at", nullable = false, updatable = false)
  private Instant expiresAt;

  /**
   * Required by JPA
   */
  public PasswordResetToken() {
  }

  /**
   * Creates a token to verify the reset password demande of the user. PK inherited from the user
   * (see {@link MapsId}).
   */
  public PasswordResetToken(User user, String tokenHash, Instant expiresAt) {
    this.user = user;
    this.tokenHash = tokenHash;
    this.expiresAt = expiresAt;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PasswordResetToken that)) {
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
    return "PasswordResetToken{" +
        "id=" + id +
        ", tokenHash='" + tokenHash + '\'' +
        ", createdAt=" + createdAt +
        ", expiresAt=" + expiresAt +
        '}';
  }
}
