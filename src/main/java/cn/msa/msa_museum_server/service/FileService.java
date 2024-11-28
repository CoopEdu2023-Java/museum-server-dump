package cn.msa.msa_museum_server.service;

import cn.msa.msa_museum_server.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileService {
    Page<FileEntity> getFileList(Pageable pageable);
}