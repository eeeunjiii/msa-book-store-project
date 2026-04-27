package com.example.response;

import com.example.dto.DeliveryInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteOrderResponse {
    private Long orderId;
    private LocalDateTime orderDate;

    private List<CompleteOrderItemResponse> orderItems;

    private DeliveryInfo deliveryInfo;
    private int totalPrice;

    public CompleteOrderResponse(Long orderId, LocalDateTime orderDate, DeliveryInfo deliveryInfo) {
        this.orderId=orderId;
        this.orderDate=orderDate;
        this.deliveryInfo=deliveryInfo;
    }

    static public CompleteOrderResponse of(Long orderId, LocalDateTime orderDate,
                                           List<CompleteOrderItemResponse> orderItems,
                                           DeliveryInfo deliveryInfo, int totalPrice) {
        return new CompleteOrderResponse(orderId, orderDate, orderItems, deliveryInfo, totalPrice);
    }

    static public CompleteOrderResponse of(Long orderId, LocalDateTime orderDate, DeliveryInfo deliveryInfo) {
        return new CompleteOrderResponse(orderId, orderDate, deliveryInfo);
    }
}
