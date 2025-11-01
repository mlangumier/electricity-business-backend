package fr.hb.mlang.electricitybusiness.security.auth.controller;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigIT;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.utils.JsonTestUtil;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LoginIT extends DatabaseConfigIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  UserRepository userRepository;

  @BeforeAll
  static void setup() {}

  @AfterAll
  static void teardown() {}

  // Test Invalid - Not registered (Unknown email)

  // Test Invalid - Not verified

  // Test Invalid - Bad credentials (DTO annotations)

  // Test Invalid - Wrong credentials (wrong password)

  @Test
  @DisplayName("Valid login: verified user with valid credentials")
  void givenVerifiedUserWithValidCredentials_whenLoggingIn_thenShouldSucceedAndReturnDto() throws Exception {
    String requestJson = this.readJson("valid_goodCredentials.json");

    // Mock request
    // Assert response status
    // Assert userDto

    // Get & assert refreshToken entity
    // Get user & assert last login
  }

  private String readJson(String fileName) throws IOException {
    return JsonTestUtil.readJsonFile("auth/login", fileName);
  }
}
