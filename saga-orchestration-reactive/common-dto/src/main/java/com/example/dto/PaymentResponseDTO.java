package com.example.dto;

import java.util.UUID;

import com.example.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private Integer userId;
    private UUID orderId;
    private Double amount;
    private PaymentStatus status;
}
