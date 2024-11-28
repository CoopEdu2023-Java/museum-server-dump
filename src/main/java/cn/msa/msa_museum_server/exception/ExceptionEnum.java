package cn.msa.msa_museum_server.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    // 通用错误码
    INVALID_ENTRY(1001, "Invalid entry"),
    INVALID_ENTRY_TYPE(1002, "Invalid entry type");

    private final Integer code;
    private final String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}