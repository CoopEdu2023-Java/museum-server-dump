package cn.msa.msa_museum_server.exception;

public enum ExceptionEnum {
    INVALID_PARAMETER(400, "Invalid parameter"),
    FILE_NOT_FOUND(404, "File not found"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    FILE_UPLOAD_ERROR(500, "Failed to upload file"),
    DIRECTORY_CREATION_FAILED(500, "Failed to create directory"),
    DATABASE_SAVE_ERROR(500, "Failed to save file information to the database");

    private final int code;
    private final String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

