package cn.msa.msa_museum_server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.msa.msa_museum_server.dto.LoginDto;
import cn.msa.msa_museum_server.entity.UserEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.UserRepository;
import cn.msa.msa_museum_server.service.JwtService;
import cn.msa.msa_museum_server.service.UserService;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        UserEntity existUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.WRONG_PASSWORD));

        if (!bCryptPasswordEncoder.matches(password, existUser.getPassword())) {
            throw new BusinessException(ExceptionEnum.WRONG_PASSWORD);
        }
        return jwtService.setToken(existUser);
    }
}