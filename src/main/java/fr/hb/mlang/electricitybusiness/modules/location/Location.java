package fr.hb.mlang.electricitybusiness.modules.location;

import fr.hb.mlang.electricitybusiness.modules.station.Station;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.geo.Point;

@Entity
@Table(name = "locations")
public class Location extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "address_2")
  private String address2;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  @Column(name = "coordinates", nullable = false)
  private Point coordinates;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
  private Set<Station> stations = new HashSet<>();

  public Location() {
    // Required by JPA
  }

  public Location(
      UUID id,
      String address,
      String address2,
      String city,
      String postalCode,
      Point coordinates,
      User user
  ) {
    this.id = id;
    this.address = address;
    this.address2 = address2;
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
        '}';
  }
}
