package com.example.demo.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServicePublisher {

	private static final Logger logger = LoggerFactory.getLogger(OrderServicePublisher.class);

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	private final static String TOPIC = "order-service";

	@Autowired
	private ObjectMapper objectMapper;

	public void sendOrder(OrderDTO orderDTO) {
		try {
			kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(orderDTO));
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
	}
}