package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Delivery;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery, String> {
}