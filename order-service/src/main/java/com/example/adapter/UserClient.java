package com.example.adapter;

import com.example.request.UserRequest;
import com.example.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserClient {
    private final WebClient webClient;

    public UserClient (WebClient.Builder builder) {
        this.webClient=builder.baseUrl("http://api-gateway").build();
    }

    public UserResponse sendUserResponse(String email) {
        UserRequest userRequest=createUserRequest(email);

        return webClient.post()
                .uri("/user-service/user")
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    private UserRequest createUserRequest(String email) {
        return new UserRequest(email);
    }
}
