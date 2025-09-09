package fr.hb.mlang.electricitybusiness.modules.token;

import java.time.LocalDateTime;
import java.util.UUID;

public class GenericToken {

  private UUID id;
  private String tokenHash;
  private LocalDateTime expiresAt;
}
