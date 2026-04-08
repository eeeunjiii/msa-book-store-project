package com.example.dto;

import com.example.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionDto {
    private final String code;
    private final String message;
    private final String exceptionType;

    public static ExceptionDto of(CustomException e) {
        return new ExceptionDto(
                e.getErrorCode().getErrorCode(),
                e.getErrorCode().getMessage(),
                e.getClass().getSimpleName()
        );
    }
}
