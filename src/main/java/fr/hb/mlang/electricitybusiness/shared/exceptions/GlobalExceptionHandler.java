package fr.hb.mlang.electricitybusiness.shared.exceptions;

import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailVerificationTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.UserAlreadyVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyInUseException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  void handleNotFound() {};

  @ExceptionHandler(UserAlreadyVerifiedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  void handleAlreadyVerifiedUser() {};

  @ExceptionHandler(EmailVerificationTokenException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  void handleEmailVerificationTokenNotFound() {};

}
