package com.example.response;

import com.example.dto.ExceptionDto;
import com.example.exception.CustomException;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;

    @Nullable
    private final T data;

    @Nullable
    private final ExceptionDto error;

    @Nullable
    private final String message;

    public static <T> ApiResponse<T> success(final T data, String message) {
        return new ApiResponse<>(true, data, null, message);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, null, null, message);
    }

    public static <T> ApiResponse<T> success(final T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> created(@Nullable final T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> fail(final CustomException e) {
        return new ApiResponse<>(false, null, ExceptionDto.of(e), null);
    }
}
