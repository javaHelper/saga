package com.example.demo.event;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private OrderRepository orderRepository;

	private static final String TOPIC_ORDER_CALLBACK = "order-service-callback";

	@KafkaListener(topics = TOPIC_ORDER_CALLBACK)
	public void callback(String message) {
		log.info("###  OrderConsumer | callback | orderservicecallback ###");
		try {
			OrderDTO orderDTO = mapper.readValue(message, OrderDTO.class);
			Optional<Order> orderOptional = orderRepository.findById(orderDTO.getId());
			if (orderOptional.isPresent()) {
				Order order = orderOptional.get();
				order.setStatusDescription(orderDTO.getStatusDescription());
				order.setOrderStatus(orderDTO.getOrderStatus());
				orderRepository.save(order);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
