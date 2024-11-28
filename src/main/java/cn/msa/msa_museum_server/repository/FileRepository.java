package cn.msa.msa_museum_server.repository;

import cn.msa.msa_museum_server.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Page<FileEntity> findAll(Pageable pageable);
}