package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.servce.PurchaseOrderService;

@RestController
@RequestMapping("/order-service")
public class OrderController {
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@GetMapping("/all")
	public List<PurchaseOrder> getAllOrders() {
		return this.purchaseOrderService.getPurchaseOrders();
	}

	@PostMapping("/create")
	public void createOrder(@RequestBody PurchaseOrder purchaseOrder) {
		this.purchaseOrderService.createPurchaseOrder(purchaseOrder);
	}
}
