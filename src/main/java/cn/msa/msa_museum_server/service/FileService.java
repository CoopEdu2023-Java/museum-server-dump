package cn.msa.msa_museum_server.service;

import org.springframework.core.io.Resource;

import cn.msa.msa_museum_server.dto.FileContentRequestTypeDto;
import cn.msa.msa_museum_server.dto.FileMetadataDto;

public interface FileService {
    FileMetadataDto getFileMetadata(String filename);

    Resource getFileContent(String filename);

    boolean supportFileContentRequestType(String filename, FileContentRequestTypeDto requestType);
}
