package cn.msa.msa_museum_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.msa.msa_museum_server.entity.UserEntity;

public interface FileRepository extends JpaRepository<UserEntity, Integer> {

}