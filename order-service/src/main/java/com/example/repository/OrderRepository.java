package com.example.repository;

import java.util.List;
import java.util.Optional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findListByUserId(Long userId);
    Optional<Order> findById(Long orderId);

    @Query("select oi from OrderItem oi join fetch oi.order where oi.order.id=:orderId")
    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}
