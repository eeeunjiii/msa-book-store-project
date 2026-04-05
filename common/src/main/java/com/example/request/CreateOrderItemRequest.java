package com.example.request;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderItemRequest {
    private int quantity;
    private int orderPrice;
    private Long orderId;
    private Long itemId;
    private String title;

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null || getClass()!=obj.getClass()) return false;
        CreateOrderItemRequest that=(CreateOrderItemRequest) obj;
        return itemId.equals(that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
