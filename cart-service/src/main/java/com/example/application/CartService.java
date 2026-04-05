package com.example.application;

import com.example.repository.CartRepository;
import com.example.domain.Item;
import com.example.domain.Cart;
import com.example.domain.CartItem;

import com.example.event.CartOrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final CartItemService cartItemService;

    @Transactional
    public void addItemToCart(Long userId, Item item, int quantity) {
        Cart cart=findCartByUserId(userId);

        if(cart==null) {
            cart= Cart.createCart(userId);
            save(cart);
        }

        Item findItem=itemService.findById(item.getId());
        CartItem cartItem=cartItemService.findByCartIdAndItemId(cart.getId(), findItem.getId());

        if(cartItem==null) { // 새로 추가한 품목인 경우
            cartItem=CartItem.create(quantity, item.getId());
//            cartItemService.save(cartItem);
        } else { // 이미 담아둔 품목인 경우, 개수만 증가
            cart.removeCartItem(cartItem);
            cartItem.updateCartItem(cartItem.getQuantity()+quantity);
//            cartItemService.save(cartItem);
        }
        updateCartTotalQuantity(cart, quantity);

        cart.addCartItem(cartItem);

        save(cart);
    }

    @Transactional
    @KafkaListener(topics = "cart-order-completed-topic", groupId = "cart-service-group")
    public void removeOrderedItemFromCart(CartOrderCreatedEvent event) {
        Cart cart=findCartByUserId(event.getUserId());

        for (Long itemId:event.getItemIds()) {
            CartItem cartItem=cartItemService.findByCartIdAndItemId(cart.getId(), itemId);
            cart.removeCartItem(cartItem);
            cartItemService.delete(cartItem);
        }
    }

    @Transactional
    public Cart findCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }

    private void save(Cart cart) {
        cartRepository.save(cart);
    }

    private void updateCartTotalQuantity(Cart cart, int quantity) {
        cart.updateCart(quantity);
    }
}
