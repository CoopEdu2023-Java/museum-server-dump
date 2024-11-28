package cn.msa.msa_museum_server.service;
import cn.msa.msa_museum_server.dto.ResetPasswordDto;

public interface UserService {
    public void resetPassword(ResetPasswordDto resetpasswordDto);
}