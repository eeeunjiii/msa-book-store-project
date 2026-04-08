package com.example.exception;

import com.example.constant.ErrorCode;

public class ReusedTokenException extends CustomException {
    public ReusedTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
