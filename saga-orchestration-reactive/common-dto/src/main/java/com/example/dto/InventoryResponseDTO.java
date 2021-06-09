package com.example.dto;

import java.util.UUID;

import com.example.enums.InventoryStatus;

import lombok.Data;

@Data
public class InventoryResponseDTO {

    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private InventoryStatus status;

}
