package cn.msa.msa_museum_server.controller;

import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @Test
    void testGetFileList_ValidParameters() throws Exception {
        List<FileEntity> mockFiles = Arrays.asList(
                new FileEntity("file1.txt", 1024L, LocalDateTime.parse("2024-11-24T10:00")),
                new FileEntity("file2.txt", 2048L, LocalDateTime.parse("2024-11-24T11:00"))
        );

        Page<FileEntity> mockPage = new PageImpl<>(mockFiles, PageRequest.of(0, 5), mockFiles.size());

        when(fileService.getFileList(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(get("/files/list")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)) // 检查响应代码
                .andExpect(jsonPath("$.message").value("")); // 验证当前页
    }

    @Test
    void testGetFileList_InvalidNumberFormat() throws Exception {
        mockMvc.perform(get("/files/list")
                        .param("page", "abc") // 无效的 page 参数
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ExceptionEnum.INVALID_ENTRY_TYPE.getCode()));
    }

    @Test
    void testGetFileList_InvalidPaginationParameters() throws Exception {
        mockMvc.perform(get("/files/list")
                        .param("page", "-1") // Invalid pagination parameters
                        .param("size", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ExceptionEnum.INVALID_ENTRY.getCode()));
    }
}