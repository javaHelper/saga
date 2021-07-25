package com.example.paymentservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository <PaymentEntity, String>{
    
}