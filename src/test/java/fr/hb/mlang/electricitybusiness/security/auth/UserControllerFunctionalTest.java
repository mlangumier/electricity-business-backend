package fr.hb.mlang.electricitybusiness.security.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigTests;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.utils.JsonTestUtil;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE) // Keep TestContainers datasource
public class UserControllerFunctionalTest extends DatabaseConfigTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("Valid registration")
  void givenRegisterRequest_whenDataIsValid_thenCreateUserShouldSucceed() throws Exception {
    String requestJson = this.readJson("valid.json");
    String expectedResponse = this.readJson("expected.json");

    String response = mockMvc
        .perform(MockMvcRequestBuilders.post("/api/v1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    System.out.println("--- TEST VALID: " + response);
    JSONAssert.assertEquals(expectedResponse, response, false);
  }

  @Test
  @DisplayName("Invalid registration: dateOfBirth < 18")
  void givenRegisterRequest_whenDataIsNotValid_thenCreateUserShouldFail() throws Exception {
    String requestJson = this.readJson("invalid_dateOfBirth.json");

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/v1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
