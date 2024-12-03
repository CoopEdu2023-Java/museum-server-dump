package cn.msa.msa_museum_server.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;

    public BusinessException(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }
}
