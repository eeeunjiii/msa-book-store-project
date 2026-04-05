package com.example.request;

import com.example.constant.Payment;
import com.example.constant.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderInfoRequest { // 상품 외 주문과 관련된 주문 정보

    @NotBlank
    private String customer;

    private String memo;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String destination;

    private int point;

    @ValidEnum(enumClass= Payment.class)
    private Payment payment;
}
