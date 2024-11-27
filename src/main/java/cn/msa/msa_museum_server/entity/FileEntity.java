package cn.msa.msa_museum_server.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
class FileEntity {
    @Id
    @GeneratedValue
    private String id;

    // e.g., "test.pdf"
    private String filename;
    
    // e.g., "application/pdf"
    // 用nio里的 Files.probeContentType(Path) 生成
    private String contentType;
    
    // 文件字节数
    private long size;
}
