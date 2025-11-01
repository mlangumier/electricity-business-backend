package fr.hb.mlang.electricitybusiness.security.auth.controller.dto;

import fr.hb.mlang.electricitybusiness.modules.user.domain.Role;
import java.util.UUID;

public record LoginResponseDto(
    String accessToken,
    UserAuth userAuth
    ) {

  public record UserAuth (
      UUID id,
      String email,
      Role role,
      String firstName,
      String lastName,
      String avatar,
      String preferences
  ) {}

}
