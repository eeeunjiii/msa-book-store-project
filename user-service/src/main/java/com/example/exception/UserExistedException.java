package com.example.exception;

import com.example.constant.ErrorCode;

import java.util.Map;

public class UserExistedException extends CustomException {
    public UserExistedException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
