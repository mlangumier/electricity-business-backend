package fr.hb.mlang.electricitybusiness.security;

import fr.hb.mlang.electricitybusiness.config.AppProperties;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

  private static AppProperties appProperties;

  /**
   * Creates a cookie with the refresh token, to be sent to the user's device.
   *
   * @param token Refresh token
   * @return the generated cookie
   */
  public static ResponseCookie createRefreshTokenCookie(String token) {
    return ResponseCookie
        .from("refreshToken", token)
        .httpOnly(true)
        .secure(false)
        .path("/api/v1/auth")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(appProperties.jwt().refreshExpiration())
        .build();
  }

  /**
   * Invalidates the user's cookie by generating an empty one, effectively preventing them from
   * accessing authenticated routes until they log in again.
   *
   * @return the cookie without a refresh token
   */
  public static ResponseCookie cleanRefreshTokenCookie() {
    return ResponseCookie
        .from("refreshToken", "")
        .httpOnly(true)
        .secure(false)
        .path("/api/v1/auth")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(0)
        .build();
  }
}
