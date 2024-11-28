package cn.msa.msa_museum_server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import cn.msa.msa_museum_server.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByUsername(String username);

    public boolean existsByUsernameAndPassword(String username, String password);

    public Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    public boolean existsByUsername(String username);
}
