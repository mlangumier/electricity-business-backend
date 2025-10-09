package fr.hb.mlang.electricitybusiness.security.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigTests;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationTokenRepository;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * User Account Verification - Integration Tests - Tests that the DTO validation, endpoint, service
 * & database all work properly together.
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class VerifyAccountIntegrationTest extends DatabaseConfigTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  UserRepository userRepository;
  @Autowired
  EmailVerificationTokenRepository emailRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    emailRepository.deleteAll();
  }

  //ERROR
  // Error running 'VerifyAccountIntegrationTest'
  // Failed to resolve org.junit.vintage:junit-vintage-engine:5.12.2

  @Test
  @DisplayName("Valid account verification")
  void givenValidVerificationToken_whenVerifyingAccount_thenVerificationShouldSucceed()
      throws Exception {
    Seed seed = this.seedUnverifiedUser("test@test.com", 5);

    mockMvc
        .perform(get("/api/v1/verify-account")
            .param("token", seed.rawToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Check that the user is now verified
    User user = userRepository.findByEmail(seed.user.getEmail()).orElseThrow();
    Assertions.assertTrue(user.getAuth().getEmailVerified());

    // Check that the emailVerificationToken has been deleted from the database
    EmailVerificationToken emailToken = emailRepository
        .findById(seed.emailToken().getId())
        .orElse(null);
    Assertions.assertNull(emailToken);
  }

  // Test: already verified
  @Test
  @DisplayName("Invalid - Already verified")
  void givenAlreadyVerifiedUser_whenVerifyingAccount_thenVerificationShouldFail() throws Exception {
    Seed seed = this.seedUnverifiedUser("test@test.com", 5);

    // Manually verify the user
    seed.user.getAuth().setEmailVerified(true);
    seed.user.setEmailVerificationToken(null);
    userRepository.saveAndFlush(seed.user);

    // Verify data has been modified as expected
    User user = userRepository.findByEmail(seed.user.getEmail()).orElseThrow();
    Assertions.assertTrue(user.getAuth().getEmailVerified());
    Assertions.assertNull(user.getEmailVerificationToken());

    // Verify the user and expect it to fail
    mockMvc
        .perform(post("/api/v1/verify-account")
            .param("token", seed.rawToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()); //ERROR: returns a 405
  }

  @Test
  @DisplayName("Invalid - Expired token")
  void givenExpiredToken_whenVerifyingAccount_thenVerificationShouldFail() throws Exception {
    Seed seed = this.seedUnverifiedUser("test@test.com", 0);

    mockMvc
        .perform(post("/api/v1/verify-account")
            .param("token", seed.rawToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    EmailVerificationToken emailToken = emailRepository.findById(seed.emailToken().getId()).orElseThrow();
    Assertions.assertFalse(emailToken.getExpiresAt().isAfter(Instant.now()));
    //ERROR (assert): ConstraintViolationImpl{interpolatedMessage='doit être une date dans le futur', propertyPath=expiresAt
  }

  @Test
  @DisplayName("Invalid - Unknown token")
  void givenUnknownToken_whenVerifyingAccount_thenVerificationShouldFail() throws Exception {
    Seed seed = this.seedUnverifiedUser("test@test.com", 5);
    String invalidToken = seed.rawToken() + "0";

    mockMvc.perform(post("/api/v1/verify-account")
            .param("token", invalidToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    EmailVerificationToken emailToken = emailRepository.findById(seed.emailToken().getId()).orElseThrow();
    Assertions.assertNotEquals(emailToken.getTokenHash(), invalidToken);
  }


  /**
   * Create a fake user for testing
   */
  private Seed seedUnverifiedUser(String email, Integer expiresInMins) {
    User user = new User(email, null);
    user.setAuth(new UserAuth(encoder.encode("password")));
    user.setProfile(new UserProfile(
        "Test",
        "Testson",
        LocalDate.of(1990, 1, 1),
        "21 Test street, 69000, Lyon"
    ));
    String rawToken = VerificationToken.generateRawToken();
    String generatedToken = VerificationToken.hashToken(rawToken);
    user.setEmailVerificationToken(new EmailVerificationToken(
        generatedToken,
        Instant.now().plus(expiresInMins, ChronoUnit.MINUTES)
    ));
    userRepository.save(user);

    return new Seed(user, user.getEmailVerificationToken(), rawToken);
  }

  private record Seed(User user, EmailVerificationToken emailToken, String rawToken) {

  }
}
