package com.example.util;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.dto.DeliveryInfo;
import com.example.response.CompleteOrderItemResponse;
import com.example.response.CompleteOrderResponse;
import com.example.response.ItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // Order
    public DeliveryInfo toDeliveryInfo(Order order) {
        return DeliveryInfo.of(
                order.getPhoneNumber(),
                order.getDestination(),
                order.getCustomer(),
                order.getMemo(),
                order.getDeliveryStatus());
    }

    public CompleteOrderResponse toOrderResponse(Order order, List<CompleteOrderItemResponse> orderItems) {
        return CompleteOrderResponse.of(
                order.getId(),
                order.getOrderDate(),
                orderItems,
                toDeliveryInfo(order),
                order.getTotalPrice());
    }

    public CompleteOrderResponse toOrderResponse(Order order, DeliveryInfo deliveryInfo) {
        return CompleteOrderResponse.of(
                order.getId(),
                order.getOrderDate(),
                deliveryInfo);
    }

    public List<CompleteOrderResponse> toOrderResponseList(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    DeliveryInfo deliveryInfo=toDeliveryInfo(order);
                    return toOrderResponse(order, deliveryInfo);
                }).toList();
    }


    // OrderItem
    public CompleteOrderItemResponse toOrderItemResponse(ItemResponse item, OrderItem orderItem) {
        return CompleteOrderItemResponse.of(
                item.getTitle(),
                orderItem.getOrderCount(),
                orderItem.getOrderPrice(),
                orderItem.getTotalPrice()
        );
    }

    public List<CompleteOrderItemResponse> toOrderItemResponseList(List<ItemResponse> items, List<OrderItem> orderItems) {
        Map<Long, ItemResponse> itemMap=items.stream()
                .collect(Collectors.toMap(ItemResponse::getId, item->item));

        return orderItems.stream()
                .map(orderItem -> {
                    ItemResponse item=itemMap.get(orderItem.getItemId());

                    return toOrderItemResponse(item, orderItem);
                })
                .toList();
    }
}
