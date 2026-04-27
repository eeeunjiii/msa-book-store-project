package com.example.application;

import java.util.List;

import com.example.adapter.ItemClient;
import com.example.constant.ErrorCode;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.exception.OrderNotFoundException;
import com.example.repository.OrderRepository;
import com.example.response.CompleteOrderItemResponse;
import com.example.response.ItemResponse;
import com.example.util.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemClient itemClient;

    public List<CompleteOrderItemResponse> findOrderItemList(Long orderId) {
        Order order=orderRepository.findById(orderId).orElse(null);

        if (order!=null) {
            List<OrderItem> orderItems=order.getOrderItems();

            List<Long> itemIds=orderItems.stream()
                    .map(OrderItem::getItemId)
                    .toList();

            List<ItemResponse> itemResponses=itemClient.sendItemResponses(itemIds);
            return orderMapper.toOrderItemResponseList(itemResponses, orderItems);
        } else {
            throw new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND);
        }
    }

    public OrderItem createOrderItem(ItemResponse itemResponse, int quantity) {
        return OrderItem.create(quantity, itemResponse.getId(), itemResponse.getPrice());
    }
}
