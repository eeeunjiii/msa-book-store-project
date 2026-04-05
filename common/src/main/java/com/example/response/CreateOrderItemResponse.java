package com.example.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderItemResponse {
    private int quantity;
    private Long orderId;
    private Long itemId;
    private String title;

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null || getClass()!=obj.getClass()) return false;
        CreateOrderItemResponse that=(CreateOrderItemResponse) obj;
        return itemId.equals(that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
