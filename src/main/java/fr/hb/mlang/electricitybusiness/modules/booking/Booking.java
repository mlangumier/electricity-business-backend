package fr.hb.mlang.electricitybusiness.modules.booking;

import fr.hb.mlang.electricitybusiness.modules.station.Station;
import fr.hb.mlang.electricitybusiness.modules.transaction.Transaction;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "index_booking_station", columnList = "station_id"),
    @Index(name = "index_booking_customer", columnList = "customer_id"),
    @Index(name = "index_booking_status", columnList = "status"),
})
public class Booking extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotNull
  @Column(name = "start", nullable = false, updatable = false)
  private Instant start;

  @NotNull
  @Column(name = "end", nullable = false, updatable = false)
  private Instant end;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private BookingStatus status;

  @ManyToOne(optional = false)
  @JoinColumn(name = "station_id", nullable = false)
  private Station station;

  @ManyToOne(optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
  private User customer;

  @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
  private Transaction transaction;

  /**
   * Required by JPA
   */
  public Booking() {
  }

  public Booking(
      Instant start,
      Instant end,
      Station station,
      User customer
  ) {
    this.start = start;
    this.end = end;
    this.status = BookingStatus.PENDING;
    this.station = station;
    this.customer = customer;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Instant getStart() {
    return start;
  }

  public void setStart(Instant start) {
    this.start = start;
  }

  public Instant getEnd() {
    return end;
  }

  public void setEnd(Instant end) {
    this.end = end;
  }

  public BookingStatus getStatus() {
    return status;
  }

  public void setStatus(BookingStatus status) {
    this.status = status;
  }

  public Station getStation() {
    return station;
  }

  public void setStation(Station station) {
    this.station = station;
  }

  public User getCustomer() {
    return customer;
  }

  public void setCustomer(User customer) {
    this.customer = customer;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Booking booking)) {
      return false;
    }
    return Objects.equals(getId(), booking.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Booking{" +
        "id=" + id +
        ", start=" + start +
        ", end=" + end +
        ", status=" + status +
        '}';
  }
}
