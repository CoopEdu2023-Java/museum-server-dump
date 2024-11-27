package cn.msa.msa_museum_server.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import cn.msa.msa_museum_server.config.FileProperties;
import cn.msa.msa_museum_server.dto.FileMetadataDto;
import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.FileRepository;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @TempDir
    private Path storageLocation;

    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        FileProperties fileProperties = new FileProperties();
        fileProperties.setStorageLocation(storageLocation.toString());
        fileService = new FileServiceImpl(fileProperties, fileRepository);
    }

    private FileEntity createFileEntity() {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId("test-file-id");
        fileEntity.setFilename("test-file.txt");
        fileEntity.setContentType("text/plain");
        fileEntity.setSize(12);
        return fileEntity;
    }

    private Path createTestFile(FileEntity fileEntity, String content) throws IOException {
        Path filePath = storageLocation.resolve(fileEntity.getFilename());
        Files.createFile(filePath);
        Files.writeString(filePath, content);
        return filePath;
    }

    @Test
    public void testGetFileMetadata_Success() throws IOException {
        FileEntity fileEntity = createFileEntity();
        createTestFile(fileEntity, "test content");
        String fileId = fileEntity.getId();

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));

        FileMetadataDto fileMetadataDto = fileService.getFileMetadata(fileId);

        assertEquals(fileId, fileMetadataDto.getId());
        assertEquals(fileEntity.getFilename(), fileMetadataDto.getName());
        assertEquals(fileEntity.getContentType(), fileMetadataDto.getType());
        assertEquals(fileEntity.getSize() + " B", fileMetadataDto.getSize());
        assertEquals("/files/" + fileId + "/content", fileMetadataDto.getUrl());
    }

    @Test
    public void testGetFileMetadata_FileNotFound() {
        String fileId = "test-file-id";
        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            fileService.getFileMetadata(fileId);
        });

        assertEquals(ExceptionEnum.FILE_NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    public void testGetFileContent_Success() throws IOException {
        FileEntity fileEntity = createFileEntity();
        String fileId = fileEntity.getId();

        Path filePath = createTestFile(fileEntity, "test content");

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));

        Resource resource = fileService.getFileContent(fileId);

        assertInstanceOf(UrlResource.class, resource);
        assertEquals(filePath.toUri(), resource.getURI());
    }

    @Test
    public void testGetFileContent_FileNotFound() {
        String fileId = "test-file-id";
        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            fileService.getFileContent(fileId);
        });

        assertEquals(ExceptionEnum.FILE_NOT_FOUND.getCode(), exception.getCode());
    }
}