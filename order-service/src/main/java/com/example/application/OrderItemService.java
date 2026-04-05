package com.example.application;

import java.util.List;

import com.example.domain.OrderItem;
import com.example.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {
    private final OrderRepository orderRepository;

    public List<OrderItem> findListByOrderId(Long orderId) {
//        return orderItemRepository.findOrderItemByOrderId(orderId);
        return orderRepository.findOrderItemsByOrderId(orderId);
    }
}
