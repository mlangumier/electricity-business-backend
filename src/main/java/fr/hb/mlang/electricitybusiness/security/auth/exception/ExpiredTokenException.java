package fr.hb.mlang.electricitybusiness.security.auth.exception;

public class ExpiredTokenException extends RuntimeException {

  public ExpiredTokenException(String message) {
    super(message);
  }
}
