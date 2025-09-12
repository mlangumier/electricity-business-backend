package fr.hb.mlang.electricitybusiness.modules.station;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.location.Location;
import fr.hb.mlang.electricitybusiness.modules.stationpicture.StationPicture;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stations")
public class Station extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "label", nullable = false)
  private String label;

  @Column(name = "description")
  private String description;

  @Column(name = "max_power", nullable = false)
  private Integer maxPower;

  @Column(name = "is_wall_mounted", nullable = false)
  private Boolean wallMounted;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "is_available", nullable = false)
  private Boolean available;

  @Column(name = "additional_info")
  private String additionalInfo;

  @ManyToOne
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<StationPicture> pictures;

  @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
  private Set<Booking> bookings = new HashSet<>();

  public Station() {
    // Required by JPA
  }

  public Station(
      UUID id,
      String label,
      String description,
      Integer maxPower,
      Boolean wallMounted,
      BigDecimal price,
      Boolean available,
      String additionalInfo,
      Location location
  ) {
    this.id = id;
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

  public Boolean getWallMounted() {
    return wallMounted;
  }

  public void setWallMounted(Boolean wallMounted) {
    this.wallMounted = wallMounted;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
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
