package com.example.core.events;

import com.example.core.data.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
    private String orderId;
    private OrderStatus orderStatus = OrderStatus.APPROVED;
}
