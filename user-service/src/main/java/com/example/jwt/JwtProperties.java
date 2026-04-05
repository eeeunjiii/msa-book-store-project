package com.example.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private Long accessExpirationTime;
    private Long refreshExpirationTime;
}
