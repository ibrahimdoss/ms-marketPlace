package com.project.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(){}

    private ErrorCode errorCode;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
