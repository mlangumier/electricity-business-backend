package fr.hb.mlang.electricitybusiness.security.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hb.mlang.electricitybusiness.config.DatabaseConfigIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RefreshIT extends DatabaseConfigIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
}
