package cn.msa.msa_museum_server.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.msa.msa_museum_server.dto.ResponseDto;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDto<String> exceptionHandler(Exception e) {
        System.out.println("The error exception: " + e);
        return new ResponseDto<String>(1949, "Unknwon Error: " + e.getMessage(), null);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseDto<String> exceptionHandler(BusinessException e) {
        System.out.println("The error exception: " + e);
        return new ResponseDto<String>(e.getCode(), "Unknwon Error: " + e.getMessage(), null);
    }
}
