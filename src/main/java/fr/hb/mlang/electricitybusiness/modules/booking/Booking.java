package fr.hb.mlang.electricitybusiness.modules.booking;

import java.time.Instant;
import java.util.UUID;

public class Booking {

  private UUID id;
  private Instant start;
  private Instant end;
  private BookingStatus status;

  //private Station station; //ManyToOne
  //private User user; //ManyToOne
  //private Transaction transaction; //OneToOne
}
