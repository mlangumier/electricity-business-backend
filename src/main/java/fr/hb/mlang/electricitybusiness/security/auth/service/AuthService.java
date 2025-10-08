package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.EmailAvailableRequest;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.RegisterRequest;

public interface AuthService {

  /**
   * Checks the database to see if the email provided by the user in the registration form is
   * available.
   *
   * @param request Object containing the email with data validation.
   * @return {true} is the email is available, {false} if already in use.
   */
  Boolean checkIsEmailAvailable(EmailAvailableRequest request);

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
