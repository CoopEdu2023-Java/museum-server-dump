package cn.msa.msa_museum_server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import cn.msa.msa_museum_server.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
}