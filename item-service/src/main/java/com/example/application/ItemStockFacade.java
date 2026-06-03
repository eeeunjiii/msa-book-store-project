package com.example.application;

import com.example.annotation.DistributedLock;
import com.example.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemStockFacade {

    private final ItemService itemService;

    @KafkaListener(topics = "order-completed-topic", groupId = "item-service-group")
    @DistributedLock(key="'item:lock' + #event.itemId", waitTime = 5, leaseTime = 10)
    public void reduceItemStock(OrderCreatedEvent event) {
        itemService.reduceItemStockTransactional(event);
    }
}
