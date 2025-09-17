package fr.hb.mlang.electricitybusiness.modules.transaction;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import fr.hb.mlang.electricitybusiness.shared.utils.MoneyConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "index_tx_booking", columnList = "booking_id"),
    @Index(name = "index_tx_status", columnList = "status")
})
public class Transaction extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank
  @Size(max = 100)
  @Column(name = "external_reference", unique = true, nullable = false, updatable = false, length = 100)
  private UUID externalReference;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private TransactionStatus status = TransactionStatus.REQUIRES_PAYMENT;

  @NotNull
  @DecimalMin(value = "0.00")
  @Digits(integer = 3, fraction = 2)
  @Convert(converter = MoneyConverter.class)
  @Column(name = "amount", nullable = false, updatable = false, precision = 5, scale = 2)
  private BigDecimal amount;

  @OneToOne(optional = false)
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  /**
   * Required by JPA
   */
  public Transaction() {
  }

  public Transaction(UUID externalReference, BigDecimal amount, Booking booking) {
    this.externalReference = externalReference;
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
        super.toString() +
        '}';
  }
}
