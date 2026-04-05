package com.example.jwt;

import com.example.dto.TokenSetDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {

    private final JwtService jwtService;
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(@CookieValue(name = "refreshToken") String refreshToken,
                                          HttpServletResponse response) throws IllegalAccessException {
        TokenSetDto tokenSetDto=jwtService.reissue(refreshToken);

        ResponseCookie accessCookie=jwtUtil.createAccessTokenCookie(tokenSetDto.getAccessToken());
        ResponseCookie refreshCookie= jwtUtil.createRefreshTokenCookie(tokenSetDto.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok("Reissue successfully");
    }
}
