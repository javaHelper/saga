package com.example.demo.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.example.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrder {

	@Id
	private UUID id;
	private Integer userId;
	private Integer productId;
	private Double price;
	private OrderStatus status;
}