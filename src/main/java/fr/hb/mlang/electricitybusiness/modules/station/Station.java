package fr.hb.mlang.electricitybusiness.modules.station;

import java.math.BigDecimal;
import java.util.UUID;

public class Station {

  private UUID id;
  private String label;
  private String description;
  private Integer maxPower;
  private Boolean wallMounted;
  private BigDecimal price;
  private Boolean available;

  //private Location location; //ManyToOne
  //private Set<StationPicture> pictures; //OneToMany
  //private Set<Booking> bookings; //OneToMany
}
