package com.example.demo.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class CreateOrderCommand {

	@TargetAggregateIdentifier
	public final String orderId;

	public final String itemType;

	public final BigDecimal price;

	public final String currency;

	public final String orderStatus;

	public CreateOrderCommand(String orderId, String itemType, BigDecimal price, String currency, String orderStatus) {
		this.orderId = orderId;
		this.itemType = itemType;
		this.price = price;
		this.currency = currency;
		this.orderStatus = orderStatus;
	}
}