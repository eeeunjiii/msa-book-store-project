package com.example.util;

import com.example.domain.User;
import com.example.constant.Role;
import com.example.dto.UserInfoDto;
import com.example.request.JoinRequest;
import com.example.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    public User toEntity(JoinRequest joinRequest, String encodedPassword) {
        return User.builder()
            .email(joinRequest.getEmail())
            .name(joinRequest.getName())
            .address(joinRequest.getAddress())
            .phoneNum(joinRequest.getPhoneNum())
            .password(encodedPassword)
            .role(Role.USER)
            .point(0)
            .build();
    }

    public UserInfoDto toDto(User user) {
        return UserInfoDto.builder()
                .email(user.getEmail())
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .address(user.getAddress())
                .phoneNum(user.getPhoneNum())
                .point(user.getPoint())
                .role(user.getRole())
                .build();
    }
}
