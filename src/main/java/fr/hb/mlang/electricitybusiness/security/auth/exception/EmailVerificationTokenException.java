package fr.hb.mlang.electricitybusiness.security.auth.exception;

public class EmailVerificationTokenException extends RuntimeException {

  public EmailVerificationTokenException() {
    super("An error occurred while trying to verify email verification token.");
  }

  public EmailVerificationTokenException(String message) {
    super(message);
  }
}
