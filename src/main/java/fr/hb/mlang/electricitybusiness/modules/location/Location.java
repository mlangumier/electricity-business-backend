package fr.hb.mlang.electricitybusiness.modules.location;

import java.util.UUID;
import org.springframework.data.geo.Point;

public class Location {

  private UUID id;
  private String address;
  private String address2;
  private String city;
  private String postalCode;
  private Point coordinates; // Check & test
  private String otherInformation; // Access info, portal, private street, etc.

  //private User user; //ManyToOne
  //private Set<Station> stations; //OneToMany
}
