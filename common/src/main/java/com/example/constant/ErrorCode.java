package com.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_PAGE(HttpStatus.NOT_FOUND, "PAGE_001", "존재하지 않는 API입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_001", "서버 내부 오류입니다."),

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_001", "유저를 찾을 수 없습니다."),
    EMAIL_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST,"USER_002", "이메일이나 비밀번호를 다시 입력해주세요."),
    PASSWORD_NOT_EQUAL(HttpStatus.UNAUTHORIZED, "USER_003", "비밀번호가 일치하지 않습니다."),
    USER_EXISTED(HttpStatus.CONFLICT,"USER_004", "이미 존재하는 사용자입니다."),

    // JWT
    UNAUTHORIZED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_001", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_002", "유효하지 않은 토큰입니다."),
    ALREADY_USED_TOKEN(HttpStatus.FORBIDDEN, "JWT_003", "이미 사용된 토큰입니다."),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_001", "주문을 찾을 수 없습니다."),

    // Item
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_001", "도서를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
