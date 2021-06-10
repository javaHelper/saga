package com.example.demo.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;

@Builder
public class CreateShippingCommand {

	@TargetAggregateIdentifier
	public final String shippingId;

	public final String orderId;

	public final String paymentId;

	public CreateShippingCommand(String shippingId, String orderId, String paymentId) {
		this.shippingId = shippingId;
		this.orderId = orderId;
		this.paymentId = paymentId;
	}
}