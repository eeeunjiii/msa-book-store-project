package com.example.dto;

import com.example.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExceptionDto {
    private final LocalDateTime timeStamp;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final String exceptionType;
    private final Map<String, Object> details;

    public static ExceptionDto of(CustomException e) {
        return new ExceptionDto(
                e.getTimeStamp(),
                e.getErrorCode().getHttpStatus(),
                e.getErrorCode().getErrorCode(),
                e.getErrorCode().getMessage(),
                e.getClass().getSimpleName(),
                e.getDetails()
        );
    }
}
