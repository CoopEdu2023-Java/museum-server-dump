package cn.msa.msa_museum_server.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.msa.msa_museum_server.dto.ResponseDto;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDto<String> handleGlobalException(Exception e) {
        System.err.println("Global Exception: " + e);
        return new ResponseDto<>(1000, "Unknown Error: " + e.getMessage(), null); // 改为整数类型
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseDto<String> handleBusinessException(BusinessException e) {
        System.err.println("Business Exception: " + e);
        return new ResponseDto<>(e.getCode(), "Error: " + e.getMessage(), null);
    }
}