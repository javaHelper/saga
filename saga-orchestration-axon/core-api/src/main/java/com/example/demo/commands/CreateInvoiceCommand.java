package com.example.demo.commands;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;

@Builder
public class CreateInvoiceCommand{

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;

    public CreateInvoiceCommand(String paymentId, String orderId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
    }
}