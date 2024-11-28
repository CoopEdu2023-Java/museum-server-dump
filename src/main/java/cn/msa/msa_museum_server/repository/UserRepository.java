package cn.msa.msa_museum_server.repository;


import cn.msa.msa_museum_server.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByUsername(String username);
}