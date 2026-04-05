package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenSetDto {
    private String email;
    private String accessToken;
    private String refreshToken;
    private Date accessExpireTime;
    private Date refreshExpireTime;
    private String refreshJti;
}
