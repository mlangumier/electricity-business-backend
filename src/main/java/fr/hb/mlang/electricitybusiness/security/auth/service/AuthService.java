package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.security.auth.web.dto.RegisterRequest;

public interface AuthService {

  // checkIsEmailIsAvailable

  /**
   * Registers the new {@link User} and emails them a link with a verification token so they can
   * verify their account.
   *
   * @param request Data from the registration form
   */
  void register(RegisterRequest request);

  // verifyAccount

  // sendResetPasswordEmail
  // updatePassword

  // login
  // refreshToken

  // logout


}
