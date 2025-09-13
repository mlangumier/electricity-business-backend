package fr.hb.mlang.electricitybusiness.modules.userprofile;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.shared.jpa.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends AuditedEntity {

  @Id
  private UUID id;

  @MapsId
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "id", nullable = false)
  private User user;

  @NotBlank
  @Size(min = 2, max = 50)
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @NotBlank
  @Size(min = 2, max = 50)
  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Past
  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @NotBlank
  @Size(max = 512)
  @Column(name = "home_address", nullable = false, length = 512)
  private String homeAddress;

  @Column(columnDefinition = "JSON", name = "preferences", nullable = false)
  private String preferences = "{}";

  @Size(max = 512)
  @Column(name = "avatar", length = 512)
  private String avatar;


  /**
   * Required by JPA
   */
  public UserProfile() {
  }

  public UserProfile(
      User user,
      String firstName,
      String lastName,
      LocalDate dateOfBirth,
      String homeAddress,
      String avatar
  ) {
    this.user = user;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.homeAddress = homeAddress;
    this.preferences = "{}";
    this.avatar = avatar;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
  }

  public String getPreferences() {
    return preferences;
  }

  public void setPreferences(String preferences) {
    this.preferences = preferences;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof UserProfile that)) {
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
    return "UserProfile{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        ", homeAddress='" + homeAddress + '\'' +
        ", preferences='" + preferences + '\'' +
        ", avatar='" + avatar + '\'' +
        '}';
  }
}
