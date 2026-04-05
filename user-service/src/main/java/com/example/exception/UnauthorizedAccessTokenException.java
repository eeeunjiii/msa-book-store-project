package com.example.exception;

import com.example.constant.ErrorCode;

public class UnauthorizedAccessTokenException extends CustomException {
    public UnauthorizedAccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
