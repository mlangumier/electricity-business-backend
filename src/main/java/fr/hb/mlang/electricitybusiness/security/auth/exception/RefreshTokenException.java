package fr.hb.mlang.electricitybusiness.security.auth.exception;

public class RefreshTokenException extends RuntimeException {

  public RefreshTokenException() {
    super("Missing refresh token");
  }

  public RefreshTokenException(String message) {
    super(message);
  }
}
