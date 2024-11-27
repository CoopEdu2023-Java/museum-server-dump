package cn.msa.msa_museum_server.service;

import org.springframework.core.io.Resource;

import cn.msa.msa_museum_server.dto.FileContentRequestTypeDto;
import cn.msa.msa_museum_server.dto.FileMetadataDto;

public interface FileService {
    FileMetadataDto getFileMetadata(String id);

    Resource getFileContent(String id);

    boolean supportFileContentRequestType(String id, FileContentRequestTypeDto requestType);
}
