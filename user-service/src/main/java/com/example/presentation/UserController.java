package com.example.presentation;

import com.example.domain.User;
import com.example.dto.TokenSetDto;
import com.example.jwt.JwtUtil;
import com.example.request.JoinRequest;
import com.example.request.LoginRequest;
import com.example.request.UserRequest;
import com.example.response.ApiResponse;
import com.example.response.UserResponse;
import com.example.security.PrincipalDetails;
import com.example.application.UserService;
import com.example.util.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;


    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<String>> getCurrentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(ApiResponse.success(principalDetails.getUsername(), "사용자 조회 성공"));
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@RequestBody UserRequest userRequest) { // Order Service에서 사용자 정보 얻어올 때 사용하는 API
        User user=userService.findUserByEmail(userRequest.getEmail());
        UserResponse response=userMapper.toUserResponse(user);

        return ResponseEntity.ok(ApiResponse.success(response, "사용자 조회 성공"));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<UserResponse>> join(@RequestBody JoinRequest joinRequest) {
        UserResponse response=userService.join(joinRequest);

        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenSetDto tokenSetDto=userService.login(loginRequest);

        ResponseCookie accessCookie=jwtUtil.createAccessTokenCookie(tokenSetDto.getAccessToken());
        ResponseCookie refreshCookie=jwtUtil.createRefreshTokenCookie(tokenSetDto.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(ApiResponse.success(tokenSetDto.getEmail(), "로그인 성공"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@CookieValue(name = "accessToken") String accessToken,
                                    HttpServletResponse response) throws IllegalAccessException {
        ResponseCookie accessCookie=userService.logout(accessToken);
        ResponseCookie refreshCookie=jwtUtil.expireRefreshTokenCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }

    @GetMapping("/{userId}/my-page")
    public ResponseEntity<ApiResponse<UserResponse>> myPage(@PathVariable("userId") Long userId) {
        User user=userService.findUserById(userId);
        UserResponse response=userMapper.toUserResponse(user);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
