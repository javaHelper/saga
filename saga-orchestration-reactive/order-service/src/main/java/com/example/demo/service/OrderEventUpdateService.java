package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.PurchaseOrderRepository;
import com.example.dto.OrchestratorResponseDTO;

@Service
public class OrderEventUpdateService {

    @Autowired
    private PurchaseOrderRepository repository;

    @Transactional
    public void updateOrder(final OrchestratorResponseDTO responseDTO){
        this.repository.findById(responseDTO.getOrderId())
                .ifPresent(po -> {
                    po.setStatus(responseDTO.getStatus());
                    this.repository.save(po);
                });
    }

}
