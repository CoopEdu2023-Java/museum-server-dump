package cn.msa.msa_museum_server.service;

import cn.msa.msa_museum_server.dto.LoginDto;
import cn.msa.msa_museum_server.dto.ResetPasswordDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  public String login(LoginDto loginDto);

  public void resetPassword(ResetPasswordDto resetpasswordDto);
}
