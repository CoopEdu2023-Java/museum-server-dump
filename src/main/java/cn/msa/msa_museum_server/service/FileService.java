package cn.msa.msa_museum_server.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.FileRepository;

@Service
public class FileService {

    private final String uploadDir = ""; // 存储路径

    @Autowired
    private FileRepository fileRepository;

    public FileEntity upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ExceptionEnum.EMPTY_FILE);
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // 文件名
            Path filePath = Paths.get(uploadDir, fileName); // 文件路径
            long size = file.getSize(); // 文件大小

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);

            FileEntity fileEntity = new FileEntity();
            fileRepository.save(fileEntity);
            Long id = fileEntity.getId();

            fileEntity = new FileEntity(id, fileName, filePath.toString(), size);

            return fileEntity;

        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}