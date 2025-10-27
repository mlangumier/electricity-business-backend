package fr.hb.mlang.electricitybusiness.security.auth.controller;

import fr.hb.mlang.electricitybusiness.config.DatabaseConfigIT;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

  // Test Valid - Login

  // Test Invalid - Bad credentials (DTO annotations)

  // Test Invalid - Wrong credentials (wrong password)

  // Test Invalid - Not verified

  // Test Invalid - Unknown email (no user found)
}
