package cn.msa.msa_museum_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import cn.msa.msa_museum_server.dto.RegisterDto;
import cn.msa.msa_museum_server.entity.UserEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    // Register API
    public void register(RegisterDto registerDto) {
        String username = registerDto.getUsername();
        String password = bCryptPasswordEncoder.encode(registerDto.getPassword());

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(ExceptionEnum.USER_EXISTS);
        }

        userRepository.save(new UserEntity(username, password));
    }

}