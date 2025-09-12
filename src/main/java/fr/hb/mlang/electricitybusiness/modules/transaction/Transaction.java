package fr.hb.mlang.electricitybusiness.modules.transaction;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "external_reference", nullable = false, updatable = false)
  private UUID externalReference;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private TransactionStatus status;

  @Column(name = "amount", nullable = false, updatable = false)
  private BigDecimal amount;

  @OneToOne
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  public Transaction() {
    // Required by JPA
  }

  public Transaction(
      UUID id,
      UUID externalReference,
      BigDecimal amount,
      Booking booking
  ) {
    this.id = id;
    this.externalReference = externalReference;
    this.status = TransactionStatus.PENDING;
    this.amount = amount;
    this.booking = booking;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getExternalReference() {
    return externalReference;
  }

  public void setExternalReference(UUID externalReference) {
    this.externalReference = externalReference;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Booking getBooking() {
    return booking;
  }

  public void setBooking(Booking booking) {
    this.booking = booking;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Transaction that)) {
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
    return "Transaction{" +
        "id=" + id +
        ", externalReference=" + externalReference +
        ", status=" + status +
        ", amount=" + amount +
        '}';
  }
}
