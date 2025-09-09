package fr.hb.mlang.electricitybusiness.modules.profile;

import io.swagger.v3.core.util.Json;
import java.time.LocalDate;
import java.util.UUID;

public class Profile {

  private UUID id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String homeAddress;
  private Json preferences;
  private String avatar;

  //private User user; //OneToOne
}
