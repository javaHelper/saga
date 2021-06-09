package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PurchaseOrder;
import com.example.demo.service.OrderService;
import com.example.dto.OrderRequestDTO;
import com.example.dto.OrderResponseDTO;

@RestController
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrderService service;

	@PostMapping("/create")
	public PurchaseOrder createOrder(@RequestBody OrderRequestDTO requestDTO) {
		requestDTO.setOrderId(UUID.randomUUID());
		return this.service.createOrder(requestDTO);
	}

	@GetMapping("/all")
	public List<OrderResponseDTO> getOrders() {
		return this.service.getAll();
	}

}