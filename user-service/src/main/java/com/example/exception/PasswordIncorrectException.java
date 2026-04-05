package com.example.exception;

import com.example.constant.ErrorCode;

public class PasswordIncorrectException extends CustomException {
    public PasswordIncorrectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
