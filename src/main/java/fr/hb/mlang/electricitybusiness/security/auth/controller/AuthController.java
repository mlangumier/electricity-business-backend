package fr.hb.mlang.electricitybusiness.security.auth.controller;

import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.EmailAvailableRequest;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.RegisterRequest;
import fr.hb.mlang.electricitybusiness.security.auth.service.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  private final AuthServiceImpl authService;

  public AuthController(AuthServiceImpl authService) {
    this.authService = authService;
  }

  //TODO: Check & correct HttpStatus & responses

  @PostMapping("/email-available")
  public ResponseEntity<Boolean> checkEmailIsAvailable(@Valid @RequestBody EmailAvailableRequest request) {
    Boolean isAvailable = authService.checkIsEmailAvailable(request);
    return ResponseEntity.status(HttpStatus.OK).body(isAvailable);
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered!");
  }

  @GetMapping("/verify-account")
  public ResponseEntity<Void> verify(@RequestParam("token") String token) {
    authService.verifyAccount(token);
    return ResponseEntity.noContent().build();
  }

  // sendResetPassword
  // updatePassword
}
