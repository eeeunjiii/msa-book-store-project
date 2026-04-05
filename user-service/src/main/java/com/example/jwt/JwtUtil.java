package com.example.jwt;

import com.example.domain.RefreshToken;
import com.example.dto.RefreshTokenDto;
import com.example.dto.TokenSetDto;
import com.example.dto.UserInfoDto;
import com.example.redis.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private Key key;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        byte[] keyBytes= Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenSetDto createToken(UserInfoDto userInfoDto) {
        Date accessTokenExpirationTime=new Date(System.currentTimeMillis()+jwtProperties.getAccessExpirationTime());
        Date refreshTokenExpirationTime=new Date(System.currentTimeMillis()+jwtProperties.getRefreshExpirationTime());

        String accessToken=createAccessToken(userInfoDto, accessTokenExpirationTime);
        RefreshTokenDto refreshTokenDto=createRefreshToken(userInfoDto, refreshTokenExpirationTime);

        save(refreshTokenDto);

        return TokenSetDto.builder()
                .email(userInfoDto.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshTokenDto.getTokenValue())
                .accessExpireTime(accessTokenExpirationTime)
                .refreshExpireTime(refreshTokenExpirationTime)
                .refreshJti(refreshTokenDto.getJti())
                .build();
    }

    public String createAccessToken(UserInfoDto userInfoDto, Date expireTime) {
        String jti= UUID.randomUUID().toString();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setId(jti)
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(expireTime)
                .claim("userId", userInfoDto.getUserId())
                .claim("email", userInfoDto.getEmail())
                .claim("role", userInfoDto.getRole())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshTokenDto createRefreshToken(UserInfoDto userInfoDto, Date expireTime) {
        String jti=UUID.randomUUID().toString();

        String refreshToken=Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setId(jti)
                .setSubject("RefreshToken")
                .setIssuedAt(new Date())
                .setExpiration(expireTime)
                .claim("userId", userInfoDto.getUserId())
                .claim("email", userInfoDto.getEmail())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return RefreshTokenDto.builder()
                .jti(jti)
                .tokenValue(refreshToken)
                .email(userInfoDto.getEmail())
                .expireTime(expireTime)
                .build();
    }

    private void save(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken=RefreshToken.builder()
                .jti(refreshTokenDto.getJti())
                .email(refreshTokenDto.getEmail())
                .expireTime(refreshTokenDto.getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        long accessMaxAge=jwtProperties.getAccessExpirationTime()/1000;

        return ResponseCookie.from("accessToken", accessToken)
                .secure(true)
                .httpOnly(true) // JavaScript 접근을 차단하여 XSS 공격 차단
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Lax") // CSRF 보안 취약점 방지
                .build();

    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        long refreshMaxAge=jwtProperties.getRefreshExpirationTime()/1000;

        return ResponseCookie.from("refreshToken", refreshToken)
                .secure(true)
                .httpOnly(true)
                .path("/api/v1/users/reissue") // 해당 쿠키를 전송할 url 지정
                .maxAge(refreshMaxAge)
                .sameSite("Lax")
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !redisUtil.hasKeyBlackList(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("JWT 토큰이 만료되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims가 비어 있습니다.", e);
        }
        return false;
    }

    public ResponseCookie expireRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .path("/api/v1/users/reissue")
                .build();
    }

    public Long getExpiration(String token) {
        try {
            Date expiration=parseClaims(token).getExpiration();
            Long now=new Date().getTime();

            return expiration.getTime()-now;
        } catch (Exception e) {
            return 0L;
        }
    }

    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    public String getJti(String refreshToken) {
        return parseClaims(refreshToken).getId();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
