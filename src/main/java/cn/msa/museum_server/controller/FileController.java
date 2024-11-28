package cn.msa.museum_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.msa.museum_server.dto.ResponseDto;
import cn.msa.museum_server.service.FileService;
import cn.msa.museum_server.entity.FileEntity;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseDto<FileEntity> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseDto<>(fileService.upload(file));
    }
}
