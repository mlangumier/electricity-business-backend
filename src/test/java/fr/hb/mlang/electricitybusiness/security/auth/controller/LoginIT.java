package fr.hb.mlang.electricitybusiness.security.auth.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hb.mlang.electricitybusiness.config.DatabaseConfigIT;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginResponseDto;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import fr.hb.mlang.electricitybusiness.utils.JsonTestUtil;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
class LoginIT extends DatabaseConfigIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  UserRepository userRepository;
  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    generateUserValid();
    generateUserUnverified();
  }

  @AfterEach
  void teardown() {
    userRepository.deleteAll();
    userRepository.flush();
  }

  // Test Invalid - Not registered (Unknown email)

  // Test Invalid - Not verified

  // Test Invalid - Missing credentials (DTO annotations)

  // Test Invalid - Wrong credentials (wrong password)

  @Test
  @DisplayName("Valid login: verified user with valid credentials")
  void givenVerifiedUserWithValidCredentials_whenLoggingIn_thenShouldSucceedAndReturnDto()
      throws Exception {
    String requestJson = this.readJson("valid.json");

    String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        //.with(csrf())) //Add when implemented
        .andExpect(status().isOk())
        .andExpect(authenticated().withUsername("user@test.com")) //ERROR: "Authentication should not be null"
        .andExpect(cookie().exists("refreshToken"))
        .andExpect(cookie().httpOnly("refreshToken", true))
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertNotNull(response, "Response should not be null");

    LoginResponseDto responseDto = objectMapper.readValue(response, LoginResponseDto.class);

    assertNotNull(responseDto.accessToken(), "AccessToken should not be null");

    User user = userRepository.findByEmail(responseDto.userAuth().email()).orElseThrow();
    assertNotNull(user);

    assertNotNull(user.getAuth().getLastLogin());
    assert (user.getAuth().getLastLogin()).isAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
    assert (user.getAuth().getLastLogin()).isBefore(Instant.now());

    //TODO: Assert expected response <- set JsonTestHelper method to ignore IDs & dynamic
  }


  private void generateUserValid() {
    User user = new User("user@test.com", null);
    user.setAuth(new UserAuth(encoder.encode("password")));
    user.setProfile(new UserProfile(
        "User",
        "Adminson",
        LocalDate.of(1991, 1, 1),
        "1 street of something, 69001, Lyon"
    ));
    user.getAuth().setEmailVerified(true);
    userRepository.save(user);
  }

  private void generateUserUnverified() {
    User user = new User("unverified-user@test.com", null);
    user.setAuth(new UserAuth(encoder.encode("password")));
    user.setProfile(new UserProfile(
        "Unv",
        "Erified",
        LocalDate.of(1991, 2, 2),
        "2 street of something, 69002, Lyon"
    ));
    String rawToken = VerificationToken.generateRawToken();
    String generatedToken = VerificationToken.hashToken(rawToken);
    user.setEmailVerificationToken(new EmailVerificationToken(
        generatedToken,
        Instant.now().plus(5, ChronoUnit.MINUTES)
    ));

    userRepository.save(user);
  }

  /**
   * Reads a JSON file. Uses a method from {@link JsonTestUtil} and sets the folder used in this
   * test file.
   *
   * @param fileName Name of the JSON file
   * @return The readable file ready for assertion
   * @throws IOException if the file cannot be found?
   */
  private String readJson(String fileName) throws IOException {
    return JsonTestUtil.readJsonFile("auth/login", fileName);
  }
}
