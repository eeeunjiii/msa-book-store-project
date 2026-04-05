package com.example.dto;

import com.example.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String email;
    private Role role;
}
