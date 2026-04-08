package com.example.exception;

import com.example.constant.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final LocalDateTime timeStamp;
    private final Map<String, Object> details=new HashMap<>();

    public CustomException(ErrorCode errorCode) {
        this.errorCode=errorCode;
        this.timeStamp=LocalDateTime.now();
    }

    public CustomException(ErrorCode errorCode, Map<String, Object> details) {
        this.errorCode=errorCode;
        this.timeStamp=LocalDateTime.now();
        this.details.putAll(details);
    }

    public String getMessage() {
        return errorCode.getMessage();
    }
}
