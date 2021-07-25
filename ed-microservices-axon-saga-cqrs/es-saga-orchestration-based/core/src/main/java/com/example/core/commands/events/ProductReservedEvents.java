package com.example.core.commands.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservedEvents {
    private String productId;
    private int quantity;
    private String orderId;
    private String userId;
}
