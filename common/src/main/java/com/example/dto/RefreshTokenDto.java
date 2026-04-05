package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    private String jti;
    private String email;
    private Date expireTime;
    private String tokenValue;
}
