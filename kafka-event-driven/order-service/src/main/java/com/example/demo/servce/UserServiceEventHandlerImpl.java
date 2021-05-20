package com.example.demo.servce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.model.User;
import com.example.demo.repository.PurchaseOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j	
public class UserServiceEventHandlerImpl implements UserServiceEventHandler {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
	
	@Override
	@Transactional
	public void updateUser(User user) {
		List<PurchaseOrder> userOrders = purchaseOrderRepository.findByUserId(user.getId());
		userOrders.forEach(uo -> uo.setUser(user));
		purchaseOrderRepository.saveAll(userOrders);
	}
	
	
	// Annotation that marks a method to be the target of a Kafka message listener on thespecified topics. 
	@KafkaListener(topics = "t_user_service_event")
	public void consumeData(String userjson) {
		try {
			User user = objectMapper().readValue(userjson, User.class);
			this.updateUser(user);
		} catch (Exception e) {
			log.error("UserServiceEventHandlerImpl | consumeData | Error {}", e.getMessage());
		}
	}
}
