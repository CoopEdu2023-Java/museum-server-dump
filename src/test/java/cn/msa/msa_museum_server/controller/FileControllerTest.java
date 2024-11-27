package cn.msa.msa_museum_server.controller;

import cn.msa.msa_museum_server.dto.FileMetadataDto;
import cn.msa.msa_museum_server.dto.FileContentRequestTypeDto;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(FileController.class)
@WithMockUser
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    @Mock
    private Resource resource;

    private FileMetadataDto setupFileMetadata(String fileId) {
        FileMetadataDto fileMetadataDto = new FileMetadataDto(fileId, "test.pdf", "application/pdf", "3.5 MB",
                "/files/123/content");
        when(fileService.getFileMetadata(fileId)).thenReturn(fileMetadataDto);
        return fileMetadataDto;
    }

    @Test
    public void testGetFileMetadata_Success() throws Exception {
        String fileId = "123";
        setupFileMetadata(fileId);

        mockMvc.perform(get("/files/123/metadata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value("123"))
                .andExpect(jsonPath("$.data.name").value("test.pdf"))
                .andExpect(jsonPath("$.data.type").value("application/pdf"))
                .andExpect(jsonPath("$.data.size").value("3.5 MB"))
                .andExpect(jsonPath("$.data.url").value("/files/123/content"));
    }

    @Test
    public void testGetFileContent_Success() throws Exception {
        String fileId = "123";
        setupFileMetadata(fileId);
        when(fileService.getFileContent(fileId)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(fileId, FileContentRequestTypeDto.FULL)).thenReturn(true);
        when(resource.getFilename()).thenReturn("test.pdf");

        MockHttpServletResponse response = mockMvc.perform(get("/files/123/content"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertTrue(response.getHeaderNames().contains(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("inline; filename=\"test.pdf\"", response.getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("application/pdf", response.getContentType());
    }

    @Test
    public void testGetFileContentRanged_Success() throws Exception {
        String fileId = "123";
        setupFileMetadata(fileId);
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileService.getFileContent(fileId)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(fileId, FileContentRequestTypeDto.RANGE)).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(get("/files/123/content")
                .header(HttpHeaders.RANGE, "bytes=0-1023"))
                .andExpect(status().isPartialContent())
                .andReturn()
                .getResponse();

        assertTrue(response.getHeaderNames().contains(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("inline; filename=\"test.pdf\"", response.getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("application/pdf", response.getContentType());
    }

    @Test
    public void testGetFileContentRanged_InvalidRange() throws Exception {
        String fileId = "123";
        setupFileMetadata(fileId);
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileService.getFileContent(fileId)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(fileId, FileContentRequestTypeDto.RANGE)).thenReturn(true);

        mockMvc.perform(get("/files/123/content")
                .header(HttpHeaders.RANGE, "bytes=1024-2047"))
                .andExpect(status().isRequestedRangeNotSatisfiable());
    }

    @Test
    public void testGetFileContentRanged_InvalidRequestType() throws Exception {
        String fileId = "123";
        setupFileMetadata(fileId);
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileService.getFileContent(fileId)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(fileId, FileContentRequestTypeDto.RANGE)).thenReturn(false);

        mockMvc.perform(get("/files/123/content")
                .header(HttpHeaders.RANGE, "bytes=1024-2047"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionEnum.INVALID_FILE_REQUEST_TYPE.getCode()));
    }
}