package com.example.jwt;

import com.example.domain.RefreshToken;
import com.example.domain.User;
import com.example.dto.TokenSetDto;
import com.example.dto.UserInfoDto;
import com.example.application.UserService;
import com.example.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public TokenSetDto reissue(String refreshToken) throws IllegalAccessException {
        // 1. JWT 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalAccessException("Refresh Token is not validated.");
        }

        // 2. 토큰에서 정보 추출
        String jti=jwtUtil.getJti(refreshToken);
        String email= jwtUtil.getEmail(refreshToken);

        // 3. DB에 저장된 jti와 대조 ()
        RefreshToken savedToken=refreshTokenRepository.findByJti(jti)
                .orElseGet(() -> {
                    // 만약 jti로 대조했을 때 없다면
                    // 이미 사용되어 삭제 되었거나, 해킹 시도일 수 있으므로
                    // 이메일로 남은 토큰이 있다면 해당 이메일로 조회하여 모두 삭제
                    handlePotentialTheft(email);
                    throw new BadCredentialsException("Invalid Credential Information");
                });

        // jti로 조회가 된다면 해당 토큰은 삭제 조치
        refreshTokenRepository.delete(savedToken);
        refreshTokenRepository.flush();

        // 4. 새로운 토큰 세트 생성
        User user=userService.findUserByEmail(email);
        UserInfoDto userInfoDto=userMapper.toDto(user);

        return jwtUtil.createToken(userInfoDto);
    }

    @Transactional
    private void handlePotentialTheft(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}
