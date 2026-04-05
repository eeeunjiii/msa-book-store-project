package com.example;

import com.example.dto.ExceptionDto;
import com.example.exception.CustomException;
import com.example.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={CustomException.class})
    public ResponseEntity<?> handleException(CustomException e) {
        ExceptionDto exceptionDto=ExceptionDto.of(e);

        log.warn("Exception Caught - exceptionType: {} | detail: {}", exceptionDto.getExceptionType(), exceptionDto);

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ErrorResponse(e.getErrorCode()));
    }
}
