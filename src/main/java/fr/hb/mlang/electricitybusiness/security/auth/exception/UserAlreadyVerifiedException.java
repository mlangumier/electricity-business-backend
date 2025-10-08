package fr.hb.mlang.electricitybusiness.security.auth.exception;

public class UserAlreadyVerifiedException extends RuntimeException {

  public UserAlreadyVerifiedException(String username) {
    super("User '" + username + "' is already verified");
  }
}
