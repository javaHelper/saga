package com.example.demo.saga;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.aggregates.OrderStatus;
import com.example.demo.commands.CreateInvoiceCommand;
import com.example.demo.commands.CreateShippingCommand;
import com.example.demo.commands.UpdateOrderStatusCommand;
import com.example.demo.events.InvoiceCreatedEvent;
import com.example.demo.events.OrderCreatedEvent;
import com.example.demo.events.OrderShippedEvent;
import com.example.demo.events.OrderUpdatedEvent;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderManagementSaga {

	@Autowired
	private CommandGateway commandGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		log.info("*** Saga Invoked ****");
		String paymentId = UUID.randomUUID().toString();

		//Associate Saga
		SagaLifecycle.associateWith("paymentId", paymentId);
		log.info("***   OrderId = {}    *****", orderCreatedEvent.orderId);

		commandGateway.send(CreateInvoiceCommand.builder().paymentId(paymentId).orderId(orderCreatedEvent.orderId).build());
	}

	@SagaEventHandler(associationProperty = "paymentId")
	public void handle(InvoiceCreatedEvent invoiceCreatedEvent) {
		log.info("*** Saga Continued ***");
		String shippingId = UUID.randomUUID().toString();

		//associate Saga with shipping
		SagaLifecycle.associateWith("shipping", shippingId);

		//send the create shipping command
		commandGateway.send(CreateShippingCommand.builder()
				.shippingId(shippingId)
				.orderId(invoiceCreatedEvent.orderId)
				.paymentId(invoiceCreatedEvent.paymentId)
				.build());
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderShippedEvent event) {
		UpdateOrderStatusCommand command = UpdateOrderStatusCommand.builder()
				.orderId(event.orderId)
				.orderStatus(String.valueOf(OrderStatus.SHIPPED))
				.build();
		
		commandGateway.send(command);
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderUpdatedEvent event) {
		SagaLifecycle.end();
	}
}
