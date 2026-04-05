package com.example.util;

import com.example.domain.Item;
import com.example.request.NewItemRequest;
import com.example.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ModelMapper modelMapper;

    public ItemResponse mapToItemResponse(Item item) {
        return modelMapper.map(item, ItemResponse.class);
    }

    public Item toEntity(NewItemRequest newItemRequest) {
        return Item.builder()
            .title(newItemRequest.getTitle())
            .author(newItemRequest.getAuthor())
            .publisher(newItemRequest.getPublisher())
            .publish_year(newItemRequest.getPublish_year())
            .price(newItemRequest.getPrice())
            .stock(newItemRequest.getStock())
            .build();
    }

}
