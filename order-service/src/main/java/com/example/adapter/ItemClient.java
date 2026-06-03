package com.example.adapter;

import com.example.response.ApiResponse;
import com.example.response.ItemResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ItemClient {
    private final WebClient webClient;

    public ItemClient(WebClient.Builder builder) {
        this.webClient=builder.baseUrl("http://api-gateway").build();
    }

    public ItemResponse sendItemResponse(Long itemId) { // OrderController
        ParameterizedTypeReference<ApiResponse<ItemResponse>> responseType=
                new ParameterizedTypeReference<>() {
                };

        ApiResponse<ItemResponse> response=webClient.get()
                .uri("/item-service/{itemId}", itemId)
                .retrieve()
                .bodyToMono(responseType)
                .block();

        return response!=null?response.getData():null;
    }

    public List<ItemResponse> sendItemResponses(List<Long> itemIds) {
        return webClient.post()
                .uri("/bulk")
                .bodyValue(itemIds)
                .retrieve()
                .bodyToFlux(ItemResponse.class)
                .collectList()
                .block();
    }
}
