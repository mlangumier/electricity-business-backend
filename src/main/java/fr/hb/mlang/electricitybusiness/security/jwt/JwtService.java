package fr.hb.mlang.electricitybusiness.security.jwt;

import com.auth0.jwt.JWT;
import fr.hb.mlang.electricitybusiness.config.AppProperties;
import fr.hb.mlang.electricitybusiness.security.auth.exception.RefreshTokenException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final AppProperties.Jwt jwtProps;
  private final JwtKeyManager jwtKeyManager;

  public JwtService(AppProperties appProps, JwtKeyManager jwtKeyManager) {
    this.jwtProps = appProps.jwt();
    this.jwtKeyManager = jwtKeyManager;
  }

  public String generateAccessToken(String email) {
    return this.generateToken(email, jwtProps.accessExpiration());
  }

  public String generateRefreshToken(String email) {
    return this.generateToken(email, jwtProps.refreshExpiration());
  }

  /**
   * Generic method that generates a new JWT.
   *
   * @param email              Email of the user who will receive the token
   * @param expirationDuration How long should the token be active
   * @return the generated token.
   */
  private String generateToken(String email, Duration expirationDuration) {
    Instant expirationDate = Instant.now().plus(expirationDuration);
    return JWT
        .create()
        .withSubject(email)
        .withIssuedAt(Instant.now())
        .withExpiresAt(expirationDate)
        .sign(jwtKeyManager.getAlgorithm());
  }

  /**
   * Checks the validity of a token by verifying if the email corresponds to the user and if the
   * token is expired or not.
   *
   * @param token The token to verify
   * @param email Email of the user who owns the token
   * @return {true} if the user's email and token's email correspond and the token hasn't expired.
   */
  public boolean isTokenValid(String token, String email) {
    if (token == null || token.isBlank()) {
      throw new RefreshTokenException("Token is missing or empty");
    }
    String tokenEmail = this.extractUserEmail(token);
    return (tokenEmail.equals(email) && !this.isTokenExpired(token));
  }

  /**
   * Checks if the token is expired.
   *
   * @param token The token to verify.
   * @return {true} if the token is expired, else {false}.
   */
  public boolean isTokenExpired(String token) {
    return this.extractExpiration(token).before(new Date());
  }

  /**
   * Gets the expiration date of the token.
   *
   * @param token The token to verify
   * @return The expiration date.
   */
  public Date extractExpiration(String token) {
    return JWT.decode(token).getExpiresAt();
  }

  /**
   * Gets the email used to create the token.
   *
   * @param token The token to verify
   * @return A user's email address.
   */
  public String extractUserEmail(String token) {
    return JWT.decode(token).getSubject();
  }

  /**
   * Asserts that the given token corresponds to the expected format & signature.
   *
   * @param token Token to verify
   */
  public void assertSignatureIsValid(String token) {
    jwtKeyManager.getAlgorithm().verify(JWT.decode(token));
  }
}
