package com.example.demo.commands;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;

@Builder
public class UpdateOrderStatusCommand {

    @TargetAggregateIdentifier
    public final String orderId;

    public final String orderStatus;

    public UpdateOrderStatusCommand(String orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}