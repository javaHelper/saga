package com.example.demo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.PurchaseOrder;


public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, ObjectId> {
    @Query("{ 'user.userId': ?0 }")
    List<PurchaseOrder> findByUserId(Long userId);
}
