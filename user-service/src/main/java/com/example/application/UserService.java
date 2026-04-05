package com.example.application;

import com.example.constant.ErrorCode;
import com.example.domain.User;
import com.example.dto.TokenSetDto;
import com.example.dto.UserInfoDto;
import com.example.exception.PasswordIncorrectException;
import com.example.exception.UnauthorizedAccessTokenException;
import com.example.exception.UserExistedException;
import com.example.exception.UserNotFoundException;
import com.example.jwt.JwtUtil;
import com.example.redis.RedisUtil;
import com.example.jwt.RefreshTokenRepository;
import com.example.repository.UserRepository;
import com.example.request.JoinRequest;
import com.example.request.LoginRequest;
import com.example.response.UserResponse;
import com.example.util.UserMapper;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.constant.ErrorCode.UNAUTHORIZED_ACCESS_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Transactional
    public UserResponse join(JoinRequest request){
        String password=request.getPassword();
        String checkPassword=request.getCheckPassword();

        String encodedPassword=encoder.encode(password);

        checkPasswordIsEqual(password, checkPassword);
        validateDuplicationUser(userMapper.toEntity(request, encodedPassword));

        User user=userRepository.save(userMapper.toEntity(request, encodedPassword));

        return userMapper.toUserResponse(user);
    }

    private void checkPasswordIsEqual(String password, String checkPassword) {
        if(!password.equals(checkPassword)) throw new PasswordIncorrectException(ErrorCode.PASSWORD_NOT_EQUAL);
    }

    private void validateDuplicationUser(User user){
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m->{
                    throw new UserExistedException(ErrorCode.USER_EXISTED, Map.of(user.getEmail(), user));
                });
    }

    public boolean checkDuplicatedUserName(String name) {
        return userRepository.existsByName(name);
    }

    @Transactional
    public TokenSetDto login(LoginRequest req) {
        String email=req.getEmail();
        String password=req.getPassword();

        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if(!encoder.matches(password, user.getPassword())) {
            throw new PasswordIncorrectException(ErrorCode.EMAIL_PASSWORD_INCORRECT);
        }
        UserInfoDto userInfoDto=userMapper.toDto(user);
        return jwtUtil.createToken(userInfoDto);
    }

    @Transactional
    public ResponseCookie logout(String accessToken) throws IllegalAccessException{
        if (!jwtUtil.validateToken(accessToken)) {
            throw new UnauthorizedAccessTokenException(UNAUTHORIZED_ACCESS_TOKEN);
        }

        String email=jwtUtil.getEmail(accessToken);
        refreshTokenRepository.deleteByEmail(email);

        Long expiration=jwtUtil.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "access_token", expiration);

        return ResponseCookie.from("accessToken", "")
                .maxAge(0)
                .path("/")
                .build();
    }

    public List<User> findUserList(){
        return userRepository.findAll();
    }

    public UserInfoDto getUserInfoDtoByEmail(String email) {
        User user=findUserByEmail(email);
        return userMapper.toDto(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
