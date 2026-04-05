package com.example.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems=new ArrayList<>();

    @Builder
    public Cart(int quantity, Long userId){
        this.quantity=quantity;
        this.userId = userId;
    }

    public static Cart createCart(Long userId) {
        return Cart.builder()
                .quantity(0)
                .userId(userId)
                .build();
    }

    public void updateCart(int quantity) {
        this.quantity+=quantity;
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

    public void removeCartItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
    }
}
