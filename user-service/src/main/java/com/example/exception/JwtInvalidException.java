package com.example.exception;

import com.example.constant.ErrorCode;

public class JwtInvalidException extends CustomException {
    public JwtInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
