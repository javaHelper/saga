package com.example.demo.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.demo.commands.CreateInvoiceCommand;
import com.example.demo.events.InvoiceCreatedEvent;

import lombok.Data;

@Aggregate
@Data
public class InvoiceAggregate {

	@AggregateIdentifier
    private String paymentId;

    private String orderId;

    private InvoiceStatus invoiceStatus;

    public InvoiceAggregate() {
    }
    
    @CommandHandler
    public InvoiceAggregate(CreateInvoiceCommand command) {
    	AggregateLifecycle.apply(new InvoiceCreatedEvent(command.paymentId, command.orderId));
    }
    
    @EventSourcingHandler
    public void on(InvoiceCreatedEvent event) {
    	this.paymentId = event.paymentId;
    	this.orderId = event.orderId;
    	this.invoiceStatus = InvoiceStatus.PAID;
    }
}
