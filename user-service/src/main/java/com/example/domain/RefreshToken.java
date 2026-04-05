package com.example.domain;

import com.example.dto.TokenSetDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String jti;
    public String email;
    public Date expireTime;

    @Builder
    public RefreshToken(Long id, String jti, String email, Date expireTime) {
        this.id=id;
        this.jti=jti;
        this.email=email;
        this.expireTime=expireTime;
    }

    static public RefreshToken from(TokenSetDto tokenSetDto) {
        return RefreshToken.builder()
                .jti(tokenSetDto.getRefreshJti())
                .email(tokenSetDto.getEmail())
                .expireTime(tokenSetDto.getRefreshExpireTime())
                .build();
    }
}
