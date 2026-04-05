package com.example.util;

import com.example.domain.User;
import com.example.security.PrincipalDetails;
import com.example.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final UserService userService;

    @ModelAttribute("user")
    public User loginUser(@AuthenticationPrincipal PrincipalDetails principal) {
        if(principal!=null) {
            User user=userService.findUserByEmail(principal.getUsername());
            if(user!=null) {
                return user;
            }
        }
        return null;
    }
}
