package com.example.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderCount;
    private int orderPrice; // 주문 시점 가격

    @Column(name = "item_id")
    private Long itemId;

    @Builder
    public OrderItem(Long id, int orderCount, int orderPrice, Long itemId){
        this.id=id;
        this.orderCount=orderCount;
        this.orderPrice=orderPrice;
        this.itemId=itemId;
    }

    public static OrderItem create(int quantity, Long itemId, int orderPrice) {
        return OrderItem.builder()
                .orderCount(quantity)
                .orderPrice(orderPrice)
                .itemId(itemId)
                .build();
    }

    public int getTotalPrice() {
        return this.orderPrice*this.orderCount;
    }
}
