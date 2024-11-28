package cn.msa.msa_museum_server.controller;

import cn.msa.msa_museum_server.exception.BusinessException;
import cn.msa.msa_museum_server.exception.ExceptionEnum;
import cn.msa.msa_museum_server.repository.UserRepository;
import cn.msa.msa_museum_server.service.impl.UserServiceimpl;
import cn.msa.msa_museum_server.dto.LoginDto;
import cn.msa.msa_museum_server.dto.ResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserServiceimpl userService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
	public ResponseDto<String> login(@RequestBody LoginDto loginDto) {
        if (loginDto.getUsername() == null || loginDto.getPassword() == null) {
			throw new BusinessException(ExceptionEnum.MISSING_PARAMETERS);
		}

        String token = userService.login(loginDto);
		return new ResponseDto<String>(token);
	}

    

}
