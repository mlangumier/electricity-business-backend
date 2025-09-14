package fr.hb.mlang.electricitybusiness.modules.station;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.location.Location;
import fr.hb.mlang.electricitybusiness.modules.stationpicture.StationPicture;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import fr.hb.mlang.electricitybusiness.shared.utils.MoneyConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stations", indexes = {
    @Index(name = "index_station_available", columnList = "is_available"),
    @Index(name = "index_station_location_available", columnList = "location_id, is_available")
})
public class Station extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Size(max = 150)
  @Column(name = "label", nullable = false, length = 150)
  private String label;

  @Size(max = 512)
  @Column(name = "description", length = 512)
  private String description;

  @NotNull
  @Positive
  @Column(name = "max_power", nullable = false)
  private Integer maxPower;

  @Column(name = "is_wall_mounted", nullable = false)
  private boolean wallMounted;

  @NotNull
  @DecimalMin(value = "0.00")
  @Digits(integer = 3, fraction = 2)
  @Convert(converter = MoneyConverter.class)
  @Column(name = "price", nullable = false, precision = 5, scale = 2)
  private BigDecimal price;

  @Column(name = "is_available", nullable = false)
  private boolean available;

  @Size(max = 1024)
  @Column(name = "additional_info", length = 1024)
  private String additionalInfo;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<StationPicture> pictures;

  @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Booking> bookings = new HashSet<>();

  /**
   * Required by JPA
   */
  public Station() {
  }

  public Station(
      String label,
      String description,
      Integer maxPower,
      boolean wallMounted,
      BigDecimal price,
      boolean available,
      String additionalInfo,
      Location location
  ) {
    this.label = label;
    this.description = description;
    this.maxPower = maxPower;
    this.wallMounted = wallMounted;
    this.price = price;
    this.available = available;
    this.additionalInfo = additionalInfo;
    this.location = location;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getMaxPower() {
    return maxPower;
  }

  public void setMaxPower(Integer maxPower) {
    this.maxPower = maxPower;
  }

  public boolean getWallMounted() {
    return wallMounted;
  }

  public void setWallMounted(boolean wallMounted) {
    this.wallMounted = wallMounted;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public boolean getAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Set<StationPicture> getPictures() {
    return pictures;
  }

  public void setPictures(Set<StationPicture> pictures) {
    this.pictures = pictures;
  }

  public Set<Booking> getBookings() {
    return bookings;
  }

  public void setBookings(Set<Booking> bookings) {
    this.bookings = bookings;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Station station)) {
      return false;
    }
    return Objects.equals(getId(), station.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Station{" +
        "id=" + id +
        ", label='" + label + '\'' +
        ", description='" + description + '\'' +
        ", maxPower=" + maxPower +
        ", wallMounted=" + wallMounted +
        ", price=" + price +
        ", available=" + available +
        ", additionalInfo='" + additionalInfo + '\'' +
        '}';
  }
}
