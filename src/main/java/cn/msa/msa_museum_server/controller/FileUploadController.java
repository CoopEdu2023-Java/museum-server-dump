package cn.msa.msa_museum_server.controller;

import cn.msa.msa_museum_server.dto.MultipleFilesDto;
import cn.msa.msa_museum_server.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<MultipleFilesDto> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        MultipleFilesDto result = fileUploadService.uploadMultipleFiles(files);
        return ResponseEntity.ok(result);
    }
}
