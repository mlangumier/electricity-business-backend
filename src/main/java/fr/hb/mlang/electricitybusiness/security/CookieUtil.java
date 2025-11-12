package fr.hb.mlang.electricitybusiness.security;

import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

  private static final String COOKIE_NAME = "refreshToken";

  private CookieUtil() {
    // Prevents initialization
  }

  /**
   * Creates a cookie with the refresh token, to be sent to the user's device.
   *
   * @param token         Refresh token
   * @param tokenDuration Duration before the token is expired
   * @return the generated cookie
   */
  public static ResponseCookie createRefreshTokenCookie(String token, Duration tokenDuration) {
    return ResponseCookie
        .from(COOKIE_NAME, token)
        .httpOnly(true)
        .secure(false) // Update to `true` after setting up HTTPS environments & for deployment
        .path("/api/v1/auth/refresh")
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
        .from(COOKIE_NAME, "")
        .httpOnly(true)
        .secure(false)
        .path("/api/v1/auth/refresh")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(0)
        .build();
  }

  /**
   * Reads the list of cookies from a request, finds the one containing the refresh token and
   * returns it if present.
   *
   * @param cookies The list of cookies in the request.
   * @return The cookie containing the refresh token or null.
   */
  public static String readRefreshTokenCookie(Cookie[] cookies) {
    return Arrays
        .stream(Optional.ofNullable(cookies).orElse(new Cookie[0]))
        .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
        .map(Cookie::getValue)
        .findFirst().orElse(null);
  }
}
