package com.example.adapter;

import com.example.response.ItemResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
        return webClient.get()
                .uri("/item-service/{itemId}", itemId)
                .retrieve()
                .bodyToMono(ItemResponse.class)
                .block();
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
