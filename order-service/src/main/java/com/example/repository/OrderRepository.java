package com.example.repository;

import java.util.List;
import java.util.Optional;

import com.example.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findListByUserId(Long userId);
    Optional<Order> findById(Long orderId);
}
