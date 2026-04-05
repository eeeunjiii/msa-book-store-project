package com.example.response;

import com.example.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final LocalDateTime timeStamp;
    private final String error;
    private final String message;
    private final HttpStatus httpStatus;

    public ErrorResponse(ErrorCode errorCode) {
        this.timeStamp=LocalDateTime.now();
        this.error=errorCode.getErrorCode();
        this.message=errorCode.getMessage();
        this.httpStatus=errorCode.getHttpStatus();
    }
}
