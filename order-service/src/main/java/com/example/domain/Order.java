package com.example.domain;

import com.example.constant.DeliveryStatus;
import com.example.constant.Payment;
import com.example.request.OrderInfoRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalPrice;
    private DeliveryStatus deliveryStatus;

    private LocalDateTime orderDate;

    @PrePersist
    public void createAt() {
        this.orderDate =LocalDateTime.now();
    }

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems=new ArrayList<>();

    private String customer;
    private String memo;
    private String phoneNumber;
    private String destination;
    private int point;
    private Payment payment;

    @Builder
    public Order(Long id, int totalPrice, DeliveryStatus deliveryStatus,
                 Long userId, String customer, String memo, String phoneNumber,
                 String destination, int point, Payment payment){
        this.id=id;
        this.totalPrice =totalPrice;
        this.deliveryStatus = deliveryStatus;
        this.userId=userId;
        this.customer=customer;
        this.memo=memo;
        this.phoneNumber=phoneNumber;
        this.destination=destination;
        this.point=point;
        this.payment=payment;
    }

    public static Order create(Long userId, OrderInfoRequest infoForm) {
        return Order.builder()
                .userId(userId)
                .customer(infoForm.getCustomer())
                .memo(infoForm.getMemo())
                .phoneNumber(infoForm.getPhoneNumber())
                .destination(infoForm.getDestination())
                .point(infoForm.getPoint())
                .payment(infoForm.getPayment())
                .deliveryStatus(DeliveryStatus.PREPARING)
                .build();
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        this.totalPrice+=orderItem.getTotalPrice();
    }
}