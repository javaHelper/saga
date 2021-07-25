package com.example.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.example.core.commands.CancelProductReservationCommand;
import com.example.core.commands.ReserveProductCommand;
import com.example.core.commands.events.ProductReservedEvents;
import com.example.core.event.ProductCreatedEvent;
import com.example.core.events.ProductReservationCancelledEvent;

import lombok.Data;


@Data
@Aggregate
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        // Perform Validations
        if (command.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price can not be less than or equal to zero");
        }

        if (command.getTitle() == null) {
            throw new IllegalArgumentException("Title can not be empty");
        }

        ProductCreatedEvent event = new ProductCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) {
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number of items in stock !");
        }

        ProductReservedEvents productReservedEvents = ProductReservedEvents.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvents);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.price = event.getPrice();
        this.title = event.getTitle();
        this.quantity = event.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvents productReservedEvents) {
        this.quantity -= productReservedEvents.getQuantity();
    }
    
    
    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
    	ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
    			.orderId(cancelProductReservationCommand.getOrderId())
    			.productId(cancelProductReservationCommand.getProductId())
    			.quantity(cancelProductReservationCommand.getQuantity())
    			.reason(cancelProductReservationCommand.getReason())
    			.userId(cancelProductReservationCommand.getUserId())
    			.build();
    	AggregateLifecycle.apply(productReservationCancelledEvent);
    }
    
    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
    	this.quantity += productReservationCancelledEvent.getQuantity();
    }
}
