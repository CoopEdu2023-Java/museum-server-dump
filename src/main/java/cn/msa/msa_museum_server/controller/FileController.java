package cn.msa.msa_museum_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.msa.msa_museum_server.dto.FileMetadataDto;
import cn.msa.msa_museum_server.dto.FileContentRequestTypeDto;
import cn.msa.msa_museum_server.dto.ResponseDto;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/{id}/metadata")
    public ResponseDto<FileMetadataDto> getFileMetadata(@PathVariable String id) {
        if (id == null) {
            throw new BusinessException(ExceptionEnum.MISSING_PARAMETERS);
        }
        return new ResponseDto<>(fileService.getFileMetadata(id));
    }

    // Range is a request header that specifies the range of bytes to retrieve from
    // the file. The Range header is automatically handled by Spring when we return
    // a Resource object from a controller method.
    @GetMapping("/{id}/content")
    public ResponseEntity<Resource> getFileContent(
            @RequestHeader(value = "Range", required = false) String range,
            @PathVariable String id) {
        if (id == null)
            throw new BusinessException(ExceptionEnum.MISSING_PARAMETERS);

        // Check if the request type is supported for the file type
        FileContentRequestTypeDto requestType = range == null ? FileContentRequestTypeDto.FULL
                : FileContentRequestTypeDto.RANGE;
        if (!fileService.supportFileContentRequestType(id, requestType))
            throw new BusinessException(ExceptionEnum.INVALID_FILE_REQUEST_TYPE);

        FileMetadataDto fileMetadataDto = fileService.getFileMetadata(id);
        String filename = fileMetadataDto.getName();
        MediaType mediaType = MediaType.parseMediaType(fileMetadataDto.getType());

        Resource resource = fileService.getFileContent(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .body(resource);
    }
}
