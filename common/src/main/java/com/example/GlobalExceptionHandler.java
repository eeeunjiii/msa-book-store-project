package com.example;

import com.example.constant.ErrorCode;
import com.example.dto.ExceptionDto;
import com.example.exception.CustomException;
import com.example.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("Caught NoHandlerFoundException - {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_PAGE));
    }

    @ExceptionHandler(value={CustomException.class})
    public ApiResponse<?> handleCustomException(CustomException e) {
        ExceptionDto exceptionDto=ExceptionDto.of(e);

        log.warn("Caught CustomException - exceptionType: {} | detail: {}", exceptionDto.getExceptionType(), exceptionDto);

        return ApiResponse.fail(e);
    }

    @ExceptionHandler(value={Exception.class})
    public ApiResponse<?> handleException(Exception e) {
        log.error("Caught Exception - {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
