package cn.msa.msa_museum_server.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import cn.msa.msa_museum_server.config.FileProperties;
import cn.msa.msa_museum_server.dto.FileContentRequestTypeDto;
import cn.msa.msa_museum_server.dto.FileMetadataDto;
import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.FileRepository;
import cn.msa.msa_museum_server.service.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileProperties fileProperties;

    private final FileRepository fileRepository;

    public FileServiceImpl(FileProperties fileProperties, FileRepository fileRepository) {
        this.fileProperties = fileProperties;
        this.fileRepository = fileRepository;
    }

    @Override
    public FileMetadataDto getFileMetadata(String filename) {
        FileEntity fileEntity = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.FILE_NOT_FOUND));

        Path filePath = getFilePath(fileEntity);

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new BusinessException(ExceptionEnum.FILE_NOT_FOUND);
        }

        long size = fileEntity.getSize();

        return new FileMetadataDto(
                fileEntity.getFilename(),
                fileEntity.getContentType(),
                size,
                "/files/" + fileEntity.getFilename() + "/content");
    }

    @Override
    public Resource getFileContent(String filename) {
        FileEntity fileEntity = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.FILE_NOT_FOUND));

        Path filePath = getFilePath(fileEntity);

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.isReadable()) {
                throw new BusinessException(ExceptionEnum.FILE_NOT_FOUND);
            }

            return resource;
        } catch (MalformedURLException e) {
            log.error("Error retrieving file content: {}", e.getMessage());
            throw new BusinessException(ExceptionEnum.MALFORMED_FILE_PATH);
        }
    }

    @Override
    public boolean supportFileContentRequestType(String filename, FileContentRequestTypeDto requestType) {
        FileEntity fileEntity = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.FILE_NOT_FOUND));

        switch (fileEntity.getContentType()) {
            case "image/jpeg":
            case "image/png":
            case "image/webp":
            case "application/pdf":
            case "text/plain":
                return fileEntity.getSize() <= fileProperties.getMaxDownloadSize()
                        && requestType == FileContentRequestTypeDto.FULL;
            case "audio/mpeg":
            case "audio/ogg":
            case "audio/wav":
            case "video/mp4":
                return requestType == FileContentRequestTypeDto.RANGE;
            default:
                log.warn("Unsupported content type: {}", fileEntity.getContentType());
                return false;
        }
    }

    private Path getFilePath(FileEntity fileEntity) {
        return Path.of(fileProperties.getStorageLocation()).resolve(fileEntity.getFilename());
    }

}