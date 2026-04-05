package com.example.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Column(name = "item_id")
    private Long itemId;

    @Builder
    public CartItem(int quantity, Long itemId){
        this.quantity=quantity;
        this.itemId=itemId;
    }

    public static CartItem create(int quantity, Long itemId) {
        return CartItem.builder()
                .itemId(itemId)
                .quantity(quantity)
                .build();
    }

    public void updateCartItem(int quantity) {
        this.quantity=quantity;
    }

    public void addCount(int count) {
        this.quantity=count;
    }
}
