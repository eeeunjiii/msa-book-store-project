package com.example.repository;

import com.example.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndItemId(@Param("cartId") Long cartId, @Param("itemId") Long itemId);

    List<CartItem> findCartItemsByCart(Long cartId);
}
