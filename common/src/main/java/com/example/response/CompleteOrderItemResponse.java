package com.example.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteOrderItemResponse { // completeOrderForm에서 item과 관련된 상세 정보를 제공하기 위해 사용
    private String title;
    private int orderCount;
    private Integer price;
    private int totalPrice;

    static public CompleteOrderItemResponse of(String title, int orderCount, Integer price, int totalPrice) {
        return new CompleteOrderItemResponse(title, orderCount, price, totalPrice);
    }
}
