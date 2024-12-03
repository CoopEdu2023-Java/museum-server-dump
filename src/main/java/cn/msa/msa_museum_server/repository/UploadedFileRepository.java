package cn.msa.msa_museum_server.repository;

import cn.msa.msa_museum_server.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<FileEntity, Long> {
}
