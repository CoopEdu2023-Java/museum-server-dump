package cn.msa.msa_museum_server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    // Error codes
    MISSING_PARAMETERS(1001, "Missing the parameters"),
    USER_EXISTS(2001, "User already exists"),
    USER_DOES_NOT_EXIST(2002, "User does not exist"),
    WRONG_PASSWORD(2003, "Wrong password");

    private final Integer code;
    private final String message;
}
