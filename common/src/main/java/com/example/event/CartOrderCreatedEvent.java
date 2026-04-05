package com.example.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderCreatedEvent { // Cart에 있는 OrderItem을 삭제할 때 발행되는 이벤트
    private List<Long> itemIds;
    private Long userId;

    public static CartOrderCreatedEvent create(List<Long> itemIds, Long userId) {
        return new CartOrderCreatedEvent(itemIds, userId);
    }
}
