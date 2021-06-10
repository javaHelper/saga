package com.example.demo.events;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public class OrderCreatedEvent {
    public final String orderId;
    public final String itemType;
    public final BigDecimal price;
    public final String currency;
    public final String orderStatus;

    public OrderCreatedEvent(String orderId, String itemType, BigDecimal price, String currency, String orderStatus) {
        this.orderId = orderId;
        this.itemType = itemType;
        this.price = price;
        this.currency = currency;
        this.orderStatus = orderStatus;
    }
}