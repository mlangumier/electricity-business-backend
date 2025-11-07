package fr.hb.mlang.electricitybusiness.security;

import java.time.Duration;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

  private CookieUtil() {
    // Prevents initialization
  }

  /**
   * Creates a cookie with the refresh token, to be sent to the user's device.
   *
   * @param token Refresh token
   * @param tokenDuration Duration before the token is expired
   * @return the generated cookie
   */
  public static ResponseCookie createRefreshTokenCookie(String token, Duration tokenDuration) {
    return ResponseCookie
        .from("refreshToken", token)
        .httpOnly(true)
        .secure(false) // Update to `true` after setting up HTTPS environments & for deployment
        .path("/api/v1/auth")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(tokenDuration)
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
