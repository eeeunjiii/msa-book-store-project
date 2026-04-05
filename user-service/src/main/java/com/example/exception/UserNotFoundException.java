package com.example.exception;

import com.example.constant.ErrorCode;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
