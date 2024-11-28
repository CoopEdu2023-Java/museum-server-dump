package cn.msa.msa_museum_server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

    MISSING_PARAMETERS(1001, "Missing Parameters"),
    USER_EXISTS(2001, "User exists"),
    USER_DOES_NOT_EXIST(2002, "User does not exist"),
    WRONG_PASSWORD(2003, "Wrong password"),
    NEW_PASSWORD_SAME_AS_OLD(2004, "Your new password is the same as the old one. Change a different password."),
    NEW_PASSWORD_UNDER_REQUIREMENTS(2005,
            "Your new password must have a length of 8 and has at least one of the following: Uppercase Letters [A-Z], lowercase Letters [a-z], numbers [0-9], and symbols[!@~]."),
    FILE_NOT_FOUND(2007, "File not found"),
    INVALID_FILE_REQUEST_TYPE(2008, "Invalid file request type"),
    MALFORMED_FILE_PATH(2009, "Malformed file path");

    private final Integer code;
    private final String message;
}
