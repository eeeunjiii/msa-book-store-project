package com.example.constant;

public enum Payment {
    DEPOSIT("무통장 입금"), CREDIT_CART("신용 카드"), DEBIT_CART("체크 카드");

    private final String description;

    Payment(String description) { this.description=description; }

    public String getDescription() { return description; }
}
