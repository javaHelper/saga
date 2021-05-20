package com.example.demo.servce;

import java.util.List;

import com.example.demo.model.PurchaseOrder;

public interface PurchaseOrderService {
	List<PurchaseOrder> getPurchaseOrders();

	void createPurchaseOrder(PurchaseOrder purchaseOrder);
}