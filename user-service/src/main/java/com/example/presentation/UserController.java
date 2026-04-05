package com.example.presentation;

import com.example.domain.User;
import com.example.dto.TokenSetDto;
import com.example.jwt.JwtUtil;
import com.example.request.JoinRequest;
import com.example.request.LoginRequest;
import com.example.request.UserRequest;
import com.example.response.UserResponse;
import com.example.security.PrincipalDetails;
import com.example.application.UserService;
import com.example.util.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        return ResponseEntity.ok(principalDetails.getUsername());
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> getUser(@RequestBody UserRequest userRequest) { // Order Service에서 사용자 정보 얻어올 때 사용하는 API
        User user=userService.findUserByEmail(userRequest.getEmail());
        UserResponse userResponse=userMapper.toUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/join")
    public ResponseEntity<UserResponse> join(@RequestBody JoinRequest joinRequest) {
        UserResponse userResponse=userService.join(joinRequest);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenSetDto tokenSetDto =userService.login(loginRequest);

        ResponseCookie accessCookie=jwtUtil.createAccessTokenCookie(tokenSetDto.getAccessToken());
        ResponseCookie refreshCookie=jwtUtil.createRefreshTokenCookie(tokenSetDto.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok("Login successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "accessToken") String accessToken,
                                    HttpServletResponse response) throws IllegalAccessException {
        ResponseCookie accessCookie=userService.logout(accessToken);
        ResponseCookie refreshCookie=jwtUtil.expireRefreshTokenCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok("Logout Successfully");
    }

    @GetMapping("/{userId}/my-page")
    public ResponseEntity<UserResponse> myPage(@PathVariable("userId") Long userId) {
        User user=userService.findUserById(userId);
        UserResponse userResponse=userMapper.toUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }
}
