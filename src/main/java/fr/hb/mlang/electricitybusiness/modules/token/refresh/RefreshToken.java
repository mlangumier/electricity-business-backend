package fr.hb.mlang.electricitybusiness.modules.token.refresh;

import fr.hb.mlang.electricitybusiness.modules.token.GenericToken;

public class RefreshToken extends GenericToken {

  private String deviceInfo;
  private Boolean revoked;

  //private User user; //ManyToOne (one per device)
}
