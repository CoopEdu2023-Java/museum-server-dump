package cn.msa.msa_museum_server.service.impl;

import cn.msa.msa_museum_server.dto.ResetPasswordDto;
import cn.msa.msa_museum_server.entity.UserEntity;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.UserRepository;
import cn.msa.msa_museum_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;

  private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Override
  public void resetPassword(ResetPasswordDto resetpasswordDto) {
    String username = resetpasswordDto.getUsername();
    String password = resetpasswordDto.getPassword();
    String new_password = resetpasswordDto.getNewPassword();

    UserEntity existUser =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new BusinessException(ExceptionEnum.USER_DOES_NOT_EXIST));

    if (!bCryptPasswordEncoder.matches(password, existUser.getPassword())) {
      throw new BusinessException(ExceptionEnum.WRONG_PASSWORD);
    }

    if (bCryptPasswordEncoder.matches(new_password, existUser.getPassword())) {
      throw new BusinessException(ExceptionEnum.NEW_PASSWORD_SAME_AS_OLD);
    }

    if (!isValidPassword(new_password)) {
      throw new BusinessException(ExceptionEnum.NEW_PASSWORD_UNDER_REQUIREMENTS);
    }

    String hashedNewPassword = bCryptPasswordEncoder.encode(new_password);
    existUser.setPassword(hashedNewPassword);
    userRepository.save(existUser);
  }

  private boolean isValidPassword(String password) {
    // Example requirements
    return password.length() >= 8
        && password.matches(".*[A-Z].*")
        && password.matches(".*[a-z].*")
        && password.matches(".*[0-9].*")
        && password.matches(".*[!@#$%^&*()].*");
  }
}
