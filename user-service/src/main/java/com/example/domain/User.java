package com.example.domain;

import com.example.constant.Role;
import com.example.dto.UserInfoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;
    private String password;
    private String address;
    private String phoneNum;
    private int point;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String email, String name, String password, String address,
        String phoneNum, int point, Role role){
        this.id=id;
        this.email=email;
        this.name=name;
        this.password=password;
        this.address=address;
        this.phoneNum = phoneNum;
        this.point=point;
        this.role=role;
    }
}
