package cn.msa.msa_museum_server.service.impl;

import cn.msa.msa_museum_server.config.FileProperties;
import cn.msa.msa_museum_server.dto.MultipleFilesDto;
import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.UploadedFileRepository;
import cn.msa.msa_museum_server.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
// import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final FileProperties fileProperties;
    private final UploadedFileRepository fileRepository;

    @Autowired
    public FileUploadServiceImpl(FileProperties fileProperties, UploadedFileRepository fileRepository) {
        this.fileProperties = fileProperties;
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public MultipleFilesDto uploadMultipleFiles(List<MultipartFile> files) {
        List<String> uploadedFiles = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();

        // 遍历文件列表并处理每个文件
        for (MultipartFile file : files) {
            // 验证文件名是否为空
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                failedFiles.add("File with no name cannot be uploaded");
                continue;
            }

            // 验证文件大小
            if (file.getSize() > fileProperties.getMaxUploadSize()) {
                failedFiles.add(originalFilename + " (exceeds max upload size)");
                continue;
            }

            // 保存文件到磁盘
            String filePath = fileProperties.getStorageLocation() + File.separator + originalFilename;
            File destination = new File(filePath);
            ensureDirectoryExists(destination.getParentFile());
            try {
                file.transferTo(destination);
            } catch (IOException e) {
                failedFiles.add(originalFilename + " (Error: " + e.getMessage() + ")");
                continue;
            }

            // 保存文件元数据到数据库
            FileEntity uploadedFile = new FileEntity();
            uploadedFile.setFilename(originalFilename); // 设置文件名
            try {
                uploadedFile.setContentType(Files.probeContentType(destination.toPath()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // 设置文件类型
            uploadedFile.setSize(file.getSize()); // 设置文件大小

            // 打印调试信息
            System.out.println("Filename: " + uploadedFile.getFilename());
            System.out.println("File size: " + uploadedFile.getSize());
            System.out.println("Content type: " + uploadedFile.getContentType());

            // 保存文件信息到数据库
            fileRepository.save(uploadedFile);

            // 添加到成功列表
            uploadedFiles.add(originalFilename);
        }

        // 如果所有文件都上传失败，抛出自定义异常
        if (failedFiles.size() == files.size()) {
            throw new BusinessException(ExceptionEnum.FILE_UPLOAD_ERROR, "All files failed to upload.");
        }

        // 返回上传成功和失败的文件列表
        return new MultipleFilesDto(uploadedFiles, failedFiles);
    }

    // 确保目标文件夹存在
    private void ensureDirectoryExists(File directory) {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new BusinessException(ExceptionEnum.FILE_UPLOAD_ERROR, "Failed to create directory: " + directory.getAbsolutePath());
        }
    }
}
