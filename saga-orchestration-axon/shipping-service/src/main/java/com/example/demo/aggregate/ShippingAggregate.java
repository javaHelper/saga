package com.example.demo.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.demo.commands.CreateShippingCommand;
import com.example.demo.events.OrderShippedEvent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Aggregate
@Slf4j
public class ShippingAggregate {

	@AggregateIdentifier
    private String shippingId;

    private String orderId;

    private String paymentId;
    
    public ShippingAggregate() {}
    
    @CommandHandler
    public ShippingAggregate(CreateShippingCommand command) {
    	log.info("### shipping-service |  CreateShippingCommand  ###");
    	OrderShippedEvent event = OrderShippedEvent.builder().shippingId(command.shippingId).orderId(command.orderId).paymentId(command.paymentId).build();
    	
    	AggregateLifecycle.apply(event);
    }
    
    @EventSourcingHandler
    public void on(OrderShippedEvent event) {
    	log.info("### shipping-service |  OrderShippedEvent  ###");
    	this.shippingId = event.shippingId;
    	this.orderId = event.orderId;
    }
}
