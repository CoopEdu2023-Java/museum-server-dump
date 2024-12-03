package cn.msa.msa_museum_server.repository;

import java.util.Optional;
import cn.msa.msa_museum_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByUsername(String username);
    public boolean existsByUsernameAndPassword(String username, String password);

    public Optional<UserEntity> findByUsernameAndPassword(String username, String password);
    public boolean existsByUsername(String username);
} 