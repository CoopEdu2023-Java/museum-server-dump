package cn.msa.museum_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

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
