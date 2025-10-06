package fr.hb.mlang.electricitybusiness.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Class that generates and hashes email verification & password reset tokens, so they can be sent
 * raw in the email sent to the user, or be hashed and stored (opaque) in the database.
 */
public final class VerificationToken {

  private static final SecureRandom RNG = new SecureRandom();

  /**
   * Hash a token to be stored in the database.
   *
   * @param rawToken token to hash.
   * @return the hashed token.
   */
  public static String hashToken(String rawToken) {
    byte[] digest = null;
    try {
      digest = MessageDigest
          .getInstance("SHA-256")
          .digest(rawToken.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    StringBuilder sb = new StringBuilder(digest.length * 2);

      for (byte b : digest) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
  }

  /**
   * Generates a token that will be shared to the user via email links url (email verification &
   * password reset).
   *
   * @return a raw token of a 32-characters length
   */
  public static String generateRawToken() {
    byte[] buf = new byte[32]; // 256-bit
    RNG.nextBytes(buf);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
  }
}
