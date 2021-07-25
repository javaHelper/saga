package com.example.core.events;

import com.example.core.data.OrderStatus;

import lombok.Value;

@Value
public class OrderRejectedEvent {

	private String orderId;
	private String reason;
	private OrderStatus orderStatus = OrderStatus.REJECTED;
}
