package cn.msa.msa_museum_server.controller;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest {
        private String username;
        private String password;
    }

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Login tests
    @Test
    public void testLoginWithValidCredentials() throws Exception {
        // 构造请求参数
        String loginRequestJson = objectMapper.writeValueAsString(new UserControllerTests.LoginRequest("user", "1aA@93r3f"));

        // 执行请求
        String responseToken = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk()) // 确认返回 HTTP 200 状态
                .andReturn()
                .getResponse()
                .getContentAsString(); // 获取响应的内容

        // 验证响应的 token 是否非空
        assertNotNull(responseToken, "Token should not be null");
        assertFalse(responseToken.trim().isEmpty(), "Token should not be empty");

        // 打印 token（可选，用于调试）
        System.out.println("Login Token: " + responseToken);
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        String invalidLoginJson = objectMapper.writeValueAsString(new UserControllerTests.LoginRequest("wrongUser", "wrongPassword"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLoginJson))
                .andExpect(status().isOk()) // Expect 200 status for invalid login (if that's the API design)
                .andExpect(jsonPath("$.code").value(2002)) // Verify error code
                .andExpect(jsonPath("$.message").value("Error: User does not exist")); // Verify error message
    }

    @Test
    public void testRetrieveJwtTokenAfterLogin() throws Exception {
        String loginRequestJson = objectMapper.writeValueAsString(new UserControllerTests.LoginRequest("user", "1aA@93r3f"));

        String jwtToken = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(jwtToken, "Token should not be null");
        assertFalse(jwtToken.isEmpty(), "Token should not be empty");
    }

    //Reset Password
    @Test
    public void testResetPasswordWithValidToken() throws Exception {
        // Mock login to get JWT token
        String loginRequestJson = objectMapper.writeValueAsString(new UserControllerTests.LoginRequest("user", "1aA@93r3f"));
        String jwtToken = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Reset password
        String resetPasswordRequestJson = objectMapper.writeValueAsString(new UserControllerTests.ResetPasswordRequest("user", "1aA@93r3f", "2aA@93r3f"));

        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(resetPasswordRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    public void testResetPasswordWithoutToken() throws Exception {
        String resetPasswordRequestJson = objectMapper.writeValueAsString(
                new UserControllerTests.ResetPasswordRequest("testUser", "testPassword", "newPassword")
        );

        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resetPasswordRequestJson))
                .andExpect(status().isForbidden()); // Expect 403 due to missing token
    }
}
