package com.example.demo.servce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Override
	public List<PurchaseOrder> getPurchaseOrders() {
		return this.purchaseOrderRepository.findAll();
	}

	@Override
	public void createPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrderRepository.save(purchaseOrder);
	}

}