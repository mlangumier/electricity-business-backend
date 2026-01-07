package fr.hb.mlang.electricitybusiness.shared.exceptions;

import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailVerificationTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.RefreshTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.UserAlreadyVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  //--- AUTHENTICATION

  @ExceptionHandler({
      EmailAlreadyInUseException.class,
      UserAlreadyVerifiedException.class,
      UsernameNotFoundException.class
  })
  @ResponseStatus(HttpStatus.CONFLICT)
  public @ResponseBody ErrorResponse userAlreadyInUseException(EmailAlreadyInUseException ex) {
    return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
  }

  @ExceptionHandler({EmailVerificationTokenException.class, RefreshTokenException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ErrorResponse handleEmailVerificationTokenNotFound(
      EmailVerificationTokenException ex
  ) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  //--- DEFAULT exceptions handler for un-handled exceptions

  //TODO: test if works properly with & without auth exceptions
  @ExceptionHandler({RuntimeException.class, Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorResponse handleException(Exception ex) {
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }
}
