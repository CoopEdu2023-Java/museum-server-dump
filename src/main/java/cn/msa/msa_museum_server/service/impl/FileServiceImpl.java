package cn.msa.msa_museum_server.service.impl;

import cn.msa.msa_museum_server.entity.FileEntity;
import cn.msa.msa_museum_server.repository.FileRepository;
import cn.msa.msa_museum_server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public Page<FileEntity> getFileList(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }
}