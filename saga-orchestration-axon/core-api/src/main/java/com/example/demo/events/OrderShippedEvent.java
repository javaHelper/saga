package com.example.demo.events;

import lombok.Builder;

@Builder
public class OrderShippedEvent {
	public final String shippingId;
	public final String orderId;
	public final String paymentId;

	public OrderShippedEvent(String shippingId, String orderId, String paymentId) {
		this.shippingId = shippingId;
		this.orderId = orderId;
		this.paymentId = paymentId;
	}
}