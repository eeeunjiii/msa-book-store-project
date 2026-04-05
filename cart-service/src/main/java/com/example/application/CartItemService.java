package com.example.application;

import com.example.repository.CartItemRepository;
import com.example.domain.CartItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public List<CartItem> findCartItemsByCart(Long cartId) {
        return cartItemRepository.findCartItemsByCart(cartId);
    }

    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
         return cartItemRepository.findByCartIdAndItemId(cartId, itemId)
                .orElse(null);
    }

    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }
}
