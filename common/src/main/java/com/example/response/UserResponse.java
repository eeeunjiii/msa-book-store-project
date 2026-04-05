package com.example.response;

import com.example.constant.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String address;
    private String phoneNum;
    private int point;

    @Enumerated(EnumType.STRING)
    private Role role;
}
