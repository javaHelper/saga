package com.example.paymentservice.command;

import com.example.core.commands.ProcessPaymentCommand;
import com.example.core.events.PaymentProcessedEvent;

import lombok.Data;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Data
@Aggregate
public class PaymentAggregate {
	@AggregateIdentifier
	private String paymentId;

	private String orderId;

	public PaymentAggregate() { }

	@CommandHandler
	public PaymentAggregate(ProcessPaymentCommand processPaymentCommand){
		// Check Payment Details is present
		if(processPaymentCommand.getPaymentDetails() == null) {
			throw new IllegalArgumentException("Missing payment details");
		}

		// Check OrderId is present
		if(processPaymentCommand.getOrderId() == null) {
			throw new IllegalArgumentException("Missing orderId");
		}

		// Check if PaymentId is present
		if(processPaymentCommand.getPaymentId() == null) {
			throw new IllegalArgumentException("Missing paymentId");
		}
		PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(processPaymentCommand.getOrderId(),
				processPaymentCommand.getPaymentId());

		AggregateLifecycle.apply(paymentProcessedEvent);
	}

	@EventSourcingHandler
	protected void on(PaymentProcessedEvent paymentProcessedEvent){
		this.paymentId = paymentProcessedEvent.getPaymentId();
		this.orderId = paymentProcessedEvent.getOrderId();
	}
}
