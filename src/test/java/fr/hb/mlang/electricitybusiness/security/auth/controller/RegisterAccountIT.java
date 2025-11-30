package fr.hb.mlang.electricitybusiness.security.auth.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigIT;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.utils.JsonTestUtil;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * User Registration - Integration Tests - Tests that the DTO validation, controller endpoint,
 * service & database all work properly together.
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE) // Keep TestContainers datasource
class RegisterAccountIT extends DatabaseConfigIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    userRepository.flush();
  }

  @Test
  @DisplayName("Valid registration: user registers with all fields")
  void givenDataWithAllFields_whenRegisteringUser_thenCreateUserShouldSucceedAndUserShouldPersist()
      throws Exception {
    String requestJson = this.readJson("valid_allFields.json");

    // Test endpoint
    mockMvc
        .perform(post("/api/v1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // Check that User & related entities have been created as well
    User user = userRepository.findByEmail("test@test.com").orElse(null);
    assertNotNull(user);
    assertNotNull(user.getProfile());
    assertNotNull(user.getEmailVerificationToken());
  }

  @Test
  @DisplayName("Valid registration: user registers with required fields")
  void givenDataWithRequiredFieldsOnly_whenRegisteringUser_thenCreateUserShouldSucceedAndUserShouldPersist()
      throws Exception {
    String requestJson = this.readJson("valid_requiredFields.json");

    // Test endpoint
    mockMvc
        .perform(post("/api/v1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // Check that User & related entities have been created as well
    User user = userRepository.findByEmail("test@test.com").orElse(null);
    assertNotNull(user);
    assertNotNull(user.getProfile());
    assertNotNull(user.getEmailVerificationToken());
  }

  @Test
  @DisplayName("Invalid registration: error dateOfBirth < 18")
  void givenInvalidDateOfBirth_whenRegisteringUser_thenDtoValidationShouldThrow() throws Exception {
    String requestJson = this.readJson("invalid_dateOfBirth.json");

    // Test endpoint validation
    mockMvc
        .perform(post("/api/v1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // Checks that User hasn't been persisted
    User user = userRepository.findByEmail("test@test.com").orElse(null);
    assertNull(user);
  }

  @Test
  @DisplayName("Invalid registration: missing fields")
  void givenDataWithMissingFields_whenRegisteringUser_thenCreateUserShouldFail() throws Exception {
    String requestJson = this.readJson("invalid_missingFields.json");

    // Test endpoint validation
    mockMvc
        .perform(post("/api/v1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // Checks that User hasn't been persisted
    User user = userRepository.findByEmail("test@test.com").orElse(null);
    assertNull(user);
  }

  /**
   * Reads a JSON file. Uses a method from {@link JsonTestUtil} and sets the folder used in this
   * test file.
   *
   * @param fileName Name of the JSON file
   * @return The readable file ready for assertion
   * @throws IOException if the file cannot be found.
   */
  private String readJson(String fileName) throws IOException {
    return JsonTestUtil.readJsonFile("auth/register", fileName);
  }
}
