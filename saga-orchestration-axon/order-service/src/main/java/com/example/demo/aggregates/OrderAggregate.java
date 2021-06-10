package com.example.demo.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.demo.commands.CreateOrderCommand;
import com.example.demo.commands.UpdateOrderStatusCommand;
import com.example.demo.events.OrderCreatedEvent;
import com.example.demo.events.OrderUpdatedEvent;

import lombok.Data;

@Aggregate
@Data
public class OrderAggregate {

	@AggregateIdentifier
    private String orderId;

    private ItemType itemType;

    private BigDecimal price;

    private String currency;

    private OrderStatus orderStatus;

    public OrderAggregate() {
    }
    
    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
    	OrderCreatedEvent event = OrderCreatedEvent.builder()
    		.orderId(command.orderId)
    		.itemType(command.itemType)
    		.price(command.price)
    		.currency(command.currency)
    		.orderStatus(command.orderStatus)
    		.build();
    	AggregateLifecycle.apply(event);
    }
    
    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
    	this.orderId = event.orderId;
    	this.itemType = ItemType.valueOf(event.itemType);
    	this.price = event.price;
    	this.currency = event.currency;
    	this.orderStatus = OrderStatus.valueOf(event.orderStatus);
    }
    
    @CommandHandler
    public void on(UpdateOrderStatusCommand command) {
    	OrderUpdatedEvent event = OrderUpdatedEvent.builder()
    			.orderId(command.orderId)
    			.orderStatus(command.orderStatus)
    			.build();
    	AggregateLifecycle.apply(event);
    }
}
