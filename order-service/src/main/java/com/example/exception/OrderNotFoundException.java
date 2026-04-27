package com.example.exception;

import com.example.constant.ErrorCode;

public class OrderNotFoundException extends CustomException {
    public OrderNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
