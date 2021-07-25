package com.example.command;

import com.example.command.commands.ApproveOrderCommand;
import com.example.command.commands.CreateOrderCommand;
import com.example.command.commands.RejectOrderCommand;
import com.example.core.data.OrderStatus;
import com.example.core.events.OrderApprovedEvent;
import com.example.core.events.OrderCreatedEvent;
import com.example.core.events.OrderRejectedEvent;

import lombok.Data;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

/**
 * Annotation that informs Axon's auto configurer for Spring that a given Component is an aggregate instance
 */

@Data
@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command){
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(command, orderCreatedEvent);

        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) throws Exception {
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.addressId = event.getAddressId();
        this.quantity = event.getQuantity();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand){
        // Create and Publish OrderApprovedEvent
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    protected void on(OrderApprovedEvent orderApprovedEvent){
        this.orderStatus = orderApprovedEvent.getOrderStatus();
    }
    
    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
    	OrderRejectedEvent orderRejectEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(), rejectOrderCommand.getReason());
    	AggregateLifecycle.apply(orderRejectEvent);
    }
    
    
    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
    	this.orderStatus = orderRejectedEvent.getOrderStatus();
    }
}
