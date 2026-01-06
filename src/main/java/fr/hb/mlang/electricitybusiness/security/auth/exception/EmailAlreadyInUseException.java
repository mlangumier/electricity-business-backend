package fr.hb.mlang.electricitybusiness.security.auth.exception;

public class EmailAlreadyInUseException extends RuntimeException {

  public EmailAlreadyInUseException() {}

  public EmailAlreadyInUseException(String email) {
    super("Email '" + email + "' is already in use");
  }
}
