package com.example.core.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveProductCommand {
    @TargetAggregateIdentifier
    private String productId;
    private int quantity;
    private String orderId;
    private String userId;
}
