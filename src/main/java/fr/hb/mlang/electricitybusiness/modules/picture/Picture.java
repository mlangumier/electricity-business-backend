package fr.hb.mlang.electricitybusiness.modules.picture;

import java.util.UUID;

public class Picture {

  private UUID id;
  private String fileName;
  private String url;
  private String alt;
  private Boolean featured; // First image

  // TODO: Check & verify:
  //private UUID entityId; // Foreign Key not linked to any table
  //private enum entityType;

  // Else, use this:
  //private Station station; //ManyToOne
}
