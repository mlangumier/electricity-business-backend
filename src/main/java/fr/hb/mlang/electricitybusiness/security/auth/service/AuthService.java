package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.refresh.RefreshToken;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.EmailAvailableRequest;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginRequestDto;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginResponseDto;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

  /**
   * Checks the database to see if the email provided by the user in the registration form is
   * available.
   *
   * @param request Object containing the email with data validation.
   */
  void checkIsEmailAvailable(EmailAvailableRequest request);

  /**
   * Registers the new {@link User} and emails them a link with a verification token so they can
   * verify their account.
   *
   * @param request Data from the registration form
   */
  void register(RegisterRequest request);


  /**
   * Verifies the {@link User}'s account by checking that the token corresponds to the
   * {@link EmailVerificationToken} stored in the database, belongs to an existing non-verified user
   * and has a valid expiration date.
   *
   * @param token Raw token sent to the user by email.
   */
  void verifyAccount(String token);

  // sendResetPasswordEmail

  // updatePassword

  /**
   * Authenticates the {@link User} after verifying their credentials, and sets a cookie with a
   * refresh token to keep them authenticated.
   *
   * @param credentials Email and password provided by the user on login
   * @param response    Http response that will set the refresh token in the cookies
   * @return A DTO containing the user and the access token.
   */
  LoginResponseDto authenticateUser(LoginRequestDto credentials, HttpServletResponse response);

  /**
   * Automatically authenticates the user by returning a new <code>Access Token</code> if their
   * {@link RefreshToken} is still valid.
   *
   * @param request  Object containing the cookie with the refresh token
   * @param response Http response that will set the refresh token in the cookies
   * @return A DTO containing the user and the access token.
   */
  LoginResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response);

  /**
   * Logs out a user by deleting the current refresh token and setting the cookie's token to expire immediately.
   * @param request Object containing the cookie with the refresh token
   * @param response Http response that will set the token in the cookies
   */
  void logout(HttpServletRequest request, HttpServletResponse response);


}
