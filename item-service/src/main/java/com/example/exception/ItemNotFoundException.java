package com.example.exception;

import com.example.constant.ErrorCode;

public class ItemNotFoundException extends CustomException {
    public ItemNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
