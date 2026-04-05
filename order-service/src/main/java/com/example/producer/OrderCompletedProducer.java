package com.example.producer;

import com.example.event.CartOrderCreatedEvent;
import com.example.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCompletedProducer {
    private static final String ORDER_COMPLETED_TOPIC="order-completed-topic";
    private static final String CART_ORDER_COMPLETED_TOPIC="cart-order-completed-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreateEvent(OrderCreatedEvent event) { // Item의 수량을 주문 수량만큼 빼는 로직
        log.info("Sending message to kafka: {} on topic: {}", event.getQuantity(), ORDER_COMPLETED_TOPIC);

        kafkaTemplate.send(ORDER_COMPLETED_TOPIC, event)
                .whenComplete((result, ex) -> {
                    if (ex==null) {
                        log.info("Message sent successfully: {}", result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }

    public void publishCartOrderEvent(CartOrderCreatedEvent event) { kafkaTemplate.send(CART_ORDER_COMPLETED_TOPIC, event); }
}
