package com.example.demo.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.aggregates.OrderStatus;
import com.example.demo.commands.CreateOrderCommand;
import com.example.demo.dto.command.OrderCreateDTO;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {

	@Autowired
	private CommandGateway commandGateway;
	
	@Override
	public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO) {
		CreateOrderCommand command = CreateOrderCommand.builder()
				.orderId(UUID.randomUUID().toString())
				.itemType(orderCreateDTO.getItemType())
				.price(orderCreateDTO.getPrice())
				.currency(orderCreateDTO.getCurrency())
				.orderStatus(String.valueOf(OrderStatus.CREATED))
				.build();
		return commandGateway.send(command);
	}
}
