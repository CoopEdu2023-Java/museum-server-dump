package cn.msa.msa_museum_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class FileEntity {
    
    // 使用 UUID 类型作为主键
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; // 使用 UUID 作为主键

    @Column(nullable = false, length = 255) // 确保 filename 不为空，且长度为 255
    private String filename; // 文件名

    @Column(length = 100) // content_type 的最大长度为 100
    private String contentType; // 文件的 MIME 类型

    @Column(nullable = false) // 确保 size 不为空
    private long size; // 文件大小（以字节为单位）

    // 默认构造函数
    // public FileEntity() {
    //     // 使用 UUID 随机生成 ID
    //     this.id = UUID.randomUUID();
    // }
    
    // 根据实际需求可以加入其他方法，如：
    public FileEntity(String filename, String contentType, long size) {
        this.filename = filename;
        this.contentType = contentType;
        this.size = size;
    }
}
