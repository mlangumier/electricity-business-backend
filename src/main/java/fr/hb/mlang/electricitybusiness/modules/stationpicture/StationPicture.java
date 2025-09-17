package fr.hb.mlang.electricitybusiness.modules.stationpicture;

import fr.hb.mlang.electricitybusiness.modules.station.Station;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "station_pictures", indexes = {
    @Index(name = "index_picture_station", columnList = "station_id"),
    @Index(name = "index_picture_featured", columnList = "station_id, is_featured")
})
public class StationPicture extends AuditedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank
  @Size(max = 512)
  @Column(name = "url", nullable = false, length = 512)
  private String url;

  @NotBlank
  @Column(name = "type", nullable = false, length = 10)
  private String type;

  @Column(name = "is_featured", nullable = false)
  private boolean featured = false;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "station_id", nullable = false)
  private Station station;

  /**
   * Required by JPA
   */
  public StationPicture() {
  }

  public StationPicture(String url, String type, Station station) {
    this.url = url;
    this.type = type;
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
        super.toString() +
        '}';
  }
}
