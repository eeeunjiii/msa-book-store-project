package com.example.dto;

import com.example.constant.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {
    private String phoneNumber;
    private String destination;
    private String customer;
    private String memo;
    private DeliveryStatus deliveryStatus;

    static public DeliveryInfo of(String phoneNumber, String destination, String customer,
                                  String memo, DeliveryStatus deliveryStatus) {
        return new DeliveryInfo(phoneNumber, destination, customer, memo, deliveryStatus);
    }
}
