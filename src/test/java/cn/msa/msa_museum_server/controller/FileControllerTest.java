package cn.msa.msa_museum_server.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.service.FileService;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    FileService fileService;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFileUploadSuccess() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello world".getBytes());
        String id = "1";
        FileEntity mockfileEntity = new FileEntity(id, "test.txt", "text/plain", 1024);
        when(fileService.upload(file)).thenReturn(mockfileEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .content(objectMapper.writeValueAsString(file))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}