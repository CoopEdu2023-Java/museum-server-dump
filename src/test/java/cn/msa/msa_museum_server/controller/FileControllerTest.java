package cn.msa.msa_museum_server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cn.msa.msa_museum_server.dto.FileContentRequestTypeDto;
import cn.msa.msa_museum_server.dto.FileMetadataDto;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.service.FileService;
import cn.msa.msa_museum_server.service.JwtService;

@WebMvcTest(FileController.class)
@WithMockUser
@Import(JwtService.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    @Mock
    private Resource resource;

    private static final String TEST_FILENAME = "test.pdf";
    private static final String TEST_FILE_TYPE = "application/pdf";
    private static final long TEST_FILE_SIZE = 3500L;
    private static final String TEST_FILE_URL = "/files/test.pdf/content";
    private static final String TEST_FILE_METADATA_URL = "/files/test.pdf/metadata";

    private FileMetadataDto setupFileMetadata() {
        FileMetadataDto fileMetadataDto = new FileMetadataDto(TEST_FILENAME, TEST_FILE_TYPE, TEST_FILE_SIZE,
                TEST_FILE_URL);
        when(fileService.getFileMetadata(TEST_FILENAME)).thenReturn(fileMetadataDto);
        return fileMetadataDto;
    }

    @Test
    public void testGetFileMetadata_Success() throws Exception {
        setupFileMetadata();

        mockMvc.perform(get(TEST_FILE_METADATA_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.name").value(TEST_FILENAME))
                .andExpect(jsonPath("$.data.type").value(TEST_FILE_TYPE))
                .andExpect(jsonPath("$.data.size").value(TEST_FILE_SIZE))
                .andExpect(jsonPath("$.data.url").value(TEST_FILE_URL));
    }

    @Test
    public void testGetFileContent_Success() throws Exception {
        FileMetadataDto fileMetadataDto = setupFileMetadata();
        when(fileService.getFileContent(fileMetadataDto.getName())).thenReturn(resource);
        when(fileService.supportFileContentRequestType(fileMetadataDto.getName(), FileContentRequestTypeDto.FULL))
                .thenReturn(true);
        when(resource.getFilename()).thenReturn(TEST_FILENAME);

        MockHttpServletResponse response = mockMvc.perform(get(TEST_FILE_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertTrue(response.getHeaderNames().contains(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("inline; filename=\"test.pdf\"", response.getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("application/pdf", response.getContentType());
    }

    @Test
    public void testGetFileContentRanged_Success() throws Exception {
        setupFileMetadata();
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileService.getFileContent(TEST_FILENAME)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(TEST_FILENAME, FileContentRequestTypeDto.RANGE))
                .thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(get(TEST_FILE_URL)
                .header(HttpHeaders.RANGE, "bytes=0-1023"))
                .andExpect(status().isPartialContent())
                .andReturn()
                .getResponse();

        assertTrue(response.getHeaderNames().contains(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("inline; filename=\"" + TEST_FILENAME + "\"", response.getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(TEST_FILE_TYPE, response.getContentType());
    }

    @Test
    public void testGetFileContentRanged_InvalidRange() throws Exception {
        setupFileMetadata();
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileService.getFileContent(TEST_FILENAME)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(TEST_FILENAME, FileContentRequestTypeDto.RANGE))
                .thenReturn(true);

        mockMvc.perform(get(TEST_FILE_URL)
                .header(HttpHeaders.RANGE, "bytes=1024-2047"))
                .andExpect(status().isRequestedRangeNotSatisfiable());
    }

    @Test
    public void testGetFileContentRanged_InvalidRequestType() throws Exception {
        setupFileMetadata();
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileService.getFileContent(TEST_FILENAME)).thenReturn(resource);
        when(fileService.supportFileContentRequestType(TEST_FILENAME, FileContentRequestTypeDto.RANGE))
                .thenReturn(false);

        mockMvc.perform(get(TEST_FILE_URL)
                .header(HttpHeaders.RANGE, "bytes=1024-2047"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionEnum.INVALID_FILE_REQUEST_TYPE.getCode()));
    }
}