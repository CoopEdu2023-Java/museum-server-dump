package cn.msa.msa_museum_server.service;

import cn.msa.msa_museum_server.dto.MultipleFilesDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    MultipleFilesDto uploadMultipleFiles(List<MultipartFile> files);
}
