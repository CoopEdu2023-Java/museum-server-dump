package cn.msa.msa_museum_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    
    // 添加无参构造函数
    public UserEntity() {
    }
    
    // 现有的构造函数
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    
}
