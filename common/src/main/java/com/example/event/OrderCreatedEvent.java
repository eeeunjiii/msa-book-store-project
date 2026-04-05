package com.example.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long itemId;
    private int quantity;

    public static OrderCreatedEvent of(Long itemId, int quantity) {
        return new OrderCreatedEvent(itemId, quantity);
    }
}
