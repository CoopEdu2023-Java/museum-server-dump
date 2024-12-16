package cn.msa.msa_museum_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class ResetPasswordRequest {
    private String username;
    private String password;
    private String newPassword;
  }
}

@Nested
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerUnitTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  // Reset Password
  @Test
  public void testResetPasswordWithValidToken() throws Exception {
    // Directly provide JWT token
    String jwtToken =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNyIsImV4cCI6MTczMjg1MDQ0Mn0.rXoHBMS4AhDsmhJiwiEW3FX6ZK9G-HIfGMiyO-0wEcw";

    // Reset password
    String resetPasswordRequestJson =
        objectMapper.writeValueAsString(
            new UserControllerTests.ResetPasswordRequest("user", "2aA@93r3f", "1aA@93r3f"));

    mockMvc
        .perform(
            post("/users/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content(resetPasswordRequestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("success"));
  }

  @Test
  public void testResetPasswordWithoutToken() throws Exception {
    String resetPasswordRequestJson =
        objectMapper.writeValueAsString(
            new UserControllerTests.ResetPasswordRequest(
                "testUser", "testPassword", "newPassword"));

    mockMvc
        .perform(
            post("/users/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(resetPasswordRequestJson))
        .andExpect(status().isForbidden()); // Expect 403 due to missing token
  }
}
