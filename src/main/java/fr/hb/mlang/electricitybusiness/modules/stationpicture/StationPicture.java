package fr.hb.mlang.electricitybusiness.modules.stationpicture;

import fr.hb.mlang.electricitybusiness.modules.station.Station;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "station_pictures")
public class StationPicture extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "url", nullable = false)
  private String url;

  @Column(name = "type", nullable = false)
  private String type;

  @Column(name = "is_featured", nullable = false)
  private boolean featured;

  @ManyToOne(optional = false)
  @JoinColumn(name = "station_id")
  private Station station;

  /**
   * Required by JPA
   */
  public StationPicture() {
  }

  public StationPicture(UUID id, String url, String type, Boolean featured, Station station) {
    this.id = id;
    this.url = url;
    this.type = type;
    this.featured = featured;
    this.station = station;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Boolean getFeatured() {
    return featured;
  }

  public void setFeatured(Boolean featured) {
    this.featured = featured;
  }

  public Station getStation() {
    return station;
  }

  public void setStation(Station station) {
    this.station = station;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof StationPicture that)) {
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
    return "StationPicture{" +
        "id=" + id +
        ", url='" + url + '\'' +
        ", type='" + type + '\'' +
        ", featured=" + featured +
        '}';
  }
}
