package cn.msa.msa_museum_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.msa.msa_museum_server.dto.LoginDto;
import cn.msa.msa_museum_server.dto.ResetPasswordDto;
import cn.msa.msa_museum_server.dto.ResponseDto;
import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService; // 修改变量名，语义更清晰

    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody LoginDto loginDto) {
        if (loginDto.getUsername() == null || loginDto.getPassword() == null) {
            throw new BusinessException(ExceptionEnum.MISSING_PARAMETERS);
        }

        String token = userService.login(loginDto);
        return new ResponseDto<String>(token);
    }

    @PostMapping("/password/reset")
    public ResponseDto<Void> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        if (resetPasswordDto.getUsername() == null || resetPasswordDto.getPassword() == null
                || resetPasswordDto.getNewPassword() == null) {
            throw new BusinessException(ExceptionEnum.MISSING_PARAMETERS);
        }
        userService.resetPassword(resetPasswordDto);
        return new ResponseDto<>();
    }

}
