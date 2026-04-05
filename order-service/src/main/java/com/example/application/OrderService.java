package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.adapter.UserClient;
import com.example.event.CartOrderCreatedEvent;
import com.example.repository.OrderRepository;
import com.example.adapter.ItemClient;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.event.OrderCreatedEvent;
import com.example.producer.OrderCompletedProducer;
import com.example.request.CreateOrderItemRequest;
import com.example.request.OrderInfoRequest;
import com.example.response.CompleteOrderItemResponse;
import com.example.response.CompleteOrderResponse;
import com.example.response.ItemResponse;
import com.example.response.UserResponse;
import com.example.util.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemClient itemClient;
    private final UserClient userClient;
    private final OrderCompletedProducer producer;
    private final OrderMapper orderMapper;

    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public CompleteOrderResponse createOrder(Long userId, Long itemId,
                                             int quantity, OrderInfoRequest infoForm) {
        ItemResponse itemResponse=itemClient.sendItemResponse(itemId);

        Order order=Order.create(userId, infoForm);
        OrderItem orderItem=OrderItem.create(quantity, itemResponse.getId(), itemResponse.getPrice());

        order.addOrderItem(orderItem);

        orderRepository.save(order);

        OrderCreatedEvent event=OrderCreatedEvent.of(itemId, quantity);
        producer.publishOrderCreateEvent(event);

        CompleteOrderItemResponse orderItemResponse=orderMapper.toOrderItemResponse(itemResponse, orderItem, order);

        return orderMapper.toOrderResponse(order, List.of(orderItemResponse));
    }

    @Transactional
    public CompleteOrderResponse createOrder(String email, List<CreateOrderItemRequest> orderItemList,
                             OrderInfoRequest infoRequest) {
        List<OrderItem> orderItems=new ArrayList<>();
        List<Long> itemIds=new ArrayList<>();

        UserResponse userResponse=userClient.sendUserResponse(email);
        List<ItemResponse> itemResponses=fetchItemsInfo(orderItemList);

        Order order=Order.create(userResponse.getId(), infoRequest);

        for(CreateOrderItemRequest request:orderItemList) {
            itemIds.add(request.getItemId());

            OrderItem orderItem=OrderItem.create(request.getQuantity(),
                    request.getItemId(),
                    request.getOrderPrice());
            orderItems.add(orderItem);

            order.addOrderItem(orderItem);
        }
        orderRepository.save(order);

        CartOrderCreatedEvent event=CartOrderCreatedEvent.create(itemIds, userResponse.getId());
        producer.publishCartOrderEvent(event);

        List<CompleteOrderItemResponse> orderItemResponses=orderMapper.toOrderItemResponseList(itemResponses, orderItems, order);

        return orderMapper.toOrderResponse(order, orderItemResponses);
    }

    private List<ItemResponse> fetchItemsInfo(List<CreateOrderItemRequest> orderItemRequests) {
        List<Long> itemIds=orderItemRequests.stream()
                .map(CreateOrderItemRequest::getItemId)
                .collect(Collectors.toList());

        return itemClient.sendItemResponses(itemIds);
    }

    public List<CompleteOrderResponse> findOrderListByUser(String email) {
        UserResponse userResponse=userClient.sendUserResponse(email);
        List<Order> orders=orderRepository.findListByUserId(userResponse.getId());

        return orderMapper.toOrderResponses(orders);
    }

    public int getTotalPrice(List<CreateOrderItemRequest> orderItemDtoList) {
        Order order=orderRepository.findById(orderItemDtoList.get(0).getOrderId()).orElse(null);
        return order.getTotalPrice();
    }
}
