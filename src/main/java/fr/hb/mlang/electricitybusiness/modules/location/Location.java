package fr.hb.mlang.electricitybusiness.modules.location;

import fr.hb.mlang.electricitybusiness.modules.station.Station;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.geo.Point;

@Entity
@Table(name = "locations", indexes = {
    @Index(name = "index_location_city", columnList = "city"),
    @Index(name = "index_location_postal_code", columnList = "postal_code")
})
public class Location extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Size(max = 255)
  @Column(name = "address", nullable = false)
  private String address;

  @Size(max = 255)
  @Column(name = "address_2")
  private String address2;

  @NotBlank
  @Size(max = 100)
  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @NotBlank
  @Size(max = 5)
  @Column(name = "postal_code", nullable = false, length = 5)
  private String postalCode;

  @NotNull
  @JdbcTypeCode(SqlTypes.GEOMETRY)
  @Column(name = "coordinates", nullable = false, columnDefinition = "POINT SRID 4326")
  private Point coordinates;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Station> stations = new HashSet<>();

  /**
   * Required by JPA
   */
  public Location() {
  }

  /**
   * Entity constructor
   */
  public Location(String address, String city, String postalCode, Point coordinates, User user) {
    this.address = address;
    this.city = city;
    this.postalCode = postalCode;
    this.coordinates = coordinates;
    this.user = user;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public Point getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Point coordinates) {
    this.coordinates = coordinates;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Set<Station> getStations() {
    return stations;
  }

  public void setStations(Set<Station> stations) {
    this.stations = stations;
  }

  //--- Helper methods

  public void addStation(Station station) {
    if (station == null) return;
    this.stations.add(station);
    station.setLocation(this);
  }

  public void removeStation(Station station) {
    if (station == null) return;
    this.stations.remove(station);
    station.setLocation(null);
  }

  //--- Overrides

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Location location)) {
      return false;
    }
    return Objects.equals(getId(), location.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", address='" + address + '\'' +
        ", address2='" + address2 + '\'' +
        ", city='" + city + '\'' +
        ", postalCode='" + postalCode + '\'' +
        ", coordinates=" + coordinates +
        super.toString() +
        '}';
  }
}
